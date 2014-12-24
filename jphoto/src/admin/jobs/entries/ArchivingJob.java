package admin.jobs.entries;

import admin.jobs.JobRuntimeEnvironment;
import admin.jobs.enums.SavedJobType;
import admin.services.jobs.JobExecutionHistoryEntry;
import admin.services.jobs.JobExecutionHistoryService;
import core.enums.SavedJobParameterKey;
import core.general.base.CommonProperty;
import core.general.configuration.ConfigurationKey;
import core.general.photo.Photo;
import core.log.LogHelper;
import core.services.archiving.ArchivingService;
import core.services.photo.PhotoService;
import core.services.translator.message.TranslatableMessage;
import core.services.utils.DateUtilsService;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

public class ArchivingJob extends AbstractJob {

	private static final String YES = "Yes";
	private static final String NO = "No";
	private static final int OPERATIONS_COUNT = 3;

	private boolean previewsArchivingEnabled;
	private int archivePreviewsOlderThen;

	private boolean appraisalArchivingEnabled;
	private int archiveAppraisalOlderThen;

	private boolean photosArchivingEnabled;
	private int archivePhotosOlderThen;

	public ArchivingJob( final JobRuntimeEnvironment jobEnvironment ) {
		super( new LogHelper( ArchivingJob.class ), jobEnvironment );
	}

	@Override
	protected void runJob() throws Throwable {

		if ( previewsArchivingEnabled ) {
			archivePhotoPreviews();
		}

		increment();

		if ( appraisalArchivingEnabled ) {
			archivePhotoAppraisal();
		}

		increment();

		if ( photosArchivingEnabled ) {
			archivePhotos();
		}

		increment();
	}

	private void archivePhotoPreviews() {
		services.getArchivingService().archivePhotosPreviewsOlderThen( archivePreviewsOlderThen );
	}

	private void archivePhotoAppraisal() {
		services.getArchivingService().archivePhotosAppraisalsOlderThen( archiveAppraisalOlderThen );
	}

	private void archivePhotos() {

		final DateUtilsService dateUtilsService = services.getDateUtilsService();
		final ArchivingService archivingService = services.getArchivingService();
		final PhotoService photoService = services.getPhotoService();
		final JobExecutionHistoryService jobExecutionHistoryService = services.getJobExecutionHistoryService();

		final Date time = archivingService.getArchiveStartDate( archivePhotosOlderThen );

		log.debug( String.format( "About to archiving photos uploaded earlie then %s", dateUtilsService.formatDateTime( time ) ) );

		final List<Integer> photoIdsToArchive = photoService.getPhotosIdsUploadedEarlieThen( time );

		final JobExecutionHistoryEntry historyEntry = jobExecutionHistoryService.load( jobId );
		jobExecutionHistoryService.updateTotalJobSteps( historyEntry.getId(), photoIdsToArchive.size() + 2 );

		for ( final int photoId : photoIdsToArchive ) {

			final Photo photo = photoService.load( photoId );

			archivingService.archivePhoto( photo );

			increment();

			if ( hasJobFinishedWithAnyResult() ) {
				break;
			}

			log.debug( String.format( "Photo %s has been archived", photo ) );
		}
	}

	@Override
	public Map<SavedJobParameterKey, CommonProperty> getParametersMap() {

		final Map<SavedJobParameterKey, CommonProperty> parametersMap = newHashMap();

		parametersMap.put( SavedJobParameterKey.PREVIEWS_ARCHIVING_ENABLED, new CommonProperty( SavedJobParameterKey.PREVIEWS_ARCHIVING_ENABLED.getId(), previewsArchivingEnabled ) );
		parametersMap.put( SavedJobParameterKey.APPRAISAL_ARCHIVING_ENABLED, new CommonProperty( SavedJobParameterKey.APPRAISAL_ARCHIVING_ENABLED.getId(), appraisalArchivingEnabled ) );
		parametersMap.put( SavedJobParameterKey.PHOTOS_ARCHIVING_ENABLED, new CommonProperty( SavedJobParameterKey.PHOTOS_ARCHIVING_ENABLED.getId(), photosArchivingEnabled ) );

		return parametersMap;
	}

