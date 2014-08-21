package ui.services.photo;

import core.general.base.PagingModel;
import core.general.genre.Genre;
import core.general.user.User;
import ui.elements.PhotoList;

public interface PhotoListFactoryService {

	PhotoList gallery( final PagingModel pagingModel );

	PhotoList galleryTopBest( final PagingModel pagingModel );

	PhotoList galleryBest( final PagingModel pagingModel );

	PhotoList galleryForGenre( final Genre genre, final PagingModel pagingModel );

	PhotoList galleryForGenreTopBest( final Genre genre, final PagingModel pagingModel );

	PhotoList galleryForGenreBest( final Genre genre, final PagingModel pagingModel );

	PhotoList galleryForUser( final User user, final PagingModel pagingModel );

	PhotoList galleryForUserTopBest( final User user, final PagingModel pagingModel );

	PhotoList galleryForUserBest( final User user, final PagingModel pagingModel );

	PhotoList galleryForUserAndGenre( User user, Genre genre, PagingModel pagingModel );

	PhotoList galleryForUserAndGenreTopBest( User user, Genre genre, PagingModel pagingModel );

	PhotoList galleryForUserAndGenreBest( final User user, final Genre genre, final PagingModel pagingModel );

	PhotoList appraisedByUserPhotos( final User user, final PagingModel pagingModel );
}
