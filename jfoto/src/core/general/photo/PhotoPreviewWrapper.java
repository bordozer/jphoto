package core.general.photo;

import core.general.genre.Genre;

public class PhotoPreviewWrapper {

	private final Photo photo;
	private final Genre genre;
	private final String photoPreviewImgUrl;
	private boolean photoPreviewHasToBeHiddenBecauseOfNudeContent;

	public PhotoPreviewWrapper( final Photo photo, final Genre genre, final String photoPreviewImgUrl ) {
		this.photo = photo;
		this.genre = genre;
		this.photoPreviewImgUrl = photoPreviewImgUrl;
	}

	public Photo getPhoto() {
		return photo;
	}

	public Genre getGenre() {
		return genre;
	}

	public boolean isPhotoPreviewHasToBeHiddenBecauseOfNudeContent() {
		return photoPreviewHasToBeHiddenBecauseOfNudeContent;
	}

	public void setPhotoPreviewHasToBeHiddenBecauseOfNudeContent( final boolean photoPreviewHasToBeHiddenBecauseOfNudeContent ) {
		this.photoPreviewHasToBeHiddenBecauseOfNudeContent = photoPreviewHasToBeHiddenBecauseOfNudeContent;
	}

	public String getPhotoPreviewImgUrl() {
		return photoPreviewImgUrl;
	}

	@Override
	public int hashCode() {
		return photo.getId();
	}

	@Override
	public boolean equals( final Object obj ) {
		if ( obj == null ) {
			return false;
		}

		if ( obj == this ) {
			return true;
		}

		if ( !( obj instanceof PhotoPreviewWrapper ) ) {
			return false;
		}

		final PhotoPreviewWrapper previewWrapper = ( PhotoPreviewWrapper ) obj;
		return previewWrapper.getPhoto().getId() == photo.getId();
	}

	@Override
	public String toString() {
		return photo.toString();
	}
}
