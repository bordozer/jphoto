package core.services.dao;

import core.general.photo.PhotoAward;

import java.util.List;

public interface PhotoAwardDao {

	boolean doesPhotoHaveThisAward( final PhotoAward photoAward );

	void savePhotoAward( final PhotoAward photoAward );

	List<PhotoAward> getPhotoAwards( final int photoId );

	void deletePhotoAwards( final int photoId );
}
