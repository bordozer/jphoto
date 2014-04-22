package core.services.dao;

import core.general.photo.Photo;
import core.general.photo.PhotoFile;

import java.util.List;

public interface PhotoDao extends BaseEntityDao<Photo> {

	int getPhotoQty();

	int getPhotoQtyByGenre( final int genreId );

	int getPhotoQtyByUser( final int userId );

	int getPhotoQtyByUserAndGenre( final int userId, final int genreId );

	boolean updatePhotoFile( int photoId, final PhotoFile photoFile );

	int getLastUserPhotoId( final int userId );

	List<Photo> getUserPhotos( final int userId );

	List<Integer> getUserPhotosIds( final int userId );
}
