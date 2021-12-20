package com.bordozer.jphoto.ui.controllers.users.genreRank;

import com.bordozer.jphoto.core.general.photo.ValidationResult;
import com.bordozer.jphoto.core.services.entry.FavoritesService;
import com.bordozer.jphoto.core.services.security.SecurityService;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserGenreRankVotingValidator implements Validator {

    @Autowired
    private FavoritesService favoritesService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private TranslatorService translatorService;

    @Autowired
    private DateUtilsService dateUtilsService;

    @Override
    public boolean supports(final Class<?> clazz) {
        return UserGenreRankVotingModel.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        final UserGenreRankVotingModel model = (UserGenreRankVotingModel) target;

        validateThatUserLogged(model, errors);

        validateThatUserDoesNotVoteForOwnRanks(model, errors);

        validateUserPreviousVoting(model, errors);

        validateThatUserIsNotInBlackList(model, errors);
    }

    private void validateThatUserIsNotInBlackList(final UserGenreRankVotingModel model, final Errors errors) {
        if (favoritesService.isUserInBlackListOfUser(model.getUser().getId(), model.getVoter().getId())) {
            errors.reject(translatorService.translate("Voting error", EnvironmentContext.getLanguage()), translatorService.translate("You are in the black list of the member", EnvironmentContext.getLanguage()));
        }
    }

    private void validateThatUserLogged(final UserGenreRankVotingModel model, final Errors errors) {
        if (!UserUtils.isLoggedUser(model.getVoter())) {
            errors.reject(translatorService.translate("Voting error", EnvironmentContext.getLanguage()), translatorService.translate("Only logged members can vote", EnvironmentContext.getLanguage()));
        }
    }

    private void validateThatUserDoesNotVoteForOwnRanks(final UserGenreRankVotingModel model, final Errors errors) {
        if (UserUtils.isTheUserThatWhoIsCurrentUser(model.getUser())) {
            errors.reject(translatorService.translate("Voting error", EnvironmentContext.getLanguage()), translatorService.translate("You can not vote for your oun rank", EnvironmentContext.getLanguage()));
        }
    }

    private void validateUserPreviousVoting(final UserGenreRankVotingModel model, final Errors errors) {

        final VotingModel votingModel = model.getVotingModel();
        final int votingPoints = votingModel.getLoggedUserVotingPoints();

        if (votingPoints == 0) {
            errors.reject(translatorService.translate("Voting error", EnvironmentContext.getLanguage()), translatorService.translate("ZERO Voting For User Rank In Genre", EnvironmentContext.getLanguage(), model.getGenre().getName()));
            return;
        }

        final ValidationResult validationResult
                = securityService.getUserRankInGenreVotingValidationResult(model.getUser(), EnvironmentContext.getCurrentUser(), model.getGenre(), dateUtilsService.getCurrentTime(), model.getVoter().getLanguage());

        if (validationResult.isValidationFailed()) {
            errors.reject(validationResult.getValidationMessage());
        }
    }
}
