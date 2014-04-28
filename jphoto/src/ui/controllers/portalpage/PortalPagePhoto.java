package ui.controllers.portalpage;

import core.general.photo.Photo;

public class PortalPagePhoto {

	private Photo photo;
	private String photoImgUrl;
	private boolean photoPreviewHasToBeHiddenBecauseOfNudeContent;

	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto( final Photo photo ) {
		this.photo = photo;
	}

	public String getPhotoImgUrl() {
		return photoImgUrl;
	}

	public void setPhotoImgUrl( final String photoImgUrl ) {
		this.photoImgUrl = photoImgUrl;
	}

	public boolean isPhotoPreviewHasToBeHiddenBecauseOfNudeContent() {
		return photoPreviewHasToBeHiddenBecauseOfNudeContent;
	}

	public void setPhotoPreviewHasToBeHiddenBecauseOfNudeContent( final boolean photoPreviewHasToBeHiddenBecauseOfNudeContent ) {
		this.photoPreviewHasToBeHiddenBecauseOfNudeContent = photoPreviewHasToBeHiddenBecauseOfNudeContent;
	}
}
