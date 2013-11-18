package core.services.dao;

import core.enums.UserTeamMemberType;
import core.general.cache.CacheEntryFactory;
import core.general.cache.CacheKey;
import core.general.photo.Photo;
import core.general.photoTeam.PhotoTeam;
import core.general.photoTeam.PhotoTeamMember;
import core.general.user.User;
import core.general.user.userTeam.UserTeamMember;
import core.log.LogHelper;
import core.services.system.CacheService;
import core.services.dao.mappers.IdsRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newLinkedHashMap;

public class UserTeamMemberDaoImpl extends BaseEntityDaoImpl<UserTeamMember> implements UserTeamMemberDao {

	public static final String TABLE_USER_TEAM = "userTeam";
	public static final String TABLE_USER_TEAM_COL_USER_ID = "userId";
	public static final String TABLE_USER_TEAM_COL_TEAM_MEMBER_USER_ID = "teamMemberUserId";
	public static final String TABLE_USER_TEAM_COL_TEAM_MEMBER_NAME = "teamMemberName";
	public static final String TABLE_USER_TEAM_COL_TEAM_MEMBER_TYPE_ID = "teamMemberTypeId";

	public static final String TABLE_PHOTO_TEAM = "photoTeam";
	public static final String TABLE_PHOTO_TEAM_COL_PHOTO_ID = "photoId";
	public static final String TABLE_PHOTO_TEAM_COL_USER_TEAM_MEMBER_ID = "userTeamMemberId";
	public static final String TABLE_PHOTO_TEAM_COL_DESCRIPTION = "description";

	public static final Map<Integer, String> fields = newLinkedHashMap();

	@Autowired
	private UserDao userDao;

	@Autowired
	private PhotoDao photoDao;

	@Autowired
	private CacheService<UserTeamMember> cacheService;

	private final LogHelper log = new LogHelper( UserTeamMemberDaoImpl.class );

	static {
		fields.put( 1, TABLE_USER_TEAM_COL_USER_ID );
		fields.put( 2, TABLE_USER_TEAM_COL_TEAM_MEMBER_USER_ID );
		fields.put( 3, TABLE_USER_TEAM_COL_TEAM_MEMBER_NAME );
		fields.put( 4, TABLE_USER_TEAM_COL_TEAM_MEMBER_TYPE_ID );
	}

	@Override
	protected String getTableName() {
		return TABLE_USER_TEAM;
	}

	@Override
	public boolean saveToDB( final UserTeamMember userTeamMember ) {
		final boolean isSaved = createOrUpdateEntry( userTeamMember, fields, fields );

		if ( isSaved ) {
			cacheService.expire( CacheKey.USER_TEAM_MEMBER, userTeamMember.getId() );
		}

		return isSaved;
	}

	@Override
	public UserTeamMember load( final int userTeamMemberId ) {
		final UserTeamMember userTeamMember = cacheService.getEntry( CacheKey.USER_TEAM_MEMBER, userTeamMemberId, new CacheEntryFactory<UserTeamMember>() {
			@Override
			public UserTeamMember createEntry() {
				return loadEntryById( userTeamMemberId, new UserTeamMemberMapper() );
			}
		} );

		if ( userTeamMember == null ) {
			return null;
		}

		userTeamMember.setUser( userDao.load( userTeamMember.getUser().getId() ) );

		final User teamMemberUser = userTeamMember.getTeamMemberUser();
		if ( teamMemberUser != null ) {
			userTeamMember.setTeamMemberUser( userDao.load( teamMemberUser.getId() ) );
		}

		return userTeamMember;
	}

	@Override
	protected RowMapper<UserTeamMember> getRowMapper() {
		return new UserTeamMemberMapper();
	}

	@Override
	public boolean delete( final int entryId ) {
		final boolean isDeleted = deleteEntryById( entryId );

		if ( isDeleted ) {
			cacheService.expire( CacheKey.USER_TEAM_MEMBER, entryId );
		}

		return isDeleted;
	}

	@Override
	protected MapSqlParameterSource getParameters( final UserTeamMember entry ) {
		final MapSqlParameterSource paramSource = new MapSqlParameterSource();

		paramSource.addValue( TABLE_USER_TEAM_COL_USER_ID, entry.getUser().getId() );

		final User teamMemberUser = entry.getTeamMemberUser();
		if ( teamMemberUser != null ) {
			paramSource.addValue( TABLE_USER_TEAM_COL_TEAM_MEMBER_USER_ID, teamMemberUser.getId() );
		} else {
			paramSource.addValue( TABLE_USER_TEAM_COL_TEAM_MEMBER_USER_ID, 0 );
		}

		paramSource.addValue( TABLE_USER_TEAM_COL_TEAM_MEMBER_NAME, entry.getName() );
		paramSource.addValue( TABLE_USER_TEAM_COL_TEAM_MEMBER_TYPE_ID, entry.getTeamMemberType().getId() );

		return paramSource;
	}

	@Override
	public List<Integer> loadUserTeamMembersIds( final int userId ) {
		final String sql = String.format( "SELECT %s FROM %s WHERE %s=:userId ORDER BY %s;", ENTITY_ID, TABLE_USER_TEAM, TABLE_USER_TEAM_COL_USER_ID, TABLE_USER_TEAM_COL_TEAM_MEMBER_NAME );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "userId", userId );

