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
import core.services.translator.TranslatorService;
import core.services.utils.DateUtilsService;
import core.services.utils.UtilsService;
import core.services.utils.sql.PhotoSqlHelperServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import utils.PhotoUtils;
import utils.StringUtilities;

import java.util.Date;


public class PhotoListCriteriasServiceImpl implements PhotoListCriteriasService {

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private UtilsService utilsService;
	
	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private TranslatorService translatorService;

	@Override
	public String getPhotoListTitle( final PhotoListCriterias criterias ) {
		final StringBuilder builder = new StringBuilder();

		if ( criterias.getUser() != null && criterias.getMinimalMarks() > PhotoSqlHelperServiceImpl.MIN_POSSIBLE_MARK ) {
			builder.append( translatorService.translate( "The very best of $1", criterias.getUser().getNameEscaped() ) );
			if ( criterias.getGenre() != null ) {
				builder.append( " " ).append( translatorService.translate( "photos from category '$1'", criterias.getGenre().getName() ) );
			}

			addVotingTimeText( criterias, builder );

			return builder.toString();
		}

		builder.append( translatorService.translate( "Photo gallery" ) );

		if ( criterias.getUser() != null ) {
			builder.append( " " ).append( translatorService.translate( "photos of member $1", criterias.getUser().getName() ) );
		}

		if ( criterias.getGenre() != null ) {
			builder.append( " " ).append( translatorService.translate( "photos from category '$1'", criterias.getGenre().getName() ) );
		}

		if ( criterias.getMinimalMarks() > PhotoSqlHelperServiceImpl.MIN_POSSIBLE_MARK ) {
			builder.append( ", " ).append( translatorService.translate( "that have got at least $1 marks", criterias.getMinimalMarks() ) );

			addVotingTimeText( criterias, builder );
		}

		return builder.toString();
	}

	@Override
	public String getPhotoListCriteriasDescription( final PhotoListCriterias criterias ) {
		final StringBuilder builder = new StringBuilder();

		if ( criterias.isTopBestPhotoList() ) {
			builder.append( translatorService.translate( "Top $1 best photos", criterias.getPhotoQtyLimit() ) ).append( " " );
		} else {
			builder.append( translatorService.translate( "Photos" ) );
		}

		if ( !thereAreNoFilterCriterias( criterias ) ) {
			if ( criterias.getUser() != null ) {
				builder.append( " " ).append( translatorService.translate( "photos of member $1", criterias.getUser().getName() ) );
			}

			if ( criterias.getGenre() != null ) {
				builder.append( " " ).append( translatorService.translate( "photos from category '$1'", criterias.getGenre().getName() ) );
			}

			final Date uploadDateTo = criterias.getUploadDateTo();
			final Date uploadDateFrom = criterias.getUploadDateFrom();

			if ( dateUtilsService.isNotEmptyTime( uploadDateFrom ) ) {
				builder.append( " " ).append( translatorService.translate( "posted in period from $1", dateUtilsService.formatDate( uploadDateFrom ) ) );
			}

			if ( dateUtilsService.isNotEmptyTime( uploadDateTo ) ) {
				if ( dateUtilsService.isEmptyTime( uploadDateFrom ) ) {
					builder.append( translatorService.translate( "posted in period" ) ).append( " " );
				}
				builder.append( " " ).append( translatorService.translate( "to $1", dateUtilsService.formatDate( uploadDateTo ) ) );
			}

			if ( criterias.getVotedUser() != null ) {
				builder.append( ", " ).append( translatorService.translate( "voted member:  $1", StringUtilities.escapeHtml( criterias.getVotedUser().getName() ) ) );
			}

			if ( criterias.getVotingCategory() != null ) {
				builder.append( ", " ).append( translatorService.translate( "category:  $1", StringUtilities.escapeHtml( criterias.getVotingCategory().getName() ) ) );
			}
		}

		if ( criterias.getMinimalMarks() > PhotoSqlHelperServiceImpl.MIN_POSSIBLE_MARK ) {
			builder.append( ", " ).append( translatorService.translate( "that have got at least $1 marks", criterias.getMinimalMarks() ) );

			addVotingTimeText( criterias, builder );
		}

		if ( criterias.getMembershipType() != null ) {
			builder.append( " " ).append( translatorService.translate( "uploaded by users with membership type '$1'", translatorService.translate( criterias.getMembershipType().getName() ) ) );
		}

		builder.append( "<br />" );
		builder.append( translatorService.translate( "Sorted by" ) ).append( " " );
		switch( criterias.getPhotoSort()  ) {
			case UPLOAD_TIME:
				builder.append( translatorService.translate( "upload time descending" ) );
				break;
			case SUM_MARKS:
				builder.append( translatorService.translate( "marks for period descending" ) );
				break;
		}

		return builder.toString();
	}

