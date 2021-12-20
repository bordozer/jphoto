package com.bordozer.jphoto.admin.controllers.jobs.edit.activityStream;

import com.bordozer.jphoto.admin.controllers.jobs.edit.AbstractAdminJobModel;
import com.bordozer.jphoto.admin.controllers.jobs.edit.AbstractJobController;
import com.bordozer.jphoto.admin.jobs.entries.ActivityStreamCleanupJob;
import com.bordozer.jphoto.admin.jobs.enums.SavedJobType;
import com.bordozer.jphoto.core.enums.SavedJobParameterKey;
import com.bordozer.jphoto.core.general.base.CommonProperty;
import com.bordozer.jphoto.ui.activity.ActivityType;
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
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

@SessionAttributes(ActivityStreamCleanupJobController.JOB_MODEL_NAME)
@Controller
@RequestMapping("/admin/jobs/activityStreamCleanup")
public class ActivityStreamCleanupJobController extends AbstractJobController {

    public static final String JOB_MODEL_NAME = "activityStreamCleanupJobModel";
    private static final String START_VIEW = "admin/jobs/edit/activityStream/Start";

    @Autowired
    private ActivityStreamCleanupJobValidator activityStreamCleanupJobValidator;

    @InitBinder
    protected void initBinder(final WebDataBinder binder) {
        binder.setValidator(activityStreamCleanupJobValidator);
    }

    @ModelAttribute(JOB_MODEL_NAME)
    public ActivityStreamCleanupJobModel prepareModel() {
        return new ActivityStreamCleanupJobModel();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public ModelAndView showForm(final @ModelAttribute(JOB_MODEL_NAME) ActivityStreamCleanupJobModel model) {
        return doShowForm(model, SavedJobType.ACTIVITY_STREAM_CLEAN_UP);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/")
    public ModelAndView postForm(final @Valid @ModelAttribute(JOB_MODEL_NAME) ActivityStreamCleanupJobModel model, final BindingResult result) {
        return doPostForm(model, result);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/save/")
    public ModelAndView saveJob(final @Valid @ModelAttribute(JOB_MODEL_NAME) ActivityStreamCleanupJobModel model, final BindingResult result) {
        return doSaveJob(model, result);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{savedJobId}/edit/")
    public ModelAndView editEntry(final @PathVariable("savedJobId") int savedJobId, final @ModelAttribute(JOB_MODEL_NAME) ActivityStreamCleanupJobModel model, final HttpServletRequest request) {
        return processEditing(savedJobId, model, request);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{savedJobId}/delete/")
    public ModelAndView deleteEntry(final @PathVariable("savedJobId") int savedJobId, final @ModelAttribute(JOB_MODEL_NAME) ActivityStreamCleanupJobModel model) {
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
        final ActivityStreamCleanupJob job = (ActivityStreamCleanupJob) model.getJob();
        final ActivityStreamCleanupJobModel aModel = (ActivityStreamCleanupJobModel) model;

        job.setLeaveActivityForDays(NumberUtils.convertToInt(aModel.getLeaveActivityForDays()));
        job.setTotalJopOperations(1);

        final List<ActivityType> activityTypes = newArrayList();
        for (final String activityTypeId : aModel.getActivityStreamTypeIdsToDelete()) {
            activityTypes.add(ActivityType.getById(Integer.parseInt(activityTypeId)));
        }
        job.setActivityTypes(activityTypes);

    }

    @Override
    protected void initModelFromSavedJob(final AbstractAdminJobModel model, final int savedJobId) {
        final ActivityStreamCleanupJobModel aModel = (ActivityStreamCleanupJobModel) model;
        final Map<SavedJobParameterKey, CommonProperty> savedJobParametersMap = savedJobService.getSavedJobParametersMap(savedJobId);

        aModel.setLeaveActivityForDays(savedJobParametersMap.get(SavedJobParameterKey.DAYS).getValue());
        aModel.setActivityStreamTypeIdsToDelete(ListUtils.convertIntegerListToString(savedJobParametersMap.get(SavedJobParameterKey.ENTRY_TYPES).getValueListInt()));
    }

    @Override
    protected String getStartViewName() {
        return START_VIEW;
    }
}
