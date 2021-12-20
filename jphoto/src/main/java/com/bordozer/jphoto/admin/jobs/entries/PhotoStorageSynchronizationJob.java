package com.bordozer.jphoto.admin.jobs.entries;

import com.bordozer.jphoto.admin.controllers.jobs.edit.NoParametersAbstractJob;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.importParameters.AbstractImportParameters;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.importParameters.RemoteSitePhotosImportParameters;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.AbstractPhotoImportStrategy;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCacheXmlUtils;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategory;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteImportStrategy;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.photosight.PhotosightCategory;
import com.bordozer.jphoto.admin.jobs.JobRuntimeEnvironment;
import com.bordozer.jphoto.admin.jobs.enums.SavedJobType;
import com.bordozer.jphoto.admin.services.jobs.JobExecutionHistoryEntry;
import com.bordozer.jphoto.core.enums.UserGender;
import com.bordozer.jphoto.core.general.photo.PhotoImageLocationType;
import com.bordozer.jphoto.core.general.user.UserMembershipType;
import com.bordozer.jphoto.core.log.LogHelper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class PhotoStorageSynchronizationJob extends NoParametersAbstractJob {

    public PhotoStorageSynchronizationJob(final JobRuntimeEnvironment jobEnvironment) {
        super(new LogHelper(), jobEnvironment);
    }

    @Override
    protected void runJob() throws Throwable {
        final List<String> usersIds = getUsersIds();

        final List<RemotePhotoSiteCategory> photosightCategories = Arrays.asList(PhotosightCategory.values());

        final AbstractImportParameters importParameters = new RemoteSitePhotosImportParameters(getPhotosImportSource()
                , usersIds
                , UserGender.MALE
                , UserMembershipType.AUTHOR
                , true
                , 0
                , 0
                , getLanguage()
                , true
                , photosightCategories
                , PhotoImageLocationType.WEB); // TODO: send this as parameter selected on UI

        final AbstractPhotoImportStrategy importStrategy = new RemotePhotoSiteImportStrategy(this, importParameters, services);

        updateTotalOperations(importStrategy);

        importStrategy.doImport();
    }

    @Override
    public SavedJobType getJobType() {
        return SavedJobType.PHOTO_STORAGE_SYNCHRONIZATION;
    }

    // TODO: the same is in PhotosImportJob
    private void updateTotalOperations(final AbstractPhotoImportStrategy importStrategy) throws IOException {

        final boolean operationCountIsDefinedByUser = totalJopOperations > 0 && totalJopOperations != AbstractJob.OPERATION_COUNT_UNKNOWN;
        if (!operationCountIsDefinedByUser) {
            totalJopOperations = importStrategy.calculateTotalPagesToProcess();
        }

        generationMonitor.setTotal(totalJopOperations);

        final JobExecutionHistoryEntry historyEntry = services.getJobExecutionHistoryService().load(jobId);
        services.getJobExecutionHistoryService().updateTotalJobSteps(historyEntry.getId(), totalJopOperations);

        log.debug(String.format("Update operation count: %d", totalJopOperations));
    }

    private List<String> getUsersIds() {
        final List<String> usersIds = newArrayList();
        final File storage = new RemotePhotoSiteCacheXmlUtils(
                getPhotosImportSource(),
                new File("/home/blu/temp/jphoto_remote_sites_cache/"),
                services.getRemotePhotoCategoryService(),
                PhotoImageLocationType.WEB
        ).getPhotoStorage();

        final File[] userDirList = storage.listFiles(services.getPredicateUtilsService().getDirFilter());
        for (final File file : userDirList) {
            usersIds.add(file.getName());
        }
        return usersIds;
    }

    private PhotosImportSource getPhotosImportSource() {
        return PhotosImportSource.PHOTOSIGHT;
    }
}
