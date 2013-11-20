package core.services.dao;

import core.general.cache.CacheEntryFactory;
import core.general.cache.CacheKey;
import core.general.genre.Genre;
import core.general.photo.PhotoVotingCategory;
import core.services.system.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newLinkedHashMap;

public class GenreDaoImpl extends BaseEntityDaoImpl<Genre> implements GenreDao {

	public final static String TABLE_GENRES = "genres";

	public final static String TABLE_COLUMN_NAME = "name";
	public final static String TABLE_COLUMN_MIN_MARKS = "minmarksforbest";

	public final static String TABLE_COLUMN_CAN_CONTAIN_NUDE_CONTENT = "canContainNudeContent";
	public final static String TABLE_COLUMN_CONTAINS_NUDE_CONTENT = "containsNudeContent";

	public final static String TABLE_COLUMN_DESCRIPTION = "description";

	public static final Map<Integer, String> fields = newLinkedHashMap();

	@Autowired
	private CacheService<Genre> cacheService;

	@Autowired
	private PhotoVotingDao photoVotingDao;

	static {
		fields.put( 1, TABLE_COLUMN_NAME );
		fields.put( 2, TABLE_COLUMN_MIN_MARKS );
		fields.put( 3, TABLE_COLUMN_CAN_CONTAIN_NUDE_CONTENT );
		fields.put( 4, TABLE_COLUMN_CONTAINS_NUDE_CONTENT );
		fields.put( 5, TABLE_COLUMN_DESCRIPTION );
	}

	@Override
	protected String getTableName() {
		return TABLE_GENRES;
	}

	@Override
	public boolean saveToDB( final Genre genre ) {
		final boolean isGenreDataSavedSuccessfully = createOrUpdateEntry( genre, fields, fields );
		final boolean isGenreVotingCategoriesSavedSuccessfully = saveGenreVotingCategory( genre );

		final boolean isSaved = isGenreDataSavedSuccessfully && isGenreVotingCategoriesSavedSuccessfully;

		if ( isSaved ) {
			cacheService.expire( CacheKey.GENRE, genre.getId() );
			cacheService.expire( CacheKey.GENRE_VOTING_CATEGORY, genre.getId() );
		}

		return isSaved;
	}

	@Override
	public Genre load( final int entryId ) {
		return cacheService.getEntry( CacheKey.GENRE, entryId, new CacheEntryFactory<Genre>() {
			@Override
			public Genre createEntry() {
				return loadGenreFromDb( entryId );
			}
		} );
	}

	private Genre loadGenreFromDb( final int entryId ) {
		final String sql = String.format( "SELECT * FROM %s WHERE %s=:id", TABLE_GENRES, BaseEntityDao.ENTITY_ID );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "id", entryId );

		final Genre genre = getEntryOrNull( sql, paramSource, new GenreMapper() );

		if ( genre != null ) {
			genre.setPhotoVotingCategories( photoVotingDao.getGenreVotingCategories( entryId ).getVotingCategories() );
		}
		return genre;
	}

	@Override
	public List<Genre> loadAll() {
		final String sql = String.format( "SELECT * FROM %s ORDER BY %s", TABLE_GENRES, TABLE_COLUMN_NAME );
		return jdbcTemplate.query( sql, new MapSqlParameterSource(), new GenreMapper() );
	}

	@Override
	protected RowMapper<Genre> getRowMapper() {
		return new GenreMapper();
	}

	@Override
	public boolean delete( final int entryId ) {

		deleteGenreVotingCategories( entryId );

		final boolean isDeleted = deleteEntryById( entryId );

		if ( isDeleted ) {
			cacheService.expire( CacheKey.GENRE, entryId );
		}

		return isDeleted;
	}

	private boolean saveGenreVotingCategory( final Genre genre ) {
		boolean result = true;

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "genreId", genre.getId() );
		jdbcTemplate.update( String.format( "DELETE FROM genreVotingCategories WHERE %s=:genreId;", PhotoVotingDaoImpl.TABLE_GENRE_VOTING_CATEGORIES_GENRE_ID ), paramSource );

		final String insertSQL = String.format( "INSERT INTO genreVotingCategories ( %s, %s ) VALUES ( :genreId, :votingCategoryId );", PhotoVotingDaoImpl.TABLE_GENRE_VOTING_CATEGORIES_GENRE_ID, PhotoVotingDaoImpl.TABLE_GENRE_VOTING_CATEGORIES_VOTING_CATEGORY_ID );
		for ( PhotoVotingCategory photoVotingCategory : genre.getPhotoVotingCategories() ) {
			final MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue( "genreId", genre.getId() );
			params.addValue( "votingCategoryId", photoVotingCategory.getId() );

			result &= jdbcTemplate.update( insertSQL, params ) > 0;
		}

		return result;
	}

	@Override
	public int loadIdByName( final String genreName ) {
		final String sql = String.format( "SELECT %s FROM %s WHERE %s=:name", ENTITY_ID, TABLE_GENRES, TABLE_COLUMN_NAME );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "name", genreName );

		return getIntValueOrZero( sql, paramSource );
	}

	private void deleteGenreVotingCategories( final int genreId ) {
		final String sql = String.format( "DELETE FROM %s WHERE %s=:id"
			, PhotoVotingDaoImpl.TABLE_GENRE_VOTING_CATEGORIES, PhotoVotingDaoImpl.TABLE_GENRE_VOTING_CATEGORIES_GENRE_ID );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "id", genreId );

		jdbcTemplate.update( sql, paramSource );
	}

	@Override
	protected MapSqlParameterSource getParameters( final Genre entry ) {
		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( TABLE_COLUMN_NAME, entry.getName() );
		paramSource.addValue( TABLE_COLUMN_MIN_MARKS, entry.getMinMarksForBest() );
		paramSource.addValue( TABLE_COLUMN_CAN_CONTAIN_NUDE_CONTENT, entry.isCanContainNudeContent() );
		paramSource.addValue( TABLE_COLUMN_CONTAINS_NUDE_CONTENT, entry.isContainsNudeContent() );
		paramSource.addValue( TABLE_COLUMN_DESCRIPTION, entry.getDescription() );

		return paramSource;
	}

	private class GenreMapper implements RowMapper<Genre> {

		@Override
		public Genre mapRow( final ResultSet rs, final int rowNum ) throws SQLException {
			final Genre result = new Genre();

			final int genreId = rs.getInt( BaseEntityDao.ENTITY_ID );

			result.setId( genreId );
			result.setName( rs.getString( TABLE_COLUMN_NAME ) );
			result.setMinMarksForBest( rs.getInt( TABLE_COLUMN_MIN_MARKS ) );

			result.setCanContainNudeContent( rs.getBoolean( TABLE_COLUMN_CAN_CONTAIN_NUDE_CONTENT ) );
			result.setContainsNudeContent( rs.getBoolean( TABLE_COLUMN_CONTAINS_NUDE_CONTENT ) );

			result.setDescription( rs.getString( TABLE_COLUMN_DESCRIPTION ) );

			return result;
		}
	}
}
