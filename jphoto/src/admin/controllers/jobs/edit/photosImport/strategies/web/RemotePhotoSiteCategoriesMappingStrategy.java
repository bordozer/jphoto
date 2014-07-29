package admin.controllers.jobs.edit.photosImport.strategies.web;

import admin.controllers.jobs.edit.photosImport.LocalCategory;
import admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import admin.controllers.jobs.edit.photosImport.strategies.web.naturelight.NaturelightCategory;
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
			case FILE_SYSTEM:
				return getLocalStrategy();
			case PHOTOSIGHT:
				return getPhotosightStrategy();
			case PHOTO35:
				return getPhoto35Strategy();
			case NATURELIGHT:
				return getNaturelightStrategy();
		}

		throw new IllegalArgumentException( String.format( "Illegal PhotosImportSource: '%s'", importSource ) );
	}

	public static RemotePhotoSiteCategoriesMappingStrategy getStrategyFor( final RemotePhotoSiteCategory remotePhotoSiteCategory ) {

		if ( remotePhotoSiteCategory instanceof LocalCategory ) {
			return getLocalStrategy();
		}

		if ( remotePhotoSiteCategory instanceof PhotosightCategory ) {
			return getPhotosightStrategy();
		}

		if ( remotePhotoSiteCategory instanceof Photo35Category ) {
			return getPhoto35Strategy();
		}

		if ( remotePhotoSiteCategory instanceof NaturelightCategory ) {
			return getNaturelightStrategy();
		}

		throw new IllegalArgumentException( String.format( "Unsupported remote photo site category class: '%s'", remotePhotoSiteCategory.getClass().getName() ) );
	}

	private static RemotePhotoSiteCategoriesMappingStrategy getLocalStrategy() {

		return new RemotePhotoSiteCategoriesMappingStrategy() {

			{
				for ( final LocalCategory localCategory : LocalCategory.values() ) {
					categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( localCategory, localCategory ) );
				}
			}
		};
	}

	private static RemotePhotoSiteCategoriesMappingStrategy getPhotosightStrategy() {

		return new RemotePhotoSiteCategoriesMappingStrategy() {

			{
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.NUDE, LocalCategory.NUDE ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.GLAMOUR, LocalCategory.GLAMOUR ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.CITY, LocalCategory.CITY ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.CHILDREN, LocalCategory.CHILDREN ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.GENRE, LocalCategory.GENRE ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.GENRE_PORTRAIT, LocalCategory.GENRE ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.ANIMALS, LocalCategory.ANIMALS ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.DIGITAL_ART, LocalCategory.DIGITAL_ART ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.MACRO, LocalCategory.MACRO ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.STILL, LocalCategory.STILL ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.LANDSCAPE, LocalCategory.LANDSCAPE ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.UNDERWATER, LocalCategory.UNDERWATER ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.PORTRAIT, LocalCategory.PORTRAIT ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.TRAVELLING, LocalCategory.TRAVELLING ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.ADVERTISING, LocalCategory.ADVERTISING ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.REPORTING, LocalCategory.REPORTING ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.WEDDING, LocalCategory.WEDDING ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.SPORT, LocalCategory.SPORT ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.PHOTOSIGHT, LocalCategory.GENRE ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.PHOTO_HUNTING, LocalCategory.ANIMALS ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.ARCHITECTURE, LocalCategory.CITY ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.MODELS, LocalCategory.MODELS ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.HUMOR, LocalCategory.HUMOR ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.MOBILE_PHOTO, LocalCategory.OTHER ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.MUSEUM, LocalCategory.OTHER ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.NATURE, LocalCategory.OTHER ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.TECH, LocalCategory.OTHER ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.REST, LocalCategory.OTHER ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( PhotosightCategory.PAPARAZZI, LocalCategory.OTHER ) );
			}
		};
	}

	private static RemotePhotoSiteCategoriesMappingStrategy getPhoto35Strategy() {

		return new RemotePhotoSiteCategoriesMappingStrategy() {

			{
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( Photo35Category.GLAMOUR, LocalCategory.GLAMOUR ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( Photo35Category.CITY, LocalCategory.CITY ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( Photo35Category.GENRE, LocalCategory.GENRE ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( Photo35Category.GENRE_PORTRAIT, LocalCategory.GENRE ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( Photo35Category.ANIMALS, LocalCategory.ANIMALS ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( Photo35Category.COLLAGE, LocalCategory.DIGITAL_ART ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( Photo35Category.MACRO, LocalCategory.MACRO ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( Photo35Category.STILL_LIFE, LocalCategory.STILL ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( Photo35Category.EROTICA, LocalCategory.NUDE ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( Photo35Category.OTHER, LocalCategory.OTHER ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( Photo35Category.LANDSCAPE, LocalCategory.LANDSCAPE ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( Photo35Category.UNDERWATER, LocalCategory.UNDERWATER ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( Photo35Category.PORTRAIT, LocalCategory.PORTRAIT ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( Photo35Category.NATURE, LocalCategory.ANIMALS ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( Photo35Category.TRAVEL, LocalCategory.TRAVELLING ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( Photo35Category.COMMERCIAL_PHOTOGRAPHY, LocalCategory.ADVERTISING ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( Photo35Category.REPORTAGE, LocalCategory.REPORTING ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( Photo35Category.SPORT, LocalCategory.SPORT ) );
			}
		};
	}

	private static RemotePhotoSiteCategoriesMappingStrategy getNaturelightStrategy() {

		return new RemotePhotoSiteCategoriesMappingStrategy() {
			{
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( NaturelightCategory.BIRDS, LocalCategory.ANIMALS ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( NaturelightCategory.MACRO, LocalCategory.MACRO ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( NaturelightCategory.LANDSCAPE, LocalCategory.LANDSCAPE ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( NaturelightCategory.CREEP, LocalCategory.ANIMALS ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( NaturelightCategory.MAMMAL, LocalCategory.ANIMALS ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( NaturelightCategory.FLORA, LocalCategory.OTHER ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( NaturelightCategory.HUMAN_AND_NATURE, LocalCategory.OTHER ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( NaturelightCategory.OTHER, LocalCategory.OTHER ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( NaturelightCategory.UNDERWATER, LocalCategory.UNDERWATER ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( NaturelightCategory.TRAVELLING, LocalCategory.TRAVELLING ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( NaturelightCategory.ARTHROPODA, LocalCategory.ANIMALS ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( NaturelightCategory.AMPHIBIA, LocalCategory.ANIMALS ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( NaturelightCategory.Action, LocalCategory.REPORTING ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( NaturelightCategory.APERIODICITIES, LocalCategory.ANIMALS ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( NaturelightCategory.UNDERWATER_MACRO, LocalCategory.MACRO ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( NaturelightCategory.ASTROPHOTOGRAPHY, LocalCategory.LANDSCAPE ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( NaturelightCategory.FACES, LocalCategory.GENRE ) );
				categoryToGenreMapping.add( new RemotePhotoSiteCategoryToGenreMapping( NaturelightCategory.TECH, LocalCategory.GENRE ) );
			}
		};
	}
}
