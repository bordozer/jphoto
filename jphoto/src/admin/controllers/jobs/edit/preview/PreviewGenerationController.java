package admin.controllers.jobs.edit.preview;

import admin.controllers.jobs.edit.AbstractAdminJobModel;
import admin.controllers.jobs.edit.AbstractJobController;
import admin.jobs.entries.PreviewGenerationJob;
import admin.jobs.enums.SavedJobType;
import core.enums.SavedJobParameterKey;
import core.general.base.CommonProperty;
import core.general.configuration.ConfigurationKey;
import core.general.conversion.ConversionOptions;
import core.services.system.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import utils.NumberUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.awt.*;
import java.util.Map;

@SessionAttributes( {PreviewGenerationController.JOB_MODEL_NAME} )
@Controller
@RequestMapping( "jobs/previews" )
public class PreviewGenerationController extends AbstractJobController {

	public static final String JOB_MODEL_NAME = "previewGenerationModel";

	private static final String START_VIEW = "/admin/jobs/edit/previews/Start";

	@Autowired
	private PreviewGenerationValidator previewGenerationValidator;

	@Autowired
	private ConfigurationService configurationService;

	@InitBinder
	protected void initBinder( WebDataBinder binder ) {
		binder.setValidator( previewGenerationValidator );
	}

	@ModelAttribute( JOB_MODEL_NAME )
	public PreviewGenerationModel prepareModel() {
		return new PreviewGenerationModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public ModelAndView showForm( final @ModelAttribute( PreviewGenerationController.JOB_MODEL_NAME ) PreviewGenerationModel model ) {
		return doShowForm( model, SavedJobType.PREVIEW_GENERATION );
	}

	@RequestMapping( method = RequestMethod.POST, value = "/" )
	public ModelAndView postForm( final @Valid @ModelAttribute( JOB_MODEL_NAME ) PreviewGenerationModel model, final BindingResult result ) {
		return doPostForm( model, result );
	}

	@RequestMapping( method = RequestMethod.POST, value = "/save/" )
	public ModelAndView saveJob( final @Valid @ModelAttribute( JOB_MODEL_NAME ) PreviewGenerationModel model, final BindingResult result ) {
		return doSaveJob( model, result );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{savedJobId}/edit/" )
	public ModelAndView editEntry( final @PathVariable( "savedJobId" ) int savedJobId, final @ModelAttribute( JOB_MODEL_NAME ) PreviewGenerationModel model, final HttpServletRequest request ) {
		return processEditing( savedJobId, model, request );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{savedJobId}/delete/" )
	public ModelAndView deleteEntry( final @PathVariable( "savedJobId" ) int savedJobId, final @ModelAttribute( JOB_MODEL_NAME ) PreviewGenerationModel model ) {
		return deleteAndReturnView( savedJobId, model );
	}

	@RequestMapping( value = "/progress/{jobId}/" )
	public ModelAndView showProgress( final @PathVariable( "jobId" ) int jobId, @ModelAttribute( JOB_MODEL_NAME ) AbstractAdminJobModel model ) {
		return getProgressOrFinishView( model, jobId );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/stop/{jobId}/" )
	public ModelAndView stopJob( final @PathVariable( "jobId" ) int jobId, final @ModelAttribute( JOB_MODEL_NAME ) AbstractAdminJobModel model ) {
		stopJobWithChildByUserDemand( jobId );
		return getProgressOrFinishView( model, jobId );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/finish/" )
	public ModelAndView finishJob( @ModelAttribute( JOB_MODEL_NAME ) AbstractAdminJobModel model ) {
		return getFinishView( model );
	}

	@Override
	protected void showFormCustomAction( final AbstractAdminJobModel model ) {
		final PreviewGenerationModel aModel = ( PreviewGenerationModel ) model;

		aModel.setPreviewSize( configurationService.getString( ConfigurationKey.ADMIN_PHOTO_PREVIEW_DIMENSION ) );
	}

	@Override
	protected void initJobFromModel( final AbstractAdminJobModel model ) {
		final PreviewGenerationJob job = ( PreviewGenerationJob ) model.getJob();
		final PreviewGenerationModel aModel = ( PreviewGenerationModel ) model;

		final ConversionOptions options = new ConversionOptions();
		options.setDensity( 72 );
		final int previewSize = NumberUtils.convertToInt( aModel.getPreviewSize() );
		options.setDimension( new Dimension( previewSize, previewSize ) );

		job.setOptions( options );

		job.setPreviewSize( previewSize );
		job.setSkipPhotosWithExistingPreview( aModel.isSkipPhotosWithExistingPreview() );
	}

	@Override
	protected void initModelFromSavedJob( final AbstractAdminJobModel model, final int savedJobId ) {
		final PreviewGenerationModel aModel = ( PreviewGenerationModel ) model;

		final Map<SavedJobParameterKey,CommonProperty> savedJobParametersMap = savedJobService.getSavedJobParametersMap( savedJobId );

		final String previewSize = savedJobParametersMap.get( SavedJobParameterKey.PARAM_PREVIEW_SIZE ).getValue();
		final boolean isSkip = savedJobParametersMap.get( SavedJobParameterKey.PARAM_SKIP_PHOTO_WITH_PREVIEW ).getValueBoolean();

		aModel.setPreviewSize( previewSize );
		aModel.setSkipPhotosWithExistingPreview( isSkip );
	}

	@Override
	protected String getStartViewName() {
		return START_VIEW;
	}
}
