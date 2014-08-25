package core.services.photo.list;

import core.enums.FavoriteEntryType;
import core.general.data.TimeRange;
import core.general.genre.Genre;
import core.general.photo.PhotoVotingCategory;
import core.general.user.User;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.general.user.userTeam.UserTeamMember;
import core.services.photo.list.factory.AbstractPhotoListFactory;

import java.util.Date;

public interface PhotoListFactoryService {

	AbstractPhotoListFactory gallery( final int page, final int itemsOnPage, final User accessor );

	AbstractPhotoListFactory galleryTopBest( final User accessor );

	AbstractPhotoListFactory galleryAbsolutelyBest( final int page, final int itemsOnPage, final User accessor );

	AbstractPhotoListFactory galleryForGenre( final Genre genre, final int page, final int itemsOnPage, final User accessor );

	AbstractPhotoListFactory galleryForGenreTopBest( final Genre genre, final int page, final int itemsOnPage, final User accessor );

	AbstractPhotoListFactory galleryForGenreBest( final Genre genre, final int page, final int itemsOnPage, final User accessor );

	AbstractPhotoListFactory galleryUploadedInDateRange( final Date timeFrom, final Date timeTo, final int page, final int itemsOnPage, final User accessor );

	AbstractPhotoListFactory galleryForUser( final User user, final int page, final int itemsOnPage, final User accessor );

	AbstractPhotoListFactory galleryForUserTopBest( final User user, final int page, final int itemsOnPage, final User accessor );

	AbstractPhotoListFactory galleryForUserBest( final User user, final int page, final int itemsOnPage, final User accessor );

	AbstractPhotoListFactory galleryForUserAndGenre( final User user, final Genre genre, final int page, final int itemsOnPage, final User accessor );

	AbstractPhotoListFactory galleryForUserAndGenreTopBest( final User user, Genre genre, final User accessor );

	AbstractPhotoListFactory galleryForUserAndGenreBest( final User user, final Genre genre, final int page, final int itemsOnPage, final User accessor );

	AbstractPhotoListFactory appraisedByUserPhotos( final User user, final int page, final int itemsOnPage, final User accessor );

	AbstractPhotoListFactory appraisedByUserPhotos( final User user, PhotoVotingCategory photoVotingCategory, final int page, final int itemsOnPage, final User accessor );

	AbstractPhotoListFactory userTeamMemberPhotosLast( final User user, final UserTeamMember userTeamMember, final User accessor );

	AbstractPhotoListFactory userTeamMemberPhotos( final User user, final UserTeamMember userTeamMember, final int page, final User accessor );

	AbstractPhotoListFactory userAlbumPhotosLast( final User user, final UserPhotoAlbum userPhotoAlbum, final User accessor );

	AbstractPhotoListFactory userAlbumPhotos( final User user, final UserPhotoAlbum userPhotoAlbum, final int page, final User accessor );

	AbstractPhotoListFactory userCardPhotosBest( final User user, final User accessor );

	AbstractPhotoListFactory userCardPhotosLast( final User user, final User accessor );

	AbstractPhotoListFactory userCardPhotosLastAppraised( final User user, final User accessor );

	AbstractPhotoListFactory userBookmarkedPhotos( final User user, final FavoriteEntryType favoriteEntryType, final int page, final User accessor );

	AbstractPhotoListFactory photosOfFavoriteAuthorsOfUser( final User user, final int page, final User accessor );

	TimeRange getTimeRange( int days );
}
