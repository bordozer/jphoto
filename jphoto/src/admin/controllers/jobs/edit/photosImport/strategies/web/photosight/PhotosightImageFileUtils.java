package admin.controllers.jobs.edit.photosImport.strategies.web.photosight;

import admin.controllers.jobs.edit.photosImport.GenreDiscEntry;
import admin.controllers.jobs.edit.photosImport.ImageDiscEntry;
import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategory;
import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSitePhoto;
import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteUser;
import core.log.LogHelper;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class PhotosightImageFileUtils {

	public static final String PHOTOSIGHT_LOCAL_IMAGE_DIR = "/home/blu"; // TODO:
	public static final String PHOTOSIGHT_HOST = "photosight.ru";

	private final static LogHelper log = new LogHelper( PhotosightImageFileUtils.class );

	public static void prepareUserGenreFolders( final RemotePhotoSiteUser remotePhotoSiteUser, final List<RemotePhotoSitePhoto> remotePhotoSitePhotos ) throws IOException {

		for ( final RemotePhotoSitePhoto remotePhotoSitePhoto : remotePhotoSitePhotos ) {
			final File userFolder = getUserFolderForPhotoDownloading( remotePhotoSiteUser );
			final GenreDiscEntry genreDiscEntry = getGenreDiscEntry( remotePhotoSitePhoto.getRemotePhotoSiteCategory() );

			final File userGenrePath = new File( userFolder, genreDiscEntry.getName() );
			if ( ! userGenrePath.exists() ) {
				userGenrePath.mkdirs();
			}
		}
	}

	public static ImageDiscEntry downloadPhotoOnDisk( final RemotePhotoSitePhoto remotePhotoSitePhoto, final String imageContent ) throws IOException {

		if ( imageContent == null ) {
			return null;
		}

		final ImageDiscEntry imageDiscEntry = getImageFile( remotePhotoSitePhoto, imageContent );

		log.debug( String.format( "Photo %s has been saved on disc: %s", remotePhotoSitePhoto, imageDiscEntry.getImageFile().getCanonicalPath() ) );

		return imageDiscEntry;
	}

	private static ImageDiscEntry getImageFile( final RemotePhotoSitePhoto remotePhotoSitePhoto, final String imageContent ) throws IOException {
		final RemotePhotoSiteCategory category = remotePhotoSitePhoto.getRemotePhotoSiteCategory();

		final GenreDiscEntry genreDiscEntry = getGenreDiscEntry( category );

		final File imageFile = getPhotosightPhotoLocalImageFile( remotePhotoSitePhoto );

		PhotosightXmlUtils.writeImageContentToFile( imageFile, imageContent, "ISO-8859-1" );

		return new ImageDiscEntry( imageFile, genreDiscEntry );
	}

	public static File getPhotosightPhotoLocalImageFile( final RemotePhotoSitePhoto remotePhotoSitePhoto ) throws IOException {
		final String imageFileName = getPhotosightPhotoFileName( remotePhotoSitePhoto.getPhotoId() );

		final GenreDiscEntry genreDiscEntry = getGenreDiscEntry( remotePhotoSitePhoto.getRemotePhotoSiteCategory() );
		final File userFolderForPhotoDownloading = getUserFolderForPhotoDownloading( remotePhotoSitePhoto.getRemotePhotoSiteUser() );
		final File imageFolder = new File( userFolderForPhotoDownloading, genreDiscEntry.getName() );
		return new File( imageFolder, imageFileName );
	}

	public static String getPhotosightPhotoFileName( final int photoId ) {
		return String.format( "%d.jpg", photoId );
	}

	public static GenreDiscEntry getGenreDiscEntry( final RemotePhotoSiteCategory remotePhotoSiteCategory ) {
		for ( final PhotosightCategoryToGenreMapping photoCategoryMapping : PhotosightCategoryToGenreMapping.PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING ) {
			if ( photoCategoryMapping.getRemotePhotoSiteCategory() == remotePhotoSiteCategory ) {
				return photoCategoryMapping.getGenreDiscEntry();
			}
		}

		log.warn( String.format( "Photosight category %s does not mach any genre", remotePhotoSiteCategory ) );

		return GenreDiscEntry.OTHER;
	}

	public static File getUserFolderForPhotoDownloading( final RemotePhotoSiteUser remotePhotoSiteUser ) throws IOException {
		final String userFolderName = String.format( "%s", remotePhotoSiteUser.getId() );
		return new File( getPhotoStorage().getPath(), userFolderName );
	}

	public static File getPhotoStorage() {
		return new File( PHOTOSIGHT_LOCAL_IMAGE_DIR, PHOTOSIGHT_HOST );
	}

	public static File writePageContentToFile( final String fileName, final String content ) throws IOException {
		final String filePath = String.format( "%s/%s/%s.html", PHOTOSIGHT_LOCAL_IMAGE_DIR, PHOTOSIGHT_HOST, fileName );
		final File file = new File( filePath );

		final PrintWriter writer = new PrintWriter( file, "UTF-8" );
		writer.println( content );
		writer.close();

		return file;
	}

	public static void createUserFolderForPhotoDownloading( final RemotePhotoSiteUser remotePhotoSiteUser ) throws IOException {
		final File userFolder = getUserFolderForPhotoDownloading( remotePhotoSiteUser );
		if ( ! userFolder.exists() ) {
			userFolder.mkdirs();
		}
	}

}
