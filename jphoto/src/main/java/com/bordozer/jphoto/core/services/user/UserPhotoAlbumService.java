package com.bordozer.jphoto.core.services.user;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.userAlbums.UserPhotoAlbum;
import com.bordozer.jphoto.core.interfaces.AllEntriesByIdLoadable;
import com.bordozer.jphoto.core.interfaces.BaseEntityService;
import com.bordozer.jphoto.core.interfaces.IdsSqlSelectable;

import java.util.List;

public interface UserPhotoAlbumService extends BaseEntityService<UserPhotoAlbum>, IdsSqlSelectable, AllEntriesByIdLoadable<UserPhotoAlbum> {

    List<Integer> loadAlbumPhotoIds(final int albumId);

    boolean savePhotoAlbums(final Photo photo, final List<UserPhotoAlbum> photoAlbums);

    List<UserPhotoAlbum> loadPhotoAlbums(final int photoId);

    UserPhotoAlbum loadPhotoAlbumByName(final User user, final String albumName);

    void deletePhotoFromAllAlbums(final int photoId);

    int getUserPhotoAlbumPhotosQty(final int userPhotoAlbumId);

    boolean isPhotoInAlbum(final int photoId, final int photoAlbumId);

    boolean addPhotoToAlbum(final int photoId, final int photoAlbumId);

    boolean deletePhotoFromAlbum(final int photoId, final int photoAlbumId);
}
