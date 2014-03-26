package core.services.dao;

import core.enums.PhotoActionAllowance;
import core.enums.UserGender;
import core.general.cache.CacheEntryFactory;
import core.general.cache.CacheKey;
import core.general.user.EmailNotificationType;
import core.general.user.User;
import core.general.user.UserMembershipType;
import core.general.user.UserStatus;
import core.services.system.CacheService;
import core.services.translator.Language;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import utils.NumberUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Maps.newLinkedHashMap;
import static com.google.common.collect.Sets.newHashSet;

public class UserDaoImpl extends BaseEntityDaoImpl<User> implements UserDao {

	public final static String TABLE_USERS = "users";

	public final static String TABLE_COLUMN_NAME = "name";
	public final static String TABLE_COLUMN_LOGIN = "login";
	public final static String TABLE_COLUMN_EMAIL = "email";
	public final static String TABLE_COLUMN_HOME_SITE = "homeSite";
	public final static String TABLE_COLUMN_SELF_DESCRIPTION = "selfDescription";
	public final static String TABLE_COLUMN_DATE_OF_BIRTH = "dateOfBirth";
	public final static String TABLE_COLUMN_PHOTOS_IN_LINE = "photosInLine";
	public final static String TABLE_COLUMN_PHOTOS_LINES = "photoLines";
	public final static String TABLE_COLUMN_MEMBERSHIP_TYPE = "membershipType";
	public final static String TABLE_COLUMN_USER_STATUS = "userStatus";
	public final static String TABLE_COLUMN_REGISTER_TIME = "registerTime";
	public final static String TABLE_COLUMN_USER_GENDER = "gender";
	public final static String TABLE_COLUMN_SHOW_NUDE_CONTENT = "showNudeContent";
	public final static String TABLE_COLUMN_LANGUAGE_ID = "languageId";

	public final static String TABLE_COLUMN_EMAIL_NOTIFICATION_OPTIONS = "emailNotificationOptions";

	public final static String TABLE_COLUMN_DEFAULT_COMMENTS_ALLOWANCE_ID = "defaultPhotoCommentsAllowanceId";
	public final static String TABLE_COLUMN_DEFAULT_VOTING_ALLOWANCE_ID = "defaultPhotoVotingAllowanceId";

	private static final Map<Integer, String> fields = newLinkedHashMap();
	private static final Map<Integer, String> updatableFields = newLinkedHashMap();

	@Autowired
	private CacheService<User> cacheService;

	static {
		fields.put( 1, TABLE_COLUMN_NAME );
		fields.put( 2, TABLE_COLUMN_LOGIN );
		fields.put( 3, TABLE_COLUMN_EMAIL );
		fields.put( 4, TABLE_COLUMN_HOME_SITE );
		fields.put( 5, TABLE_COLUMN_DATE_OF_BIRTH );
		fields.put( 6, TABLE_COLUMN_PHOTOS_IN_LINE );
		fields.put( 7, TABLE_COLUMN_PHOTOS_LINES );
		fields.put( 8, TABLE_COLUMN_MEMBERSHIP_TYPE );
		fields.put( 9, TABLE_COLUMN_USER_STATUS );
		fields.put( 10, TABLE_COLUMN_REGISTER_TIME );
		fields.put( 11, TABLE_COLUMN_USER_GENDER );
		fields.put( 12, TABLE_COLUMN_EMAIL_NOTIFICATION_OPTIONS );
		fields.put( 13, TABLE_COLUMN_DEFAULT_COMMENTS_ALLOWANCE_ID );
		fields.put( 14, TABLE_COLUMN_DEFAULT_VOTING_ALLOWANCE_ID );
		fields.put( 15, TABLE_COLUMN_SHOW_NUDE_CONTENT );
		fields.put( 16, TABLE_COLUMN_SELF_DESCRIPTION );
		fields.put( 17, TABLE_COLUMN_LANGUAGE_ID );
	}

	static {
		updatableFields.put( 1, TABLE_COLUMN_NAME );
		updatableFields.put( 2, TABLE_COLUMN_LOGIN );
		updatableFields.put( 3, TABLE_COLUMN_EMAIL );
		updatableFields.put( 4, TABLE_COLUMN_HOME_SITE );
		updatableFields.put( 5, TABLE_COLUMN_DATE_OF_BIRTH );
		updatableFields.put( 6, TABLE_COLUMN_PHOTOS_IN_LINE );
		updatableFields.put( 7, TABLE_COLUMN_PHOTOS_LINES );
		updatableFields.put( 8, TABLE_COLUMN_MEMBERSHIP_TYPE );
		updatableFields.put( 11, TABLE_COLUMN_USER_GENDER );
		updatableFields.put( 12, TABLE_COLUMN_EMAIL_NOTIFICATION_OPTIONS );
		updatableFields.put( 13, TABLE_COLUMN_DEFAULT_COMMENTS_ALLOWANCE_ID );
		updatableFields.put( 14, TABLE_COLUMN_DEFAULT_VOTING_ALLOWANCE_ID );
		updatableFields.put( 15, TABLE_COLUMN_SHOW_NUDE_CONTENT );
		updatableFields.put( 16, TABLE_COLUMN_SELF_DESCRIPTION );
		updatableFields.put( 17, TABLE_COLUMN_LANGUAGE_ID );
	}

