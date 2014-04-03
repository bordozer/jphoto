package ui.controllers.users.photoAlbums.list;

import core.general.base.AbstractGeneralModel;
import core.general.user.User;
import core.general.user.userAlbums.UserPhotoAlbum;

import java.util.List;
import java.util.Map;

public class UserPhotoAlbumListModel extends AbstractGeneralModel {

	private User user;
	private List<UserPhotoAlbum> userPhotoAlbums;
	private Map<Integer, Integer> userPhotoAlbumsQtyMap;

	public void setUser( final User user ) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public List<UserPhotoAlbum> getUserPhotoAlbums() {
		return userPhotoAlbums;
	}

	public void setUserPhotoAlbums( final List<UserPhotoAlbum> userPhotoAlbums ) {
		this.userPhotoAlbums = userPhotoAlbums;
	}

	public Map<Integer, Integer> getUserPhotoAlbumsQtyMap() {
		return userPhotoAlbumsQtyMap;
	}

	public void setUserPhotoAlbumsQtyMap( final Map<Integer, Integer> userPhotoAlbumsQtyMap ) {
		this.userPhotoAlbumsQtyMap = userPhotoAlbumsQtyMap;
	}
}
