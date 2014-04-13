package core.services.system;

import admin.services.jobs.*;
import admin.services.services.SqlUtilsService;
import core.services.conversion.PhotoPreviewService;
import core.services.conversion.PreviewGenerationService;
import core.services.entry.*;
import core.services.mail.MailService;
import core.services.menu.EntryMenuService;
import core.services.photo.PhotoCommentService;
import core.services.photo.PhotoRatingService;
import core.services.photo.PhotoService;
import core.services.photo.PhotoVotingService;
import core.services.security.SecurityService;
import core.services.translator.TranslatorService;
import core.services.user.*;
import core.services.utils.*;
import core.services.utils.sql.BaseSqlUtilsService;
import core.services.utils.sql.PhotoCriteriasSqlService;
import core.services.utils.sql.PhotoSqlFilterService;
import core.services.utils.sql.UserSqlUtilsService;
import ui.services.security.UsersSecurityService;

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

	EntryMenuService getEntryMenuService();

	TranslatorService getTranslatorService();

//	ScheduledTasksExecutionService getScheduledTasksExecutionService(); // TODO: mustn't be here because it has init method that uses Services
}
