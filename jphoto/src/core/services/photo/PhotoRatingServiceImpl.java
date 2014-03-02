package core.services.photo;

import core.general.data.PhotoMarksForPeriod;
import core.general.data.PhotoRating;
import core.general.data.TimeRange;
import core.services.dao.PhotoRatingDao;
import core.services.dao.PhotoVotingDao;
import core.services.utils.DateUtilsService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;


public class PhotoRatingServiceImpl implements PhotoRatingService {

	@Autowired
	private PhotoVotingDao photoVotingDao;

	@Autowired
	private final PhotoRatingDao photoRatingDao;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	public PhotoRatingServiceImpl( final PhotoRatingDao photoRatingDao ) {
		this.photoRatingDao = photoRatingDao;
	}

	@Override
	public void recalculatePhotoRatingForPeriodInDB( final Date timeFrom, final Date timeTo ) {
		final List<PhotoMarksForPeriod> marksForPeriods = photoVotingDao.getSummaryPhotoVotingForPeriodSortedBySummaryMarkDesc( timeFrom, timeTo );

		if ( marksForPeriods.size() == 0 ) {
			return;
		}

		synchronized ( photoRatingDao ) {
			photoRatingDao.deletePhotosRatingsForPeriod( timeFrom, timeTo );

			int ratingPosition = 1;
			for ( final PhotoMarksForPeriod marksForPeriod : marksForPeriods ) {

				final PhotoRating photoRating = new PhotoRating();

				photoRating.setPhotoId( marksForPeriod.getPhotoId() );
				photoRating.setTimeFrom( timeFrom );
				photoRating.setTimeTo( timeTo );
				photoRating.setRatingPosition( ratingPosition );
				photoRating.setSummaryMark( marksForPeriod.getSumMarks() );

				photoRatingDao.savePhotoRatingForPeriod( photoRating );

				ratingPosition++;
			}
		}
	}

	@Override
	public PhotoRating getPhotoRatingForPeriod( final int photoId, final Date timeFrom, final Date timeTo ) {
		return photoRatingDao.getPhotoRatingsForPeriod( photoId, timeFrom, timeTo );
	}

	@Override
	public PhotoRating getPhotoRatingToday( final int photoId ) {
		final TimeRange timeRangeToday = dateUtilsService.getTimeRangeToday();

		return getPhotoRatingForPeriod( photoId, timeRangeToday.getTimeFrom(), timeRangeToday.getTimeTo() );
	}

	@Override
	public void deletePhotoRatings( final int photoId ) {
		photoRatingDao.deletePhotoRatings( photoId );
	}
}
