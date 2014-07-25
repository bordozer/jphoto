package admin.controllers.jobs.edit.photosImport;

import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategory;

public class PhotosightCategoryWrapper {

	private final RemotePhotoSiteCategory remotePhotoSiteCategory;
	private String cssClasses;

	public PhotosightCategoryWrapper( final RemotePhotoSiteCategory remotePhotoSiteCategory ) {
		this.remotePhotoSiteCategory = remotePhotoSiteCategory;
	}

	public RemotePhotoSiteCategory getRemotePhotoSiteCategory() {
		return remotePhotoSiteCategory;
	}

	public String getCssClasses() {
		return cssClasses;
	}

	public void setCssClasses( final String cssClasses ) {
		this.cssClasses = cssClasses;
	}

	public void addCssClass( final String cssClass ) {
		cssClasses = String.format( "%s %s", cssClasses, cssClass );
	}
}
