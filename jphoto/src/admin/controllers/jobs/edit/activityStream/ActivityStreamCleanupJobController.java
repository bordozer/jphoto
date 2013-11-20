package admin.controllers.jobs.edit.activityStream;

import admin.controllers.jobs.edit.AbstractAdminJobModel;
import admin.controllers.jobs.edit.AbstractJobController;
import admin.jobs.entries.ActivityStreamCleanupJob;
import admin.jobs.enums.SavedJobType;
import core.enums.SavedJobParameterKey;
import core.general.base.CommonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import utils.NumberUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@SessionAttributes( ActivityStreamCleanupJobController.JOB_MODEL_NAME )
@Controller
@RequestMapping( "jobs/activityStreamCleanup" )
public class ActivityStreamCleanupJobController extends AbstractJobController {

	public static final String JOB_MODEL_NAME = "activityStreamCleanupJobModel";
	private static final String START_VIEW = "admin/jobs/edit/activityStream/Start";

	@Autowired
	private ActivityStreamCleanupJobValidator activityStreamCleanupJobValidator;

	@InitBinder
	protected void initBinder( final WebDataBinder binder ) {
		binder.setValidator( activityStreamCleanupJobValidator );
	}

	@ModelAttribute( JOB_MODEL_NAME )
	public ActivityStreamCleanupJobModel prepareModel() {
		return new ActivityStreamCleanupJobModel();
	}
	
	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public ModelAndView showForm( final @ModelAttribute( JOB_MODEL_NAME ) ActivityStreamCleanupJobModel model ) {
		return doShowForm( model, SavedJobType.ACTIVITY_STREAM_CLEAN_UP );
	}

	@RequestMapping( method = RequestMethod.POST, value = "/" )
	public ModelAndView postForm( final @Valid @ModelAttribute( JOB_MODEL_NAME ) ActivityStreamCleanupJobModel model, final BindingResult result ) {
		return doPostForm( model, result );
	}

	@RequestMapping( method = RequestMethod.POST, value = "/save/" )
	public ModelAndView saveJob( final @Valid @ModelAttribute( JOB_MODEL_NAME ) ActivityStreamCleanupJobModel model, final BindingResult result ) {
		return doSaveJob( model, result );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{savedJobId}/edit/" )
	public ModelAndView editEntry( final @PathVariable( "savedJobId" ) int savedJobId, final @ModelAttribute( JOB_MODEL_NAME ) ActivityStreamCleanupJobModel model, final HttpServletRequest request ) {
		return processEditing( savedJobId, model, request );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{savedJobId}/delete/" )
	public ModelAndView deleteEntry( final @PathVariable( "savedJobId" ) int savedJobId, final @ModelAttribute( JOB_MODEL_NAME ) ActivityStreamCleanupJobModel model ) {
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
		final ActivityStreamCleanupJob job = ( ActivityStreamCleanupJob ) model.getJob();
		final ActivityStreamCleanupJobModel aModel = ( ActivityStreamCleanupJobModel ) model;

		job.setLeaveActivityForDays( NumberUtils.convertToInt( aModel.getLeaveActivityForDays() ) );
		job.setTotalJopOperations( 1 );
	}

	@Override
	protected void initModelFromSavedJob( final AbstractAdminJobModel model, final int savedJobId ) {
		final ActivityStreamCleanupJobModel aModel = ( ActivityStreamCleanupJobModel ) model;
		final Map<SavedJobParameterKey,CommonProperty> savedJobParametersMap = savedJobService.getSavedJobParametersMap( savedJobId );

		aModel.setLeaveActivityForDays( savedJobParametersMap.get( SavedJobParameterKey.DAYS ).getValue() );
	}

	@Override
	protected String getStartViewName() {
		return START_VIEW;
	}
}
