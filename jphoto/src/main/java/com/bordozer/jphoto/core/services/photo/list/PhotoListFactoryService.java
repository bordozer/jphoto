package com.bordozer.jphoto.core.services.photo.list;

import com.bordozer.jphoto.core.enums.FavoriteEntryType;
import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.photo.PhotoVotingCategory;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.UserMembershipType;
import com.bordozer.jphoto.core.general.user.userAlbums.UserPhotoAlbum;
import com.bordozer.jphoto.core.general.user.userTeam.UserTeamMember;
import com.bordozer.jphoto.core.services.photo.list.factory.AbstractPhotoListFactory;

import java.util.Date;

public interface PhotoListFactoryService {

    AbstractPhotoListFactory gallery(final int page, final int itemsOnPage, final User accessor);

    AbstractPhotoListFactory galleryTopBest(final User accessor);

    AbstractPhotoListFactory galleryLastPopular(final int page, final int itemsOnPage, final User accessor);

    AbstractPhotoListFactory galleryAbsolutelyBest(final int page, final int itemsOnPage, final User accessor);

    AbstractPhotoListFactory galleryForGenre(final Genre genre, final int page, final int itemsOnPage, final User accessor);

    AbstractPhotoListFactory galleryForGenreTopBest(final Genre genre, final User accessor);

    AbstractPhotoListFactory galleryForGenreBest(final Genre genre, final int page, final int itemsOnPage, final User accessor);

    AbstractPhotoListFactory galleryUploadedInDateRange(final Date timeFrom, final Date timeTo, final int page, final int itemsOnPage, final User accessor);

    AbstractPhotoListFactory galleryUploadedInDateRangeBest(final Date timeFrom, final Date timeTo, final int page, final int itemsOnPage, final User accessor);

    AbstractPhotoListFactory galleryByUserMembershipType(final UserMembershipType userMembershipType, final int page, final int itemsOnPage, final User accessor);

    AbstractPhotoListFactory galleryByUserMembershipTypeTopBest(final UserMembershipType userMembershipType, final User accessor);

    AbstractPhotoListFactory galleryByUserMembershipTypeBest(final UserMembershipType userMembershipType, final int page, final int itemsOnPage, final User accessor);

    AbstractPhotoListFactory galleryForUser(final User user, final int page, final int itemsOnPage, final User accessor);

    AbstractPhotoListFactory galleryForUserTopBest(final User user, final int page, final int itemsOnPage, final User accessor);

    AbstractPhotoListFactory galleryForUserBest(final User user, final int page, final int itemsOnPage, final User accessor);

    AbstractPhotoListFactory galleryForUserAndGenre(final User user, final Genre genre, final int page, final int itemsOnPage, final User accessor);

    AbstractPhotoListFactory galleryForUserAndGenreTopBest(final User user, Genre genre, final User accessor);

    AbstractPhotoListFactory galleryForUserAndGenreBest(final User user, final Genre genre, final int page, final int itemsOnPage, final User accessor);

    AbstractPhotoListFactory appraisedByUserPhotos(final User user, final int page, final int itemsOnPage, final User accessor);

    AbstractPhotoListFactory appraisedByUserPhotos(final User user, PhotoVotingCategory photoVotingCategory, final int page, final int itemsOnPage, final User accessor);

    AbstractPhotoListFactory userTeamMemberPhotosLast(final User user, final UserTeamMember userTeamMember, final User accessor);

    AbstractPhotoListFactory userTeamMemberPhotos(final User user, final UserTeamMember userTeamMember, final int page, final int itemsOnPage, final User accessor);

    AbstractPhotoListFactory userAlbumPhotosLast(final User user, final UserPhotoAlbum userPhotoAlbum, final User accessor);

    AbstractPhotoListFactory userAlbumPhotos(final User user, final UserPhotoAlbum userPhotoAlbum, final int page, final int itemsOnPage, final User accessor);

    AbstractPhotoListFactory userCardPhotosBest(final User user, final User accessor);

    AbstractPhotoListFactory userCardPhotosLast(final User user, final User accessor);

    AbstractPhotoListFactory userCardPhotosLastAppraised(final User user, final User accessor);

    AbstractPhotoListFactory userBookmarkedPhotos(final User user, final FavoriteEntryType favoriteEntryType, final int page, final int itemsOnPage, final User accessor);

    AbstractPhotoListFactory photosOfFavoriteAuthorsOfUser(final User user, final int page, final int itemsOnPage, final User accessor);
}
