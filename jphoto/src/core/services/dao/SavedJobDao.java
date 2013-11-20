package core.services.dao;

import admin.jobs.general.SavedJob;
import admin.jobs.enums.SavedJobType;
import core.enums.SavedJobParameterKey;
import core.general.base.CommonProperty;

import java.util.List;
import java.util.Map;

public interface SavedJobDao extends BaseEntityDao<SavedJob> {

	List<Integer> loadIdsAll();

	boolean activate( final int savedJobId );

	boolean deactivate( final int savedJobId );

	SavedJob loadByName( final String jobName );

	boolean saveParameter( final int savedJobId, final SavedJobParameterKey key, final String value );

	boolean deleteJobParameters( final int savedJobId );

	CommonProperty getJobParameter( final int savedJobId, final SavedJobParameterKey key );

	Map<SavedJobParameterKey, CommonProperty> getJobParameters( final int savedJobId );

	List<SavedJob> getJobsByType( final SavedJobType savedJobType );
}
