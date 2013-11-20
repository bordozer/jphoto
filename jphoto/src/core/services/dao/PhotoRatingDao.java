package core.services.dao;

import core.general.data.PhotoRating;

import java.util.Date;
import java.util.List;

public interface PhotoRatingDao {

	void savePhotoRatingForPeriod( final PhotoRating photoRating );

	void deletePhotosRatingsForPeriod( final Date timeFrom, final Date timeTo );

	PhotoRating getPhotoRatingsForPeriod( final int photoId, final Date timeFrom, final Date timeTo );

	List<PhotoRating> getPhotosRatingsForPeriod( final Date timeFrom, final Date timeTo );

	void deletePhotoRatings( final int photoId );
}
