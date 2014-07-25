package admin.controllers.jobs.edit.photosImport.strategies.web;

import admin.controllers.jobs.edit.photosImport.GenreDiscEntry;

public class RemotePhotoSiteCategoryToGenreMapping {

	private final RemotePhotoSiteCategory photosightCategory;

	private final GenreDiscEntry genreDiscEntry;


	public RemotePhotoSiteCategoryToGenreMapping( final RemotePhotoSiteCategory photosightCategory, final GenreDiscEntry genreDiscEntry ) {
		this.photosightCategory = photosightCategory;
		this.genreDiscEntry = genreDiscEntry;
	}

	public RemotePhotoSiteCategory getPhotosightCategory() {
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
