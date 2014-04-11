package core.services.dao;

import core.general.cache.CacheEntryFactory;
import core.general.cache.CacheKey;
import core.general.configuration.ConfigurationKey;
import core.general.data.PhotoMarksForPeriod;
import core.general.data.UserRating;
import core.general.genre.GenreVotingCategories;
import core.general.photo.Photo;
import core.general.photo.PhotoVotingCategory;
import core.general.user.User;
import core.general.user.UserPhotoVote;
import core.services.system.CacheService;
import core.services.system.ConfigurationService;
import core.services.utils.DateUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import ui.controllers.users.card.MarksByCategoryInfo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newLinkedHashMap;

public class PhotoVotingDaoImpl extends BaseEntityDaoImpl<PhotoVotingCategory> implements PhotoVotingDao {

	public final static String TABLE_VOTING_CATEGORIES = "votingCategories";

	public final static String TABLE_COLUMN_NAME = "name";
	public final static String TABLE_COLUMN_DESCRIPTION = "description";

	public final static String TABLE_GENRE_VOTING_CATEGORIES = "genreVotingCategories";
	public final static String TABLE_GENRE_VOTING_CATEGORIES_GENRE_ID = "genreId";
	public final static String TABLE_GENRE_VOTING_CATEGORIES_VOTING_CATEGORY_ID = "votingCategoryId";

	public final static String TABLE_PHOTO_VOTING = "photoVoting";
	public final static String TABLE_PHOTO_VOTING_USER_ID = "userId";
	public final static String TABLE_PHOTO_VOTING_PHOTO_ID = "photoId";
	public final static String TABLE_PHOTO_VOTING_VOTING_CATEGORY_ID = "votingCategoryId";
	public final static String TABLE_PHOTO_VOTING_MARK = "mark";
	public final static String TABLE_PHOTO_VOTING_MAX_ACCESSIBLE_MARK = "maxAccessibleMark";
	public final static String TABLE_PHOTO_VOTING_TIME = "votingTime";

	public final static String TABLE_PHOTO_VOTING_SUMMARY = "photoVotingSummary";
	public final static String TABLE_PHOTO_VOTING_SUMMARY_PHOTO_ID = "photoId";
	public final static String TABLE_PHOTO_VOTING_SUMMARY_CATEGORY_ID = "photoVotingCategoryId";
	public final static String TABLE_PHOTO_VOTING_SUMMARY_MARKS = "photoSummaryMark";
	public final static String TABLE_PHOTO_VOTING_SUMMARY_VOICES = "photoSummaryVoices";

	private final static String USER_SUMMARY_RATING_COLUMN = "summaryRating";
	private final static String PHOTO_SUM_MARK_COLUMN = "summaryMark";

	public static final Map<Integer, String> fields = newLinkedHashMap();

	@Autowired
	private UserDao userDao;

	@Autowired
	private PhotoDao photoDao;

	@Autowired
	private CacheService<PhotoVotingCategory> photoVotingCategoryCacheService;

	@Autowired
	private CacheService<GenreVotingCategories> genreVotingCategoriesCacheService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private DateUtilsService dateUtilsService;

	static {
		fields.put( 1, TABLE_COLUMN_NAME );
		fields.put( 2, TABLE_COLUMN_DESCRIPTION );
	}

	@Override
	protected String getTableName() {
		return TABLE_VOTING_CATEGORIES;
	}

	@Override
	public boolean saveToDB( final PhotoVotingCategory photoVotingCategory ) {
		final boolean isSaved = createOrUpdateEntry( photoVotingCategory, fields, fields );

		if ( isSaved ) {
			photoVotingCategoryCacheService.expire( CacheKey.PHOTO_VOTING_CATEGORY, photoVotingCategory.getId() );
		}

		return isSaved;
	}

