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
import org.springframework.beans.factory.annotation.Autowired;

public class ServicesImpl implements Services {

	@Autowired
	protected UserService userService;

	@Autowired
	protected PhotoService photoService;

	@Autowired
	protected GenreService genreService;

	@Autowired
	protected ConfigurationService configurationService;

	@Autowired
	protected PreviewGenerationService previewGenerationService;

	@Autowired
	protected UserRankService userRankService;

	@Autowired
	protected CacheService cacheService;

	@Autowired
	protected VotingCategoryService votingCategoryService;

	@Autowired
	protected PhotoVotingService photoVotingService;

	@Autowired
	protected PhotoCommentService photoCommentService;

	@Autowired
	protected FavoritesService favoritesService;

	@Autowired
	protected PhotoRatingService photoRatingService;

	@Autowired
	protected UserTeamService userTeamService;

	@Autowired
	private UserPhotoAlbumService userPhotoAlbumService;

	@Autowired
	protected FakeUserService fakeUserService;

	@Autowired
	private RandomUtilsService randomUtilsService;

	@Autowired
	private SqlUtilsService sqlUtilsService;

	@Autowired
	private PrivateMessageService privateMessageService;

	@Autowired
	private SavedJobService savedJobService;

	@Autowired
	private JobExecutionHistoryService jobExecutionHistoryService;

	@Autowired
	private AnonymousDaysService anonymousDaysService;

	@Autowired
	private UsersSecurityService usersSecurityService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private JobStatusChangeStrategyService jobStatusChangeStrategyService;

	@Autowired
	private JobExecutionService jobExecutionService;

	@Autowired
	private JobHelperService jobHelperService;

	@Autowired
	private SystemVarsService systemVarsService;

	@Autowired
	private ImageFileUtilsService imageFileUtilsService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private EntityLinkUtilsService entityLinkUtilsService;

	@Autowired
	private PredicateUtilsService predicateUtilsService;

	@Autowired
	private UserPhotoFilePathUtilsService userPhotoFilePathUtilsService;

	@Autowired
	private PhotoPreviewService photoPreviewService;

	@Autowired
	private BaseSqlUtilsService baseSqlUtilsService;

	@Autowired
	private PhotoCriteriasSqlService photoCriteriasSqlService;

	@Autowired
	private PhotoSqlFilterService photoSqlFilterService;

	@Autowired
	private UserSqlUtilsService userSqlUtilsService;

	@Autowired
	private UrlUtilsService urlUtilsService;

	@Autowired
	private ActivityStreamService activityStreamService;

	@Autowired
	private MailService mailService;

	@Autowired
	private EntryMenuService entryMenuService;

	@Override
	public UserService getUserService() {
		return userService;
	}

	@Override
	public PhotoService getPhotoService() {
		return photoService;
	}

	@Override
	public GenreService getGenreService() {
		return genreService;
	}

	@Override
	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	@Override
	public PreviewGenerationService getPreviewGenerationService() {
		return previewGenerationService;
	}

	@Override
	public UserRankService getUserRankService() {
		return userRankService;
	}

	@Override
	public CacheService getCacheService() {
		return cacheService;
	}

	@Override
	public VotingCategoryService getVotingCategoryService() {
		return votingCategoryService;
	}

	@Override
	public PhotoVotingService getPhotoVotingService() {
		return photoVotingService;
	}

	@Override
	public PhotoCommentService getPhotoCommentService() {
		return photoCommentService;
	}

	@Override
	public FavoritesService getFavoritesService() {
		return favoritesService;
	}

	@Override
	public PhotoRatingService getPhotoRatingService() {
		return photoRatingService;
	}

	@Override
	public UserTeamService getUserTeamService() {
		return userTeamService;
	}

	@Override
	public UserPhotoAlbumService getUserPhotoAlbumService() {
		return userPhotoAlbumService;
	}

	@Override
	public FakeUserService getFakeUserService() {
		return fakeUserService;
	}

	@Override
	public RandomUtilsService getRandomUtilsService() {
		return randomUtilsService;
	}

	@Override
	public SqlUtilsService getSqlUtilsService() {
		return sqlUtilsService;
	}

	@Override
	public PrivateMessageService getPrivateMessageService() {
		return privateMessageService;
	}

	@Override
	public SavedJobService getSavedJobService() {
		return savedJobService;
	}

	@Override
	public JobExecutionHistoryService getJobExecutionHistoryService() {
		return jobExecutionHistoryService;
	}

	@Override
	public AnonymousDaysService getAnonymousDaysService() {
		return anonymousDaysService;
	}

	@Override
	public UsersSecurityService getUsersSecurityService() {
		return usersSecurityService;
	}

	@Override
	public SecurityService getSecurityService() {
		return securityService;
	}

	@Override
	public JobStatusChangeStrategyService getJobStatusChangeStrategyService() {
		return jobStatusChangeStrategyService;
	}

	@Override
	public JobExecutionService getJobExecutionService() {
		return jobExecutionService;
	}

	@Override
	public JobHelperService getJobHelperService() {
		return jobHelperService;
	}

	@Override
	public SystemVarsService getSystemVarsService() {
		return systemVarsService;
	}

	@Override
	public ImageFileUtilsService getImageFileUtilsService() {
		return imageFileUtilsService;
	}

	@Override
	public DateUtilsService getDateUtilsService() {
		return dateUtilsService;
	}

	@Override
	public EntityLinkUtilsService getEntityLinkUtilsService() {
		return entityLinkUtilsService;
	}

	@Override
	public PredicateUtilsService getPredicateUtilsService() {
		return predicateUtilsService;
	}

	@Override
	public UserPhotoFilePathUtilsService getUserPhotoFilePathUtilsService() {
		return userPhotoFilePathUtilsService;
	}

	@Override
	public PhotoPreviewService getPhotoPreviewService() {
		return photoPreviewService;
	}

	@Override
	public BaseSqlUtilsService getBaseSqlUtilsService() {
		return baseSqlUtilsService;
	}

	@Override
	public PhotoCriteriasSqlService getPhotoCriteriasSqlService() {
		return photoCriteriasSqlService;
	}

	@Override
	public PhotoSqlFilterService getPhotoSqlFilterService() {
		return photoSqlFilterService;
	}

	@Override
	public UserSqlUtilsService getUserSqlUtilsService() {
		return userSqlUtilsService;
	}

	@Override
	public UrlUtilsService getUrlUtilsService() {
		return urlUtilsService;
	}

	@Override
	public ActivityStreamService getActivityStreamService() {
		return activityStreamService;
	}

	@Override
	public MailService getMailService() {
		return mailService;
	}

	@Override
	public EntryMenuService getEntryMenuService() {
		return entryMenuService;
	}
}
