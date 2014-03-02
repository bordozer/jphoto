package utils;

import controllers.users.edit.UserEditDataModel;
import core.context.ApplicationContextHelper;
import core.general.configuration.ConfigurationKey;
import core.services.system.ConfigurationService;

public class EditDataValidationUtils {

	public static final String HINT_LINE_BREAK = "<br />";

	public static class UserRequirement {

		public static String getLoginRequirement( final int minLoginLength, final int maxLoginLength ) {
			final StringBuilder builder = new StringBuilder();

			final String minLen = String.valueOf( minLoginLength );
			final String maxLen = String.valueOf( maxLoginLength );
			builder.append( TranslatorUtils.translate( "Your login should have length $1-$2, a-z, A-Z, 0-9 or _.", minLen, maxLen ) );
			builder.append( HINT_LINE_BREAK );
			builder.append( TranslatorUtils.translate( "Your login will never be shown to another users." ) );

			return builder.toString();
		}

		public static String getPasswordRequirement( final boolean isClickablePasswordExamples ) {
			final StringBuilder builder = new StringBuilder();
			builder.append( TranslatorUtils.translate( "Must contains one digit from 0-9" ) );
			builder.append( HINT_LINE_BREAK );
			builder.append( TranslatorUtils.translate( "Must contains one lowercase characters" ) );
			builder.append( HINT_LINE_BREAK );
			builder.append( TranslatorUtils.translate( "Must contains one uppercase characters" ) );
			builder.append( HINT_LINE_BREAK );
			builder.append( String.format( "%s %s", TranslatorUtils.translate( "Must contains one special symbols from the list" ), "'@#$%^&+='" ) );
			builder.append( HINT_LINE_BREAK );
			builder.append( TranslatorUtils.translate( "No whitespace allowed in the entire string" ) );
			builder.append( HINT_LINE_BREAK );
			builder.append( HINT_LINE_BREAK );

			builder.append( TranslatorUtils.translate( "<b>Randomly generated passwords</b> (you can use one of them):" ) );
			builder.append( HINT_LINE_BREAK );
			builder.append( HINT_LINE_BREAK );

			builder.append( String.format( "%s: %s", TranslatorUtils.translate( "Minimal secure password example" )
					, getClickablePassword ( PasswordUtils.generatePassword( UserEditDataModel.MIN_PASSWORD_LENGTH ), isClickablePasswordExamples ) ) );
			builder.append( HINT_LINE_BREAK );

			final int length = UserEditDataModel.MIN_PASSWORD_LENGTH + ( UserEditDataModel.MAX_PASSWORD_LENGTH - UserEditDataModel.MIN_PASSWORD_LENGTH ) / 2;
			builder.append( String.format( "%s: %s", TranslatorUtils.translate( "Good password example" )
					, getClickablePassword ( PasswordUtils.generatePassword( length ), isClickablePasswordExamples ) ) );
			builder.append( HINT_LINE_BREAK );

			builder.append( String.format( "%s: %s", TranslatorUtils.translate( "Excellent password example" )
					, getClickablePassword ( PasswordUtils.generatePassword( UserEditDataModel.MAX_PASSWORD_LENGTH ), isClickablePasswordExamples ) ) );

			return builder.toString();
		}

		private static String getClickablePassword( final String password, final boolean isClickablePasswordExamples ) {
			if ( ! isClickablePasswordExamples ) {
				return password;
			}

			return String.format( "<a href='#' onclick=\"$( '#%1$s' ).val( '%3$s' );$( '#%2$s' ).val( '%3$s' );\">%3$s</a>"
					, UserEditDataModel.USER_PASSWORD_FORM_CONTROL, UserEditDataModel.USER_CONFIRM_PASSWORD_FORM_CONTROL, password );
		}

		public static String getConfirmPasswordRequirement() {
			final StringBuilder builder = new StringBuilder();

			builder.append( TranslatorUtils.translate( "Confirm your password." ) );

			return builder.toString();
		}

		public static String getNameRequirement( final int minUserNameLength, final int maxUserNameLength ) {
			final StringBuilder builder = new StringBuilder();

			final String minLen = String.valueOf( minUserNameLength );
			final String maxLen = String.valueOf( maxUserNameLength );

			builder.append( TranslatorUtils.translate( "Your public name in the club." ) );
			builder.append( HINT_LINE_BREAK );

			builder.append( TranslatorUtils.translate( "Your name should have length $1-$2, a-z, A-Z, 0-9 or _.", minLen, maxLen ) );
			builder.append( HINT_LINE_BREAK );

			builder.append( TranslatorUtils.translate( "Will be visible for all users." ) );

			return builder.toString();
		}

		public static String getEmailRequirement() {
			final StringBuilder builder = new StringBuilder();

			builder.append( TranslatorUtils.translate( "Your email will never be shown to another users." ) );

			return builder.toString();
		}

		public static String getBirthdayRequirement() {
			final StringBuilder builder = new StringBuilder();

			builder.append( TranslatorUtils.translate( "Your birthday." ) );
			builder.append( HINT_LINE_BREAK );
			builder.append( TranslatorUtils.translate( "Will be visible for all users." ) );

			return builder.toString();
		}

		public static String getHomeSiteRequirement() {
			final StringBuilder builder = new StringBuilder();

			builder.append( TranslatorUtils.translate( "Your home site if exists." ) );
			builder.append( HINT_LINE_BREAK );
			builder.append( TranslatorUtils.translate( "Will be visible for all users." ) );

			return builder.toString();
		}
	}

	public static class PhotoRequirement {
		public static String getNameRequirement() {

			final ConfigurationService configurationService = ApplicationContextHelper.getBean( ConfigurationService.BEAN_NAME );
			final StringBuilder builder = new StringBuilder();

			builder.append( TranslatorUtils.translate( "The name of your photo. Max length is $1 symbols", configurationService.getInt( ConfigurationKey.SYSTEM_PHOTO_NAME_MAX_LENGTH ) ) );

			return builder.toString();
		}

		public static String getKeywordsRequirement() {
			final StringBuilder builder = new StringBuilder();

			builder.append( TranslatorUtils.translate( "Comma (,) separated keywords." ) );

			return builder.toString();
		}

		public static String getDescriptionRequirement() {
			final StringBuilder builder = new StringBuilder();

			builder.append( TranslatorUtils.translate( "Any additional information about the photo" ) );

			return builder.toString();
		}
	}

	public static String getFieldIsMandatoryText() {
		return TranslatorUtils.translate( "The field is mandatory." );
	}

	public static String getFieldIsOptionalText() {
		return TranslatorUtils.translate( "The field is optional." );
	}
}
