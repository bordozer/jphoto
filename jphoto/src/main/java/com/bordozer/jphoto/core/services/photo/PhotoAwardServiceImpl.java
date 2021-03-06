package com.bordozer.jphoto.core.services.photo;

import com.bordozer.jphoto.core.enums.PhotoAwardKey;
import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.data.PhotoRating;
import com.bordozer.jphoto.core.general.data.TimeRange;
import com.bordozer.jphoto.core.general.photo.PhotoAward;
import com.bordozer.jphoto.core.services.dao.PhotoAwardDao;
import com.bordozer.jphoto.core.services.system.ConfigurationService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("photoAwardService")
public class PhotoAwardServiceImpl implements PhotoAwardService {

    @Autowired
    private PhotoRatingService photoRatingService;

    @Autowired
    private PhotoAwardDao photoAwardDao;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private DateUtilsService dateUtilsService;

    @Override
    public void calculatePhotoAwards(final int photoId) {
        calculateTodayAwards(photoId);
    }

    @Override
    public List<PhotoAward> getPhotoAwards(final int photoId) {
        return photoAwardDao.getPhotoAwards(photoId);
    }

    @Override
    public void deletePhotoAwards(final int photoId) {
        photoAwardDao.deletePhotoAwards(photoId);
    }

    private void calculateTodayAwards(final int photoId) {
        final PhotoRating photoRatingToday = photoRatingService.getPhotoRatingToday(photoId);

        if (photoRatingToday == null) {
            return;
        }

        final int positionToday = photoRatingToday.getRatingPosition();

        if (positionToday > 50) {
            return;
        }

        final int minMarkForTop = configurationService.getInt(ConfigurationKey.PHOTO_RATING_MIN_MARKS_TO_BE_IN_THE_BEST_PHOTO);
        final int summaryMark = photoRatingToday.getSummaryMark();
        if (summaryMark < minMarkForTop) {
            return;
        }

        final TimeRange timeRangeToday = dateUtilsService.getTimeRangeToday();

        final PhotoAward award = new PhotoAward(photoId);
        award.setTimeFrom(timeRangeToday.getTimeFrom());
        award.setTimeTo(timeRangeToday.getTimeTo());

        if (positionToday == 1) {
            award.setAwardKey(PhotoAwardKey.PHOTO_OF_THE_DAY);
        } else if (positionToday > 1 && positionToday <= 3) {
            award.setAwardKey(PhotoAwardKey.TOP_3_OF_THE_DAY);
        } else if (positionToday > 3 && positionToday <= 10) {
            award.setAwardKey(PhotoAwardKey.TOP_10_OF_THE_DAY);
        } else if (positionToday > 10 && positionToday <= 50) {
            award.setAwardKey(PhotoAwardKey.TOP_50_OF_THE_DAY);
        }
        if (!photoAwardDao.doesPhotoHaveThisAward(award)) {
            photoAwardDao.savePhotoAward(award);
        }
    }
}
