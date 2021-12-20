package com.bordozer.jphoto.core.services.photo;

import com.bordozer.jphoto.core.exceptions.BaseRuntimeException;
import com.bordozer.jphoto.core.general.cache.CacheKey;
import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.data.PhotoRating;
import com.bordozer.jphoto.core.general.data.TimeRange;
import com.bordozer.jphoto.core.general.data.UserRating;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.photo.PhotoInfo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.UserPhotoVote;
import com.bordozer.jphoto.core.services.dao.PhotoVotingDao;
import com.bordozer.jphoto.core.services.entry.ActivityStreamService;
import com.bordozer.jphoto.core.services.system.CacheService;
import com.bordozer.jphoto.core.services.system.ConfigurationService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.ui.controllers.users.card.MarksByCategoryInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("photoVotingService")
public class PhotoVotingServiceImpl implements PhotoVotingService {

    @Autowired
    private PhotoVotingDao photoVotingDao;

    @Autowired
    private PhotoRatingService photoRatingService;

    @Autowired
    private PhotoAwardService photoAwardService;

    @Autowired
    private CacheService<PhotoInfo> cacheServicePhotoInfo;

    @Autowired
    private DateUtilsService dateUtilsService;

    @Autowired
    private ActivityStreamService activityStreamService;

    @Autowired
    private ConfigurationService configurationService;

    @Override
    public boolean saveUserPhotoVoting(final User user, final Photo photo, final Date votingTime, final List<UserPhotoVote> userPhotoVotes) {
        final boolean isSaved = photoVotingDao.saveUserPhotoVoting(user, photo, userPhotoVotes);

        if (!isSaved) {
            throw new BaseRuntimeException("photoVotingDao.saveUserPhotoVoting() error");
        }

        photoVotingDao.updatePhotoSummaryMarksByVotingCategories(photo.getId(), userPhotoVotes);

        final Date currentTime = dateUtilsService.getCurrentTime();
        final Date firstSecondOfToday = dateUtilsService.getFirstSecondOfDay(currentTime);
        final Date lastSecondOfToday = dateUtilsService.getFirstSecondOfDay(currentTime);

        photoRatingService.recalculatePhotoRatingForPeriodInDB(firstSecondOfToday, lastSecondOfToday);

        final PhotoRating photoRatingToday = photoRatingService.getPhotoRatingToday(photo.getId());
        if (photoRatingToday != null && photoRatingToday.getRatingPosition() > 0) {
            photoAwardService.calculatePhotoAwards(photo.getId());
        }

        cacheServicePhotoInfo.expire(CacheKey.PHOTO_INFO, photo.getId());

        activityStreamService.savePhotoVoting(user, photo, userPhotoVotes, votingTime);

        return true;
    }

    @Override
    public void deletePhotoVoting(final int photoId) {
        photoVotingDao.deletePhotoVotesSummary(photoId);
        photoVotingDao.deletePhotoVotes(photoId);
    }

    @Override
    public boolean isUserVotedForPhoto(final User user, final Photo photo) {
        return photoVotingDao.isUserVotedForPhoto(user, photo);
    }

    @Override
    public List<UserPhotoVote> getUserVotesForPhoto(final User user, final Photo photo) {
        return photoVotingDao.getUserVotesForPhoto(user, photo);
    }

    @Override
    public List<UserPhotoVote> getPhotoVotes(final Photo photo) {
        return photoVotingDao.getPhotoVotes(photo);
    }

    @Override
    public List<UserPhotoVote> getUserVotes(final User user) {
        return photoVotingDao.getUserVotes(user);
    }

    public List<MarksByCategoryInfo> getPhotoSummaryVoicesByPhotoCategories(final Photo photo) {
        return photoVotingDao.getUserSummaryVoicesByPhotoCategories(photo);
    }

    public List<MarksByCategoryInfo> getUserSummaryVoicesByPhotoCategories(final User user) {
        return photoVotingDao.getUserSummaryVoicesByPhotoCategories(user);
    }

    @Override
    public int getPhotoMarksForPeriod(final int photoId, final Date timeFrom, final Date timeTo) {
        return photoVotingDao.getPhotoMarksForPeriod(photoId, timeFrom, timeTo);
    }

    @Override
    public List<UserRating> getUserRatingForPeriod(final Date timeFrom, final Date timeTo, final int limit) {
        return photoVotingDao.getUserRatingForPeriod(timeFrom, timeTo, limit);
    }

    @Override
    public TimeRange getTopBestDateRange() {
        final int days = configurationService.getInt(ConfigurationKey.PHOTO_RATING_CALCULATE_MARKS_FOR_THE_BEST_PHOTOS_FOR_LAST_DAYS);
        return new TimeRange(dateUtilsService.getFirstSecondOfDay(dateUtilsService.getDatesOffsetFromCurrentDate(-days + 1)), dateUtilsService.getLastSecondOfToday());
    }

    @Override
    public TimeRange getPortalPageBestDateRange() {
        final int days = configurationService.getInt(ConfigurationKey.PHOTO_RATING_PORTAL_PAGE_BEST_PHOTOS_FROM_PHOTOS_THAT_GOT_ENOUGH_MARKS_FOR_N_LAST_DAYS);
        return new TimeRange(dateUtilsService.getFirstSecondOfDay(dateUtilsService.getDatesOffsetFromCurrentDate(-days + 1)), dateUtilsService.getLastSecondOfToday());
    }

    @Override
    public int getSummaryPhotoMark(final Photo photo) {
        int sumMark = 0;

        final List<MarksByCategoryInfo> marksByCategories = getPhotoSummaryVoicesByPhotoCategories(photo);
        for (final MarksByCategoryInfo marksByCategory : marksByCategories) {
            sumMark += marksByCategory.getSumMark();
        }

        return sumMark;
    }
}
