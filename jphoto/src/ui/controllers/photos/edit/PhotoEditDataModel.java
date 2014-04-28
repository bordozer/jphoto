package ui.controllers.photos.edit;

import core.general.base.AbstractGeneralModel;
import core.general.photo.Photo;

public class PhotoEditDataModel extends AbstractGeneralModel {

	private int photoId;
	private Photo photo;

	public int getPhotoId() {
		return photoId;
	}

	public void setPhotoId( final int photoId ) {
		this.photoId = photoId;
	}

	public void setPhoto( final Photo photo ) {
		this.photo = photo;
	}

	public Photo getPhoto() {
		return photo;
	}
}
