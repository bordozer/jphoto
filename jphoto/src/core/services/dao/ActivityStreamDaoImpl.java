package core.services.dao;

import core.general.activity.*;
import core.general.photo.Photo;
import core.general.user.User;
import core.log.LogHelper;
import core.services.photo.PhotoService;
import core.services.security.Services;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newLinkedHashMap;

public class ActivityStreamDaoImpl extends BaseEntityDaoImpl<AbstractActivityStreamEntry> implements ActivityStreamDao {

	public static final String TABLE_ACTIVITY_STREAM = "activityStream";

	public static final String TABLE_ACTIVITY_STREAM_COL_ACTIVITY_TYPE = "activityType";
	public static final String TABLE_ACTIVITY_STREAM_COL_ACTIVITY_TIME = "activityTime";
	public static final String TABLE_ACTIVITY_STREAM_COL_USER_ID = "userId";
	public static final String TABLE_ACTIVITY_STREAM_COL_PHOTO_ID = "photoId";
	public static final String TABLE_ACTIVITY_STREAM_COL_ACTIVITY_XML = "activityXML";

	final LogHelper log = new LogHelper( ActivityStreamDaoImpl.class );

	@Autowired
	private Services services;

	@Autowired
	private UserDao userDao;

	@Autowired
	private PhotoService photoService;

	public static final Map<Integer, String> fields = newLinkedHashMap();

	static {
		fields.put( 1, TABLE_ACTIVITY_STREAM_COL_ACTIVITY_TIME );
		fields.put( 2, TABLE_ACTIVITY_STREAM_COL_ACTIVITY_TYPE );
		fields.put( 3, TABLE_ACTIVITY_STREAM_COL_USER_ID );
		fields.put( 4, TABLE_ACTIVITY_STREAM_COL_PHOTO_ID );
		fields.put( 5, TABLE_ACTIVITY_STREAM_COL_ACTIVITY_XML );
	}

	@Override
	public List<AbstractActivityStreamEntry> getActivityForPeriod( final Date dateFrom, final Date dateTo ) {
		final String sql = String.format( "SELECT * FROM %1$s WHERE %2$s >= :timeFrom AND %2$s <= :timeTo ORDER BY %2$s DESC;", TABLE_ACTIVITY_STREAM, TABLE_ACTIVITY_STREAM_COL_ACTIVITY_TIME );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "timeFrom", dateFrom );
		paramSource.addValue( "timeTo", dateTo );

