package core.services.utils;

import admin.jobs.enums.SavedJobType;
import core.enums.PrivateMessageType;
import core.general.user.UserMembershipType;
import core.general.configuration.ConfigurationTab;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class UrlUtilsServiceImpl implements UrlUtilsService {

	public static final String USERS_URL = "members";
	public static final String PHOTOS_URL = "photos";
	public static final String GENRES_URL = "genres";
	public static final String VOTING_CATEGORIES_URL = "votingcategories";
	public static final String USER_TEAM_URL = "team";
	public static final String USER_PHOTO_ALBUM = "albums";

	public static final String SITE_IMAGE_FOLDER_NAME = "common/images";

	public static final String UNDER_CONSTRUCTION_VIEW = "errors/UnderConstruction";
	public static final String ACCESS_DENIED_VIEW = "errors/AccessDenied";
	public static final String ENTRY_NOT_FOUND_VIEW = "errors/EntryNotFound";
	public static final String NOT_LOGGED_USER_VIEW = "errors/NotLoggedUser";
	public static final String NUDE_CONTENT_WARNING_VIEW = "NudeContentWarning";

	private static final String PORTAL_PAGE_ROOT_FOLDER = "";

	@Autowired
	private SystemVarsService systemVarsService;
	
	@Autowired
	private DateUtilsService dateUtilsService;

	private String getContextName() {
		return systemVarsService.getTomcatWorkerName();
	}

	private String getContextWithPrefix() {
		return String.format( "%s/%s", getContextName(), systemVarsService.getApplicationPrefix() );
	}

	@Override
	public String getServerUrl() {
		return systemVarsService.getProjectUrl();
	}

	@Override
	public String getServerUrlClosed() {
		return systemVarsService.getProjectUrlClosed();
	}

	@Override
	public String getBaseURLWithPrefix() {
		return getServerUrlClosed() + getContextWithPrefix();
	}

	private String getAdminContextWithPrefix() {
		return String.format( "%s/%s", getContextName(), systemVarsService.getAdminPrefix() );
	}

	@Override
	public String getAdminURLWithPrefix() {
		return getServerUrlClosed() + getAdminContextWithPrefix();
	}

	@Override
	public String getAdminBaseURLWithPrefix() {
		return getServerUrlClosed() + getAdminContextWithPrefix();
	}

	@Override
	public String getBaseURL() {
		return getServerUrlClosed() + getContextName();
	}

	@Override
	public String getBaseURLWithPrefixClosed() {
		return String.format( "%s%s/", getServerUrlClosed(), getContextWithPrefix() );
	}

	@Override
	public String getPortalPageURL() {
		return String.format( "%s%s/%s", getServerUrlClosed(), getContextWithPrefix(), PORTAL_PAGE_ROOT_FOLDER );
	}

	@Override
	public String getCSSFolderUrl() {
		return String.format( "/%s/", getContextWithPrefix() );
	}

	@Override
	public String getSiteImagesPath() {
		return String.format( "%s%s/%s", systemVarsService.getProjectUrlClosed(), getContextName(), SITE_IMAGE_FOLDER_NAME );
	}

	@Override
	public String getPhotoNewLink() {
		return String.format( "%s/%s/new/", getBaseURLWithPrefix(), PHOTOS_URL );
	}

	@Override
	public String getAllPhotosLink() {
		return String.format( "%s/%s/", getBaseURLWithPrefix(), PHOTOS_URL );
	}

	@Override
	public String getPhotosByUserLink( final int userId ) {
		return String.format( "%s/%s/%s/%s/", getBaseURLWithPrefix(), PHOTOS_URL, USERS_URL, userId );
	}

	@Override
	public String getPhotosByUserLinkBest( final int userId ) {
		return String.format( "%sbest/", getPhotosByUserLink( userId ) );
	}

	@Override
	public String getPhotosByGenreLink( final int genreId ) {
		return String.format( "%s/%s/%s/%s/", getBaseURLWithPrefix(), PHOTOS_URL, GENRES_URL, genreId );
	}

	@Override
	public String getPhotosByGenreLinkBest( final int genreId ) {
		return String.format( "%sbest/", getPhotosByGenreLink( genreId ) );
	}

	@Override
	public String getPhotosByUserByGenreLink( final int userId, final int genreId ) {
		return String.format( "%s/%s/%s/%s/genre/%s/", getBaseURLWithPrefix(), PHOTOS_URL, USERS_URL, userId, genreId );
	}

	@Override
	public String getPhotosByUserByGenreLinkBest( final int userId, final int genreId ) {
		return String.format( "%sbest/", getPhotosByUserByGenreLink( userId, genreId ) );
	}

	@Override
	public String getPhotosUploadedOnDateUrl( final Date date ) {
		return String.format( "%s/%s/date/%s/uploaded/", getBaseURLWithPrefix(), PHOTOS_URL, dateUtilsService.formatDate( date ) );
	}

	@Override
	public String getPhotosUploadedInPeriodUrl( final Date dateFrom, final Date dateTo ) {
		final String fDateFrom = dateUtilsService.formatDate( dateFrom );
		final String fDateTo = dateUtilsService.formatDate( dateTo );
		return String.format( "%s/%s/from/%s/to/%s/uploaded/", getBaseURLWithPrefix(), PHOTOS_URL, fDateFrom, fDateTo );
	}

	@Override
	public String getPhotosBestOnDateUrl( final Date date ) {
		return String.format( "%s/%s/date/%s/best/", getBaseURLWithPrefix(), PHOTOS_URL, dateUtilsService.formatDate( date ) );
	}

	@Override
	public String getPhotosBestInPeriodUrl( final Date dateFrom, final Date dateTo ) {
		final String fDateFrom = dateUtilsService.formatDate( dateFrom );
		final String fDateTo = dateUtilsService.formatDate( dateTo );
		return String.format( "%s/%s/from/%s/to/%s/best/", getBaseURLWithPrefix(), PHOTOS_URL, fDateFrom, fDateTo );
	}

	@Override
	public String getPhotosAbsoluteBestURL() {
		return String.format( "%s/%s/best/", getBaseURLWithPrefix(), PHOTOS_URL );
	}

	@Override
	public String getPhotoCardLink( final int photoId ) {
		return String.format( "%s/%s/%s/card/", getBaseURLWithPrefix(), PHOTOS_URL, photoId );
	}

	@Override
	public String getPhotoEditLink( final int photoId ) {
		return String.format( "%s/%s/%s/edit/", getBaseURLWithPrefix(), PHOTOS_URL, photoId );
	}

	@Override
	public String getPhotoDeleteLink( final int photoId ) {
		return String.format( "%s/%s/%s/delete/", getBaseURLWithPrefix(), PHOTOS_URL, photoId );
	}

	@Override
	public String getPhotoVotingLink( final int photoId ) {
		return String.format( "%s/%s/%s/voting/", getBaseURLWithPrefix(), PHOTOS_URL, photoId );
	}

	@Override
	public String getPhotoInfoLink( final int photoId ) {
		return String.format( "%s/%s/%s/info/", getBaseURLWithPrefix(), PHOTOS_URL, photoId );
	}

	@Override
	public String getPhotoMarksListLink( final int photoId ) {
		return String.format( "%s/%s/%s/marks/", getBaseURLWithPrefix(), PHOTOS_URL, photoId );
	}

	@Override
	public String getPhotoPreviewsListLink( final int photoId ) {
		return String.format( "%s/%s/%s/previews/", getBaseURLWithPrefix(), PHOTOS_URL, photoId );
	}

	@Override
	public String getUserNewLink() {
		return String.format( "%s/%s/new/", getBaseURLWithPrefix(), USERS_URL );
	}

	@Override
	public String getUserLoginLink() {
		return String.format( "%s/%s/login/", getBaseURLWithPrefix(), USERS_URL );
	}

	@Override
	public String getAllUsersLink() {
		return String.format( "%s/%s/", getBaseURLWithPrefix(), USERS_URL );
	}

	@Override
	public String getFilterUsersLink() {
		return String.format( "%s/%s/filter/", getBaseURLWithPrefix(), USERS_URL );
	}

	@Override
	public String getUserCardLink( final int userId ) {
		return String.format( "%s/%s/%s/card/", getBaseURLWithPrefix(), USERS_URL, userId );
	}

	@Override
	public String getUserEditLink( final int userId ) {
		return String.format( "%s/%s/%s/edit/", getBaseURLWithPrefix(), USERS_URL, userId );
	}

	@Override
	public String getChangeUserPasswordLink( final int userId ) {
		return String.format( "%s/%s/%s/password/", getBaseURLWithPrefix(), USERS_URL, userId );
	}

	@Override
	public String getEditUserAvatarLink( final int userId ) {
		return String.format( "%s/%s/%s/avatar/", getBaseURLWithPrefix(), USERS_URL, userId );
	}

	@Override
	public String getUserPhotoAlbumListLink( final int userId ) {
		return String.format( "%s/%s/%s/albums/", getBaseURLWithPrefix(), USERS_URL, userId );
	}

	@Override
	public String getUserPhotoAlbumPhotosLink( final int userId, final int albumId ) {
		return String.format( "%s/%s/%d/albums/%d/", getBaseURLWithPrefix(), USERS_URL, userId, albumId );
	}

	@Override
	public String getUserDisableLink( final int userId ) {
		return String.format( "%s/%s/%s/disable/", getBaseURLWithPrefix(), USERS_URL, userId );
	}

	@Override
	public String getUseEnableLink( final int userId ) {
		return String.format( "%s/%s/%s/enable/", getBaseURLWithPrefix(), USERS_URL, userId );
	}

	@Override
	public String getUserTechLink( final int userId ) {
		return String.format( "%s/%s/%s/tech/", getBaseURLWithPrefix(), USERS_URL, userId );
	}

	@Override
	public String getUserFavoritePhotosLink( final int userId ) {
		return String.format( "%s/%s/%s/favorites/photos/", getBaseURLWithPrefix(), USERS_URL, userId );
	}

	@Override
	public String getUserFavoriteMembersLink( final int userId ) {
		return String.format( "%s/%s/%s/favorites/members/", getBaseURLWithPrefix(), USERS_URL, userId );
	}

	@Override
	public String getUsersQtyWhoAddedInFavoriteMembersLink( final int userId ) {
		return String.format( "%s/%s/%s/favorites/members/in/", getBaseURLWithPrefix(), USERS_URL, userId );
	}

	@Override
	public String getUserFavoriteFriendsLink( final int userId ) {
		return String.format( "%s/%s/%s/friends/", getBaseURLWithPrefix(), USERS_URL, userId );
	}

	@Override
	public String getUserFavoriteBlackListLink( final int userId ) {
		return String.format( "%s/%s/%s/blacklist/", getBaseURLWithPrefix(), USERS_URL, userId );
	}

	@Override
	public String getUserBookmarkedPhotosLink( final int userId ) {
		return String.format( "%s/%s/%s/bookmark/", getBaseURLWithPrefix(), USERS_URL, userId );
	}

	@Override
	public String getPhotosWithSubscribeOnNewCommentsLink( final int userId ) {
		return String.format( "%s/%s/%s/notification/comments/", getBaseURLWithPrefix(), USERS_URL, userId );
	}

	@Override
	public String getUserCommentsList( final int userId ) {
		return String.format( "%s/%s/%s/comments/", getBaseURLWithPrefix(), USERS_URL, userId );
	}

	@Override
	public String getCommentsToUserList( final int userId ) {
		return String.format( "%s/%s/%s/comments/to/", getBaseURLWithPrefix(), USERS_URL, userId );
	}

	@Override
	public String getUnreadCommentsToUserList( final int userId ) {
		return String.format( "%s/%s/%s/comments/to/unread/", getBaseURLWithPrefix(), USERS_URL, userId );
	}

	@Override
	public String getPrivateMessagesList( final int userId ) {
		return String.format( "%s/%s/%s/messages/", getBaseURLWithPrefix(), USERS_URL, userId );
	}

	@Override
	public String getPrivateMessagesList( final int userId, final PrivateMessageType messageType ) {
		return String.format( "%s/%s/%s/messages/%d/", getBaseURLWithPrefix(), USERS_URL, userId, messageType.getId() );
	}

	@Override
	public String getUsersNewPhotosNotificationMenuLink( final int userId ) {
		return String.format( "%s/%s/%s/notification/photos/", getBaseURLWithPrefix(), USERS_URL, userId );
	}

	@Override
	public String getPhotosByMembership( final UserMembershipType membershipType, final String url ) {
		return String.format( "%s/%s/type/%s/", getBaseURLWithPrefix(), url, membershipType.getId() );
	}

	@Override
	public String getActivityStreamUrl() {
		return String.format( "%s/activityStream/", getBaseURLWithPrefix() );
	}

	@Override
	public String getPhotosByMembershipBest( final UserMembershipType membershipType, final String url ) {
		return String.format( "%sbest/", getPhotosByMembership( membershipType, url ) );
	}

	@Override
	public String getPhotosVotedByUserLink( final int userId ) {
		return String.format( "%s/%s/%s/%s/category/", getBaseURLWithPrefix(), PHOTOS_URL, USERS_URL, userId );
	}

	@Override
	public String getPhotosByUserByVotingCategoryLink( final int userId, final int votingCategoryId ) {
		return String.format( "%s/%s/%s/%s/category/%s/", getBaseURLWithPrefix(), PHOTOS_URL, USERS_URL, userId, votingCategoryId );
	}

	@Override
	public String getUserTeamMembersLink( final int userId ) {
		return String.format( "%s/%s/%s/%s/", getBaseURLWithPrefix(), USERS_URL, userId, USER_TEAM_URL );
	}

	@Override
	public String getUserTeamNewMemberLink( final int userId ) {
		return String.format( "%s/%s/%s/%s/new/", getBaseURLWithPrefix(), USERS_URL, userId, USER_TEAM_URL );
	}

	@Override
	public String getUserTeamMemberEditLink( final int userId, final int userTeamMemberId ) {
		return String.format( "%s/%s/%s/%s/%s/edit/", getBaseURLWithPrefix(), USERS_URL, userId, USER_TEAM_URL, userTeamMemberId );
	}

	@Override
	public String getUserTeamMemberCardLink( final int userId, final int userTeamMemberId ) {
		return String.format( "%s/%s/%s/%s/%s/", getBaseURLWithPrefix(), USERS_URL, userId, USER_TEAM_URL, userTeamMemberId );
	}

	@Override
	public String getUserTeamMemberDeleteLink( final int userId, final int userTeamMemberId ) {
		return String.format( "%s/%s/%s/%s/%s/delete/", getBaseURLWithPrefix(), USERS_URL, userId, USER_TEAM_URL, userTeamMemberId );
	}

	@Override
	public String getUserPhotoAlbumNewLink( final int userId ) {
		return String.format( "%s/%s/%s/%s/new/", getBaseURLWithPrefix(), USERS_URL, userId, USER_PHOTO_ALBUM );
	}

	@Override
	public String getUserPhotoAlbumEditLink( final int userId, final int albumId ) {
		return String.format( "%s/%s/%d/%s/%d/edit/", getBaseURLWithPrefix(), USERS_URL, userId, USER_PHOTO_ALBUM, albumId );
	}

	@Override
	public String getUserPhotoAlbumDeleteLink( final int userId, final int albumId ) {
		return String.format( "%s/%s/%d/%s/%d/delete/", getBaseURLWithPrefix(), USERS_URL, userId, USER_PHOTO_ALBUM, albumId );
	}

	@Override
	public String getUserNotificationsMenu( final int userId ) {
		return String.format( "%s/members/%d/notifications/", getBaseURLWithPrefix(), userId );
	}

	@Override
	public String getAdminGenreListLink() {
		return String.format( "%s/%s/", getAdminBaseURLWithPrefix(), GENRES_URL );
	}

	@Override
	public String getAdminGenreNewLink() {
		return String.format( "%s/%s/new/", getAdminBaseURLWithPrefix(), GENRES_URL );
	}

	@Override
	public String getAdminGenreEditLink( final int genreId ) {
		return String.format( "%s/%s/%s/edit/", getAdminBaseURLWithPrefix(), GENRES_URL, genreId );
	}

	@Override
	public String getAdminGenreDeleteLink( final int genreId ) {
		return String.format( "%s/%s/%s/delete/", getAdminBaseURLWithPrefix(), GENRES_URL, genreId );
	}

	@Override
	public String getAdminAnonymousDaysLink() {
		return String.format( "%s/anonymousDays/", getAdminBaseURLWithPrefix() );
	}

	@Override
	public String getAdminJobsLink() {
		return String.format( "%s/jobs/", getAdminBaseURLWithPrefix() );
	}

	@Override
	public String getAdminSchedulerTaskListLink() {
		return String.format( "%s/scheduler/tasks/", getAdminBaseURLWithPrefix() );
	}

	@Override
	public String getAdminUpgradeLink() {
		return String.format( "%s/upgrade/", getAdminBaseURLWithPrefix() );
	}

	@Override
	public String getBestPhotosMenuLink() {
		return String.format( "%s/menu/", getAdminBaseURLWithPrefix() );
	}

	@Override
	public String getAdminSchedulerTaskNewLink() {
		return String.format( "%s/scheduler/tasks/new/", getAdminBaseURLWithPrefix() );
	}

	@Override
	public String getAdminSchedulerRunLink() {
		return String.format( "%s/scheduler/run/", getAdminBaseURLWithPrefix() );
	}

	@Override
	public String getAdminSchedulerStopLink() {
		return String.format( "%s/scheduler/stop/", getAdminBaseURLWithPrefix() );
	}

	@Override
	public String getAdminSystemConfigurationListLink() {
		return String.format( "%s/configuration/", getAdminBaseURLWithPrefix() );
	}

	@Override
	public String getAdminSystemConfigurationNewLink() {
		return String.format( "%s/configuration/new/", getAdminBaseURLWithPrefix() );
	}

	@Override
	public String getAdminConfigurationTabsLink( final int systemConfigurationId ) {
		return getAdminConfigurationTabLink( systemConfigurationId, ConfigurationTab.ALL.getKey() );
	}

	@Override
	public String getAdminConfigurationTabLink( final int systemConfigurationId, final String tabKey ) {
		return String.format( "%s/configuration/%s/tabs/%s/info/", getAdminBaseURLWithPrefix(), systemConfigurationId, tabKey );
	}

	@Override
	public String getAdminConfigurationListLink() {
		return String.format( "%s/configuration/", getAdminBaseURLWithPrefix() );
	}

	@Override
	public String getAdminConfigurationEditLink( final int systemConfigurationId ) {
		return String.format( "%s/configuration/%s/edit/", getAdminBaseURLWithPrefix(), systemConfigurationId );
	}

	@Override
	public String getAdminVotingCategoriesLink() {
		return String.format( "%s/%s/", getAdminBaseURLWithPrefix(), VOTING_CATEGORIES_URL );
	}

	@Override
	public String getAdminVotingCategoryNewLink() {
		return String.format( "%s/%s/new/", getAdminBaseURLWithPrefix(), VOTING_CATEGORIES_URL );
	}

	@Override
	public String getAdminVotingCategoryEditLink( final int votingCategoryId ) {
		return String.format( "%s/%s/%s/edit/", getAdminBaseURLWithPrefix(), VOTING_CATEGORIES_URL, votingCategoryId );
	}

	@Override
	public String getAdminVotingCategoryDeleteLink( final int votingCategoryId ) {
		return String.format( "%s/%s/%s/delete/", getAdminBaseURLWithPrefix(), VOTING_CATEGORIES_URL, votingCategoryId );
	}

	@Override
	public String getAdminSavedJobEditLink( final SavedJobType jobType, final int savedJobId ) {
		return String.format( "%s/jobs/%s/%s/edit/", getAdminBaseURLWithPrefix(), jobType.getPrefix(), savedJobId );
	}

	@Override
	public String getAdminSavedJobProgressLink( final SavedJobType jobType, final int savedJobId ) {
		return String.format( "%s/jobs/%s/progress/%d/", getAdminBaseURLWithPrefix(), jobType.getPrefix(), savedJobId );
	}
}
