package core.services.photo.list;

import core.enums.FavoriteEntryType;
import core.general.base.PagingModel;
import core.general.genre.Genre;
import core.general.user.User;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.general.user.userTeam.UserTeamMember;
import core.services.photo.list.factory.AbstractPhotoListFactory;

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

	AbstractPhotoListFactory userTeamMemberPhotosLast( final User user, final UserTeamMember userTeamMember, final User accessor );

	AbstractPhotoListFactory userTeamMemberPhotos( final User user, final UserTeamMember userTeamMember, final int page, final User accessor );

	AbstractPhotoListFactory userAlbumPhotosLast( final User user, final UserPhotoAlbum userPhotoAlbum, final User accessor );

	AbstractPhotoListFactory userAlbumPhotos( final User user, final UserPhotoAlbum userPhotoAlbum, final int page, final User accessor );

	AbstractPhotoListFactory userCardPhotosBest( final User user, final User accessor );

	AbstractPhotoListFactory userCardPhotosLast( final User user, final User accessor );

	AbstractPhotoListFactory userCardPhotosLastAppraised( final User user, final User accessor );

	AbstractPhotoListFactory userBookmarkedPhotos( final User user, final FavoriteEntryType favoriteEntryType, final int page, final User accessor );
}
