package com.bordozer.jphoto.core.services.photo;

import com.bordozer.jphoto.core.general.data.PhotoRating;

import java.util.Date;

public interface PhotoRatingService {

    void recalculatePhotoRatingForPeriodInDB(final Date timeFrom, final Date timeTo);

    PhotoRating getPhotoRatingForPeriod(final int photoId, final Date timeFrom, final Date timeTo);

    PhotoRating getPhotoRatingToday(final int photoId);

    void deletePhotoRatings(final int photoId);
}
