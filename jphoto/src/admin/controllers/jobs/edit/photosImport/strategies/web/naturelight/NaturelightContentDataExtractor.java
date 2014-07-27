package admin.controllers.jobs.edit.photosImport.strategies.web.naturelight;

import admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import admin.controllers.jobs.edit.photosImport.strategies.web.AbstractRemotePhotoSitePageContentDataExtractor;
import core.exceptions.BaseRuntimeException;
import core.services.system.Services;
import utils.NumberUtils;
import utils.StringUtilities;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.collect.Lists.newArrayList;

public class NaturelightContentDataExtractor extends AbstractRemotePhotoSitePageContentDataExtractor {

	@Override
	public List<String> extractImageUrl( final String remotePhotoSiteUserId, final int remotePhotoSitePhotoId, final String photoPageContent ) {
		// <img src="http://naturelight.ru/photo/2014-05-18/56073.jpg"

		final Pattern pattern = Pattern.compile( String.format( "<img src=\"http://%s/photo/(.+?)/%s.jpg\"", getHost(), remotePhotoSitePhotoId ) );
		final Matcher matcher = pattern.matcher( photoPageContent );

		if ( matcher.find() ) {
			return newArrayList( String.format( "%s/photo/%s/%s.jpg", getHost(), matcher.group( 1 ), remotePhotoSitePhotoId ) );
		}

		return null;
	}

	@Override
	public String getPhotoIdRegex( final String remotePhotoSiteUserId ) {
		// <a href="/show_photo/56743.html"><img src="http://naturelight.ru/photo/2014-07-26/pv56743_w320.jpg" title="?????? ? ??????" border="0"/></a>
		return String.format( "<a href=\"http://%s//show_photo/(.+?).html/", getHost() );
	}

	@Override
	public String extractRemotePhotoSiteUserName( final String userPageContent ) {
		// <div class="nav_header">????????</div>
		final Pattern pattern = Pattern.compile(  "<div class=\"nav_header\">(.+?)</div>" );
		final Matcher matcher = pattern.matcher( userPageContent );

		if ( matcher.find() ) {
			final String photosightUserName = matcher.group( 1 );
			return photosightUserName.trim();
		}

		return null;
	}

	@Override
	public int extractRemotePhotoSiteUserPhotosCount( final String remotePhotoSiteUserId ) {
		// <li>????? ??????????:<b>412</b></li>
		final String userPageContent = new NaturelightRemoteContentHelper().getUserPageContent( 1, remotePhotoSiteUserId );
		final Pattern pattern = Pattern.compile( "<li>(.+?):\\s+<b>(.+?)</b></li>" );
		final Matcher matcher = pattern.matcher( userPageContent );

		if ( matcher.find() ) {
			final String photosightUserName = matcher.group( 2 );
			return NumberUtils.convertToInt( photosightUserName.trim() );
		}

		return 0;
	}

	@Override
	public int getTotalPagesQty( final String userCardContent, final String remotePhotoSiteUserId ) {
		// <a href="/author/2608/page5.html">5</a>
		final Pattern pattern = Pattern.compile( String.format( "<a href=\"/author/%s/page(.+?).html\">(.+?)</a>", remotePhotoSiteUserId ) );
		final Matcher matcher = pattern.matcher( userCardContent );

		String pages = "";
		while ( matcher.find() ) {
			pages = matcher.group( 2 );
		}

		return NumberUtils.convertToInt( pages );
	}

	@Override
	public int extractPhotoCategoryId( final String photoPageContent ) {
		// <b id="group_value"><a href="/show_group/1.html">?????</a></b><br/>
		final Pattern pattern = Pattern.compile( "<b id=\"group_value\"><a href=\"/show_group/(.+?).html\">(.+?)</a></b>" );
		final Matcher matcher = pattern.matcher( photoPageContent );

		if ( matcher.find() ) {
			final String _categoryId = matcher.group( 1 );
			return NumberUtils.convertToInt( _categoryId );
		}

		throw new BaseRuntimeException( String.format( "%s: can not find photo category from the page context", getHost() ) );
	}

	@Override
	public String extractPhotoName( final String photoPageContent ) {
		//<div class="blockname" style="margin: 0 0 0 0;"> ?????????? </div>
		final Pattern pattern = Pattern.compile( "<div class=\"blockname\" style=\"margin: 0 0 0 0;\">\\s+(.+?)\\s+</div>" );
		final Matcher matcher = pattern.matcher( photoPageContent );

		if ( matcher.find() ) {
			final String name = matcher.group( 1 );

			return StringUtilities.truncateString( name, 255 );
		}

		return NO_PHOTO_NAME;
	}

	@Override
	public List<String> extractComments( final String photoPageContent ) {
		// <div class="sp-comment-text"> ?????? ???????)<div class="pad15"><a href="javascript:setAnswerCommentID(271352);" id="answer_button271352" class="answer-link"></div></div>
		final List<String> result = newArrayList();

		final Pattern pattern = Pattern.compile( "<div class=\"sp-comment-text\">(.+?)<div class=\"pad15\">" );
		final Matcher matcher = pattern.matcher( photoPageContent );
		while ( matcher.find() ) {
			result.add( matcher.group( 1 ).trim() );
		}

		return result;
	}

	@Override
	public Date extractPhotoUploadTime( final String photoPageContent, final Services services ) {
		// <a href="/calendar/2014-05-18.html" class="link">19:31 18/05/2014</a>
		return null;
	}

	@Override
	protected String getHost() {
		return PhotosImportSource.PHOTO35.getUrl();
	}
}
