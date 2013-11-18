package core.services.dao;

import admin.jobs.enums.SavedJobType;
import admin.jobs.general.SavedJob;
import core.enums.SavedJobParameterKey;
import core.general.base.CommonProperty;
import core.services.dao.mappers.IdsRowMapper;
import core.services.utils.DateUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Maps.newLinkedHashMap;

public class SavedJobDaoImpl extends BaseEntityDaoImpl<SavedJob> implements SavedJobDao {

	public static final String TABLE_JOBS = "job";
	public static final String TABLE_JOBS_COLUMN_NAME = "name";
	public static final String TABLE_JOBS_COLUMN_IS_ACTIVE = "isActive";
	public static final String TABLE_JOBS_COLUMN_JOB_TYPE = "jobType";

	public static final String TABLE_JOB_PARAMETERS = "jobParameters";
	public static final String TABLE_JOBS_PARAMS_COLUMN_JOB_ID = "jobId";
	public static final String TABLE_JOBS_PARAMS_COLUMN_KEY = "pKey";
	public static final String TABLE_JOBS_PARAMS_COLUMN_VALUE = "pValue";

	public static final Map<Integer, String> fields = newLinkedHashMap();
	public static final Map<Integer, String> updatableFields = newLinkedHashMap();

	public static final int ACTIVE = 1;
	public static final int INACTIVE = 0;

	@Autowired
	private DateUtilsService dateUtilsService;

	static {
		fields.put( 1, TABLE_JOBS_COLUMN_NAME );
		fields.put( 2, TABLE_JOBS_COLUMN_IS_ACTIVE );
		fields.put( 3, TABLE_JOBS_COLUMN_JOB_TYPE );
	}

	static {
		updatableFields.put( 1, TABLE_JOBS_COLUMN_NAME );
		updatableFields.put( 3, TABLE_JOBS_COLUMN_IS_ACTIVE );
	}

	@Override
	protected String getTableName() {
		return TABLE_JOBS;
	}

	@Override
	public boolean saveToDB( final SavedJob savedJob ) {
		return createOrUpdateEntry( savedJob, fields, fields );
	}

	@Override
	public List<Integer> loadIdsAll() {
		final String sql = String.format( "SELECT %1$s FROM %2$s ORDER BY %1$s", ENTITY_ID, TABLE_JOBS );
		return jdbcTemplate.query( sql, new MapSqlParameterSource(), new IdsRowMapper() );
	}

	@Override
	protected RowMapper<SavedJob> getRowMapper() {
		return new SavedJobMapper();
	}

	@Override
	public boolean activate( final int savedJobId ) {
		return setJobActivity( savedJobId, ACTIVE );
	}

	@Override
	public boolean deactivate( final int savedJobId ) {
		return setJobActivity( savedJobId, INACTIVE );
	}

	@Override
	public SavedJob loadByName( final String jobName ) {
		final String sql = String.format( "SELECT * FROM %s WHERE %s=:jobName", TABLE_JOBS, TABLE_JOBS_COLUMN_NAME );
		return getEntryOrNull( sql, new MapSqlParameterSource( "jobName", jobName ), new SavedJobMapper() );
	}

	@Override
	protected MapSqlParameterSource getParameters( final SavedJob entry ) {
		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( TABLE_JOBS_COLUMN_NAME, entry.getName() );
		paramSource.addValue( TABLE_JOBS_COLUMN_IS_ACTIVE, entry.isActive() );
		paramSource.addValue( TABLE_JOBS_COLUMN_JOB_TYPE, entry.getJobType().getId() );

		return paramSource;
	}

	@Override
	public boolean saveParameter( final int savedJobId, final SavedJobParameterKey key, final String value ) {
		final String sql = String.format( "INSERT INTO %s ( %s, %s, %s) VALUES ( :jobId, :key, :value )"
			, TABLE_JOB_PARAMETERS, TABLE_JOBS_PARAMS_COLUMN_JOB_ID, TABLE_JOBS_PARAMS_COLUMN_KEY, TABLE_JOBS_PARAMS_COLUMN_VALUE );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "jobId", savedJobId );
		paramSource.addValue( "key", key.getId() );
		paramSource.addValue( "value", value );

