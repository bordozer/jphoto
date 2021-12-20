package com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.importParameters;

import com.bordozer.jphoto.admin.jobs.general.JobDateRange;
import com.bordozer.jphoto.core.services.translator.Language;

public class FileSystemImportParameters extends AbstractImportParameters {

    private final String pictureDir;
    private final int photoQtyLimit;
    private final boolean deletePictureAfterImport;
    private final int assignAllGeneratedPhotosToUserId;

    private final JobDateRange jobDateRange;

    public FileSystemImportParameters(final String pictureDir, final int photoQtyLimit, final boolean deletePictureAfterImport, final int assignAllGeneratedPhotosToUserId, final JobDateRange jobDateRange, final Language language) {
        super(language);
        this.pictureDir = pictureDir;
        this.photoQtyLimit = photoQtyLimit;
        this.deletePictureAfterImport = deletePictureAfterImport;
        this.assignAllGeneratedPhotosToUserId = assignAllGeneratedPhotosToUserId;
        this.jobDateRange = jobDateRange;
    }

    public String getPictureDir() {
        return pictureDir;
    }

    public int getPhotoQtyLimit() {
        return photoQtyLimit;
    }

    public boolean isDeletePictureAfterImport() {
        return deletePictureAfterImport;
    }

    public int getAssignAllGeneratedPhotosToUserId() {
        return assignAllGeneratedPhotosToUserId;
    }

    public JobDateRange getJobDateRange() {
        return jobDateRange;
    }
}
