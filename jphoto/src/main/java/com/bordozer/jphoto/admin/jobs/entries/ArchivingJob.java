package com.bordozer.jphoto.admin.jobs.entries;

import com.bordozer.jphoto.admin.jobs.JobRuntimeEnvironment;
import com.bordozer.jphoto.admin.jobs.enums.SavedJobType;
import com.bordozer.jphoto.admin.services.jobs.JobExecutionHistoryEntry;
import com.bordozer.jphoto.admin.services.jobs.JobExecutionHistoryService;
import com.bordozer.jphoto.core.enums.SavedJobParameterKey;
import com.bordozer.jphoto.core.general.base.CommonProperty;
import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.log.LogHelper;
import com.bordozer.jphoto.core.services.archiving.ArchivingService;
import com.bordozer.jphoto.core.services.photo.PhotoService;
import com.bordozer.jphoto.core.services.translator.message.TranslatableMessage;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;

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

    public ArchivingJob(final JobRuntimeEnvironment jobEnvironment) {
        super(new LogHelper(), jobEnvironment);
    }

    @Override
    protected void runJob() throws Throwable {

        if (previewsArchivingEnabled) {
            archivePhotoPreviews();
        }

        increment();

        if (appraisalArchivingEnabled) {
            archivePhotoAppraisal();
        }

        increment();

        if (photosArchivingEnabled) {
            archivePhotos();
        }

        increment();
    }

    private void archivePhotoPreviews() {
        services.getArchivingService().archivePhotosPreviewsOlderThen(archivePreviewsOlderThen);
    }

    private void archivePhotoAppraisal() {
        services.getArchivingService().archivePhotosAppraisalsOlderThen(archiveAppraisalOlderThen);
    }

    private void archivePhotos() {

        final DateUtilsService dateUtilsService = services.getDateUtilsService();
        final ArchivingService archivingService = services.getArchivingService();
        final JobExecutionHistoryService jobExecutionHistoryService = services.getJobExecutionHistoryService();

        final Date time = archivingService.getArchiveStartDate(archivePhotosOlderThen);

        log.debug(String.format("About to archiving photos uploaded earlie then %s", dateUtilsService.formatDateTime(time)));

        final List<Integer> photoIdsToArchive = archivingService.getNotArchivedPhotosIdsUploadedAtOrEarlieThen(time);

        final JobExecutionHistoryEntry historyEntry = jobExecutionHistoryService.load(jobId);
        jobExecutionHistoryService.updateTotalJobSteps(historyEntry.getId(), photoIdsToArchive.size() + 2);

        logPhotosToArchiveCount(photoIdsToArchive);

        //		archivePhoto( 351678 );

        for (final int photoId : photoIdsToArchive) {

            archivePhoto(photoId);

            increment();

            if (hasJobFinishedWithAnyResult()) {
                break;
            }
        }
    }

    private void archivePhoto(final int photoId) {

        final PhotoService photoService = services.getPhotoService();
        final ArchivingService archivingService = services.getArchivingService();

        final Photo photo = photoService.load(photoId);

        if (photo == null) {
            return;
        }

        archivingService.archivePhoto(photo);

        logPhotoIsArchived(photo);
    }

    @Override
    public Map<SavedJobParameterKey, CommonProperty> getParametersMap() {

        final Map<SavedJobParameterKey, CommonProperty> parametersMap = newHashMap();

        parametersMap.put(SavedJobParameterKey.PREVIEWS_ARCHIVING_ENABLED, new CommonProperty(SavedJobParameterKey.PREVIEWS_ARCHIVING_ENABLED.getId(), previewsArchivingEnabled));
        parametersMap.put(SavedJobParameterKey.APPRAISAL_ARCHIVING_ENABLED, new CommonProperty(SavedJobParameterKey.APPRAISAL_ARCHIVING_ENABLED.getId(), appraisalArchivingEnabled));
        parametersMap.put(SavedJobParameterKey.PHOTOS_ARCHIVING_ENABLED, new CommonProperty(SavedJobParameterKey.PHOTOS_ARCHIVING_ENABLED.getId(), photosArchivingEnabled));

        return parametersMap;
    }

    @Override
    public void initJobParameters(Map<SavedJobParameterKey, CommonProperty> jobParameters) {

        totalJopOperations = OPERATIONS_COUNT;

        previewsArchivingEnabled = jobParameters.get(SavedJobParameterKey.PREVIEWS_ARCHIVING_ENABLED).getValueBoolean();
        archivePreviewsOlderThen = services.getConfigurationService().getInt(ConfigurationKey.ARCHIVING_PREVIEWS);

        appraisalArchivingEnabled = jobParameters.get(SavedJobParameterKey.APPRAISAL_ARCHIVING_ENABLED).getValueBoolean();
        archiveAppraisalOlderThen = services.getConfigurationService().getInt(ConfigurationKey.ARCHIVING_VOTES);

        photosArchivingEnabled = jobParameters.get(SavedJobParameterKey.PHOTOS_ARCHIVING_ENABLED).getValueBoolean();
        archivePhotosOlderThen = services.getConfigurationService().getInt(ConfigurationKey.ARCHIVING_PHOTOS);
    }

    @Override
    public String getJobParametersDescription() {

        final TranslatableMessage translatableMessage = new TranslatableMessage(services);

        translatableMessage.addTranslatableMessageParameter(new TranslatableMessage("Delete information about photos previews older then $1 days", services)
                .addIntegerParameter(archivePreviewsOlderThen)
                .string(": ")
                .string(previewsArchivingEnabled ? YES : NO)
        ).lineBreakHtml();

        translatableMessage.addTranslatableMessageParameter(new TranslatableMessage("Archive information about photos appraisal older then $1 days", services)
                .addIntegerParameter(archiveAppraisalOlderThen)
                .string(": ")
                .string(appraisalArchivingEnabled ? YES : NO)
        ).lineBreakHtml();

        translatableMessage.addTranslatableMessageParameter(new TranslatableMessage("Archive photos uploaded earlie then $1 days", services)
                .addIntegerParameter(archivePhotosOlderThen)
                .string(": ")
                .string(photosArchivingEnabled ? YES : NO)
        );

        return translatableMessage.build(getLanguage());
    }

    @Override
    public SavedJobType getJobType() {
        return SavedJobType.ARCHIVING;
    }

    private void logPhotosToArchiveCount(final List<Integer> photoIdsToArchive) {
        final TranslatableMessage message = new TranslatableMessage("ArchivingJob: $1 photos are going to be archived", services)
                .addIntegerParameter(photoIdsToArchive.size());
        addJobRuntimeLogMessage(message);

        log.debug(String.format("%d photos are going to be archived", photoIdsToArchive.size()));
    }

    private void logPhotoIsArchived(final Photo photo) {
        final TranslatableMessage photoIsArchivedMessage = new TranslatableMessage("ArchivingJob: Photo $1 has been archived", services)
                .addPhotoCardLinkParameter(photo);
        addJobRuntimeLogMessage(photoIsArchivedMessage);

        log.debug(String.format("Photo %s has been archived", photo));
    }

    public void setArchivePreviewsOlderThen(int archivePreviewsOlderThen) {
        this.archivePreviewsOlderThen = archivePreviewsOlderThen;
    }

    public int getArchivePreviewsOlderThen() {
        return archivePreviewsOlderThen;
    }

    public void setArchiveAppraisalOlderThen(int archiveAppraisalOlderThen) {
        this.archiveAppraisalOlderThen = archiveAppraisalOlderThen;
    }

    public int getArchiveAppraisalOlderThen() {
        return archiveAppraisalOlderThen;
    }

    public void setArchivePhotosOlderThen(int archivePhotosOlderThen) {
        this.archivePhotosOlderThen = archivePhotosOlderThen;
    }

    public int getArchivePhotosOlderThen() {
        return archivePhotosOlderThen;
    }

    public void setPreviewsArchivingEnabled(boolean previewsArchivingEnabled) {
        this.previewsArchivingEnabled = previewsArchivingEnabled;
    }

    public boolean isPreviewsArchivingEnabled() {
        return previewsArchivingEnabled;
    }

    public void setAppraisalArchivingEnabled(boolean appraisalArchivingEnabled) {
        this.appraisalArchivingEnabled = appraisalArchivingEnabled;
    }

    public boolean isAppraisalArchivingEnabled() {
        return appraisalArchivingEnabled;
    }

    public void setPhotosArchivingEnabled(boolean photosArchivingEnabled) {
        this.photosArchivingEnabled = photosArchivingEnabled;
    }

    public boolean isPhotosArchivingEnabled() {
        return photosArchivingEnabled;
    }
}
