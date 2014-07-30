package admin.controllers.jobs.edit.photosImport.strategies.web;

import admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import admin.controllers.jobs.edit.photosImport.strategies.web.naturelight.NaturelightUrlHelper;
import admin.controllers.jobs.edit.photosImport.strategies.web.photos35.Photo35UrlHelper;
import admin.controllers.jobs.edit.photosImport.strategies.web.photosight.PhotosightUrlHelper;
import core.log.LogHelper;
import core.services.entry.GenreService;
import core.services.remotePhotoSite.RemotePhotoCategoryService;
import core.services.translator.Language;
import core.services.utils.EntityLinkUtilsService;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import utils.StringUtilities;

import java.io.IOException;

public abstract class AbstractRemotePhotoSiteUrlHelper {

	protected final LogHelper log;

	protected AbstractRemotePhotoSiteUrlHelper( final LogHelper log ) {
		this.log = log;
	}

	public abstract PhotosImportSource getPhotosImportSource();

	public abstract String getUserCardUrl( final String remotePhotoSiteUserId, final int pageNumber );

	public abstract String getPhotoCardUrl( final String remotePhotoSiteUserId, int remotePhotoSitePhotoId );

	public abstract String getPhotoCategoryUrl( final RemotePhotoSiteCategory remotePhotoSiteCategory );

	protected abstract AbstractRemotePhotoSitePageContentDataExtractor getRemotePhotoSiteContentDataExtractor();

	protected void addNecessaryCookies( final DefaultHttpClient httpClient, final String remotePhotoSiteUserId ) {
	}

	public String getRemotePhotoSiteHost() {
		return getPhotosImportSource().getUrl();
	}

	public static AbstractRemotePhotoSiteUrlHelper getInstance( final PhotosImportSource importSource ) {

		switch ( importSource ) {
			case PHOTOSIGHT:
				return new PhotosightUrlHelper();
			case PHOTO35:
				return new Photo35UrlHelper();
			case NATURELIGHT:
				return new NaturelightUrlHelper();
		}

		throw new IllegalArgumentException( String.format( "Illegal web photos import source: '%s'", importSource ) );
	}

	public String getUserCardLink( final RemoteUser remoteUser ) {

		return String.format( "<a href='%s' target='_blank'>%s</a> ( #<b>%s</b> )"
			, getUserCardUrl( remoteUser.getId(), 1 )
			, StringUtilities.unescapeHtml( remoteUser.getName() )
			, remoteUser.getId()
		);
	}

	public String getUserCardUrl( final String remotePhotoSiteUserId ) {
		return getUserCardUrl( remotePhotoSiteUserId, 0 );
	}

	public String extractUserNameFromRemoteSite( final String remotePhotoSiteUserId ) {

		final String userPageContent = getUserPageContent( 1, remotePhotoSiteUserId );
		if ( StringUtils.isEmpty( userPageContent ) ) {
			return null;
		}

		return getRemotePhotoSiteContentDataExtractor().extractRemotePhotoSiteUserName( userPageContent );
	}

	public String extractUserNameFromRemoteSite( final RemoteUser remoteUser ) {
		return extractUserNameFromRemoteSite( remoteUser.getId() );
	}

	public String getPhotoCardLink( final String remotePhotoSiteUserId, final int remotePhotoSitePhotoId ) {
		return String.format( "<a href='%s'>%d</a>", getPhotoCardUrl( remotePhotoSiteUserId, remotePhotoSitePhotoId ), remotePhotoSitePhotoId );
	}

	public String getPhotoCardLink( final RemotePhotoSitePhoto remotePhotoSitePhoto ) {

		return String.format( "<a href='%s' target='_blank'>%s</a> ( #<b>%d</b> )"
			, getPhotoCardUrl( remotePhotoSitePhoto.getRemoteUser().getId(), remotePhotoSitePhoto.getPhotoId() )
			, StringUtilities.unescapeHtml( remotePhotoSitePhoto.getName() )
			, remotePhotoSitePhoto.getPhotoId()
		);
	}

	public String getPhotoCategoryLink( final RemotePhotoSiteCategory remotePhotoSiteCategory, final EntityLinkUtilsService entityLinkUtilsService, final GenreService genreService, final Language language, final RemotePhotoCategoryService remotePhotoCategoryService) {
		return String.format( "<a href='%s' target='_blank'>%s</a> ( mapped to %s )"
			, getPhotoCategoryUrl( remotePhotoSiteCategory )
			, remotePhotoSiteCategory.getName()
			, entityLinkUtilsService.getPhotosByGenreLink( remotePhotoCategoryService.getMappedGenreOrNull( remotePhotoSiteCategory ), language )
		);
	}

	public String getUserPageContent( final int pageNumber, final String remotePhotoSiteUserId ) {
		return getRemotePageContent( remotePhotoSiteUserId, getUserCardUrl( remotePhotoSiteUserId, pageNumber ) );
	}

	public String getPhotoPageContent( final RemoteUser remoteUser, final int remotePhotoSitePhotoId ) {
		return getRemotePageContent( remoteUser.getId(), getPhotoCardUrl( remoteUser.getId(), remotePhotoSitePhotoId ) );
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

	private  String getRemotePageContent( final String remotePhotoSiteUserId, final String pageUrl ) {

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

	protected BasicClientCookie getRemoteUserCardCookie( final String cookieName, final String remotePhotoSiteUserId ) {
		return getRemoteUserCardCookie( "/", cookieName, remotePhotoSiteUserId );
	}

	protected BasicClientCookie getRemoteUserCardCookie( final String path, final String cookieName, final String remotePhotoSiteUserId ) {
		final BasicClientCookie cookie = new BasicClientCookie( cookieName, remotePhotoSiteUserId );
		cookie.setVersion( 0 );
		cookie.setDomain( String.format( "www.%s", getRemotePhotoSiteHost() ) );
		cookie.setPath( path );

		return cookie;
	}
}
