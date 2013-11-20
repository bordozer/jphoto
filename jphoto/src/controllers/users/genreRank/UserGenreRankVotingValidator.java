package controllers.users.genreRank;

import core.context.EnvironmentContext;
import core.general.photo.ValidationResult;
import core.services.entry.FavoritesService;
import core.services.security.SecurityService;
import core.services.user.UserRankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import utils.TranslatorUtils;
import utils.UserUtils;

public class UserGenreRankVotingValidator implements Validator {

	@Autowired
	private UserRankService userRankService;

	@Autowired
	private FavoritesService favoritesService;

	@Autowired
	private SecurityService securityService;

	@Override
	public boolean supports( final Class<?> clazz ) {
		return UserGenreRankVotingModel.class.equals( clazz );
	}

	@Override
	public void validate( final Object target, final Errors errors ) {
		final UserGenreRankVotingModel model = ( UserGenreRankVotingModel ) target;

		validateThatUserLogged( model, errors );

		validateThatUserDoesNotVoteForOwnRanks( model, errors );

		validateUserPreviousVoting( model, errors );

		validateThatUserIsNotInBlackList( model, errors );
	}

	private void validateThatUserIsNotInBlackList( final UserGenreRankVotingModel model, final Errors errors ) {
		if ( favoritesService.isUserInBlackListOfUser( model.getUser().getId(), model.getVoter().getId() ) ) {
			errors.reject( TranslatorUtils.translate( "Voting error" ), TranslatorUtils.translate( "You are in the black list of the member" ) );
		}
	}

	private void validateThatUserLogged( final UserGenreRankVotingModel model, final Errors errors ) {
		if ( ! UserUtils.isLoggedUser( model.getVoter() ) ) {
			errors.reject( TranslatorUtils.translate( "Voting error" ), TranslatorUtils.translate( "Only logged members can vote" ) );
		}
	}

	private void validateThatUserDoesNotVoteForOwnRanks( final UserGenreRankVotingModel model, final Errors errors ) {
		if ( UserUtils.isUserEqualsToCurrentUser( model.getUser() ) ) {
			errors.reject( TranslatorUtils.translate( "Voting error" ), TranslatorUtils.translate( "You can not vote for your oun rank" ) );
		}
	}

	private void validateUserPreviousVoting( final UserGenreRankVotingModel model, final Errors errors ) {

		final VotingModel votingModel = model.getVotingModel();
		final int votingPoints = votingModel.getLoggedUserVotingPoints();

		if ( votingPoints == 0 ) {
			errors.reject( TranslatorUtils.translate( "Voting error" ), TranslatorUtils.translate( "ZERO Voting For User Rank In Genre", model.getGenre().getName() ) );
			return;
		}

		final ValidationResult validationResult = securityService.getUserRankInGenreVotingValidationResult( model.getUser(), EnvironmentContext.getCurrentUser(), model.getGenre() );

		if ( validationResult.isValidationFailed() ) {
			errors.reject( validationResult.getValidationMessage() );
		}
	}
}
