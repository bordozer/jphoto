package admin.jobs.entries;

import admin.jobs.JobRuntimeEnvironment;
import admin.jobs.enums.SavedJobType;
import core.enums.SavedJobParameterKey;
import core.general.base.CommonProperty;
import core.general.configuration.ConfigurationKey;
import core.general.genre.Genre;
import core.general.user.User;
import core.general.user.UserRankInGenreVoting;
import core.log.LogHelper;
import core.services.translator.message.TranslatableMessage;
import core.services.user.UserRankService;
import core.services.utils.EntityLinkUtilsService;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;

public class RankVotingJob extends AbstractJob {

	private final static Integer MAX_ITERATIONS = 1000;

	private final int[] voteArrays = { -1, 1, 1, 1, 1, 1 };

	public RankVotingJob( final JobRuntimeEnvironment jobEnvironment ) {
		super( new LogHelper( RankVotingJob.class ), jobEnvironment );
	}

	@Override
	protected void runJob() throws Throwable {
		int counter = 0;

		final RandomGenresGenerator genresGenerator = new RandomGenresGenerator();

		final UserRankService userRankService = services.getUserRankService();
		final EntityLinkUtilsService entityLinkUtilsService = services.getEntityLinkUtilsService();

		while( ! isFinished() && ! hasJobFinishedWithAnyResult() ) {
			counter++;

			if ( counter > MAX_ITERATIONS ) {
				getLog().info( "Rank voting generating: max iteration number reached..." );
				break;
			}

			final User votingUser = services.getRandomUtilsService().getRandomUser( beingProcessedUsers );
			final User beingVotedUser = services.getRandomUtilsService().getRandomUserButNotThisOne( votingUser, beingProcessedUsers );

			if ( beingVotedUser == null ) {
				continue;
			}

			final Genre genre = genresGenerator.getRandomGenreWhereUserHasEnoughPhotos( beingVotedUser );
			final boolean isAppropriateGenreFound = genre == null;
			if ( isAppropriateGenreFound ) {
				continue;
			}

			final int genreId = genre.getId();

			final int beingVotedUserRank = userRankService.getUserRankInGenre( beingVotedUser.getId(), genreId );
			if ( userRankService.isUserVotedLastTimeForThisRankInGenre( votingUser.getId(), beingVotedUser.getId(), genreId, beingVotedUserRank ) ) {
				continue;
			}

			final int accessibleVotingPoints = userRankService.getUserRankInGenreVotingPoints( votingUser.getId(), genreId ); // must return at least 1 - zero points are not allowed
			final int randomVotingPoints = accessibleVotingPoints * services.getRandomUtilsService().getRandomIntegerArrayElement( voteArrays );

			final Date votingTime = services.getDateUtilsService().getCurrentTime();

			final UserRankInGenreVoting rankVoting = new UserRankInGenreVoting();
			rankVoting.setVoterId( votingUser.getId() );
			rankVoting.setUserId( beingVotedUser.getId() );
			rankVoting.setGenreId( genreId );
			rankVoting.setPoints( randomVotingPoints );
			rankVoting.setVotingTime( votingTime );
			rankVoting.setUserRankWhenVoting( beingVotedUserRank );

			userRankService.saveVotingForUserRankInGenre( rankVoting, votingUser );

			services.getUsersSecurityService().saveLastUserActivityTime( votingUser.getId(), votingTime );

//			getLog().debug( String.format( "User %s is voting for %s in genre %s (mark %s)", votingUser.getId(), beingVotedUser.getId(), genreId, accessibleVotingPoints ) );

			/*final Language language = getLanguage();
			addJobRuntimeLogMessage( services.getTranslatorService().translate( "User $1 has voted for $2's rank in $3 ( $4 )"
				, language
				, entityLinkUtilsService.getUserCardLink( votingUser, language )
				, entityLinkUtilsService.getUserCardLink( beingVotedUser, language )
				, entityLinkUtilsService.getPhotosByUserByGenreLink( beingVotedUser, genre, language )
				, String.valueOf( randomVotingPoints ) )
			);*/
			final TranslatableMessage translatableMessage = new TranslatableMessage( "User $1 has voted for $2's rank in $3 ( $4 )", services )
				.addLinkToUserCardUnit( votingUser )
				.addLinkToUserCardUnit( beingVotedUser )
				.addLinkToPhotosByUserByGenreUnit( beingVotedUser, genre )
				.addIntegerUnit( randomVotingPoints )
				;
			addJobRuntimeLogMessage( translatableMessage );

			counter = 0;
			increment();
		}
	}

	@Override
	public SavedJobType getJobType() {
		return SavedJobType.RANK_VOTING_GENERATION;
	}

	@Override
	public void initJobParameters( final Map<SavedJobParameterKey, CommonProperty> jobParameters ) {
		totalJopOperations = jobParameters.get( SavedJobParameterKey.ACTIONS_QTY ).getValueInt();
	}

	@Override
	public Map<SavedJobParameterKey, CommonProperty> getParametersMap() {
		final Map<SavedJobParameterKey, CommonProperty> parametersMap = newHashMap();

		parametersMap.put( SavedJobParameterKey.ACTIONS_QTY, new CommonProperty( SavedJobParameterKey.ACTIONS_QTY.getId(), totalJopOperations ) );

		return parametersMap;
	}

	@Override
	public String getJobParametersDescription() {
		final StringBuilder builder = new StringBuilder();

		builder.append( services.getTranslatorService().translate( "Total job steps", getLanguage() ) ).append( ": " ).append( totalJopOperations ).append( "<br />" );

		return builder.toString();
	}

	private class RandomGenresGenerator {

		private final Map<User, Set<Genre>> userPhotosByGenresCacheMap = newHashMap();
		private final Set<Genre> genres = newHashSet();
		private final int minPhotosQtyForGettingVotesForRankInGenre;

		private RandomGenresGenerator() {
			initGenres();
			minPhotosQtyForGettingVotesForRankInGenre = services.getConfigurationService().getInt( ConfigurationKey.RANK_VOTING_MIN_PHOTOS_QTY_IN_GENRE );
		}

		private void initGenres() {
			final List<Genre> genreList = services.getGenreService().loadAll();

			for ( final Genre genre : genreList ) {
				genres.add( genre );
			}
		}

		public Genre getRandomGenreWhereUserHasEnoughPhotos( final User user ) {
			final Set<Genre> genresWhereUserHasPhotos = getGenresWhereUserHasEnoughPhotos( user );

			return services.getRandomUtilsService().getRandomGenericSetElement( genresWhereUserHasPhotos );
		}

		public Set<Genre> getGenresWhereUserHasEnoughPhotos( final User user ) {

			final Set<Genre> userGenresWhereHeHasPhotos = userPhotosByGenresCacheMap.get( user );
			if ( userGenresWhereHeHasPhotos != null ) {
				return userGenresWhereHeHasPhotos;
			}

			final Set<Genre> result = newHashSet();

			for ( final Genre genre : genres ) {
				final int photosInGenre = services.getPhotoService().getPhotoQtyByUserAndGenre( user.getId(), genre.getId() );
				if ( photosInGenre >= minPhotosQtyForGettingVotesForRankInGenre ) {
					result.add( genre );
				}
			}

			userPhotosByGenresCacheMap.put( user, result );

			return result;
		}
	}
}
