package admin.jobs.entries;

import admin.controllers.jobs.edit.NoParametersAbstractJob;
import admin.jobs.enums.SavedJobType;
import core.enums.PrivateMessageType;
import core.enums.SavedJobParameterKey;
import core.general.base.CommonProperty;
import core.general.cache.CacheKey;
import core.general.genre.Genre;
import core.general.user.User;
import core.general.message.PrivateMessage;
import core.log.LogHelper;
import core.services.user.UserRankService;
import core.services.utils.EntityLinkUtilsService;

import java.util.List;
import java.util.Map;

public class UsersGenresRanksRecalculationJob extends NoParametersAbstractJob {

	public UsersGenresRanksRecalculationJob() {
		super( new LogHelper( UsersGenresRanksRecalculationJob.class ) );
	}

	@Override
	protected void runJob() throws Throwable {

		final List<Integer> genreIds = services.getGenreService().load( services.getBaseSqlUtilsService().getGenresIdsSQL() ).getIds();

		final UserRankService userRankService = services.getUserRankService();

		final EntityLinkUtilsService entityLinkUtilsService = services.getEntityLinkUtilsService();

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

					addJobExecutionFinalMessage( String.format( "User %s has bees given a new rank %s in %s ( the previous one was %s )"
						, entityLinkUtilsService.getUserCardLink( user )
						, userNewRank
						, entityLinkUtilsService.getPhotosByUserByGenreLink( user, genre )
						, userCurrentRank ) );
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
			, userNewRank, services.getEntityLinkUtilsService().getPhotosByGenreLink( genre ), userCurrentRank )
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