	private void addVotingTimeText( final PhotoListCriterias criterias, final StringBuilder builder ) {
		final Date votingTimeFrom = criterias.getVotingTimeFrom();
		final Date votingTimeTo = criterias.getVotingTimeTo();

		if ( dateUtilsService.isEmptyTime( votingTimeFrom ) || dateUtilsService.isEmptyTime( votingTimeTo ) ) {
			return;
		}

		final Date votingDateFrom = dateUtilsService.getFirstSecondOfDay( votingTimeFrom );
		final Date votingDateTo = dateUtilsService.getFirstSecondOfDay( votingTimeTo );

		if ( dateUtilsService.isNotEmptyTime( votingTimeFrom ) && votingDateFrom == votingDateTo ) {
			builder.append( translatorService.translate( "voting at $1", dateUtilsService.formatDate( votingTimeFrom ) ) );
		} else {
			if ( dateUtilsService.isNotEmptyTime( votingTimeFrom ) ) {
				builder.append( " " ).append( translatorService.translate( "voting time: $1", dateUtilsService.formatDate( votingTimeFrom ) ) ).append( " " );
			}

			if ( dateUtilsService.isNotEmptyTime( votingTimeTo ) ) {
				builder.append( String.format( " - %s ", dateUtilsService.formatDate( votingTimeTo ) ) );
			}
		}
	}

	@Override
	public String getLinkToFullListText( final PhotoListCriterias topBestPhotoListCriterias ) {
		return translatorService.translate( "See all photos" );
	}

	private boolean thereAreNoFilterCriterias( final PhotoListCriterias criterias ) {
		return criterias.getUser() == null
			   && criterias.getGenre() == null
			   && dateUtilsService.isEmptyTime( criterias.getUploadDateFrom() )
			   && dateUtilsService.isEmptyTime( criterias.getUploadDateTo() )
			   && criterias.getMembershipType() == null
			   && criterias.getVotingCategory() == null
			   && criterias.getVotedUser() == null
			;
	}

	// All photos -->
	@Override
	public PhotoListCriterias getForAllPhotos( final User user ) {
		return new PhotoListRegular().getPhotoListCriterias( user );
	}

	@Override
	public PhotoListCriterias getForAllPhotosTopBest( final User user ) {
		return new PhotoListTopBest().getPhotoListCriterias( user );
	}

	@Override
	public PhotoListCriterias getForAbsolutelyBest( final User user ) {
		return new PhotoListAbsolutelyBest().getPhotoListCriterias( user );
	}
	// All photos <--

	// Photos by genre -->
	@Override
	public PhotoListCriterias getForGenre( final Genre genre, final User user ) {
		final PhotoListCriterias criterias = new PhotoListRegular().getPhotoListCriterias( user );

		criterias.setGenre( genre );

		return criterias;
	}

