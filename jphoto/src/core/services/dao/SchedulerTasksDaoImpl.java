package core.services.dao;

import core.enums.SchedulerTaskProperty;
import core.general.executiontasks.ExecutionTaskType;
import core.general.scheduler.SchedulerTask;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newLinkedHashMap;

public class SchedulerTasksDaoImpl extends BaseEntityDaoImpl<SchedulerTask> implements SchedulerTasksDao {

	public final static String TABLE_SCHEDULER_TASKS = "schedulerTasks";

	public final static String TABLE_SCHEDULER_TASKS_COLUMN_NAME = "name";
	public final static String TABLE_SCHEDULER_TASKS_COLUMN_TASK_TYPE = "taskType";
	public final static String TABLE_SCHEDULER_TASKS_COLUMN_JOB_ID = "jobId";

	public final static String TABLE_SCHEDULER_TASKS_PROPERTIES = "schedulerTasksProperties";

	public final static String TABLE_SCHEDULER_TASKS_PROPERTIES_COLUMN_TASK_ID = "schedulerTaskId";
	public final static String TABLE_SCHEDULER_TASKS_PROPERTIES_COLUMN_KEY = "propertyKey";
	public final static String TABLE_SCHEDULER_TASKS_PROPERTIES_COLUMN_VALUE = "propertyValue";

	public static final Map<Integer, String> fields = newLinkedHashMap();

	static {
		fields.put( 1, TABLE_SCHEDULER_TASKS_COLUMN_NAME );
		fields.put( 2, TABLE_SCHEDULER_TASKS_COLUMN_TASK_TYPE );
		fields.put( 3, TABLE_SCHEDULER_TASKS_COLUMN_JOB_ID );
	}

	@Override
	protected String getTableName() {
		return TABLE_SCHEDULER_TASKS;
	}

	@Override
	public boolean saveToDB( final SchedulerTask schedulerTask ) {
		return createOrUpdateEntry( schedulerTask, fields, fields );
	}

	@Override
	protected RowMapper<SchedulerTask> getRowMapper() {
		return new SchedulerTaskMapper();
	}

	@Override
	public List<SchedulerTask> loadAll() {
		final String sql = String.format( "SELECT * FROM %s ORDER BY %s", TABLE_SCHEDULER_TASKS, TABLE_SCHEDULER_TASKS_COLUMN_NAME );

		return jdbcTemplate.query( sql, new MapSqlParameterSource(), new SchedulerTaskMapper() );
	}

	@Override
	public int loadIdByName( final String schedulerTaskName ) {
		final String sql = String.format( "SELECT %s FROM %s WHERE %s=:name", ENTITY_ID, TABLE_SCHEDULER_TASKS, TABLE_SCHEDULER_TASKS_COLUMN_NAME );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "name", schedulerTaskName );

		return getIntValueOrZero( sql, paramSource );
	}

	@Override
	public boolean saveProperties( final int schedulerTaskId, final SchedulerTaskProperty key, final String value ) {
		final String sql = String.format( "INSERT INTO %s ( %s, %s, %s) VALUES ( :schedulerTaskId, :propertyKey, :propertyValue );"
			, TABLE_SCHEDULER_TASKS_PROPERTIES, TABLE_SCHEDULER_TASKS_PROPERTIES_COLUMN_TASK_ID, TABLE_SCHEDULER_TASKS_PROPERTIES_COLUMN_KEY, TABLE_SCHEDULER_TASKS_PROPERTIES_COLUMN_VALUE );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "schedulerTaskId", schedulerTaskId );
		paramSource.addValue( "propertyKey", key.getId() );
		paramSource.addValue( "propertyValue", value );

		return jdbcTemplate.update( sql, paramSource ) > 0;
	}

	@Override
	public String getSchedulerTaskPropertyValue( final int schedulerTaskId, final SchedulerTaskProperty key ) {
		final String sql = String.format( "SELECT %s FROM %s WHERE %s=:schedulerTaskId AND %s=:propertyKey"
			, TABLE_SCHEDULER_TASKS_PROPERTIES_COLUMN_VALUE, TABLE_SCHEDULER_TASKS_PROPERTIES, TABLE_SCHEDULER_TASKS_PROPERTIES_COLUMN_TASK_ID, TABLE_SCHEDULER_TASKS_PROPERTIES_COLUMN_KEY );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "schedulerTaskId", schedulerTaskId );
		paramSource.addValue( "propertyKey", key.getId() );

		return getValueOrEmptyString( sql, paramSource );
	}

	@Override
	public void deleteProperties( final int schedulerTaskId ) {
		final String sql = String.format( "DELETE FROM %s WHERE %s=:schedulerTaskId", TABLE_SCHEDULER_TASKS_PROPERTIES, TABLE_SCHEDULER_TASKS_PROPERTIES_COLUMN_TASK_ID );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "schedulerTaskId", schedulerTaskId );

		jdbcTemplate.update( sql, paramSource );
	}

	@Override
	public boolean isJobScheduled( final int savedJobId ) {
		final String sql = String.format( "SELECT 1 FROM %s WHERE %s=:savedJobId LIMIT 1", TABLE_SCHEDULER_TASKS, TABLE_SCHEDULER_TASKS_COLUMN_JOB_ID );

		return getIntValueOrZero( sql, new MapSqlParameterSource( "savedJobId", savedJobId ) ) > 0;
	}

	@Override
	protected MapSqlParameterSource getParameters( final SchedulerTask entry ) {
		final MapSqlParameterSource paramSource = new MapSqlParameterSource();

		paramSource.addValue( TABLE_SCHEDULER_TASKS_COLUMN_NAME, entry.getName() );
		paramSource.addValue( TABLE_SCHEDULER_TASKS_COLUMN_TASK_TYPE, entry.getTaskType().getId() );
		paramSource.addValue( TABLE_SCHEDULER_TASKS_COLUMN_JOB_ID, entry.getSavedJobId() );

		return paramSource;
	}

	private class SchedulerTaskMapper implements RowMapper<SchedulerTask> {

		@Override
		public SchedulerTask mapRow( final ResultSet rs, final int rowNum ) throws SQLException {
			final SchedulerTask result = new SchedulerTask();

			result.setId( rs.getInt( ENTITY_ID ) );
			result.setName( rs.getString( TABLE_SCHEDULER_TASKS_COLUMN_NAME ) );
			result.setTaskType( ExecutionTaskType.getById( rs.getInt( TABLE_SCHEDULER_TASKS_COLUMN_TASK_TYPE ) ) );
			result.setSavedJobId( rs.getInt( TABLE_SCHEDULER_TASKS_COLUMN_JOB_ID ) );

			return result;
		}
	}
}
