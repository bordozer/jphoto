package com.bordozer.jphoto.admin.controllers.jobs.edit.genreRank;

import com.bordozer.jphoto.admin.controllers.jobs.edit.AbstractAdminJobModel;
import com.bordozer.jphoto.admin.controllers.jobs.edit.AbstractJobController;
import com.bordozer.jphoto.admin.jobs.entries.UsersGenresRanksRecalculationJob;
import com.bordozer.jphoto.admin.jobs.enums.SavedJobType;
import com.bordozer.jphoto.core.services.user.UserService;
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

@SessionAttributes(UsersGenresRanksRecalculationController.JOB_MODEL_NAME)
@Controller
@RequestMapping("/admin/jobs/genreRank")
public class UsersGenresRanksRecalculationController extends AbstractJobController {

    public static final String JOB_MODEL_NAME = "genreRankRecalculationModel";

    private static final String START_VIEW = "admin/jobs/edit/genreRank/Start";

    @Autowired
    private UsersGenresRanksRecalculationValidator usersGenresRanksRecalculationValidator;

    @Autowired
    private UserService userService;

    @InitBinder
    protected void initBinder(final WebDataBinder binder) {
        binder.setValidator(usersGenresRanksRecalculationValidator);
    }

    @ModelAttribute(JOB_MODEL_NAME)
    public UsersGenresRanksRecalculationModel prepareModel() {
        return new UsersGenresRanksRecalculationModel();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public ModelAndView showForm(final @ModelAttribute(JOB_MODEL_NAME) UsersGenresRanksRecalculationModel model) {
        return doShowForm(model, SavedJobType.USER_GENRES_RANKS_RECALCULATING);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/")
    public ModelAndView postForm(final @Valid @ModelAttribute(JOB_MODEL_NAME) UsersGenresRanksRecalculationModel model, final BindingResult result) {
        return doPostForm(model, result);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/save/")
    public ModelAndView saveJob(final @Valid @ModelAttribute(JOB_MODEL_NAME) UsersGenresRanksRecalculationModel model, final BindingResult result) {
        return doSaveJob(model, result);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{savedJobId}/edit/")
    public ModelAndView editEntry(final @PathVariable("savedJobId") int savedJobId, final @ModelAttribute(JOB_MODEL_NAME) UsersGenresRanksRecalculationModel model, final HttpServletRequest request) {
        return processEditing(savedJobId, model, request);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{savedJobId}/delete/")
    public ModelAndView deleteEntry(final @PathVariable("savedJobId") int savedJobId, final @ModelAttribute(JOB_MODEL_NAME) UsersGenresRanksRecalculationModel model) {
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

    protected void initModelFromSavedJob(final AbstractAdminJobModel model, final int savedJobId) {
    }

    @Override
    protected void initJobFromModel(final AbstractAdminJobModel model) {
        final UsersGenresRanksRecalculationJob job = (UsersGenresRanksRecalculationJob) model.getJob();

        job.setTotalJopOperations(userService.getUserCount());
    }

    @Override
    protected String getStartViewName() {
        return START_VIEW;
    }
}
