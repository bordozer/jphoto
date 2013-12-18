package core.services.dao;

import core.exceptions.BaseRuntimeException;
import core.general.user.User;
import core.interfaces.BaseEntity;
import core.log.LogHelper;
import core.interfaces.IdsSqlSelectable;
import core.services.dao.mappers.IdsRowMapper;
import core.services.entry.PrivateMessageService;
import core.services.security.SecurityService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import sql.SqlSelectIdsResult;
import sql.SqlSelectResult;
import sql.builder.*;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

public abstract class BaseEntityDaoImpl<T extends BaseEntity> extends BaseDaoImpl implements IdsSqlSelectable {

	private static final String SELECT_ENTRY_BY_ID_SQL = "SELECT * FROM %s WHERE %s=:id;";
	private static final String DELETE_ENTRY_BY_ID_SQL = "DELETE FROM %s WHERE %s=:id;";

	protected abstract MapSqlParameterSource getParameters( final T entry );

	protected abstract String getTableName();

	protected abstract RowMapper<T> getRowMapper();

	private LogHelper logHelper = new LogHelper( BaseEntityDaoImpl.class );

	@Autowired
	private PrivateMessageService privateMessageService;

	@Autowired
	private SecurityService securityService;

	public boolean createOrUpdateEntry( final T entry, final Map<Integer, String> fieldsToInsert, final Map<Integer, String> fieldsToUpdate ) {

		final String tableName = getTableName();

		final StringBuilder sqlBuilder = new StringBuilder();

		final boolean aNew = isNew( entry );
		if ( aNew ) {
			sqlBuilder.append( "INSERT INTO " ).append( tableName );
			sqlBuilder.append( "( " ).append( getInsertFieldList( fieldsToInsert ) ).append( " )" );
			sqlBuilder.append( " VALUES ( " ).append( getValuesForInsertStatement( fieldsToInsert ) ).append( " )" );
		} else {
			sqlBuilder.append( " UPDATE " ).append( tableName ).append( " SET " ).append( getUpdateFieldsAssignment( fieldsToUpdate ) );
			sqlBuilder.append( " WHERE " ).append( BaseEntityDao.ENTITY_ID ).append( "=" ).append( entry.getId() );
		}

		final String sql = sqlBuilder.toString();

		logHelper.debug( String.format( "SQL: %s", sql ) );

		final MapSqlParameterSource parameters = getParameters( entry );
		final boolean entryHasBeenCreated = jdbcTemplate.update( sql, parameters ) > 0;

		if ( ! entryHasBeenCreated ) {
			final String message = String.format( "Record is not created/updated ( %s, id=%d ). sql: '%s', %s", entry.getClass().getName(), entry.getId(), sql, parameters );
			logHelper.error( message );
			sendSystemNotificationAboutErrorToAdmins( message );
			return false;
		}

		if ( aNew ) {
			final long newId = jdbcTemplate.queryForLong( "SELECT LAST_INSERT_ID()", new MapSqlParameterSource() );

			if ( newId == 0 ) {
				final String message = String.format( "SELECT LAST_INSERT_ID() has not returned last ID ( %s ). sql: '%s', %s", entry.getClass().getName(), sql, parameters );
				sendSystemNotificationAboutErrorToAdmins( message );
				throw new BaseRuntimeException( message );
			}

			entry.setId( ( int ) newId );

			logHelper.debug( String.format( "new entry (%s) has been creates ID: %s", entry.getClass().getName(), newId ) );
		}

		return true;
	}

	private void sendSystemNotificationAboutErrorToAdmins( final String message ) {
		for ( final User adminUser : securityService.getSuperAdminUsers() ) {
			privateMessageService.sendSystemNotificationMessage( adminUser, message );
		}
	}

	final public SqlSelectResult<T> load( final SqlSelectQuery selectQuery ) {
		return loadSelectResult( selectQuery, getRowMapper() );
	}

