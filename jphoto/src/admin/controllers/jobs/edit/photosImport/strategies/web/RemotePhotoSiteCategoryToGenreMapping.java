package admin.controllers.jobs.edit.photosImport.strategies.web;

import admin.controllers.jobs.edit.photosImport.GenreDiscEntry;

public class RemotePhotoSiteCategoryToGenreMapping {

	private final RemotePhotoSiteCategory remotePhotoSiteCategory;

	private final GenreDiscEntry genreDiscEntry;


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
