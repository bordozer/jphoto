package core.services.user;

import core.general.photo.Photo;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.interfaces.AllEntriesByIdLoadable;
import core.interfaces.BaseEntityService;
import core.interfaces.IdsSqlSelectable;

import java.util.List;

public interface UserPhotoAlbumService extends BaseEntityService<UserPhotoAlbum>, IdsSqlSelectable, AllEntriesByIdLoadable<UserPhotoAlbum> {

	boolean savePhotoAlbums( final Photo photo, final List<UserPhotoAlbum> photoAlbums );

	List<UserPhotoAlbum> loadPhotoAlbums( final int photoId );

	UserPhotoAlbum loadPhotoAlbumByName( final String name );

	void deletePhotoFromAllAlbums( final int photoId );

	int getUserPhotoAlbumPhotosQty( final int userPhotoAlbumId );

	boolean isPhotoInAlbum( final int photoId, final int photoAlbumId );

	boolean addPhotoToAlbum( final int photoId, final int photoAlbumId );

	boolean deletePhotoFromAlbum( final int photoId, final int photoAlbumId );
}
