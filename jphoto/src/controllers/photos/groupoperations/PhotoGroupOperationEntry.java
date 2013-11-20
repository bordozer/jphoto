package controllers.photos.groupoperations;

import core.general.photo.Photo;

public class PhotoGroupOperationEntry {

	private final Photo photo;
	private String photoPreviewImgUrl;

	private boolean isGroupOperationAccessible = true;
	private String photoOperationAllowanceMessage;
	private String photoOperationInfo;

	public PhotoGroupOperationEntry( final Photo photo ) {
		this.photo = photo;
	}

	public Photo getPhoto() {
		return photo;
	}

	public String getPhotoPreviewImgUrl() {
		return photoPreviewImgUrl;
	}

	public void setPhotoPreviewImgUrl( final String photoPreviewImgUrl ) {
		this.photoPreviewImgUrl = photoPreviewImgUrl;
	}

	public String getPhotoOperationAllowanceMessage() {
		return photoOperationAllowanceMessage;
	}

	public void setPhotoOperationAllowanceMessage( final String photoOperationAllowanceMessage ) {
		this.photoOperationAllowanceMessage = photoOperationAllowanceMessage;
	}

	public String getPhotoOperationInfo() {
		return photoOperationInfo;
	}

	public void setPhotoOperationInfo( final String photoOperationAllowanceMessage ) {
		this.photoOperationInfo = photoOperationAllowanceMessage;
	}

	public boolean isGroupOperationAccessible() {
		return isGroupOperationAccessible;
	}

	public void setGroupOperationAccessible( final boolean groupOperationAccessible ) {
		isGroupOperationAccessible = groupOperationAccessible;
	}
}
