package admin.controllers.jobs.edit.photosImport.strategies.web.photosight;

import admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import admin.controllers.jobs.edit.photosImport.strategies.web.AbstractRemotePhotoSitePageContentDataExtractor;
import core.exceptions.BaseRuntimeException;
import org.apache.commons.lang.StringUtils;
import utils.NumberUtils;
import utils.StringUtilities;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.collect.Lists.newArrayList;

public class PhotosightContentDataExtractor extends AbstractRemotePhotoSitePageContentDataExtractor {

	@Override
	public String extractImageUrl( final int photosightPhotoId, final String photoPageContent ) {
		final String imageUrlNew = extractImageUrlByNewRules( photosightPhotoId, photoPageContent );
		if ( StringUtils.isNotEmpty( imageUrlNew ) ) {
			return imageUrlNew;
		}
		return extractImageUrlByOldRules( photosightPhotoId, photoPageContent );
	}

	@Override
	public int extractRemotePhotoSitePhotoId( final String group ) {
		return NumberUtils.convertToInt( group );
	}

	@Override
	public String extractImageUrlByNewRules( final int photosightPhotoId, final String photoPageContent ) {
		// <img src="http://icon.s.photosight.ru/img/8/f4f/3725335_large.jpeg"
		final Pattern pattern = Pattern.compile( String.format( "<img src=\"http://(.+?).%s/(.+?)/%d_large.jp(e*?)g\"", PhotosImportSource.PHOTOSIGHT.getUrl(), photosightPhotoId ) );
		final Matcher matcher = pattern.matcher( photoPageContent );

		if ( matcher.find() ) {
			final String photoImageServerUrl = matcher.group( 1 );
			final String someShit = matcher.group( 2 );
			final String extension = matcher.group( 3 );
			return String.format( "%s.%s/%s/%d_large.jp%sg", photoImageServerUrl, PhotosImportSource.PHOTOSIGHT.getUrl(), someShit, photosightPhotoId, extension );
		}

		return null;
	}

	@Override
	public String extractImageUrlByOldRules( final int photosightPhotoId, final String photoPageContent ) {
		//<img src="http://img-2007-09.photosight.ru/24/2318529.jpg" alt="
		final Pattern pattern = Pattern.compile( String.format( "<img src=\"http://(.+?).%s/(.+?)/%d.jp(e*?)g\"", PhotosImportSource.PHOTOSIGHT.getUrl(), photosightPhotoId ) );
		final Matcher matcher = pattern.matcher( photoPageContent );

		if ( matcher.find() ) {
			final String photoImageServerUrl = matcher.group( 1 );
			final String someShit = matcher.group( 2 );
			final String extension = matcher.group( 3 );
			return String.format( "%s.%s/%s/%d.jp%sg", photoImageServerUrl, PhotosImportSource.PHOTOSIGHT.getUrl(), someShit, photosightPhotoId, extension );
		}

		return null;
	}

	@Override
	public String getPhotoIdRegex() {
		return "<a href=\"/photos/(.+?)/\\?from_member\" class=\"preview230\">";
	}

	@Override
	public String extractPhotosightUserName( final String userPageContent ) {
		final Pattern pattern = Pattern.compile(  "<div class=\"usertitle\">(.+?)<h1>(.+?)</h1>" );
		final Matcher matcher = pattern.matcher( userPageContent );

		if ( matcher.find() ) {
			final String photosightUserName = matcher.group( 2 );
			return photosightUserName.trim();
		}

		return null;
	}

	@Override
	public int extractPhotosightUserPhotosCount( final String photosightUserId ) {
		final String userPageContent = new PhotosightRemoteContentHelper().getUserPageContent( 1, photosightUserId );
		// <a href="/users/344981/" class="uploaded current"><div>196</div>
		final Pattern pattern = Pattern.compile( String.format( "<a href=\"/users/%s/\" class=\"uploaded current\">\\s+<div>(.+?)</div>", photosightUserId ) );
		final Matcher matcher = pattern.matcher( userPageContent );

		if ( matcher.find() ) {
			final String photosightUserName = matcher.group( 1 );
			return NumberUtils.convertToInt( photosightUserName.trim() );
		}

		return 0;
	}

	@Override
	public int getTotalPagesQty( final String userCardContent, final String photosightUserId ) {
		// <a class="" href="/users/316896/?pager=8">8</a>
		final Pattern pattern = Pattern.compile( String.format( "<a class=\"(.*?)\" href=\"/users/%s/\\?pager=(.+?)\">", photosightUserId ) );
		final Matcher matcher = pattern.matcher( userCardContent );

		int result = 1;
		while ( matcher.find() ) {
			result = NumberUtils.convertToInt( matcher.group( 2 ) );
		}

		return result;
	}

	@Override
	public int extractPhotoCategoryId( final String photoPageContent ) {
		final Pattern pattern = Pattern.compile( "href=\"/photos/category/(.+?)/\" id=\"currentcat\"" );
		final Matcher matcher = pattern.matcher( photoPageContent );

		if ( matcher.find() ) {
			final String _categoryId = matcher.group( 1 );
			return NumberUtils.convertToInt( _categoryId );
		}

		throw new BaseRuntimeException( "Can not find photosight photo category in the page context" );
	}

	@Override
	public String extractPhotoName( final String photoPageContent ) {

		final Pattern pattern = Pattern.compile( "<div class=\"photoinfobox\">\\s+<h1>(.+?)</h1>" );
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

		final Pattern pattern = Pattern.compile( "<div class=\"commenttext\">(.+?)</div>" );
		final Matcher matcher = pattern.matcher( photoPageContent );
		while ( matcher.find() ) {
			result.add( matcher.group( 1 ) );
		}

		return result;
	}
}