	public T load( final int entryId ) {
		return loadEntryById( entryId, getRowMapper() );
	}

	public boolean delete( final int entryId ) {
		return deleteEntryById( entryId );
	}

	protected T loadEntryById( final int entryId, final RowMapper<T> rowMapper ) {
		final String sql = String.format( SELECT_ENTRY_BY_ID_SQL, getTableName(), BaseEntityDao.ENTITY_ID );

		return getEntryOrNull( sql, new MapSqlParameterSource( "id", entryId ), rowMapper );
	}

	protected boolean deleteEntryById( final int entryId ) {
		final String sql = String.format( DELETE_ENTRY_BY_ID_SQL, getTableName(), BaseEntityDao.ENTITY_ID );
		return jdbcTemplate.update( sql, new MapSqlParameterSource( "id", entryId ) ) > 0;
	}

	private String getInsertFieldList( final Map<Integer, String> fields ) {
		return StringUtils.join( fields.values(), ", " );
	}

	private String getValuesForInsertStatement( final Map<Integer, String> fields ) {
		final List<String> strings = newArrayList();

		for ( Integer key : fields.keySet() ) {
			strings.add( String.format( ":%s", fields.get( key ) ) );
		}

		return StringUtils.join( strings, ", " );
	}

	private String getUpdateFieldsAssignment( final Map<Integer, String> fields ) {
		final List<String> strings = newArrayList();

		for ( Integer key : fields.keySet() ) {
			strings.add( String.format( "%1$s=:%1$s", fields.get( key ) ) );
		}

		return StringUtils.join( strings, ", " );
	}

	public boolean isNew( T entry ) {
		return entry.getId() <= 0;
	}

	protected int getTotalRecordQty( final BaseSqlSelectQuery selectQuery ) {
		final SqlSelectQuery query = convertQryToIdsQuery( selectQuery );

		final List<Map<String, Object>> maps = jdbcTemplate.queryForList( query.build(), new MapSqlParameterSource() ); // TODO: do something with this!

		return maps.size();
	}

	protected SqlSelectQuery convertQryToIdsQuery( final BaseSqlSelectQuery selectQuery ) {
		final SqlSelectQuery query = selectQuery.cloneQuery();

		query.resetSelectColumns();
		query.resetLimitAndOffset();
		query.resetSortCriterias();

		final SqlColumnSelect idColumn = new SqlColumnSelect( query.getMainTable(), BaseEntityDao.ENTITY_ID );
		query.addSelect( idColumn );
		return query;
	}

	@Override
	public final SqlSelectIdsResult load( final SqlIdsSelectQuery selectQuery ) {
		SqlSelectIdsResult sqlSelectIdsResult = new SqlSelectIdsResult();

		sqlSelectIdsResult.setIds( jdbcTemplate.query( selectQuery.build(), new MapSqlParameterSource(), new IdsRowMapper() ) );

		final int total = getTotalRecordQty( selectQuery );
		sqlSelectIdsResult.setRecordQty( total );

		return sqlSelectIdsResult;
	}

	public SqlSelectResult<T> loadSelectResult( final SqlSelectQuery selectQuery, final RowMapper<T> mapper ) {
		final MapSqlParameterSource paramSource = new MapSqlParameterSource();

		final List<T> photos = jdbcTemplate.query( selectQuery.build(), paramSource, mapper );

		final SqlSelectResult<T> selectResult = new SqlSelectResult<T>();
		selectResult.setItems( photos );

		final int total = getTotalRecordQty( selectQuery );
		selectResult.setRecordQty( total );

		return selectResult;
	}

	public final boolean exists( final T entry ) {
		return exists( entry.getId() );
	}

	public final boolean exists( final int entryId ) {
		final String sql = String.format( "SELECT 1 FROM %s WHERE %s=:entryId;", getTableName(), BaseEntityDao.ENTITY_ID );
		return existsInt( sql, new MapSqlParameterSource( "entryId", entryId ) );
	}
}
