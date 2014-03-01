package admin.controllers.jobs.edit.photosImport.strategies.photosight;

import core.log.LogHelper;
import core.services.entry.GenreService;
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

public class PhotosightRemoteContentHelper {

	private static final LogHelper log = new LogHelper( PhotosightRemoteContentHelper.class );

	public static String getImageContentFromUrl( final String cardUrl ) {
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

	public static String getPhotoCardUrl( final int photoId ) {
		return getPageUrl( photoId, PhotosightImportStrategy.PHOTOS );
	}

	public static String getUserCardUrl( final int userId, final int page ) {
		return String.format( "%s/?pager=%d", getPageUrl( userId, PhotosightImportStrategy.USERS ), page );
	}

	private static String getPageUrl( final int photoId, final String photos ) {
		return String.format( "http://www.%s/%s/%d/", PhotosightImageFileUtils.PHOTOSIGHT_HOST, photos, photoId );
	}

	public static String getPhotosightCategoryPageUrl( final PhotosightCategory photosightCategory ) {
		return String.format( "http://www.%s/%s/category/%d/", PhotosightImageFileUtils.PHOTOSIGHT_HOST, PhotosightImportStrategy.PHOTOS, photosightCategory.getId() );
	}

	public static String getPhotosightUserName( final PhotosightUser photosightUser ) {
		final String userPageContent = PhotosightRemoteContentHelper.getUserPageContent( 1, photosightUser.getId() );
		if ( StringUtils.isEmpty( userPageContent ) ) {
			return null;
		}
		return PhotosightContentDataExtractor.extractPhotosightUserName( userPageContent );
	}

	public static String getPhotosightUserPageLink( final PhotosightUser photosightUser ) {
		final int photosightUserId = photosightUser.getId();
		return String.format( "<a href='%s' target='_blank'>%s</a> ( #<b>%d</b> )", PhotosightRemoteContentHelper.getUserCardUrl( photosightUserId, 1 ), StringUtilities.unescapeHtml( photosightUser.getName() ), photosightUserId );
	}

	public static String getPhotosightPhotoPageLink( final PhotosightPhoto photosightPhoto ) {
		final int photosightPhotoId = photosightPhoto.getPhotoId();
		return String.format( "<a href='%s' target='_blank'>%s</a> ( #<b>%d</b> )", PhotosightRemoteContentHelper.getPhotoCardUrl( photosightPhotoId ), StringUtils.isNotEmpty( photosightPhoto.getName() ) ? StringUtilities.unescapeHtml( photosightPhoto.getName() ) : "-no name-", photosightPhotoId );
	}

	public static String getPhotosightCategoryPageLink( final PhotosightCategory photosightCategory, final EntityLinkUtilsService entityLinkUtilsService, final GenreService genreService ) {
		return String.format( "<a href='%s' target='_blank'>%s</a> ( mapped to %s )"
			, PhotosightRemoteContentHelper.getPhotosightCategoryPageUrl( photosightCategory )
			, photosightCategory.getName()
			, entityLinkUtilsService.getPhotosByGenreLink( genreService.loadIdByName( PhotosightImageFileUtils.getGenreDiscEntry( photosightCategory ).getName() ) )
		);
	}

	public static String getPhotoCardLink( final int photosightPhotoId ) {
		return String.format( "<a href='%s'>%d</a>", PhotosightRemoteContentHelper.getPhotoCardUrl( photosightPhotoId ), photosightPhotoId );
	}

	public static String getUserPageContent( final int page, int photosightUserId ) {
		return getContent( photosightUserId, getUserCardUrl( photosightUserId, page ) );
	}

	public static String getPhotoPageContent( final PhotosightUser photosightUser, final int photoId ) {
		return getContent( photosightUser.getId(), getPhotoCardUrl( photoId ) );
	}

	private static void setCookie( final DefaultHttpClient httpClient, final int userId ) {
		final CookieStore cookieStore = new BasicCookieStore();

		final BasicClientCookie cookieIsDisabledNude = getCookie( String.format( "is_disabled_nude_profile_%d", userId ), "1" );
		cookieStore.addCookie( cookieIsDisabledNude );

		final BasicClientCookie cookieShowNude = getCookie( "show_nude", "1" );
		cookieStore.addCookie( cookieShowNude );

		httpClient.setCookieStore( cookieStore );
	}

	private static String getContent( final int userId, final String pageUrl ) {
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

	private static BasicClientCookie getCookie( final String w3t_myname, final String bordark ) {
		final BasicClientCookie cookie = new BasicClientCookie( w3t_myname, bordark );
		cookie.setVersion( 0 );
		cookie.setDomain( String.format( "www.%s", PhotosightImageFileUtils.PHOTOSIGHT_HOST ) );
		cookie.setPath( "/" );

		return cookie;
	}
}
