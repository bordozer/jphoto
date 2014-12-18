package admin.jobs.entries;

import admin.controllers.jobs.edit.NoParametersAbstractJob;
import admin.jobs.JobRuntimeEnvironment;
import admin.jobs.enums.SavedJobType;
import core.log.LogHelper;

public class ArchivingJob extends NoParametersAbstractJob {

	private int archivePreviewsOlderThen;
	private int archiveAppraisalOlderThen;
	private int archivePhotosOlderThen;
	private boolean previewsArchivingEnabled;
	private boolean appraisalArchivingEnabled;
	private boolean photosArchivingEnabled;

	public ArchivingJob( final JobRuntimeEnvironment jobEnvironment ) {
		super( new LogHelper( ArchivingJob.class ), jobEnvironment );
	}

	@Override
	protected void runJob() throws Throwable {

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
