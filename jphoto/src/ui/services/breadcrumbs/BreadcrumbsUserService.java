package ui.services.breadcrumbs;

import core.enums.FavoriteEntryType;
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

	PageTitleData getUserListByFilter();

	PageTitleData getUsersByMembershipType( final UserMembershipType membershipType );

	PageTitleData getUserTeamMemberListData( final User user );

	PageTitleData getUserPrivateMessagesListData( final User user );

	PageTitleData getUserTeamMemberNewData( final User user );

	PageTitleData getUserTeamMemberEditData( final UserTeamMember userTeamMember );

	PageTitleData getUserTeamMemberCardData( final UserTeamMember userTeamMember );

	PageTitleData getUserPhotoAlbumsData( final User user );

	PageTitleData getUserPhotoAlbumsNew( final User user );

	PageTitleData getUserPhotoAlbumsEdit( final UserPhotoAlbum photoAlbum );

	PageTitleData getUserPhotoAlbumsPhotos( final UserPhotoAlbum photoAlbum );

	PageTitleData getFavoriteEntry( final User user, final FavoriteEntryType favoriteEntryType );

	PageTitleData getAddedToFavoritesByEntry( final User user );

	String getUserRootTranslated();

	PageTitleData getUserData( final User user, final String tran );

	PageTitleData getUserWrongLogin();

	PageTitleData getChangeUserPasswordData( final User user );

	PageTitleData getUserNotificationsControlData( final User user );

	PageTitleData getPhotosOfUserFavoriteMembers( final User user );
}
