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
import core.services.translator.TranslatorService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
	public String getPortalPageLink() {
		return String.format( "<a href=\"%s\" title=\"Main page\">%s</a>"
			, urlUtilsService.getPortalPageURL()
			, systemVarsService.getProjectName()
		);
	}

	@Override
	public String getUserCardLink( final User user ) {
		return getUserCardLink( user, user.getName() );
	}

	@Override
	public String getUserCardLink( final User user, final String name ) {
		return String.format( "<a class=\"member-link\" href=\"%1$s\" title=\"%2$s: card\">%2$s</a>"
			, urlUtilsService.getUserCardLink( user.getId() )
			, StringUtilities.escapeHtml( name )
		);
	}

	@Override
	public String getPhotosByUserLink( final User user ) {
		return String.format( "<a href=\"%s\" title=\"%s: all photos\">%s</a>"
			, urlUtilsService.getPhotosByUserLink( user.getId() )
			, StringUtilities.escapeHtml( user.getName() )
			, translatorService.translate( "All user's photos" )
		);
	}

	@Override
	public String getPhotosByGenreLink( final Genre genre ) {
		return String.format( "<a href=\"%1$s\" title=\"All photos in genre '%2$s'\">%2$s</a>"
			, urlUtilsService.getPhotosByGenreLink( genre.getId() )
			, genre.getName()
		);
	}

	@Override
	public String getPhotosByUserByGenreLink( final User user, final Genre genre ) {
		return String.format( "<a class=\"photos-by-user-by-genre-link\" href=\"%1$s\" title=\"%2$s: all photos in category '%3$s'\">%3$s</a>"
			, urlUtilsService.getPhotosByUserByGenreLink( user.getId(), genre.getId() )
			, StringUtilities.escapeHtml( user.getName() )
			, genre.getName()
		);
	}

	@Override
	public String getPhotosByMembershipLink( final UserMembershipType membershipType ) {
		return String.format( "<a href=\"%s\">%s</a>", urlUtilsService.getPhotosByMembership( membershipType, UrlUtilsServiceImpl.PHOTOS_URL )
			, StringUtilities.toUpperCaseFirst( translatorService.translate( membershipType.getNamePlural() ) ) );
	}

	@Override
	public String getPhotosByPeriod( final Date dateFrom, final Date dateTo ) {
		final String fDateFrom = dateUtilsService.formatDate( dateFrom );
		final String fDateTo = dateUtilsService.formatDate( dateTo );
		return String.format( "<a href=\"%s\">%s - %s</a>", urlUtilsService.getPhotosUploadedInPeriodUrl( dateFrom, dateTo ), fDateFrom, fDateTo );
	}

	@Override
	public String getPhotoCardLink( final Photo photo ) {
		final String photoNameTranslated = StringUtilities.escapeHtml( photo.getName() );

		return String.format( "<a class=\"photo-link\" href=\"%1$s\" title=\"%2$s\">%3$s</a>"
			, urlUtilsService.getPhotoCardLink( photo.getId() )
			, translatorService.translate( "$1: photo card", photoNameTranslated )
			, photoNameTranslated
		);
	}

	@Override
	public String getUsersRootLink() {
		final String link = String.format( "%s", urlUtilsService.getAllUsersLink() );
		return String.format( "<a href=\"%s\">%s</a>", link, translatorService.translate( "Members" ) );
	}

	@Override
	public String getUserTeamMemberListLink( final int userId ) {
		final String link = String.format( "%s", urlUtilsService.getUserTeamMembersLink( userId ) );
		return String.format( "<a href=\"%s\">%s</a>", link, translatorService.translate( "Team" ) );
	}

	@Override
	public String getUserTeamMemberCardLink( final UserTeamMember userTeamMember ) {
		final String link = String.format( "%s", urlUtilsService.getUserTeamMemberCardLink( userTeamMember.getUser().getId(), userTeamMember.getId() ) );
		final String name = StringUtilities.escapeHtml( userTeamMember.getTeamMemberName() );

		return String.format( "<a href=\"%s\" title=\"%s: %s\">%s</a>", link, translatorService.translate( "Team member" ), userTeamMember.getTeamMemberNameWithType(), name );
	}

	@Override
	public String getUserPhotoAlbumListLink( final int userId ) {
		final String link = String.format( "%s", urlUtilsService.getUserPhotoAlbumListLink( userId ) );
		return String.format( "<a href=\"%s\">%s</a>", link, translatorService.translate( "Albums" ) );
	}

	@Override
	public String getPhotosVotedByUserLinkUser( final int userId ) {
		final String link = String.format( "%s", urlUtilsService.getPhotosVotedByUserLink( userId ) );
		return String.format( "<a href=\"%s\">%s</a>", link, translatorService.translate( "Appraised photos" ) );
	}

	@Override
	public String getUserPhotoAlbumPhotosLink( final UserPhotoAlbum photoAlbum ) {
		final String link = String.format( "%s", urlUtilsService.getUserPhotoAlbumPhotosLink( photoAlbum.getUser().getId(), photoAlbum.getId() ) );
		final String name = StringUtilities.escapeHtml( photoAlbum.getName() );

		return String.format( "<a href=\"%1$s\" title\"%3$s: %2$s\">%2$s</a>", link, name, translatorService.translate( "Photo album" ) );
	}

	@Override
	public String getPhotosRootLink() {
		final String link = String.format( "%s", urlUtilsService.getAllPhotosLink() );
		return String.format( "<a href=\"%s\" title=\"All photos\">%s</a>", link, translatorService.translate( "Photo gallery" ) );
	}

	@Override
	public String getPhotoCommentsToUserLink( final User user ) {
		final String link = String.format( "%s", urlUtilsService.getCommentsToUserList( user.getId() ) );
		return String.format( "<a href=\"%s\" title=\"%s\">%s</a>"
			, link
			, translatorService.translate( "Comments of $1", StringUtilities.escapeHtml( user.getName() ) )
			, translatorService.translate( "Comments" )
		);
	}

	@Override
	public String getAdminGenresRootLink() {
		final String link = String.format( "%s", urlUtilsService.getAdminGenreListLink() );
		return String.format( "<a href=\"%s\">%s</a>", link, translatorService.translate( "Genres" ) );
	}

	@Override
	public String getAdminConfigurationTabsLink( final SystemConfiguration systemConfiguration ) {
		final String link = String.format( "%s", urlUtilsService.getAdminConfigurationTabsLink( systemConfiguration.getId() ) );
		return String.format( "<a href=\"%s\">%s</a>", link, systemConfiguration.getName() );
	}

	@Override
	public String getAdminSystemConfigurationListLink() {
		final String link = String.format( "%s", urlUtilsService.getAdminSystemConfigurationListLink() );
		return String.format( "<a href=\"%s\">%s</a>", link, translatorService.translate( "System configuration" ) );
	}

	@Override
	public String getAdminConfigurationLink( final int systemConfigurationId, final ConfigurationTab configTab ) {
	final String link = String.format( "%s", urlUtilsService.getAdminConfigurationTabLink( systemConfigurationId, configTab.getKey() ) );
		return String.format( "<a href=\"%s\">%s</a>", link, translatorService.translate( configTab.getName() ) );
	}

	@Override
	public String getAdminJobsRootLink() {
		final String link = String.format( "%s", urlUtilsService.getAdminJobsLink() );
		return String.format( "<a href=\"%s\">%s</a>", link, translatorService.translate( "Jobs" ) );
	}

	@Override
	public String getAdminSchedulerTaskListLink() {
		final String link = String.format( "%s", urlUtilsService.getAdminSchedulerTaskListLink() );
		return String.format( "<a href=\"%s\">%s</a>", link, translatorService.translate( "Scheduler tasks" ) );
	}

	@Override
	public String getAdminVotingCategoriesRootLink() {
		final String link = String.format( "%s", urlUtilsService.getAdminVotingCategoriesLink() );
		return String.format( "<a href=\"%s\">%s</a>", link, translatorService.translate( "Voting category" ) );
	}

	@Override
	public String getAdminUpgradeLink() {
		final String link = String.format( "%s", urlUtilsService.getAdminUpgradeLink() );
		return String.format( "<a href=\"%s\">%s</a>", link, translatorService.translate( "Upgrade tasks" ) );
	}

	@Override
	public String getAdminSavedJobLink( final SavedJobType jobType, final SavedJob savedJob ) {
		final String link = String.format( "%s", urlUtilsService.getAdminSavedJobEditLink( jobType, savedJob.getId() ) );
		final String result = String.format( "<a href=\"%s\" title=\"%s\">%s</a>", link, translatorService.translate( "$1: '$2' $3"
			, translatorService.translate( savedJob.getJobType().getName() ), savedJob.getName(), ( !savedJob.isActive() ? "(inactive)" : StringUtils.EMPTY ) ), savedJob.getName() );

		if ( savedJob.isActive() ) {
			return result;
		}

		return String.format( "<span style=\"text-decoration: line-through\">%s</span>", result );
	}

	@Override
	public String getActivityStreamRootLink() {
		final String link = String.format( "%s/activityStream/", urlUtilsService.getBaseURLWithPrefix() );
		return String.format( "<a href=\"%s\">%s</a>", link, translatorService.translate( "Activity stream" ) );
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
}
