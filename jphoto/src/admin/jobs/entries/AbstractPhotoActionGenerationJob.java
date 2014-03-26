package admin.jobs.entries;

import admin.jobs.JobRuntimeEnvironment;
import admin.jobs.enums.DateRangeType;
import admin.jobs.enums.SavedJobType;
import core.enums.SavedJobParameterKey;
import core.general.base.CommonProperty;
import core.general.photo.Photo;
import core.general.photo.PhotoPreview;
import core.general.user.User;
import core.log.LogHelper;
import core.services.conversion.PhotoPreviewService;
import core.services.photo.PhotoService;
import core.services.translator.TranslatorService;
import core.services.user.UserService;
import core.services.utils.DateUtilsService;
import core.services.utils.EntityLinkUtilsService;
import core.services.utils.RandomUtilsService;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

public abstract class AbstractPhotoActionGenerationJob extends AbstractDateRangeableJob {

	private final static Integer MAX_ITERATIONS = 1000;

	protected final SavedJobType savedJobType;

	public abstract boolean doPhotoAction( final Photo photo, final User user );

	protected AbstractPhotoActionGenerationJob( final SavedJobType savedJobType, final LogHelper log, final JobRuntimeEnvironment jobEnvironment ) {
		super( log, jobEnvironment );
		this.savedJobType = savedJobType;
	}

	@Override
	protected void runJob() throws Throwable {

		final RandomUtilsService randomUtilsService = services.getRandomUtilsService();

		int emptyIterations = 0;
		while ( !isFinished() ) {

			final Photo photo = randomUtilsService.getRandomPhotoIds( beingProcessedPhotosIds );
			final User actionCommitter = randomUtilsService.getRandomUser( beingProcessedUsers );

			if ( doPhotoAction( photo, actionCommitter ) ) {
				increment();

				doActionForAnotherPhotoOfPhotoAuthor( photo.getUserId(), actionCommitter );
				doActionForPhotoOfAuthorsForWhomPhotoAuthorHasVotesRecently( photo.getUserId(), actionCommitter );
				emptyIterations = 0;
			} else {
				emptyIterations++;
			}

			if ( emptyIterations > MAX_ITERATIONS ) {
				getLog().debug( String.format( "%s empty iterations. Stopped.", MAX_ITERATIONS ) );
				break;
			}

			if ( hasJobFinishedWithAnyResult() ) {
				break;
			}
		}
	}

	@Override
	public SavedJobType getJobType() {
		return savedJobType;
	}

	@Override
	public Map<SavedJobParameterKey, CommonProperty> getParametersMap() {

		final Map<SavedJobParameterKey, CommonProperty> parametersMap = newHashMap();

		parametersMap.put( SavedJobParameterKey.PARAM_ACTIONS_QTY, new CommonProperty( SavedJobParameterKey.PARAM_ACTIONS_QTY.getId(), totalJopOperations ) );
		parametersMap.put( SavedJobParameterKey.PARAM_PHOTOS_QTY, new CommonProperty( SavedJobParameterKey.PARAM_PHOTOS_QTY.getId(), photoQtyLimit ) );

		getDateRangeParametersMap( parametersMap );

		return parametersMap;
	}

	@Override
	public String getJobParametersDescription() {
		final TranslatorService translatorService = services.getTranslatorService();

		final StringBuilder builder = new StringBuilder();

		builder.append( translate( "Actions" ) ).append( ": " ).append( totalJopOperations ).append( "<br />" );
		builder.append( translate( "Photos" ) ).append( ": " ).append( photoQtyLimit > 0 ? photoQtyLimit : translate( "all" ) ).append( "<br />" );

		builder.append( translate( "Actions type" ) ).append( ": " ).append( translate( getJobType().getName() ) ).append( "<br />" );

		addDateRangeParameters( builder );

		return builder.toString();
	}

	@Override
	public void initJobParameters( final Map<SavedJobParameterKey, CommonProperty> jobParameters ) {

		totalJopOperations = jobParameters.get( SavedJobParameterKey.PARAM_ACTIONS_QTY ).getValueInt();
		photoQtyLimit = jobParameters.get( SavedJobParameterKey.PARAM_PHOTOS_QTY ).getValueInt();

		setDateRangeParameters( jobParameters );
	}

