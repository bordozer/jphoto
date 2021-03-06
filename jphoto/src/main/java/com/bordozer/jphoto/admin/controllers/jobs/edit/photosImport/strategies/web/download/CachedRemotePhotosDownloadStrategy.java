package com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.download;

import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.ImageToImport;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhoto;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoData;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCacheXmlUtils;
import com.bordozer.jphoto.admin.jobs.entries.AbstractJob;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class CachedRemotePhotosDownloadStrategy extends RemotePhotosDownloadStrategy {

    private PhotosImportSource importSource;

    public CachedRemotePhotosDownloadStrategy(final AbstractJob job, final PhotosImportSource importSource, final List<RemotePhotoData> remotePhotosData, final RemotePhotoSiteCacheXmlUtils remotePhotoSiteCacheXmlUtils) {
        super(job, remotePhotoSiteCacheXmlUtils);
        this.remotePhotosData = filterRemotePhotosData(remotePhotosData, true);
        this.importSource = importSource;
    }

    @Override
    public List<RemotePhoto> download() throws IOException {

        final List<RemotePhoto> result = newArrayList();

        for (final RemotePhotoData remotePhotoData : remotePhotosData) {
            final File imageFile = remotePhotoSiteCacheXmlUtils.getRemotePhotoCacheFile(remotePhotoData);

            if (!imageFile.exists()) {
                // The image presents in XML-filen but there is no image file stored locally
                // TODO: maybe it's logically to try to download the image again
                continue;
            }

            final ImageToImport imageToImport = new ImageToImport(importSource, remotePhotoSiteCacheXmlUtils.getPhotoImageLocationType(), remotePhotoData.getRemotePhotoSiteCategory().getKey(), imageFile, remotePhotoData.getImageUrl());
            result.add(new RemotePhoto(remotePhotoData, imageToImport));
        }

        return result;
    }
}
