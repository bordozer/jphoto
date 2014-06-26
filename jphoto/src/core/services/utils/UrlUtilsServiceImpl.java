package core.services.utils;

import admin.jobs.enums.SavedJobType;
import core.enums.PrivateMessageType;
import core.general.configuration.ConfigurationTab;
import core.general.user.UserMembershipType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class UrlUtilsServiceImpl implements UrlUtilsService {

	public static final String USERS_URL = "members";
	public static final String PHOTOS_URL = "photos";
	public static final String GENRES_URL = "genres";
	public static final String VOTING_CATEGORIES_URL = "votingcategories";
	public static final String USER_TEAM_URL = "team";
	public static final String USER_PHOTO_ALBUM = "albums";

	public static final String SITE_IMAGE_FOLDER_NAME = "images";

	public static final String UNDER_CONSTRUCTION_VIEW = "errors/UnderConstruction";
	public static final String ACCESS_DENIED_VIEW = "errors/AccessDenied";
	public static final String ENTRY_NOT_FOUND_VIEW = "errors/EntryNotFound";
	public static final String NOT_LOGGED_USER_VIEW = "errors/NotLoggedUser";
	public static final String NUDE_CONTENT_WARNING_VIEW = "NudeContentWarning";

	@Autowired
	private SystemVarsService systemVarsService;
	
	@Autowired
	private DateUtilsService dateUtilsService;

	@Override
	public String getBaseURL() {
		return String.format( "%s%s", systemVarsService.getProjectUrl(), systemVarsService.getTomcatWorkerName() );
	}

	@Override
	public String getBaseAdminURL() {
		return String.format( "%s%s", getBaseURL(), systemVarsService.getAdminPrefix() );
	}

	@Override
	public String getPortalPageURL() {
		return getBaseURL();
	}

	@Override
	public String getSiteImagesPath() {
		return String.format( "%s/%s", getBaseURL(), SITE_IMAGE_FOLDER_NAME );
	}

	@Override
	public String getPhotoNewLink() {
		return String.format( "%s/%s/new/", getBaseURL(), PHOTOS_URL );
	}

	@Override
	public String getAllPhotosLink() {
		return String.format( "%s/%s/", getBaseURL(), PHOTOS_URL );
	}

	@Override
	public String getPhotosByUserLink( final int userId ) {
		return String.format( "%s/%s/%s/%s/", getBaseURL(), PHOTOS_URL, USERS_URL, userId );
	}

	@Override
	public String getPhotosByUserLinkBest( final int userId ) {
		return String.format( "%sbest/", getPhotosByUserLink( userId ) );
	}

	@Override
	public String getPhotosByGenreLink( final int genreId ) {
		return String.format( "%s/%s/%s/%s/", getBaseURL(), PHOTOS_URL, GENRES_URL, genreId );
	}

	@Override
	public String getPhotosByGenreLinkBest( final int genreId ) {
		return String.format( "%sbest/", getPhotosByGenreLink( genreId ) );
	}

	@Override
	public String getPhotosByUserByGenreLink( final int userId, final int genreId ) {
		return String.format( "%s/%s/%s/%s/genre/%s/", getBaseURL(), PHOTOS_URL, USERS_URL, userId, genreId );
	}

	@Override
	public String getPhotosByUserByGenreLinkBest( final int userId, final int genreId ) {
		return String.format( "%sbest/", getPhotosByUserByGenreLink( userId, genreId ) );
	}

	@Override
	public String getPhotosUploadedOnDateUrl( final Date date ) {
		return String.format( "%s/%s/date/%s/uploaded/", getBaseURL(), PHOTOS_URL, dateUtilsService.formatDate( date ) );
	}

	@Override
	public String getPhotosUploadedInPeriodUrl( final Date dateFrom, final Date dateTo ) {
		final String fDateFrom = dateUtilsService.formatDate( dateFrom );
		final String fDateTo = dateUtilsService.formatDate( dateTo );
		return String.format( "%s/%s/from/%s/to/%s/uploaded/", getBaseURL(), PHOTOS_URL, fDateFrom, fDateTo );
	}

	@Override
	public String getPhotosBestOnDateUrl( final Date date ) {
		return String.format( "%s/%s/date/%s/best/", getBaseURL(), PHOTOS_URL, dateUtilsService.formatDate( date ) );
	}

	@Override
	public String getPhotosBestInPeriodUrl( final Date dateFrom, final Date dateTo ) {
		final String fDateFrom = dateUtilsService.formatDate( dateFrom );
		final String fDateTo = dateUtilsService.formatDate( dateTo );
		return String.format( "%s/%s/from/%s/to/%s/best/", getBaseURL(), PHOTOS_URL, fDateFrom, fDateTo );
	}

	@Override
	public String getPhotosAbsoluteBestURL() {
		return String.format( "%s/%s/best/", getBaseURL(), PHOTOS_URL );
	}

	@Override
	public String getPhotoCardLink( final int photoId ) {
		return String.format( "%s/%s/%s/card/", getBaseURL(), PHOTOS_URL, photoId );
	}

	@Override
	public String getPhotoEditLink( final int photoId ) {
		return String.format( "%s/%s/%s/edit/", getBaseURL(), PHOTOS_URL, photoId );
	}

	@Override
	public String getPhotoDeleteLink( final int photoId ) {
		return String.format( "%s/%s/%s/delete/", getBaseURL(), PHOTOS_URL, photoId );
	}

	@Override
	public String getPhotoVotingLink( final int photoId ) {
		return String.format( "%s/%s/%s/voting/", getBaseURL(), PHOTOS_URL, photoId );
	}

	@Override
	public String getPhotoInfoLink( final int photoId ) {
		return String.format( "%s/%s/%s/info/", getBaseURL(), PHOTOS_URL, photoId );
	}

	@Override
	public String getPhotoMarksListLink( final int photoId ) {
		return String.format( "%s/%s/%s/marks/", getBaseURL(), PHOTOS_URL, photoId );
	}

	@Override
	public String getPhotoPreviewsListLink( final int photoId ) {
		return String.format( "%s/%s/%s/previews/", getBaseURL(), PHOTOS_URL, photoId );
	}

	@Override
	public String getUserNewLink() {
		return String.format( "%s/%s/new/", getBaseURL(), USERS_URL );
	}

	@Override
	public String getUserLoginLink() {
		return String.format( "%s/%s/login/", getBaseURL(), USERS_URL );
	}

	@Override
	public String getAllUsersLink() {
		return String.format( "%s/%s/", getBaseURL(), USERS_URL );
	}

	@Override
	public String getFilterUsersLink() {
		return String.format( "%s/%s/filter/", getBaseURL(), USERS_URL );
	}

	@Override
	public String getUserCardLink( final int userId ) {
		return String.format( "%s/%s/%s/card/", getBaseURL(), USERS_URL, userId );
	}

	@Override
	public String getUserEditLink( final int userId ) {
		return String.format( "%s/%s/%s/edit/", getBaseURL(), USERS_URL, userId );
	}

	@Override
	public String getChangeUserPasswordLink( final int userId ) {
		return String.format( "%s/%s/%s/password/", getBaseURL(), USERS_URL, userId );
	}

	@Override
	public String getEditUserAvatarLink( final int userId ) {
		return String.format( "%s/%s/%s/avatar/", getBaseURL(), USERS_URL, userId );
	}

	@Override
	public String getUserPhotoAlbumListLink( final int userId ) {
		return String.format( "%s/%s/%s/albums/", getBaseURL(), USERS_URL, userId );
	}

	@Override
	public String getUserPhotoAlbumPhotosLink( final int userId, final int albumId ) {
		return String.format( "%s/%s/%d/albums/%d/", getBaseURL(), USERS_URL, userId, albumId );
	}

	@Override
	public String getUserDisableLink( final int userId ) {
		return String.format( "%s/%s/%s/disable/", getBaseURL(), USERS_URL, userId );
	}

	@Override
	public String getUseEnableLink( final int userId ) {
		return String.format( "%s/%s/%s/enable/", getBaseURL(), USERS_URL, userId );
	}

	@Override
	public String getUserTechLink( final int userId ) {
		return String.format( "%s/%s/%s/tech/", getBaseURL(), USERS_URL, userId );
	}

	@Override
	public String getUserFavoritePhotosLink( final int userId ) {
		return String.format( "%s/%s/%s/favorites/photos/", getBaseURL(), USERS_URL, userId );
	}

	@Override
	public String getUserFavoriteMembersLink( final int userId ) {
		return String.format( "%s/%s/%s/favorites/members/", getBaseURL(), USERS_URL, userId );
	}

	@Override
	public String getUserPhotosOfFavoriteMembersLink( final int userId ) {
		return String.format( "%s/%s/%s/favorites/members/photos/", getBaseURL(), USERS_URL, userId );
	}

	@Override
	public String getUsersQtyWhoAddedInFavoriteMembersLink( final int userId ) {
		return String.format( "%s/%s/%s/favorites/members/in/", getBaseURL(), USERS_URL, userId );
	}

	@Override
	public String getUserFavoriteFriendsLink( final int userId ) {
		return String.format( "%s/%s/%s/friends/", getBaseURL(), USERS_URL, userId );
	}

	@Override
	public String getUserFavoriteBlackListLink( final int userId ) {
		return String.format( "%s/%s/%s/blacklist/", getBaseURL(), USERS_URL, userId );
	}

	@Override
	public String getUserBookmarkedPhotosLink( final int userId ) {
		return String.format( "%s/%s/%s/bookmark/", getBaseURL(), USERS_URL, userId );
	}

	@Override
	public String getPhotosWithSubscribeOnNewCommentsLink( final int userId ) {
		return String.format( "%s/%s/%s/notification/comments/", getBaseURL(), USERS_URL, userId );
	}

	@Override
	public String getWrittenComments( final int userId ) {
		return String.format( "%s/%s/%s/comments/written/", getBaseURL(), USERS_URL, userId );
	}

	@Override
	public String getReceivedComments( final int userId ) {
		return String.format( "%s/%s/%s/comments/received/", getBaseURL(), USERS_URL, userId );
	}

	@Override
	public String getReceivedUnreadComments( final int userId ) {
		return String.format( "%s/%s/%s/comments/received/unread/", getBaseURL(), USERS_URL, userId );
	}

	@Override
	public String getPrivateMessagesList( final int userId ) {
		return String.format( "%s/%s/%s/messages/", getBaseURL(), USERS_URL, userId );
	}

	@Override
	public String getPrivateMessagesList( final int userId, final PrivateMessageType messageType ) {
		return String.format( "%s/%s/%s/messages/%d/", getBaseURL(), USERS_URL, userId, messageType.getId() );
	}

	@Override
	public String getUsersNewPhotosNotificationMenuLink( final int userId ) {
		return String.format( "%s/%s/%s/notification/photos/", getBaseURL(), USERS_URL, userId );
	}

	@Override
	public String getPhotosByMembership( final UserMembershipType membershipType, final String url ) {
		return String.format( "%s/%s/type/%s/", getBaseURL(), url, membershipType.getId() );
	}

	@Override
	public String getActivityStreamUrl() {
		return String.format( "%s/activityStream/", getBaseURL() );
	}

	@Override
	public String getPhotosByMembershipBest( final UserMembershipType membershipType, final String url ) {
		return String.format( "%sbest/", getPhotosByMembership( membershipType, url ) );
	}

	@Override
	public String getPhotosVotedByUserLink( final int userId ) {
		return String.format( "%s/%s/%s/%s/category/", getBaseURL(), PHOTOS_URL, USERS_URL, userId );
	}

	@Override
	public String getPhotosByUserByVotingCategoryLink( final int userId, final int votingCategoryId ) {
		return String.format( "%s/%s/%s/%s/category/%s/", getBaseURL(), PHOTOS_URL, USERS_URL, userId, votingCategoryId );
	}

	@Override
	public String getUserTeamMembersLink( final int userId ) {
		return String.format( "%s/%s/%s/%s/", getBaseURL(), USERS_URL, userId, USER_TEAM_URL );
	}

	@Override
	public String getUserTeamNewMemberLink( final int userId ) {
		return String.format( "%s/%s/%s/%s/new/", getBaseURL(), USERS_URL, userId, USER_TEAM_URL );
	}

	@Override
	public String getUserTeamMemberEditLink( final int userId, final int userTeamMemberId ) {
		return String.format( "%s/%s/%s/%s/%s/edit/", getBaseURL(), USERS_URL, userId, USER_TEAM_URL, userTeamMemberId );
	}

	@Override
	public String getUserTeamMemberCardLink( final int userId, final int userTeamMemberId ) {
		return String.format( "%s/%s/%s/%s/%s/", getBaseURL(), USERS_URL, userId, USER_TEAM_URL, userTeamMemberId );
	}

	@Override
	public String getUserTeamMemberDeleteLink( final int userId, final int userTeamMemberId ) {
		return String.format( "%s/%s/%s/%s/%s/delete/", getBaseURL(), USERS_URL, userId, USER_TEAM_URL, userTeamMemberId );
	}

	@Override
	public String getUserPhotoAlbumNewLink( final int userId ) {
		return String.format( "%s/%s/%s/%s/new/", getBaseURL(), USERS_URL, userId, USER_PHOTO_ALBUM );
	}

	@Override
	public String getUserPhotoAlbumEditLink( final int userId, final int albumId ) {
		return String.format( "%s/%s/%d/%s/%d/edit/", getBaseURL(), USERS_URL, userId, USER_PHOTO_ALBUM, albumId );
	}

	@Override
	public String getUserPhotoAlbumDeleteLink( final int userId, final int albumId ) {
		return String.format( "%s/%s/%d/%s/%d/delete/", getBaseURL(), USERS_URL, userId, USER_PHOTO_ALBUM, albumId );
	}

	@Override
	public String getUserNotificationsMenu( final int userId ) {
		return String.format( "%s/members/%d/notifications/", getBaseURL(), userId );
	}

	@Override
	public String getAdminGenreListLink() {
		return String.format( "%s/%s/", getBaseAdminURL(), GENRES_URL );
	}

	@Override
	public String getAdminGenreNewLink() {
		return String.format( "%s/%s/new/", getBaseAdminURL(), GENRES_URL );
	}

	@Override
	public String getAdminGenreEditLink( final int genreId ) {
		return String.format( "%s/%s/%s/edit/", getBaseAdminURL(), GENRES_URL, genreId );
	}

	@Override
	public String getAdminGenreDeleteLink( final int genreId ) {
		return String.format( "%s/%s/%s/delete/", getBaseAdminURL(), GENRES_URL, genreId );
	}

	@Override
	public String getAdminAnonymousDaysLink() {
		return String.format( "%s/anonymousDays/", getBaseAdminURL() );
	}

	@Override
	public String getAdminJobsLink() {
		return String.format( "%s/jobs/", getBaseAdminURL() );
	}

	@Override
	public String getAdminSchedulerTaskListLink() {
		return String.format( "%s/scheduler/tasks/", getBaseAdminURL() );
	}

	@Override
	public String getAdminUpgradeLink() {
		return String.format( "%s/upgrade/", getBaseAdminURL() );
	}

	@Override
	public String getBestPhotosMenuLink() {
		return String.format( "%s/menu/", getBaseAdminURL() );
	}

	@Override
	public String getAdminSchedulerTaskNewLink() {
		return String.format( "%s/scheduler/tasks/new/", getBaseAdminURL() );
	}

	@Override
	public String getAdminSchedulerRunLink() {
		return String.format( "%s/scheduler/run/", getBaseAdminURL() );
	}

	@Override
	public String getAdminSchedulerStopLink() {
		return String.format( "%s/scheduler/stop/", getBaseAdminURL() );
	}

	@Override
	public String getAdminSystemConfigurationListLink() {
		return String.format( "%s/configuration/", getBaseAdminURL() );
	}

	@Override
	public String getAdminSystemConfigurationNewLink() {
		return String.format( "%s/configuration/new/", getBaseAdminURL() );
	}

	@Override
	public String getAdminConfigurationTabsLink( final int systemConfigurationId ) {
		return getAdminConfigurationTabLink( systemConfigurationId, ConfigurationTab.ALL.getKey() );
	}

	@Override
	public String getAdminConfigurationTabLink( final int systemConfigurationId, final String tabKey ) {
		return String.format( "%s/configuration/%s/tabs/%s/info/", getBaseAdminURL(), systemConfigurationId, tabKey );
	}

	@Override
	public String getAdminConfigurationListLink() {
		return String.format( "%s/configuration/", getBaseAdminURL() );
	}

	@Override
	public String getAdminConfigurationEditLink( final int systemConfigurationId ) {
		return String.format( "%s/configuration/%s/edit/", getBaseAdminURL(), systemConfigurationId );
	}

	@Override
	public String getAdminVotingCategoriesLink() {
		return String.format( "%s/%s/", getBaseAdminURL(), VOTING_CATEGORIES_URL );
	}

	@Override
	public String getAdminTranslatorLink() {
		return String.format( "%s/translator/", getBaseAdminURL() );
	}

	@Override
	public String getAdminUntranslatedLink() {
		return String.format( "%s" + "untranslated/", getAdminTranslatorLink() );
	}

	@Override
	public String getAdminControlPanelLink() {
		return String.format( "%s/control-panel/", getBaseAdminURL() );
	}

	@Override
	public String getAdminVotingCategoryNewLink() {
		return String.format( "%s/%s/new/", getBaseAdminURL(), VOTING_CATEGORIES_URL );
	}

	@Override
	public String getAdminVotingCategoryEditLink( final int votingCategoryId ) {
		return String.format( "%s/%s/%s/edit/", getBaseAdminURL(), VOTING_CATEGORIES_URL, votingCategoryId );
	}

	@Override
	public String getAdminVotingCategoryDeleteLink( final int votingCategoryId ) {
		return String.format( "%s/%s/%s/delete/", getBaseAdminURL(), VOTING_CATEGORIES_URL, votingCategoryId );
	}

	@Override
	public String getAdminSavedJobEditLink( final SavedJobType jobType, final int savedJobId ) {
		return String.format( "%s/jobs/%s/%s/edit/", getBaseAdminURL(), jobType.getPrefix(), savedJobId );
	}

	@Override
	public String getAdminSavedJobProgressLink( final SavedJobType jobType, final int savedJobId ) {
		return String.format( "%s/jobs/%s/progress/%d/", getBaseAdminURL(), jobType.getPrefix(), savedJobId );
	}

	@Override
	public String getAdminGenresTranslationsUrl() {
		return String.format( "%s/translations/custom/genres/", getBaseAdminURL() );
	}

	@Override
	public String getAdminVotingCategoriesTranslationsUrl() {
		return String.format( "%s/translations/custom/voting-categories/", getBaseAdminURL() );
	}

	public void setSystemVarsService( final SystemVarsService systemVarsService ) {
		this.systemVarsService = systemVarsService;
	}

	public void setDateUtilsService( final DateUtilsService dateUtilsService ) {
		this.dateUtilsService = dateUtilsService;
	}
}
