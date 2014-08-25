package core.services.photo;

import core.general.configuration.ConfigurationKey;
import core.general.data.PhotoListCriterias;
import core.general.data.PhotoSort;
import core.general.data.TimeRange;
import core.general.genre.Genre;
import core.general.photo.PhotoVotingCategory;
import core.general.user.User;
import core.general.user.UserMembershipType;
import core.services.system.ConfigurationService;
import core.services.utils.DateUtilsService;
import core.services.utils.sql.PhotoQueryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import utils.PhotoUtils;

import java.util.Date;

public class PhotoListCriteriasServiceImpl implements PhotoListCriteriasService {

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Override
	public PhotoListCriterias getForAppraisedByUserPhotos( final PhotoVotingCategory votingCategory, final User votedUser, final User accessor ) {
		final PhotoListCriterias criterias = new PhotoListGallery().getPhotoListCriterias( accessor );

		criterias.setMinimalMarks( PhotoQueryServiceImpl.MIN_POSSIBLE_MARK );
		criterias.setVotingCategory( votingCategory );
		criterias.setVotedUser( votedUser );

		return criterias;
	}

	@Override
	public PhotoListCriterias getForPeriod( final Date dateFrom, final Date dateTo, final User accessor ) {
		final PhotoListCriterias criterias = new PhotoListGallery().getPhotoListCriterias( accessor );

		addUploadDateCriteria( criterias, dateFrom, dateTo );

		return criterias;
	}

	@Override
	public PhotoListCriterias getForPeriodBest( final Date dateFrom, final Date dateTo, final User accessor ) {
		final PhotoListCriterias criterias = new PhotoListAbsolutelyBest().getPhotoListCriterias( accessor );

		addVotingDateCriteria( criterias, dateFrom, dateTo );

		return criterias;
	}

	@Override
	public PhotoListCriterias getForMembershipType( UserMembershipType membershipType, final User accessor ) {
		final PhotoListCriterias criterias = new PhotoListGallery().getPhotoListCriterias( accessor );

		criterias.setMembershipType( membershipType );

		return criterias;
	}

	@Override
	public PhotoListCriterias getForMembershipTypeTopBest( UserMembershipType membershipType, final User accessor ) {
		final PhotoListCriterias criterias = new PhotoListTopBest().getPhotoListCriterias( accessor );

		criterias.setMembershipType( membershipType );

		return criterias;
	}

	@Override
	public PhotoListCriterias getForMembershipTypeBestForPeriod( UserMembershipType membershipType, final User accessor ) {
		final PhotoListCriterias criterias = new PhotoListBestForPeriod().getPhotoListCriterias( accessor );

		criterias.setMembershipType( membershipType );

		return criterias;
	}

	@Override
	public PhotoListCriterias getUserCardUserPhotosLast( final User user, final User accessor ) {
		final PhotoListCriterias criterias = new PhotoListGallery().getPhotoListCriterias( accessor );

		criterias.setUser( user );
		criterias.setPhotoQtyLimit( accessor.getPhotosOnPage() );

		return criterias;
	}

	protected void addUploadDateCriteria( final PhotoListCriterias criterias, final Date dateFrom, final Date dateTo ) {
		final TimeRange timeRangeToday = dateUtilsService.getTimeRangeFullDays( dateFrom, dateTo );
		criterias.setUploadDateFrom( timeRangeToday.getTimeFrom() );
		criterias.setUploadDateTo( timeRangeToday.getTimeTo() );
	}

	protected void addVotingDateCriteria( final PhotoListCriterias criterias, final Date dateFrom, final Date dateTo ) {
		criterias.setVotingTimeFrom( dateUtilsService.getFirstSecondOfDay( dateFrom ) );
		criterias.setVotingTimeTo( dateUtilsService.getLastSecondOfDay( dateTo ) );
	}

	private int getSystemDefaultMinMarks() {
		return configurationService.getInt( ConfigurationKey.PHOTO_RATING_MIN_MARKS_TO_BE_IN_THE_BEST_PHOTO );
	}

	private int getLastDaysForMarksCalculations() {
		return configurationService.getInt( ConfigurationKey.PHOTO_RATING_CALCULATE_MARKS_FOR_THE_BEST_PHOTOS_FOR_LAST_DAYS );
	}

	private abstract class AbstractPhotoListCriterias {

		public abstract PhotoListCriterias getPhotoListCriterias( final User accessor );

		protected void addVotingDateCriteriaFromCurrentDate( final PhotoListCriterias criterias ) {
			final Date dateFrom = dateUtilsService.getDatesOffsetFromCurrentDate( -getLastDaysForMarksCalculations() );
			final Date dateTo = dateUtilsService.getCurrentDate();
			addVotingDateCriteria( criterias, dateFrom, dateTo );
		}
	}

	private class PhotoListGallery extends AbstractPhotoListCriterias {

		@Override
		public PhotoListCriterias getPhotoListCriterias( final User accessor ) {
			final PhotoListCriterias criterias = new PhotoListCriterias();

			criterias.setPhotoSort( PhotoSort.UPLOAD_TIME );

			return criterias;
		}
	}

	private abstract class AbstractVotePhotoListCriterias extends AbstractPhotoListCriterias {

		@Override
		public PhotoListCriterias getPhotoListCriterias( final User accessor ) {
			final PhotoListCriterias criterias = new PhotoListCriterias();

			criterias.setMinimalMarks( getSystemDefaultMinMarks() );
			criterias.setPhotoSort( PhotoSort.SUM_MARKS );

			return criterias;
		}
	}

	private class PhotoListTopBest extends AbstractVotePhotoListCriterias {

		@Override
		public PhotoListCriterias getPhotoListCriterias( final User accessor ) {
			final PhotoListCriterias criterias = super.getPhotoListCriterias( accessor );

			addVotingDateCriteriaFromCurrentDate( criterias );
			criterias.setTopBestPhotoList( true );

			criterias.setPhotoQtyLimit( configurationService.getInt( ConfigurationKey.PHOTO_LIST_PHOTO_TOP_QTY ) );

			return criterias;
		}
	}

	private class PhotoListBestForPeriod extends AbstractVotePhotoListCriterias {

		@Override
		public PhotoListCriterias getPhotoListCriterias( final User accessor ) {
			final PhotoListCriterias criterias = super.getPhotoListCriterias( accessor );

			addVotingDateCriteriaFromCurrentDate( criterias );

			criterias.setPhotoQtyLimit( accessor.getPhotosOnPage() );

			return criterias;
		}
	}

	private class PhotoListAbsolutelyBest extends AbstractVotePhotoListCriterias {

		@Override
		public PhotoListCriterias getPhotoListCriterias( final User accessor ) {
			final PhotoListCriterias criterias = super.getPhotoListCriterias( accessor );

			criterias.setPhotoQtyLimit( accessor.getPhotosOnPage() );

			return criterias;
		}
	}

	public void setConfigurationService( final ConfigurationService configurationService ) {
		this.configurationService = configurationService;
	}

	public void setDateUtilsService( final DateUtilsService dateUtilsService ) {
		this.dateUtilsService = dateUtilsService;
	}
}
