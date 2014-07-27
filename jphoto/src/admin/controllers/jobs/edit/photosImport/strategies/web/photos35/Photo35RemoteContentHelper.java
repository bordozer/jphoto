package admin.controllers.jobs.edit.photosImport.strategies.web.photos35;

import admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import admin.controllers.jobs.edit.photosImport.strategies.web.AbstractRemoteContentHelper;
import admin.controllers.jobs.edit.photosImport.strategies.web.AbstractRemotePhotoSitePageContentDataExtractor;
import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategory;
import core.log.LogHelper;

public class Photo35RemoteContentHelper extends AbstractRemoteContentHelper {

	private final AbstractRemotePhotoSitePageContentDataExtractor photosightContentDataExtractor = new Photo35ContentDataExtractor();

	public Photo35RemoteContentHelper() {
		super( new LogHelper( Photo35RemoteContentHelper.class ) );
	}

	@Override
	public PhotosImportSource getPhotosImportSource() {
		return PhotosImportSource.PHOTO35;
	}

	@Override
	public String getUserCardUrl( final String remotePhotoSiteUserId, final int pageNumber ) {
		return String.format( "http://www.%s/%s/%s/?pager=%d", getRemotePhotoSiteHost(), "users", remotePhotoSiteUserId, pageNumber );
	}

	@Override
	public String getPhotoCardUrl( final int remotePhotoSitePhotoId ) {
		return String.format( "http://www.%s/%s/%d/", getRemotePhotoSiteHost(), "photos", remotePhotoSitePhotoId );
	}

	@Override
	public String getPhotoCategoryUrl( final RemotePhotoSiteCategory remotePhotoSiteCategory ) {
		return String.format( "http://www.%s/%s/category/%d/", getRemotePhotoSiteHost(), "photos", remotePhotoSiteCategory.getId() );
	}

	@Override
	protected AbstractRemotePhotoSitePageContentDataExtractor getRemotePhotoSiteContentDataExtractor() {
		return photosightContentDataExtractor;
	}
}
