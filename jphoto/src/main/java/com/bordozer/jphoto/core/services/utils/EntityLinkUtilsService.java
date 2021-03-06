package com.bordozer.jphoto.core.services.utils;

import com.bordozer.jphoto.admin.jobs.enums.JobListTab;
import com.bordozer.jphoto.admin.jobs.enums.SavedJobType;
import com.bordozer.jphoto.admin.jobs.general.SavedJob;
import com.bordozer.jphoto.core.enums.UserCardTab;
import com.bordozer.jphoto.core.general.configuration.ConfigurationTab;
import com.bordozer.jphoto.core.general.configuration.SystemConfiguration;
import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.UserMembershipType;
import com.bordozer.jphoto.core.general.user.userAlbums.UserPhotoAlbum;
import com.bordozer.jphoto.core.general.user.userTeam.UserTeamMember;
import com.bordozer.jphoto.core.services.translator.Language;

import java.util.Date;

public interface EntityLinkUtilsService {

    String BEAN_NAME = "entityLinkUtilsService";

    String ALL_USER_S_PHOTOS = "Breadcrumbs: All user's photos";
    String BREADCRUMBS_APPRAISED_PHOTOS = "Breadcrumbs: Appraised photos";

    String BREADCRUMBS_USER_TEAM = "Breadcrumbs: User team";
    String USER_PHOTO_ALBUM_LIST = UserCardTab.ALBUMS.getName();
    String BREADCRUMBS_PHOTO_CATEGORIES = "Breadcrumbs: Photo categories";

    String getPortalPageLink(final Language language);

    String getUserCardLink(User user, final Language language);

    String getUserCardLink(User user, String customName, final Language language);

    String getPhotosByUserLink(User user, final Language language);

    String getPhotosByGenreLink(Genre genre, final Language language);

    String getPhotosByUserByGenreLink(User user, Genre genre, final Language language);

    String getPhotosByMembershipLink(UserMembershipType membershipType, final Language language);

    String getPhotosByPeriod(Date dateFrom, Date dateTo);

    String getPhotoCardLink(final Photo photo, final Language language);

    //	String getPhotoCardLink( final Photo photo, final int cutNameTo, final Language language );

    String getUsersRootLink(final Language language);

    String getUserTeamMemberListLink(int userId, final Language language);

    String getUserTeamMemberCardLink(UserTeamMember userTeamMember, final Language language);

    String getUserPhotoAlbumListLink(int userId, final Language language);

    String getUserPhotoAlbumsWithPreviewsLink(int userId, final Language language);

    String getPhotosVotedByUserLinkUser(final User user, final Language language);

    String getUserPhotoAlbumPhotosLink(UserPhotoAlbum photoAlbum, final Language language);

    String getPhotosRootLink(final Language language);

    String getPhotoCommentsToUserLink(User user, final Language language);

    String getAdminGenresRootLink(final Language language);

    String getAdminConfigurationTabsLink(SystemConfiguration systemConfiguration);

    String getAdminSystemConfigurationListLink(final Language language);

    String getAdminConfigurationLink(int systemConfigurationId, ConfigurationTab configTab, final Language language);

    String getAdminJobsRootLink(final Language language);

    String getAdminSchedulerTaskListLink(final Language language);

    String getAdminVotingCategoriesRootLink(final Language language);

    String getAdminUpgradeLink(final Language language);

    String getAdminSavedJobLink(SavedJobType jobType, SavedJob savedJob, final Language language);

    String getAdminJobsOnTabLink(final JobListTab jobListTab, final Language language);

    String getActivityStreamRootLink(final Language language);

    String getProjectNameLink(Language language);

    String getMembershipPhotosLinkText(UserMembershipType membershipType, Language language);
}
