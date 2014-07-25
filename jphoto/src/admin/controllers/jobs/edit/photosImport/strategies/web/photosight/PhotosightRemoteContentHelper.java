package admin.controllers.jobs.edit.photosImport.strategies.web.photosight;

import admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import admin.controllers.jobs.edit.photosImport.strategies.web.*;
import core.log.LogHelper;
import core.services.entry.GenreService;
import core.services.translator.Language;
import core.services.utils.EntityLinkUtilsService;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.CookieStore;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import utils.StringUtilities;

import java.io.IOException;

public class PhotosightRemoteContentHelper extends AbstractRemoteContentHelper {

	public PhotosightRemoteContentHelper() {
		super( new LogHelper( PhotosightRemoteContentHelper.class ) );
	}

	@Override
	public PhotosImportSource getPhotosImportSource() {
		return PhotosImportSource.PHOTOSIGHT;
	}

	@Override
	public String getUserCardUrl( final String userId ) {
		return getUserCardUrl( userId, 0 );
	}

	@Override
	public String getUserCardUrl( final String userId, final int page ) {
		return String.format( "%s/?pager=%d", getUserCardPageUrl( userId ), page );
	}

	@Override
	public String getRemotePhotoSiteCategoryPageUrl( final RemotePhotoSiteCategory remotePhotoSiteCategory ) {
		return String.format( "http://www.%s/%s/category/%d/", PhotosImportSource.PHOTOSIGHT.getUrl(), "photos", remotePhotoSiteCategory.getId() );
	}

	@Override
	public String getRemotePhotoSiteUserName( final RemotePhotoSiteUser remotePhotoSiteUser ) {
		return getRemotePhotoSiteUserName( remotePhotoSiteUser.getId() );
	}

	@Override
	public String getRemotePhotoSiteUserName( final String remotePhotoSiteUserId ) {
		final String userPageContent = getUserPageContent( 1, remotePhotoSiteUserId );
		if ( StringUtils.isEmpty( userPageContent ) ) {
			return null;
		}
		return new PhotosightContentDataExtractor().extractPhotosightUserName( userPageContent );
	}

	@Override
	public String getRemotePhotoSiteUserPageLink( final RemotePhotoSiteUser remotePhotoSiteUser ) {
		final String photosightUserId = remotePhotoSiteUser.getId();
		return String.format( "<a href='%s' target='_blank'>%s</a> ( #<b>%s</b> )", getUserCardUrl( photosightUserId, 1 ), StringUtilities.unescapeHtml( remotePhotoSiteUser.getName() ), photosightUserId );
	}

	@Override
	public String getRemotePhotoSitePhotoPageLink( final RemotePhotoSitePhoto remotePhotoSitePhoto ) {
		final int photosightPhotoId = remotePhotoSitePhoto.getPhotoId();
		return String.format( "<a href='%s' target='_blank'>%s</a> ( #<b>%d</b> )", getPhotoCardUrl( photosightPhotoId ), StringUtilities.unescapeHtml( remotePhotoSitePhoto.getName() ), photosightPhotoId );
	}

	@Override
	public String getRemotePhotoSiteCategoryPageLink( final RemotePhotoSiteCategory remotePhotoSiteCategory, final EntityLinkUtilsService entityLinkUtilsService, final GenreService genreService, final Language language ) {
		return String.format( "<a href='%s' target='_blank'>%s</a> ( mapped to %s )", getRemotePhotoSiteCategoryPageUrl( remotePhotoSiteCategory ), remotePhotoSiteCategory.getName(), entityLinkUtilsService.getPhotosByGenreLink( genreService.loadIdByName( RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( remotePhotoSiteCategory ).getName() ), language ) );
	}

	@Override
	public String getPhotoCardLink( final int remotePhotoSitePhotoId ) {
		return String.format( "<a href='%s'>%d</a>", getPhotoCardUrl( remotePhotoSitePhotoId ), remotePhotoSitePhotoId );
	}

	@Override
	public String getUserPageContent( final int page, final String remotePhotoSiteUserId ) {
		return getContent( remotePhotoSiteUserId, getUserCardUrl( remotePhotoSiteUserId, page ) );
	}

	@Override
	public String getPhotoPageContent( final RemotePhotoSiteUser remotePhotoSiteUser, final int photoId ) {
		return getContent( remotePhotoSiteUser.getId(), getPhotoCardUrl( photoId ) );
	}

	private void setCookie( final DefaultHttpClient httpClient, final String userId ) {
		final CookieStore cookieStore = new BasicCookieStore();

		final BasicClientCookie cookieIsDisabledNude = getCookie( String.format( "is_disabled_nude_profile_%s", userId ), "1" );
		cookieStore.addCookie( cookieIsDisabledNude );

		final BasicClientCookie cookieShowNude = getCookie( "show_nude", "1" );
		cookieStore.addCookie( cookieShowNude );

		httpClient.setCookieStore( cookieStore );
	}

	private String getContent( final String userId, final String pageUrl ) {
		final DefaultHttpClient httpClient = new DefaultHttpClient();

		final HttpGet httpGet = new HttpGet( pageUrl );
		setCookie( httpClient, userId );

		try {
			final ResponseHandler<String> responseHandler = new BasicResponseHandler();
			return httpClient.execute( httpGet, responseHandler ); // -XX:-LoopUnswitching
		} catch ( final IOException e ) {
			log.error( String.format( "Can not get photosight page content: '%s'", pageUrl ), e );
		} finally {
			httpClient.getConnectionManager().shutdown();
		}

		return null;
	}

	private BasicClientCookie getCookie( final String w3t_myname, final String bordark ) {
		final BasicClientCookie cookie = new BasicClientCookie( w3t_myname, bordark );
		cookie.setVersion( 0 );
		cookie.setDomain( String.format( "www.%s", PhotosImportSource.PHOTOSIGHT.getUrl() ) );
		cookie.setPath( "/" );

		return cookie;
	}

	private String getPhotoCardUrl( final int photoId ) {
		return String.format( "http://www.%s/%s/%d/", PhotosImportSource.PHOTOSIGHT.getUrl(), "photos", photoId );
	}

	private String getUserCardPageUrl( final String userId ) {
		return String.format( "http://www.%s/%s/%s/", PhotosImportSource.PHOTOSIGHT.getUrl(), "users", userId );
	}
}
