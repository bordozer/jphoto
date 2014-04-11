package core.services.utils;

import admin.jobs.enums.SavedJobType;
import admin.jobs.general.SavedJob;
import core.general.configuration.ConfigurationTab;
import core.general.configuration.SystemConfiguration;
import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.user.User;
import core.general.user.UserMembershipType;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.general.user.userTeam.UserTeamMember;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import ui.services.MenuService;
import ui.services.breadcrumbs.items.BreadcrumbsBuilder;
import utils.StringUtilities;

import java.util.Date;

public class EntityLinkUtilsServiceImpl implements EntityLinkUtilsService {

	@Autowired
	private UrlUtilsService urlUtilsService;
	
	@Autowired
	private SystemVarsService systemVarsService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private TranslatorService translatorService;
	
	@Override
	public String getPortalPageLink( final Language language ) {
		return getPortalPageLink( translatorService.translate( BreadcrumbsBuilder.BREADCRUMBS_PORTAL_PAGE, language ), language );
	}

	@Override
	public String getProjectNameLink( final Language language ) {
		return getPortalPageLink( systemVarsService.getProjectName(), language );
	}

	@Override
	public String getUserCardLink( final User user, final Language language ) {
		return getUserCardLink( user, user.getName(), language );
	}

	@Override
	public String getUserCardLink( final User user, final String name, final Language language ) {
		return String.format( "<a class=\"member-link\" href=\"%s\" title=\"%s\">%s</a>"
			, urlUtilsService.getUserCardLink( user.getId() )
			, translatorService.translate( "$1: card", language, StringUtilities.escapeHtml( user.getName() ) )
			, StringUtilities.escapeHtml( name )
		);
	}

	@Override
	public String getPhotosByUserLink( final User user, final Language language ) {
		return String.format( "<a href=\"%s\" title=\"%s\">%s</a>"
			, urlUtilsService.getPhotosByUserLink( user.getId() )
			, translatorService.translate( "$1: all photos", language, StringUtilities.escapeHtml( user.getName() ) )
			, translatorService.translate( ALL_USER_S_PHOTOS, language )
		);
	}

	@Override
	public String getPhotosByGenreLink( final Genre genre, final Language language ) {
		final String genreTranslated = translatorService.translateGenre( genre, language );
		return String.format( "<a href=\"%s\" title=\"%s\">%s</a>"
			, urlUtilsService.getPhotosByGenreLink( genre.getId() )
			, translatorService.translate( "Breadcrumbs: All photos in category '$1'", language, genreTranslated )
			, genreTranslated
		);
	}

	@Override
	public String getPhotosByUserByGenreLink( final User user, final Genre genre, final Language language ) {
		final String userName = StringUtilities.escapeHtml( user.getName() );
		final String genreName = translatorService.translateGenre( genre, language );

		return String.format( "<a class=\"photos-by-user-by-genre-link\" href=\"%s\" title=\"%s\">%s</a>"
			, urlUtilsService.getPhotosByUserByGenreLink( user.getId(), genre.getId() )
			, translatorService.translate( "$1: all photos in category '$2'", language, userName, genreName )
			, genreName
		);
	}

	@Override
	public String getPhotosByMembershipLink( final UserMembershipType membershipType, final Language language ) {
		return String.format( "<a href=\"%s\">%s</a>"
			, urlUtilsService.getPhotosByMembership( membershipType, UrlUtilsServiceImpl.PHOTOS_URL )
			, getMembershipPhotosLinkText( membershipType, language )
		);
	}

	@Override
	public String getMembershipPhotosLinkText( final UserMembershipType membershipType, final Language language ) {
		return translatorService.translate( String.format( MenuService.MAIN_MENU_MEMBERSHIP_TYPE_NERD, UrlUtilsServiceImpl.PHOTOS_URL, membershipType.getName() ), language );
	}

	@Override
	public String getPhotosByPeriod( final Date dateFrom, final Date dateTo ) {
		final String fDateFrom = dateUtilsService.formatDate( dateFrom );
		final String fDateTo = dateUtilsService.formatDate( dateTo );
		return String.format( "<a href=\"%s\">%s - %s</a>", urlUtilsService.getPhotosUploadedInPeriodUrl( dateFrom, dateTo ), fDateFrom, fDateTo );
	}

