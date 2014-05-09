package json.photo.upload.description;

import core.general.configuration.ConfigurationKey;
import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.user.User;
import core.general.user.UserStatus;
import core.services.system.Services;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.translator.message.TranslatableMessage;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public abstract class AbstractPhotoUploadAllowance {

	private final User photoAuthor;
	private User accessor;
	private Genre genre;

	protected Language language;
	protected Services services;

	private List<Photo> uploadThisWeekPhotos;
	private boolean userCanUploadPhoto = true;
	private Date nextPhotoUploadTime;

	public abstract UserStatus getUserStatus();

	public abstract int getMaxPhotoSize();

	public abstract int getDailyLimitPhotosQty();

	public abstract int getWeeklyLimitPhotosQty();

	public abstract int getDailyLimitUploadSize();

	public abstract int getWeeklyLimitUploadSize();

	public AbstractPhotoUploadAllowance( final User photoAuthor, final User accessor, final Language language, final Services services ) {
		this.photoAuthor = photoAuthor;
		this.accessor = accessor;
		this.language = language;
		this.services = services;
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

		final TranslatableMessage translatableMessage = new TranslatableMessage( "Max photo's size you can upload is $1 $2.", services )
			.addIntegerParameter( getMaxPhotoSize() )
			.translatableString( ConfigurationKey.PHOTO_UPLOAD_ADDITIONAL_SIZE_WEEKLY_LIMIT_PER_RANK_KB.getUnit().getName() )
			;

		uploadDescription.setUploadRuleDescription( translatableMessage.build( language ) );

		photoUploadDescriptions.add( uploadDescription );
	}

	private void addDailyPhotosQtyDescription( final List<PhotoUploadDescription> photoUploadDescriptions ) {
		final String period1 = "photo uploading: today";
		final String period2 = "photo uploading: day";
		final int limitPhotosQty = getDailyLimitPhotosQty();
		final List<Integer> uploadedPhotosIds = services.getPhotoUploadService().getUploadedTodayPhotosIds( photoAuthor.getId() );

		addPhotoQtyLimitDescription( photoUploadDescriptions, period1, period2, limitPhotosQty, uploadedPhotosIds.size(), services.getDateUtilsService().getFirstSecondOfTomorrow() );
	}

	private void addWeeklyPhotosQtyDescription( final List<PhotoUploadDescription> photoUploadDescriptions ) {
		final String period1 = "photo uploading: this week";
		final String period2 = "photo uploading: week";
		final int limitPhotosQty = getWeeklyLimitPhotosQty();
		final List<Integer> uploadedPhotosIds = services.getPhotoUploadService().getUploadedThisWeekPhotosIds( photoAuthor.getId() );

		addPhotoQtyLimitDescription( photoUploadDescriptions, period1, period2, limitPhotosQty, uploadedPhotosIds.size(), services.getDateUtilsService().getFirstSecondOfNextMonday() );
	}

	private void addPhotoQtyLimitDescription( final List<PhotoUploadDescription> photoUploadDescriptions, final String period1, final String period2, final int limitPhotosQty, final int uploadedPhotosQty, final Date nextVotingTime ) {
		if ( limitPhotosQty > 0 ) {

			final Language language = accessor.getLanguage();

			final PhotoUploadDescription uploadDescription = new PhotoUploadDescription();

			final StringBuilder builder = new StringBuilder();

			final TranslatorService translatorService = services.getTranslatorService();
			final String period1_t = translatorService.translate( period1, language );
			final String period2_t = translatorService.translate( period2, language );

			builder.append( translatorService.translate( "Your status' limit is $1 photo(s) per $2.", language, String.valueOf( limitPhotosQty ), period2_t ) ).append( " " );
			builder.append( translatorService.translate( "You uploaded $1 photo(s) $2.", language, String.valueOf( uploadedPhotosQty ), period1_t ) ).append( " " );
			if ( userCanUploadPhoto ) {
				final int canBeUploadedPhotos = limitPhotosQty - uploadedPhotosQty;
				if ( canBeUploadedPhotos > 0 ) {
					builder.append( translatorService.translate( "You can upload $1 photo(s) more $2.", language, String.valueOf( canBeUploadedPhotos ), period1_t ) );
				} else {
					builder.append( translatorService.translate( "You can not upload photo $1.", language, period1_t ) );
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
		final String period1 = "photo uploading: today";
		final String period2 = "photo uploading: day";
		final int uploadSizeLimit = getDailyLimitUploadSize();
		final long uploadedSummarySize = services.getPhotoUploadService().getUploadedTodayPhotosSummarySize( photoAuthor.getId() );

		getPhotoSizeLimitDescription( photoUploadDescriptions, period1, period2, uploadSizeLimit, services.getImageFileUtilsService().getFileSizeInKb( uploadedSummarySize ), services.getDateUtilsService().getFirstSecondOfTomorrow() );
	}

	private void addWeeklyPhotosSizeDescription( final List<PhotoUploadDescription> photoUploadDescriptions ) {
		final String period1 = "photo uploading: week";
		final String period2 = "photo uploading: this week";
		final int uploadSizeLimit = getWeeklyLimitUploadSize();
		final long uploadedSummarySize = services.getPhotoUploadService().getUploadedThisWeekPhotosSummarySize( photoAuthor.getId() );

		getPhotoSizeLimitDescription( photoUploadDescriptions, period1, period2, uploadSizeLimit, services.getImageFileUtilsService().getFileSizeInKb( uploadedSummarySize ), services.getDateUtilsService().getFirstSecondOfNextMonday() );
	}

	private void getPhotoSizeLimitDescription( final List<PhotoUploadDescription> photoUploadDescriptions, final String period1, final String period2, final int uploadSizeLimit, final float uploadedSummarySize, final Date nextVotingTime ) {
		if ( uploadSizeLimit > 0 ) {
			final PhotoUploadDescription uploadDescription = new PhotoUploadDescription();

			final StringBuilder builder = new StringBuilder();

			final String unit = ConfigurationKey.CANDIDATES_DAILY_FILE_SIZE_LIMIT.getUnit().getName();
			final TranslatorService translatorService = services.getTranslatorService();
			builder.append( translatorService.translate( "Your status' limit is $1 $2 per $3. ", accessor.getLanguage(), String.valueOf( uploadSizeLimit ), unit, period2 ) );
			builder.append( translatorService.translate( "You uploaded $1 $2 $3. ", accessor.getLanguage(), String.valueOf( uploadedSummarySize ), unit, period1 ) );
			if ( userCanUploadPhoto ) {
				final float canUploadKb = uploadSizeLimit - uploadedSummarySize;
				if ( canUploadKb > 0 ) {
					builder.append( translatorService.translate( "You can upload $1 Kb more $3.", accessor.getLanguage(), String.valueOf( canUploadKb ), period1, period2 ) );
				} else {
					builder.append( translatorService.translate( "You can not upload photo $1.", accessor.getLanguage(), period1 ) );
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
		final int additionalWeeklyLimitPerGenreRank = services.getConfigurationService().getInt( ConfigurationKey.PHOTO_UPLOAD_ADDITIONAL_SIZE_WEEKLY_LIMIT_PER_RANK_KB );
		if ( additionalWeeklyLimitPerGenreRank > 0 ) {

			final Language language = accessor.getLanguage();

			final int userRankInGenre = services.getUserRankService().getUserRankInGenre( photoAuthor.getId(), genre.getId() );

			final PhotoUploadDescription uploadDescription = new PhotoUploadDescription();

			final StringBuilder builder = new StringBuilder();

			final TranslatorService translatorService = services.getTranslatorService();
			builder.append( translatorService.translate( "Each rank in a genre except first ont increases your weekly limit on $1 Kb.", language, String.valueOf( additionalWeeklyLimitPerGenreRank ) ) );
			builder.append( translatorService.translate( "Your rank in genre '$1' is $2.", language, translatorService.translateGenre( genre, language ), String.valueOf( userRankInGenre ) ) );
			if ( userRankInGenre > 0 ) {
				final int additionalRankSize = ( userRankInGenre ) * additionalWeeklyLimitPerGenreRank;
				builder.append( translatorService.translate( "So it gives you possibility to upload on $1 Kb more this week.", language, String.valueOf( additionalRankSize ) ) );
			} else {
				builder.append( translatorService.translate( "So it is too small to give you any bonuses :(.", language ) );
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
}
