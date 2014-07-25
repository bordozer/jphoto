package admin.controllers.jobs.edit.photosImport.strategies.web;

import admin.controllers.jobs.edit.photosImport.GenreDiscEntry;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class RemotePhotoSiteCategoryToGenreMapping {

	private final static List<RemotePhotoSiteCategoryToGenreMapping> PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING = newArrayList();

	private final RemotePhotoSiteCategory remotePhotoSiteCategory;
	private final GenreDiscEntry genreDiscEntry;

	static {
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( RemotePhotoSiteCategory.NUDE, GenreDiscEntry.NUDE ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( RemotePhotoSiteCategory.GLAMOUR, GenreDiscEntry.GLAMOUR ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( RemotePhotoSiteCategory.CITY, GenreDiscEntry.CITY ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( RemotePhotoSiteCategory.CHILDREN, GenreDiscEntry.CHILDREN ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( RemotePhotoSiteCategory.GENRE, GenreDiscEntry.GENRE ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( RemotePhotoSiteCategory.GENRE_PORTRAIT, GenreDiscEntry.GENRE ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( RemotePhotoSiteCategory.ANIMALS, GenreDiscEntry.ANIMALS ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( RemotePhotoSiteCategory.DIGITAL_ART, GenreDiscEntry.DIGITAL_ART ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( RemotePhotoSiteCategory.MACRO, GenreDiscEntry.MACRO ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( RemotePhotoSiteCategory.STILL, GenreDiscEntry.STILL ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( RemotePhotoSiteCategory.LANDSCAPE, GenreDiscEntry.LANDSCAPE ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( RemotePhotoSiteCategory.UNDERWATER, GenreDiscEntry.UNDERWATER ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( RemotePhotoSiteCategory.PORTRAIT, GenreDiscEntry.PORTRAIT ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( RemotePhotoSiteCategory.TRAVELLING, GenreDiscEntry.TRAVELLING ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( RemotePhotoSiteCategory.ADVERTISING, GenreDiscEntry.ADVERTISING ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( RemotePhotoSiteCategory.REPORTING, GenreDiscEntry.REPORTING ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( RemotePhotoSiteCategory.WEDDING, GenreDiscEntry.WEDDING ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( RemotePhotoSiteCategory.SPORT, GenreDiscEntry.SPORT ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( RemotePhotoSiteCategory.PHOTOSIGHT, GenreDiscEntry.GENRE ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( RemotePhotoSiteCategory.PHOTO_HUNTING, GenreDiscEntry.ANIMALS ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( RemotePhotoSiteCategory.ARCHITECTURE, GenreDiscEntry.CITY ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( RemotePhotoSiteCategory.MODELS, GenreDiscEntry.MODELS ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( RemotePhotoSiteCategory.HUMOR, GenreDiscEntry.HUMOR ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( RemotePhotoSiteCategory.MOBILE_PHOTO, GenreDiscEntry.OTHER ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( RemotePhotoSiteCategory.MUSEUM, GenreDiscEntry.OTHER ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( RemotePhotoSiteCategory.NATURE, GenreDiscEntry.OTHER ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( RemotePhotoSiteCategory.TECH, GenreDiscEntry.OTHER ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( RemotePhotoSiteCategory.REST, GenreDiscEntry.OTHER ) );
		PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING.add( new RemotePhotoSiteCategoryToGenreMapping( RemotePhotoSiteCategory.PAPARAZZI, GenreDiscEntry.OTHER ) );
	}

	public static List<RemotePhotoSiteCategoryToGenreMapping> getRemotePhotoSiteCategoryToGenreMapping() {
		return PHOTOSIGHT_CATEGORY_TO_GENRE_MAPPING;
	}

	public RemotePhotoSiteCategoryToGenreMapping( final RemotePhotoSiteCategory remotePhotoSiteCategory, final GenreDiscEntry genreDiscEntry ) {
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
