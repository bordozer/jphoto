package core.services.validation;

import controllers.users.edit.UserEditDataModel;
import core.services.translator.TranslatorService;
import utils.PasswordUtils;

public class UserRequirement {

	private TranslatorService translatorService;

	public UserRequirement( final TranslatorService translatorService ) {
		this.translatorService = translatorService;
	}

	public String getLoginRequirement( final int minLoginLength, final int maxLoginLength ) {
		final StringBuilder builder = new StringBuilder();

		final String minLen = String.valueOf( minLoginLength );
		final String maxLen = String.valueOf( maxLoginLength );
		builder.append( translatorService.translate( "Your login should have length $1-$2, a-z, A-Z, 0-9 or _.", minLen, maxLen ) );
		builder.append( DataRequirementService.HINT_LINE_BREAK );
		builder.append( translatorService.translate( "Your login will never be shown to another users." ) );

		return builder.toString();
	}

	public String getPasswordRequirement( final boolean isClickablePasswordExamples ) {
		final StringBuilder builder = new StringBuilder();
		builder.append( translatorService.translate( "Must contains one digit from 0-9" ) );
		builder.append( DataRequirementService.HINT_LINE_BREAK );
		builder.append( translatorService.translate( "Must contains one lowercase characters" ) );
		builder.append( DataRequirementService.HINT_LINE_BREAK );
		builder.append( translatorService.translate( "Must contains one uppercase characters" ) );
		builder.append( DataRequirementService.HINT_LINE_BREAK );
		builder.append( String.format( "%s %s", translatorService.translate( "Must contains one special symbols from the list" ), "'@#$%^&+='" ) );
		builder.append( DataRequirementService.HINT_LINE_BREAK );
		builder.append( translatorService.translate( "No whitespace allowed in the entire string" ) );
		builder.append( DataRequirementService.HINT_LINE_BREAK );
		builder.append( DataRequirementService.HINT_LINE_BREAK );

		builder.append( translatorService.translate( "<b>Randomly generated passwords</b> (you can use one of them):" ) );
		builder.append( DataRequirementService.HINT_LINE_BREAK );
		builder.append( DataRequirementService.HINT_LINE_BREAK );

		builder.append( String.format( "%s: %s", translatorService.translate( "Minimal secure password example" )
				, getClickablePassword ( PasswordUtils.generatePassword( UserEditDataModel.MIN_PASSWORD_LENGTH ), isClickablePasswordExamples ) ) );
		builder.append( DataRequirementService.HINT_LINE_BREAK );

		final int length = UserEditDataModel.MIN_PASSWORD_LENGTH + ( UserEditDataModel.MAX_PASSWORD_LENGTH - UserEditDataModel.MIN_PASSWORD_LENGTH ) / 2;
		builder.append( String.format( "%s: %s", translatorService.translate( "Good password example" )
				, getClickablePassword ( PasswordUtils.generatePassword( length ), isClickablePasswordExamples ) ) );
		builder.append( DataRequirementService.HINT_LINE_BREAK );

		builder.append( String.format( "%s: %s", translatorService.translate( "Excellent password example" )
				, getClickablePassword ( PasswordUtils.generatePassword( UserEditDataModel.MAX_PASSWORD_LENGTH ), isClickablePasswordExamples ) ) );

		return builder.toString();
	}

	private String getClickablePassword( final String password, final boolean isClickablePasswordExamples ) {
		if ( ! isClickablePasswordExamples ) {
			return password;
		}

		return String.format( "<a href='#' onclick=\"$( '#%1$s' ).val( '%3$s' );$( '#%2$s' ).val( '%3$s' );\">%3$s</a>"
				, UserEditDataModel.USER_PASSWORD_FORM_CONTROL, UserEditDataModel.USER_CONFIRM_PASSWORD_FORM_CONTROL, password );
	}

	public String getConfirmPasswordRequirement() {
		final StringBuilder builder = new StringBuilder();

		builder.append( translatorService.translate( "Confirm your password." ) );

		return builder.toString();
	}

	public String getNameRequirement( final int minUserNameLength, final int maxUserNameLength ) {
		final StringBuilder builder = new StringBuilder();

		final String minLen = String.valueOf( minUserNameLength );
		final String maxLen = String.valueOf( maxUserNameLength );

		builder.append( translatorService.translate( "Your public name in the club." ) );
		builder.append( DataRequirementService.HINT_LINE_BREAK );

		builder.append( translatorService.translate( "Your name should have length $1-$2, a-z, A-Z, 0-9 or _.", minLen, maxLen ) );
		builder.append( DataRequirementService.HINT_LINE_BREAK );

		builder.append( translatorService.translate( "Will be visible for all users." ) );

		return builder.toString();
	}

	public String getEmailRequirement() {
		final StringBuilder builder = new StringBuilder();

		builder.append( translatorService.translate( "Your email will never be shown to another users." ) );

		return builder.toString();
	}

	public String getBirthdayRequirement() {
		final StringBuilder builder = new StringBuilder();

		builder.append( translatorService.translate( "Your birthday." ) );
		builder.append( DataRequirementService.HINT_LINE_BREAK );
		builder.append( translatorService.translate( "Will be visible for all users." ) );

		return builder.toString();
	}

	public String getHomeSiteRequirement() {
		final StringBuilder builder = new StringBuilder();

		builder.append( translatorService.translate( "Your home site if exists." ) );
		builder.append( DataRequirementService.HINT_LINE_BREAK );
		builder.append( translatorService.translate( "Will be visible for all users." ) );

		return builder.toString();
	}
}
