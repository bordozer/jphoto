package com.bordozer.jphoto.admin.controllers.jobs.edit.users;

import com.bordozer.jphoto.admin.controllers.jobs.edit.AbstractAdminJobModel;
import com.bordozer.jphoto.admin.controllers.jobs.edit.AbstractJobController;
import com.bordozer.jphoto.admin.jobs.entries.UserGenerationJob;
import com.bordozer.jphoto.admin.jobs.enums.SavedJobType;
import com.bordozer.jphoto.core.enums.SavedJobParameterKey;
import com.bordozer.jphoto.core.general.base.CommonProperty;
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
import java.util.Map;

@SessionAttributes({UserGenerationController.JOB_MODEL_NAME})
@Controller
@RequestMapping("/admin/jobs/data/users")
public class UserGenerationController extends AbstractJobController {

    private static final String START_VIEW = "admin/jobs/edit/users/Start";
    public static final String JOB_MODEL_NAME = "userGenerationModel";

    @Autowired
    private UserGenerationValidator userGenerationValidator;

    @InitBinder
    protected void initBinder(final WebDataBinder binder) {
        binder.setValidator(userGenerationValidator);
    }

    @ModelAttribute(JOB_MODEL_NAME)
    public UserGenerationModel prepareModel() {
        return new UserGenerationModel();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public ModelAndView showForm(final @ModelAttribute(JOB_MODEL_NAME) UserGenerationModel model) {
        return doShowForm(model, SavedJobType.USER_GENERATION);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/")
    public ModelAndView postForm(final @Valid @ModelAttribute(JOB_MODEL_NAME) UserGenerationModel model, final BindingResult result) {
        return doPostForm(model, result);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/save/")
    public ModelAndView saveJob(final @Valid @ModelAttribute(JOB_MODEL_NAME) UserGenerationModel model, final BindingResult result) {
        return doSaveJob(model, result);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{savedJobId}/edit/")
    public ModelAndView editEntry(final @PathVariable("savedJobId") int savedJobId, final @ModelAttribute(JOB_MODEL_NAME) UserGenerationModel model, final HttpServletRequest request) {
        return processEditing(savedJobId, model, request);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{savedJobId}/delete/")
    public ModelAndView deleteEntry(final @PathVariable("savedJobId") int savedJobId, final @ModelAttribute(JOB_MODEL_NAME) UserGenerationModel model) {
        return deleteAndReturnView(savedJobId, model);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/progress/{jobId}/")
    public ModelAndView showProgress(final @PathVariable("jobId") int jobId, @ModelAttribute(JOB_MODEL_NAME) AbstractAdminJobModel model) {
        return getProgressOrFinishView(model, jobId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/stop/{jobId}/")
    public ModelAndView stopJob(final @PathVariable("jobId") int jobId, final @ModelAttribute(JOB_MODEL_NAME) AbstractAdminJobModel model) {
        stopJobWithChildByUserDemand(jobId);
        return getProgressOrFinishView(model, jobId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/finish/")
    public ModelAndView finishJob(@ModelAttribute(JOB_MODEL_NAME) AbstractAdminJobModel model) {
        return getFinishView(model);
    }

    @Override
    protected void showFormCustomAction(final AbstractAdminJobModel model) {
    }

    @Override
    protected void initJobFromModel(final AbstractAdminJobModel model) {
        final UserGenerationJob job = (UserGenerationJob) model.getJob();
        final UserGenerationModel aModel = (UserGenerationModel) model;

        job.setAvatarsDir(aModel.getAvatarsDir());

        job.setTotalJopOperations(NumberUtils.convertToInt(aModel.getUserQtyLimit()));
    }

    @Override
    protected void initModelFromSavedJob(final AbstractAdminJobModel model, final int savedJobId) {
        final UserGenerationModel aModel = (UserGenerationModel) model;

        final Map<SavedJobParameterKey, CommonProperty> savedJobParametersMap = savedJobService.getSavedJobParametersMap(savedJobId);

        final String userQtyLimit = savedJobParametersMap.get(SavedJobParameterKey.PARAM_USER_QTY).getValue();
        final String avatarsDir = savedJobParametersMap.get(SavedJobParameterKey.PARAM_AVATARS_DIR).getValue();

        aModel.setUserQtyLimit(userQtyLimit);
        aModel.setAvatarsDir(avatarsDir);
    }

    @Override
    protected String getStartViewName() {
        return START_VIEW;
    }
}
