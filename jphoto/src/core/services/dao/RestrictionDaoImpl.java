package core.services.dao;

import core.enums.RestrictionType;
import core.general.restriction.EntryRestriction;
import core.services.utils.DateUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
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

	public static final Map<Integer, String> fields = newLinkedHashMap();
	public static final Map<Integer, String> updatableFields = newLinkedHashMap();

	@Autowired
	private DateUtilsService dateUtilsService;

	static {
		fields.put( 1, TABLE_RESTRICTION_COLUMN_ENTRY_ID );
		fields.put( 2, TABLE_RESTRICTION_COLUMN_RESTRICTION_TYPE_ID );
		fields.put( 3, TABLE_RESTRICTION_COLUMN_RESTRICTION_TIME_FROM );
		fields.put( 4, TABLE_RESTRICTION_COLUMN_RESTRICTION_TIME_TO );
		fields.put( 5, TABLE_RESTRICTION_COLUMN_RESTRICTION_MESSAGE );
		fields.put( 6, TABLE_RESTRICTION_COLUMN_RESTRICTION_COMMENT );
	}

	static {
		updatableFields.put( 3, TABLE_RESTRICTION_COLUMN_RESTRICTION_TIME_FROM );
		updatableFields.put( 4, TABLE_RESTRICTION_COLUMN_RESTRICTION_TIME_TO );
		updatableFields.put( 5, TABLE_RESTRICTION_COLUMN_RESTRICTION_MESSAGE );
		updatableFields.put( 6, TABLE_RESTRICTION_COLUMN_RESTRICTION_COMMENT );
	}

	@Override
	public boolean saveToDB( final EntryRestriction entry ) {
		return createOrUpdateEntry( entry, fields, updatableFields );
	}

	@Override
	public List<EntryRestriction> loadRestrictions( final int entryId, final RestrictionType restrictionType ) {
		final String sql = String.format( "SELECT * FROM %s WHERE %s=:entryId AND %s=:restrictionTypeId ORDER BY %s;"
			, TABLE_RESTRICTION, TABLE_RESTRICTION_COLUMN_ENTRY_ID, TABLE_RESTRICTION_COLUMN_RESTRICTION_TYPE_ID, TABLE_RESTRICTION_COLUMN_RESTRICTION_TIME_FROM );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "entryId", entryId );
		paramSource.addValue( "restrictionTypeId", restrictionType.getId() );

		return jdbcTemplate.query( sql, paramSource, getRowMapper() );
	}

	@Override
	public boolean isRestrictedNow( final int entryId, final RestrictionType restrictionType, final Date time ) {
		final List<EntryRestriction> restrictions = loadRestrictions( entryId, restrictionType );

		return restrictions != null && restrictions.get( restrictions.size() - 1 ).getRestrictionTimeTo().getTime() >= time.getTime();
	}

	@Override
	protected MapSqlParameterSource getParameters( final EntryRestriction entry ) {
		final MapSqlParameterSource paramSource = new MapSqlParameterSource();

		paramSource.addValue( TABLE_RESTRICTION_COLUMN_ENTRY_ID, entry.getEntryId() );
		paramSource.addValue( TABLE_RESTRICTION_COLUMN_RESTRICTION_TYPE_ID, entry.getRestrictionType().getId() );
		paramSource.addValue( TABLE_RESTRICTION_COLUMN_RESTRICTION_TIME_FROM, entry.getRestrictionTimeFrom() );
		paramSource.addValue( TABLE_RESTRICTION_COLUMN_RESTRICTION_TIME_TO, entry.getRestrictionTimeTo() );
		paramSource.addValue( TABLE_RESTRICTION_COLUMN_RESTRICTION_MESSAGE, entry.getRestrictionMessage() );
		paramSource.addValue( TABLE_RESTRICTION_COLUMN_RESTRICTION_COMMENT, entry.getRestrictionRestrictionComment() );

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

	private class EntryRestrictionRowMapper implements RowMapper<EntryRestriction> {

		@Override
		public EntryRestriction mapRow( final ResultSet rs, final int rowNum ) throws SQLException {

			final int id = rs.getInt( BaseEntityDao.ENTITY_ID );
			final int entryId = rs.getInt( TABLE_RESTRICTION_COLUMN_ENTRY_ID );
			final RestrictionType restrictionType = RestrictionType.getById( rs.getInt( TABLE_RESTRICTION_COLUMN_RESTRICTION_TYPE_ID ) );

			final EntryRestriction result = new EntryRestriction( entryId, restrictionType );
			result.setId( id );

			result.setRestrictionTimeFrom( rs.getTimestamp( TABLE_RESTRICTION_COLUMN_RESTRICTION_TIME_FROM ) );
			result.setRestrictionTimeTo( rs.getTimestamp( TABLE_RESTRICTION_COLUMN_RESTRICTION_TIME_TO ) );

			result.setRestrictionMessage( rs.getString( TABLE_RESTRICTION_COLUMN_RESTRICTION_MESSAGE ) );
			result.setRestrictionRestrictionComment( rs.getString( TABLE_RESTRICTION_COLUMN_RESTRICTION_COMMENT ) );

			return result;
		}
	}
}
