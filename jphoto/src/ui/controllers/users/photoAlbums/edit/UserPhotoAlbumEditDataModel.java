package ui.controllers.users.photoAlbums.edit;

import core.general.base.AbstractGeneralModel;
import core.general.user.User;
import org.apache.commons.lang.StringUtils;

public class UserPhotoAlbumEditDataModel extends AbstractGeneralModel {

	public static final String FORM_CONTROL_PHOTO_ALBUM_NAME = "albumName";
	public static final String FORM_CONTROL_PHOTO_ALBUM_DESCRIPTION = "albumDescription";

	private User user;

	private int albumId;
	private String albumName;
	private String albumDescription;

	public User getUser() {
		return user;
	}

	public void setUser( final User user ) {
		this.user = user;
	}

	public int getAlbumId() {
		return albumId;
	}

	public void setAlbumId( final int albumId ) {
		this.albumId = albumId;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName( final String albumName ) {
		this.albumName = albumName;
	}

	public String getAlbumDescription() {
		return albumDescription;
	}

	public void setAlbumDescription( final String albumDescription ) {
		this.albumDescription = albumDescription;
	}

	@Override
	public void clear() {
		super.clear();

		albumId = 0;
		albumName = StringUtils.EMPTY;
		albumDescription = StringUtils.EMPTY;
	}
}
