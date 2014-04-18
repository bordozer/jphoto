package json.photo;

public class PhotoEntryDTO {

	private final int photoId;

	private String name;
	private String photoImageUrl;

	public PhotoEntryDTO( final int photoId ) {
		this.photoId = photoId;
	}

	public int getPhotoId() {
		return photoId;
	}

	public String getName() {
		return name;
	}

	public void setName( final String name ) {
		this.name = name;
	}

	public String getPhotoImageUrl() {
		return photoImageUrl;
	}

	public void setPhotoImageUrl( final String photoImageUrl ) {
		this.photoImageUrl = photoImageUrl;
	}
}
