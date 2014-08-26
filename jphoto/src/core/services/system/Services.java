package core.services.system;

import admin.services.jobs.*;
import admin.services.services.SqlUtilsService;
import core.services.conversion.PreviewGenerationService;
import core.services.entry.*;
import core.services.mail.MailService;
import core.services.photo.*;
import core.services.remotePhotoSite.RemotePhotoCategoryService;
import core.services.security.RestrictionService;
import core.services.security.SecurityService;
import core.services.translator.TranslatorService;
import core.services.user.*;
import core.services.utils.*;
import core.services.utils.sql.BaseSqlUtilsService;
import ui.services.UtilsService;
import ui.services.menu.entry.EntryMenuService;
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

	//	ScheduledTasksExecutionService getScheduledTasksExecutionService(); // TODO: mustn't be here because it has init method that uses Services
}