	@Override
	public PhotoVotingCategory load( final int votingCategoryId ) {
		return photoVotingCategoryCacheService.getEntry( CacheKey.PHOTO_VOTING_CATEGORY, votingCategoryId, new CacheEntryFactory<PhotoVotingCategory>() {
			@Override
			public PhotoVotingCategory createEntry() {
				return loadEntryById( votingCategoryId, new VotingCategoryMapper() );
			}
		} );
	}

	@Override
	protected RowMapper<PhotoVotingCategory> getRowMapper() {
		return new VotingCategoryMapper();
	}

	@Override
	public PhotoVotingCategory loadByName( final String name ) {
		final String sql = String.format( "SELECT * FROM %s WHERE %s=:name", TABLE_VOTING_CATEGORIES, TABLE_COLUMN_NAME );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "name", name );

		return getEntryOrNull( sql, paramSource, new VotingCategoryMapper() );
	}

	@Override
	public boolean delete( final int entryId ) {

		final boolean isDeleted = deleteEntryById( entryId );

		if ( isDeleted ) {
			photoVotingCategoryCacheService.expire( CacheKey.PHOTO_VOTING_CATEGORY, entryId );
		}

		return isDeleted;
	}

	@Override
	public List<PhotoVotingCategory> loadAll() {
		final String sql = String.format( "SELECT * FROM %s ORDER BY %s", TABLE_VOTING_CATEGORIES, TABLE_COLUMN_NAME );

		return jdbcTemplate.query( sql, new MapSqlParameterSource(), new VotingCategoryMapper() );
	}

	@Override
	protected MapSqlParameterSource getParameters( final PhotoVotingCategory entry ) {
		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( TABLE_COLUMN_NAME, entry.getName() );
		paramSource.addValue( TABLE_COLUMN_DESCRIPTION, entry.getDescription() );

		return paramSource;
	}

	public GenreVotingCategories getGenreVotingCategories( final int genreId ) {

		return genreVotingCategoriesCacheService.getEntry( CacheKey.GENRE_VOTING_CATEGORY, genreId, new CacheEntryFactory<GenreVotingCategories>() {
			@Override
			public GenreVotingCategories createEntry() {
				return loadGenreVotingCategories( genreId );
			}
		} );
	}

	private GenreVotingCategories loadGenreVotingCategories( final int genreId ) {
		final String sql = String.format( "SELECT vc.* FROM %s gvc, %s vc WHERE gvc.%s = vc.%s AND gvc.%s = :genreId ORDER BY vc.%s", TABLE_GENRE_VOTING_CATEGORIES, TABLE_VOTING_CATEGORIES, TABLE_GENRE_VOTING_CATEGORIES_VOTING_CATEGORY_ID, BaseEntityDao.ENTITY_ID, TABLE_GENRE_VOTING_CATEGORIES_GENRE_ID, BaseEntityDao.ENTITY_ID );
		final List<PhotoVotingCategory> votingCategories = jdbcTemplate.query( sql, new MapSqlParameterSource( "genreId", genreId ), new VotingCategoryMapper() );

		final GenreVotingCategories genreVotingCategories = new GenreVotingCategories( genreId );
		genreVotingCategories.setVotingCategories( votingCategories );
		return genreVotingCategories;
	}

	@Override
	public boolean saveUserPhotoVoting( final User user, final Photo photo, final List<UserPhotoVote> userPhotoVotes ) {
		boolean result = true;

		for ( final UserPhotoVote userVoting : userPhotoVotes ) {

			final MapSqlParameterSource paramSource = new MapSqlParameterSource();
			paramSource.addValue( "userId", user.getId() );
			paramSource.addValue( "photoId", photo.getId() );
			paramSource.addValue( "votingCategoryId", userVoting.getPhotoVotingCategory().getId() );
			paramSource.addValue( "mark", userVoting.getMark() );
			paramSource.addValue( "maxAccessibleMark", userVoting.getMaxAccessibleMark() );
			paramSource.addValue( "votingTime", userVoting.getVotingTime() );

			final String sql = String.format( "INSERT INTO %s ( %s, %s, %s, %s, %s, %s ) VALUES( :userId, :photoId, :votingCategoryId, :mark, :maxAccessibleMark, :votingTime );"
				, TABLE_PHOTO_VOTING, TABLE_PHOTO_VOTING_USER_ID, TABLE_PHOTO_VOTING_PHOTO_ID, TABLE_PHOTO_VOTING_VOTING_CATEGORY_ID, TABLE_PHOTO_VOTING_MARK, TABLE_PHOTO_VOTING_MAX_ACCESSIBLE_MARK, TABLE_PHOTO_VOTING_TIME );

			result &= jdbcTemplate.update( sql, paramSource ) > 0;
		}

		return result;
	}

	@Override
	public void deletePhotoVotes( final int photoId ) {
		final String sql = String.format( "DELETE FROM %s WHERE %s=:photoId", TABLE_PHOTO_VOTING, TABLE_PHOTO_VOTING_PHOTO_ID );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "photoId", photoId );

		jdbcTemplate.update( sql, paramSource );
	}

	@Override
	public void deletePhotoVotesSummary( final int photoId ) {
		final String sql = String.format( "DELETE FROM %s WHERE %s=:photoId", TABLE_PHOTO_VOTING_SUMMARY, TABLE_PHOTO_VOTING_SUMMARY_PHOTO_ID );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "photoId", photoId );

		jdbcTemplate.update( sql, paramSource );
	}

	@Override
	public boolean isUserVotedForPhoto( final User user, final Photo photo ) {
		final String sql = String.format( "SELECT 1 FROM %s WHERE %s=:userId AND %s=:photoId LIMIT 1;", TABLE_PHOTO_VOTING, TABLE_PHOTO_VOTING_USER_ID, TABLE_PHOTO_VOTING_PHOTO_ID );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "userId", user.getId() );
		paramSource.addValue( "photoId", photo.getId() );

		return hasEntry( sql, paramSource );
	}

	@Override
	public List<UserPhotoVote> getUserVotesForPhoto( final User user, final Photo photo ) {
		final String sql = String.format( "SELECT * FROM %s WHERE %s=:userId AND %s=:photoId ORDER BY %s DESC;"
			, TABLE_PHOTO_VOTING
			, TABLE_PHOTO_VOTING_USER_ID
			, TABLE_PHOTO_VOTING_PHOTO_ID
			, TABLE_PHOTO_VOTING_TIME
		);

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "userId", user.getId() );
		paramSource.addValue( "photoId", photo.getId() );

		return jdbcTemplate.query( sql, paramSource, new UserPhotoVoteMapper() );
	}

	@Override
	public List<UserPhotoVote> getPhotoVotes( final Photo photo ) {
		final String sql = String.format( "SELECT * FROM %s WHERE %s=:photoId ORDER BY %s DESC;"
			, TABLE_PHOTO_VOTING
			, TABLE_PHOTO_VOTING_PHOTO_ID
			, TABLE_PHOTO_VOTING_TIME
		);

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "photoId", photo.getId() );

		return jdbcTemplate.query( sql, paramSource, new UserPhotoVoteMapper() );
	}

	@Override
	public List<UserPhotoVote> getUserVotes( final User user ) {
		final String sql = String.format( "SELECT * FROM %s WHERE %s=:userId ORDER BY %s DESC;"
			, TABLE_PHOTO_VOTING
			, TABLE_PHOTO_VOTING_USER_ID
			, TABLE_PHOTO_VOTING_TIME
		);

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "userId", user.getId() );

		return jdbcTemplate.query( sql, paramSource, new UserPhotoVoteMapper() );
	}

	@Override
	public List<MarksByCategoryInfo> getUserSummaryVoicesByPhotoCategories( final Photo photo ) {
		final String sql = String.format( "SELECT * FROM %s WHERE %s = :photoId;"
					, TABLE_PHOTO_VOTING_SUMMARY
					, TABLE_PHOTO_VOTING_SUMMARY_PHOTO_ID
				);

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "photoId", photo.getId() );

		return jdbcTemplate.query( sql, paramSource, new PhotoVotingSummaryMapper() );
	}

	@Override
	public List<MarksByCategoryInfo> getUserSummaryVoicesByPhotoCategories( final User user ) {
		final String sql = String.format( "SELECT %1$s, COUNT( %5$s ) AS %6$s, SUM( %2$s ) AS %7$s FROM %3$s WHERE %4$s=:userId GROUP BY %1$s"
			, TABLE_PHOTO_VOTING_VOTING_CATEGORY_ID
			, TABLE_PHOTO_VOTING_MARK
			, TABLE_PHOTO_VOTING
			, TABLE_PHOTO_VOTING_USER_ID
			, TABLE_PHOTO_VOTING_PHOTO_ID
			, TABLE_PHOTO_VOTING_SUMMARY_VOICES
			, TABLE_PHOTO_VOTING_SUMMARY_MARKS
		);

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "userId", user.getId() );

		return jdbcTemplate.query( sql, paramSource, new AgregatePhotoVotingMapper() );
	}

	@Override
	public void updatePhotoSummaryMarksByVotingCategories( final int photoId, final List<UserPhotoVote> userPhotoVotes ) {
		String sql;

		for ( final UserPhotoVote userPhotoVote : userPhotoVotes ) {
			final int photoVotingCategoryId = userPhotoVote.getPhotoVotingCategory().getId();

			if ( isPhotoCategorySummaryVoteEntryExist( photoId, photoVotingCategoryId ) ) {
				sql = String.format( "UPDATE %1$s SET %2$s=%2$s + :marks, %3$s=%3$s + 1 WHERE %4$s=:photoId AND %5$s=:votingCategoryId;"
					, TABLE_PHOTO_VOTING_SUMMARY
					, TABLE_PHOTO_VOTING_SUMMARY_MARKS
					, TABLE_PHOTO_VOTING_SUMMARY_VOICES
					, TABLE_PHOTO_VOTING_SUMMARY_PHOTO_ID
					, TABLE_PHOTO_VOTING_SUMMARY_CATEGORY_ID
				);
			} else {
				sql = String.format( "INSERT INTO %s ( %s, %s, %s, %s) VALUES( :photoId, :votingCategoryId, :marks, 1 );"
					, TABLE_PHOTO_VOTING_SUMMARY
					, TABLE_PHOTO_VOTING_SUMMARY_PHOTO_ID
					, TABLE_PHOTO_VOTING_SUMMARY_CATEGORY_ID
					, TABLE_PHOTO_VOTING_SUMMARY_MARKS
					, TABLE_PHOTO_VOTING_SUMMARY_VOICES
				);
			}

			final MapSqlParameterSource paramSource = new MapSqlParameterSource();
			paramSource.addValue( "photoId", photoId );
			paramSource.addValue( "votingCategoryId", photoVotingCategoryId );
			paramSource.addValue( "marks", userPhotoVote.getMark() );

			jdbcTemplate.update( sql, paramSource );
		}
	}

	@Override
	public int getPhotoMarksForPeriod( final int photoId, final Date timeFrom, final Date timeTo ) {
		final String sql = String.format( "SELECT SUM( %s ) AS summark FROM %s WHERE %s=:photoId AND %s >= :timeFrom AND %s <= :timeTo"
			, TABLE_PHOTO_VOTING_MARK
			, TABLE_PHOTO_VOTING
			, TABLE_PHOTO_VOTING_PHOTO_ID
			, TABLE_PHOTO_VOTING_TIME
			, TABLE_PHOTO_VOTING_TIME
		);

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "photoId", photoId );
		paramSource.addValue( "timeFrom", timeFrom );
		paramSource.addValue( "timeTo", timeTo );

		return jdbcTemplate.queryForInt( sql, paramSource );
	}

	@Override
	public List<UserRating> getUserRatingForPeriod( final Date timeFrom, final Date timeTo, final int limit ) {
		final String sql = String.format( "SELECT %1$s.%2$s, SUM( %3$s.%4$s ) AS %8$s  "
										  + "FROM %1$s AS %1$s INNER JOIN %3$s ON ( %1$s.id = %3$s.%5$s ) "
										  + "WHERE ( ( %3$s.%6$s >= :timeFrom ) "
										  + "AND %3$s.%6$s <= :timeTo ) "
										  + "GROUP BY %1$s.%2$s "
										  + "HAVING SUM( %3$s.%4$s ) >= '1' "
										  + "ORDER BY SUM( %3$s.%4$s ) DESC "
										  + "LIMIT %7$s"
										  , PhotoDaoImpl.TABLE_PHOTOS					// 1
										  , PhotoDaoImpl.TABLE_COLUMN_USER_ID			// 2
										  , TABLE_PHOTO_VOTING							// 3
										  , TABLE_PHOTO_VOTING_MARK						// 4
										  , TABLE_PHOTO_VOTING_PHOTO_ID					// 5
										  , TABLE_PHOTO_VOTING_TIME						// 6
										  , limit										// 7
										  , USER_SUMMARY_RATING_COLUMN					// 8
		);

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "timeFrom", timeFrom );
		paramSource.addValue( "timeTo", timeTo );

		return jdbcTemplate.query( sql, paramSource, new UserRatingMapper() );
	}

	@Override
	public List<PhotoMarksForPeriod> getSummaryPhotoVotingForPeriodSortedBySummaryMarkDesc( final Date timeFrom, final Date timeTo ) {
		final String sql = String.format( "SELECT p.%1$s, SUM( pv.%6$s) AS %8$s "
										  + "FROM %2$s p, %3$s pv "
										  + "WHERE p.%1$s = pv.%4$s"
										  + "  AND pv.%5$s >= :timeFrom "
										  + "  AND pv.%5$s <= :timeTo "
										  + "  GROUP BY p.%1$s "
										  + "HAVING SUM( pv.%6$s) >= :minMark "
										  + "ORDER BY SUM( pv.%6$s) DESC, p.%7$s DESC"
			, ENTITY_ID
			, PhotoDaoImpl.TABLE_PHOTOS
			, TABLE_PHOTO_VOTING
			, TABLE_PHOTO_VOTING_PHOTO_ID
			, TABLE_PHOTO_VOTING_TIME
			, TABLE_PHOTO_VOTING_MARK
			, PhotoDaoImpl.TABLE_COLUMN_UPLOAD_TIME
			, PHOTO_SUM_MARK_COLUMN
		);

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "timeFrom", timeFrom );
		paramSource.addValue( "timeTo", timeTo );

		final int minMarkForTop = configurationService.getInt( ConfigurationKey.PHOTO_RATING_MIN_MARKS_TO_BE_IN_THE_BEST_PHOTO );
		paramSource.addValue( "minMark", minMarkForTop );

		return jdbcTemplate.query( sql, paramSource, new PhotoMarksForPeriodMapper() );
	}

	private boolean isPhotoCategorySummaryVoteEntryExist( final int photoId, final int photoVotingCategoryId ) {
		final String sql = String.format( "SELECT 1 FROM %s WHERE %s=:photoId AND %s=:votingCategoryId;"
			, TABLE_PHOTO_VOTING_SUMMARY, TABLE_PHOTO_VOTING_SUMMARY_PHOTO_ID, TABLE_PHOTO_VOTING_SUMMARY_CATEGORY_ID );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "photoId", photoId );
		paramSource.addValue( "votingCategoryId", photoVotingCategoryId );

		return existsInt( sql, paramSource );
	}

	private class VotingCategoryMapper implements RowMapper<PhotoVotingCategory> {

		@Override
		public PhotoVotingCategory mapRow( final ResultSet rs, final int rowNum ) throws SQLException {
			final PhotoVotingCategory result = new PhotoVotingCategory();

			result.setId( rs.getInt( BaseEntityDao.ENTITY_ID ) );
			result.setName( rs.getString( TABLE_COLUMN_NAME ) );
			result.setDescription( rs.getString( TABLE_COLUMN_DESCRIPTION ) );

			return result;
		}
	}

	private class UserPhotoVoteMapper implements RowMapper<UserPhotoVote> {

		@Override
		public UserPhotoVote mapRow( final ResultSet rs, final int rowNum ) throws SQLException {

			final int votingCategoryId = rs.getInt( TABLE_PHOTO_VOTING_VOTING_CATEGORY_ID );
			final PhotoVotingCategory photoVotingCategory = load( votingCategoryId );

			final User user = userDao.load( rs.getInt( TABLE_PHOTO_VOTING_USER_ID ) );
			final Photo photo = photoDao.load( rs.getInt( TABLE_PHOTO_VOTING_PHOTO_ID ) );

			final UserPhotoVote userVote = new UserPhotoVote( user, photo, photoVotingCategory );
			userVote.setMark( rs.getInt( TABLE_PHOTO_VOTING_MARK ) );
			userVote.setVotingTime( rs.getTimestamp( TABLE_PHOTO_VOTING_TIME ) );
			userVote.setMaxAccessibleMark( rs.getInt( TABLE_PHOTO_VOTING_MAX_ACCESSIBLE_MARK ) );

			return userVote;
		}
	}

	private class PhotoVotingSummaryMapper implements RowMapper<MarksByCategoryInfo> {

		@Override
		public MarksByCategoryInfo mapRow( final ResultSet rs, final int rowNum ) throws SQLException {

			final int votingCategoryId = rs.getInt( TABLE_PHOTO_VOTING_SUMMARY_CATEGORY_ID );
			final PhotoVotingCategory photoVotingCategory = load( votingCategoryId );

			final MarksByCategoryInfo categoriesInfo = new MarksByCategoryInfo();
			categoriesInfo.setPhotoVotingCategory( photoVotingCategory );
			categoriesInfo.setSumMark( rs.getInt( TABLE_PHOTO_VOTING_SUMMARY_MARKS ) );
			categoriesInfo.setQuantity( rs.getInt( TABLE_PHOTO_VOTING_SUMMARY_VOICES ) );

			return categoriesInfo;
		}
	}

	private class AgregatePhotoVotingMapper implements RowMapper<MarksByCategoryInfo> {

		@Override
		public MarksByCategoryInfo mapRow( final ResultSet rs, final int rowNum ) throws SQLException {

			final int votingCategoryId = rs.getInt( TABLE_PHOTO_VOTING_VOTING_CATEGORY_ID );
			final PhotoVotingCategory photoVotingCategory = load( votingCategoryId );

			final MarksByCategoryInfo categoriesInfo = new MarksByCategoryInfo();
			categoriesInfo.setPhotoVotingCategory( photoVotingCategory );
			categoriesInfo.setSumMark( rs.getInt( TABLE_PHOTO_VOTING_SUMMARY_MARKS ) );
			categoriesInfo.setQuantity( rs.getInt( TABLE_PHOTO_VOTING_SUMMARY_VOICES ) );

			return categoriesInfo;
		}
	}

	private class UserRatingMapper implements RowMapper<UserRating> {

		@Override
		public UserRating mapRow( final ResultSet rs, final int rowNum ) throws SQLException {
			final UserRating result = new UserRating();

			result.setUser( userDao.load( rs.getInt( PhotoDaoImpl.TABLE_COLUMN_USER_ID ) ) );
			result.setRating( rs.getInt( USER_SUMMARY_RATING_COLUMN ) );

			return result;
		}
	}

	private class PhotoMarksForPeriodMapper implements RowMapper<PhotoMarksForPeriod> {

		@Override
		public PhotoMarksForPeriod mapRow( final ResultSet rs, final int rowNum ) throws SQLException {
			final PhotoMarksForPeriod result = new PhotoMarksForPeriod();

			result.setPhotoId( rs.getInt( ENTITY_ID ) );
			result.setSumMarks( rs.getInt( PHOTO_SUM_MARK_COLUMN ) );

			return result;
		}
	}
}
