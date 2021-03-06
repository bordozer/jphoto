package com.bordozer.jphoto.admin.controllers.jobs.edit;

import com.bordozer.jphoto.admin.jobs.JobRuntimeEnvironment;
import com.bordozer.jphoto.admin.jobs.entries.AbstractJob;
import com.bordozer.jphoto.admin.jobs.entries.ActivityStreamCleanupJob;
import com.bordozer.jphoto.admin.jobs.entries.ArchivingJob;
import com.bordozer.jphoto.admin.jobs.entries.FavoritesJob;
import com.bordozer.jphoto.admin.jobs.entries.JobChainJob;
import com.bordozer.jphoto.admin.jobs.entries.JobExecutionHistoryCleanupJob;
import com.bordozer.jphoto.admin.jobs.entries.PhotoActionGenerationCommentsJob;
import com.bordozer.jphoto.admin.jobs.entries.PhotoActionGenerationPreviewsJob;
import com.bordozer.jphoto.admin.jobs.entries.PhotoActionGenerationVotingJob;
import com.bordozer.jphoto.admin.jobs.entries.PhotoRatingJob;
import com.bordozer.jphoto.admin.jobs.entries.PhotoStorageSynchronizationJob;
import com.bordozer.jphoto.admin.jobs.entries.PhotosImportJob;
import com.bordozer.jphoto.admin.jobs.entries.PreviewGenerationJob;
import com.bordozer.jphoto.admin.jobs.entries.RankVotingJob;
import com.bordozer.jphoto.admin.jobs.entries.ReindexJob;
import com.bordozer.jphoto.admin.jobs.entries.UserGenerationJob;
import com.bordozer.jphoto.admin.jobs.entries.UserStatusRecalculationJob;
import com.bordozer.jphoto.admin.jobs.entries.UsersGenresRanksRecalculationJob;
import com.bordozer.jphoto.admin.jobs.enums.JobExecutionStatus;
import com.bordozer.jphoto.admin.jobs.enums.SavedJobType;
import com.bordozer.jphoto.admin.jobs.general.SavedJob;
import com.bordozer.jphoto.admin.jobs.loaders.SavedJobLoaderFactory;
import com.bordozer.jphoto.admin.services.jobs.JobExecutionHistoryEntry;
import com.bordozer.jphoto.admin.services.jobs.JobExecutionHistoryService;
import com.bordozer.jphoto.admin.services.jobs.JobExecutionService;
import com.bordozer.jphoto.admin.services.jobs.JobHelperService;
import com.bordozer.jphoto.admin.services.jobs.SavedJobService;
import com.bordozer.jphoto.core.log.LogHelper;
import com.bordozer.jphoto.core.services.photo.PhotoService;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.core.services.user.UserService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.ui.services.breadcrumbs.BreadcrumbsAdminService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractJobController {

    public static final String PROGRESS_VIEW = "/admin/jobs/edit/Progress";
    public static final String FINISH_VIEW = "/admin/jobs/edit/Finish";
    public static final String JOB_HISTORY_ENTRY_NOT_FOUND_VIEW = "/admin/jobs/edit/JobHistoryEntryNotFound";
    public static final String MODEL_NAME = "command";

    @Autowired
    protected JobExecutionService jobExecutionService;

    @Autowired
    protected SavedJobService savedJobService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected JobHelperService jobHelperService;

    @Autowired
    protected JobExecutionHistoryService jobExecutionHistoryService;

    @Autowired
    private BreadcrumbsAdminService breadcrumbsAdminService;

    @Autowired
    protected DateUtilsService dateUtilsService;

    @Autowired
    private Services services;

    @Autowired
    private TranslatorService translatorService;

    protected abstract void showFormCustomAction(final AbstractAdminJobModel model);

    protected abstract void initJobFromModel(final AbstractAdminJobModel model);

    protected abstract void initModelFromSavedJob(final AbstractAdminJobModel model, final int savedJobId);

    protected abstract String getStartViewName();

    protected LogHelper log = new LogHelper();

    protected ModelAndView doShowForm(final AbstractAdminJobModel model, final SavedJobType jobType) {
        model.clear();

        final AbstractJob job = createInstance(jobType, getCurrentUserJobRuntimeEnvironment());

        model.setJob(job);
        model.setJobName(translatorService.translate(job.getJobType().getName(), getLanguage()));
        model.setActive(true);

        model.setPageTitleData(breadcrumbsAdminService.getAdminJobsDataTemplateBreadcrumbs(services.getTranslatorService().translate(job.getJobName(), getLanguage())));

        addUsersAndPhotosInfo(model);

        model.setActiveJobs(jobExecutionService.getActiveJobs());

        setTabInfo(model);

        showFormCustomAction(model);

        return getView(getStartViewName(), model);
    }

    protected ModelAndView doPostForm(final AbstractAdminJobModel model, final BindingResult result) {

        setTabInfo(model);

        model.setBindingResult(result);
        if (result.hasErrors()) {
            return getView(getStartViewName(), model);
        }

        final AbstractJob job = model.getJob();

        if (job == null) {
            return getView(getStartViewName(), model);
        }

        model.setPageTitleData(breadcrumbsAdminService.getAdminJobsBreadcrumbs(job.getJobName()));

        initJobFromModel(model);

        jobExecutionService.execute(job);

        return getRedirectToProgressView(model);
    }

    protected ModelAndView doSaveJob(final AbstractAdminJobModel model, final BindingResult result) {

        setTabInfo(model);

        model.setBindingResult(result);
        if (result.hasErrors()) {
            return getView(getStartViewName(), model);
        }

        initJobFromModel(model);

        return saveJobToDBAndReturnView(model, result);
    }

    protected ModelAndView getProgressOrFinishView(final AbstractAdminJobModel model, final int jobId) {

        setTabInfo(model);

        final JobExecutionHistoryEntry historyEntry = jobExecutionHistoryService.load(jobId);

        if (historyEntry == null) {
            model.setPageTitleData(breadcrumbsAdminService.getAdminJobsBreadcrumbs("Job history entry not found"));
            return getView(JOB_HISTORY_ENTRY_NOT_FOUND_VIEW, model).addObject("jobExecutionHistoryEntryId", jobId);
        }

        model.setJobExecutionHistoryEntry(historyEntry);

        if (historyEntry.getJobExecutionStatus().isActive()) {
            final AbstractJob activeJob = jobExecutionService.getActiveJob(jobId);
            if (activeJob == null) {
                // There in NO active job but JobExecutionHistoryEntry is in progress - just set ERROR status of this job
                final String message = translatorService.translate("There in NO active job #$1 but JobExecutionHistoryEntry is in progress.<br /><br />Something must have happened during job execution (exception, worker stopping, etc.).<br /><br />ERROR status of the job was assigned forcibly", getLanguage(), String.valueOf(jobId));
                jobExecutionHistoryService.setJobExecutionHistoryEntryStatus(historyEntry.getId(), message, JobExecutionStatus.ERROR);
                return getRedirectToJobDoneListView(model);
            }
        }

        final AbstractJob recreatedFromHistoryEntryJob = createInstance(historyEntry.getSavedJobType(), getCurrentUserJobRuntimeEnvironment());
        recreatedFromHistoryEntryJob.setJobId(jobId);

        jobExecutionService.initJobServices(recreatedFromHistoryEntryJob);

        recreatedFromHistoryEntryJob.initJobParameters(historyEntry.getParametersMap());
        historyEntry.setJobParametersDescription(recreatedFromHistoryEntryJob.getJobParametersDescription());

        final JobExecutionStatus jobExecutionStatus = historyEntry.getJobExecutionStatus();
        if (jobExecutionStatus.isNotActive()) {

            model.setPageTitleData(breadcrumbsAdminService.getAdminJobsBreadcrumbs(translatorService.translate("The job execution finished", getLanguage())));
            return getFinishView(model);
        }

        model.setPageTitleData(breadcrumbsAdminService.getAdminJobsBreadcrumbs(translatorService.translate(historyEntry.getSavedJobType().getName(), getLanguage())));
        model.setJob(recreatedFromHistoryEntryJob);

        return getView(PROGRESS_VIEW, model);
    }

    protected ModelAndView processEditing(final int savedJobId, final AbstractAdminJobModel model, final HttpServletRequest request) {
        model.setReferrer(request.getHeader("Referer"));

        setTabInfo(model);

        model.setBindingResult(null);

        final SavedJob savedJob = savedJobService.load(savedJobId);
        final AbstractJob job = savedJob.getJob();

        model.setJob(job);

        model.setSavedJobId(savedJob.getId());
        model.setJobName(savedJob.getName());
        model.setActive(savedJob.isActive());

        model.setPageTitleData(breadcrumbsAdminService.getJobEditBreadcrumbs(savedJob));

        addUsersAndPhotosInfo(model);

        initModelFromSavedJob(model, savedJobId);

        return getStartViewName(model);
    }

    protected ModelAndView saveJobToDBAndReturnView(final AbstractAdminJobModel model, final BindingResult result) {

        final SavedJob savedJob = new SavedJob();


        if (!model.isSaveAsCopy()) {
            savedJob.setId(model.getSavedJobId());
        }
        final AbstractJob job = model.getJob();
        job.setServices(services);

        savedJob.setJob(job);
        savedJob.setActive(model.isActive());
        savedJob.setName(model.getJobName());
        savedJob.setJobType(job.getJobType());

        if (!savedJobService.save(savedJob)) {
            result.reject(translatorService.translate("Error", getLanguage()), translatorService.translate("Data saving error", getLanguage()));
            return getStartViewName(model);
        }

        return getRedirectToJobListView(model);
    }

    protected ModelAndView deleteAndReturnView(final int savedJobId, final AbstractAdminJobModel model) {
        if (!savedJobService.isJobCanBeDeleted(savedJobId)) {
            log.error("The job can not be deleted. It mush have been assigned to a scheduler task or is a parameter of another job");

            return getRedirectToJobListView(model);
        }

        if (!savedJobService.delete(savedJobId)) {
            log.error(String.format("Can not delete saved job %s", savedJobId));
        }

        return getRedirectToJobListView(model);
    }

    protected void stopJobWithChildByUserDemand(final int jobId) {
        jobExecutionService.stopJobWithChildByUserDemand(jobId);
    }

    protected void addUsersAndPhotosInfo(final AbstractAdminJobModel model) {
        model.setPhotosTotal(photoService.getPhotosCount());
        model.setUsersTotal(userService.getUserCount());
    }

    protected static ModelAndView getView(final String viewName, final AbstractAdminJobModel model) {
        return new ModelAndView(viewName, MODEL_NAME, model);
    }

    private ModelAndView getStartViewName(final AbstractAdminJobModel model) {
        return getView(getStartViewName(), model);
    }

    protected ModelAndView getJobListView(final AbstractAdminJobModel model) {
        return getView("/admin/jobs/", model);
    }

    protected ModelAndView getRedirectToJobListView(final AbstractAdminJobModel model) {
        final String referrer = model.getReferrer();
        if (StringUtils.isNotEmpty(referrer)) {
            return getView(String.format("redirect:%s", referrer), model);
        }
        return getView("redirect:/admin/jobs/", model);
    }

    private ModelAndView getRedirectToJobDoneListView(final AbstractAdminJobModel model) {
        return getView("redirect:/admin/jobs/done/", model);
    }

    protected static ModelAndView getRedirectToProgressView(final AbstractAdminJobModel model) {
        final AbstractJob job = model.getJob();
        return getView(String.format("redirect:/admin/jobs/%s/progress/%d/", job.getJobType().getPrefix(), job.getJobId()), model);
    }

    protected ModelAndView getFinishView(final AbstractAdminJobModel model) {
        return getView(FINISH_VIEW, model);
    }

    private void setTabInfo(final AbstractAdminJobModel model) {
        final SavedJobLoaderFactory factory = new SavedJobLoaderFactory(savedJobService, jobExecutionHistoryService);
        model.setTabJobInfosMap(factory.getTabJobInfos());
    }

    public static AbstractJob createInstance(final SavedJobType jobType, final JobRuntimeEnvironment jobEnvironment) {

        switch (jobType) {
            case PREVIEW_GENERATION:
                return new PreviewGenerationJob(jobEnvironment);
            case USER_STATUS:
                return new UserStatusRecalculationJob(jobEnvironment);
            case USER_GENRES_RANKS_RECALCULATING:
                return new UsersGenresRanksRecalculationJob(jobEnvironment);
            case PHOTO_RATING:
                return new PhotoRatingJob(jobEnvironment);
            case REINDEX:
                return new ReindexJob(jobEnvironment);
            case JOB_CHAIN:
                return new JobChainJob(jobEnvironment);
            case USER_GENERATION:
                return new UserGenerationJob(jobEnvironment);
            case ACTIONS_GENERATION:
                return new PhotoActionGenerationVotingJob(jobEnvironment);
            case ACTIONS_GENERATION_COMMENTS:
                return new PhotoActionGenerationCommentsJob(jobEnvironment);
            case ACTIONS_GENERATION_VIEWS:
                return new PhotoActionGenerationPreviewsJob(jobEnvironment);
            case RANK_VOTING_GENERATION:
                return new RankVotingJob(jobEnvironment);
            case FAVORITES_GENERATION:
                return new FavoritesJob(jobEnvironment);
            case PHOTOS_IMPORT:
                return new PhotosImportJob(jobEnvironment);
            case PHOTO_STORAGE_SYNCHRONIZATION:
                return new PhotoStorageSynchronizationJob(jobEnvironment);
            case ACTIVITY_STREAM_CLEAN_UP:
                return new ActivityStreamCleanupJob(jobEnvironment);
            case JOB_EXECUTION_HISTORY_CLEAN_UP:
                return new JobExecutionHistoryCleanupJob(jobEnvironment);
            case ARCHIVING:
                return new ArchivingJob(jobEnvironment);
        }

        throw new IllegalArgumentException(String.format("Illegal SavedJobType: %s", jobType));
    }

    private JobRuntimeEnvironment getCurrentUserJobRuntimeEnvironment() {
        return new JobRuntimeEnvironment(EnvironmentContext.getCurrentUser().getLanguage());
    }

    private Language getLanguage() {
        return EnvironmentContext.getLanguage();
    }
}
