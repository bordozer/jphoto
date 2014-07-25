package admin.controllers.jobs.edit.photosImport.strategies.web;

import admin.controllers.jobs.edit.photosImport.GenreDiscEntry;
import admin.controllers.jobs.edit.photosImport.ImageDiscEntry;
import admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import core.log.LogHelper;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class RemotePhotoSitePhotoImageFileUtils {

	private final PhotosImportSource photosImportSource;
	private final File remotePhotoSitesCachePath;

	private final static LogHelper log = new LogHelper( RemotePhotoSitePhotoImageFileUtils.class );

	public RemotePhotoSitePhotoImageFileUtils( final PhotosImportSource photosImportSource, final File remotePhotoSitesCachePath ) {
		this.photosImportSource = photosImportSource;
		this.remotePhotoSitesCachePath = remotePhotoSitesCachePath;
	}

	public void prepareUserGenreFolders( final RemotePhotoSiteUser remotePhotoSiteUser, final List<RemotePhotoSitePhoto> remotePhotoSitePhotos ) throws IOException {

		for ( final RemotePhotoSitePhoto remotePhotoSitePhoto : remotePhotoSitePhotos ) {
			final File userFolder = getUserFolderForPhotoDownloading( remotePhotoSiteUser );
			final GenreDiscEntry genreDiscEntry = getGenreDiscEntry( remotePhotoSitePhoto.getRemotePhotoSiteCategory() );

			final File userGenrePath = new File( userFolder, genreDiscEntry.getName() );
			if ( ! userGenrePath.exists() ) {
				userGenrePath.mkdirs();
			}
		}
	}

	public ImageDiscEntry createRemotePhotoSiteDiskEntry( final RemotePhotoSitePhoto remotePhotoSitePhoto, final String imageContent ) throws IOException {

		final ImageDiscEntry imageDiscEntry = writeImageContentOnDiskAndReturnDiskEntry( remotePhotoSitePhoto, imageContent );

		log.debug( String.format( "Photo %s has been saved on disc: %s", remotePhotoSitePhoto, imageDiscEntry.getImageFile().getCanonicalPath() ) );

		return imageDiscEntry;
	}

	private ImageDiscEntry writeImageContentOnDiskAndReturnDiskEntry( final RemotePhotoSitePhoto remotePhotoSitePhoto, final String imageContent ) throws IOException {
		final RemotePhotoSiteCategory category = remotePhotoSitePhoto.getRemotePhotoSiteCategory();

		final GenreDiscEntry genreDiscEntry = getGenreDiscEntry( category );

		final File imageFile = getRemoteSitePhotoLocalImageFile( remotePhotoSitePhoto );

		RemotePhotoSiteCacheXmlUtils.writeImageContentToFile( imageFile, imageContent, "ISO-8859-1" );

		return new ImageDiscEntry( imageFile, genreDiscEntry );
	}

	public File getRemoteSitePhotoLocalImageFile( final RemotePhotoSitePhoto remotePhotoSitePhoto ) throws IOException {
		final String imageFileName = getRemoteSitePhotoFileName( remotePhotoSitePhoto.getPhotoId() );

		final GenreDiscEntry genreDiscEntry = getGenreDiscEntry( remotePhotoSitePhoto.getRemotePhotoSiteCategory() );
		final File userFolderForPhotoDownloading = getUserFolderForPhotoDownloading( remotePhotoSitePhoto.getRemotePhotoSiteUser() );
		final File imageFolder = new File( userFolderForPhotoDownloading, genreDiscEntry.getName() );
		return new File( imageFolder, imageFileName );
	}

	public static String getRemoteSitePhotoFileName( final int photoId ) {
		return String.format( "%d.jpg", photoId );
	}

	public static GenreDiscEntry getGenreDiscEntry( final RemotePhotoSiteCategory remotePhotoSiteCategory ) {
		for ( final RemotePhotoSiteCategoryToGenreMapping photoCategoryMapping : RemotePhotoSiteCategoryToGenreMapping.getRemotePhotoSiteCategoryToGenreMapping() ) {
			if ( photoCategoryMapping.getRemotePhotoSiteCategory() == remotePhotoSiteCategory ) {
				return photoCategoryMapping.getGenreDiscEntry();
			}
		}

		log.warn( String.format( "Photosight category %s does not mach any genre", remotePhotoSiteCategory ) );

		return GenreDiscEntry.OTHER;
	}

	public File getUserFolderForPhotoDownloading( final RemotePhotoSiteUser remotePhotoSiteUser ) throws IOException {
		final String userFolderName = String.format( "%s", remotePhotoSiteUser.getId() );
		return new File( getPhotoStorage().getPath(), userFolderName );
	}

	public File writePageContentToFile( final String fileName, final String content ) throws IOException {
		final String filePath = String.format( "%s/%s/%s.html", getPhotoStorage().getPath(), photosImportSource.getUrl(), fileName );
		final File file = new File( filePath );

		final PrintWriter writer = new PrintWriter( file, "UTF-8" );
		writer.println( content );
		writer.close();

		return file;
	}

	public void createUserFolderForPhotoDownloading( final RemotePhotoSiteUser remotePhotoSiteUser ) throws IOException {
		final File userFolder = getUserFolderForPhotoDownloading( remotePhotoSiteUser );
		if ( ! userFolder.exists() ) {
			userFolder.mkdirs();
		}
	}

	public File getPhotoStorage() {

		if ( ! remotePhotoSitesCachePath.exists() ) {
			remotePhotoSitesCachePath.mkdir();
		}

		final File remotePhotoSiteCacheFolder = new File( String.format( "%s/%s", remotePhotoSitesCachePath.getPath(), photosImportSource.getLocalStorageFolder() ) );

		if ( ! remotePhotoSiteCacheFolder.exists() ) {
			remotePhotoSiteCacheFolder.mkdir();
		}

		return remotePhotoSiteCacheFolder;
	}
}
