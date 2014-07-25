package admin.controllers.jobs.edit.photosImport.strategies.web.photosight;

import admin.controllers.jobs.edit.photosImport.strategies.web.AbstractRemoteContentHelper;
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
	public String getImageContentFromUrl( final String cardUrl ) {
		final DefaultHttpClient httpClient = new DefaultHttpClient();

		final String uri = String.format( "http://%s", cardUrl );

		log.debug( String.format( "Getting content: %s", uri ) );

		final HttpGet httpGet = new HttpGet( uri );

		try {
			final ResponseHandler<String> responseHandler = new BasicResponseHandler();
			return httpClient.execute( httpGet, responseHandler ); // -XX:-LoopUnswitching
		} catch ( final IOException e ) {
			log.error( String.format( "Can not get image content: '%s'", cardUrl ), e );
		} finally {
			httpClient.getConnectionManager().shutdown();
		}

		return null;
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
	public String getPhotosightCategoryPageUrl( final PhotosightCategory photosightCategory ) {
		return String.format( "http://www.%s/%s/category/%d/", PhotosightImageFileUtils.PHOTOSIGHT_HOST, "photos", photosightCategory.getId() );
	}

	@Override
	public String getPhotosightUserName( final PhotosightUser photosightUser ) {
		return getPhotosightUserName( photosightUser.getId() );
	}

	@Override
	public String getPhotosightUserName( final String photosightUserId ) {
		final String userPageContent = getUserPageContent( 1, photosightUserId );
		if ( StringUtils.isEmpty( userPageContent ) ) {
			return null;
		}
		return PhotosightContentDataExtractor.extractPhotosightUserName( userPageContent );
	}

	@Override
	public String getPhotosightUserPageLink( final PhotosightUser photosightUser ) {
		final String photosightUserId = photosightUser.getId();
		return String.format( "<a href='%s' target='_blank'>%s</a> ( #<b>%s</b> )", getUserCardUrl( photosightUserId, 1 ), StringUtilities.unescapeHtml( photosightUser.getName() ), photosightUserId );
	}

	@Override
	public String getPhotosightPhotoPageLink( final PhotosightPhoto photosightPhoto ) {
		final int photosightPhotoId = photosightPhoto.getPhotoId();
		return String.format( "<a href='%s' target='_blank'>%s</a> ( #<b>%d</b> )", getPhotoCardUrl( photosightPhotoId ), StringUtilities.unescapeHtml( photosightPhoto.getName() ), photosightPhotoId );
	}

	@Override
	public String getPhotosightCategoryPageLink( final PhotosightCategory photosightCategory, final EntityLinkUtilsService entityLinkUtilsService, final GenreService genreService, final Language language ) {
		return String.format( "<a href='%s' target='_blank'>%s</a> ( mapped to %s )", getPhotosightCategoryPageUrl( photosightCategory ), photosightCategory.getName(), entityLinkUtilsService.getPhotosByGenreLink( genreService.loadIdByName( PhotosightImageFileUtils.getGenreDiscEntry( photosightCategory ).getName() ), language ) );
	}

	@Override
	public String getPhotoCardLink( final int photosightPhotoId ) {
		return String.format( "<a href='%s'>%d</a>", getPhotoCardUrl( photosightPhotoId ), photosightPhotoId );
	}

	@Override
	public String getUserPageContent( final int page, final String photosightUserId ) {
		return getContent( photosightUserId, getUserCardUrl( photosightUserId, page ) );
	}

	@Override
	public String getPhotoPageContent( final PhotosightUser photosightUser, final int photoId ) {
		return getContent( photosightUser.getId(), getPhotoCardUrl( photoId ) );
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
		cookie.setDomain( String.format( "www.%s", PhotosightImageFileUtils.PHOTOSIGHT_HOST ) );
		cookie.setPath( "/" );

		return cookie;
	}

	private String getPhotoCardUrl( final int photoId ) {
		return String.format( "http://www.%s/%s/%d/", PhotosightImageFileUtils.PHOTOSIGHT_HOST, "photos", photoId );
	}

	private String getUserCardPageUrl( final String userId ) {
		return String.format( "http://www.%s/%s/%s/", PhotosightImageFileUtils.PHOTOSIGHT_HOST, "users", userId );
	}
}
