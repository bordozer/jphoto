package admin.services.jobs;

import admin.jobs.enums.JobExecutionStatus;
import admin.jobs.enums.SavedJobType;
import admin.jobs.general.SavedJob;
import core.enums.SavedJobParameterKey;
import core.general.base.CommonProperty;
import core.services.dao.BaseEntityDao;
import core.services.dao.BaseEntityDaoImpl;
import core.services.dao.SavedJobDao;
import core.services.dao.mappers.IdsRowMapper;
import core.services.utils.DateUtilsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import utils.ListUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Maps.newLinkedHashMap;

public class JobExecutionHistoryDaoImpl extends BaseEntityDaoImpl<JobExecutionHistoryEntry> implements JobExecutionHistoryDao {

	public static final String TABLE_JOB_EXECUTION_HISTORY = "jobExecutionHistory";

	public static final String TABLE_JOB_DONE_COL_JOB_TYPE_ID = "jobTypeId";
	public static final String TABLE_JOB_DONE_COL_PARAMETERS_DESCRIPTION = "parametersDescription";
	public static final String TABLE_JOB_DONE_COL_START_TIME = "startTime";
	public static final String TABLE_JOB_DONE_COL_END_TIME = "endTime";
	public static final String TABLE_JOB_DONE_COL_JOB_STATUS_ID = "jobStatusId";
	public static final String TABLE_JOB_DONE_COL_JOB_MESSAGE = "jobMessage";
	public static final String TABLE_JOB_DONE_COL_SAVED_JOB_ID = "savedJobId";
	public static final String TABLE_JOB_DONE_COL_CURRENT_JOB_STEP = "currentJobStep";
	public static final String TABLE_JOB_DONE_COL_TOTAL_JOB_STEPS = "totalJobSteps";
	public static final String TABLE_JOB_DONE_COL_SCHEDULER_TASK_ID = "schedulerTaskId";

	public static final String TABLE_JOB_EXECUTION_HISTORY_PARAMETERS = "jobExecutionHistoryParameters";
	public static final String TABLE_JOB_EXECUTION_HISTORY_PARAMETERS_COLUMN_JOB_ID = "jobExecutionHistoryId";
	public static final String TABLE_JOB_EXECUTION_HISTORY_PARAMETERS_COLUMN_KEY = "pKey";
	public static final String TABLE_JOB_EXECUTION_HISTORY_PARAMETERS_COLUMN_VALUE = "pValue";

	private static final Map<Integer, String> fields = newLinkedHashMap();
	private static final Map<Integer, String> updatableFields = newLinkedHashMap();

	@Autowired
	private SavedJobDao savedJobDao;

	@Autowired
	private DateUtilsService dateUtilsService;

	static {
		fields.put( 1, TABLE_JOB_DONE_COL_JOB_TYPE_ID );
		fields.put( 2, TABLE_JOB_DONE_COL_PARAMETERS_DESCRIPTION );
		fields.put( 3, TABLE_JOB_DONE_COL_START_TIME );
		fields.put( 4, TABLE_JOB_DONE_COL_END_TIME );
		fields.put( 5, TABLE_JOB_DONE_COL_JOB_STATUS_ID );
		fields.put( 6, TABLE_JOB_DONE_COL_JOB_MESSAGE );
		fields.put( 7, TABLE_JOB_DONE_COL_SAVED_JOB_ID );
		fields.put( 8, TABLE_JOB_DONE_COL_CURRENT_JOB_STEP );
		fields.put( 9, TABLE_JOB_DONE_COL_TOTAL_JOB_STEPS );
		fields.put( 10, TABLE_JOB_DONE_COL_SCHEDULER_TASK_ID );
	}

	static {
		updatableFields.put( 1, TABLE_JOB_DONE_COL_END_TIME );
		updatableFields.put( 5, TABLE_JOB_DONE_COL_JOB_STATUS_ID );
		updatableFields.put( 6, TABLE_JOB_DONE_COL_JOB_MESSAGE );
		updatableFields.put( 7, TABLE_JOB_DONE_COL_SAVED_JOB_ID );
	}

	@Override
	protected String getTableName() {
		return TABLE_JOB_EXECUTION_HISTORY;
	}

	@Override
	public List<JobExecutionHistoryEntry> getJobExecutionHistoryEntries() {
		final String sql = String.format( "SELECT * FROM %s ORDER BY %s DESC;", TABLE_JOB_EXECUTION_HISTORY, ENTITY_ID );
		return jdbcTemplate.query( sql, new MapSqlParameterSource(  ), new JobExecutionHistoryMapper() );
	}

