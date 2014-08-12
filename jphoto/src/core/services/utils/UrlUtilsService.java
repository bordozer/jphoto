package core.services.utils;

import admin.jobs.enums.SavedJobType;
import core.enums.PrivateMessageType;
import core.general.user.UserMembershipType;

import java.util.Date;

public interface UrlUtilsService {

	String BEAN_NAME = "urlUtilsService";

	String getBaseURL();

	String getBaseAdminURL();

	String getPortalPageURL();

	String getSiteImagesPath();

	String getPhotoNewLink();

	String getAllPhotosLink();

	String getPhotosByUserLink( final int userId );

	String getPhotosByUserLinkBest( final int userId );

	String getPhotosByGenreLink( final int genreId );

	String getPhotosByGenreLinkBest( final int genreId );

	String getPhotosByUserByGenreLink( final int userId, final int genreId );

	String getPhotosByUserByGenreLinkBest( int userId, int genreId );

	String getPhotosUploadedOnDateUrl( final Date date );

	String getPhotosUploadedInPeriodUrl( final Date dateFrom, final Date dateTo );

	String getPhotosBestOnDateUrl( final Date date );

	String getPhotosBestInPeriodUrl( final Date dateFrom, final Date dateTo );

	String getPhotosAbsoluteBestURL();

	String getPhotoCardLink( final int photoId );

	String getPhotoEditLink( final int photoId );

	String getPhotoDeleteLink( final int photoId );

	String getPhotoVotingLink( final int photoId );

	String getPhotoInfoLink( final int photoId );

	String getPhotoMarksListLink( final int photoId );

	String getPhotoPreviewsListLink( final int photoId );

	String getUserNewLink();

	String getUserLoginLink();

	String getAllUsersLink();

	String getFilterUsersLink();

	String getUserCardLink( final int userId );

	String getUserEditLink( final int userId );

	String getChangeUserPasswordLink( final int userId );

	String getEditUserAvatarLink( final int userId );

	String getUserPhotoAlbumListLink( final int userId );

	String getUserPhotoAlbumPhotosLink( final int userId, final int albumId );

	String getUserDisableLink( final int userId );

	String getUseEnableLink( final int userId );

	String getUserTechLink( final int userId );

	String getUserFavoritePhotosLink( final int userId );

	String getUserFavoriteMembersLink( final int userId );

	String getUserPhotosOfFavoriteMembersLink( final int userId );

	String getUsersQtyWhoAddedInFavoriteMembersLink( final int userId );

	String getUserFavoriteFriendsLink( final int userId );

	String getUserFavoriteBlackListLink( final int userId );

	String getUserBookmarkedPhotosLink( final int userId );

	String getPhotosWithSubscribeOnNewCommentsLink( final int userId );

	String getWrittenComments( final int userId );

	String getReceivedComments( final int userId );

	String getReceivedUnreadComments( final int userId );

	String getPrivateMessagesList( final int userId );

	String getPrivateMessagesList( final int userId, PrivateMessageType messageType );

	String getUsersNewPhotosNotificationMenuLink( final int userId );

	String getPhotosByMembership( UserMembershipType membershipType, String url );

	String getActivityStreamUrl();

	String getPhotosByMembershipBest( UserMembershipType membershipType, String url );

	String getPhotosVotedByUserLink( final int userId );

	String getPhotosByUserByVotingCategoryLink( final int userId, final int votingCategoryId );

	String getUserTeamMembersLink( final int userId );

	String getUserTeamNewMemberLink( final int userId );

	String getUserTeamMemberEditLink( final int userId, final int userTeamMemberId );

	String getUserTeamMemberCardLink( final int userId, final int userTeamMemberId );

	String getUserTeamMemberDeleteLink( final int userId, final int userTeamMemberId );

	String getUserPhotoAlbumNewLink( final int userId );

	String getUserPhotoAlbumEditLink( final int userId, final int albumId );

	String getUserPhotoAlbumDeleteLink( final int userId, final int albumId );

	String getUserNotificationsMenu( final int userId );

	String getAdminGenreListLink();

	String getAdminGenreNewLink();

	String getAdminGenreEditLink( final int genreId );

	String getAdminGenreDeleteLink( final int genreId );

	String getAdminAnonymousDaysLink();

	String getRestrictionListLink();

	String getAdminJobsLink();

	String getAdminSchedulerTaskListLink();

	String getAdminUpgradeLink();

	String getBestPhotosMenuLink();

	String getAdminSchedulerTaskNewLink();

	String getAdminSchedulerRunLink();

	String getAdminSchedulerStopLink();

	String getAdminSystemConfigurationListLink();

	String getAdminSystemConfigurationNewLink();

	String getAdminConfigurationTabsLink( final int systemConfigurationId );

	String getAdminConfigurationTabLink( final int systemConfigurationId, String tabKey );

	String getAdminConfigurationListLink();

	String getAdminConfigurationEditLink( final int systemConfigurationId );

	String getAdminVotingCategoriesLink();

	String getAdminTranslatorLink();

	String getAdminUntranslatedLink();

	String getAdminControlPanelLink();

	String getAdminVotingCategoryNewLink();

	String getAdminVotingCategoryEditLink( final int votingCategoryId );

	String getAdminVotingCategoryDeleteLink( final int votingCategoryId );

	String getAdminSavedJobEditLink( SavedJobType jobType, final int savedJobId );

	String getAdminSavedJobProgressLink( SavedJobType jobType, final int savedJobId );

	String getAdminGenresTranslationsUrl();

	String getAdminVotingCategoriesTranslationsUrl();

	String getGenreListLink();
}
