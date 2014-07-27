package admin.controllers.jobs.edit.photosImport.strategies.web.photos35;

import admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import admin.controllers.jobs.edit.photosImport.strategies.web.AbstractRemoteContentHelper;
import admin.controllers.jobs.edit.photosImport.strategies.web.photosight.PhotosightCategory;
import admin.controllers.jobs.edit.photosImport.strategies.web.photosight.PhotosightContentDataExtractor;
import core.log.LogHelper;

public class Photo35RemoteContentHelper extends AbstractRemoteContentHelper {

	public Photo35RemoteContentHelper() {
		super( new LogHelper( Photo35RemoteContentHelper.class ) );
	}

	@Override
	public PhotosImportSource getPhotosImportSource() {
		return null;
	}

	@Override
	public String getUserCardUrl( final String remotePhotoSiteUserId, final int pageNumber ) {
		return null;
	}

	@Override
	public String getPhotoCardUrl( final int remotePhotoSitePhotoId ) {
		return null;
	}

	@Override
	public String getPhotoCategoryUrl( final PhotosightCategory photosightCategory ) {
		return null;
	}

	@Override
	protected PhotosightContentDataExtractor getPhotosightContentDataExtractor() {
		return null;
	}
}
