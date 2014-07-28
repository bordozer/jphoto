package core.services.dao;

import core.general.photo.Photo;
import core.general.user.userAlbums.UserPhotoAlbum;

import java.util.List;

public interface UserPhotoAlbumDao extends BaseEntityDao<UserPhotoAlbum> {

	List<UserPhotoAlbum> loadAllUserPhotoAlbums( final int userId );

	void deletePhotoFromAllAlbums( final int photoId );

	boolean savePhotoAlbums( final Photo photo, final List<UserPhotoAlbum> photoAlbums );

	List<UserPhotoAlbum> loadPhotoAlbums( final int photoId );

	UserPhotoAlbum loadPhotoAlbumByName( final int userId, String albumName );

	int getUserPhotoAlbumPhotosQty( final int userPhotoAlbumId );

	boolean isPhotoInAlbum( final int photoId, final int photoAlbumId );

	boolean addPhotoToAlbum( final int photoId, final int photoAlbumId );

	boolean deletePhotoFromAlbum( final int photoId, final int photoAlbumId );
}
