package core.services.job;

import admin.controllers.jobs.edit.AbstractJobController;
import admin.jobs.entries.AbstractJob;
import admin.services.jobs.JobExecutionService;
import admin.jobs.general.SavedJob;
import admin.jobs.enums.SavedJobType;
import core.enums.SavedJobParameterKey;
import core.general.base.CommonProperty;
import core.log.LogHelper;
import core.services.dao.SavedJobDao;
import core.services.dao.SchedulerTasksDao;
import core.services.utils.DateUtilsService;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

public class SavedJobServiceImpl implements SavedJobService {

	@Autowired
	private SavedJobDao savedJobDao;

	@Autowired
	private SchedulerTasksDao schedulerTasksDao;

	@Autowired
	private JobExecutionService jobExecutionService;

	@Autowired
	private DateUtilsService dateUtilsService;

	private final LogHelper log = new LogHelper( SavedJobServiceImpl.class );

	@Override
	public boolean save( final SavedJob entry ) {
		boolean isSuccessful = savedJobDao.saveToDB( entry );

		if ( isSuccessful ) {
			savedJobDao.deleteJobParameters( entry.getId() );

			final Map<SavedJobParameterKey, CommonProperty> parametersMap = entry.getJob().getParametersMap();
			for ( final SavedJobParameterKey key : parametersMap.keySet() ) {
				isSuccessful &= savedJobDao.saveParameter( entry.getId(), key, parametersMap.get( key ).getValue() );
			}
		}

		return isSuccessful;
	}

	@Override
	public List<SavedJob> loadAll() {
		final List<Integer> savedJobIds = savedJobDao.loadIdsAll();

		final List<SavedJob> savedJobs = newArrayList();

		for ( final int savedJobId : savedJobIds ) {
			savedJobs.add( load( savedJobId ) );
		}

		return savedJobs;
	}

	@Override
	public SavedJob load( final int savedJobId ) {
		final SavedJob savedJob = savedJobDao.load( savedJobId );

		final SavedJobType jobType = savedJob.getJobType();

		final AbstractJob job = AbstractJobController.createInstance( jobType );
		job.setSavedJobId( savedJobId );

		jobExecutionService.initJobServices( job );

		final Map<SavedJobParameterKey, CommonProperty> savedJobParameterMap = getSavedJobParametersMap( savedJobId );
		job.initJobParameters( savedJobParameterMap );

		savedJob.setJob( job );

		return savedJob;
	}

	@Override
	public Map<SavedJobParameterKey, CommonProperty> getSavedJobParametersMap( final int savedJobId ) {
		final Map<SavedJobParameterKey, CommonProperty> jobParameters = savedJobDao.getJobParameters( savedJobId );

		for ( final SavedJobParameterKey savedJobParameterKey : SavedJobParameterKey.values() ) {
			if ( ! jobParameters.containsKey( savedJobParameterKey ) ) {
				jobParameters.put( savedJobParameterKey, CommonProperty.getEmptyProperty( savedJobParameterKey.getId() ) );
			}
		}

		return jobParameters;
	}

	@Override
	public CommonProperty getJobParameter( final int savedJobId, final SavedJobParameterKey key ) {
		return savedJobDao.getJobParameter( savedJobId, key );
	}

	@Override
	public List<SavedJob> loadAllActive() {
		final List<SavedJob> savedJobs = loadAll();

		CollectionUtils.filter( savedJobs, new Predicate<SavedJob>() {
			@Override
			public boolean evaluate( final SavedJob savedJob ) {
				return savedJob.isActive();
			}
		} );

		return savedJobs;
	}

	@Override
	public boolean delete( final int entryId ) {
		savedJobDao.deleteJobParameters( entryId );
		return savedJobDao.delete( entryId );
	}

	@Override
	public boolean activate( final int savedJobId ) {
		return savedJobDao.activate( savedJobId );
	}

	@Override
	public boolean deactivate( final int savedJobId ) {
		return savedJobDao.deactivate( savedJobId );
	}

	@Override
	public SavedJob loadByName( final String jobName ) {
		return savedJobDao.loadByName( jobName );
	}

	@Override
	public List<SavedJob> getJobsByType( final SavedJobType savedJobType ) {
		return savedJobDao.getJobsByType( savedJobType );
	}

	@Override
	public Set<Integer> getNotDeletableJobIds() {
		final List<SavedJob> allSavedJobs = loadAll();

		final Set<Integer> notDeletableJobIds = newHashSet();
		for ( final SavedJob savedJob : allSavedJobs ) {
			if ( schedulerTasksDao.isJobScheduled( savedJob.getId() ) ) {
				notDeletableJobIds.add( savedJob.getId() );
			}
		}

		final List<SavedJob> chainJobs = getJobsByType( SavedJobType.JOB_CHAIN );
		for ( final SavedJob chainJob : chainJobs ) {
			final CommonProperty jobChainProperty = getJobParameter( chainJob.getId(), SavedJobParameterKey.PARAM_SAVED_JOB_CHAIN );
			final List<Integer> jobChainIds = jobChainProperty.getValueListInt();
			for ( final SavedJob savedJob : allSavedJobs ) {
				for ( final Integer jobChainId : jobChainIds ) {
					if ( jobChainId == savedJob.getId() ) {
						notDeletableJobIds.add( jobChainId );
					}
				}
			}
		}
		return notDeletableJobIds;
	}

	@Override
	public boolean isJobCanBeDeleted( final int savedJobId ) {
		return ! getNotDeletableJobIds().contains( savedJobId );
	}

	@Override
	public boolean exists( final int entryId ) {
		return savedJobDao.exists( entryId );
	}

	@Override
	public boolean exists( final SavedJob entry ) {
		return savedJobDao.exists( entry );
	}
}