	@Override
	public PhotoListCriterias getForGenreTopBest( final Genre genre, final User user ) {
		final PhotoListCriterias criterias = new PhotoListTopBest().getPhotoListCriterias( user );

		criterias.setMinimalMarks( PhotoUtils.getGenreMinBallForBest( genre, getSystemDefaultMinMarks() ) );
		criterias.setGenre( genre );

		return criterias;
	}

	@Override
	public PhotoListCriterias getForGenreBestForPeriod( final Genre genre, final User user ) {
		final PhotoListCriterias criterias = new PhotoListBestForPeriod().getPhotoListCriterias( user );

		criterias.setGenre( genre );

		return criterias;
	}

	@Override
	public PhotoListCriterias getForGenreAbsolutelyBest( final Genre genre, final User user ) {
		final PhotoListCriterias criterias = new PhotoListAbsolutelyBest().getPhotoListCriterias( user );

		criterias.setGenre( genre );

		return criterias;
	}
	// Photos by genre <--

	// Photos by user -->
	@Override
	public PhotoListCriterias getForUser( final User user, final User accessor ) {
		final PhotoListCriterias criterias = new PhotoListRegular().getPhotoListCriterias( accessor );

		criterias.setUser( user );

		return criterias;
	}

	@Override
	public PhotoListCriterias getForUserTopBest( final User user, final User accessor ) {
		final PhotoListCriterias criterias = new PhotoListTopBest().getPhotoListCriterias( accessor );

		criterias.setVotingTimeFrom( dateUtilsService.getEmptyTime() );
		criterias.setVotingTimeTo( dateUtilsService.getEmptyTime() );

		criterias.setMinimalMarks( PhotoSqlHelperServiceImpl.MIN_MARK_FOR_BEST );
		criterias.setUser( user );

		return criterias;
	}

	@Override
	public PhotoListCriterias getForUserBestForPeriod( final User user, final User accessor ) {
		final PhotoListCriterias criterias = new PhotoListBestForPeriod().getPhotoListCriterias( accessor );

		//		criterias.setMarksForLastNDays( PhotoSqlHelperServiceImpl.FROM_THE_BEFINNING_OF_TIME );
		criterias.setMinimalMarks( PhotoSqlHelperServiceImpl.MIN_MARK_FOR_BEST );
		criterias.setUser( user );

		return criterias;
	}

	@Override
	public PhotoListCriterias getForUserAbsolutelyBest( final User user, final User accessor ) {
		final PhotoListCriterias criterias = new PhotoListAbsolutelyBest().getPhotoListCriterias( accessor );

		criterias.setMinimalMarks( PhotoSqlHelperServiceImpl.MIN_MARK_FOR_BEST );
		criterias.setUser( user );

		criterias.setVotingTimeFrom( dateUtilsService.getEmptyTime() );
		criterias.setVotingTimeTo( dateUtilsService.getEmptyTime() );

		return criterias;
	}
	// Photos by user <--

	// Photos by user and genre -->
	@Override
	public PhotoListCriterias getForUserAndGenre( final User user, final Genre genre, final User accessor ) {
		final PhotoListCriterias criterias = new PhotoListRegular().getPhotoListCriterias( accessor );

		criterias.setUser( user );
		criterias.setGenre( genre );

		return criterias;
	}

	@Override
	public PhotoListCriterias getForUserAndGenreTopBest( final User user, final Genre genre, final User accessor ) {
		final PhotoListCriterias criterias = new PhotoListTopBest().getPhotoListCriterias( accessor );

		criterias.setVotingTimeFrom( dateUtilsService.getEmptyTime() );
		criterias.setVotingTimeTo( dateUtilsService.getEmptyTime() );

		criterias.setMinimalMarks( PhotoSqlHelperServiceImpl.MIN_MARK_FOR_BEST );
		criterias.setUser( user );
		criterias.setGenre( genre );

		return criterias;
	}

