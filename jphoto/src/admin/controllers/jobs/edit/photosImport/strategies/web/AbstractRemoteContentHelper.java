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

import java.io.IOException;

public abstract class AbstractRemoteContentHelper {

	protected final LogHelper log;

	protected AbstractRemoteContentHelper( final LogHelper log ) {
		this.log = log;
	}

	public abstract PhotosImportSource getPhotosImportSource();

	public abstract String getUserCardUrl( final String userId );

	public abstract String getUserCardUrl( final String userId, int page );

	public abstract String getRemotePhotoSiteCategoryPageUrl( final RemotePhotoSiteCategory remotePhotoSiteCategory );

	public abstract String getRemotePhotoSiteUserName( final RemotePhotoSiteUser remotePhotoSiteUser );

	public abstract String getRemotePhotoSiteUserName( final String remotePhotoSiteUserId );

	public abstract String getRemotePhotoSiteUserPageLink( final RemotePhotoSiteUser remotePhotoSiteUser );

	public abstract String getRemotePhotoSitePhotoPageLink( final RemotePhotoSitePhoto remotePhotoSitePhoto );

	public abstract String getRemotePhotoSiteCategoryPageLink( final RemotePhotoSiteCategory remotePhotoSiteCategory, final EntityLinkUtilsService entityLinkUtilsService, final GenreService genreService, Language language );

	public abstract String getPhotoCardLink( final int remotePhotoSitePhotoId );

	public abstract String getUserPageContent( final int page, final String remotePhotoSiteUserId );

	public abstract String getPhotoPageContent( final RemotePhotoSiteUser remotePhotoSiteUser, final int photoId );

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

	public static AbstractRemoteContentHelper getInstance( final PhotosImportSource importSource ) {

		switch ( importSource ) {
			case PHOTOSIGHT:
				return new PhotosightRemoteContentHelper();
		}

		throw new IllegalArgumentException( String.format( "Illegal web photos import source: '%s'", importSource ) );
	}
}
