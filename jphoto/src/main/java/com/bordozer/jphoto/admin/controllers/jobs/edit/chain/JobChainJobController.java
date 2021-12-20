package com.bordozer.jphoto.admin.controllers.jobs.edit.chain;

import com.bordozer.jphoto.admin.controllers.jobs.edit.AbstractAdminJobModel;
import com.bordozer.jphoto.admin.controllers.jobs.edit.AbstractJobController;
import com.bordozer.jphoto.admin.jobs.entries.JobChainJob;
import com.bordozer.jphoto.admin.jobs.enums.JobRunMode;
import com.bordozer.jphoto.admin.jobs.enums.SavedJobType;
import com.bordozer.jphoto.admin.jobs.general.SavedJob;
import com.bordozer.jphoto.admin.services.jobs.SavedJobService;
import com.bordozer.jphoto.core.enums.SavedJobParameterKey;
import com.bordozer.jphoto.core.general.base.CommonProperty;
import com.bordozer.jphoto.utils.ListUtils;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

@SessionAttributes(JobChainJobController.JOB_MODEL_NAME)
@Controller
@RequestMapping("/admin/jobs/chain")
public class JobChainJobController extends AbstractJobController {

    public static final String JOB_MODEL_NAME = "jobChainJobModel";
    private static final String START_VIEW = "admin/jobs/edit/chain/Start";

    @Autowired
    private JobChainJobValidator jobChainJobValidator;

    @Autowired
    private SavedJobService savedJobService;

    @InitBinder
    protected void initBinder(final WebDataBinder binder) {
        binder.setValidator(jobChainJobValidator);
    }

    @ModelAttribute(JOB_MODEL_NAME)
    public JobChainJobModel prepareModel() {
        return new JobChainJobModel();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public ModelAndView showForm(final @ModelAttribute(JOB_MODEL_NAME) JobChainJobModel model) {
        return doShowForm(model, SavedJobType.JOB_CHAIN);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/")
    public ModelAndView postForm(final @Valid @ModelAttribute(JOB_MODEL_NAME) JobChainJobModel model, final BindingResult result) {
        return doPostForm(model, result);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/save/")
    public ModelAndView saveJob(final @Valid @ModelAttribute(JOB_MODEL_NAME) JobChainJobModel model, final BindingResult result) {
        return doSaveJob(model, result);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{savedJobId}/edit/")
    public ModelAndView editEntry(final @PathVariable("savedJobId") int savedJobId, final @ModelAttribute(JOB_MODEL_NAME) JobChainJobModel model, final HttpServletRequest request) {
        return processEditing(savedJobId, model, request);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{savedJobId}/delete/")
    public ModelAndView deleteEntry(final @PathVariable("savedJobId") int savedJobId, final @ModelAttribute(JOB_MODEL_NAME) JobChainJobModel model) {
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
        final JobChainJobModel aModel = (JobChainJobModel) model;

        final List<SavedJob> savedJobs = getSavedJobList(aModel);
        aModel.setSavedJobs(savedJobs);

        final List<String> list = newArrayList();
        aModel.setSelectedSavedJobsIds(list);

        final JobRunMode jobRunMode = JobRunMode.PARALLEL;
        aModel.setJobRunModeId(jobRunMode.getId());
    }

    @Override
    protected void initJobFromModel(final AbstractAdminJobModel model) {

        final JobChainJobModel aModel = (JobChainJobModel) model;
        final JobChainJob job = (JobChainJob) model.getJob();

        job.setSavedJobToExecuteIds(ListUtils.convertStringListToInteger(aModel.getSelectedSavedJobsIds()));
        job.setJobRunMode(JobRunMode.getById(aModel.getJobRunModeId()));

        job.setTotalJopOperations(aModel.getSelectedSavedJobsIds().size());

        final String stopDependantJobsIfError = aModel.getBreakChainExecutionIfError();
        job.setBreakChainExecutionIfError(StringUtils.isNotEmpty(stopDependantJobsIfError) && stopDependantJobsIfError.equals("ON"));
    }

    @Override
    protected void initModelFromSavedJob(final AbstractAdminJobModel model, final int savedJobId) {
        final JobChainJobModel aModel = (JobChainJobModel) model;

        final Map<SavedJobParameterKey, CommonProperty> savedJobParametersMap = savedJobService.getSavedJobParametersMap(savedJobId);

        final List<Integer> selectedSavedJobIds = savedJobParametersMap.get(SavedJobParameterKey.PARAM_SAVED_JOB_CHAIN).getValueListInt();

        aModel.setSelectedSavedJobsIds(ListUtils.convertIntegerListToString(selectedSavedJobIds));

        final List<SavedJob> savedJobs = getSavedJobList(aModel);

        final List<SavedJob> selectedJobs = newArrayList();
        for (final int jobId : selectedSavedJobIds) {
            final SavedJob job = getJobById(jobId, savedJobs);
            if (job != null) {
                selectedJobs.add(job);
            }
        }

        final List<SavedJob> unselectedJobs = newArrayList();
        for (final SavedJob job : savedJobs) {
            if (selectedSavedJobIds.contains(job.getId())) {
                continue;
            }
            unselectedJobs.add(job);
        }
        Collections.sort(unselectedJobs, new Comparator<SavedJob>() {
            @Override
            public int compare(final SavedJob o1, final SavedJob o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });

        final List<SavedJob> jobListOrdered = newArrayList();
        jobListOrdered.addAll(selectedJobs);
        jobListOrdered.addAll(unselectedJobs);
        aModel.setSavedJobs(jobListOrdered);

        aModel.setJobRunModeId(savedJobParametersMap.get(SavedJobParameterKey.JOB_RUN_MODE_ID).getValueInt());
        aModel.setBreakChainExecutionIfError(savedJobParametersMap.get(SavedJobParameterKey.BREAK_CHAIN_EXECUTION_IF_ERROR).getValueBoolean() ? "ON" : "");
    }

    private SavedJob getJobById(final int jobId, final List<SavedJob> jobs) {
        for (final SavedJob job : jobs) {
            if (job.getId() == jobId) {
                return job;
            }
        }

        return null;
    }

    private List<SavedJob> getSavedJobList(final JobChainJobModel aModel) {
        final int currentSavedJobId = aModel.getJob().getSavedJobId();

        final List<SavedJob> savedJobs = savedJobService.loadAll();

        final Iterator<SavedJob> iterator = savedJobs.iterator();
        while (iterator.hasNext()) {
            final SavedJob savedJob = iterator.next();
            if (savedJob.getId() == currentSavedJobId) {
                iterator.remove();
                break;
            }
        }

        return savedJobs;
    }

    @Override
    protected String getStartViewName() {
        return START_VIEW;
    }
}
