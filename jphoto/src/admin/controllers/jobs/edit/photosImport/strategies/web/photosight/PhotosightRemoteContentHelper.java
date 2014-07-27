package admin.controllers.jobs.edit.photosImport.strategies.web.photosight;

import admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import admin.controllers.jobs.edit.photosImport.strategies.web.*;
import core.log.LogHelper;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;

public class PhotosightRemoteContentHelper extends AbstractRemoteContentHelper {

	private final AbstractRemotePhotoSitePageContentDataExtractor photosightContentDataExtractor = new PhotosightContentDataExtractor();

	public PhotosightRemoteContentHelper() {
		super( new LogHelper( PhotosightRemoteContentHelper.class ) );
	}

	@Override
	public PhotosImportSource getPhotosImportSource() {
		return PhotosImportSource.PHOTOSIGHT;
	}

	@Override
	public String getUserCardUrl( final String remotePhotoSiteUserId, final int pageNumber ) {
		return String.format( "http://www.%s/users/%s/?pager=%d", getRemotePhotoSiteHost(),remotePhotoSiteUserId, pageNumber );
	}

	@Override
	public String getPhotoCardUrl( final String remotePhotoSiteUserId, final int remotePhotoSitePhotoId ) {
		return String.format( "http://www.%s/%s/%d/", getRemotePhotoSiteHost(), "photos", remotePhotoSitePhotoId );
	}

	@Override
	public String getPhotoCategoryUrl( final RemotePhotoSiteCategory remotePhotoSiteCategory ) {
		return String.format( "http://www.%s/photos/category/%d/", getRemotePhotoSiteHost(), remotePhotoSiteCategory.getId() );
	}

	@Override
	protected void addNecessaryCookies( final DefaultHttpClient httpClient, final String remotePhotoSiteUserId ) {
		final CookieStore cookieStore = new BasicCookieStore();

		final BasicClientCookie cookieIsDisabledNude = getRemoteUserCardCookie( String.format( "is_disabled_nude_profile_%s", remotePhotoSiteUserId ), "1" );
		cookieStore.addCookie( cookieIsDisabledNude );

		final BasicClientCookie cookieShowNude = getRemoteUserCardCookie( "show_nude", "1" );
		cookieStore.addCookie( cookieShowNude );

		httpClient.setCookieStore( cookieStore );
	}

	@Override
	protected AbstractRemotePhotoSitePageContentDataExtractor getRemotePhotoSiteContentDataExtractor() {
		return photosightContentDataExtractor;
	}

}
