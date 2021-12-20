package com.bordozer.jphoto.admin.controllers.jobs.edit.jobExecutionHistoryCleanup;

import com.bordozer.jphoto.admin.controllers.jobs.edit.AbstractAdminJobModel;
import com.bordozer.jphoto.admin.controllers.jobs.edit.AbstractJobController;
import com.bordozer.jphoto.admin.jobs.entries.JobExecutionHistoryCleanupJob;
import com.bordozer.jphoto.admin.jobs.enums.JobExecutionStatus;
import com.bordozer.jphoto.admin.jobs.enums.SavedJobType;
import com.bordozer.jphoto.core.enums.SavedJobParameterKey;
import com.bordozer.jphoto.core.general.base.CommonProperty;
import com.bordozer.jphoto.utils.ListUtils;
import com.bordozer.jphoto.utils.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

@SessionAttributes(JobExecutionHistoryCleanupJobController.JOB_MODEL_NAME)
@Controller
@RequestMapping("/admin/jobs/jobExecutionHistoryCleanup")
public class JobExecutionHistoryCleanupJobController extends AbstractJobController {

    public static final String JOB_MODEL_NAME = "jobExecutionHistoryCleanupJobModel";
    private static final String START_VIEW = "admin/jobs/edit/jobExecutionHistoryCleanup/Start";

    @Autowired
    private JobExecutionHistoryCleanupJobValidator jobExecutionHistoryCleanupJobValidator;

    @InitBinder
    protected void initBinder(final WebDataBinder binder) {
        binder.setValidator(jobExecutionHistoryCleanupJobValidator);
    }

    @ModelAttribute(JOB_MODEL_NAME)
    public JobExecutionHistoryCleanupJobModel prepareModel() {
        return new JobExecutionHistoryCleanupJobModel();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public ModelAndView showForm(final @ModelAttribute(JOB_MODEL_NAME) JobExecutionHistoryCleanupJobModel model) {
        return doShowForm(model, SavedJobType.JOB_EXECUTION_HISTORY_CLEAN_UP);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/")
    public ModelAndView postForm(final @Valid @ModelAttribute(JOB_MODEL_NAME) JobExecutionHistoryCleanupJobModel model, final BindingResult result) {
        return doPostForm(model, result);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/save/")
    public ModelAndView saveJob(final @Valid @ModelAttribute(JOB_MODEL_NAME) JobExecutionHistoryCleanupJobModel model, final BindingResult result) {
        return doSaveJob(model, result);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{savedJobId}/edit/")
    public ModelAndView editEntry(final @PathVariable("savedJobId") int savedJobId, final @ModelAttribute(JOB_MODEL_NAME) JobExecutionHistoryCleanupJobModel model, final HttpServletRequest request) {
        return processEditing(savedJobId, model, request);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{savedJobId}/delete/")
    public ModelAndView deleteEntry(final @PathVariable("savedJobId") int savedJobId, final @ModelAttribute(JOB_MODEL_NAME) JobExecutionHistoryCleanupJobModel model) {
        return deleteAndReturnView(savedJobId, model);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/progress/{jobId}/")
    public ModelAndView jobProgress(final @PathVariable("jobId") int jobId, @ModelAttribute(JOB_MODEL_NAME) AbstractAdminJobModel model) {
        return getProgressOrFinishView(model, jobId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/stop/{jobId}/")
    public ModelAndView jobStop(final @PathVariable("jobId") int jobId, final @ModelAttribute(JOB_MODEL_NAME) AbstractAdminJobModel model) {
        stopJobWithChildByUserDemand(jobId);
        return getProgressOrFinishView(model, jobId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/finish/")
    public ModelAndView jobFinish(@ModelAttribute(JOB_MODEL_NAME) AbstractAdminJobModel model) {
        return getFinishView(model);
    }

    @Override
    protected void showFormCustomAction(final AbstractAdminJobModel model) {
    }

    @Override
    protected void initJobFromModel(final AbstractAdminJobModel model) {
        final JobExecutionHistoryCleanupJob job = (JobExecutionHistoryCleanupJob) model.getJob();
        final JobExecutionHistoryCleanupJobModel aModel = (JobExecutionHistoryCleanupJobModel) model;

        final int deleteEntriesOlderThenDays = NumberUtils.convertToInt(aModel.getDeleteEntriesOlderThenDays());
        job.setDeleteEntriesOlderThenDays(deleteEntriesOlderThenDays);

        final List<JobExecutionStatus> statuses = newArrayList();
        for (final String _id : aModel.getJobExecutionStatusIdsToDelete()) {
            statuses.add(JobExecutionStatus.getById(NumberUtils.convertToInt(_id)));
        }
        job.setJobExecutionStatusesToDelete(statuses);

        final Date timeFrame = dateUtilsService.getFirstSecondOfTheDayNDaysAgo(deleteEntriesOlderThenDays);
        job.setTotalJopOperations(jobExecutionHistoryService.getEntriesIdsOlderThen(timeFrame, statuses).size());
    }

    @Override
    protected void initModelFromSavedJob(final AbstractAdminJobModel model, final int savedJobId) {
        final JobExecutionHistoryCleanupJobModel aModel = (JobExecutionHistoryCleanupJobModel) model;
        final Map<SavedJobParameterKey, CommonProperty> savedJobParametersMap = savedJobService.getSavedJobParametersMap(savedJobId);

        aModel.setDeleteEntriesOlderThenDays(savedJobParametersMap.get(SavedJobParameterKey.DAYS).getValue());
        aModel.setJobExecutionStatusIdsToDelete(ListUtils.convertIntegerListToString(savedJobParametersMap.get(SavedJobParameterKey.ENTRY_TYPES).getValueListInt()));
    }

    @Override
    protected String getStartViewName() {
        return START_VIEW;
    }
}
