package core.services.pageTitle;

import core.enums.UserCardTab;
import core.enums.FavoriteEntryType;
import core.general.genre.Genre;
import core.general.user.User;
import core.general.user.UserMembershipType;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.general.user.userTeam.UserTeamMember;
import elements.PageTitleData;

public interface PageTitleUserUtilsService {

	PageTitleData getUserListData();

	PageTitleData getUserNewData();

	PageTitleData getVotesForUserRankInGenreData( final User user, final Genre genre );

	PageTitleData getUserEditData( final User user );

	PageTitleData setUserAvatarData( final User user );

	PageTitleData getUserCardData( final User user, final UserCardTab userCardTab );

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
}
