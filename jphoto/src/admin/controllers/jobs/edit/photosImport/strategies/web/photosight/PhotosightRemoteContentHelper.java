package admin.controllers.jobs.edit.photosImport.strategies.web.photosight;

import admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import admin.controllers.jobs.edit.photosImport.strategies.web.*;
import core.log.LogHelper;
import core.services.entry.GenreService;
import core.services.translator.Language;
import core.services.utils.EntityLinkUtilsService;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import utils.StringUtilities;

public class PhotosightRemoteContentHelper extends AbstractRemoteContentHelper {

	public PhotosightRemoteContentHelper() {
		super( new LogHelper( PhotosightRemoteContentHelper.class ) );
	}

	@Override
	public PhotosImportSource getPhotosImportSource() {
		return PhotosImportSource.PHOTOSIGHT;
	}

	@Override
	public String getUserCardLink( final RemotePhotoSiteUser remotePhotoSiteUser ) {
		final String photosightUserId = remotePhotoSiteUser.getId();
		return String.format( "<a href='%s' target='_blank'>%s</a> ( #<b>%s</b> )", getUserCardUrl( photosightUserId, 1 ), StringUtilities.unescapeHtml( remotePhotoSiteUser.getName() ), photosightUserId );
	}

	@Override
	public String getUserCardUrl( final String remotePhotoSiteUserId ) {
		return getUserCardUrl( remotePhotoSiteUserId, 0 );
	}

	@Override
	public String getUserCardUrl( final String remotePhotoSiteUserId, final int page ) {
		return String.format( "%s/?pager=%d", getUserCardPageUrl( remotePhotoSiteUserId ), page );
	}

	@Override
	public String getUserName( final RemotePhotoSiteUser remotePhotoSiteUser ) {
		return getUserName( remotePhotoSiteUser.getId() );
	}

	@Override
	public String getUserName( final String remotePhotoSiteUserId ) {
		final String userPageContent = getUserPageContent( 1, remotePhotoSiteUserId );
		if ( StringUtils.isEmpty( userPageContent ) ) {
			return null;
		}
		return new PhotosightContentDataExtractor().extractPhotosightUserName( userPageContent );
	}

	@Override
	public String getUserPageContent( final int pageNumber, final String remotePhotoSiteUserId ) {
		return getContent( remotePhotoSiteUserId, getUserCardUrl( remotePhotoSiteUserId, pageNumber ) );
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
	public String getPhotoPageContent( final RemotePhotoSiteUser remotePhotoSiteUser, final int remotePhotoSitePhotoId ) {
		return getContent( remotePhotoSiteUser.getId(), getPhotoCardUrl( remotePhotoSitePhotoId ) );
	}

	@Override
	public String getPhotoCategoryUrl( final RemotePhotoSiteCategory remotePhotoSiteCategory ) {
		return String.format( "http://www.%s/%s/category/%d/", PhotosImportSource.PHOTOSIGHT.getUrl(), "photos", remotePhotoSiteCategory.getId() );
	}

	@Override
	public String getPhotoCategoryLink( final RemotePhotoSiteCategory remotePhotoSiteCategory, final EntityLinkUtilsService entityLinkUtilsService, final GenreService genreService, final Language language ) {
		return String.format( "<a href='%s' target='_blank'>%s</a> ( mapped to %s )", getPhotoCategoryUrl( remotePhotoSiteCategory ), remotePhotoSiteCategory.getName(), entityLinkUtilsService.getPhotosByGenreLink( genreService.loadIdByName( RemotePhotoSitePhotoImageFileUtils.getGenreDiscEntry( remotePhotoSiteCategory ).getName() ), language ) );
	}

	@Override
	protected void setCookie( final DefaultHttpClient httpClient, final String remotePhotoSiteUserId ) {
		final CookieStore cookieStore = new BasicCookieStore();

		final BasicClientCookie cookieIsDisabledNude = getCookie( String.format( "is_disabled_nude_profile_%s", remotePhotoSiteUserId ), "1" );
		cookieStore.addCookie( cookieIsDisabledNude );

		final BasicClientCookie cookieShowNude = getCookie( "show_nude", "1" );
		cookieStore.addCookie( cookieShowNude );

		httpClient.setCookieStore( cookieStore );
	}

	@Override
	protected BasicClientCookie getCookie( final String cookieName, final String remotePhotoSiteUserId ) {
		final BasicClientCookie cookie = new BasicClientCookie( cookieName, remotePhotoSiteUserId );
		cookie.setVersion( 0 );
		cookie.setDomain( String.format( "www.%s", PhotosImportSource.PHOTOSIGHT.getUrl() ) );
		cookie.setPath( "/" );

		return cookie;
	}

	private String getPhotoCardUrl( final int remotePhotoSitePhotoId ) {
		return String.format( "http://www.%s/%s/%d/", PhotosImportSource.PHOTOSIGHT.getUrl(), "photos", remotePhotoSitePhotoId );
	}

	private String getUserCardPageUrl( final String remotePhotoSiteUserId ) {
		return String.format( "http://www.%s/%s/%s/", PhotosImportSource.PHOTOSIGHT.getUrl(), "users", remotePhotoSiteUserId );
	}
}
