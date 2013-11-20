package admin.controllers.jobs.edit.action;

import admin.controllers.jobs.edit.AbstractAdminJobModel;
import admin.controllers.jobs.edit.DateRangableController;
import admin.jobs.entries.AbstractDateRangeableJob;
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

@SessionAttributes( {PhotoActionGenerationController.JOB_MODEL_NAME} )
@Controller
@RequestMapping( "jobs/data/voting" )
public class PhotoActionGenerationController extends DateRangableController {

	public static final String JOB_MODEL_NAME = "jobModelPhotoAction";
	private static final String START_VIEW = "admin/jobs/edit/actions/Start";

	@Autowired
	private PhotoActionGenerationValidator photoActionGenerationValidator;

	@InitBinder
	protected void initBinder( WebDataBinder binder ) {
		binder.setValidator( photoActionGenerationValidator );
	}

	@ModelAttribute( JOB_MODEL_NAME )
	public PhotoActionGenerationModel prepareModel() {
		final PhotoActionGenerationModel model = new PhotoActionGenerationModel();

		model.setTotalActions( "100" );
		model.setPhotosQty( "1000" );

		prepareDateRange( model );

		return model;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public ModelAndView showForm( @ModelAttribute( JOB_MODEL_NAME ) PhotoActionGenerationModel model ) {
		return doShowForm( model, SavedJobType.ACTIONS_GENERATION );
	}

	@RequestMapping( method = RequestMethod.POST, value = "/" )
	public ModelAndView postForm( @Valid @ModelAttribute( JOB_MODEL_NAME ) PhotoActionGenerationModel model, final BindingResult result ) {
		return doPostForm( model, result );
	}

	@RequestMapping( method = RequestMethod.POST, value = "/save/" )
	public ModelAndView saveJob( final @Valid @ModelAttribute( JOB_MODEL_NAME ) PhotoActionGenerationModel model, final BindingResult result ) {
		return doSaveJob( model, result );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{savedJobId}/edit/" )
	public ModelAndView editEntry( final @PathVariable( "savedJobId" ) int savedJobId, final @ModelAttribute( JOB_MODEL_NAME ) PhotoActionGenerationModel model, final HttpServletRequest request ) {
		return processEditing( savedJobId, model, request );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{savedJobId}/delete/" )
	public ModelAndView deleteEntry( final @PathVariable( "savedJobId" ) int savedJobId, final @ModelAttribute( JOB_MODEL_NAME ) PhotoActionGenerationModel model ) {
		return deleteAndReturnView( savedJobId, model );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/progress/{jobId}/" )
	public ModelAndView showProgress( final @PathVariable( "jobId" ) int jobId, @ModelAttribute( JOB_MODEL_NAME ) AbstractAdminJobModel model ) {
		return getProgressOrFinishView( model, jobId );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/stop/{jobId}/" )
	public ModelAndView stopJob( final @PathVariable( "jobId" ) int jobId, @ModelAttribute( JOB_MODEL_NAME ) AbstractAdminJobModel model ) {
		stopJobWithChildByUserDemand( jobId );
		return getProgressOrFinishView( model, jobId );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/finish/" )
	public ModelAndView finishJob( @ModelAttribute( JOB_MODEL_NAME ) AbstractAdminJobModel model ) {
		return getFinishView( model );
	}

	@Override
	protected void showFormCustomAction( final AbstractAdminJobModel model ) {
	}

	@Override
	protected void initJobFromModel( final AbstractAdminJobModel model ) {
		final AbstractDateRangeableJob job = ( AbstractDateRangeableJob ) model.getJob();
		final PhotoActionGenerationModel aModel = ( PhotoActionGenerationModel) model;

		final int totalActions = NumberUtils.convertToInt( aModel.getTotalActions() );
		job.setPhotoQtyLimit( NumberUtils.convertToInt( aModel.getPhotosQty() ) );

		initDateRangeJob( aModel, job );

		job.setTotalJopOperations( totalActions );
	}

	@Override
	protected void initModelFromSavedJob( final AbstractAdminJobModel model, final int savedJobId ) {
		final PhotoActionGenerationModel aModel = ( PhotoActionGenerationModel ) model;

		final Map<SavedJobParameterKey, CommonProperty> savedJobParametersMap = savedJobService.getSavedJobParametersMap( savedJobId );

		final String actions = savedJobParametersMap.get( SavedJobParameterKey.PARAM_ACTIONS_QTY ).getValue();
		final String photos = savedJobParametersMap.get( SavedJobParameterKey.PARAM_PHOTOS_QTY ).getValue();

		aModel.setTotalActions( actions );
		aModel.setPhotosQty( photos.equals( "0" ) ? "" : photos );

		initModelDateRange( aModel, savedJobParametersMap );
	}

	@Override
	protected String getStartViewName() {
		return START_VIEW;
	}
}
