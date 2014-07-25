package admin.controllers.jobs.edit.photosImport.strategies.web;

import admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import admin.controllers.jobs.edit.photosImport.strategies.web.photosight.PhotosightRemoteContentHelper;
import core.log.LogHelper;
import core.services.entry.GenreService;
import core.services.translator.Language;
import core.services.utils.EntityLinkUtilsService;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.io.IOException;

public abstract class AbstractRemoteContentHelper {

	protected final LogHelper log;

	protected AbstractRemoteContentHelper( final LogHelper log ) {
		this.log = log;
	}

	public abstract PhotosImportSource getPhotosImportSource();

	public abstract String getUserCardUrl( final String remotePhotoSiteUserId );

	public abstract String getUserCardUrl( final String remotePhotoSiteUserId, int page );

	public abstract String getUserCardLink( final RemotePhotoSiteUser remotePhotoSiteUser );

	public abstract String getUserName( final RemotePhotoSiteUser remotePhotoSiteUser );

	public abstract String getUserName( final String remotePhotoSiteUserId );

	public abstract String getUserPageContent( final int pageNumber, final String remotePhotoSiteUserId );

	public abstract String getPhotoCardLink( final RemotePhotoSitePhoto remotePhotoSitePhoto );

	public abstract String getPhotoCardLink( final int remotePhotoSitePhotoId );

	public abstract String getPhotoPageContent( final RemotePhotoSiteUser remotePhotoSiteUser, final int remotePhotoSitePhotoId );

	public abstract String getPhotoCategoryUrl( final RemotePhotoSiteCategory remotePhotoSiteCategory );

	public abstract String getPhotoCategoryLink( final RemotePhotoSiteCategory remotePhotoSiteCategory, final EntityLinkUtilsService entityLinkUtilsService, final GenreService genreService, Language language );

	protected abstract BasicClientCookie getCookie( final String cookieName, final String remotePhotoSiteUserId );

	public String getImageContentFromUrl( final String imageUrl ) {

		final DefaultHttpClient httpClient = new DefaultHttpClient();

		final String uri = String.format( "http://%s", imageUrl );

		log.debug( String.format( "Getting content: %s", uri ) );

		final HttpGet httpGet = new HttpGet( uri );

		try {
			final ResponseHandler<String> responseHandler = new BasicResponseHandler();
			return httpClient.execute( httpGet, responseHandler ); // -XX:-LoopUnswitching
		} catch ( final IOException e ) {
			log.error( String.format( "Can not get image content: '%s'", imageUrl ), e );
		} finally {
			httpClient.getConnectionManager().shutdown();
		}

		return null;
	}

	protected String getRemotePageContent( final String userId, final String pageUrl ) {
		final DefaultHttpClient httpClient = new DefaultHttpClient();

		final HttpGet httpGet = new HttpGet( pageUrl );
		addNecessaryCookies( httpClient, userId );

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

	protected void addNecessaryCookies( final DefaultHttpClient httpClient, final String remotePhotoSiteUserId ) {

	}

	public static AbstractRemoteContentHelper getInstance( final PhotosImportSource importSource ) {

		switch ( importSource ) {
			case PHOTOSIGHT:
				return new PhotosightRemoteContentHelper();
		}

		throw new IllegalArgumentException( String.format( "Illegal web photos import source: '%s'", importSource ) );
	}
}
