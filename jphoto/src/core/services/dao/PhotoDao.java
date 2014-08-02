package core.services.dao;

import core.general.photo.Photo;

import java.util.List;

public interface PhotoDao extends BaseEntityDao<Photo> {

	int getPhotoQty();

	int getPhotoQtyByGenre( final int genreId );

	int getPhotoQtyByUser( final int userId );

	int getPhotoQtyByUserAndGenre( final int userId, final int genreId );

	int getLastUserPhotoId( final int userId );

	int getLastGenrePhotoId( int genreId );

	List<Photo> getUserPhotos( final int userId );

	List<Integer> getUserPhotosIds( final int userId );
}
