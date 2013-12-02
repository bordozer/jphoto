package core.services.security;

import admin.services.jobs.JobExecutionHistoryService;
import admin.services.jobs.JobExecutionService;
import admin.services.jobs.JobHelperService;
import admin.services.jobs.JobStatusChangeStrategyService;
import admin.services.services.SqlUtilsService;
import core.services.conversion.PhotoPreviewService;
import core.services.conversion.PreviewGenerationService;
import core.services.entry.*;
import admin.services.jobs.SavedJobService;
import core.services.mail.MailService;
import core.services.photo.PhotoCommentService;
import core.services.photo.PhotoRatingService;
import core.services.photo.PhotoService;
import core.services.photo.PhotoVotingService;
import core.services.system.CacheService;
import core.services.system.ConfigurationService;
import core.services.user.*;
import core.services.utils.*;
import core.services.utils.sql.BaseSqlUtilsService;
import core.services.utils.sql.PhotoCriteriasSqlService;
import core.services.utils.sql.PhotoSqlFilterService;
import core.services.utils.sql.UserSqlUtilsService;

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

	SystemVarsService getSystemVarsService();

	ImageFileUtilsService getImageFileUtilsService();

	DateUtilsService getDateUtilsService();

	EntityLinkUtilsService getEntityLinkUtilsService();

	PredicateUtilsService getPredicateUtilsService();

	UserPhotoFilePathUtilsService getUserPhotoFilePathUtilsService();

	PhotoPreviewService getPhotoPreviewService();

	BaseSqlUtilsService getBaseSqlUtilsService();

	PhotoCriteriasSqlService getPhotoCriteriasSqlService();

	PhotoSqlFilterService getPhotoSqlFilterService();

	UserSqlUtilsService getUserSqlUtilsService();

	UrlUtilsService getUrlUtilsService();

	ActivityStreamService getActivityStreamService();

	MailService getMailService();
}
