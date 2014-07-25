package admin.controllers.jobs.edit.photosImport.strategies.web;

import admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategory;

public class RemotePhotoSiteCategoryWrapper {

	private final RemotePhotoSiteCategory remotePhotoSiteCategory;
	private String cssClasses;

	public RemotePhotoSiteCategoryWrapper( final RemotePhotoSiteCategory remotePhotoSiteCategory ) {
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
