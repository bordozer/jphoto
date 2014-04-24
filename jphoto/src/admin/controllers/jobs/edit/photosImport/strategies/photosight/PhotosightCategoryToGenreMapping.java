package admin.controllers.jobs.edit.photosImport.strategies.photosight;

import admin.controllers.jobs.edit.photosImport.GenreDiscEntry;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class PhotosightCategoryToGenreMapping {

	public static List<PhotosightCategoryToGenreMapping> PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING = newArrayList();

	private final PhotosightCategory photosightCategory;
	private final GenreDiscEntry genreDiscEntry;

	static {
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( PhotosightCategory.NUDE, GenreDiscEntry.NUDE ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( PhotosightCategory.GLAMOUR, GenreDiscEntry.GLAMOUR ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( PhotosightCategory.CITY, GenreDiscEntry.CITY ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( PhotosightCategory.CHILDREN, GenreDiscEntry.CHILDREN ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( PhotosightCategory.GENRE, GenreDiscEntry.GENRE ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( PhotosightCategory.GENRE_PORTRAIT, GenreDiscEntry.GENRE ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( PhotosightCategory.ANIMALS, GenreDiscEntry.ANIMALS ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( PhotosightCategory.DIGITAL_ART, GenreDiscEntry.DIGITAL_ART ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( PhotosightCategory.MACRO, GenreDiscEntry.MACRO ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( PhotosightCategory.STILL, GenreDiscEntry.STILL ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( PhotosightCategory.LANDSCAPE, GenreDiscEntry.LANDSCAPE ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( PhotosightCategory.UNDERWATER, GenreDiscEntry.UNDERWATER ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( PhotosightCategory.PORTRAIT, GenreDiscEntry.PORTRAIT ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( PhotosightCategory.TRAVELLING, GenreDiscEntry.TRAVELLING ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( PhotosightCategory.ADVERTISING, GenreDiscEntry.ADVERTISING ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( PhotosightCategory.REPORTING, GenreDiscEntry.REPORTING ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( PhotosightCategory.WEDDING, GenreDiscEntry.WEDDING ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( PhotosightCategory.SPORT, GenreDiscEntry.SPORT ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( PhotosightCategory.PHOTOSIGHT, GenreDiscEntry.GENRE ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( PhotosightCategory.PHOTO_HUNTING, GenreDiscEntry.ANIMALS ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( PhotosightCategory.ARCHITECTURE, GenreDiscEntry.CITY ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( PhotosightCategory.MODELS, GenreDiscEntry.MODELS ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( PhotosightCategory.HUMOR, GenreDiscEntry.HUMOR ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( PhotosightCategory.MOBILE_PHOTO, GenreDiscEntry.OTHER ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( PhotosightCategory.MUSEUM, GenreDiscEntry.OTHER ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( PhotosightCategory.NATURE, GenreDiscEntry.OTHER ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( PhotosightCategory.TECH, GenreDiscEntry.OTHER ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( PhotosightCategory.REST, GenreDiscEntry.OTHER ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( PhotosightCategory.PAPARAZZI, GenreDiscEntry.OTHER ) );
	}

	public PhotosightCategoryToGenreMapping( final PhotosightCategory photosightCategory, final GenreDiscEntry genreDiscEntry ) {
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
