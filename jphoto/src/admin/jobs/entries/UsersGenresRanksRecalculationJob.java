package admin.jobs.entries;

import admin.controllers.jobs.edit.NoParametersAbstractJob;
import admin.jobs.JobRuntimeEnvironment;
import admin.jobs.enums.SavedJobType;
import core.enums.PrivateMessageType;
import core.enums.SavedJobParameterKey;
import core.general.base.CommonProperty;
import core.general.cache.CacheKey;
import core.general.genre.Genre;
import core.general.message.PrivateMessage;
import core.general.user.User;
import core.log.LogHelper;
import core.services.entry.ActivityStreamService;
import core.services.translator.message.TranslatableMessage;
import core.services.user.UserRankService;
import core.services.utils.DateUtilsService;
import core.services.utils.EntityLinkUtilsService;

import java.util.List;
import java.util.Map;

public class UsersGenresRanksRecalculationJob extends NoParametersAbstractJob {

	public UsersGenresRanksRecalculationJob( final JobRuntimeEnvironment jobEnvironment ) {
		super( new LogHelper( UsersGenresRanksRecalculationJob.class ), jobEnvironment );
	}

	@Override
	protected void runJob() throws Throwable {

		final List<Integer> genreIds = services.getGenreService().load( services.getBaseSqlUtilsService().getGenresIdsSQL() ).getIds();

		final UserRankService userRankService = services.getUserRankService();
		final ActivityStreamService activityStreamService = services.getActivityStreamService();
		final EntityLinkUtilsService entityLinkUtilsService = services.getEntityLinkUtilsService();
		final DateUtilsService dateUtilsService = services.getDateUtilsService();

		for ( final User user : beingProcessedUsers ) {
			final int userId = user.getId();

			for ( final int genreId : genreIds ) {


				final int userCurrentRank = userRankService.getUserRankInGenre( userId, genreId );
				final int userNewRank = userRankService.calculateUserRankInGenre( userId, genreId );

				final boolean isUserCanVote = userNewRank != userCurrentRank; // TODO: add conf settings checking and move to the service
				if ( isUserCanVote ) {
					userRankService.saveUserRankForGenre( userId, genreId, userNewRank );

					final Genre genre = services.getGenreService().load( genreId );

					sendSystemNotificationAboutGotNewRankToUser( user, userCurrentRank, userNewRank, genre );

					getLog().info( String.format( "User %s has bees given a new rank %s in %s (the previous one was %s)", user, userNewRank, genre, userCurrentRank ) );

					/*final Language language = getLanguage();
					addJobRuntimeLogMessage( services.getTranslatorService().translate( "User $1 has bees given a new rank $2 in $3 ( the previous one was $4 )"
						, language
						, entityLinkUtilsService.getUserCardLink( user, language )
						, String.valueOf( userNewRank )
						, entityLinkUtilsService.getPhotosByUserByGenreLink( user, genre, language )
						, String.valueOf( userCurrentRank ) )
					);*/
					final TranslatableMessage translatableMessage = new TranslatableMessage( "User $1 has bees given a new rank $2 in $3 ( the previous one was $4 )", services )
						.addUserCardLinkUnit( user )
						.addIntegerUnit( userNewRank )
						.addPhotosByUserByGenreLinkUnit( user, genre )
						.addIntegerUnit( userCurrentRank )
						;
					addJobRuntimeLogMessage( translatableMessage );

					activityStreamService.saveUserRankInGenreChanged( user, genre, userCurrentRank, userNewRank, dateUtilsService.getCurrentTime(), services );
				}
			}
			increment();

			if ( isFinished() ) {
				break;
			}

			if ( hasJobFinishedWithAnyResult() ) {
				break;
			}

//			throw new BaseRuntimeException( "Planned exception" ); // TODO: remove!!!
		}

		services.getCacheService().expire( CacheKey.PHOTO_INFO );

		getLog().debug( "Photo info cache has been cleared" );
	}

	private void sendSystemNotificationAboutGotNewRankToUser( final User user, final int userCurrentRank, final int userNewRank, final Genre genre ) {
		final PrivateMessage message = new PrivateMessage();

		message.setToUser( user );
		message.setCreationTime( services.getDateUtilsService().getCurrentTime() );
		message.setPrivateMessageType( PrivateMessageType.SYSTEM_NOTIFICATIONS );
		message.setMessageText( String.format( "You have bees given a new rank %s in category %s ( the previous one was %s )"
			, userNewRank, services.getEntityLinkUtilsService().getPhotosByGenreLink( genre, getLanguage() ), userCurrentRank )
		);

		services.getPrivateMessageService().save( message );
	}

	@Override
	public void initJobParameters( final Map<SavedJobParameterKey, CommonProperty> jobParameters ) {
		super.initJobParameters( jobParameters );

		totalJopOperations = services.getUserService().getUserCount();
	}

	@Override
	public SavedJobType getJobType() {
		return SavedJobType.USER_GENRES_RANKS_RECALCULATING;
	}
}
