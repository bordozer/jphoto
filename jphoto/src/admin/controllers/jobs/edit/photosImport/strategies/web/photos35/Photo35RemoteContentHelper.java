package admin.controllers.jobs.edit.photosImport.strategies.web.photos35;

import admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import admin.controllers.jobs.edit.photosImport.strategies.web.AbstractRemoteContentHelper;
import admin.controllers.jobs.edit.photosImport.strategies.web.AbstractRemotePhotoSitePageContentDataExtractor;
import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategory;
import core.log.LogHelper;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;

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
		return String.format( "http://www.%s.%s/", remotePhotoSiteUserId, getRemotePhotoSiteHost() );
	}

	@Override
	public String getPhotoCardUrl( final String remotePhotoSiteUserId, final int remotePhotoSitePhotoId ) {
		return String.format( "http://%s.%s/photo_%d/", remotePhotoSiteUserId, getRemotePhotoSiteHost(), remotePhotoSitePhotoId );
	}

	@Override
	public String getPhotoCategoryUrl( final RemotePhotoSiteCategory remotePhotoSiteCategory ) {
		return String.format( "http://www.%s/rating/photo_day/cat%d/", getRemotePhotoSiteHost(), remotePhotoSiteCategory.getId() );
	}

	@Override
	protected AbstractRemotePhotoSitePageContentDataExtractor getRemotePhotoSiteContentDataExtractor() {
		return photosightContentDataExtractor;
	}

	@Override
	protected void addNecessaryCookies( final DefaultHttpClient httpClient, final String remotePhotoSiteUserId ) {
		final CookieStore cookieStore = new BasicCookieStore();

		cookieStore.addCookie( getRemoteUserCardCookie( "nude", "true" ) );
		cookieStore.addCookie( getRemoteUserCardCookie( "user_lang", "en" ) );

		httpClient.setCookieStore( cookieStore );
	}
}
