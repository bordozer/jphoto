package core.services.remotePhotoSite;

import admin.controllers.jobs.edit.photosImport.GenreDiscEntry;
import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategory;
import core.general.genre.Genre;

public interface RemotePhotoCategoryService {

	Genre getGenre( final RemotePhotoSiteCategory remotePhotoSiteCategory );

	GenreDiscEntry getGenreDiscEntryOrOther( RemotePhotoSiteCategory remotePhotoSiteCategory );
}
