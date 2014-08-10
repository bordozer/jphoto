package core.services.dao;

import core.enums.RestrictionType;
import core.general.restriction.EntryRestriction;
import core.interfaces.Restrictable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newLinkedHashMap;

public class RestrictionDaoImpl extends BaseEntityDaoImpl<EntryRestriction> implements RestrictionDao {

	public final static String TABLE_RESTRICTION = "restriction";

	public final static String TABLE_RESTRICTION_COLUMN_ENTRY_ID = "entryId";
	public final static String TABLE_RESTRICTION_COLUMN_RESTRICTION_TYPE_ID = "restrictionTypeId";
	public final static String TABLE_RESTRICTION_COLUMN_RESTRICTION_TIME_FROM = "restrictionTimeFrom";
	public final static String TABLE_RESTRICTION_COLUMN_RESTRICTION_TIME_TO = "restrictionTimeTo";
	public final static String TABLE_RESTRICTION_COLUMN_RESTRICTION_MESSAGE = "restrictionMessage";
	public final static String TABLE_RESTRICTION_COLUMN_RESTRICTION_COMMENT = "restrictionComment";

	public final static String TABLE_RESTRICTION_COLUMN_ACTIVE = "active";
	public final static String TABLE_RESTRICTION_COLUMN_CREATING_TIME = "creatingTime";
	public final static String TABLE_RESTRICTION_COLUMN_CREATED_USER_ID = "createdUserId";
	public final static String TABLE_RESTRICTION_COLUMN_CANCELLED_USER_ID = "cancelledUserId";
	public final static String TABLE_RESTRICTION_COLUMN_CANCEL_TIME = "cancellingTime";

	public static final Map<Integer, String> fields = newLinkedHashMap();
	public static final Map<Integer, String> updatableFields = newLinkedHashMap();

	@Autowired
	private UserDao userDao;

	@Autowired
	private PhotoDao photoDao;

	static {
		fields.put( 1, TABLE_RESTRICTION_COLUMN_ENTRY_ID );
		fields.put( 2, TABLE_RESTRICTION_COLUMN_RESTRICTION_TYPE_ID );
		fields.put( 3, TABLE_RESTRICTION_COLUMN_RESTRICTION_TIME_FROM );
		fields.put( 4, TABLE_RESTRICTION_COLUMN_RESTRICTION_TIME_TO );
		fields.put( 5, TABLE_RESTRICTION_COLUMN_RESTRICTION_MESSAGE );
		fields.put( 6, TABLE_RESTRICTION_COLUMN_RESTRICTION_COMMENT );
		fields.put( 7, TABLE_RESTRICTION_COLUMN_ACTIVE );
		fields.put( 8, TABLE_RESTRICTION_COLUMN_CREATED_USER_ID );
		fields.put( 9, TABLE_RESTRICTION_COLUMN_CREATING_TIME );
	}

	static {
		updatableFields.put( 3, TABLE_RESTRICTION_COLUMN_RESTRICTION_TIME_FROM );
		updatableFields.put( 4, TABLE_RESTRICTION_COLUMN_RESTRICTION_TIME_TO );
		updatableFields.put( 5, TABLE_RESTRICTION_COLUMN_RESTRICTION_MESSAGE );
		updatableFields.put( 6, TABLE_RESTRICTION_COLUMN_RESTRICTION_COMMENT );
		updatableFields.put( 7, TABLE_RESTRICTION_COLUMN_CANCELLED_USER_ID );
		updatableFields.put( 8, TABLE_RESTRICTION_COLUMN_CANCEL_TIME );
		updatableFields.put( 9, TABLE_RESTRICTION_COLUMN_ACTIVE );
	}

	@Override
	public boolean saveToDB( final EntryRestriction entry ) {
		return createOrUpdateEntry( entry, fields, updatableFields );
	}

	@Override
	public List<EntryRestriction> loadRestrictions( final int entryId, final RestrictionType restrictionType ) {
		final String sql = String.format( "SELECT * FROM %s WHERE %s=:entryId AND %s=:restrictionTypeId ORDER BY %s;"
			, TABLE_RESTRICTION, TABLE_RESTRICTION_COLUMN_ENTRY_ID, TABLE_RESTRICTION_COLUMN_RESTRICTION_TYPE_ID, ENTITY_ID );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "entryId", entryId );
		paramSource.addValue( "restrictionTypeId", restrictionType.getId() );

