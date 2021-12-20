package com.bordozer.jphoto.core.services.photo;

import com.bordozer.jphoto.core.general.data.PhotoMarksForPeriod;
import com.bordozer.jphoto.core.general.data.PhotoRating;
import com.bordozer.jphoto.core.general.data.TimeRange;
import com.bordozer.jphoto.core.services.dao.PhotoRatingDao;
import com.bordozer.jphoto.core.services.dao.PhotoVotingDao;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("photoRatingService")
public class PhotoRatingServiceImpl implements PhotoRatingService {

    @Autowired
    private PhotoVotingDao photoVotingDao;

    @Autowired
    private PhotoRatingDao photoRatingDao;

    @Autowired
    private DateUtilsService dateUtilsService;

    @Override
    public void recalculatePhotoRatingForPeriodInDB(final Date timeFrom, final Date timeTo) {
        final List<PhotoMarksForPeriod> marksForPeriods = photoVotingDao.getSummaryPhotoVotingForPeriodSortedBySummaryMarkDesc(timeFrom, timeTo);

        if (marksForPeriods.size() == 0) {
            return;
        }

        synchronized (photoRatingDao) {
            photoRatingDao.deletePhotosRatingsForPeriod(timeFrom, timeTo);

            int ratingPosition = 1;
            for (final PhotoMarksForPeriod marksForPeriod : marksForPeriods) {

                final PhotoRating photoRating = new PhotoRating();

                photoRating.setPhotoId(marksForPeriod.getPhotoId());
                photoRating.setTimeFrom(timeFrom);
                photoRating.setTimeTo(timeTo);
                photoRating.setRatingPosition(ratingPosition);
                photoRating.setSummaryMark(marksForPeriod.getSumMarks());

                photoRatingDao.savePhotoRatingForPeriod(photoRating);

                ratingPosition++;
            }
        }
    }

    @Override
    public PhotoRating getPhotoRatingForPeriod(final int photoId, final Date timeFrom, final Date timeTo) {
        return photoRatingDao.getPhotoRatingsForPeriod(photoId, timeFrom, timeTo);
    }

    @Override
    public PhotoRating getPhotoRatingToday(final int photoId) {
        final TimeRange timeRangeToday = dateUtilsService.getTimeRangeToday();

        return getPhotoRatingForPeriod(photoId, timeRangeToday.getTimeFrom(), timeRangeToday.getTimeTo());
    }

    @Override
    public void deletePhotoRatings(final int photoId) {
        photoRatingDao.deletePhotoRatings(photoId);
    }
}
