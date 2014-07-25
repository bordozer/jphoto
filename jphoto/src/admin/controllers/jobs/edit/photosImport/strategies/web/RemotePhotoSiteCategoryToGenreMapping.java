package admin.controllers.jobs.edit.photosImport.strategies.web;

import admin.controllers.jobs.edit.photosImport.GenreDiscEntry;
import admin.controllers.jobs.edit.photosImport.strategies.web.photosight.PhotosightCategory;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class RemotePhotoSiteCategoryToGenreMapping {

	private final static List<RemotePhotoSiteCategoryToGenreMapping> PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING = newArrayList();

	private final PhotosightCategory photosightCategory;
	private final GenreDiscEntry genreDiscEntry;

	static {
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.NUDE, GenreDiscEntry.NUDE ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.GLAMOUR, GenreDiscEntry.GLAMOUR ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.CITY, GenreDiscEntry.CITY ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.CHILDREN, GenreDiscEntry.CHILDREN ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.GENRE, GenreDiscEntry.GENRE ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.GENRE_PORTRAIT, GenreDiscEntry.GENRE ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.ANIMALS, GenreDiscEntry.ANIMALS ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.DIGITAL_ART, GenreDiscEntry.DIGITAL_ART ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.MACRO, GenreDiscEntry.MACRO ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.STILL, GenreDiscEntry.STILL ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.LANDSCAPE, GenreDiscEntry.LANDSCAPE ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.UNDERWATER, GenreDiscEntry.UNDERWATER ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.PORTRAIT, GenreDiscEntry.PORTRAIT ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.TRAVELLING, GenreDiscEntry.TRAVELLING ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.ADVERTISING, GenreDiscEntry.ADVERTISING ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.REPORTING, GenreDiscEntry.REPORTING ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.WEDDING, GenreDiscEntry.WEDDING ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.SPORT, GenreDiscEntry.SPORT ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.PHOTOSIGHT, GenreDiscEntry.GENRE ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.PHOTO_HUNTING, GenreDiscEntry.ANIMALS ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.ARCHITECTURE, GenreDiscEntry.CITY ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.MODELS, GenreDiscEntry.MODELS ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.HUMOR, GenreDiscEntry.HUMOR ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.MOBILE_PHOTO, GenreDiscEntry.OTHER ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.MUSEUM, GenreDiscEntry.OTHER ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.NATURE, GenreDiscEntry.OTHER ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.TECH, GenreDiscEntry.OTHER ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.REST, GenreDiscEntry.OTHER ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.PAPARAZZI, GenreDiscEntry.OTHER ) );
	}

	public static List<RemotePhotoSiteCategoryToGenreMapping> getRemotePhotoSiteCategoryToGenreMapping() {
		return PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING;
	}

	public RemotePhotoSiteCategoryToGenreMapping( final PhotosightCategory photosightCategory, final GenreDiscEntry genreDiscEntry ) {
		this.photosightCategory = photosightCategory;
		this.genreDiscEntry = genreDiscEntry;
	}

	public PhotosightCategory getPhotosightCategory() {
		return photosightCategory;
	}

	public GenreDiscEntry getGenreDiscEntry() {
		return genreDiscEntry;
	}

	@Override
	public String toString() {
		return String.format( "%s => %s", photosightCategory, genreDiscEntry.getName() );
	}
}