	@Override
	public String getPhotoCardLink( final Photo photo, final Language language ) {
		final String photoNameEscaped = StringUtilities.escapeHtml( photo.getName() );

		return String.format( "<a class=\"photo-link\" href=\"%s\" title=\"%s\">%s</a>"
			, urlUtilsService.getPhotoCardLink( photo.getId() )
			, translatorService.translate( "$1: photo card", language, photoNameEscaped )
			, photoNameEscaped
		);
	}

	@Override
	public String getUsersRootLink( final Language language ) {
		final String link = String.format( "%s", urlUtilsService.getAllUsersLink() );
		return String.format( "<a href=\"%s\" title=\"%s\">%s</a>"
			, link
			, translatorService.translate( "Breadcrumbs: Members list", language )
			, translatorService.translate( MenuService.MAIN_MENU_MEMBERS, language )
		);
	}

	@Override
	public String getUserTeamMemberListLink( final int userId, final Language language ) {
		final String link = String.format( "%s", urlUtilsService.getUserTeamMembersLink( userId ) );
		return String.format( "<a href=\"%s\" title=\"%s\">%s</a>"
			, link
			, translatorService.translate( BREADCRUMBS_USER_TEAM, language )
			, translatorService.translate( BREADCRUMBS_USER_TEAM, language )
		);
	}

	@Override
	public String getUserTeamMemberCardLink( final UserTeamMember userTeamMember, final Language language ) {
		final String link = String.format( "%s", urlUtilsService.getUserTeamMemberCardLink( userTeamMember.getUser().getId(), userTeamMember.getId() ) );
		final String name = StringUtilities.escapeHtml( userTeamMember.getTeamMemberName() );

		return String.format( "<a href=\"%s\" title=\"%s: %s\">%s</a>", link, translatorService.translate( "Team member", language ), userTeamMember.getTeamMemberNameWithType(), name );
	}

	@Override
	public String getUserPhotoAlbumListLink( final int userId, final Language language ) {
		final String link = String.format( "%s", urlUtilsService.getUserPhotoAlbumListLink( userId ) );
		return String.format( "<a href=\"%s\" title=\"%s\">%s</a>"
			, link
			, translatorService.translate( "Albums", language )
			, translatorService.translate( USER_PHOTO_ALBUM_LIST, language )
		);
	}

	@Override
	public String getPhotosVotedByUserLinkUser( final User user, final Language language ) {
		final String link = String.format( "%s", urlUtilsService.getPhotosVotedByUserLink( user.getId() ) );
		return String.format( "<a href=\"%s\" title=\"%s\">%s</a>"
			, link
			, translatorService.translate( "Breadcrumbs: The photos appraised by user $1", language, user.getNameEscaped() )
			, translatorService.translate( BREADCRUMBS_APPRAISED_PHOTOS
			, language )
		);
	}

	@Override
	public String getUserPhotoAlbumPhotosLink( final UserPhotoAlbum photoAlbum, final Language language ) {
		final String link = String.format( "%s", urlUtilsService.getUserPhotoAlbumPhotosLink( photoAlbum.getUser().getId(), photoAlbum.getId() ) );
		final String name = StringUtilities.escapeHtml( photoAlbum.getName() );

		return String.format( "<a href=\"%1$s\" title\"%3$s: %2$s\">%2$s</a>", link, name, translatorService.translate( "Photo album", language ) );
	}

	@Override
	public String getPhotosRootLink( final Language language ) {
		final String link = String.format( "%s", urlUtilsService.getAllPhotosLink() );
		final String translate = translatorService.translate( BreadcrumbsBuilder.BREADCRUMBS_PHOTO_GALLERY_ROOT, language );
		return String.format( "<a href=\"%s\" title=\"%s\">%s</a>"
			, link
			, translate
			, translate
		);
	}

	@Override
	public String getPhotoCommentsToUserLink( final User user, final Language language ) {
		final String link = String.format( "%s", urlUtilsService.getReceivedComments( user.getId() ) );
		return String.format( "<a href=\"%s\" title=\"%s\">%s</a>"
			, link
			, translatorService.translate( "Comments of $1", language, StringUtilities.escapeHtml( user.getName() ) )
			, translatorService.translate( "Comments", language )
		);
	}

