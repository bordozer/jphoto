package core.services.photo;

import core.exceptions.BaseRuntimeException;
import core.general.cache.CacheKey;
import core.general.configuration.ConfigurationKey;
import core.general.data.PhotoRating;
import core.general.data.TimeRange;
import core.general.data.UserRating;
import core.general.photo.Photo;
import core.general.photo.PhotoInfo;
import core.general.user.User;
import core.general.user.UserPhotoVote;
import core.services.dao.PhotoVotingDao;
import core.services.entry.ActivityStreamService;
import core.services.system.CacheService;
import core.services.system.ConfigurationService;
import core.services.utils.DateUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import ui.controllers.users.card.MarksByCategoryInfo;

import java.util.Date;
import java.util.List;

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
	public boolean saveUserPhotoVoting( final User user, final Photo photo, final Date votingTime, final List<UserPhotoVote> userPhotoVotes ) {
		final boolean isSaved = photoVotingDao.saveUserPhotoVoting( user, photo, userPhotoVotes );

		if ( ! isSaved ) {
			throw new BaseRuntimeException( "photoVotingDao.saveUserPhotoVoting() error" );
		}

		photoVotingDao.updatePhotoSummaryMarksByVotingCategories( photo.getId(), userPhotoVotes );

		final Date currentTime = dateUtilsService.getCurrentTime();
		final Date firstSecondOfToday = dateUtilsService.getFirstSecondOfDay( currentTime );
		final Date lastSecondOfToday = dateUtilsService.getFirstSecondOfDay( currentTime );

		photoRatingService.recalculatePhotoRatingForPeriodInDB( firstSecondOfToday, lastSecondOfToday );

		final PhotoRating photoRatingToday = photoRatingService.getPhotoRatingToday( photo.getId() );
		if ( photoRatingToday != null && photoRatingToday.getRatingPosition() > 0 ) {
			photoAwardService.calculatePhotoAwards( photo.getId() );
		}

		cacheServicePhotoInfo.expire( CacheKey.PHOTO_INFO, photo.getId() );

		activityStreamService.savePhotoVoting( user, photo, userPhotoVotes, votingTime );

		return true;
	}

	@Override
	public void deletePhotoVoting( final int photoId ) {
		photoVotingDao.deletePhotoVotesSummary( photoId );
		photoVotingDao.deletePhotoVotes( photoId );
	}

	@Override
	public boolean isUserVotedForPhoto( final User user, final Photo photo ) {
		return photoVotingDao.isUserVotedForPhoto( user, photo );
	}

	@Override
	public List<UserPhotoVote> getUserVotesForPhoto( final User user, final Photo photo ) {
		return photoVotingDao.getUserVotesForPhoto( user, photo );
	}

	@Override
	public List<UserPhotoVote> getPhotoVotes( final Photo photo ) {
		return photoVotingDao.getPhotoVotes( photo );
	}

	@Override
	public List<UserPhotoVote> getUserVotes( final User user ) {
		return photoVotingDao.getUserVotes( user );
	}

	public List<MarksByCategoryInfo> getPhotoSummaryVoicesByPhotoCategories( final Photo photo ) {
		return photoVotingDao.getUserSummaryVoicesByPhotoCategories( photo );
	}

	public List<MarksByCategoryInfo> getUserSummaryVoicesByPhotoCategories( final User user ) {
		return photoVotingDao.getUserSummaryVoicesByPhotoCategories( user );
	}

	@Override
	public int getPhotoMarksForPeriod( final int photoId, final Date timeFrom, final Date timeTo ) {
		return photoVotingDao.getPhotoMarksForPeriod( photoId, timeFrom, timeTo );
	}

	@Override
	public List<UserRating> getUserRatingForPeriod( final Date timeFrom, final Date timeTo, final int limit ) {
		return photoVotingDao.getUserRatingForPeriod( timeFrom, timeTo, limit );
	}

	@Override
	public TimeRange getTopBestDateRange() {
		final int days = configurationService.getInt( ConfigurationKey.PHOTO_RATING_CALCULATE_MARKS_FOR_THE_BEST_PHOTOS_FOR_LAST_DAYS );
		return new TimeRange( dateUtilsService.getFirstSecondOfDay( dateUtilsService.getDatesOffsetFromCurrentDate( -days + 1 ) ), dateUtilsService.getLastSecondOfToday() );
	}

	@Override
	public int getSummaryPhotoMark( final Photo photo ) {
		int sumMark = 0;

		final List<MarksByCategoryInfo> marksByCategories = getPhotoSummaryVoicesByPhotoCategories( photo );
		for ( final MarksByCategoryInfo marksByCategory : marksByCategories ) {
			sumMark += marksByCategory.getSumMark();
		}

		return sumMark;
	}
}
