package com.bordozer.jphoto.core.services.system;

import com.bordozer.jphoto.admin.services.jobs.JobExecutionHistoryService;
import com.bordozer.jphoto.admin.services.jobs.JobExecutionService;
import com.bordozer.jphoto.admin.services.jobs.JobHelperService;
import com.bordozer.jphoto.admin.services.jobs.JobStatusChangeStrategyService;
import com.bordozer.jphoto.admin.services.jobs.SavedJobService;
import com.bordozer.jphoto.admin.services.services.SqlUtilsService;
import com.bordozer.jphoto.core.services.archiving.ArchivingService;
import com.bordozer.jphoto.core.services.conversion.PreviewGenerationService;
import com.bordozer.jphoto.core.services.entry.ActivityStreamService;
import com.bordozer.jphoto.core.services.entry.AnonymousDaysService;
import com.bordozer.jphoto.core.services.entry.FavoritesService;
import com.bordozer.jphoto.core.services.entry.GenreService;
import com.bordozer.jphoto.core.services.entry.GroupOperationService;
import com.bordozer.jphoto.core.services.entry.PrivateMessageService;
import com.bordozer.jphoto.core.services.entry.VotingCategoryService;
import com.bordozer.jphoto.core.services.mail.MailService;
import com.bordozer.jphoto.core.services.photo.PhotoCommentService;
import com.bordozer.jphoto.core.services.photo.PhotoPreviewService;
import com.bordozer.jphoto.core.services.photo.PhotoRatingService;
import com.bordozer.jphoto.core.services.photo.PhotoService;
import com.bordozer.jphoto.core.services.photo.PhotoUploadService;
import com.bordozer.jphoto.core.services.photo.PhotoVotingService;
import com.bordozer.jphoto.core.services.remotePhotoSite.RemotePhotoCategoryService;
import com.bordozer.jphoto.core.services.security.RestrictionService;
import com.bordozer.jphoto.core.services.security.SecurityService;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.core.services.user.FakeUserService;
import com.bordozer.jphoto.core.services.user.UserPhotoAlbumService;
import com.bordozer.jphoto.core.services.user.UserRankService;
import com.bordozer.jphoto.core.services.user.UserService;
import com.bordozer.jphoto.core.services.user.UserTeamService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.core.services.utils.EntityLinkUtilsService;
import com.bordozer.jphoto.core.services.utils.ImageFileUtilsService;
import com.bordozer.jphoto.core.services.utils.PredicateUtilsService;
import com.bordozer.jphoto.core.services.utils.RandomUtilsService;
import com.bordozer.jphoto.core.services.utils.UrlUtilsService;
import com.bordozer.jphoto.core.services.utils.UserPhotoFilePathUtilsService;
import com.bordozer.jphoto.core.services.utils.sql.BaseSqlUtilsService;
import com.bordozer.jphoto.ui.services.UtilsService;
import com.bordozer.jphoto.ui.services.menu.entry.EntryMenuService;
import com.bordozer.jphoto.ui.services.security.UsersSecurityService;

public interface Services {

    UserService getUserService();

    PhotoService getPhotoService();

    GenreService getGenreService();

    ConfigurationService getConfigurationService();

    PreviewGenerationService getPreviewGenerationService();

    UserRankService getUserRankService();

    CacheService getCacheService();

    VotingCategoryService getVotingCategoryService();

    PhotoVotingService getPhotoVotingService();

    PhotoCommentService getPhotoCommentService();

    FavoritesService getFavoritesService();

    PhotoRatingService getPhotoRatingService();

    UserTeamService getUserTeamService();

    UserPhotoAlbumService getUserPhotoAlbumService();

    FakeUserService getFakeUserService();

    RandomUtilsService getRandomUtilsService();

    SqlUtilsService getSqlUtilsService();

    PrivateMessageService getPrivateMessageService();

    SavedJobService getSavedJobService();

    JobExecutionHistoryService getJobExecutionHistoryService();

    AnonymousDaysService getAnonymousDaysService();

    UsersSecurityService getUsersSecurityService();

    SecurityService getSecurityService();

    JobStatusChangeStrategyService getJobStatusChangeStrategyService();

    JobExecutionService getJobExecutionService();

    JobHelperService getJobHelperService();

    ImageFileUtilsService getImageFileUtilsService();

    DateUtilsService getDateUtilsService();

    EntityLinkUtilsService getEntityLinkUtilsService();

    PredicateUtilsService getPredicateUtilsService();

    UserPhotoFilePathUtilsService getUserPhotoFilePathUtilsService();

    PhotoPreviewService getPhotoPreviewService();

    BaseSqlUtilsService getBaseSqlUtilsService();

    UrlUtilsService getUrlUtilsService();

    ActivityStreamService getActivityStreamService();

    MailService getMailService();

    EntryMenuService getEntryMenuService();

    TranslatorService getTranslatorService();

    PhotoUploadService getPhotoUploadService();

    RemotePhotoCategoryService getRemotePhotoCategoryService();

    GroupOperationService getGroupOperationService();

    RestrictionService getRestrictionService();

    UtilsService getUtilsService();

    ArchivingService getArchivingService();

    //	ScheduledTasksExecutionService getScheduledTasksExecutionService(); // TODO: mustn't be here because it has init method that uses Services
}
