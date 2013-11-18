package core.services.job;

import admin.jobs.general.SavedJob;
import admin.jobs.enums.SavedJobType;
import core.enums.SavedJobParameterKey;
import core.general.base.CommonProperty;
import core.interfaces.BaseEntityService;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SavedJobService extends BaseEntityService<SavedJob> {

	List<SavedJob> loadAll();

	boolean activate( final int savedJobId );

	boolean deactivate( final int savedJobId );

	SavedJob loadByName( final String jobName );

	CommonProperty getJobParameter( final int savedJobId, final SavedJobParameterKey key );

	List<SavedJob> loadAllActive();

	Map<SavedJobParameterKey, CommonProperty> getSavedJobParametersMap( final int savedJobId );

	List<SavedJob> getJobsByType( final SavedJobType savedJobType );

	Set<Integer> getNotDeletableJobIds();

	boolean isJobCanBeDeleted( final int savedJobId );
}
