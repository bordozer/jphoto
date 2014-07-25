package admin.controllers.jobs.edit.photosImport.strategies.web.photosight;

import admin.controllers.jobs.edit.photosImport.GenreDiscEntry;
import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategory;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class PhotosightCategoryToGenreMapping {

	public static List<PhotosightCategoryToGenreMapping> PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING = newArrayList();

	private final RemotePhotoSiteCategory remotePhotoSiteCategory;
	private final GenreDiscEntry genreDiscEntry;

	static {
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( RemotePhotoSiteCategory.NUDE, GenreDiscEntry.NUDE ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( RemotePhotoSiteCategory.GLAMOUR, GenreDiscEntry.GLAMOUR ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( RemotePhotoSiteCategory.CITY, GenreDiscEntry.CITY ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( RemotePhotoSiteCategory.CHILDREN, GenreDiscEntry.CHILDREN ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( RemotePhotoSiteCategory.GENRE, GenreDiscEntry.GENRE ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( RemotePhotoSiteCategory.GENRE_PORTRAIT, GenreDiscEntry.GENRE ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( RemotePhotoSiteCategory.ANIMALS, GenreDiscEntry.ANIMALS ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( RemotePhotoSiteCategory.DIGITAL_ART, GenreDiscEntry.DIGITAL_ART ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( RemotePhotoSiteCategory.MACRO, GenreDiscEntry.MACRO ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( RemotePhotoSiteCategory.STILL, GenreDiscEntry.STILL ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( RemotePhotoSiteCategory.LANDSCAPE, GenreDiscEntry.LANDSCAPE ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( RemotePhotoSiteCategory.UNDERWATER, GenreDiscEntry.UNDERWATER ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( RemotePhotoSiteCategory.PORTRAIT, GenreDiscEntry.PORTRAIT ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( RemotePhotoSiteCategory.TRAVELLING, GenreDiscEntry.TRAVELLING ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( RemotePhotoSiteCategory.ADVERTISING, GenreDiscEntry.ADVERTISING ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( RemotePhotoSiteCategory.REPORTING, GenreDiscEntry.REPORTING ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( RemotePhotoSiteCategory.WEDDING, GenreDiscEntry.WEDDING ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( RemotePhotoSiteCategory.SPORT, GenreDiscEntry.SPORT ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( RemotePhotoSiteCategory.PHOTOSIGHT, GenreDiscEntry.GENRE ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( RemotePhotoSiteCategory.PHOTO_HUNTING, GenreDiscEntry.ANIMALS ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( RemotePhotoSiteCategory.ARCHITECTURE, GenreDiscEntry.CITY ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( RemotePhotoSiteCategory.MODELS, GenreDiscEntry.MODELS ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( RemotePhotoSiteCategory.HUMOR, GenreDiscEntry.HUMOR ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( RemotePhotoSiteCategory.MOBILE_PHOTO, GenreDiscEntry.OTHER ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( RemotePhotoSiteCategory.MUSEUM, GenreDiscEntry.OTHER ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( RemotePhotoSiteCategory.NATURE, GenreDiscEntry.OTHER ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( RemotePhotoSiteCategory.TECH, GenreDiscEntry.OTHER ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( RemotePhotoSiteCategory.REST, GenreDiscEntry.OTHER ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new PhotosightCategoryToGenreMapping( RemotePhotoSiteCategory.PAPARAZZI, GenreDiscEntry.OTHER ) );
	}

	public PhotosightCategoryToGenreMapping( final RemotePhotoSiteCategory remotePhotoSiteCategory, final GenreDiscEntry genreDiscEntry ) {
		this.remotePhotoSiteCategory = remotePhotoSiteCategory;
		this.genreDiscEntry = genreDiscEntry;
	}

	public RemotePhotoSiteCategory getRemotePhotoSiteCategory() {
		return remotePhotoSiteCategory;
	}

	public GenreDiscEntry getGenreDiscEntry() {
		return genreDiscEntry;
	}

	@Override
	public String toString() {
		return String.format( "%s => %s", remotePhotoSiteCategory, genreDiscEntry.getName() );
	}
}
