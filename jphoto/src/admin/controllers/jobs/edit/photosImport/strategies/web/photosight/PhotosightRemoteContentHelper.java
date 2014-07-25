package admin.controllers.jobs.edit.photosImport.strategies.web.photosight;

import admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import admin.controllers.jobs.edit.photosImport.strategies.web.*;
import core.log.LogHelper;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import utils.StringUtilities;

public class PhotosightRemoteContentHelper extends AbstractRemoteContentHelper {

	private final PhotosightContentDataExtractor photosightContentDataExtractor = new PhotosightContentDataExtractor();

	public PhotosightRemoteContentHelper() {
		super( new LogHelper( PhotosightRemoteContentHelper.class ) );
	}

	@Override
	public PhotosImportSource getPhotosImportSource() {
		return PhotosImportSource.PHOTOSIGHT;
	}

	@Override
	public String getUserCardUrl( final String remotePhotoSiteUserId, final int pageNumber ) {
		return String.format( "%s/?pager=%d", getUserCardPageUrl( remotePhotoSiteUserId ), pageNumber );
	}

	@Override
	public String getPhotoCardUrl( final int remotePhotoSitePhotoId ) {
		return String.format( "http://www.%s/%s/%d/", PhotosImportSource.PHOTOSIGHT.getUrl(), "photos", remotePhotoSitePhotoId );
	}

	@Override
	public String getPhotoCardLink( final RemotePhotoSitePhoto remotePhotoSitePhoto ) {
		final int photosightPhotoId = remotePhotoSitePhoto.getPhotoId();
		return String.format( "<a href='%s' target='_blank'>%s</a> ( #<b>%d</b> )", getPhotoCardUrl( photosightPhotoId ), StringUtilities.unescapeHtml( remotePhotoSitePhoto.getName() ), photosightPhotoId );
	}

	@Override
	public String getPhotoCardLink( final int remotePhotoSitePhotoId ) {
		return String.format( "<a href='%s'>%d</a>", getPhotoCardUrl( remotePhotoSitePhotoId ), remotePhotoSitePhotoId );
	}

	@Override
	public String getPhotoCategoryUrl( final RemotePhotoSiteCategory remotePhotoSiteCategory ) {
		return String.format( "http://www.%s/%s/category/%d/", PhotosImportSource.PHOTOSIGHT.getUrl(), "photos", remotePhotoSiteCategory.getId() );
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

	private BasicClientCookie getRemoteUserCardCookie( final String cookieName, final String remotePhotoSiteUserId ) {
		final BasicClientCookie cookie = new BasicClientCookie( cookieName, remotePhotoSiteUserId );
		cookie.setVersion( 0 );
		cookie.setDomain( String.format( "www.%s", PhotosImportSource.PHOTOSIGHT.getUrl() ) );
		cookie.setPath( "/" );

		return cookie;
	}

	private String getUserCardPageUrl( final String remotePhotoSiteUserId ) {
		return String.format( "http://www.%s/%s/%s/", PhotosImportSource.PHOTOSIGHT.getUrl(), "users", remotePhotoSiteUserId );
	}

	@Override
	protected PhotosightContentDataExtractor getPhotosightContentDataExtractor() {
		return photosightContentDataExtractor;
	}
}
