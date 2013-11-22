package core.services.utils;

import admin.jobs.general.SavedJob;
import admin.jobs.enums.SavedJobType;
import core.general.activity.ActivityType;
import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.user.User;
import core.general.user.UserMembershipType;
import core.general.configuration.ConfigurationTab;
import core.general.configuration.SystemConfiguration;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.general.user.userTeam.UserTeamMember;

import java.util.Date;

public interface EntityLinkUtilsService {

	String BEAN_NAME = "entityLinkUtilsService";

	String getPortalPageLink();

	String getUserCardLink( User user );

	String getUserCardLink( User user, String name );

	String getPhotosByUserLink( User user );

	String getPhotosByGenreLink( Genre genre );

	String getPhotosByUserByGenreLink( User user, Genre genre );

	String getPhotosByMembershipLink( UserMembershipType membershipType );

	String getPhotosByPeriod( Date dateFrom, Date dateTo );

	String getPhotoCardLink( Photo photo );

	String getUsersRootLink();

	String getUserTeamMemberListLink( int userId );

	String getUserTeamMemberCardLink( UserTeamMember userTeamMember );

	String getUserPhotoAlbumListLink( int userId );

	String getPhotosVotedByUserLinkUser( int userId );

	String getUserPhotoAlbumPhotosLink( UserPhotoAlbum photoAlbum );

	String getPhotosRootLink();

	String getPhotoCommentsToUserLink( User user );

	String getAdminGenresRootLink();

	String getAdminConfigurationTabsLink( SystemConfiguration systemConfiguration );

	String getAdminSystemConfigurationListLink();

	String getAdminConfigurationLink( int systemConfigurationId, ConfigurationTab configTab );

	String getAdminJobsRootLink();

	String getAdminSchedulerTaskListLink();

	String getAdminVotingCategoriesRootLink();

	String getAdminUpgradeLink();

	String getAdminSavedJobLink( SavedJobType jobType, SavedJob savedJob );

	String getActivityStreamRootLink();
}
