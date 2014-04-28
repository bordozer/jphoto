package ui.controllers.photos.edit;

import core.general.base.AbstractGeneralModel;
import core.general.photo.Photo;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class PhotoEditDataModel extends AbstractGeneralModel {

	private int photoId;
	private Photo photo;

	private MultipartFile photoFile;
	private File tempPhotoFile;

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

	public MultipartFile getPhotoFile() {
		return photoFile;
	}

	public void setPhotoFile( final MultipartFile photoFile ) {
		this.photoFile = photoFile;
	}

	public void setTempPhotoFile( final File tempPhotoFile ) {
		this.tempPhotoFile = tempPhotoFile;
	}

	public File getTempPhotoFile() {
		return tempPhotoFile;
	}
}
