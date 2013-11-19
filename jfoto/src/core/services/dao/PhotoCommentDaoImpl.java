package core.services.dao;

import core.general.cache.CacheEntryFactory;
import core.general.cache.CacheKey;
import core.general.photo.PhotoComment;
import core.services.system.CacheService;
import core.services.user.UserService;
import core.services.dao.mappers.IdsRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newLinkedHashMap;

public class PhotoCommentDaoImpl extends BaseEntityDaoImpl<PhotoComment> implements PhotoCommentDao {

	public final static String TABLE_COMMENTS = "comments";

	public final static String TABLE_COLUMN_PHOTO_ID = "photoId";
	public final static String TABLE_COLUMN_AUTHOR_ID = "authorId";
	public final static String TABLE_COLUMN_REPLY_TO_COMMENT_ID = "replyToCommentId";
	public final static String TABLE_COLUMN_COMMENT_TEXT = "commentText";
	public final static String TABLE_COLUMN_CREATION_TIME = "creationTime";
	public final static String TABLE_COLUMN_READ_TIME = "readtime";
	public final static String TABLE_COLUMN_DELETED = "deleted";

	@Autowired
	private UserService userService;

	@Autowired
	private CacheService<PhotoComment> cacheService;

	public static final Map<Integer, String> fields = newLinkedHashMap();

	public static final Map<Integer, String> updatableFields = newLinkedHashMap();

	static {
		fields.put( 1, TABLE_COLUMN_PHOTO_ID );
		fields.put( 2, TABLE_COLUMN_AUTHOR_ID );
		fields.put( 3, TABLE_COLUMN_REPLY_TO_COMMENT_ID );
		fields.put( 4, TABLE_COLUMN_COMMENT_TEXT );
		fields.put( 5, TABLE_COLUMN_CREATION_TIME );
		fields.put( 6, TABLE_COLUMN_READ_TIME );
		fields.put( 7, TABLE_COLUMN_DELETED );
	}

	static {
		updatableFields.put( 4, TABLE_COLUMN_COMMENT_TEXT );
		updatableFields.put( 7, TABLE_COLUMN_DELETED );
	}

	@Override
	protected String getTableName() {
		return TABLE_COMMENTS;
	}

	@Override
	public boolean saveToDB( final PhotoComment photoComment ) {
		final boolean isSaved = createOrUpdateEntry( photoComment, fields, updatableFields );

		if ( isSaved ) {
			cacheService.expire( CacheKey.PHOTO_COMMENT, photoComment.getId() );
		}

		return isSaved;
	}

	@Override
	public PhotoComment load( final int commentId ) {
		return cacheService.getEntry( CacheKey.PHOTO_COMMENT, commentId, new CacheEntryFactory<PhotoComment>() {
			@Override
			public PhotoComment createEntry() {
				return loadEntryById( commentId, new PhotoCommentMapper() );
			}
		} );
	}

	@Override
	protected RowMapper<PhotoComment> getRowMapper() {
		return new PhotoCommentMapper();
	}

	@Override
	public List<Integer> loadAllIds( final int photoId ) {
		final String sql = String.format( "SELECT * FROM %s WHERE %s=:photoId ORDER BY %s", TABLE_COMMENTS, TABLE_COLUMN_PHOTO_ID, TABLE_COLUMN_CREATION_TIME );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "photoId", photoId );

