package core.services.dao;

import core.general.genre.Genre;

import java.util.List;

public interface GenreDao extends BaseEntityDao<Genre> {

	List<Genre> loadAll();

	int loadIdByName( final String genreName );
}
