package admin.controllers.jobs.edit.photosImport.strategies.photosight;

import core.exceptions.BaseRuntimeException;
import utils.NumberUtils;
import utils.StringUtilities;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.collect.Lists.newArrayList;

public class PhotosightContentHelper {

	public static final String NO_PHOTO_NAME = "-no name-";

	public static int getTotalPagesQty( final String userCardContent, int photosightUserId ) {
		// <a class="" href="/users/316896/?pager=8">8</a>
		final Pattern pattern = Pattern.compile( String.format( "<a class=\"(.*?)\" href=\"/users/%d/\\?pager=(.+?)\">", photosightUserId ) );
		final Matcher matcher = pattern.matcher( userCardContent );

		int result = 1;
		while ( matcher.find() ) {
			result = NumberUtils.convertToInt( matcher.group( 2 ) );
		}

		return result;
	}

	public static int extractPhotoCategoryId( final String photoPageContent ) {
		final Pattern pattern = Pattern.compile( "href=\"/photos/category/(.+?)/\" id=\"currentcat\"" );
		final Matcher matcher = pattern.matcher( photoPageContent );

		if ( matcher.find() ) {
			final String _categoryId = matcher.group( 1 );
			return NumberUtils.convertToInt( _categoryId );
		}

		throw new BaseRuntimeException( "Can not find photosight photo category in the page context" );
	}

	public static String extractPhotoName( final String photoPageContent ) {

		final Pattern pattern = Pattern.compile( "<div class=\"photoinfobox\">\\s+<h1>(.+?)</h1>" );
		final Matcher matcher = pattern.matcher( photoPageContent );

		if ( matcher.find() ) {
			final String name = matcher.group( 1 );

			return StringUtilities.truncateString( name, 255 );
		}

		return NO_PHOTO_NAME;
	}

	public static List<String> extractComments( final String photoPageContent ) {
		final List<String> result = newArrayList();

		final Pattern pattern = Pattern.compile( "<div class=\"commenttext\">(.+?)</div>" );
		final Matcher matcher = pattern.matcher( photoPageContent );
		while ( matcher.find() ) {
			result.add( matcher.group( 1 ) );
		}

		return result;
	}
}