	@Override
	public PhotoListCriterias getForUserAndGenreBestForPeriod( final User user, final Genre genre, final User accessor ) {
		final PhotoListCriterias criterias = new PhotoListBestForPeriod().getPhotoListCriterias( accessor );

		criterias.setVotingTimeFrom( dateUtilsService.getEmptyTime() );
		criterias.setVotingTimeTo( dateUtilsService.getEmptyTime() );

		criterias.setMinimalMarks( PhotoSqlHelperServiceImpl.MIN_MARK_FOR_BEST );
		criterias.setUser( user );
		criterias.setGenre( genre );

		return criterias;
	}

	@Override
	public PhotoListCriterias getForUserAndGenreAbsolutelyBest( final User user, final Genre genre, final User accessor ) {
		final PhotoListCriterias criterias = new PhotoListAbsolutelyBest().getPhotoListCriterias( accessor );

		//		criterias.setMarksForLastNDays( PhotoSqlHelperServiceImpl.FROM_THE_BEFINNING_OF_TIME );
		criterias.setMinimalMarks( PhotoSqlHelperServiceImpl.MIN_MARK_FOR_BEST );
		criterias.setUser( user );
		criterias.setGenre( genre );

		return criterias;
	}
	// Photos by user and genre <--

	// Photos by votingCategory -->
	@Override
	public PhotoListCriterias getForVotedPhotos( final PhotoVotingCategory votingCategory, final User accessor ) {
		final PhotoListCriterias criterias = new PhotoListRegular().getPhotoListCriterias( accessor );

		criterias.setMinimalMarks( PhotoSqlHelperServiceImpl.MIN_POSSIBLE_MARK );
		criterias.setVotingCategory( votingCategory );

		return criterias;
	}

	@Override
	public PhotoListCriterias getForVotedPhotos( final User votedUser, final User accessor ) {
		final PhotoListCriterias criterias = new PhotoListRegular().getPhotoListCriterias( accessor );

		criterias.setMinimalMarks( PhotoSqlHelperServiceImpl.MIN_POSSIBLE_MARK );
		criterias.setVotedUser( votedUser );

		return criterias;
	}

	@Override
	public PhotoListCriterias getForVotedPhotos( final PhotoVotingCategory votingCategory, final User votedUser, final User accessor ) {
		final PhotoListCriterias criterias = new PhotoListRegular().getPhotoListCriterias( accessor );

		criterias.setMinimalMarks( PhotoSqlHelperServiceImpl.MIN_POSSIBLE_MARK );
		criterias.setVotingCategory( votingCategory );
		criterias.setVotedUser( votedUser );

		return criterias;
	}

	@Override
	public PhotoListCriterias getForVotingCategoryTopBest( final PhotoVotingCategory votingCategory, final User accessor ) {
		final PhotoListCriterias criterias = new PhotoListTopBest().getPhotoListCriterias( accessor );

		criterias.setMinimalMarks( PhotoSqlHelperServiceImpl.MIN_MARK_FOR_BEST );
		criterias.setVotingCategory( votingCategory );

		return criterias;
	}

	@Override
	public PhotoListCriterias getForVotingCategoryBestForPeriod( final PhotoVotingCategory votingCategory, final User accessor ) {
		final PhotoListCriterias criterias = new PhotoListBestForPeriod().getPhotoListCriterias( accessor );

		criterias.setMinimalMarks( PhotoSqlHelperServiceImpl.MIN_MARK_FOR_BEST );
		criterias.setVotingCategory( votingCategory );

		return criterias;
	}

	@Override
	public PhotoListCriterias getForVotingCategoryAbsolutelyBest( final PhotoVotingCategory votingCategory, final User accessor ) {
		final PhotoListCriterias criterias = new PhotoListAbsolutelyBest().getPhotoListCriterias( accessor );

		criterias.setMinimalMarks( PhotoSqlHelperServiceImpl.MIN_MARK_FOR_BEST );
		criterias.setVotingCategory( votingCategory );

		return criterias;
	}
	// Photos by votingCategory <--