	protected Date getPhotoActionTime( final Date photoUploadTime ) {

		if ( jobDateRange.getDateRangeType() == DateRangeType.CURRENT_TIME ) {
			return jobDateRange.getStartDate();
		}

		Date actionTimeFrom = jobDateRange.getStartDate();
		if ( jobDateRange.getStartDate().getTime() < photoUploadTime.getTime() ) {
			actionTimeFrom = photoUploadTime;
		}

		return services.getRandomUtilsService().getRandomDate( actionTimeFrom, jobDateRange.getEndDate() );
	}

	protected boolean savePhotoPreview( final Photo photo, final User user, final Date previewTime ) {

		final PhotoPreviewService photoPreviewService = services.getPhotoPreviewService();
		if ( photoPreviewService.hasUserAlreadySeenThisPhoto( photo.getId(), user.getId() ) ) {
			return false;
		}

		final PhotoPreview photoPreview = new PhotoPreview( photo, user );
		photoPreview.setPreviewTime( previewTime );
		photoPreviewService.save( photoPreview );

		getLog().info( String.format( "User %s has seen photo %s ( time: %s )", user, photo, getDateUtilsService().formatDateTime( previewTime ) ) );

		services.getUsersSecurityService().saveLastUserActivityTime( user.getId(), previewTime );

		return true;
	}

	private DateUtilsService getDateUtilsService() {
		return services.getDateUtilsService();
	}

	private void doActionForAnotherPhotoOfPhotoAuthor( final int photoAuthorId, final User actionCommitter ) {
		final RandomUtilsService randomUtilsService = services.getRandomUtilsService();
		final User photoAuthor = services.getUserService().load( photoAuthorId );

		final boolean isHasVotedUserGoingToDoTheActionForAnotherPhotoOfPhotoAuthor = randomUtilsService.getRandomBoolean();
		if ( isHasVotedUserGoingToDoTheActionForAnotherPhotoOfPhotoAuthor ) {

			final EntityLinkUtilsService entityLinkUtilsService = services.getEntityLinkUtilsService();
			addJobExecutionFinalMessage( String.format( "User %s is going to do the action for another photo of %s", entityLinkUtilsService.getUserCardLink( actionCommitter ), entityLinkUtilsService.getUserCardLink( photoAuthor ) ) );

			final Photo randomPhotoOfPhotoAuthor = randomUtilsService.getRandomPhotoOfUser( photoAuthorId );
			if ( doPhotoAction( randomPhotoOfPhotoAuthor, actionCommitter ) ) {
				doActionForAnotherPhotoOfPhotoAuthor( photoAuthorId, actionCommitter );
			}
		}
	}

	private void doActionForPhotoOfAuthorsForWhomPhotoAuthorHasVotesRecently( final int photoAuthorId, final User actionCommitter ) {
		final RandomUtilsService randomUtilsService = services.getRandomUtilsService();
		final PhotoService photoService = services.getPhotoService();
		final UserService userService = services.getUserService();
		final EntityLinkUtilsService entityLinkUtilsService = services.getEntityLinkUtilsService();

		final User photoAuthor = userService.load( photoAuthorId );

		final boolean isHasVotedUserGoingToDoTheActionForPhotosOfUserWhoHaveSeenAuthorPhotos = randomUtilsService.getRandomBoolean();
		if ( isHasVotedUserGoingToDoTheActionForPhotosOfUserWhoHaveSeenAuthorPhotos ) {
			final List<Photo> lastVotedPhotos = photoService.getLastVotedPhotos( photoAuthor, 4, actionCommitter );

			if ( lastVotedPhotos == null || lastVotedPhotos.size() == 0 ) {
				return;
			}

			final Photo randomPhotoFromLastVoted = randomUtilsService.getRandomPhoto( lastVotedPhotos );
			final int authorIdOfRandomPhotoFromLastVoted = randomPhotoFromLastVoted.getUserId();
			final Photo randomPhoto = randomUtilsService.getRandomPhotoOfUser( authorIdOfRandomPhotoFromLastVoted );

			final User authorOfRandomPhotoFromLastVoted = userService.load( authorIdOfRandomPhotoFromLastVoted );
			addJobExecutionFinalMessage( String.format( "User %s is going to do the action for photo of %s because %s has voted for his photos recently", entityLinkUtilsService.getUserCardLink( actionCommitter ), entityLinkUtilsService.getUserCardLink( authorOfRandomPhotoFromLastVoted ), entityLinkUtilsService.getUserCardLink( photoAuthor ) ) );

			if ( doPhotoAction( randomPhoto, actionCommitter ) ) {
				doActionForPhotoOfAuthorsForWhomPhotoAuthorHasVotesRecently( authorIdOfRandomPhotoFromLastVoted, actionCommitter );
			}
		}
	}
}
