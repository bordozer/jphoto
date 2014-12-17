package core.services.user;

import core.general.photo.Photo;
import core.general.user.User;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.services.dao.UserPhotoAlbumDao;
import org.springframework.beans.factory.annotation.Autowired;
import sql.SqlSelectIdsResult;
import sql.builder.SqlIdsSelectQuery;

import java.util.List;


public class UserPhotoAlbumServiceImpl implements UserPhotoAlbumService {

	@Autowired
	private UserPhotoAlbumDao userPhotoAlbumDao;

	@Override
	public List<UserPhotoAlbum> loadAllForEntry( final int userId ) {
		return userPhotoAlbumDao.loadAllUserPhotoAlbums( userId );
	}

	@Override
	public boolean save( final UserPhotoAlbum entry ) {
		return userPhotoAlbumDao.saveToDB( entry );
	}

	@Override
	public UserPhotoAlbum load( final int photoAlbumId ) {
		return userPhotoAlbumDao.load( photoAlbumId );
	}

	@Override
	public boolean delete( final int entryId ) {
		return userPhotoAlbumDao.delete( entryId );
	}

	@Override
	public SqlSelectIdsResult load( final SqlIdsSelectQuery selectIdsQuery ) {
		return userPhotoAlbumDao.load( selectIdsQuery );
	}

	@Override
	public List<Integer> loadAlbumPhotoIds( int albumId ) {
		return userPhotoAlbumDao.loadAlbumPhotoIds( albumId );
	}

	@Override
	public boolean savePhotoAlbums( final Photo photo, final List<UserPhotoAlbum> photoAlbums ) {
		return userPhotoAlbumDao.savePhotoAlbums( photo, photoAlbums );
	}

	@Override
	public List<UserPhotoAlbum> loadPhotoAlbums( final int photoId ) {
		return userPhotoAlbumDao.loadPhotoAlbums( photoId );
	}

	@Override
	public UserPhotoAlbum loadPhotoAlbumByName( final User user, final String albumName ) {
		return userPhotoAlbumDao.loadPhotoAlbumByName( user.getId(), albumName );
	}

	@Override
	public void deletePhotoFromAllAlbums( final int photoId ) {
		userPhotoAlbumDao.deletePhotoFromAllAlbums( photoId );
	}

	@Override
	public int getUserPhotoAlbumPhotosQty( final int userPhotoAlbumId ) {
		return userPhotoAlbumDao.getUserPhotoAlbumPhotosQty( userPhotoAlbumId );
	}

	@Override
	public boolean isPhotoInAlbum( final int photoId, final int photoAlbumId ) {
		return userPhotoAlbumDao.isPhotoInAlbum( photoId, photoAlbumId );
	}

	@Override
	public boolean addPhotoToAlbum( final int photoId, final int photoAlbumId ) {
		return isPhotoInAlbum( photoId, photoAlbumId ) || userPhotoAlbumDao.addPhotoToAlbum( photoId, photoAlbumId );
	}

	@Override
	public boolean deletePhotoFromAlbum( final int photoId, final int photoAlbumId ) {
		return !isPhotoInAlbum( photoId, photoAlbumId ) || userPhotoAlbumDao.deletePhotoFromAlbum( photoId, photoAlbumId );

	}

	@Override
	public boolean exists( final int entryId ) {
		return userPhotoAlbumDao.exists( entryId );
	}

	@Override
	public boolean exists( final UserPhotoAlbum entry ) {
		return userPhotoAlbumDao.exists( entry );
	}
}
