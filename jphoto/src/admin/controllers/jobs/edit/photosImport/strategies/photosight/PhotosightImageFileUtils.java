package admin.controllers.jobs.edit.photosImport.strategies.photosight;

import admin.controllers.jobs.edit.photosImport.GenreDiscEntry;
import admin.controllers.jobs.edit.photosImport.ImageDiscEntry;
import core.log.LogHelper;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class PhotosightImageFileUtils {

	public static final String PHOTOSIGHT_LOCAL_IMAGE_DIR = "/home/blu"; // TODO:
	public static final String PHOTOSIGHT_HOST = "photosight.ru";
	private static List<PhotoCategoryMapping> photoCategoryMappings = newArrayList();

	private final static LogHelper log = new LogHelper( PhotosightImageFileUtils.class );

	static {
		photoCategoryMappings.add( new PhotoCategoryMapping( PhotosightCategory.NUDE, GenreDiscEntry.NUDE ) );
		photoCategoryMappings.add( new PhotoCategoryMapping( PhotosightCategory.GLAMOUR, GenreDiscEntry.GLAMOUR ) );
		photoCategoryMappings.add( new PhotoCategoryMapping( PhotosightCategory.CITY, GenreDiscEntry.CITY ) );
		photoCategoryMappings.add( new PhotoCategoryMapping( PhotosightCategory.CHILDREN, GenreDiscEntry.CHILDREN ) );
		photoCategoryMappings.add( new PhotoCategoryMapping( PhotosightCategory.GENRE, GenreDiscEntry.GENRE ) );
		photoCategoryMappings.add( new PhotoCategoryMapping( PhotosightCategory.GENRE_PORTRAIT, GenreDiscEntry.GENRE ) );
		photoCategoryMappings.add( new PhotoCategoryMapping( PhotosightCategory.ANIMALS, GenreDiscEntry.ANIMALS ) );
		photoCategoryMappings.add( new PhotoCategoryMapping( PhotosightCategory.DIGITAL_ART, GenreDiscEntry.DIGITAL_ART ) );
		photoCategoryMappings.add( new PhotoCategoryMapping( PhotosightCategory.MACRO, GenreDiscEntry.MACRO ) );
		photoCategoryMappings.add( new PhotoCategoryMapping( PhotosightCategory.STILL, GenreDiscEntry.STILL ) );
		photoCategoryMappings.add( new PhotoCategoryMapping( PhotosightCategory.LANDSCAPE, GenreDiscEntry.LANDSCAPE ) );
		photoCategoryMappings.add( new PhotoCategoryMapping( PhotosightCategory.UNDERWATER, GenreDiscEntry.UNDERWATER ) );
		photoCategoryMappings.add( new PhotoCategoryMapping( PhotosightCategory.PORTRAIT, GenreDiscEntry.PORTRAIT ) );
		photoCategoryMappings.add( new PhotoCategoryMapping( PhotosightCategory.TRAVELLING, GenreDiscEntry.TRAVELLING ) );
		photoCategoryMappings.add( new PhotoCategoryMapping( PhotosightCategory.ADVERTISING, GenreDiscEntry.ADVERTISING ) );
		photoCategoryMappings.add( new PhotoCategoryMapping( PhotosightCategory.REPORTING, GenreDiscEntry.REPORTING ) );
		photoCategoryMappings.add( new PhotoCategoryMapping( PhotosightCategory.WEDDING, GenreDiscEntry.WEDDING ) );
		photoCategoryMappings.add( new PhotoCategoryMapping( PhotosightCategory.SPORT, GenreDiscEntry.SPORT ) );
		photoCategoryMappings.add( new PhotoCategoryMapping( PhotosightCategory.PHOTOSIGHT, GenreDiscEntry.GENRE ) );
		photoCategoryMappings.add( new PhotoCategoryMapping( PhotosightCategory.PHOTO_HUNTING, GenreDiscEntry.ANIMALS ) );
		photoCategoryMappings.add( new PhotoCategoryMapping( PhotosightCategory.ARCHITECTURE, GenreDiscEntry.CITY ) );
		photoCategoryMappings.add( new PhotoCategoryMapping( PhotosightCategory.MODELS, GenreDiscEntry.MODELS ) );
		photoCategoryMappings.add( new PhotoCategoryMapping( PhotosightCategory.HUMOR, GenreDiscEntry.HUMOR ) );
		photoCategoryMappings.add( new PhotoCategoryMapping( PhotosightCategory.MOBILE_PHOTO, GenreDiscEntry.OTHER ) );
		photoCategoryMappings.add( new PhotoCategoryMapping( PhotosightCategory.MUSEUM, GenreDiscEntry.OTHER ) );
		photoCategoryMappings.add( new PhotoCategoryMapping( PhotosightCategory.NATURE, GenreDiscEntry.OTHER ) );
		photoCategoryMappings.add( new PhotoCategoryMapping( PhotosightCategory.TECH, GenreDiscEntry.OTHER ) );
		photoCategoryMappings.add( new PhotoCategoryMapping( PhotosightCategory.REST, GenreDiscEntry.OTHER ) );
		photoCategoryMappings.add( new PhotoCategoryMapping( PhotosightCategory.PAPARAZZI, GenreDiscEntry.OTHER ) );
	}

	public static void prepareUserGenreFolders( final PhotosightUser photosightUser, final List<PhotosightPhoto> photosightPhotos ) throws IOException {

		for ( final PhotosightPhoto photosightPhoto : photosightPhotos ) {
			final File userFolder = getUserFolderForPhotoDownloading( photosightUser );
			final GenreDiscEntry genreDiscEntry = getGenreDiscEntry( photosightPhoto.getPhotosightCategory() );

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
		if ( imageContent == null ) {
			return null;
		}
		final ImageDiscEntry imageDiscEntry = getImageFile( photosightPhoto, imageContent );

		log.debug( String.format( "Photo %s has been saved on disc: %s", photosightPhoto, imageDiscEntry.getImageFile().getCanonicalPath() ) );

		return imageDiscEntry;
	}

	private static ImageDiscEntry getImageFile( final PhotosightPhoto photosightPhoto, final String imageContent ) throws IOException {
		final PhotosightCategory category = photosightPhoto.getPhotosightCategory();

		final GenreDiscEntry genreDiscEntry = getGenreDiscEntry( category );

		final File imageFile = getPhotosightPhotoLocalImageFile( photosightPhoto );

		PhotosightXmlUtils.writeImageContentToFile( imageFile, imageContent, "ISO-8859-1" );

		return new ImageDiscEntry( imageFile, genreDiscEntry );
	}

	public static File getPhotosightPhotoLocalImageFile( final PhotosightPhoto photosightPhoto ) throws IOException {
		final String imageFileName = getPhotosightPhotoFileName( photosightPhoto.getPhotoId() );

		final GenreDiscEntry genreDiscEntry = getGenreDiscEntry( photosightPhoto.getPhotosightCategory() );
		final File userFolderForPhotoDownloading = getUserFolderForPhotoDownloading( photosightPhoto.getPhotosightUser() );
		final File imageFolder = new File( userFolderForPhotoDownloading, genreDiscEntry.getName() );
		return new File( imageFolder, imageFileName );
	}

	public static String getPhotosightPhotoFileName( final int photoId ) {
		return String.format( "%d.jpg", photoId );
	}

	public static GenreDiscEntry getGenreDiscEntry( final PhotosightCategory photosightCategory ) {
		for ( final PhotoCategoryMapping photoCategoryMapping : photoCategoryMappings ) {
			if ( photoCategoryMapping.getPhotosightCategory() == photosightCategory ) {
				return photoCategoryMapping.getGenreDiscEntry();
			}
		}

		log.warn( String.format( "Photosight category %s does not mach any genre", photosightCategory ) );

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

	static class PhotoCategoryMapping {
		private final PhotosightCategory photosightCategory;
		private final GenreDiscEntry genreDiscEntry;

		private PhotoCategoryMapping( final PhotosightCategory photosightCategory, final GenreDiscEntry genreDiscEntry ) {
			this.photosightCategory = photosightCategory;
			this.genreDiscEntry = genreDiscEntry;
		}

		public PhotosightCategory getPhotosightCategory() {
			return photosightCategory;
		}

		public GenreDiscEntry getGenreDiscEntry() {
			return genreDiscEntry;
		}
	}
}
