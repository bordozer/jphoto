package admin.controllers.jobs.edit.archiving;

import admin.controllers.jobs.edit.AbstractAdminJobModel;
import admin.controllers.jobs.edit.AbstractJobController;
import admin.jobs.entries.ArchivingJob;
import admin.jobs.enums.SavedJobType;
import core.enums.SavedJobParameterKey;
import core.general.base.CommonProperty;
import core.general.configuration.ConfigurationKey;
import core.services.system.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@SessionAttributes( ArchivingJobController.JOB_MODEL_NAME )
@Controller
@RequestMapping( "jobs/archiving" )
public class ArchivingJobController extends AbstractJobController {

	public static final String JOB_MODEL_NAME = "archivingJobJobModel";
	private static final String START_VIEW = "admin/jobs/edit/archiving/Start";

	@Autowired
	private ArchivingJobJobValidator archivingJobJobValidator;

	@Autowired
	private ConfigurationService configurationService;

	@InitBinder
	protected void initBinder( final WebDataBinder binder ) {
		binder.setValidator( archivingJobJobValidator );
	}

	@ModelAttribute( JOB_MODEL_NAME )
	public ArchivingJobJobModel prepareModel() {
		return new ArchivingJobJobModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public ModelAndView showForm( final @ModelAttribute( JOB_MODEL_NAME ) ArchivingJobJobModel model ) {
		return doShowForm( model, SavedJobType.ARCHIVING );
	}

	@RequestMapping( method = RequestMethod.POST, value = "/" )
	public ModelAndView postForm( final @Valid @ModelAttribute( JOB_MODEL_NAME ) ArchivingJobJobModel model, final BindingResult result ) {
		return doPostForm( model, result );
	}

	@RequestMapping( method = RequestMethod.POST, value = "/save/" )
	public ModelAndView saveJob( final @Valid @ModelAttribute( JOB_MODEL_NAME ) ArchivingJobJobModel model, final BindingResult result ) {
		return doSaveJob( model, result );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{savedJobId}/edit/" )
	public ModelAndView editEntry( final @PathVariable( "savedJobId" ) int savedJobId, final @ModelAttribute( JOB_MODEL_NAME ) ArchivingJobJobModel model, final HttpServletRequest request ) {
		return processEditing( savedJobId, model, request );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{savedJobId}/delete/" )
	public ModelAndView deleteEntry( final @PathVariable( "savedJobId" ) int savedJobId, final @ModelAttribute( JOB_MODEL_NAME ) ArchivingJobJobModel model ) {
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
		final ArchivingJobJobModel aModel = ( ArchivingJobJobModel ) model;

		final int archivePreviewsOlderThen = configurationService.getInt( ConfigurationKey.ARCHIVING_PREVIEWS );
		final int archiveAppraisalOlderThen = configurationService.getInt( ConfigurationKey.ARCHIVING_VOTES );
		final int photosOlderThen = configurationService.getInt( ConfigurationKey.ARCHIVING_PHOTOS );

		aModel.setArchivePreviewsOlderThen( archivePreviewsOlderThen );
		aModel.setArchiveAppraisalOlderThen( archiveAppraisalOlderThen );
		aModel.setArchivePhotosOlderThen( photosOlderThen );

		aModel.setPreviewsArchivingEnabled( archivePreviewsOlderThen > 0 );
		aModel.setAppraisalArchivingEnabled( archiveAppraisalOlderThen > 0 );
		aModel.setPhotosArchivingEnabled( photosOlderThen > 0 );
	}

	@Override
	protected void initJobFromModel( final AbstractAdminJobModel model ) {
		final ArchivingJob job = ( ArchivingJob ) model.getJob();
		final ArchivingJobJobModel aModel = ( ArchivingJobJobModel ) model;

		job.setArchivePreviewsOlderThen( aModel.getArchivePreviewsOlderThen() );
		job.setArchiveAppraisalOlderThen( aModel.getArchiveAppraisalOlderThen() );
		job.setArchivePhotosOlderThen( aModel.getArchivePreviewsOlderThen() );

		job.setPreviewsArchivingEnabled( aModel.isPreviewsArchivingEnabled() );
		job.setAppraisalArchivingEnabled( aModel.isAppraisalArchivingEnabled() );
		job.setPhotosArchivingEnabled( aModel.isPhotosArchivingEnabled() );
	}

	@Override
	protected void initModelFromSavedJob( final AbstractAdminJobModel model, int savedJobId ) {
		final ArchivingJobJobModel aModel = ( ArchivingJobJobModel ) model;

		final Map<SavedJobParameterKey,CommonProperty> savedJobParametersMap = savedJobService.getSavedJobParametersMap( savedJobId );
		aModel.setPreviewsArchivingEnabled( savedJobParametersMap.get( SavedJobParameterKey.PREVIEWS_ARCHIVING_ENABLED ).getValueBoolean() );
		aModel.setAppraisalArchivingEnabled( savedJobParametersMap.get( SavedJobParameterKey.APPRAISAL_ARCHIVING_ENABLED ).getValueBoolean() );
		aModel.setPhotosArchivingEnabled( savedJobParametersMap.get( SavedJobParameterKey.PHOTOS_ARCHIVING_ENABLED ).getValueBoolean() );

		final int archivePreviewsOlderThen = configurationService.getInt( ConfigurationKey.ARCHIVING_PREVIEWS );
		final int archiveAppraisalOlderThen = configurationService.getInt( ConfigurationKey.ARCHIVING_VOTES );
		final int photosOlderThen = configurationService.getInt( ConfigurationKey.ARCHIVING_PHOTOS );

		aModel.setArchivePreviewsOlderThen( archivePreviewsOlderThen );
		aModel.setArchiveAppraisalOlderThen( archiveAppraisalOlderThen );
		aModel.setArchivePhotosOlderThen( photosOlderThen );
	}

	@Override
	protected String getStartViewName() {
		return START_VIEW;
	}
}