	@Override
	public void initJobParameters( Map<SavedJobParameterKey, CommonProperty> jobParameters ) {

		totalJopOperations = OPERATIONS_COUNT;

		previewsArchivingEnabled = jobParameters.get( SavedJobParameterKey.PREVIEWS_ARCHIVING_ENABLED ).getValueBoolean();
		archivePreviewsOlderThen = services.getConfigurationService().getInt( ConfigurationKey.ARCHIVING_PREVIEWS );

		appraisalArchivingEnabled = jobParameters.get( SavedJobParameterKey.APPRAISAL_ARCHIVING_ENABLED ).getValueBoolean();
		archiveAppraisalOlderThen = services.getConfigurationService().getInt( ConfigurationKey.ARCHIVING_VOTES );

		photosArchivingEnabled = jobParameters.get( SavedJobParameterKey.PHOTOS_ARCHIVING_ENABLED ).getValueBoolean();
		archivePhotosOlderThen = services.getConfigurationService().getInt( ConfigurationKey.ARCHIVING_PHOTOS );
	}

	@Override
	public String getJobParametersDescription() {

		final TranslatableMessage translatableMessage = new TranslatableMessage( services );

		translatableMessage.addTranslatableMessageParameter( new TranslatableMessage( "Delete information about photos previews older then $1 days", services )
			.addIntegerParameter( archivePreviewsOlderThen )
			.string( ": " )
			.string( previewsArchivingEnabled ? YES : NO )
		).lineBreakHtml();

		translatableMessage.addTranslatableMessageParameter( new TranslatableMessage( "Archive information about photos appraisal older then $1 days", services )
			.addIntegerParameter( archiveAppraisalOlderThen )
			.string( ": " )
			.string( appraisalArchivingEnabled ? YES : NO )
		).lineBreakHtml();

		translatableMessage.addTranslatableMessageParameter( new TranslatableMessage( "Archive photos uploaded earlie then $1 days", services )
			.addIntegerParameter( archivePhotosOlderThen )
			.string( ": " )
			.string( photosArchivingEnabled ? YES : NO )
		);

		return translatableMessage.build( getLanguage() );
	}

	@Override
	public SavedJobType getJobType() {
		return SavedJobType.ARCHIVING;
	}

	public void setArchivePreviewsOlderThen( int archivePreviewsOlderThen ) {
		this.archivePreviewsOlderThen = archivePreviewsOlderThen;
	}

	public int getArchivePreviewsOlderThen() {
		return archivePreviewsOlderThen;
	}

	public void setArchiveAppraisalOlderThen( int archiveAppraisalOlderThen ) {
		this.archiveAppraisalOlderThen = archiveAppraisalOlderThen;
	}

	public int getArchiveAppraisalOlderThen() {
		return archiveAppraisalOlderThen;
	}

	public void setArchivePhotosOlderThen( int archivePhotosOlderThen ) {
		this.archivePhotosOlderThen = archivePhotosOlderThen;
	}

	public int getArchivePhotosOlderThen() {
		return archivePhotosOlderThen;
	}

	public void setPreviewsArchivingEnabled( boolean previewsArchivingEnabled ) {
		this.previewsArchivingEnabled = previewsArchivingEnabled;
	}

	public boolean isPreviewsArchivingEnabled() {
		return previewsArchivingEnabled;
	}

	public void setAppraisalArchivingEnabled( boolean appraisalArchivingEnabled ) {
		this.appraisalArchivingEnabled = appraisalArchivingEnabled;
	}

	public boolean isAppraisalArchivingEnabled() {
		return appraisalArchivingEnabled;
	}

	public void setPhotosArchivingEnabled( boolean photosArchivingEnabled ) {
		this.photosArchivingEnabled = photosArchivingEnabled;
	}

	public boolean isPhotosArchivingEnabled() {
		return photosArchivingEnabled;
	}
}
