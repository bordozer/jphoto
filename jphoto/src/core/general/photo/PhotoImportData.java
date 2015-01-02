package core.general.photo;

import admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import org.apache.commons.lang.StringUtils;

public class PhotoImportData {

	private final PhotosImportSource photosImportSource;
	private final String remoteUserId;
	private final String remoteUserName;
	private final int remotePhotoId;

	public PhotoImportData( final PhotosImportSource photosImportSource, final String remoteUserId, final String remoteUserName, final int remotePhotoId ) {
		this.photosImportSource = photosImportSource;
		this.remoteUserId = remoteUserId;
		this.remoteUserName = remoteUserName; //StringUtils.isNotEmpty( remoteUserName ) ? remoteUserName : remoteUserId; // TODO: it necessary temporarily
		this.remotePhotoId = remotePhotoId;
	}

	public PhotosImportSource getPhotosImportSource() {
		return photosImportSource;
	}

	public String getRemoteUserId() {
		return remoteUserId;
	}

	public String getRemoteUserName() {
		return remoteUserName;
	}

	public int getRemotePhotoId() {
		return remotePhotoId;
	}
}
