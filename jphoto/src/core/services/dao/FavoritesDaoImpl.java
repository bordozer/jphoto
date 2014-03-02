package core.services.dao;

import core.enums.FavoriteEntryType;
import core.general.favorite.FavoriteEntry;
import core.services.dao.mappers.IdsRowMapper;
import core.services.photo.PhotoService;
import core.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newLinkedHashMap;

public class FavoritesDaoImpl extends BaseEntityDaoImpl<FavoriteEntry> implements FavoritesDao {

	public final static String TABLE_FAVORITES = "favorites";

	public final static String TABLE_COLUMN_USER_ID = "userId";
	public final static String TABLE_COLUMN_FAVORITE_ENTRY_ID = "favoriteEntryId";
	public final static String TABLE_COLUMN_CREATED = "created";
	public final static String TABLE_COLUMN_ENTRY_TYPE = "entryType";

	@Autowired
	private UserService userService;

	@Autowired
	private PhotoService photoService;

	public static final Map<Integer, String> fields = newLinkedHashMap();

	static {
		fields.put( 1, TABLE_COLUMN_USER_ID );
		fields.put( 2, TABLE_COLUMN_FAVORITE_ENTRY_ID );
		fields.put( 3, TABLE_COLUMN_CREATED );
		fields.put( 4, TABLE_COLUMN_ENTRY_TYPE );
	}

	@Override
	public FavoriteEntry getFavoriteEntry( final int userId, final int favoriteEntryId, final FavoriteEntryType entryType ) {
		final String sql = String.format( "SELECT * FROM %s WHERE %s=:userId AND %s=:favoriteEntryId AND %s=:entryType;"
			, TABLE_FAVORITES, TABLE_COLUMN_USER_ID, TABLE_COLUMN_FAVORITE_ENTRY_ID, TABLE_COLUMN_ENTRY_TYPE );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "userId", userId );
		paramSource.addValue( "favoriteEntryId", favoriteEntryId );
		paramSource.addValue( "entryType", entryType.getId() );

