package com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web;

import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.naturelight.NaturelightContentDataExtractor;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.photos35.Photo35ContentDataExtractor;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.photosight.PhotosightContentDataExtractor;
import com.bordozer.jphoto.core.services.system.Services;

import java.util.Date;
import java.util.List;

public abstract class AbstractRemotePhotoSitePageContentDataExtractor {

    public static final String NO_PHOTO_NAME = "-no name-";

    public abstract List<RemotePhotoSiteImage> extractImageUrl(final String remotePhotoSiteUserId, final int remotePhotoSitePhotoId, final String photoPageContent);

    public abstract String getPhotoIdRegex(final String remotePhotoSiteUserId);

    public abstract String extractRemotePhotoSiteUserName(final String userPageContent);

    public abstract int extractRemotePhotoSiteUserPhotosCount(final String remotePhotoSiteUserId);

    public abstract int getRemoteUserPagesCount(final String userCardContent, final String remotePhotoSiteUserId);

    public abstract int extractPhotoCategoryId(final String photoPageContent);

    public abstract String extractPhotoName(final String photoPageContent);

    public abstract List<String> extractComments(final String photoPageContent);

    public abstract Date extractPhotoUploadTime(final String photoPageContent, final Services services);

    protected abstract String getHost();

    public static AbstractRemotePhotoSitePageContentDataExtractor getInstance(final PhotosImportSource photosImportSource) {

        switch (photosImportSource) {
            case PHOTOSIGHT:
                return new PhotosightContentDataExtractor();
            case PHOTO35:
                return new Photo35ContentDataExtractor();
            case NATURELIGHT:
                return new NaturelightContentDataExtractor();
        }

        throw new IllegalArgumentException(String.format("PhotosImportSource '%s' does not have matched page data extractor", photosImportSource));
    }
}
