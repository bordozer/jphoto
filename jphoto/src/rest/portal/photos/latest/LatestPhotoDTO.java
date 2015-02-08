package rest.portal.photos.latest;

public class LatestPhotoDTO {

	private int photoId;
	private String photoName;
	private String photoImageUrl;

	public int getPhotoId() {
		return photoId;
	}

	public void setPhotoId( final int photoId ) {
		this.photoId = photoId;
	}

	public String getPhotoName() {
		return photoName;
	}

	public void setPhotoName( final String photoName ) {
		this.photoName = photoName;
	}

	public String getPhotoImageUrl() {
		return photoImageUrl;
	}

	public void setPhotoImageUrl( final String photoImageUrl ) {
		this.photoImageUrl = photoImageUrl;
	}

	@Override
	public String toString() {
		return String.format( "#%d: %s", photoId, photoName );
	}
}
