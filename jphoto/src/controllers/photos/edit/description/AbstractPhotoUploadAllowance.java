package controllers.photos.edit.description;

import core.general.configuration.ConfigurationKey;
import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.user.User;
import core.general.user.UserStatus;
import core.services.photo.PhotoUploadService;
import core.services.system.ConfigurationService;
import core.services.translator.TranslatorService;
import core.services.user.UserRankService;
import core.services.utils.DateUtilsService;
import core.services.utils.ImageFileUtilsService;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public abstract class AbstractPhotoUploadAllowance {

	private final User photoAuthor;
	private User accessor;
	private Genre genre;

	private List<Photo> uploadThisWeekPhotos;
	private boolean userCanUploadPhoto = true;
	private Date nextPhotoUploadTime;

	protected ConfigurationService configurationService;

	protected PhotoUploadService photoUploadService;

	private UserRankService userRankService;

	private DateUtilsService dateUtilsService;

	private ImageFileUtilsService imageFileUtilsService;

	private TranslatorService translatorService;

	public abstract UserStatus getUserStatus();

	public abstract int getMaxPhotoSize();

	public abstract int getDailyLimitPhotosQty();

	public abstract int getWeeklyLimitPhotosQty();

	public abstract int getDailyLimitUploadSize();

	public abstract int getWeeklyLimitUploadSize();

	public AbstractPhotoUploadAllowance( final User photoAuthor, final User accessor ) {
		this.photoAuthor = photoAuthor;
		this.accessor = accessor;
	}

	public List<PhotoUploadDescription> getUploadAllowance() {
		final List<PhotoUploadDescription> photoUploadDescriptions = newArrayList();

		addMaxPhotoSizeDescription( photoUploadDescriptions );

		addDailyPhotosQtyDescription( photoUploadDescriptions );

		addWeeklyPhotosQtyDescription( photoUploadDescriptions );

		addDailyPhotosSizeDescription( photoUploadDescriptions );

		if ( genre != null ) {
			addWeeklyPhotosSizeDescription( photoUploadDescriptions );

			addAdditionalWeeklyKbByGenreDescription( photoUploadDescriptions );
		}

		return photoUploadDescriptions;
	}

	private void addMaxPhotoSizeDescription( final List<PhotoUploadDescription> photoUploadDescriptions ) {
		final PhotoUploadDescription uploadDescription = new PhotoUploadDescription();

		uploadDescription.setUploadRuleDescription( translatorService.translate( "Max photo's size you can upload is $1 $2.", String.valueOf( getMaxPhotoSize() ), ConfigurationKey.PHOTO_UPLOAD_ADDITIONAL_SIZE_WEEKLY_LIMIT_PER_RANK_KB.getUnit().getName() ) );

		photoUploadDescriptions.add( uploadDescription );
	}

	private void addDailyPhotosQtyDescription( final List<PhotoUploadDescription> photoUploadDescriptions ) {
		final String period1 = "today";
		final String period2 = "day";
		final int limitPhotosQty = getDailyLimitPhotosQty();
		final List<Integer> uploadedPhotosIds = photoUploadService.getUploadedTodayPhotosIds( photoAuthor.getId() );

		addPhotoQtyLimitDescription( photoUploadDescriptions, period1, period2, limitPhotosQty, uploadedPhotosIds.size(), dateUtilsService.getFirstSecondOfTomorrow() );
	}

	private void addWeeklyPhotosQtyDescription( final List<PhotoUploadDescription> photoUploadDescriptions ) {
		final String period1 = "this week";
		final String period2 = "week";
		final int limitPhotosQty = getWeeklyLimitPhotosQty();
		final List<Integer> uploadedPhotosIds = photoUploadService.getUploadedThisWeekPhotosIds( photoAuthor.getId() );

		addPhotoQtyLimitDescription( photoUploadDescriptions, period1, period2, limitPhotosQty, uploadedPhotosIds.size(), dateUtilsService.getFirstSecondOfNextMonday() );
	}

	private void addPhotoQtyLimitDescription( final List<PhotoUploadDescription> photoUploadDescriptions, final String period1, final String period2, final int limitPhotosQty, final int uploadedPhotosQty, final Date nextVotingTime ) {
		if ( limitPhotosQty > 0 ) {

			final PhotoUploadDescription uploadDescription = new PhotoUploadDescription();

			final StringBuilder builder = new StringBuilder();

			builder.append( translatorService.translate( "Your status' limit is $1 photo(s) per $2. ", String.valueOf( limitPhotosQty ), period2 ) );
			builder.append( translatorService.translate( "You uploaded $1 photo(s) $2. ", String.valueOf( uploadedPhotosQty ), period1 ) );
			if ( userCanUploadPhoto ) {
				final int canBeUploadedPhotos = limitPhotosQty - uploadedPhotosQty;
				if ( canBeUploadedPhotos > 0 ) {
					builder.append( translatorService.translate( "You can upload $1 photo(s) more $2.", String.valueOf( canBeUploadedPhotos ), period1 ) );
				} else {
					builder.append( translatorService.translate( "You can not upload photo $1.", period1 ) );
					uploadDescription.setPassed( false );
					userCanUploadPhoto = false;
					setNextPhotoUploadTime( nextVotingTime );
				}
			}

			uploadDescription.setUploadRuleDescription( builder.toString() );

			photoUploadDescriptions.add( uploadDescription );
		}
	}

	private void addDailyPhotosSizeDescription( final List<PhotoUploadDescription> photoUploadDescriptions ) {
		final String period1 = "today";
		final String period2 = "day";
		final int uploadSizeLimit = getDailyLimitUploadSize();
		final long uploadedSummarySize = photoUploadService.getUploadedTodayPhotosSummarySize( photoAuthor.getId() );

		getPhotoSizeLimitDescription( photoUploadDescriptions, period1, period2, uploadSizeLimit, imageFileUtilsService.getFileSizeInKb( uploadedSummarySize ), dateUtilsService.getFirstSecondOfTomorrow() );
	}

	private void addWeeklyPhotosSizeDescription( final List<PhotoUploadDescription> photoUploadDescriptions ) {
		final String period1 = "week";
		final String period2 = "this week";
		final int uploadSizeLimit = getWeeklyLimitUploadSize();
		final long uploadedSummarySize = photoUploadService.getUploadedThisWeekPhotosSummarySize( photoAuthor.getId() );

		getPhotoSizeLimitDescription( photoUploadDescriptions, period1, period2, uploadSizeLimit, imageFileUtilsService.getFileSizeInKb( uploadedSummarySize ), dateUtilsService.getFirstSecondOfNextMonday() );
	}

	private void getPhotoSizeLimitDescription( final List<PhotoUploadDescription> photoUploadDescriptions, final String period1, final String period2, final int uploadSizeLimit, final float uploadedSummarySize, final Date nextVotingTime ) {
		if ( uploadSizeLimit > 0 ) {
			final PhotoUploadDescription uploadDescription = new PhotoUploadDescription();

			final StringBuilder builder = new StringBuilder();

			final String unit = ConfigurationKey.CANDIDATES_DAILY_FILE_SIZE_LIMIT.getUnit().getName();
			builder.append( translatorService.translate( "Your status' limit is $1 $2 per $3. ", String.valueOf( uploadSizeLimit ), unit, period2 ) );
			builder.append( translatorService.translate( "You uploaded $1 $2 $3. ", String.valueOf( uploadedSummarySize ), unit, period1 ) );
			if ( userCanUploadPhoto ) {
				final float canUploadKb = uploadSizeLimit - uploadedSummarySize;
				if ( canUploadKb > 0 ) {
					builder.append( translatorService.translate( "You can upload $1 Kb more $3.", String.valueOf( canUploadKb ), period1, period2 ) );
				} else {
					builder.append( translatorService.translate( "You can not upload photo $1.", period1 ) );
					uploadDescription.setPassed( false );
					userCanUploadPhoto = false;
					setNextPhotoUploadTime( nextVotingTime );
				}
			}

			uploadDescription.setUploadRuleDescription( builder.toString() );

			photoUploadDescriptions.add( uploadDescription );
		}
	}

	private void addAdditionalWeeklyKbByGenreDescription( final List<PhotoUploadDescription> photoUploadDescriptions ) {
		final int additionalWeeklyLimitPerGenreRank = configurationService.getInt( ConfigurationKey.PHOTO_UPLOAD_ADDITIONAL_SIZE_WEEKLY_LIMIT_PER_RANK_KB );
		if ( additionalWeeklyLimitPerGenreRank > 0 ) {

			final int userRankInGenre = userRankService.getUserRankInGenre( photoAuthor.getId(), genre.getId() );

			final PhotoUploadDescription uploadDescription = new PhotoUploadDescription();

			final StringBuilder builder = new StringBuilder();

			builder.append( translatorService.translate( "Each rank in a genre except first ont increases your weekly limit on $1 Kb.", additionalWeeklyLimitPerGenreRank ) );
			builder.append( translatorService.translate( "Your rank in genre '$1' is $2.", translatorService.translateGenre( genre, accessor.getLanguage() ), String.valueOf( userRankInGenre ) ) );
			if ( userRankInGenre > 0 ) {
				final int additionalRankSize = ( userRankInGenre ) * additionalWeeklyLimitPerGenreRank;
				builder.append( translatorService.translate( "So it gives you possibility to upload on $1 Kb more this week.", additionalRankSize ) );
			} else {
				builder.append( translatorService.translate( "So it is too small to give you any bonuses :(." ) );
			}

			uploadDescription.setUploadRuleDescription( builder.toString() );

			photoUploadDescriptions.add( uploadDescription );
		}
	}

	private void setNextPhotoUploadTime( final Date nextTime ) {
		if ( nextPhotoUploadTime == null || nextPhotoUploadTime.getTime() < nextTime.getTime() ) {
			nextPhotoUploadTime = nextTime;
		}
	}

	public User getPhotoAuthor() {
		return photoAuthor;
	}

	public List<Photo> getUploadThisWeekPhotos() {
		return uploadThisWeekPhotos;
	}

	public void setUploadThisWeekPhotos( final List<Photo> uploadThisWeekPhotos ) {
		this.uploadThisWeekPhotos = uploadThisWeekPhotos;
	}

	public boolean isUserCanUploadPhoto() {
		return userCanUploadPhoto;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre( final Genre genre ) {
		this.genre = genre;
	}

	public Date getNextPhotoUploadTime() {
		return nextPhotoUploadTime;
	}

	public void setConfigurationService( final ConfigurationService configurationService ) {
		this.configurationService = configurationService;
	}

	public void setPhotoUploadService( final PhotoUploadService photoUploadService ) {
		this.photoUploadService = photoUploadService;
	}

	public void setUserRankService( final UserRankService userRankService ) {
		this.userRankService = userRankService;
	}

	public void setDateUtilsService( final DateUtilsService dateUtilsService ) {
		this.dateUtilsService = dateUtilsService;
	}

	public void setImageFileUtilsService( final ImageFileUtilsService imageFileUtilsService ) {
		this.imageFileUtilsService = imageFileUtilsService;
	}

	public void setTranslatorService( final TranslatorService translatorService ) {
		this.translatorService = translatorService;
	}
}
