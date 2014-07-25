package admin.controllers.jobs.edit.photosImport.strategies.web;

import admin.controllers.jobs.edit.photosImport.strategies.web.photosight.PhotosightCategory;

public class RemotePhotoSiteCategoryWrapper {

	private final PhotosightCategory photosightCategory;
	private String cssClasses;

	public RemotePhotoSiteCategoryWrapper( final PhotosightCategory photosightCategory ) {
		this.photosightCategory = photosightCategory;
	}

	public PhotosightCategory getPhotosightCategory() {
		return photosightCategory;
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
