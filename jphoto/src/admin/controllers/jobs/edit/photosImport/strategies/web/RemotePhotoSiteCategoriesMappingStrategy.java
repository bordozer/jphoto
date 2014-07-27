package admin.controllers.jobs.edit.photosImport.strategies.web;

import admin.controllers.jobs.edit.photosImport.GenreDiscEntry;
import admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import admin.controllers.jobs.edit.photosImport.strategies.web.photos35.Photo35Category;
import admin.controllers.jobs.edit.photosImport.strategies.web.photosight.PhotosightCategory;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public abstract class RemotePhotoSiteCategoriesMappingStrategy {

	protected final List<RemotePhotoSiteCategoryToGenreMapping> categoryToGenreMapping = newArrayList();

	public List<RemotePhotoSiteCategoryToGenreMapping> getMapping() {
		return categoryToGenreMapping;
	}

	public static RemotePhotoSiteCategoriesMappingStrategy getStrategyFor( final PhotosImportSource importSource ) {
		switch ( importSource ) {
			case PHOTOSIGHT:
				return getPhotosightStrategy();
			case PHOTO35:
				return getPhoto35Strategy();
		}

		throw new IllegalArgumentException( String.format( "Illegal PhotosImportSource: '%s'", importSource ) );
	}

	private static RemotePhotoSiteCategoriesMappingStrategy getPhotosightStrategy() {

		return new RemotePhotoSiteCategoriesMappingStrategy() {

			{
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.NUDE, GenreDiscEntry.NUDE ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.GLAMOUR, GenreDiscEntry.GLAMOUR ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.CITY, GenreDiscEntry.CITY ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.CHILDREN, GenreDiscEntry.CHILDREN ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.GENRE, GenreDiscEntry.GENRE ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.GENRE_PORTRAIT, GenreDiscEntry.GENRE ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.ANIMALS, GenreDiscEntry.ANIMALS ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.DIGITAL_ART, GenreDiscEntry.DIGITAL_ART ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.MACRO, GenreDiscEntry.MACRO ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.STILL, GenreDiscEntry.STILL ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.LANDSCAPE, GenreDiscEntry.LANDSCAPE ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.UNDERWATER, GenreDiscEntry.UNDERWATER ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.PORTRAIT, GenreDiscEntry.PORTRAIT ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.TRAVELLING, GenreDiscEntry.TRAVELLING ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.ADVERTISING, GenreDiscEntry.ADVERTISING ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.REPORTING, GenreDiscEntry.REPORTING ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.WEDDING, GenreDiscEntry.WEDDING ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.SPORT, GenreDiscEntry.SPORT ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.PHOTOSIGHT, GenreDiscEntry.GENRE ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.PHOTO_HUNTING, GenreDiscEntry.ANIMALS ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.ARCHITECTURE, GenreDiscEntry.CITY ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.MODELS, GenreDiscEntry.MODELS ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.HUMOR, GenreDiscEntry.HUMOR ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.MOBILE_PHOTO, GenreDiscEntry.OTHER ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.MUSEUM, GenreDiscEntry.OTHER ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.NATURE, GenreDiscEntry.OTHER ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.TECH, GenreDiscEntry.OTHER ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.REST, GenreDiscEntry.OTHER ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.PAPARAZZI, GenreDiscEntry.OTHER ) );
			}
		};
	}

	private static RemotePhotoSiteCategoriesMappingStrategy getPhoto35Strategy() {

		return new RemotePhotoSiteCategoriesMappingStrategy() {

			{
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( Photo35Category.PORTRAIT, GenreDiscEntry.PORTRAIT ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( Photo35Category.EROTIC_GLAMOUR, GenreDiscEntry.GLAMOUR ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( Photo35Category.NATURE, GenreDiscEntry.ANIMALS ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( Photo35Category.LANDSCAPES, GenreDiscEntry.LANDSCAPE ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( Photo35Category.OTHER, GenreDiscEntry.OTHER ) );
			}
		};
	}
}