	@Override
	protected String getTableName() {
		return TABLE_USERS;
	}

	@Override
	public boolean saveToDB( final User user ) {
		final boolean isSaved = createOrUpdateEntry( user, fields, updatableFields );

		if ( isSaved ) {
			cacheService.expire( CacheKey.USER, user.getId() );
		}

		return isSaved;
	}

	@Override
	public User load( final int entryId ) {
		return cacheService.getEntry( CacheKey.USER, entryId, new CacheEntryFactory<User>() {
			@Override
			public User createEntry() {
				return loadEntryById( entryId, new UserMapper() );
			}
		} );
	}

	@Override
	public User loadByName( final String name ) {
		final String sql = String.format( "SELECT * FROM %s WHERE %s=:name", TABLE_USERS, TABLE_COLUMN_NAME );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "name", name );

		return getEntryOrNull( sql, paramSource, new UserMapper() );
	}

	@Override
	public User loadByLogin( final String userLogin ) {
		final String sql = String.format( "SELECT * FROM %s WHERE %s=:login", TABLE_USERS, TABLE_COLUMN_LOGIN );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "login", userLogin );

		return getEntryOrNull( sql, paramSource, new UserMapper() );
	}

	@Override
	public User loadByEmail( final String userEmail ) {
		final String sql = String.format( "SELECT * FROM %s WHERE %s=:email", TABLE_USERS, TABLE_COLUMN_EMAIL );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "email", userEmail );

		return getEntryOrNull( sql, paramSource, new UserMapper() );
	}

	@Override
	public boolean setUserStatus( final int userId, final UserStatus userStatus ) {
		final String sql = String.format( "UPDATE %s SET %s = :userStatusId WHERE %s=:useId;"
			, TABLE_USERS, TABLE_COLUMN_USER_STATUS, ENTITY_ID );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "userStatusId", userStatus.getId() );
		paramSource.addValue( "useId", userId );

		return jdbcTemplate.update( sql, paramSource ) > 0;
	}

	@Override
	public int getUserCount() {
		final String sql = String.format( "SELECT COUNT( %s ) FROM %s;", ENTITY_ID, TABLE_USERS );

		return getIntValueOrZero( sql, new MapSqlParameterSource() );
	}

	@Override
	public List<User> searchByPartOfName( final String searchString ) {
		final StringBuilder builder = new StringBuilder( "SELECT * FROM " ).append( TABLE_USERS ).append( " WHERE " ).append( TABLE_COLUMN_NAME )
			.append( " LIKE '%" ).append( searchString ).append( "%'" ).append( " ORDER BY " ).append( TABLE_COLUMN_NAME );

		return jdbcTemplate.query( builder.toString(), new MapSqlParameterSource(), new UserMapper() );
	}

	@Override
	public List<User> loadAll() {
		// this is used only for technical functionality!
		final String sql = String.format( "SELECT * FROM %s ORDER BY %s", TABLE_USERS, TABLE_COLUMN_NAME );

		return jdbcTemplate.query( sql, new MapSqlParameterSource(), new UserMapper() );
	}

	@Override
	protected MapSqlParameterSource getParameters( final User entry ) {
		final MapSqlParameterSource paramSource = new MapSqlParameterSource();

		paramSource.addValue( TABLE_COLUMN_NAME, entry.getName() );
		paramSource.addValue( TABLE_COLUMN_LOGIN, entry.getLogin() );
		paramSource.addValue( TABLE_COLUMN_EMAIL, entry.getEmail() );
		paramSource.addValue( TABLE_COLUMN_HOME_SITE, entry.getHomeSite() );
		paramSource.addValue( TABLE_COLUMN_SELF_DESCRIPTION, entry.getSelfDescription() );
		paramSource.addValue( TABLE_COLUMN_DATE_OF_BIRTH, entry.getDateOfBirth() );
		paramSource.addValue( TABLE_COLUMN_PHOTOS_IN_LINE, entry.getPhotosInLine() );
		paramSource.addValue( TABLE_COLUMN_PHOTOS_LINES, entry.getPhotoLines() );
		paramSource.addValue( TABLE_COLUMN_MEMBERSHIP_TYPE, entry.getMembershipType().getId() );
		paramSource.addValue( TABLE_COLUMN_USER_STATUS, entry.getUserStatus().getId() );
		paramSource.addValue( TABLE_COLUMN_REGISTER_TIME, entry.getRegistrationTime() );
		paramSource.addValue( TABLE_COLUMN_USER_GENDER, entry.getGender().getId() );
		paramSource.addValue( TABLE_COLUMN_LANGUAGE_ID, entry.getLanguage().getId() );

		final Set<EmailNotificationType> emailNotificationTypes = entry.getEmailNotificationTypes();
		if ( emailNotificationTypes != null ) {
			final Set<Integer> emailNotificationOptionIds = newHashSet();
			for ( final EmailNotificationType emailNotificationType : emailNotificationTypes ) {
				emailNotificationOptionIds.add( emailNotificationType.getId() );
			}
			paramSource.addValue( TABLE_COLUMN_EMAIL_NOTIFICATION_OPTIONS, StringUtils.join( emailNotificationOptionIds, "," ) );
		} else {
			paramSource.addValue( TABLE_COLUMN_EMAIL_NOTIFICATION_OPTIONS, StringUtils.EMPTY );
		}

		paramSource.addValue( TABLE_COLUMN_DEFAULT_COMMENTS_ALLOWANCE_ID, entry.getDefaultPhotoCommentsAllowance().getId() );
		paramSource.addValue( TABLE_COLUMN_DEFAULT_VOTING_ALLOWANCE_ID, entry.getDefaultPhotoVotingAllowance().getId() );

		paramSource.addValue( TABLE_COLUMN_SHOW_NUDE_CONTENT, entry.isShowNudeContent() );

		return paramSource;
	}

	@Override
	protected RowMapper<User> getRowMapper() {
		return new UserMapper();
	}

	@Override
	public boolean delete( final int entryId ) {
		throw new IllegalArgumentException( "User can not be deleted" );
	}

	private class UserMapper implements RowMapper<User> {

		@Override
		public User mapRow( final ResultSet rs, final int rowNum ) throws SQLException {
			final int userId = rs.getInt( BaseEntityDao.ENTITY_ID );

			final User result = new User();

			result.setId( userId );
			result.setName( rs.getString( TABLE_COLUMN_NAME ) );
			result.setLogin( rs.getString( TABLE_COLUMN_LOGIN ) );
			result.setEmail( rs.getString( TABLE_COLUMN_EMAIL ) );
			result.setDateOfBirth( rs.getDate( TABLE_COLUMN_DATE_OF_BIRTH ) );
			result.setHomeSite( rs.getString( TABLE_COLUMN_HOME_SITE ) );
			result.setSelfDescription( rs.getString( TABLE_COLUMN_SELF_DESCRIPTION ) );
			result.setPhotosInLine( rs.getInt( TABLE_COLUMN_PHOTOS_IN_LINE ) );
			result.setPhotoLines( rs.getInt( TABLE_COLUMN_PHOTOS_LINES ) );

			int membershipTypeId = rs.getInt( TABLE_COLUMN_MEMBERSHIP_TYPE );
			result.setMembershipType( UserMembershipType.getById( membershipTypeId ) );

			result.setUserStatus( UserStatus.getById( rs.getInt( TABLE_COLUMN_USER_STATUS ) ) );
			result.setRegistrationTime( rs.getTimestamp( TABLE_COLUMN_REGISTER_TIME ) );
			result.setGender( UserGender.getById( rs.getInt( TABLE_COLUMN_USER_GENDER ) ) );

			final Set<EmailNotificationType> emailNotificationTypes = newHashSet();
			final String[] emailNotificationOptionArray = rs.getString( TABLE_COLUMN_EMAIL_NOTIFICATION_OPTIONS ).split( "," );
			for ( final String id : emailNotificationOptionArray ) {
				final EmailNotificationType optionEmail = EmailNotificationType.getById( NumberUtils.convertToInt( id ) );
				if ( optionEmail != null ) {
					emailNotificationTypes.add( optionEmail );
				}
			}
			result.setEmailNotificationTypes( emailNotificationTypes );
			result.setDefaultPhotoCommentsAllowance( PhotoActionAllowance.getById( rs.getInt( TABLE_COLUMN_DEFAULT_COMMENTS_ALLOWANCE_ID ) ) );
			result.setDefaultPhotoVotingAllowance( PhotoActionAllowance.getById( rs.getInt( TABLE_COLUMN_DEFAULT_VOTING_ALLOWANCE_ID ) ) );
			result.setShowNudeContent( rs.getBoolean( TABLE_COLUMN_SHOW_NUDE_CONTENT ) );

			result.setLanguage( Language.getById( rs.getInt( TABLE_COLUMN_LANGUAGE_ID ) ) );

			return result;
		}
	}

}
