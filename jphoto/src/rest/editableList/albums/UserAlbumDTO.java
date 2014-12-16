package rest.editableList.albums;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import rest.editableList.EditableListEntryDTO;

@JsonIgnoreProperties( ignoreUnknown = true )
public class UserAlbumDTO extends EditableListEntryDTO {

	private String albumName;
	private String albumLink;
	private int albumPhotosQty;

	public UserAlbumDTO() {
		super();
	}

	public UserAlbumDTO( final int entryId ) {
		super( entryId );
	}

	public void setAlbumName( final String albumName ) {
		this.albumName = albumName;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumLink( final String albumLink ) {
		this.albumLink = albumLink;
	}

	public String getAlbumLink() {
		return albumLink;
	}

	public void setAlbumPhotosQty( final int albumPhotosQty ) {
		this.albumPhotosQty = albumPhotosQty;
	}

	public int getAlbumPhotosQty() {
		return albumPhotosQty;
	}

	@Override
	public String toString() {
		return String.format( "User: %d, album: %d '%s'", userId, entryId, albumName );
	}
}
