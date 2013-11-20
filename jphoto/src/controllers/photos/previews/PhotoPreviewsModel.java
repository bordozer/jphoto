package controllers.photos.previews;

import core.general.base.AbstractGeneralPageModel;
import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.photo.PhotoPreview;
import core.general.photo.PhotoPreviewWrapper;
import core.general.user.User;

import java.util.List;

public class PhotoPreviewsModel extends AbstractGeneralPageModel {

	private Photo photo;
	private User photoAuthor;
	private Genre genre;

	private List<PhotoPreview> photoPreviews;

	private PhotoPreviewWrapper photoPreviewWrapper;

	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto( final Photo photo ) {
		this.photo = photo;
	}

	public User getPhotoAuthor() {
		return photoAuthor;
	}

	public void setPhotoAuthor( final User photoAuthor ) {
		this.photoAuthor = photoAuthor;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre( final Genre genre ) {
		this.genre = genre;
	}

	public List<PhotoPreview> getPhotoPreviews() {
		return photoPreviews;
	}

	public void setPhotoPreviews( final List<PhotoPreview> photoPreviews ) {
		this.photoPreviews = photoPreviews;
	}

	public PhotoPreviewWrapper getPhotoPreviewWrapper() {
		return photoPreviewWrapper;
	}

	public void setPhotoPreviewWrapper( final PhotoPreviewWrapper photoPreviewWrapper ) {
		this.photoPreviewWrapper = photoPreviewWrapper;
	}
}
