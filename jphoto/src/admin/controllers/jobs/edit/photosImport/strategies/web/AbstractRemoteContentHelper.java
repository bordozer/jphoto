package admin.controllers.jobs.edit.photosImport.strategies.web;

import admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import admin.controllers.jobs.edit.photosImport.strategies.web.photosight.PhotosightContentDataExtractor;
import admin.controllers.jobs.edit.photosImport.strategies.web.photosight.PhotosightRemoteContentHelper;
import core.log.LogHelper;
import core.services.entry.GenreService;
import core.services.translator.Language;
import core.services.utils.EntityLinkUtilsService;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import utils.StringUtilities;

import java.io.IOException;

public abstract class AbstractRemoteContentHelper {

	protected final LogHelper log;

	protected AbstractRemoteContentHelper( final LogHelper log ) {
		this.log = log;
	}

	public abstract PhotosImportSource getPhotosImportSource();

	public abstract String getUserCardUrl( final String remotePhotoSiteUserId, final int pageNumber );

	public abstract String getPhotoCardUrl( int remotePhotoSitePhotoId );

	public abstract String getPhotoCardLink( final RemotePhotoSitePhoto remotePhotoSitePhoto );

	public abstract String getPhotoCardLink( final int remotePhotoSitePhotoId );

	public abstract String getPhotoCategoryUrl( final RemotePhotoSiteCategory remotePhotoSiteCategory );

	protected abstract PhotosightContentDataExtractor getPhotosightContentDataExtractor();

	public String getUserCardLink( final RemotePhotoSiteUser remotePhotoSiteUser ) {

		return String.format( "<a href='%s' target='_blank'>%s</a> ( #<b>%s</b> )"
			, getUserCardUrl( remotePhotoSiteUser.getId(), 1 )
			, StringUtilities.unescapeHtml( remotePhotoSiteUser.getName() )
			, remotePhotoSiteUser.getId()
		);
	}

	public String getUserName( final String remotePhotoSiteUserId ) {
		final String userPageContent = getUserPageContent( 1, remotePhotoSiteUserId );
		if ( StringUtils.isEmpty( userPageContent ) ) {
			return null;
		}
		return getPhotosightContentDataExtractor().extractPhotosightUserName( userPageContent );
	}

	public String getUserCardUrl( final String remotePhotoSiteUserId ) {
		return getUserCardUrl( remotePhotoSiteUserId, 0 );
	}

	public String getUserName( final RemotePhotoSiteUser remotePhotoSiteUser ) {
		return getUserName( remotePhotoSiteUser.getId() );
	}

	public String getPhotoCategoryLink( final RemotePhotoSiteCategory remotePhotoSiteCategory, final EntityLinkUtilsService entityLinkUtilsService, final GenreService genreService, final Language language ) {
		return String.format( "<a href='%s' target='_blank'>%s</a> ( mapped to %s )"
			, getPhotoCategoryUrl( remotePhotoSiteCategory )
			, remotePhotoSiteCategory.getName()
			, entityLinkUtilsService.getPhotosByGenreLink( genreService.loadIdByName( RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( remotePhotoSiteCategory ).getName() )
			, language )
		);
	}

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

	public String getUserPageContent( final int pageNumber, final String remotePhotoSiteUserId ) {
		return getRemotePageContent( remotePhotoSiteUserId, getUserCardUrl( remotePhotoSiteUserId, pageNumber ) );
	}

	public String getPhotoPageContent( final RemotePhotoSiteUser remotePhotoSiteUser, final int remotePhotoSitePhotoId ) {
		return getRemotePageContent( remotePhotoSiteUser.getId(), getPhotoCardUrl( remotePhotoSitePhotoId ) );
	}

	protected String getRemotePageContent( final String remotePhotoSiteUserId, final String pageUrl ) {
		final DefaultHttpClient httpClient = new DefaultHttpClient();

		final HttpGet httpGet = new HttpGet( pageUrl );
		addNecessaryCookies( httpClient, remotePhotoSiteUserId );

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
