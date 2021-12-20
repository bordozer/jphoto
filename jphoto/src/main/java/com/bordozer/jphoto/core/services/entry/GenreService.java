package com.bordozer.jphoto.core.services.entry;

import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.interfaces.AllEntriesLoadable;
import com.bordozer.jphoto.core.interfaces.BaseEntityService;
import com.bordozer.jphoto.core.interfaces.IdsSqlSelectable;
import com.bordozer.jphoto.core.services.translator.Language;

import java.util.List;

public interface GenreService extends BaseEntityService<Genre>, AllEntriesLoadable<Genre>, IdsSqlSelectable {

    Genre loadByName(final String genreName);

    List<Genre> loadAllSortedByNameForLanguage(final Language language);
}
