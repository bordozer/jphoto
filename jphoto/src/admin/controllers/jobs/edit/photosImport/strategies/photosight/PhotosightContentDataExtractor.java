package admin.controllers.jobs.edit.photosImport.strategies.photosight;

import org.apache.commons.lang.StringUtils;
import utils.NumberUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhotosightContentDataExtractor {

	public static String extractImageUrl( final int photosightPhotoId, final String photoPageContent ) {
		final String imageUrlNew = extractImageUrlByNewRules( photosightPhotoId, photoPageContent );
		if ( StringUtils.isNotEmpty( imageUrlNew ) ) {
			return imageUrlNew;
		}
		return extractImageUrlByOldRules( photosightPhotoId, photoPageContent );
	}

	public static int extractPhotosightPhotoId( final String group ) {
		return NumberUtils.convertToInt( group );
	}

	public static String extractImageUrlByNewRules( final int photosightPhotoId, final String photoPageContent ) {
		// <img src="http://icon.s.photosight.ru/img/8/f4f/3725335_large.jpeg"
		final Pattern pattern = Pattern.compile( String.format( "<img src=\"http://(.+?).%s/(.+?)/%d_large.jp(e*?)g\"", PhotosightImageFileUtils.PHOTOSIGHT_HOST, photosightPhotoId ) );
		final Matcher matcher = pattern.matcher( photoPageContent );

		if ( matcher.find() ) {
			final String photoImageServerUrl = matcher.group( 1 );
			final String someShit = matcher.group( 2 );
			final String extension = matcher.group( 3 );
			return String.format( "%s.%s/%s/%d_large.jp%sg", photoImageServerUrl, PhotosightImageFileUtils.PHOTOSIGHT_HOST, someShit, photosightPhotoId, extension );
		}

		return null;
	}

	public static String extractImageUrlByOldRules( final int photosightPhotoId, final String photoPageContent ) {
		//<img src="http://img-2007-09.photosight.ru/24/2318529.jpg" alt="
		final Pattern pattern = Pattern.compile( String.format( "<img src=\"http://(.+?).%s/(.+?)/%d.jp(e*?)g\"", PhotosightImageFileUtils.PHOTOSIGHT_HOST, photosightPhotoId ) );
		final Matcher matcher = pattern.matcher( photoPageContent );

		if ( matcher.find() ) {
			final String photoImageServerUrl = matcher.group( 1 );
			final String someShit = matcher.group( 2 );
			final String extension = matcher.group( 3 );
			return String.format( "%s.%s/%s/%d.jp%sg", photoImageServerUrl, PhotosightImageFileUtils.PHOTOSIGHT_HOST, someShit, photosightPhotoId, extension );
		}

		return null;
	}

	public static String getPhotoIdRegex() {
		return "<a href=\"/photos/(.+?)/\\?from_member\" class=\"preview230\">";
	}

	public static String extractPhotosightUserName( final String userPageContent ) {
		final Pattern pattern = Pattern.compile(  "<div class=\"usertitle\">(.+?)<h1>(.+?)</h1>" );
		final Matcher matcher = pattern.matcher( userPageContent );

		if ( matcher.find() ) {
			final String photosightUserName = matcher.group( 2 );
			return photosightUserName.trim();
		}

		return null;
	}

	public static int extractPhotosightUserPhotosCount( final String photosightUserId ) {
		final String userPageContent = PhotosightRemoteContentHelper.getUserPageContent( 1, photosightUserId );
		// <a href="/users/344981/" class="uploaded current"><div>196</div>
		final Pattern pattern = Pattern.compile( String.format( "<a href=\"/users/%s/\" class=\"uploaded current\">\\s+<div>(.+?)</div>", photosightUserId ) );
		final Matcher matcher = pattern.matcher( userPageContent );

		if ( matcher.find() ) {
			final String photosightUserName = matcher.group( 1 );
			return NumberUtils.convertToInt( photosightUserName.trim() );
		}

		return 0;
	}
}
