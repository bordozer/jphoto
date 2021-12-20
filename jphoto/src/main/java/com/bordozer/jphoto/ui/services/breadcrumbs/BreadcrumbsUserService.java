package com.bordozer.jphoto.ui.services.breadcrumbs;

import com.bordozer.jphoto.core.enums.FavoriteEntryType;
import com.bordozer.jphoto.core.enums.PrivateMessageType;
import com.bordozer.jphoto.core.enums.UserCardTab;
import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.UserMembershipType;
import com.bordozer.jphoto.core.general.user.userAlbums.UserPhotoAlbum;
import com.bordozer.jphoto.core.general.user.userTeam.UserTeamMember;
import com.bordozer.jphoto.ui.elements.PageTitleData;

public interface BreadcrumbsUserService {

    PageTitleData getUserListBreadcrumbs();

    PageTitleData getUserRegistrationBreadcrumbs();

    PageTitleData getVotesForUserRankInGenreBreadcrumbs(final User user, final Genre genre);

    PageTitleData getUserEditBreadcrumbs(final User user);

    PageTitleData setUserAvatarBreadcrumbs(final User user);

    PageTitleData getUserCardBreadcrumbs(final User user, final UserCardTab userCardTab);

    PageTitleData getUserListSearchResultBreadcrumbs();

    PageTitleData getUsersFilteredByMembershipTypeBreadcrumbs(final UserMembershipType membershipType);

    PageTitleData getUserTeamMemberListBreadcrumbs(final User user);

    PageTitleData getUserPrivateMessagesListBreadcrumbs(final User user, final User withUser);

    PageTitleData getUserPrivateMessagesListBreadcrumbs(final User user, final PrivateMessageType messageType);

    PageTitleData getUserTeamMemberNewBreadcrumbs(final User user);

    PageTitleData getUserTeamMemberEditBreadcrumbs(final UserTeamMember userTeamMember);

    PageTitleData getUserTeamMemberCardBreadcrumbs(final UserTeamMember userTeamMember);

    PageTitleData getUserPhotoAlbumListBreadcrumbs(final User user);

    PageTitleData getUserPhotoAlbumNewBreadcrumbs(final User user);

    PageTitleData getUserPhotoAlbumEditBreadcrumbs(final UserPhotoAlbum photoAlbum);

    PageTitleData getUserPhotoAlbumPhotosBreadcrumbs(final UserPhotoAlbum photoAlbum);

    PageTitleData getUserFavoriteEntryListBreadcrumbs(final User user, final FavoriteEntryType favoriteEntryType);

    PageTitleData getUserIsAddedInFavoriteMembersByBreadcrumbs(final User user);

    PageTitleData getUserWrongLoginBreadcrumbs();

    PageTitleData getChangeUserPasswordBreadcrumbs(final User user);

    PageTitleData getUserNotificationsControlBreadcrumbs(final User user);

    PageTitleData getPhotosOfUserFavoriteMembersBreadcrumb(final User user);

    PageTitleData getUserWrittenCommentsBreadcrumb(final User user);

    PageTitleData getUserReceivedCommentsBreadcrumb(final User user);

    PageTitleData getUserWrittenUnreadCommentsBreadcrumb(final User user);

    PageTitleData getUserLoginRestrictionBreadCrumbs(final User user);
}
