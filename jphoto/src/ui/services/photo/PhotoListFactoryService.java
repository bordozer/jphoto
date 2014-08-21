package ui.services.photo;

import core.general.base.PagingModel;
import core.general.genre.Genre;
import core.general.user.User;
import ui.controllers.photos.list.factory.AbstractPhotoListFactory;
import ui.elements.PhotoList;

public interface PhotoListFactoryService {

	AbstractPhotoListFactory gallery( final PagingModel pagingModel );

	AbstractPhotoListFactory galleryTopBest( final PagingModel pagingModel );

	AbstractPhotoListFactory galleryBest( final PagingModel pagingModel );

	AbstractPhotoListFactory galleryForGenre( final Genre genre, final PagingModel pagingModel );

	AbstractPhotoListFactory galleryForGenreTopBest( final Genre genre, final PagingModel pagingModel );

	AbstractPhotoListFactory galleryForGenreBest( final Genre genre, final PagingModel pagingModel );

	AbstractPhotoListFactory galleryForUser( final User user, final PagingModel pagingModel );

	AbstractPhotoListFactory galleryForUserTopBest( final User user, final PagingModel pagingModel );

	AbstractPhotoListFactory galleryForUserBest( final User user, final PagingModel pagingModel );

	AbstractPhotoListFactory galleryForUserAndGenre( User user, Genre genre, PagingModel pagingModel );

	AbstractPhotoListFactory galleryForUserAndGenreTopBest( User user, Genre genre, PagingModel pagingModel );

	AbstractPhotoListFactory galleryForUserAndGenreBest( final User user, final Genre genre, final PagingModel pagingModel );

	AbstractPhotoListFactory appraisedByUserPhotos( final User user, final PagingModel pagingModel );
}
