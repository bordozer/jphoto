package controllers.voting;

import core.context.EnvironmentContext;
import core.general.photo.Photo;
import core.general.photo.ValidationResult;
import core.general.user.UserPhotoVote;
import core.services.photo.PhotoVotingService;
import core.services.security.SecurityService;
import core.services.translator.TranslatorService;
import core.services.user.UserRankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import utils.FormatUtils;

import java.util.List;

public class PhotoVotingValidator implements Validator {

	@Autowired
	private PhotoVotingService photoVotingService;

	@Autowired
	private UserRankService userRankService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private TranslatorService translatorService;

	@Override
	public boolean supports( final Class<?> clazz ) {
		return PhotoVotingModel.class.equals( clazz );
	}

	@Override
	public void validate( final Object target, final Errors errors ) {
		final PhotoVotingModel model = ( PhotoVotingModel ) target;

		if ( ! validateThanUserHasUserAlreadyNotVotedForPhoto( model.getPhoto(), errors ) ) {
			return;
		}

		if ( ! validateAtLeastOneVotingCategoryIsSelected( model.getUserPhotoVotes(), errors ) ) {
			return;
		}

		if ( ! validateVotingCategories( model.getUserPhotoVotes(), errors ) ) {
			return;
		}

		final ValidationResult votingValidationResult = securityService.validateUserCanVoteForPhoto( EnvironmentContext.getCurrentUser(), model.getPhoto(), EnvironmentContext.getCurrentUser().getLanguage() );
		if ( votingValidationResult.isValidationFailed() ) {
			errors.reject( votingValidationResult.getValidationMessage() );
		}
	}

	private boolean validateThanUserHasUserAlreadyNotVotedForPhoto( final Photo photo, final Errors errors ) {
		if ( photoVotingService.isUserVotedForPhoto( EnvironmentContext.getCurrentUser(), photo ) ) {
			errors.reject( translatorService.translate( "You have already voted for this photo" ) );
			return false;
		}
		return true;
	}

	private boolean validateAtLeastOneVotingCategoryIsSelected( final List<UserPhotoVote> userPhotoVotes, final Errors errors ) {
		if ( userPhotoVotes.size() == 0) {
			final String errorCode = translatorService.translate( "Select at least one $1", FormatUtils.getFormattedFieldName( "voting category" ) );
			errors.reject( errorCode );
			return false;
		}
		return true;
	}

	private boolean validateVotingCategories( final List<UserPhotoVote> voteUsers, final Errors errors ) {
		for ( UserPhotoVote userPhotoVote : voteUsers ) {
			final int votingCategoryMark = userPhotoVote.getMark();

			if ( votingCategoryMark == 0 ) {
				final String errorCode = translatorService.translate( "Set mark for $1 '$2'", FormatUtils.getFormattedFieldName( "voting category" ), userPhotoVote.getPhotoVotingCategory().getName() );
				errors.reject( errorCode );

				return false;
			}

			final int minMark = userRankService.getUserLowestNegativeMarkInGenre( userPhotoVote.getUser().getId(), userPhotoVote.getPhoto().getGenreId() );
			final int getMaxMark = userRankService.getUserHighestPositiveMarkInGenre( userPhotoVote.getUser().getId(), userPhotoVote.getPhoto().getGenreId() );
			if ( votingCategoryMark < minMark || votingCategoryMark > getMaxMark ) {
				final String errorCode = translatorService.translate( "$1 '$2' is out of bounds", FormatUtils.getFormattedFieldName( "Voting category" ), userPhotoVote.getPhotoVotingCategory().getName() );
				errors.reject( errorCode );

				return false;
			}
		}

		return true;
	}
}
