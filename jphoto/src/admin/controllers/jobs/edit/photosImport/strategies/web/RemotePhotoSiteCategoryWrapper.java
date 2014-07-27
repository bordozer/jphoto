package admin.controllers.jobs.edit.photosImport.strategies.web;

public class RemotePhotoSiteCategoryWrapper {

	private final RemotePhotoSiteCategory photosightCategory;
	private String cssClasses;

	public RemotePhotoSiteCategoryWrapper( final RemotePhotoSiteCategory photosightCategory ) {
		this.photosightCategory = photosightCategory;
	}

	public RemotePhotoSiteCategory getPhotosightCategory() {
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
