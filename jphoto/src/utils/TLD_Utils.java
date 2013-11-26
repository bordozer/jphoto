package utils;

import core.context.ApplicationContextHelper;
import core.general.genre.Genre;
import core.general.user.User;
import core.log.LogHelper;
import core.services.utils.PredicateUtilsService;
import core.services.utils.UserPhotoFilePathUtilsService;
import core.services.utils.UserPhotoFilePathUtilsServiceImpl;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

public class TLD_Utils {

	public static String getBaseURLWithPrefix() {
		return ApplicationContextHelper.getUrlUtilsService().getBaseURLWithPrefix();
	}

	public static String getAdminBaseURLWithPrefix() {
		return ApplicationContextHelper.getUrlUtilsService().getAdminBaseURLWithPrefix();
	}

	public static String getBaseURL() {
		return ApplicationContextHelper.getUrlUtilsService().getBaseURL();
	}

	public static String getSiteImagesPath() {
		return ApplicationContextHelper.getUrlUtilsService().getSiteImagesPath();
	}

	public static String formatDate( final Date date ) {
		return ApplicationContextHelper.getDateUtilsService().formatDate( date );
	}

	public static String formatTime( final Date time ) {
		return ApplicationContextHelper.getDateUtilsService().formatTime( time );
	}

	public static String formatTimeShort( final Date time ) {
		return ApplicationContextHelper.getDateUtilsService().formatTimeShort( time );
	}

	public static String formatDateTimeShort( final Date time ) {
		return ApplicationContextHelper.getDateUtilsService().formatDateTimeShort( time );
	}

	public static float getFileSizeInKb( final float fileLength ) {
		return ApplicationContextHelper.getImageFileUtilsService().getFileSizeInKb( fileLength );
	}

	public static boolean contains( final Collection collection, final Object o ) {
		final PredicateUtilsService predicateUtilsService = ApplicationContextHelper.getBean( PredicateUtilsService.BEAN_NAME );
		return predicateUtilsService.contains( collection, o );
	}

	public static boolean contains( final Map map, final Object o ) {
		final PredicateUtilsService predicateUtilsService = ApplicationContextHelper.getBean( PredicateUtilsService.BEAN_NAME );
		return predicateUtilsService.contains( map, o );
	}

	public static String getUserAvatarFileUrl( final int userId ) {
		final UserPhotoFilePathUtilsService userPhotoFilePathUtilsService = ApplicationContextHelper.getBean( UserPhotoFilePathUtilsServiceImpl.BEAN_NAME );
		return userPhotoFilePathUtilsService.getUserAvatarFileUrl( userId );
	}

	public static String getUserAvatarImage( int userId, int width, int height, String imageId, String onClick, String cssStyle ) {
		final UserPhotoFilePathUtilsService userPhotoFilePathUtilsService = ApplicationContextHelper.getBean( UserPhotoFilePathUtilsServiceImpl.BEAN_NAME );
		try {
			return userPhotoFilePathUtilsService.getUserAvatarImage( userId, width, height, imageId, onClick, cssStyle );
		} catch ( final IOException e ) {
			new LogHelper( TLD_Utils.class ).error( String.format( "Error getting user avatar file. UserId = %d", userId ), e );
		}

		return String.format( "<img src='%s/ooops1.jpg' width='170' height='120'/>", ApplicationContextHelper.getUrlUtilsService().getSiteImagesPath() );
	}

	public static String getPhotosByUserByGenreLink( final User user, final Genre genre ) {
		return ApplicationContextHelper.getEntityLinkUtilsService().getPhotosByUserByGenreLink( user, genre );
	}
}
