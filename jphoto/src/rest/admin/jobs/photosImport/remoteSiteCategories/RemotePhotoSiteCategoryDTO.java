package rest.admin.jobs.photosImport.remoteSiteCategories;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class RemotePhotoSiteCategoryDTO {

	private int remotePhotoSiteCategoryId;
	private String remotePhotoSiteCategoryName;

	private boolean checked;

	private String cssClasses;

	public int getRemotePhotoSiteCategoryId() {
		return remotePhotoSiteCategoryId;
	}

	public void setRemotePhotoSiteCategoryId( final int remotePhotoSiteCategoryId ) {
		this.remotePhotoSiteCategoryId = remotePhotoSiteCategoryId;
	}

	public String getRemotePhotoSiteCategoryName() {
		return remotePhotoSiteCategoryName;
	}

	public void setRemotePhotoSiteCategoryName( final String remotePhotoSiteCategoryName ) {
		this.remotePhotoSiteCategoryName = remotePhotoSiteCategoryName;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked( final boolean checked ) {
		this.checked = checked;
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

	@Override
	public String toString() {
		return String.format( "%d %s - %s", remotePhotoSiteCategoryId, remotePhotoSiteCategoryName, checked );
	}
}
