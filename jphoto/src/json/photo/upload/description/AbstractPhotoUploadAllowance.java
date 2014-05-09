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
		} else {
			final PhotoUploadDescription description = new PhotoUploadDescription();
			description.setUploadRuleDescription( services.getTranslatorService().translate( "Please, select a genre to see full photo upload allowance", language ) );
			photoUploadDescriptions.add( description );
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

			final TranslatableMessage translatableMessage = new TranslatableMessage( "Your status' limit is $1 photo(s) per $2. You uploaded $3 photo(s) $4.", services )
				.addIntegerParameter( limitPhotosQty )
				.translatableString( period2 )
				.addIntegerParameter( uploadedPhotosQty )
				.translatableString( period1 )
				;
			if ( userCanUploadPhoto ) {
				translatableMessage.string( " " );
				final int canBeUploadedPhotos = limitPhotosQty - uploadedPhotosQty;
				if ( canBeUploadedPhotos > 0 ) {
					final TranslatableMessage message = new TranslatableMessage( "You can upload $1 photo(s) more $2.", services )
						.addIntegerParameter( canBeUploadedPhotos )
						.translatableString( period1 )
						;
					translatableMessage.addTranslatableMessageParameter( message );
				} else {
					final TranslatableMessage message = new TranslatableMessage( "You can not upload photo $1.", services )
						.translatableString( period1 )
						;
					translatableMessage.addTranslatableMessageParameter( message );
					uploadDescription.setPassed( false );
					userCanUploadPhoto = false;
					setNextPhotoUploadTime( nextVotingTime );
				}
			}

			uploadDescription.setUploadRuleDescription( translatableMessage.build( language ) );

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

			final TranslatableMessage translatableMessage = new TranslatableMessage( "Your status' limit is $1 $2 per $3. You uploaded $4 $5 $6.", services )
				.addIntegerParameter( uploadSizeLimit )
				.translatableString( ConfigurationKey.CANDIDATES_DAILY_FILE_SIZE_LIMIT.getUnit().getName() )
				.translatableString( period1 )
				.addFloatParameter( uploadedSummarySize )
				.translatableString( ConfigurationKey.CANDIDATES_DAILY_FILE_SIZE_LIMIT.getUnit().getName() )
				.translatableString( period2 )
				;

			if ( userCanUploadPhoto ) {
				translatableMessage.string( " " );
				final float canUploadKb = uploadSizeLimit - uploadedSummarySize;
				if ( canUploadKb > 0 ) {
					final TranslatableMessage message = new TranslatableMessage( "You can upload $1 Kb more $2.", services )
						.addFloatParameter( canUploadKb )
						.translatableString( period2 )
						;
					translatableMessage.addTranslatableMessageParameter( message );
				} else {
					final TranslatableMessage message = new TranslatableMessage( "You can not upload photo $1.", services ).translatableString( period1 );
					translatableMessage.addTranslatableMessageParameter( message );
					uploadDescription.setPassed( false );
					userCanUploadPhoto = false;
					setNextPhotoUploadTime( nextVotingTime );
				}
			}

			uploadDescription.setUploadRuleDescription( translatableMessage.build( language ) );

			photoUploadDescriptions.add( uploadDescription );
		}
	}

	private void addAdditionalWeeklyKbByGenreDescription( final List<PhotoUploadDescription> photoUploadDescriptions ) {
		final int additionalWeeklyLimitPerGenreRank = services.getConfigurationService().getInt( ConfigurationKey.PHOTO_UPLOAD_ADDITIONAL_SIZE_WEEKLY_LIMIT_PER_RANK_KB );
		if ( additionalWeeklyLimitPerGenreRank > 0 ) {

			final Language language = accessor.getLanguage();

			final int userRankInGenre = services.getUserRankService().getUserRankInGenre( photoAuthor.getId(), genre.getId() );

			final PhotoUploadDescription uploadDescription = new PhotoUploadDescription();

			final TranslatableMessage translatableMessage = new TranslatableMessage( "Each rank in a genre except first one increases your weekly limit on $1 Kb. Your rank in genre '$2' is $3.", services )
				.addIntegerParameter( additionalWeeklyLimitPerGenreRank )
				.addPhotosByUserByGenreLinkParameter( accessor, genre )
				.addIntegerParameter( userRankInGenre )
				.string( " " )
				;

			if ( userRankInGenre > 0 ) {
				final int additionalRankSize = ( userRankInGenre ) * additionalWeeklyLimitPerGenreRank;
				final TranslatableMessage message = new TranslatableMessage( "So it gives you possibility to upload on $1 Kb more this week.", services )
					.addIntegerParameter( additionalRankSize )
					;
				translatableMessage.addTranslatableMessageParameter( message );
			} else {
				translatableMessage.translatableString( "So it is too small yet to give you any bonuses :(." );
			}

			uploadDescription.setUploadRuleDescription( translatableMessage.build( language ) );

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
