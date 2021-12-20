package com.bordozer.jphoto.core.services.entry;

import com.bordozer.jphoto.core.general.genre.GenreVotingCategories;
import com.bordozer.jphoto.core.general.photo.PhotoVotingCategory;
import com.bordozer.jphoto.core.interfaces.AllEntriesLoadable;
import com.bordozer.jphoto.core.interfaces.BaseEntityService;
import com.bordozer.jphoto.core.interfaces.IdsSqlSelectable;

public interface VotingCategoryService extends BaseEntityService<PhotoVotingCategory>, AllEntriesLoadable<PhotoVotingCategory>, IdsSqlSelectable {

    String BEAN_NAME = "votingCategoryService"; // TODO: delete after clean up

    PhotoVotingCategory loadByName(final String name);

    GenreVotingCategories getGenreVotingCategories(final int genreId);
}
