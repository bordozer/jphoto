package com.bordozer.jphoto.admin.controllers.jobs.edit.photoRating;

import com.bordozer.jphoto.admin.controllers.jobs.edit.AbstractAdminJobModel;
import com.bordozer.jphoto.admin.controllers.jobs.edit.DateRangableController;
import com.bordozer.jphoto.admin.jobs.entries.PhotoRatingJob;
import com.bordozer.jphoto.admin.jobs.enums.SavedJobType;
import com.bordozer.jphoto.core.enums.SavedJobParameterKey;
import com.bordozer.jphoto.core.general.base.CommonProperty;
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

@SessionAttributes({PhotoRatingJobController.JOB_MODEL_NAME})
@Controller
@RequestMapping("/admin/jobs/photoRating")
public class PhotoRatingJobController extends DateRangableController {

    private static final String START_VIEW = "admin/jobs/edit/photoRating/Start";
    public static final String JOB_MODEL_NAME = "photoRatingJobModel";

    @Autowired
    private PhotoRatingJobValidator photoRatingJobValidator;

    @InitBinder
    protected void initBinder(final WebDataBinder binder) {
        binder.setValidator(photoRatingJobValidator);
    }

    @ModelAttribute(JOB_MODEL_NAME)
    public PhotoRatingJobModel prepareModel() {
        final PhotoRatingJobModel model = new PhotoRatingJobModel();

        prepareDateRange(model);

        return model;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public ModelAndView showForm(@ModelAttribute(JOB_MODEL_NAME) PhotoRatingJobModel model) {
        return doShowForm(model, SavedJobType.PHOTO_RATING);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/")
    public ModelAndView postForm(@Valid @ModelAttribute(JOB_MODEL_NAME) PhotoRatingJobModel model, final BindingResult result) {
        return doPostForm(model, result);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/save/")
    public ModelAndView saveJob(final @Valid @ModelAttribute(JOB_MODEL_NAME) PhotoRatingJobModel model, final BindingResult result) {
        return doSaveJob(model, result);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{savedJobId}/edit/")
    public ModelAndView editEntry(final @PathVariable("savedJobId") int savedJobId, final @ModelAttribute(JOB_MODEL_NAME) PhotoRatingJobModel model, final HttpServletRequest request) {
        return processEditing(savedJobId, model, request);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{savedJobId}/delete/")
    public ModelAndView deleteEntry(final @PathVariable("savedJobId") int savedJobId, final @ModelAttribute(JOB_MODEL_NAME) PhotoRatingJobModel model) {
        return deleteAndReturnView(savedJobId, model);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/progress/{jobId}/")
    public ModelAndView showProgress(final @PathVariable("jobId") int jobId, @ModelAttribute(JOB_MODEL_NAME) AbstractAdminJobModel model) {
        return getProgressOrFinishView(model, jobId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/stop/{jobId}/")
    public ModelAndView stopJob(final @PathVariable("jobId") int jobId, @ModelAttribute(JOB_MODEL_NAME) AbstractAdminJobModel model) {
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
        final PhotoRatingJob job = (PhotoRatingJob) model.getJob();
        final PhotoRatingJobModel aModel = (PhotoRatingJobModel) model;

        initDateRangeJob(aModel, job);

        job.setTotalJopOperations(1); // TODO
    }

    @Override
    protected void initModelFromSavedJob(final AbstractAdminJobModel model, final int savedJobId) {
        final PhotoRatingJobModel aModel = (PhotoRatingJobModel) model;

        final Map<SavedJobParameterKey, CommonProperty> savedJobParametersMap = savedJobService.getSavedJobParametersMap(savedJobId);

        initModelDateRange(aModel, savedJobParametersMap);
    }

    @Override
    protected String getStartViewName() {
        return START_VIEW;
    }
}
