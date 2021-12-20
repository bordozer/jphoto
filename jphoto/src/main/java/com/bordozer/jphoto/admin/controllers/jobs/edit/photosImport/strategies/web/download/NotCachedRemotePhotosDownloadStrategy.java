package com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.download;

import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.ImageToImport;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.AbstractRemotePhotoSiteUrlHelper;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhoto;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoData;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCacheXmlUtils;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteImage;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.RemoteUser;
import com.bordozer.jphoto.admin.jobs.entries.AbstractJob;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.message.TranslatableMessage;

import java.io.IOException;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class NotCachedRemotePhotosDownloadStrategy extends RemotePhotosDownloadStrategy {

    private RemoteUser remoteUser;
    private AbstractRemotePhotoSiteUrlHelper remoteContentHelper;
    private Services services;

    public NotCachedRemotePhotosDownloadStrategy(final AbstractJob job, final RemoteUser remoteUser, final List<RemotePhotoData> remotePhotosData, final RemotePhotoSiteCacheXmlUtils remotePhotoSiteCacheXmlUtils, final AbstractRemotePhotoSiteUrlHelper remoteContentHelper, final Services services) {
        super(job, remotePhotoSiteCacheXmlUtils);
        this.remoteUser = remoteUser;
        this.remoteContentHelper = remoteContentHelper;
        this.services = services;
        this.remotePhotosData = filterRemotePhotosData(remotePhotosData, false);
    }

    @Override
    public List<RemotePhoto> download() throws IOException {
        remotePhotoSiteCacheXmlUtils.prepareUserGenreFolders(remoteUser, remotePhotosData);

        return downloadAndCacheRemoteImages(remotePhotosData);
    }

    private List<RemotePhoto> downloadAndCacheRemoteImages(final List<RemotePhotoData> remotePhotosData) throws IOException {

        final int toAddCount = remotePhotosData.size();

        if (toAddCount > 0) {
            job.addJobRuntimeLogMessage(new TranslatableMessage("$1 images about to be downloaded", services).addIntegerParameter(toAddCount));
        }

        final List<RemotePhoto> result = newArrayList();
        int counter = 1;
        for (final RemotePhotoData remotePhotoData : remotePhotosData) {

            final RemotePhotoSiteImage remotePhotoSiteImage = remotePhotoData.getRemotePhotoSiteImage();
            job.addJobRuntimeLogMessage(new TranslatableMessage("$1 / $2: Getting image '$3' content", services)
                    .addIntegerParameter(counter)
                    .addIntegerParameter(toAddCount)
                    .link(remotePhotoData.getRemotePhotoSiteImage().getImageUrl())
            );

            final String imageContent = remoteContentHelper.getImageContentFromUrl(remotePhotoSiteImage.getImageUrl());
            if (imageContent == null) {
                remotePhotoData.setHasError(true);
                job.addJobRuntimeLogMessage(new TranslatableMessage("Can not get remote photo site image content: '$1'", services).string(remotePhotoData.getRemotePhotoSiteImage().getImageUrl()));
                continue;
            }

            final ImageToImport imageToImport = remotePhotoSiteCacheXmlUtils.placeRemoteImageToCache(remotePhotoData, imageContent);

            result.add(new RemotePhoto(remotePhotoData, imageToImport));

            counter++;
        }

        return result;
    }
}