		return jdbcTemplate.query( sql, paramSource, getRowMapper() );
	}

	@Override
	protected MapSqlParameterSource getParameters( final EntryRestriction restriction ) {
		final MapSqlParameterSource paramSource = new MapSqlParameterSource();

		paramSource.addValue( TABLE_RESTRICTION_COLUMN_ENTRY_ID, restriction.getEntry().getId() );
		paramSource.addValue( TABLE_RESTRICTION_COLUMN_RESTRICTION_TYPE_ID, restriction.getRestrictionType().getId() );
		paramSource.addValue( TABLE_RESTRICTION_COLUMN_RESTRICTION_TIME_FROM, restriction.getRestrictionTimeFrom() );
		paramSource.addValue( TABLE_RESTRICTION_COLUMN_RESTRICTION_TIME_TO, restriction.getRestrictionTimeTo() );
		paramSource.addValue( TABLE_RESTRICTION_COLUMN_RESTRICTION_MESSAGE, restriction.getRestrictionMessage() );
		paramSource.addValue( TABLE_RESTRICTION_COLUMN_RESTRICTION_COMMENT, restriction.getRestrictionRestrictionComment() );

		paramSource.addValue( TABLE_RESTRICTION_COLUMN_ACTIVE, restriction.isActive() ? 1 : 0 );
		paramSource.addValue( TABLE_RESTRICTION_COLUMN_CREATING_TIME, restriction.getCreatingTime() );
		paramSource.addValue( TABLE_RESTRICTION_COLUMN_CREATED_USER_ID, restriction.getCreator().getId() );

		if ( restriction.getCanceller() != null ) {
			paramSource.addValue( TABLE_RESTRICTION_COLUMN_CANCELLED_USER_ID, restriction.getCanceller().getId() );
			paramSource.addValue( TABLE_RESTRICTION_COLUMN_CANCEL_TIME, restriction.getCancellingTime() );
		}

		return paramSource;
	}

	@Override
	protected String getTableName() {
		return TABLE_RESTRICTION;
	}

	@Override
	protected RowMapper<EntryRestriction> getRowMapper() {
		return new EntryRestrictionRowMapper();
	}

	@Override
	public boolean delete( final int entryId ) {
		throw new IllegalStateException( "Restriction history entry can not be deleted. Deactivation is allowed only!" );
	}

	private class EntryRestrictionRowMapper implements RowMapper<EntryRestriction> {

		@Override
		public EntryRestriction mapRow( final ResultSet rs, final int rowNum ) throws SQLException {

			final int entryId = rs.getInt( TABLE_RESTRICTION_COLUMN_ENTRY_ID );
			final RestrictionType restrictionType = RestrictionType.getById( rs.getInt( TABLE_RESTRICTION_COLUMN_RESTRICTION_TYPE_ID ) );

			final EntryRestriction<Restrictable> result = getRestrictionEntry( entryId, restrictionType );
			result.setId( rs.getInt( ENTITY_ID ) );

			result.setRestrictionTimeFrom( rs.getTimestamp( TABLE_RESTRICTION_COLUMN_RESTRICTION_TIME_FROM ) );
			result.setRestrictionTimeTo( rs.getTimestamp( TABLE_RESTRICTION_COLUMN_RESTRICTION_TIME_TO ) );

			result.setRestrictionMessage( rs.getString( TABLE_RESTRICTION_COLUMN_RESTRICTION_MESSAGE ) );
			result.setRestrictionRestrictionComment( rs.getString( TABLE_RESTRICTION_COLUMN_RESTRICTION_COMMENT ) );


			result.setActive( rs.getBoolean( TABLE_RESTRICTION_COLUMN_ACTIVE ) );
			result.setCreator( userDao.load( rs.getInt( TABLE_RESTRICTION_COLUMN_CREATED_USER_ID ) ) );
			result.setCreatingTime( rs.getTimestamp( TABLE_RESTRICTION_COLUMN_CREATING_TIME ) );

			final int cancellerId = rs.getInt( TABLE_RESTRICTION_COLUMN_CANCELLED_USER_ID );
			if ( cancellerId > 0 ) {
				result.setCanceller( userDao.load( cancellerId ) );
				result.setCancellingTime( rs.getTimestamp( TABLE_RESTRICTION_COLUMN_CANCEL_TIME ) );
			}

			return result;
		}

		private EntryRestriction<Restrictable> getRestrictionEntry( final int entryId, final RestrictionType restrictionType ) {

			switch ( restrictionType ) {
				case USER_LOGIN:
				case USER_PHOTO_UPLOADING:
				case USER_COMMENTING:
				case USER_MESSAGING:
				case USER_PHOTO_APPRAISAL:
				case USER_VOTING_FOR_RANK_IN_GENRE:
					return new EntryRestriction<>( userDao.load( entryId ), restrictionType );
				case PHOTO_TO_BE_PHOTO_OF_THE_DAY:
				case PHOTO_TO_BE_BEST_IN_GENRE:
				case PHOTO_COMMENTING:
					return new EntryRestriction<>( photoDao.load( entryId ), restrictionType );
			}

			throw new IllegalArgumentException( String.format( "Illegal restrictionType: '%s'", restrictionType ) );
		}
	}
}