		return jdbcTemplate.update( sql, paramSource ) > 0;
	}

	@Override
	public boolean deleteJobParameters( final int savedJobId ) {
		final String sql = String.format( "DELETE FROM %s WHERE %s=:jobId", TABLE_JOB_PARAMETERS, TABLE_JOBS_PARAMS_COLUMN_JOB_ID );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "jobId", savedJobId );

		return jdbcTemplate.update( sql, paramSource ) > 0;
	}

	@Override
	public CommonProperty getJobParameter( final int savedJobId, final SavedJobParameterKey key ) {
		final String sql = String.format( "SELECT %s FROM %s WHERE %s = :jobId AND %s = :key"
				, TABLE_JOBS_PARAMS_COLUMN_VALUE, TABLE_JOB_PARAMETERS, TABLE_JOBS_PARAMS_COLUMN_JOB_ID, TABLE_JOBS_PARAMS_COLUMN_KEY );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "jobId", savedJobId );
		paramSource.addValue( "key", key.getId() );

		final String value = getValueOrEmptyString( sql, paramSource );
		return new CommonProperty( key.getId(), value );
	}

	@Override
	public Map<SavedJobParameterKey, CommonProperty> getJobParameters( final int savedJobId ) {
		final String sql = String.format( "SELECT * FROM %s WHERE %s = :jobId", TABLE_JOB_PARAMETERS, TABLE_JOBS_PARAMS_COLUMN_JOB_ID );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "jobId", savedJobId );

		final List<CommonProperty> jobProperties = jdbcTemplate.query( sql, paramSource, new SavedJobParameterMapper() );
		final Map<SavedJobParameterKey, CommonProperty> result = newHashMap();
		for ( final CommonProperty property : jobProperties ) {
			result.put( SavedJobParameterKey.getById( property.getKey() ), property );
		}

		return result;
	}

	@Override
	public List<SavedJob> getJobsByType( final SavedJobType savedJobType ) {
		final String sql = String.format( "SELECT * FROM %s WHERE %s = :savedJobTypeId;", TABLE_JOBS, TABLE_JOBS_COLUMN_JOB_TYPE );
		return jdbcTemplate.query( sql, new MapSqlParameterSource( "savedJobTypeId", savedJobType.getId() ), new SavedJobMapper() );
	}

	private boolean setJobActivity( final int savedJobId, final int active ) {
		final String sql = String.format( "UPDATE %s SET %s = :active WHERE %s=:id", TABLE_JOBS, TABLE_JOBS_COLUMN_IS_ACTIVE, ENTITY_ID );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "id", savedJobId );
		paramSource.addValue( "active", active );

		return jdbcTemplate.update( sql, paramSource ) > 0;
	}

	private class SavedJobMapper implements RowMapper<SavedJob> {

		@Override
		public SavedJob mapRow( final ResultSet rs, final int rowNum ) throws SQLException {
			final SavedJob savedJob = new SavedJob();

			savedJob.setId( rs.getInt( ENTITY_ID ) );
			savedJob.setName( rs.getString( TABLE_JOBS_COLUMN_NAME ) );
			savedJob.setActive( rs.getBoolean( TABLE_JOBS_COLUMN_IS_ACTIVE ) );
			savedJob.setJobType( SavedJobType.getById( rs.getInt( TABLE_JOBS_COLUMN_JOB_TYPE ) ) );

			return savedJob;
		}
	}

	private class SavedJobParameterMapper implements RowMapper<CommonProperty> {

		@Override
		public CommonProperty mapRow( final ResultSet rs, final int rowNum ) throws SQLException {
			final int id = rs.getInt( TABLE_JOBS_PARAMS_COLUMN_KEY );
			final String value = rs.getString( TABLE_JOBS_PARAMS_COLUMN_VALUE );

			return new CommonProperty( id, value );
		}
	}
}
