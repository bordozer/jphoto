package admin.controllers.jobs.edit.photosImport.strategies.web.photos35;

import admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import admin.controllers.jobs.edit.photosImport.strategies.web.AbstractRemotePhotoSitePageContentDataExtractor;
import admin.controllers.jobs.edit.photosImport.strategies.web.photosight.PhotosightRemoteContentHelper;
import core.exceptions.BaseRuntimeException;
import utils.NumberUtils;
import utils.StringUtilities;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.collect.Lists.newArrayList;

public class Photo35ContentDataExtractor extends AbstractRemotePhotoSitePageContentDataExtractor {

	@Override
	public String extractImageUrl( final String remotePhotoSiteUserId, final int remotePhotoSitePhotoId, final String photoPageContent ) {
		// <img class="mainPhoto" id="mainPhoto" src="http://babakfatholahi.35photo.ru/photos/20140723/743798.jpg" style="cursor: pointer; max-width: 1387px; max-height: 814px;"/>
		final Pattern pattern = Pattern.compile( String.format( "<img class=\"mainPhoto\" id=\"mainPhoto\" src=\"http://%s.%s/photos/(.+?)/%s.jpg\""
			, remotePhotoSiteUserId, getHost(), remotePhotoSitePhotoId ) );
		final Matcher matcher = pattern.matcher( photoPageContent );

		if ( matcher.find() ) {
			return String.format( "http://%s.%s/photos/%s/%d.jpg", remotePhotoSiteUserId, getHost(), matcher.group( 1 ),  remotePhotoSitePhotoId );
		}

		return null;
	}

	@Override
	public String getPhotoIdRegex( final String remotePhotoSiteUserId ) {
		// <a href="http://babakfatholahi.35photo.ru/photo_740647/"><img src="http://35photo.ru/photos_col/r2/148/740647_500r.jpg" width="280"></img></a>
		return String.format( "<a href=\"http://%s.%s/photo_(.+?)/", remotePhotoSiteUserId, getHost() );
	}

	@Override
	public String extractRemotePhotoSiteUserName( final String userPageContent ) {
		// <div style="font-family: Helvetica, Arial;margin-top:-110px;margin-left:220px;font-size:2.5em;text-shadow: 1px 1px 1px rgba(0,0,0,1.0);color:#fff"><b>Babak Fatholahi</b></div>
		final Pattern pattern = Pattern.compile(  "<div style=\"font-family: Helvetica, Arial;margin-top:-110px;margin-left:220px;(.+?)color:#fff\"><b>(.+?)</b></div>" );
		final Matcher matcher = pattern.matcher( userPageContent );

		if ( matcher.find() ) {
			final String photosightUserName = matcher.group( 2 );
			return photosightUserName.trim();
		}

		return null;
	}

	@Override
	public int extractRemotePhotoSiteUserPhotosCount( final String remotePhotoSiteUserId ) {
		return 28; // TODO
	}

	@Override
	public int getTotalPagesQty( final String userCardContent, final String remotePhotoSiteUserId ) {
		return 1;
	}

	@Override
	public int extractPhotoCategoryId( final String photoPageContent ) {
		// <a href="http://35photo.ru/genre_96/">Portrait</a>
		final Pattern pattern = Pattern.compile( String.format( "<a href=\"http://%s/genre_(.+?)/\">(.+?)</a>", getHost() ) );
		final Matcher matcher = pattern.matcher( photoPageContent );

		if ( matcher.find() ) {
			final String _categoryId = matcher.group( 1 );
			return NumberUtils.convertToInt( _categoryId );
		}

		throw new BaseRuntimeException( String.format( "%s: can not find photo category from the page context", getHost() ) );
	}

	@Override
	public String extractPhotoName( final String photoPageContent ) {
		// <div class="photoPage"><h1>South Georgia</h1></div>
		final Pattern pattern = Pattern.compile( "<div class=\"photoPage\"><h1>(.+?)</h1></div>" );
		final Matcher matcher = pattern.matcher( photoPageContent );

		if ( matcher.find() ) {
			final String name = matcher.group( 1 );

			return StringUtilities.truncateString( name, 255 );
		}

		return NO_PHOTO_NAME;
	}

	@Override
	public List<String> extractComments( final String photoPageContent ) {
		final List<String> result = newArrayList();

		final Pattern pattern = Pattern.compile( "<div style=\"margin-top:5px\" id=\"comm(.+?)\">(.+?)</div>" );
		final Matcher matcher = pattern.matcher( photoPageContent );
		while ( matcher.find() ) {
			result.add( matcher.group( 2 ) );
		}

		return result;
	}

	@Override
	protected String getHost() {
		return PhotosImportSource.PHOTO35.getUrl();
	}
}