	@Override
	public void setJobExecutionHistoryEntryStatus( final int jobId, final String message, final JobExecutionStatus status ) {
		setJobStatusWithMessage( jobId, message, status );
	}

	@Override
	public void updateCurrentJobStep( final int jobExecutionHistoryId, final int currentJobStep ) {
		updateJobStep( jobExecutionHistoryId, currentJobStep, TABLE_JOB_DONE_COL_CURRENT_JOB_STEP );
	}

	@Override
	public void updateTotalJobSteps( final int jobExecutionHistoryId, final int totalJobSteps ) {
		updateJobStep( jobExecutionHistoryId, totalJobSteps, TABLE_JOB_DONE_COL_TOTAL_JOB_STEPS );
	}

	@Override
	public boolean saveParameter( final int jobExecutionHistoryId, final SavedJobParameterKey key, final String value ) {
		final String sql = String.format( "INSERT INTO %s ( %s, %s, %s) VALUES ( :jobExecutionHistoryId, :key, :value )"
			, TABLE_JOB_EXECUTION_HISTORY_PARAMETERS, TABLE_JOB_EXECUTION_HISTORY_PARAMETERS_COLUMN_JOB_ID, TABLE_JOB_EXECUTION_HISTORY_PARAMETERS_COLUMN_KEY, TABLE_JOB_EXECUTION_HISTORY_PARAMETERS_COLUMN_VALUE );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "jobExecutionHistoryId", jobExecutionHistoryId );
		paramSource.addValue( "key", key.getId() );
		paramSource.addValue( "value", value );