		return getEntryOrNull( sql, paramSource, new PhotoFavoriteMapper() );
	}

	@Override
	public boolean addEntryToFavorites( final int userId, final int favoriteEntryId, final Date time, final FavoriteEntryType entryType ) {
		final String sql = String.format( "INSERT INTO %s ( %s, %s, %s, %s ) VALUES ( :userId, :favoriteEntryId, :created, :entryType );"
			, TABLE_FAVORITES, TABLE_COLUMN_USER_ID, TABLE_COLUMN_FAVORITE_ENTRY_ID, TABLE_COLUMN_CREATED, TABLE_COLUMN_ENTRY_TYPE );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "userId", userId );
		paramSource.addValue( "favoriteEntryId", favoriteEntryId );
		paramSource.addValue( "created", time );
		paramSource.addValue( "entryType", entryType.getId() );

		return jdbcTemplate.update( sql, paramSource ) > 0;
	}

	@Override
	public boolean removeEntryFromFavorites( final int userId, final int favoriteEntryId, final FavoriteEntryType entryType ) {
		final String sql = String.format( "DELETE FROM %s WHERE %s=:userId AND %s=:favoriteEntryId AND %s=:entryType;"
			, TABLE_FAVORITES, TABLE_COLUMN_USER_ID, TABLE_COLUMN_FAVORITE_ENTRY_ID, TABLE_COLUMN_ENTRY_TYPE );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "userId", userId );
		paramSource.addValue( "favoriteEntryId", favoriteEntryId );
		paramSource.addValue( "entryType", entryType.getId() );

		return jdbcTemplate.update( sql, paramSource ) > 0;
	}

	@Override
	public int getFavoritePhotosQty( final int userId ) {
		return getFavoriteEntriesQty( userId, FavoriteEntryType.PHOTO );
	}

	@Override
	public int getBookmarkedPhotosQty( final int userId ) {
		return getFavoriteEntriesQty( userId, FavoriteEntryType.BOOKMARK );
	}

	@Override
	public int getUsersQtyWhoAddedInFavoriteMembers( final int userId ) {
		final String sql = String.format( "SELECT COUNT( %s ) FROM %s WHERE %s=:userId AND %s=:entryType;"
			, BaseEntityDao.ENTITY_ID
			, TABLE_FAVORITES
			, TABLE_COLUMN_FAVORITE_ENTRY_ID
			, TABLE_COLUMN_ENTRY_TYPE
		);

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "userId", userId );
		paramSource.addValue( "entryType", FavoriteEntryType.USER.getId() );

		return getIntValueOrZero( sql, paramSource );
	}

	@Override
	public int getFriendsQty( final int userId ) {
		return getFavoriteEntriesQty( userId, FavoriteEntryType.FRIEND );
	}

	@Override
	public int getFavoriteMembersQty( final int userId ) {
		return getFavoriteEntriesQty( userId, FavoriteEntryType.USER );
	}

	@Override
	public int getBackListEntriesQty( final int userId ) {
		return getFavoriteEntriesQty( userId, FavoriteEntryType.BLACKLIST );
	}

	@Override
	public int getNotificationsAboutNewPhotosQty( final int userId ) {
		return getFavoriteEntriesQty( userId, FavoriteEntryType.NEW_PHOTO_NOTIFICATION );
	}

	@Override
	public int getNotificationsAboutNewCommentsQty( final int userId ) {
		return getFavoriteEntriesQty( userId, FavoriteEntryType.NEW_COMMENTS_NOTIFICATION );
	}

	@Override
	public List<Integer> getAllUsersIdsWhoHasThisEntryInFavorites( final int favoriteEntryId, final FavoriteEntryType favoriteEntryType ) {
		final String sql = String.format( "SELECT %s FROM %s WHERE %s=:favoriteEntryId AND %s=:entryType;"
			, TABLE_COLUMN_USER_ID, TABLE_FAVORITES, TABLE_COLUMN_FAVORITE_ENTRY_ID, TABLE_COLUMN_ENTRY_TYPE );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "favoriteEntryId", favoriteEntryId );
		paramSource.addValue( "entryType", favoriteEntryType.getId() );

		return jdbcTemplate.query( sql, paramSource, new IdsRowMapper() );
	}

	private int getFavoriteEntriesQty( final int userId, final FavoriteEntryType entryType ) {
		final String sql = String.format( "SELECT COUNT( %s ) FROM %s WHERE %s=:userId AND %s=:entryType;"
			, BaseEntityDao.ENTITY_ID
			, TABLE_FAVORITES
			, TABLE_COLUMN_USER_ID
			, TABLE_COLUMN_ENTRY_TYPE
		);

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "userId", userId );

		paramSource.addValue( "entryType", entryType.getId() );

		return getIntValueOrZero( sql, paramSource );
	}

	@Override
	public boolean saveToDB( final FavoriteEntry entry ) {
		return createOrUpdateEntry( entry, fields, fields );
	}

	@Override
	protected MapSqlParameterSource getParameters( final FavoriteEntry entry ) {
		final MapSqlParameterSource paramSource = new MapSqlParameterSource();

		paramSource.addValue( TABLE_COLUMN_USER_ID, entry.getUser().getId() );
		paramSource.addValue( TABLE_COLUMN_FAVORITE_ENTRY_ID, entry.getFavoriteEntry().getId() );
		paramSource.addValue( TABLE_COLUMN_CREATED, entry.getCreated() );
		paramSource.addValue( TABLE_COLUMN_ENTRY_TYPE, entry.getFavoriteEntry().getId() );

		return paramSource;
	}

	@Override
	protected String getTableName() {
		return TABLE_FAVORITES;
	}

	@Override
	protected RowMapper<FavoriteEntry> getRowMapper() {
		return new PhotoFavoriteMapper();
	}

	private class PhotoFavoriteMapper implements RowMapper<FavoriteEntry> {

		@Override
		public FavoriteEntry mapRow( final ResultSet rs, final int rowNum ) throws SQLException {
			final FavoriteEntry result = new FavoriteEntry();

			result.setId( rs.getInt( BaseEntityDao.ENTITY_ID ) );

			final int userId = rs.getInt( TABLE_COLUMN_USER_ID );
			result.setUser( userService.load( userId ) );

			result.setCreated( rs.getTimestamp( TABLE_COLUMN_CREATED ) );

			final FavoriteEntryType entryType = FavoriteEntryType.getById( rs.getInt( TABLE_COLUMN_ENTRY_TYPE ) );
			result.setEntryType( entryType );

			final int favoriteEntryId = rs.getInt( TABLE_COLUMN_FAVORITE_ENTRY_ID );
			result.setFavoriteEntry( FavoritableFactory.createEntry( favoriteEntryId, entryType, userService, photoService ) );

			return result;
		}
	}

}
