package ui.services.breadcrumbs;

import core.enums.FavoriteEntryType;
import core.enums.PrivateMessageType;
import core.enums.UserCardTab;
import core.general.genre.Genre;
import core.general.user.User;
import core.general.user.UserMembershipType;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.general.user.userTeam.UserTeamMember;
import elements.PageTitleData;

public interface BreadcrumbsUserService {

	PageTitleData getUserListBreadcrumbs();

	PageTitleData getUserRegistrationBreadcrumbs();

	PageTitleData getVotesForUserRankInGenreBreadcrumbs( final User user, final Genre genre );

	PageTitleData getUserEditBreadcrumbs( final User user );

	PageTitleData setUserAvatarBreadcrumbs( final User user );

	PageTitleData getUserCardBreadcrumbs( final User user, final UserCardTab userCardTab );

	PageTitleData getUserListSearchResultBreadcrumbs();

	PageTitleData getUsersFilteredByMembershipTypeBreadcrumbs( final UserMembershipType membershipType );

	PageTitleData getUserTeamMemberListData( final User user );

	PageTitleData getUserPrivateMessagesListBreadcrumbs( final User user, final User withUser );

	PageTitleData getUserPrivateMessagesListBreadcrumbs( final User user, final PrivateMessageType messageType );

	PageTitleData getUserTeamMemberNewBreadcrumbs( final User user );

	PageTitleData getUserTeamMemberEditBreadcrumbs( final UserTeamMember userTeamMember );

	PageTitleData getUserTeamMemberCardBreadcrumbs( final UserTeamMember userTeamMember );

	PageTitleData getUserPhotoAlbumListBreadcrumbs( final User user );

	PageTitleData getUserPhotoAlbumNewBreadcrumbs( final User user );

	PageTitleData getUserPhotoAlbumEditBreadcrumbs( final UserPhotoAlbum photoAlbum );

	PageTitleData getUserPhotoAlbumPhotosBreadcrumbs( final UserPhotoAlbum photoAlbum );

	PageTitleData getUserFavoriteEntryListBreadcrumbs( final User user, final FavoriteEntryType favoriteEntryType );

	PageTitleData getUserIsAddedInFavoriteMembersByBreadcrumbs( final User user );

	String getUserRootTranslated();

	PageTitleData getUserData( final User user, final String tran );

	PageTitleData getUserWrongLogin();

	PageTitleData getChangeUserPasswordData( final User user );

	PageTitleData getUserNotificationsControlData( final User user );

	PageTitleData getPhotosOfUserFavoriteMembersBreadcrumb( final User user );

	PageTitleData getUserWrittenCommentsBreadcrumb( final User user );

	PageTitleData getUserReceivedCommentsBreadcrumb( final User user );

	PageTitleData getUserWrittenUnreadCommentsBreadcrumb( final User user );
}