		return jdbcTemplate.update( sql, paramSource ) > 0;
	}

	@Override
	public Map<SavedJobParameterKey, CommonProperty> getJobParameters( final int jobExecutionHistoryId ) {
		final String sql = String.format( "SELECT * FROM %s WHERE %s = :jobExecutionHistoryId"
			, TABLE_JOB_EXECUTION_HISTORY_PARAMETERS, TABLE_JOB_EXECUTION_HISTORY_PARAMETERS_COLUMN_JOB_ID );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "jobExecutionHistoryId", jobExecutionHistoryId );

		final List<CommonProperty> jobProperties = jdbcTemplate.query( sql, paramSource, new JobExecutionHistoryParameterMapper() );
		final Map<SavedJobParameterKey, CommonProperty> result = newHashMap();
		for ( final CommonProperty property : jobProperties ) {
			result.put( SavedJobParameterKey.getById( property.getKey() ), property );
		}

		return result;
	}

	@Override
	public boolean deleteJobParameters( final int jobExecutionHistoryId ) {
		final String sql = String.format( "DELETE FROM %s WHERE %s=:jobExecutionHistoryId", TABLE_JOB_EXECUTION_HISTORY_PARAMETERS, TABLE_JOB_EXECUTION_HISTORY_PARAMETERS_COLUMN_JOB_ID );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "jobExecutionHistoryId", jobExecutionHistoryId );

		return jdbcTemplate.update( sql, paramSource ) > 0;
	}

	@Override
	public void deleteOldHistoryEntries( final int leaveQty ) {

		final String sql = String.format( "SELECT %1$s FROM %2$s WHERE %4$s=:jobStatusId ORDER BY %1$s DESC LIMIT %3$d"
			, ENTITY_ID, TABLE_JOB_EXECUTION_HISTORY, leaveQty, TABLE_JOB_DONE_COL_JOB_STATUS_ID );

		final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue( "jobStatusId", JobExecutionStatus.DONE.getId() );

		final List<Map<String, Object>> list = jdbcTemplate.queryForList( sql, parameterSource );

		if ( list.size() == 0 ) {
			return;
		}

		final Map<String, Object> map = list.get( list.size() - 1 );
		final int id = ( Integer ) map.get( "id" );

		final String sql_del_properties = String.format( "DELETE FROM %s WHERE %s < %s;"
			, TABLE_JOB_EXECUTION_HISTORY_PARAMETERS, TABLE_JOB_EXECUTION_HISTORY_PARAMETERS_COLUMN_JOB_ID, id );
		jdbcTemplate.update( sql_del_properties, new MapSqlParameterSource() );

		final String sql_del = String.format( "DELETE FROM %s WHERE %s < %s;", TABLE_JOB_EXECUTION_HISTORY, ENTITY_ID, id );
		jdbcTemplate.update( sql_del, new MapSqlParameterSource() );
	}

	@Override
	public List<Integer> getEntriesIdsOlderThen( final Date timeFrame, final List<JobExecutionStatus> jobExecutionStatusesToDelete ) {
		final List<Integer> statusIds = ListUtils.convertIdentifiableListToListOfIds( jobExecutionStatusesToDelete );

		final String sql = String.format( "SELECT %s FROM %s WHERE %s IN ( %s ) AND %s < :timeFrame;"
			, ENTITY_ID, TABLE_JOB_EXECUTION_HISTORY, TABLE_JOB_DONE_COL_JOB_STATUS_ID, StringUtils.join( statusIds, "," ), TABLE_JOB_DONE_COL_END_TIME );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "timeFrame", timeFrame );

		return jdbcTemplate.query( sql, paramSource, new IdsRowMapper() );
	}

	@Override
	public void deleteEntriesOlderThen( final Date timeFrame, final List<JobExecutionStatus> jobExecutionStatusesToDelete ) {

		final List<Integer> jobExecutionHistoryToDeleteIds = getEntriesIdsOlderThen( timeFrame, jobExecutionStatusesToDelete );
		for ( final int jobExecutionHistoryId : jobExecutionHistoryToDeleteIds ) {
			delete( jobExecutionHistoryId );
		}
	}

	public void delete( final List<Integer> entryIds ) {

		if ( entryIds.size() == 0 ) {
			return;
		}

		final String sql = String.format( "DELETE FROM %s WHERE %s IN ( %s );", getTableName(), BaseEntityDao.ENTITY_ID, StringUtils.join( entryIds, "," ) );
		jdbcTemplate.update( sql, new MapSqlParameterSource() );
	}

	@Override
	public boolean delete( final int entryId ) {
		deleteJobParameters( entryId );
		return super.delete( entryId );
	}

	@Override
	public boolean saveToDB( final JobExecutionHistoryEntry entry ) {
		final boolean isNew = entry.isNew();
		synchronized ( this ) {
			boolean isSuccessful = createOrUpdateEntry( entry, fields, updatableFields );

			if ( isSuccessful ) {
				final int jobExecutionHistoryEntryId = entry.getId();

				if ( ! isNew ){
					deleteJobParameters( jobExecutionHistoryEntryId );
				}

				final Map<SavedJobParameterKey, CommonProperty> parametersMap = entry.getParametersMap();
				for ( final SavedJobParameterKey key : parametersMap.keySet() ) {
					isSuccessful &= saveParameter( jobExecutionHistoryEntryId, key, parametersMap.get( key ).getValue() );
				}
			}
			return isSuccessful;
		}
	}

	@Override
	protected RowMapper<JobExecutionHistoryEntry> getRowMapper() {
		return new JobExecutionHistoryMapper();
	}

	@Override
	protected MapSqlParameterSource getParameters( final JobExecutionHistoryEntry entry ) {
		final MapSqlParameterSource paramSource = new MapSqlParameterSource();

		paramSource.addValue( TABLE_JOB_DONE_COL_JOB_TYPE_ID, entry.getSavedJobType().getId() );
		paramSource.addValue( TABLE_JOB_DONE_COL_PARAMETERS_DESCRIPTION, entry.getParametersDescription() );
		paramSource.addValue( TABLE_JOB_DONE_COL_START_TIME, entry.getStartTime() );
		paramSource.addValue( TABLE_JOB_DONE_COL_END_TIME, entry.getEndTime() );
		paramSource.addValue( TABLE_JOB_DONE_COL_JOB_STATUS_ID, entry.getJobExecutionStatus().getId() );
		paramSource.addValue( TABLE_JOB_DONE_COL_JOB_MESSAGE, entry.getJobMessage() );

		final SavedJob savedJob = entry.getSavedJob();
		if ( savedJob != null ) {
			paramSource.addValue( TABLE_JOB_DONE_COL_SAVED_JOB_ID, savedJob.getId() );
		} else {
			paramSource.addValue( TABLE_JOB_DONE_COL_SAVED_JOB_ID, 0 );
		}

		paramSource.addValue( TABLE_JOB_DONE_COL_CURRENT_JOB_STEP, entry.getCurrentJobStep() );
		paramSource.addValue( TABLE_JOB_DONE_COL_TOTAL_JOB_STEPS, entry.getTotalJobSteps() );

		paramSource.addValue( TABLE_JOB_DONE_COL_SCHEDULER_TASK_ID, entry.getScheduledTaskId() );

		return paramSource;
	}

	private void setJobStatus( final int jobId, final JobExecutionStatus status ) {
		final String sql = String.format( "UPDATE %s SET %s = :endTime, %s = :jobStatusId WHERE %s = :id;"
			, TABLE_JOB_EXECUTION_HISTORY, TABLE_JOB_DONE_COL_END_TIME, TABLE_JOB_DONE_COL_JOB_STATUS_ID, ENTITY_ID );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "id", jobId );
		paramSource.addValue( "endTime", dateUtilsService.getCurrentTime() );
		paramSource.addValue( "jobStatusId", status.getId() );

		jdbcTemplate.update( sql, paramSource );
	}

	private void setJobStatusWithMessage( final int jobId, final String message, final JobExecutionStatus jobExecutionStatus ) {
		final String sql = String.format( "UPDATE %s SET %s = :endTime, %s = :jobStatusId, %s = :error WHERE %s = :id;"
			, TABLE_JOB_EXECUTION_HISTORY, TABLE_JOB_DONE_COL_END_TIME, TABLE_JOB_DONE_COL_JOB_STATUS_ID, TABLE_JOB_DONE_COL_JOB_MESSAGE, ENTITY_ID );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "id", jobId );
		paramSource.addValue( "endTime", dateUtilsService.getCurrentTime() );
		paramSource.addValue( "jobStatusId", jobExecutionStatus.getId() );
		paramSource.addValue( "error", message );

		jdbcTemplate.update( sql, paramSource );
	}

	private void updateJobStep( final int jobExecutionHistoryId, final int currentJobStep, final String column ) {
		final String sql = String.format( "UPDATE %s SET %s = :currentJobStep WHERE %s=:jobExecutionHistoryId"
			, TABLE_JOB_EXECUTION_HISTORY, column, ENTITY_ID );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "jobExecutionHistoryId", jobExecutionHistoryId );
		paramSource.addValue( "currentJobStep", currentJobStep );

		jdbcTemplate.update( sql, paramSource );
	}

	private class JobExecutionHistoryMapper implements RowMapper<JobExecutionHistoryEntry> {

		@Override
		public JobExecutionHistoryEntry mapRow( final ResultSet rs, final int rowNum ) throws SQLException {
			final JobExecutionHistoryEntry result = new JobExecutionHistoryEntry();

			result.setId( rs.getInt( ENTITY_ID ) );

			result.setStartTime( rs.getTimestamp( TABLE_JOB_DONE_COL_START_TIME ) );
			result.setEndTime( rs.getTimestamp( TABLE_JOB_DONE_COL_END_TIME ) );

			result.setSavedJobType( SavedJobType.getById( rs.getInt( TABLE_JOB_DONE_COL_JOB_TYPE_ID ) ) );
			result.setJobExecutionStatus( JobExecutionStatus.getById( rs.getInt( TABLE_JOB_DONE_COL_JOB_STATUS_ID ) ) );

			result.setParametersDescription( rs.getString( TABLE_JOB_DONE_COL_PARAMETERS_DESCRIPTION ) );
			result.setJobMessage( rs.getString( TABLE_JOB_DONE_COL_JOB_MESSAGE ) );

			final int savedJobId = rs.getInt( TABLE_JOB_DONE_COL_SAVED_JOB_ID );
			if ( savedJobId > 0 ) {
				result.setSavedJob( savedJobDao.load( savedJobId ) );
			}

			result.setCurrentJobStep( rs.getInt( TABLE_JOB_DONE_COL_CURRENT_JOB_STEP ) );
			result.setTotalJobSteps( rs.getInt( TABLE_JOB_DONE_COL_TOTAL_JOB_STEPS ) );

			result.setScheduledTaskId( rs.getInt( TABLE_JOB_DONE_COL_SCHEDULER_TASK_ID ) );

			return result;
		}
	}

	private class JobExecutionHistoryParameterMapper implements RowMapper<CommonProperty> {

		@Override
		public CommonProperty mapRow( final ResultSet rs, final int rowNum ) throws SQLException {
			final int id = rs.getInt( TABLE_JOB_EXECUTION_HISTORY_PARAMETERS_COLUMN_KEY );
			final String value = rs.getString( TABLE_JOB_EXECUTION_HISTORY_PARAMETERS_COLUMN_VALUE );

			return new CommonProperty( id, value );
		}
	}
}
