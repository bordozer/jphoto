package com.bordozer.jphoto.core.services.remotePhotoSite;

import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.LocalCategory;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategory;
import com.bordozer.jphoto.core.general.genre.Genre;

public interface RemotePhotoCategoryService {

    LocalCategory getMappedGenreDiscEntry(final RemotePhotoSiteCategory remotePhotoSiteCategory);

    Genre getMappedGenreOrOther(final RemotePhotoSiteCategory remotePhotoSiteCategory);

    Genre getMappedGenreOrOther(final PhotosImportSource photosImportSource, final String genreName);

    Genre getMappedGenreOrNull(final RemotePhotoSiteCategory remotePhotoSiteCategory);

    Genre getMappedGenreOrNull(final PhotosImportSource photosImportSource, final String genreName);
}
