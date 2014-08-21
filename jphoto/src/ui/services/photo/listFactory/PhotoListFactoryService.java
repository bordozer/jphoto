package ui.services.photo.listFactory;

import core.general.base.PagingModel;
import core.general.genre.Genre;
import core.general.user.User;
import ui.services.photo.listFactory.factory.AbstractPhotoListFactory;

public interface PhotoListFactoryService {

	AbstractPhotoListFactory gallery( final PagingModel pagingModel, final User accessor );

	AbstractPhotoListFactory galleryTopBest( final PagingModel pagingModel, final User accessor );

	AbstractPhotoListFactory galleryBest( final PagingModel pagingModel, final User accessor );

	AbstractPhotoListFactory galleryForGenre( final Genre genre, final PagingModel pagingModel, final User accessor );

	AbstractPhotoListFactory galleryForGenreTopBest( final Genre genre, final PagingModel pagingModel, final User accessor );

	AbstractPhotoListFactory galleryForGenreBest( final Genre genre, final PagingModel pagingModel, final User accessor );

	AbstractPhotoListFactory galleryForUser( final User user, final PagingModel pagingModel, final User accessor );

	AbstractPhotoListFactory galleryForUserTopBest( final User user, final PagingModel pagingModel, final User accessor );

	AbstractPhotoListFactory galleryForUserBest( final User user, final PagingModel pagingModel, final User accessor );

	AbstractPhotoListFactory galleryForUserAndGenre( Genre genre, PagingModel pagingModel, final User accessor );

	AbstractPhotoListFactory galleryForUserAndGenreTopBest( User user, Genre genre, PagingModel pagingModel, final User accessor );

	AbstractPhotoListFactory galleryForUserAndGenreBest( final User user, final Genre genre, final PagingModel pagingModel, final User accessor );

	AbstractPhotoListFactory appraisedByUserPhotos( final User user, final PagingModel pagingModel, final User accessor );
}
