package core.services.entry;

import core.general.genre.GenreVotingCategories;
import core.general.photo.PhotoVotingCategory;
import core.interfaces.AllEntriesLoadable;
import core.interfaces.BaseEntityService;
import core.interfaces.IdsSqlSelectable;

public interface VotingCategoryService extends BaseEntityService<PhotoVotingCategory>, AllEntriesLoadable<PhotoVotingCategory>, IdsSqlSelectable {

	String BEAN_NAME = "votingCategoryService"; // TODO: delete after clean up

	PhotoVotingCategory loadByName( final String name );

	GenreVotingCategories getGenreVotingCategories( final int genreId );
}
