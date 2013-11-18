package admin.controllers.jobs.edit.photosImport.strategies.photosight;

import admin.controllers.jobs.edit.photosImport.GenreDiscEntry;
import admin.controllers.jobs.edit.photosImport.ImageDiscEntry;
import core.log.LogHelper;

import java.io.*;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class PhotosightImageFileUtils {

	public static final String PHOTOSIGHT_LOCAL_IMAGE_DIR = "/home/blu"; // TODO:
	public static final String PHOTOSIGHT_HOST = "photosight.ru";
	private static List<CategoryPair> categoryPairs = newArrayList();

	private final static LogHelper log = new LogHelper( PhotosightImageFileUtils.class );

	static {
		categoryPairs.add( new CategoryPair( 15, GenreDiscEntry.NUDE ) );		// Nude
		categoryPairs.add( new CategoryPair( 18, GenreDiscEntry.GLAMOUR ) );	// Glamour
		categoryPairs.add( new CategoryPair( 7, GenreDiscEntry.CITY ) );		// City
		categoryPairs.add( new CategoryPair( 80, GenreDiscEntry.CHILDREN ) );	// Children
		categoryPairs.add( new CategoryPair( 12, GenreDiscEntry.GENRE ) );		// Genre
		categoryPairs.add( new CategoryPair( 70, GenreDiscEntry.GENRE ) );		// Genre portrait
		categoryPairs.add( new CategoryPair( 8, GenreDiscEntry.ANIMALS ) );		// Animals
		categoryPairs.add( new CategoryPair( 14, GenreDiscEntry.DIGITAL_ART ) );// Digital art
		categoryPairs.add( new CategoryPair( 5, GenreDiscEntry.MACRO ) ); 		// Macro
		categoryPairs.add( new CategoryPair( 78, GenreDiscEntry.OTHER ) );		// Mobile photo
		categoryPairs.add( new CategoryPair( 4, GenreDiscEntry.STILL ) );		// Still
		categoryPairs.add( new CategoryPair( 36, GenreDiscEntry.LANDSCAPE ) );	// Landscape
		categoryPairs.add( new CategoryPair( 65, GenreDiscEntry.UNDERWATER ) );	// Underwater
		categoryPairs.add( new CategoryPair( 2, GenreDiscEntry.PORTRAIT ) );	// Portrait
		categoryPairs.add( new CategoryPair( 6, GenreDiscEntry.TRAVELLING ) );	// Travelling
		categoryPairs.add( new CategoryPair( 19, GenreDiscEntry.ADVERTISING ) );// Advertising
		categoryPairs.add( new CategoryPair( 9, GenreDiscEntry.REPORTING ) );	// Reporting
		categoryPairs.add( new CategoryPair( 91, GenreDiscEntry.WEDDING ) );	// Wedding
		categoryPairs.add( new CategoryPair( 10, GenreDiscEntry.SPORT ) );		// Sport
		categoryPairs.add( new CategoryPair( 17, GenreDiscEntry.GENRE ) );		// Photosight
		categoryPairs.add( new CategoryPair( 64, GenreDiscEntry.ANIMALS ) );	// Photo hunting
		categoryPairs.add( new CategoryPair( 82, GenreDiscEntry.CITY ) );		// Architecture
		categoryPairs.add( new CategoryPair( 13, GenreDiscEntry.OTHER ) );		// Museum
		categoryPairs.add( new CategoryPair( 3, GenreDiscEntry.OTHER ) );		// Nature
		categoryPairs.add( new CategoryPair( 92, GenreDiscEntry.OTHER ) );		// Tech
		categoryPairs.add( new CategoryPair( 87, GenreDiscEntry.MODELS ) );		// Models
		categoryPairs.add( new CategoryPair( 11, GenreDiscEntry.HUMOR ) );		// humor - hidden on photosight in new version
		categoryPairs.add( new CategoryPair( 16, GenreDiscEntry.OTHER ) );		// the rest - hidden on photosight in new version
		categoryPairs.add( new CategoryPair( 27, GenreDiscEntry.OTHER ) );		// paparazzi - hidden on photosight in new version
	}

	public static void prepareUserGenreFolders( final PhotosightUser photosightUser, final List<PhotosightPhoto> photosightPhotos ) throws IOException {

		for ( final PhotosightPhoto photosightPhoto : photosightPhotos ) {
			final File userFolder = getUserFolderForPhotoDownloading( photosightUser );
			final GenreDiscEntry genreDiscEntry = getGenreDiscEntry( photosightPhoto.getPhotosightCategoryId() );

			final File userGenrePath = new File( userFolder, genreDiscEntry.getName() );
			if ( ! userGenrePath.exists() ) {
				userGenrePath.mkdirs();
			}
		}
	}

	public static ImageDiscEntry downloadPhotosightPhotoOnDisk( final PhotosightPhoto photosightPhoto ) throws IOException {

		final String imageUrl = photosightPhoto.getImageUrl();

		log.debug( String.format( "Getting photo %s content", imageUrl ) );

		final String imageContent = PhotosightRemoteContentHelper.getImageContentFromUrl( imageUrl );
		final ImageDiscEntry imageDiscEntry = getImageFile( photosightPhoto, imageContent );

		log.debug( String.format( "Photo %s has been saved on disc: %s", photosightPhoto, imageDiscEntry.getImageFile().getCanonicalPath() ) );

		return imageDiscEntry;
	}

	private static ImageDiscEntry getImageFile( final PhotosightPhoto photosightPhoto, final String imageContent ) throws IOException {
		final PhotosightUser photosightUser = photosightPhoto.getPhotosightUser();
		final int categoryId = photosightPhoto.getPhotosightCategoryId();

		final GenreDiscEntry genreDiscEntry = getGenreDiscEntry( categoryId );

		final File imageFile = getPhotosightPhotoLocalImageFile( photosightPhoto );

		PhotosightXmlUtils.writeImageContentToFile( imageFile, imageContent, "ISO-8859-1" );

		return new ImageDiscEntry( imageFile, genreDiscEntry );
	}

	public static File getPhotosightPhotoLocalImageFile( final PhotosightPhoto photosightPhoto ) throws IOException {
		final String imageFileName = getPhotosightPhotoFileName( photosightPhoto.getPhotoId() );

		final GenreDiscEntry genreDiscEntry = getGenreDiscEntry( photosightPhoto.getPhotosightCategoryId() );
		final File userFolderForPhotoDownloading = getUserFolderForPhotoDownloading( photosightPhoto.getPhotosightUser() );
		final File imageFolder = new File( userFolderForPhotoDownloading, genreDiscEntry.getName() );
		return new File( imageFolder, imageFileName );
	}

	public static String getPhotosightPhotoFileName( final int photoId ) {
		return String.format( "%d.jpg", photoId );
	}

	public static GenreDiscEntry getGenreDiscEntry( final int photosightCategoryId ) {
		for ( final CategoryPair categoryPair : categoryPairs ) {
			if ( categoryPair.getPhotosightCategoryId() == photosightCategoryId ) {
				return categoryPair.getGenreDiscEntry();
			}
		}

		log.warn( String.format( "Photosight category id %d does not mach any genre", photosightCategoryId ) );

		return GenreDiscEntry.OTHER;
	}

	public static File getUserFolderForPhotoDownloading( final PhotosightUser photosightUser ) throws IOException {
		final String userFolderName = String.format( "%d", photosightUser.getId() );
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

	public static void createUserFolderForPhotoDownloading( final PhotosightUser photosightUser ) throws IOException {
		final File userFolder = getUserFolderForPhotoDownloading( photosightUser );
		if ( ! userFolder.exists() ) {
			userFolder.mkdirs();
		}
	}

	static class CategoryPair {
		private final int photosightCategoryId;
		private final GenreDiscEntry genreDiscEntry;

		private CategoryPair( final int photosightCategoryId, final GenreDiscEntry genreDiscEntry ) {
			this.photosightCategoryId = photosightCategoryId;
			this.genreDiscEntry = genreDiscEntry;
		}

		public int getPhotosightCategoryId() {
			return photosightCategoryId;
		}

		public GenreDiscEntry getGenreDiscEntry() {
			return genreDiscEntry;
		}
	}

	/*public static boolean isPhotosightPhotoAlreadyProcessed( final PhotosightUser photosightUser, final int photosightPhotoId ) throws IOException {
		final File userFolderForPhotoDownloading = getUserFolderForPhotoDownloading( photosightUser );
		final String imageFileName = getPhotosightPhotoFileName( photosightPhotoId );

		final File[] genreDirList = userFolderForPhotoDownloading.listFiles( PredicateUtilsService.getDirFilter() );

		if ( genreDirList == null ) {
			return false;
		}

		for ( File genreDir : genreDirList ) {
			final File[] files = genreDir.listFiles( PredicateUtilsService.getFileFilter() );

			if ( files == null ) {
				continue;
			}

			for ( File file : files ) {
				if ( file.getName().equals( imageFileName ) ) {
					return true;
				}
			}
		}
		return false;
	}*/
}