		return jdbcTemplate.query( sql, paramSource, new IdsRowMapper() );
	}

	@Override
	public UserTeamMember loadUserTeamMemberByName( final int userId, final String name ) {
		final String sql = String.format( "SELECT * FROM %s WHERE %s=:userId AND %s=:name;", TABLE_USER_TEAM, TABLE_USER_TEAM_COL_USER_ID, TABLE_USER_TEAM_COL_TEAM_MEMBER_NAME );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "userId", userId );
		paramSource.addValue( "name", name );

		return getEntryOrNull( sql, paramSource, new UserTeamMemberMapper() );
	}

	@Override
	public boolean savePhotoTeam( final PhotoTeam photoTeam ) {
		final int photoId = photoTeam.getPhoto().getId();

		deletePhotoTeam( photoId );

		final List<PhotoTeamMember> photoTeamMembers = photoTeam.getPhotoTeamMembers();
		for ( final PhotoTeamMember photoTeamMember : photoTeamMembers ) {

			final int userTeamMemberId = photoTeamMember.getUserTeamMember().getId();
			final String description = photoTeamMember.getDescription();

			final String sql = String.format( "INSERT INTO %s ( %s, %s, %s ) VALUES( :photoId, :userTeamMemberId, :description );"
				, TABLE_PHOTO_TEAM, TABLE_PHOTO_TEAM_COL_PHOTO_ID, TABLE_PHOTO_TEAM_COL_USER_TEAM_MEMBER_ID, TABLE_PHOTO_TEAM_COL_DESCRIPTION );

			final MapSqlParameterSource paramSource = new MapSqlParameterSource();
			paramSource.addValue( "photoId", photoId );
			paramSource.addValue( "userTeamMemberId", userTeamMemberId );
			paramSource.addValue( "description", description );

			if ( jdbcTemplate.update( sql, paramSource ) == 0 ) {
				deletePhotoTeam( photoId );
				log.error( String.format( "Error adding user team member to photo team. PhotoId: %d, userTeamMemberId: %d", photoId, userTeamMemberId ) );

				return false;
			}
		}

		return true;
	}

	@Override
	public void deletePhotoTeam( final int photoId ) {
		final String sql = String.format( "DELETE FROM %s WHERE %s=:photoId;", TABLE_PHOTO_TEAM, TABLE_PHOTO_TEAM_COL_PHOTO_ID );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "photoId", photoId );

		jdbcTemplate.update( sql, paramSource );
	}

	@Override
	public PhotoTeam getPhotoTeam( final int photoId ) {
		final String sql = String.format( "SELECT * FROM %s WHERE %s=:photoId;", TABLE_PHOTO_TEAM, TABLE_PHOTO_TEAM_COL_PHOTO_ID );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "photoId", photoId );

		final Photo photo = photoDao.load( photoId );
		final List<PhotoTeamMember> photoTeamMembers = jdbcTemplate.query( sql, paramSource, new PhotoTeamMemberMapper() );

		return new PhotoTeam( photo, photoTeamMembers );
	}

	@Override
	public int getTeamMemberPhotosQty( final int userTeamMemberId ) {
		final String sql = String.format( "SELECT COUNT(%s) AS %s FROM %s WHERE %s=:userTeamMemberId;"
			, TABLE_PHOTO_TEAM_COL_PHOTO_ID, ENTITY_ID, TABLE_PHOTO_TEAM, TABLE_PHOTO_TEAM_COL_USER_TEAM_MEMBER_ID );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "userTeamMemberId", userTeamMemberId );

		return getEntryOrNull( sql, paramSource, new IdsRowMapper() );
	}

	@Override
	public boolean isTeamMemberAssignedToPhoto( final int photoId, final int teamMemberId ) {
		final String sql = String.format( "SELECT 1 FROM %s WHERE %s=:photoId AND %s = :teamMemberId;"
			, TABLE_PHOTO_TEAM
			, TABLE_PHOTO_TEAM_COL_PHOTO_ID
			, TABLE_PHOTO_TEAM_COL_USER_TEAM_MEMBER_ID
		);

		final MapSqlParameterSource paramSource = new MapSqlParameterSource(  );
		paramSource.addValue( "photoId", photoId );
		paramSource.addValue( "teamMemberId", teamMemberId );

		return existsInt( sql, paramSource );
	}

	private class UserTeamMemberMapper implements RowMapper<UserTeamMember> {

		@Override
		public UserTeamMember mapRow( final ResultSet rs, final int rowNum ) throws SQLException {
			final UserTeamMember result = new UserTeamMember();

			result.setId( rs.getInt( ENTITY_ID ) );
			result.setUser( userDao.load( rs.getInt( TABLE_USER_TEAM_COL_USER_ID ) ) );

			final int userTeamMemberUserId = rs.getInt( TABLE_USER_TEAM_COL_TEAM_MEMBER_USER_ID );
			if ( userTeamMemberUserId > 0) {
				result.setTeamMemberUser( userDao.load( userTeamMemberUserId ) );
			}

			result.setName( rs.getString( TABLE_USER_TEAM_COL_TEAM_MEMBER_NAME ) );
			result.setTeamMemberType( UserTeamMemberType.getById( rs.getInt( TABLE_USER_TEAM_COL_TEAM_MEMBER_TYPE_ID ) ) );

			return result;
		}
	}

	private class PhotoTeamMemberMapper implements RowMapper<PhotoTeamMember> {


		@Override
		public PhotoTeamMember mapRow( final ResultSet rs, final int rowNum ) throws SQLException {
			final PhotoTeamMember result = new PhotoTeamMember();

			final UserTeamMember userTeamMember = load( rs.getInt( TABLE_PHOTO_TEAM_COL_USER_TEAM_MEMBER_ID ) );
			result.setUserTeamMember( userTeamMember );
			result.setDescription( rs.getString( TABLE_PHOTO_TEAM_COL_DESCRIPTION ) );

			return result;
		}
	}
}
