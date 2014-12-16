package rest.users.albums;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class UserAlbumDTO {

	private int entryId;
	private int userId;
	private String albumName;
	private String albumLink;
	private int albumPhotosQty;

	public int getEntryId() {
		return entryId;
	}

	public void setEntryId( int entryId ) {
		this.entryId = entryId;
	}

	public void setUserId( final int userId ) {
		this.userId = userId;
	}

	public int getUserId() {
		return userId;
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
