package admin.controllers.jobs.edit.photosImport.strategies.web;

import admin.controllers.jobs.edit.photosImport.LocalCategory;

public class RemotePhotoSiteCategoryToGenreMapping {

	private final RemotePhotoSiteCategory remotePhotoSiteCategory;

	private final LocalCategory localCategory;


	public RemotePhotoSiteCategoryToGenreMapping( final RemotePhotoSiteCategory remotePhotoSiteCategory, final LocalCategory localCategory ) {
		this.remotePhotoSiteCategory = remotePhotoSiteCategory;
		this.localCategory = localCategory;
	}

	public RemotePhotoSiteCategory getRemotePhotoSiteCategory() {
		return remotePhotoSiteCategory;
	}

	public LocalCategory getLocalCategory() {
		return localCategory;
	}

	@Override
	public String toString() {
		return String.format( "%s => %s", remotePhotoSiteCategory, localCategory.getName() );
	}
}