		return jdbcTemplate.query( sql, paramSource, new IdsRowMapper() );
	}

	@Override
	public List<Integer> loadRootCommentsIds( final int photoId ) {
		final String sql = String.format( "SELECT * FROM %s WHERE %s=:photoId AND %s=0  ORDER BY %s"
			, TABLE_COMMENTS, TABLE_COLUMN_PHOTO_ID, TABLE_COLUMN_REPLY_TO_COMMENT_ID, TABLE_COLUMN_CREATION_TIME );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "photoId", photoId );

		return jdbcTemplate.query( sql, paramSource, new IdsRowMapper() );
	}

	@Override
	public List<Integer> loadUserCommentsIds( final int userId ) {
		final String sql = String.format( "SELECT %1$s FROM %2$s WHERE %3$s=:userId ORDER BY %4$s DESC;"
			, ENTITY_ID, TABLE_COMMENTS, TABLE_COLUMN_AUTHOR_ID, TABLE_COLUMN_CREATION_TIME );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "userId", userId );

		return jdbcTemplate.query( sql, paramSource, new IdsRowMapper() );
	}

	@Override
	public List<Integer> loadCommentsToUserPhotosIds( final int userId ) {
		final String sql = String.format( "SELECT %1$s FROM %2$s WHERE %3$s IN ( SELECT %1$s FROM %4$s WHERE %5$s=:userId ) AND %7$s <> :userId ORDER BY %3$s DESC, %6$s DESC;"
			, ENTITY_ID
			, TABLE_COMMENTS
			, TABLE_COLUMN_PHOTO_ID
			, PhotoDaoImpl.TABLE_PHOTOS
			, PhotoDaoImpl.TABLE_COLUMN_USER_ID
			, TABLE_COLUMN_CREATION_TIME
			, TABLE_COLUMN_AUTHOR_ID
		);

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "userId", userId );

		return jdbcTemplate.query( sql, paramSource, new IdsRowMapper() );
	}

	@Override
	public List<Integer> loadUnreadCommentsToUserIds( final int userId ) {
		final String sql = String.format( "SELECT %1$s FROM %2$s WHERE %3$s IN ( SELECT %1$s FROM %4$s WHERE %5$s=:userId ) AND %6$s <> :userId AND %7$s = 0 ORDER BY %8$s DESC;"
			, ENTITY_ID, TABLE_COMMENTS, TABLE_COLUMN_PHOTO_ID, PhotoDaoImpl.TABLE_PHOTOS, PhotoDaoImpl.TABLE_COLUMN_USER_ID, TABLE_COLUMN_AUTHOR_ID, TABLE_COLUMN_READ_TIME, TABLE_COLUMN_CREATION_TIME );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "userId", userId );

		return jdbcTemplate.query( sql, paramSource, new IdsRowMapper() );
	}

	@Override
	public List<Integer> loadAnswersOnCommentIds( final int commentId ) {
		final String sql = String.format( "SELECT * FROM %s WHERE %s=:commentId ORDER BY %s"
			, TABLE_COMMENTS, TABLE_COLUMN_REPLY_TO_COMMENT_ID, TABLE_COLUMN_CREATION_TIME );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "commentId", commentId );

		return jdbcTemplate.query( sql, paramSource, new IdsRowMapper() );
	}

	@Override
	public void setCommentReadTime( final int commentId, final Date time ) {
		final String sql = String.format( "UPDATE %s SET %s=:readTime WHERE %s=:commentId", TABLE_COMMENTS, TABLE_COLUMN_READ_TIME, ENTITY_ID );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "commentId", commentId );
		paramSource.addValue( "readTime", time );

		jdbcTemplate.update( sql, paramSource );

		cacheService.expire( CacheKey.PHOTO_COMMENT, commentId );
	}

	@Override
	public int getUnreadCommentsQty( final int userId ) {
		final String sql = String.format( "SELECT COUNT( %1$s ) FROM %2$s WHERE %3$s IN ( SELECT %1$s FROM %4$s WHERE %5$s=:userId ) AND %6$s <> :userId AND %7$s = 0;"
			, ENTITY_ID, TABLE_COMMENTS, TABLE_COLUMN_PHOTO_ID, PhotoDaoImpl.TABLE_PHOTOS, PhotoDaoImpl.TABLE_COLUMN_USER_ID, TABLE_COLUMN_AUTHOR_ID, TABLE_COLUMN_READ_TIME );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "userId", userId );

		return getIntValueOrZero( sql, paramSource );
	}

	@Override
	public int getWrittenCommentsQty( final int userId ) {
		final String sql = String.format( "SELECT COUNT( %1$s ) FROM %2$s WHERE %3$s = :userId;"
			, ENTITY_ID                         // 1
			, TABLE_COMMENTS                    // 2
			, TABLE_COLUMN_AUTHOR_ID            // 3
		);

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "userId", userId );

		return getIntValueOrZero( sql, paramSource );
	}

	@Override
	public int getReceivedCommentsQty( final int userId ) {
		final String sql = String.format( "SELECT COUNT( %1$s ) FROM %2$s WHERE %3$s IN ( SELECT %1$s FROM %4$s WHERE %5$s=:userId ) AND %6$s <> :userId;"
			, ENTITY_ID                         // 1
			, TABLE_COMMENTS                    // 2
			, TABLE_COLUMN_PHOTO_ID             // 3
			, PhotoDaoImpl.TABLE_PHOTOS         // 4
			, PhotoDaoImpl.TABLE_COLUMN_USER_ID // 5
			, TABLE_COLUMN_AUTHOR_ID            // 6
		);

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "userId", userId );

		return getIntValueOrZero( sql, paramSource );
	}

	@Override
	public boolean delete( final int entryId ) {

		final boolean isDeleted = deleteEntryById( entryId );

		if ( isDeleted ) {
			cacheService.expire( CacheKey.PHOTO_COMMENT, entryId );
		}

		return isDeleted;
	}

	@Override
	protected MapSqlParameterSource getParameters( final PhotoComment entry ) {
		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( TABLE_COLUMN_PHOTO_ID, entry.getPhotoId() );
		paramSource.addValue( TABLE_COLUMN_AUTHOR_ID, entry.getCommentAuthor().getId() );
		paramSource.addValue( TABLE_COLUMN_REPLY_TO_COMMENT_ID, entry.getReplyToCommentId() );
		paramSource.addValue( TABLE_COLUMN_COMMENT_TEXT, entry.getCommentText() );
		paramSource.addValue( TABLE_COLUMN_CREATION_TIME, entry.getCreationTime() );
		paramSource.addValue( TABLE_COLUMN_READ_TIME, entry.getReadTime() );
		paramSource.addValue( TABLE_COLUMN_DELETED, entry.isCommentDeleted() );

		return paramSource;
	}

	@Override
	public void deletePhotoComments( final int photoId ) {
		final String sql = String.format( "DELETE FROM %s WHERE %s=:photoId", TABLE_COMMENTS, TABLE_COLUMN_PHOTO_ID );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "photoId", photoId );

		jdbcTemplate.update( sql, paramSource );
	}

	@Override
	public int getPhotoCommentsCount( final int photoId ) {
		final String sql = String.format( "SELECT COUNT( %s ) FROM %s WHERE %s = :photoId;"
			, ENTITY_ID
			, TABLE_COMMENTS
			, TABLE_COLUMN_PHOTO_ID
		);

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "photoId", photoId );

		return getIntValueOrZero( sql, paramSource );
	}

	class PhotoCommentMapper implements RowMapper<PhotoComment> {

		@Override
		public PhotoComment mapRow( final ResultSet rs, final int rowNum ) throws SQLException {
			final PhotoComment result = new PhotoComment();

			result.setId( rs.getInt( BaseEntityDao.ENTITY_ID ) );
			result.setPhotoId( rs.getInt( TABLE_COLUMN_PHOTO_ID ) );

			final int authorId = rs.getInt( TABLE_COLUMN_AUTHOR_ID );
			result.setCommentAuthor( userService.load( authorId ) );

			result.setReplyToCommentId( rs.getInt( TABLE_COLUMN_REPLY_TO_COMMENT_ID ) );
			result.setCommentText( rs.getString( TABLE_COLUMN_COMMENT_TEXT ) );
			result.setCreationTime( rs.getTimestamp( TABLE_COLUMN_CREATION_TIME ) );
			result.setReadTime( rs.getTimestamp( TABLE_COLUMN_READ_TIME ) );

			result.setCommentDeleted( rs.getBoolean( TABLE_COLUMN_DELETED ) );

			return result;
		}
	}
}