		return jdbcTemplate.query( sql, paramSource, getRowMapper() );
	}

	@Override
	public List<AbstractActivityStreamEntry> getLastActivities( final int qty ) {
		final String sql = String.format( "SELECT * FROM %s ORDER BY %s DESC LIMIT %d;", TABLE_ACTIVITY_STREAM, TABLE_ACTIVITY_STREAM_COL_ACTIVITY_TIME, qty );

		return jdbcTemplate.query( sql, new MapSqlParameterSource(), getRowMapper() );
	}

	@Override
	public List<AbstractActivityStreamEntry> getUserActivities( final int userId ) {
		final String sql = String.format( "SELECT * FROM %s WHERE %s=:userId ORDER BY %s DESC;", TABLE_ACTIVITY_STREAM, TABLE_ACTIVITY_STREAM_COL_USER_ID, TABLE_ACTIVITY_STREAM_COL_ACTIVITY_TIME );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "userId", userId );

		return jdbcTemplate.query( sql, paramSource, getRowMapper() );
	}

	@Override
	public List<AbstractActivityStreamEntry> getUserLastActivities( final int userId, final int qty ) {
		final String sql = String.format( "SELECT * FROM %s WHERE %s=:userId ORDER BY %s DESC LIMIT %d;", TABLE_ACTIVITY_STREAM, TABLE_ACTIVITY_STREAM_COL_USER_ID, TABLE_ACTIVITY_STREAM_COL_ACTIVITY_TIME, qty );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "userId", userId );

		return jdbcTemplate.query( sql, paramSource, getRowMapper() );
	}

	@Override
	public List<AbstractActivityStreamEntry> getActivityForPhoto( final int photoId ) {
		final String sql = String.format( "SELECT * FROM %s WHERE %s=:photoId;", TABLE_ACTIVITY_STREAM, TABLE_ACTIVITY_STREAM_COL_PHOTO_ID );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "photoId", photoId );

		return jdbcTemplate.query( sql, paramSource, getRowMapper() );
	}

	@Override
	public void deleteEntriesOlderThen( final Date timeFrame ) {
		final String sql = String.format( "DELETE FROM %s WHERE %s < :timeFrame;", TABLE_ACTIVITY_STREAM, TABLE_ACTIVITY_STREAM_COL_ACTIVITY_TIME );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "timeFrame", timeFrame );

		jdbcTemplate.update( sql, paramSource );
	}

	@Override
	public boolean saveToDB( final AbstractActivityStreamEntry entry ) {
		return createOrUpdateEntry( entry, fields, fields );
	}

	@Override
	protected MapSqlParameterSource getParameters( final AbstractActivityStreamEntry entry ) {
		final MapSqlParameterSource paramSource = new MapSqlParameterSource();

		paramSource.addValue( TABLE_ACTIVITY_STREAM_COL_USER_ID, entry.getActivityOfUser().getId() );
		paramSource.addValue( TABLE_ACTIVITY_STREAM_COL_PHOTO_ID, entry.getActivityOfPhotoId() );
		paramSource.addValue( TABLE_ACTIVITY_STREAM_COL_ACTIVITY_TIME, entry.getActivityTime() );
		paramSource.addValue( TABLE_ACTIVITY_STREAM_COL_ACTIVITY_TYPE, entry.getActivityType().getId() );
		paramSource.addValue( TABLE_ACTIVITY_STREAM_COL_ACTIVITY_XML, entry.getActivityXML().asXML() );

		return paramSource;
	}

	@Override
	protected String getTableName() {
		return TABLE_ACTIVITY_STREAM;
	}

	@Override
	protected RowMapper<AbstractActivityStreamEntry> getRowMapper() {
		return new ActivityStreamEntryMapper();
	}

	private class ActivityStreamEntryMapper implements RowMapper<AbstractActivityStreamEntry> {

		@Override
		public AbstractActivityStreamEntry mapRow( final ResultSet rs, final int rowNum ) throws SQLException {

			final int userId = rs.getInt( TABLE_ACTIVITY_STREAM_COL_USER_ID );
			final int photoId = rs.getInt( TABLE_ACTIVITY_STREAM_COL_PHOTO_ID );
			final ActivityType activityType = ActivityType.getById( rs.getInt( TABLE_ACTIVITY_STREAM_COL_ACTIVITY_TYPE ) );
			final Date activityTime = rs.getTimestamp( TABLE_ACTIVITY_STREAM_COL_ACTIVITY_TIME );
			final String activityXML = rs.getString( TABLE_ACTIVITY_STREAM_COL_ACTIVITY_XML );

			try {
				final AbstractActivityStreamEntry result = getInstance( userId, photoId, activityTime, activityType, activityXML );
				if ( result == null ) {
					return null;
				}
				result.setId( rs.getInt( BaseEntityDao.ENTITY_ID ) );

				return result;
			} catch ( final DocumentException e ) {
				log.error( String.format( "Invalid activity xml: %s", activityXML ), e );
				return null;
			}
		}
	}

	private AbstractActivityStreamEntry getInstance( final int userId, final int photoId, final Date activityTime, final ActivityType activityType, final String activityXML ) throws DocumentException {

		final User user = userDao.load( userId );

		switch ( activityType ) {
			case USER_REGISTRATION:
				return new ActivityUserRegistration( user, services );
			case FAVORITE_ACTION:
				return new ActivityFavoriteAction( user, activityTime, activityXML, services );
			case VOTING_FOR_USER_RANK_IN_GENRE:
				return new ActivityVotingForUserRankInGenre( user, activityTime, activityXML, services );
			case USER_STATUS:
				return new ActivityUserStatusChange( user, activityTime, activityXML, services );
		}

		final Photo photo = photoService.load( photoId );
		if ( photo == null ) {
			return null;
		}

		switch ( activityType ) {
			case PHOTO_UPLOAD:
				return new ActivityPhotoUpload( photo, services );
			case PHOTO_VOTING:
				return new ActivityPhotoVoting( user, photo, activityTime, activityXML, services );
			case PHOTO_COMMENT:
				return new ActivityPhotoComment( user, photo, activityTime, activityXML, services );
			case PHOTO_PREVIEW:
				return new ActivityPhotoPreview( user, photo, activityTime, activityXML, services );
		}

		throw new IllegalArgumentException( String.format( "Illegal ActivityType: %s", activityType ) );
	}
}
