package admin.controllers.jobs.edit.photosImport.strategies.web.naturelight;

import admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import admin.controllers.jobs.edit.photosImport.strategies.web.AbstractRemotePhotoSitePageContentDataExtractor;
import admin.controllers.jobs.edit.photosImport.strategies.web.AbstractRemotePhotoSiteUrlHelper;
import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategory;
import core.log.LogHelper;

public class NaturelightUrlHelper extends AbstractRemotePhotoSiteUrlHelper {

	private final AbstractRemotePhotoSitePageContentDataExtractor photosightContentDataExtractor = new NaturelightContentDataExtractor();

	public NaturelightUrlHelper() {
		super( new LogHelper( NaturelightUrlHelper.class ) );
	}

	@Override
	public PhotosImportSource getPhotosImportSource() {
		return PhotosImportSource.NATURELIGHT;
	}

	@Override
	public String getUserCardUrl( final String remotePhotoSiteUserId, final int pageNumber ) {
		if ( pageNumber > 1 ) {
			return String.format( "http://%s/author/%s/page%d.html", getRemotePhotoSiteHost(), remotePhotoSiteUserId, pageNumber );
		}

		return String.format( "http://www.%s/author/%s.html", getRemotePhotoSiteHost(), remotePhotoSiteUserId );
	}

	@Override
	public String getPhotoCardUrl( final String remotePhotoSiteUserId, final int remotePhotoSitePhotoId ) {
		return String.format( "http://%s/show_photo/%s.html", getRemotePhotoSiteHost(), remotePhotoSitePhotoId );
	}

	@Override
	public String getPhotoCategoryUrl( final RemotePhotoSiteCategory remotePhotoSiteCategory ) {
		return String.format( "http://%s/show_group/%d.html", getRemotePhotoSiteHost(), remotePhotoSiteCategory.getId() );
	}

	@Override
	protected AbstractRemotePhotoSitePageContentDataExtractor getRemotePhotoSiteContentDataExtractor() {
		return photosightContentDataExtractor;
	}
}
