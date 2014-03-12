package admin.controllers.jobs.edit.userStatus;

import admin.controllers.jobs.edit.AbstractAdminJobModel;
import admin.controllers.jobs.edit.AbstractJobController;
import admin.jobs.entries.UserStatusRecalculationJob;
import admin.jobs.enums.SavedJobType;
import core.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@SessionAttributes( UserStatusRecalculationJobController.JOB_MODEL_NAME )
@Controller
@RequestMapping( "jobs/user-status" )
public class UserStatusRecalculationJobController extends AbstractJobController {

	public static final String JOB_MODEL_NAME = "userStatusRecalculationJobModel";
	private static final String START_VIEW = "admin/jobs/edit/userStatus/Start";

	@Autowired
	private UserService userService;

	@Autowired
	private UserStatusRecalculationJobValidator userStatusRecalculationJobValidator;

	@InitBinder
	protected void initBinder( final WebDataBinder binder ) {
		binder.setValidator( userStatusRecalculationJobValidator );
	}

	@ModelAttribute( JOB_MODEL_NAME )
	public UserStatusRecalculationJobModel prepareModel() {
		return new UserStatusRecalculationJobModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public ModelAndView showForm( final @ModelAttribute( JOB_MODEL_NAME ) UserStatusRecalculationJobModel model ) {
		return doShowForm( model, SavedJobType.USER_STATUS );
	}

	@RequestMapping( method = RequestMethod.POST, value = "/" )
	public ModelAndView postForm( final @Valid @ModelAttribute( JOB_MODEL_NAME ) UserStatusRecalculationJobModel model, final BindingResult result ) {
		return doPostForm( model, result );
	}

	@RequestMapping( method = RequestMethod.POST, value = "/save/" )
	public ModelAndView saveJob( final @Valid @ModelAttribute( JOB_MODEL_NAME ) UserStatusRecalculationJobModel model, final BindingResult result ) {
		return doSaveJob( model, result );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{savedJobId}/edit/" )
	public ModelAndView editEntry( final @PathVariable( "savedJobId" ) int savedJobId, final @ModelAttribute( JOB_MODEL_NAME ) UserStatusRecalculationJobModel model, final HttpServletRequest request ) {
		return processEditing( savedJobId, model, request );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{savedJobId}/delete/" )
	public ModelAndView deleteEntry( final @PathVariable( "savedJobId" ) int savedJobId, final @ModelAttribute( JOB_MODEL_NAME ) UserStatusRecalculationJobModel model ) {
		return deleteAndReturnView( savedJobId, model );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/progress/{jobId}/" )
	public ModelAndView jobProgress( final @PathVariable( "jobId" ) int jobId, @ModelAttribute( JOB_MODEL_NAME ) AbstractAdminJobModel model ) {
		return getProgressOrFinishView( model, jobId );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/stop/{jobId}/" )
	public ModelAndView jobStop( final @PathVariable( "jobId" ) int jobId, final @ModelAttribute( JOB_MODEL_NAME ) AbstractAdminJobModel model ) {
		stopJobWithChildByUserDemand( jobId );
		return getProgressOrFinishView( model, jobId );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/finish/" )
	public ModelAndView jobFinish( @ModelAttribute( JOB_MODEL_NAME ) AbstractAdminJobModel model ) {
		return getFinishView( model );
	}

	@Override
	protected void showFormCustomAction( final AbstractAdminJobModel model ) {
	}

	@Override
	protected void initJobFromModel( final AbstractAdminJobModel model ) {
		final UserStatusRecalculationJob job = ( UserStatusRecalculationJob ) model.getJob();

		job.setTotalJopOperations( userService.getUserCount() );
	}

	@Override
	protected void initModelFromSavedJob( final AbstractAdminJobModel model, final int savedJobId ) {
	}

	@Override
	protected String getStartViewName() {
		return START_VIEW;
	}
}
