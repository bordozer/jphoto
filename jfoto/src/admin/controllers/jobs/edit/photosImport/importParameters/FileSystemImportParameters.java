package admin.controllers.jobs.edit.photosImport.importParameters;

import admin.jobs.general.JobDateRange;

public class FileSystemImportParameters implements ImportParameters {

	private final String pictureDir;
	private final int photoQtyLimit;
	private final boolean generatePreview;
	private final int assignAllGeneratedPhotosToUserId;

	private final JobDateRange jobDateRange;

	public FileSystemImportParameters( final String pictureDir, final int photoQtyLimit, final boolean generatePreview, final int assignAllGeneratedPhotosToUserId, final JobDateRange jobDateRange ) {
		this.pictureDir = pictureDir;
		this.photoQtyLimit = photoQtyLimit;
		this.generatePreview = generatePreview;
		this.assignAllGeneratedPhotosToUserId = assignAllGeneratedPhotosToUserId;
		this.jobDateRange = jobDateRange;
	}

	public String getPictureDir() {
		return pictureDir;
	}

	public int getPhotoQtyLimit() {
		return photoQtyLimit;
	}

	public boolean isGeneratePreview() {
		return generatePreview;
	}

	public int getAssignAllGeneratedPhotosToUserId() {
		return assignAllGeneratedPhotosToUserId;
	}

	public JobDateRange getJobDateRange() {
		return jobDateRange;
	}
}
