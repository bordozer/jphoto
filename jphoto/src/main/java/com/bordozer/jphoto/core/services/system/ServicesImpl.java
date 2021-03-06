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
import com.bordozer.jphoto.core.services.utils.sql.PhotoQueryService;
import com.bordozer.jphoto.ui.services.UtilsService;
import com.bordozer.jphoto.ui.services.menu.entry.EntryMenuService;
import com.bordozer.jphoto.ui.services.security.UsersSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("services")
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
    private PhotoQueryService photoQueryService;
    @Autowired
    private UrlUtilsService urlUtilsService;
    @Autowired
    private ActivityStreamService activityStreamService;
    @Autowired
    private MailService mailService;
    @Autowired
    private EntryMenuService entryMenuService;
    @Autowired
    private TranslatorService translatorService;
    @Autowired
    private PhotoUploadService photoUploadService;
    @Autowired
    private RemotePhotoCategoryService remotePhotoCategoryService;
    @Autowired
    private GroupOperationService groupOperationService;
    @Autowired
    private RestrictionService restrictionService;
    @Autowired
    private UtilsService utilsService;
    @Autowired
    private ArchivingService archivingService;

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

    @Override
    public TranslatorService getTranslatorService() {
        return translatorService;
    }

    @Override
    public PhotoUploadService getPhotoUploadService() {
        return photoUploadService;
    }

    @Override
    public RemotePhotoCategoryService getRemotePhotoCategoryService() {
        return remotePhotoCategoryService;
    }

    @Override
    public GroupOperationService getGroupOperationService() {
        return groupOperationService;
    }

    @Override
    public RestrictionService getRestrictionService() {
        return restrictionService;
    }

    @Override
    public UtilsService getUtilsService() {
        return utilsService;
    }

    @Override
    public ArchivingService getArchivingService() {
        return archivingService;
    }

	/*@Override
	public ScheduledTasksExecutionService getScheduledTasksExecutionService() {
		return scheduledTasksExecutionService;
	}*/

    public void setUserService(final UserService userService) {
        this.userService = userService;
    }

    public void setPhotoService(final PhotoService photoService) {
        this.photoService = photoService;
    }

    public void setGenreService(final GenreService genreService) {
        this.genreService = genreService;
    }

    public void setConfigurationService(final ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    public void setPreviewGenerationService(final PreviewGenerationService previewGenerationService) {
        this.previewGenerationService = previewGenerationService;
    }

    public void setUserRankService(final UserRankService userRankService) {
        this.userRankService = userRankService;
    }

    public void setCacheService(final CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public void setVotingCategoryService(final VotingCategoryService votingCategoryService) {
        this.votingCategoryService = votingCategoryService;
    }

    public void setPhotoVotingService(final PhotoVotingService photoVotingService) {
        this.photoVotingService = photoVotingService;
    }

    public void setPhotoCommentService(final PhotoCommentService photoCommentService) {
        this.photoCommentService = photoCommentService;
    }

    public void setFavoritesService(final FavoritesService favoritesService) {
        this.favoritesService = favoritesService;
    }

    public void setPhotoRatingService(final PhotoRatingService photoRatingService) {
        this.photoRatingService = photoRatingService;
    }

    public void setUserTeamService(final UserTeamService userTeamService) {
        this.userTeamService = userTeamService;
    }

    public void setUserPhotoAlbumService(final UserPhotoAlbumService userPhotoAlbumService) {
        this.userPhotoAlbumService = userPhotoAlbumService;
    }

    public void setFakeUserService(final FakeUserService fakeUserService) {
        this.fakeUserService = fakeUserService;
    }

    public void setRandomUtilsService(final RandomUtilsService randomUtilsService) {
        this.randomUtilsService = randomUtilsService;
    }

    public void setSqlUtilsService(final SqlUtilsService sqlUtilsService) {
        this.sqlUtilsService = sqlUtilsService;
    }

    public void setPrivateMessageService(final PrivateMessageService privateMessageService) {
        this.privateMessageService = privateMessageService;
    }

    public void setSavedJobService(final SavedJobService savedJobService) {
        this.savedJobService = savedJobService;
    }

    public void setJobExecutionHistoryService(final JobExecutionHistoryService jobExecutionHistoryService) {
        this.jobExecutionHistoryService = jobExecutionHistoryService;
    }

    public void setAnonymousDaysService(final AnonymousDaysService anonymousDaysService) {
        this.anonymousDaysService = anonymousDaysService;
    }

    public void setUsersSecurityService(final UsersSecurityService usersSecurityService) {
        this.usersSecurityService = usersSecurityService;
    }

    public void setSecurityService(final SecurityService securityService) {
        this.securityService = securityService;
    }

    public void setJobStatusChangeStrategyService(final JobStatusChangeStrategyService jobStatusChangeStrategyService) {
        this.jobStatusChangeStrategyService = jobStatusChangeStrategyService;
    }

    public void setJobExecutionService(final JobExecutionService jobExecutionService) {
        this.jobExecutionService = jobExecutionService;
    }

    public void setJobHelperService(final JobHelperService jobHelperService) {
        this.jobHelperService = jobHelperService;
    }

    public void setImageFileUtilsService(final ImageFileUtilsService imageFileUtilsService) {
        this.imageFileUtilsService = imageFileUtilsService;
    }

    public void setDateUtilsService(final DateUtilsService dateUtilsService) {
        this.dateUtilsService = dateUtilsService;
    }

    public void setEntityLinkUtilsService(final EntityLinkUtilsService entityLinkUtilsService) {
        this.entityLinkUtilsService = entityLinkUtilsService;
    }

    public void setPredicateUtilsService(final PredicateUtilsService predicateUtilsService) {
        this.predicateUtilsService = predicateUtilsService;
    }

    public void setUserPhotoFilePathUtilsService(final UserPhotoFilePathUtilsService userPhotoFilePathUtilsService) {
        this.userPhotoFilePathUtilsService = userPhotoFilePathUtilsService;
    }

    public void setPhotoPreviewService(final PhotoPreviewService photoPreviewService) {
        this.photoPreviewService = photoPreviewService;
    }

    public void setBaseSqlUtilsService(final BaseSqlUtilsService baseSqlUtilsService) {
        this.baseSqlUtilsService = baseSqlUtilsService;
    }

    public void setPhotoQueryService(final PhotoQueryService photoQueryService) {
        this.photoQueryService = photoQueryService;
    }

    public void setUrlUtilsService(final UrlUtilsService urlUtilsService) {
        this.urlUtilsService = urlUtilsService;
    }

    public void setActivityStreamService(final ActivityStreamService activityStreamService) {
        this.activityStreamService = activityStreamService;
    }

    public void setMailService(final MailService mailService) {
        this.mailService = mailService;
    }

    public void setEntryMenuService(final EntryMenuService entryMenuService) {
        this.entryMenuService = entryMenuService;
    }

    public void setTranslatorService(final TranslatorService translatorService) {
        this.translatorService = translatorService;
    }

    public void setPhotoUploadService(final PhotoUploadService photoUploadService) {
        this.photoUploadService = photoUploadService;
    }

    public void setRemotePhotoCategoryService(final RemotePhotoCategoryService remotePhotoCategoryService) {
        this.remotePhotoCategoryService = remotePhotoCategoryService;
    }

    public void setGroupOperationService(final GroupOperationService groupOperationService) {
        this.groupOperationService = groupOperationService;
    }

    public void setRestrictionService(final RestrictionService restrictionService) {
        this.restrictionService = restrictionService;
    }

    public void setUtilsService(final UtilsService utilsService) {
        this.utilsService = utilsService;
    }

    public void setArchivingService(final ArchivingService archivingService) {
        this.archivingService = archivingService;
    }

	/*public void setScheduledTasksExecutionService( final ScheduledTasksExecutionService scheduledTasksExecutionService ) {
		this.scheduledTasksExecutionService = scheduledTasksExecutionService;
	}*/
}
