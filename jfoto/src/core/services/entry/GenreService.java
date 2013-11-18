package core.services.entry;

import core.general.genre.Genre;
import core.interfaces.AllEntriesLoadable;
import core.interfaces.BaseEntityService;
import core.interfaces.IdsSqlSelectable;

public interface GenreService extends BaseEntityService<Genre>, AllEntriesLoadable<Genre>, IdsSqlSelectable {

	Genre loadIdByName( final String genreName );
}
