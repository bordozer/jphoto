package com.bordozer.jphoto.admin.controllers.jobs.edit.rankVoting;

import com.bordozer.jphoto.admin.controllers.jobs.edit.AbstractAdminJobModel;
import com.bordozer.jphoto.admin.controllers.jobs.edit.AbstractJobController;
import com.bordozer.jphoto.admin.jobs.entries.RankVotingJob;
import com.bordozer.jphoto.admin.jobs.enums.SavedJobType;
import com.bordozer.jphoto.core.enums.SavedJobParameterKey;
import com.bordozer.jphoto.core.general.base.CommonProperty;
import com.bordozer.jphoto.utils.NumberUtils;
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
import java.util.Map;

@SessionAttributes({RankVotingJobController.JOB_MODEL_NAME})
@Controller
@RequestMapping("/admin/jobs/data/rankVoting")
public class RankVotingJobController extends AbstractJobController {

    public static final String JOB_MODEL_NAME = "rankVotingJobModel";

    private static final String START_VIEW = "admin/jobs/edit/rankVoting/Start";

    @Autowired
    private RankVotingJobValidator rankVotingJobValidator;

    @InitBinder
    protected void initBinder(final WebDataBinder binder) {
        binder.setValidator(rankVotingJobValidator);
    }

    @ModelAttribute(JOB_MODEL_NAME)
    public RankVotingJobModel initModel() {
        return new RankVotingJobModel();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public ModelAndView showForm(final @ModelAttribute(JOB_MODEL_NAME) RankVotingJobModel model) {
        return doShowForm(model, SavedJobType.RANK_VOTING_GENERATION);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/")
    public ModelAndView postForm(final @Valid @ModelAttribute(JOB_MODEL_NAME) RankVotingJobModel model, final BindingResult result) {
        return doPostForm(model, result);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/save/")
    public ModelAndView saveJob(final @Valid @ModelAttribute(JOB_MODEL_NAME) RankVotingJobModel model, final BindingResult result) {
        return doSaveJob(model, result);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{savedJobId}/edit/")
    public ModelAndView editEntry(final @PathVariable("savedJobId") int savedJobId, final @ModelAttribute(JOB_MODEL_NAME) RankVotingJobModel model, final HttpServletRequest request) {
        return processEditing(savedJobId, model, request);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{savedJobId}/delete/")
    public ModelAndView deleteEntry(final @PathVariable("savedJobId") int savedJobId, final @ModelAttribute(JOB_MODEL_NAME) RankVotingJobModel model) {
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
        final RankVotingJobModel aModel = (RankVotingJobModel) model;

        final int usersTotal = aModel.getUsersTotal();
        if (StringUtils.isEmpty(aModel.getActionsQty())) {
            final int actions = usersTotal * 2;
            aModel.setActionsQty(String.valueOf(actions));
        }
    }

    @Override
    protected void initModelFromSavedJob(final AbstractAdminJobModel model, final int savedJobId) {
        final RankVotingJobModel aModel = (RankVotingJobModel) model;

        final Map<SavedJobParameterKey, CommonProperty> savedJobParametersMap = savedJobService.getSavedJobParametersMap(savedJobId);

        final String actionsQty = savedJobParametersMap.get(SavedJobParameterKey.ACTIONS_QTY).getValue();

        aModel.setActionsQty(actionsQty);
    }

    @Override
    protected void initJobFromModel(final AbstractAdminJobModel model) {
        final RankVotingJob job = (RankVotingJob) model.getJob();
        final RankVotingJobModel aModel = (RankVotingJobModel) model;

        job.setTotalJopOperations(NumberUtils.convertToInt(aModel.getActionsQty()));
    }

    @Override
    protected String getStartViewName() {
        return START_VIEW;
    }
}
