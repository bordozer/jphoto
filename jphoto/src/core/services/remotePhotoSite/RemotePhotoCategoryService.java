package core.services.remotePhotoSite;

import admin.controllers.jobs.edit.photosImport.LocalCategory;
import admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategory;
import core.general.genre.Genre;

public interface RemotePhotoCategoryService {

	LocalCategory getMappedGenreDiscEntry( final RemotePhotoSiteCategory remotePhotoSiteCategory );

	Genre getMappedGenreOrOther( final RemotePhotoSiteCategory remotePhotoSiteCategory );

	Genre getMappedGenreOrOther( final PhotosImportSource photosImportSource, final String genreName );

	Genre getMappedGenreOrNull( final RemotePhotoSiteCategory remotePhotoSiteCategory );

	Genre getMappedGenreOrNull( final PhotosImportSource photosImportSource, final String genreName );
}
