package rest.portal.photos;

public class LatestPhotoDTO {

	private int photoId;
	private String photoName;
	private String photoImageUrl;
	private String photoCardUrl;

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

	public String getPhotoCardUrl() {
		return photoCardUrl;
	}

	public void setPhotoCardUrl( final String photoCardUrl ) {
		this.photoCardUrl = photoCardUrl;
	}

	@Override
	public String toString() {
		return String.format( "#%d: %s", photoId, photoName );
	}
}