	@Override
	public String getAdminGenresRootLink( final Language language ) {
		final String link = String.format( "%s", urlUtilsService.getAdminGenreListLink() );
		return String.format( "<a href=\"%s\" title=\"%s\">%s</a>"
			, link
			, translatorService.translate( "Breadcrumbs: Photo category list", language )
			, translatorService.translate( BREADCRUMBS_PHOTO_CATEGORIES, language )
		);
	}

	@Override
	public String getAdminConfigurationTabsLink( final SystemConfiguration systemConfiguration ) {
		final String link = String.format( "%s", urlUtilsService.getAdminConfigurationTabsLink( systemConfiguration.getId() ) );
		return String.format( "<a href=\"%s\">%s</a>", link, systemConfiguration.getName() );
	}

	@Override
	public String getAdminSystemConfigurationListLink( final Language language ) {
		final String link = String.format( "%s", urlUtilsService.getAdminSystemConfigurationListLink() );
		return String.format( "<a href=\"%s\">%s</a>", link, translatorService.translate( "System configuration", language ) );
	}

	@Override
	public String getAdminConfigurationLink( final int systemConfigurationId, final ConfigurationTab configTab, final Language language ) {
	final String link = String.format( "%s", urlUtilsService.getAdminConfigurationTabLink( systemConfigurationId, configTab.getKey() ) );
		return String.format( "<a href=\"%s\">%s</a>", link, translatorService.translate( configTab.getName(), language ) );
	}

	@Override
	public String getAdminJobsRootLink( final Language language ) {
		final String link = String.format( "%s", urlUtilsService.getAdminJobsLink() );
		return String.format( "<a href=\"%s\">%s</a>", link, translatorService.translate( "Jobs", language ) );
	}

	@Override
	public String getAdminSchedulerTaskListLink( final Language language ) {
		final String link = String.format( "%s", urlUtilsService.getAdminSchedulerTaskListLink() );
		return String.format( "<a href=\"%s\">%s</a>", link, translatorService.translate( "Scheduler tasks", language ) );
	}

	@Override
	public String getAdminVotingCategoriesRootLink( final Language language ) {
		final String link = String.format( "%s", urlUtilsService.getAdminVotingCategoriesLink() );
		return String.format( "<a href=\"%s\">%s</a>", link, translatorService.translate( "Voting category", language ) );
	}

	@Override
	public String getAdminUpgradeLink( final Language language ) {
		final String link = String.format( "%s", urlUtilsService.getAdminUpgradeLink() );
		return String.format( "<a href=\"%s\">%s</a>", link, translatorService.translate( "Upgrade tasks", language ) );
	}

	@Override
	public String getAdminSavedJobLink( final SavedJobType jobType, final SavedJob savedJob, final Language language ) {
		final String link = String.format( "%s", urlUtilsService.getAdminSavedJobEditLink( jobType, savedJob.getId() ) );
		final String result = String.format( "<a href=\"%s\" title=\"%s\">%s</a>", link, translatorService.translate( "$1: '$2' $3", language
			, translatorService.translate( savedJob.getJobType().getName(), language ), savedJob.getName(), ( !savedJob.isActive() ? "(inactive)" : StringUtils.EMPTY ) ), savedJob.getName() );

		if ( savedJob.isActive() ) {
			return result;
		}

		return String.format( "<span style=\"text-decoration: line-through\">%s</span>", result );
	}

	@Override
	public String getActivityStreamRootLink( final Language language ) {
		final String link = String.format( "%s/activityStream/", urlUtilsService.getBaseURLWithPrefix() );
		return String.format( "<a href=\"%s\">%s</a>", link, translatorService.translate( "Activity stream", language ) );
	}

	public void setUrlUtilsService( final UrlUtilsService urlUtilsService ) {
		this.urlUtilsService = urlUtilsService;
	}

	public void setSystemVarsService( final SystemVarsService systemVarsService ) {
		this.systemVarsService = systemVarsService;
	}

	public void setDateUtilsService( final DateUtilsService dateUtilsService ) {
		this.dateUtilsService = dateUtilsService;
	}

	public void setTranslatorService( final TranslatorService translatorService ) {
		this.translatorService = translatorService;
	}

	private String getPortalPageLink( final String projectName, final Language language ) {
		return String.format( "<a href=\"%s\" title=\"%s\">%s</a>"
			, urlUtilsService.getPortalPageURL()
			, translatorService.translate( "Page title: $1: Portal page", language, systemVarsService.getProjectName() )
			, projectName
		);
	}
}
