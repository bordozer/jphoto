package com.bordozer.jphoto.admin.controllers.jobs.edit.archiving;

import com.bordozer.jphoto.admin.controllers.jobs.edit.AbstractAdminJobModel;

public class ArchivingJobJobModel extends AbstractAdminJobModel {

    private boolean previewsArchivingEnabled;
    private int archivePreviewsOlderThen;

    private boolean appraisalArchivingEnabled;
    private int archiveAppraisalOlderThen;

    private boolean photosArchivingEnabled;
    private int archivePhotosOlderThen;

    public boolean isPreviewsArchivingEnabled() {
        return previewsArchivingEnabled;
    }

    public void setPreviewsArchivingEnabled(boolean previewsArchivingEnabled) {
        this.previewsArchivingEnabled = previewsArchivingEnabled;
    }

    public int getArchivePreviewsOlderThen() {
        return archivePreviewsOlderThen;
    }

    public void setArchivePreviewsOlderThen(int archivePreviewsOlderThen) {
        this.archivePreviewsOlderThen = archivePreviewsOlderThen;
    }

    public boolean isAppraisalArchivingEnabled() {
        return appraisalArchivingEnabled;
    }

    public void setAppraisalArchivingEnabled(boolean appraisalArchivingEnabled) {
        this.appraisalArchivingEnabled = appraisalArchivingEnabled;
    }

    public int getArchiveAppraisalOlderThen() {
        return archiveAppraisalOlderThen;
    }

    public void setArchiveAppraisalOlderThen(int archiveAppraisalOlderThen) {
        this.archiveAppraisalOlderThen = archiveAppraisalOlderThen;
    }

    public boolean isPhotosArchivingEnabled() {
        return photosArchivingEnabled;
    }

    public void setPhotosArchivingEnabled(boolean photosArchivingEnabled) {
        this.photosArchivingEnabled = photosArchivingEnabled;
    }

    public int getArchivePhotosOlderThen() {
        return archivePhotosOlderThen;
    }

    public void setArchivePhotosOlderThen(int archivePhotosOlderThen) {
        this.archivePhotosOlderThen = archivePhotosOlderThen;
    }
}
