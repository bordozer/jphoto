package core.services.entry;

import core.general.genre.Genre;
import core.services.dao.GenreDao;
import core.services.dao.GenreDaoImpl;
import core.services.utils.sql.BaseSqlUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import sql.SqlSelectIdsResult;
import sql.builder.SqlColumnSelect;
import sql.builder.SqlIdsSelectQuery;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class GenreServiceImpl implements GenreService {

	@Autowired
	private GenreDao genreDao;
	
	@Autowired
	private BaseSqlUtilsService baseSqlUtilsService;

	@Override
	public boolean save( final Genre entry ) {
		return genreDao.saveToDB( entry );
	}

	@Override
	public Genre load( final int genreId ) {
		return genreDao.load( genreId );
	}

	@Override
	public Genre loadIdByName( final String genreName ) {
		return load( genreDao.loadIdByName( genreName ) );
	}

	@Override
	public boolean delete( final int entryId ) {
		return genreDao.delete( entryId );
	}

	@Override
	public List<Genre> loadAll() {
		final SqlIdsSelectQuery idsSQL = baseSqlUtilsService.getGenresIdsSQL();
		idsSQL.addSortingAsc( new SqlColumnSelect( idsSQL.getMainTable(), GenreDaoImpl.TABLE_COLUMN_NAME )  );

		final List<Integer> genreIds = load( idsSQL ).getIds();
		final List<Genre> genres = newArrayList();

		for ( final int genreId : genreIds ) {
			genres.add( load( genreId ) );
		}

		return genres;
	}

	@Override
	public SqlSelectIdsResult load( final SqlIdsSelectQuery selectIdsQuery ) {
		return genreDao.load( selectIdsQuery );
	}

	@Override
	public boolean exists( final int entryId ) {
		return genreDao.exists( entryId );
	}

	@Override
	public boolean exists( final Genre entry ) {
		return genreDao.exists( entry );
	}
}
