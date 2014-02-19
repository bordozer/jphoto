package controllers.comment.edit;

import core.context.EnvironmentContext;
import core.general.photo.Photo;
import core.general.photo.ValidationResult;
import core.general.user.User;
import core.general.configuration.ConfigurationKey;
import core.services.photo.PhotoService;
import core.services.system.ConfigurationService;
import core.services.photo.PhotoCommentService;
import core.services.security.SecurityService;
import core.services.utils.DateUtilsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import utils.*;

import java.util.Date;

public class PhotoCommentValidator implements Validator {

	@Autowired
	private PhotoCommentService photoCommentService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private PhotoService photoService;

	@Override
	public boolean supports( final Class<?> clazz ) {
		return PhotoCommentModel.class.equals( clazz );
	}

	@Override
	public void validate( final Object target, final Errors errors ) {
		final PhotoCommentModel model = ( PhotoCommentModel ) target;

		validateCommentText( model, errors );

		validateCommentDelay( errors );

		final Photo photo = photoService.load( model.getPhotoId() );
		final ValidationResult commentingValidationResult = securityService.getPhotoCommentingValidationResult( EnvironmentContext.getCurrentUser(), photo );
		if ( commentingValidationResult.isValidationFailed() ) {
			errors.reject( commentingValidationResult.getValidationMessage() );
		}

		final int photoCommentId = model.getPhotoCommentId();
		if ( photoCommentId > 0 && ! securityService.userCanEditPhotoComment( EnvironmentContext.getCurrentUser(), photoCommentService.load( photoCommentId ) ) ) {
			errors.reject( TranslatorUtils.translate( "You do not have permission to edit this comment" ) );
		}
	}

	private void validateCommentText( final PhotoCommentModel model, final Errors errors ) {

		final String commentText = model.getCommentText();

		if ( StringUtils.isEmpty( commentText ) ) {
			errors.rejectValue( PhotoCommentModel.COMMENT_TEXT_FORM_CONTROL
				, TranslatorUtils.translate( String.format( "%s must not be empty.", FormatUtils.getFormattedFieldName( "Comment" ) ) ) );
			return;
		}

		final int actualLength = commentText.length();
		final int minLength = configurationService.getInt( ConfigurationKey.COMMENTS_MIN_LENGTH );
		final int maxLength = configurationService.getInt( ConfigurationKey.COMMENTS_MAX_LENGTH );
		if ( actualLength < minLength || actualLength > maxLength ) {
			final String text = String.format( "%s must be more then $1 symbols and less then $2. Entered: $3", FormatUtils.getFormattedFieldName( "Comment" ) );
			errors.rejectValue( PhotoCommentModel.COMMENT_TEXT_FORM_CONTROL
				, TranslatorUtils.translate( text, String.valueOf( minLength ), String.valueOf( maxLength ), String.valueOf( actualLength ) ) );
			return;
		}

	}

	private void validateCommentDelay( final Errors errors ) {
		final User currentUser = EnvironmentContext.getCurrentUser();
		if ( !photoCommentService.isUserCanCommentPhotos( currentUser.getId() ) ) {
			final long userDelayToNextComment = photoCommentService.getUserDelayToNextComment( currentUser.getId() );
			final long delayInSecs = ( long ) NumberUtils.round( userDelayToNextComment / 1000, 0 );

			final Date lastCommentTime = photoCommentService.getUserLastCommentTime( currentUser.getId() );
			final Date nextCommentTime = photoCommentService.getUserNextCommentTime( currentUser.getId() );

			final String lastCommentTimeTxt = dateUtilsService.formatTime( lastCommentTime );
			final String nextCommentTimeTxt = dateUtilsService.formatTime( nextCommentTime );

			String error = TranslatorUtils.translate( "You can leave the next comment in $1 seconds. <br />Your last comment $2<br /> You can leave the next $3"
				, String.valueOf( delayInSecs ), lastCommentTimeTxt, nextCommentTimeTxt );
			errors.reject( error );
		}
	}
}