	// Photos by Period -->
	@Override
	public PhotoListCriterias getForPeriod( final Date dateFrom, final Date dateTo, final User accessor ) {
		final PhotoListCriterias criterias = new PhotoListRegular().getPhotoListCriterias( accessor );

//		setDatePeriodCriterias( criterias, dateFrom, dateTo );

		addUploadDateCriteria( criterias, dateFrom, dateTo );

		return criterias;
	}

	@Override
	public PhotoListCriterias getForPeriodTopBest( final Date dateFrom, final Date dateTo, final User accessor ) {
		final PhotoListCriterias criterias = new PhotoListTopBest().getPhotoListCriterias( accessor );

		setDatePeriodCriterias( criterias, dateFrom, dateTo );

		return criterias;
	}

	@Override
	public PhotoListCriterias getForPeriodBest( final Date dateFrom, final Date dateTo, final User accessor ) {
		final PhotoListCriterias criterias = new PhotoListAbsolutelyBest().getPhotoListCriterias( accessor );

		addVotingDateCriteria( criterias, dateFrom, dateTo );

		return criterias;
	}
	// Photos by Period <--

	// Photos for MembershipType -->
	@Override
	public PhotoListCriterias getForMembershipType( UserMembershipType membershipType, final User accessor ) {
		final PhotoListCriterias criterias = new PhotoListRegular().getPhotoListCriterias( accessor );

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
	public PhotoListCriterias getForMembershipTypeAbsolutelyBest( UserMembershipType membershipType, final User accessor ) {
		final PhotoListCriterias criterias = new PhotoListAbsolutelyBest().getPhotoListCriterias( accessor );

		criterias.setMembershipType( membershipType );

		return criterias;
	}
	// Photos for MembershipType <--

	// user card -->
	@Override
	public PhotoListCriterias getUserCardUserPhotosBest( final User cardOwner, final User accessor ) {
		final PhotoListCriterias criterias = new PhotoListAbsolutelyBest().getPhotoListCriterias( accessor );

		criterias.setUser( cardOwner );
		criterias.setMinimalMarks( PhotoSqlHelperServiceImpl.MIN_MARK_FOR_BEST );
		criterias.setPhotoQtyLimit( accessor.getPhotoQtyOnPage() );

		return criterias;
	}

	@Override
	public PhotoListCriterias getUserCardUserPhotosLast( final User user, final User accessor ) {
		final PhotoListCriterias criterias = new PhotoListRegular().getPhotoListCriterias( accessor );

		criterias.setUser( user );
		criterias.setPhotoQtyLimit( accessor.getPhotoQtyOnPage() );

		return criterias;
	}

	@Override
	public PhotoListCriterias getUserCardLastVotedPhotos( final User user, final User accessor ) {
		final PhotoListCriterias criterias = new PhotoListRegular().getPhotoListCriterias( accessor );

		criterias.setVotedUser( user );
		criterias.setPhotoQtyLimit( accessor.getPhotoQtyOnPage() );
		criterias.setMinimalMarks( PhotoSqlHelperServiceImpl.MIN_POSSIBLE_MARK );
		criterias.setPhotoSort( PhotoSort.VOTING_TIME );

		return criterias;
	}
	// user card <--

	protected void addUploadDateCriteria( final PhotoListCriterias criterias, final Date dateFrom, final Date dateTo ) {
		final TimeRange timeRangeToday = dateUtilsService.getTimeRangeFullDays( dateFrom, dateTo );
		criterias.setUploadDateFrom( timeRangeToday.getTimeFrom() );
		criterias.setUploadDateTo( timeRangeToday.getTimeTo() );
	}

	private void setDatePeriodCriterias( final PhotoListCriterias criterias, final Date dateFrom, final Date dateTo ) {
		addUploadDateCriteria( criterias, dateFrom, dateTo );

		addVotingDateCriteria( criterias, dateFrom, dateTo );
	}

	protected void addVotingDateCriteria( final PhotoListCriterias criterias, final Date dateFrom, final Date dateTo ) {
		criterias.setVotingTimeFrom( dateUtilsService.getFirstSecondOfDay( dateFrom ) );
		criterias.setVotingTimeTo( dateUtilsService.getLastSecondOfDay( dateTo ) );
	}

	protected void resetVotingDateCriteria( final PhotoListCriterias criterias ) {
		criterias.setVotingTimeFrom( dateUtilsService.getEmptyTime() );
		criterias.setVotingTimeTo( dateUtilsService.getEmptyTime() );
	}

	private int getSystemDefaultMinMarks() {
		return configurationService.getInt( ConfigurationKey.PHOTO_RATING_MIN_MARKS_TO_BE_IN_THE_BEST_PHOTO );
	}

	private int getLastDaysForMarksCalculations() {
		return configurationService.getInt( ConfigurationKey.PHOTO_RATING_CALCULATE_MARKS_FOR_THE_BEST_PHOTOS_FOR_LAST_DAYS );
	}

	private abstract class AbstractPhotoListCriterias {

		public abstract PhotoListCriterias getPhotoListCriterias( final User user );

		protected void addVotingDateCriteriaFromCurrentDate( final PhotoListCriterias criterias ) {
			final Date dateFrom = dateUtilsService.getDatesOffsetFromCurrentDate( -getLastDaysForMarksCalculations() );
			final Date dateTo = dateUtilsService.getCurrentDate();
			addVotingDateCriteria( criterias, dateFrom, dateTo );
		}
	}

	private class PhotoListRegular extends AbstractPhotoListCriterias {

		@Override
		public PhotoListCriterias getPhotoListCriterias( final User user ) {
			final PhotoListCriterias criterias = new PhotoListCriterias();

			criterias.setPhotoSort( PhotoSort.UPLOAD_TIME );

			return criterias;
		}
	}

	private abstract class AbstractVotePhotoListCriterias extends AbstractPhotoListCriterias {

		@Override
		public PhotoListCriterias getPhotoListCriterias( final User user ) {
			final PhotoListCriterias criterias = new PhotoListCriterias();

			criterias.setMinimalMarks( getSystemDefaultMinMarks() );
			criterias.setPhotoSort( PhotoSort.SUM_MARKS );

			return criterias;
		}
	}

	private class PhotoListTopBest extends AbstractVotePhotoListCriterias {

		@Override
		public PhotoListCriterias getPhotoListCriterias( final User user ) {
			final PhotoListCriterias criterias = super.getPhotoListCriterias( user );

			addVotingDateCriteriaFromCurrentDate( criterias );
			criterias.setTopBestPhotoList( true );

			criterias.setPhotoQtyLimit( utilsService.getPhotosInLine( user ) );

			return criterias;
		}
	}

	private class PhotoListBestForPeriod extends AbstractVotePhotoListCriterias {

		@Override
		public PhotoListCriterias getPhotoListCriterias( final User user ) {
			final PhotoListCriterias criterias = super.getPhotoListCriterias( user );

			addVotingDateCriteriaFromCurrentDate( criterias );

			criterias.setPhotoQtyLimit( user.getPhotoQtyOnPage() );

			return criterias;
		}
	}

	private class PhotoListAbsolutelyBest extends AbstractVotePhotoListCriterias {

		@Override
		public PhotoListCriterias getPhotoListCriterias( final User user ) {
			final PhotoListCriterias criterias = super.getPhotoListCriterias( user );

			criterias.setPhotoQtyLimit( user.getPhotoQtyOnPage() );

			return criterias;
		}
	}

	public void setConfigurationService( final ConfigurationService configurationService ) {
		this.configurationService = configurationService;
	}

	public void setUtilsService( final UtilsService utilsService ) {
		this.utilsService = utilsService;
	}

	public void setDateUtilsService( final DateUtilsService dateUtilsService ) {
		this.dateUtilsService = dateUtilsService;
	}
}
