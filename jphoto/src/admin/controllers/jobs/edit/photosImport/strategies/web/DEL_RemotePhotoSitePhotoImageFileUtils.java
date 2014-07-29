package admin.controllers.jobs.edit.photosImport.strategies.web;

import admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import core.log.LogHelper;
import core.services.remotePhotoSite.RemotePhotoCategoryService;

import java.io.File;

public class DEL_RemotePhotoSitePhotoImageFileUtils {

	private final PhotosImportSource photosImportSource;
	private final File remotePhotoSitesCachePath;
	private final RemotePhotoCategoryService remotePhotoCategoryService;

	private final static LogHelper log = new LogHelper( DEL_RemotePhotoSitePhotoImageFileUtils.class );

	public DEL_RemotePhotoSitePhotoImageFileUtils( final PhotosImportSource photosImportSource, final File remotePhotoSitesCachePath, final RemotePhotoCategoryService remotePhotoCategoryService ) {
		this.photosImportSource = photosImportSource;
		this.remotePhotoSitesCachePath = remotePhotoSitesCachePath;
		this.remotePhotoCategoryService = remotePhotoCategoryService;
	}

	/*public void prepareUserGenreFolders( final RemotePhotoSiteUser remotePhotoSiteUser, final List<RemotePhotoSitePhoto> remotePhotoSitePhotos ) throws IOException {

		final File userFolder = getUserFolderForPhotoDownloading( remotePhotoSiteUser );

		for ( final RemotePhotoSitePhoto remotePhotoSitePhoto : remotePhotoSitePhotos ) {
			final GenreDiscEntry genreDiscEntry = remotePhotoCategoryService.getGenreDiscEntryOrOther( remotePhotoSitePhoto.getRemotePhotoSiteCategory() );

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

	public File getRemoteSitePhotoLocalImageFile( final RemotePhotoSitePhoto remotePhotoSitePhoto ) throws IOException {
		final String imageFileName = getRemoteSitePhotoFileName( remotePhotoSitePhoto );

		final GenreDiscEntry genreDiscEntry = remotePhotoCategoryService.getGenreDiscEntryOrOther( remotePhotoSitePhoto.getRemotePhotoSiteCategory() );
		final File userFolderForPhotoDownloading = getUserFolderForPhotoDownloading( remotePhotoSitePhoto.getRemotePhotoSiteUser() );
		final File imageFolder = new File( userFolderForPhotoDownloading, genreDiscEntry.getName() );
		return new File( imageFolder, imageFileName );
	}

	private ImageDiscEntry writeImageContentOnDiskAndReturnDiskEntry( final RemotePhotoSitePhoto remotePhotoSitePhoto, final String imageContent ) throws IOException {
		final RemotePhotoSiteCategory category = remotePhotoSitePhoto.getRemotePhotoSiteCategory();

		final GenreDiscEntry genreDiscEntry = remotePhotoCategoryService.getGenreDiscEntryOrOther( category );

		final File imageFile = getRemoteSitePhotoLocalImageFile( remotePhotoSitePhoto );

		RemotePhotoSiteCacheXmlUtils.writeImageContentToFile( imageFile, imageContent, "ISO-8859-1" );

		return new ImageDiscEntry( imageFile, genreDiscEntry );
	}

	public static String getRemoteSitePhotoFileName( final RemotePhotoSitePhoto remotePhotoSitePhoto ) {
		return String.format( "%d_%d.jpg", remotePhotoSitePhoto.getPhotoId(), remotePhotoSitePhoto.getNumberInSeries() );
	}

	public File getUserFolderForPhotoDownloading( final RemotePhotoSiteUser remotePhotoSiteUser ) throws IOException {
		final String userFolderName = String.format( "%s", remotePhotoSiteUser.getId() );
		return new File( getPhotoStorage().getPath(), userFolderName );
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
	}*/
}
