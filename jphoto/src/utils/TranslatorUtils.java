package utils;

import admin.controllers.translator.translations.TranslationEntryType;
import core.context.ApplicationContextHelper;
import core.context.EnvironmentContext;
import core.services.translator.Language;

/*
* For using in web/WEB-INF/tags.tld ONLY
*/
public class TranslatorUtils {

	public static String translate( final String nerd ) {
		return ApplicationContextHelper.getTranslatorService().translateWithParameters( nerd );
	}

	public static String translate( final String nerd, final String param ) {
		return ApplicationContextHelper.getTranslatorService().translateWithParameters( nerd, param );
	}

	public static String translate( final String nerd, final long param ) {
		return ApplicationContextHelper.getTranslatorService().translateWithParameters( nerd, String.valueOf( param ) );
	}

	public static String translate( final String nerd, final String param1, final String param2 ) {
		return ApplicationContextHelper.getTranslatorService().translateWithParameters( nerd, param1, param2 );
	}

	public static String translate( final String nerd, final String param1, final String param2, final String param3 ) {
		return ApplicationContextHelper.getTranslatorService().translateWithParameters( nerd, param1, param2, param3 );
	}

	public static String translate( final String nerd, final String param1, final String param2, final String param3, final String param4 ) {
		return ApplicationContextHelper.getTranslatorService().translateWithParameters( nerd, param1, param2, param3, param4 );
	}

	public static String translate( final String nerd, final int param1, final int param2 ) {
		return ApplicationContextHelper.getTranslatorService().translateWithParameters( nerd, String.valueOf( param1 ), String.valueOf( param2 ) );
	}

	public static String translateWithParameters( final String nerd, final String... params ) {
		return ApplicationContextHelper.getTranslatorService().translateWithParameters( nerd, params );
	}

	public static String translateGenre( final int entryId ) {
		return ApplicationContextHelper.getTranslatorService().translateCustom( TranslationEntryType.GENRE, entryId, EnvironmentContext.getLanguage() );
	}

	public static String translateVotingCategory( final int entryId ) {
		return ApplicationContextHelper.getTranslatorService().translateCustom( TranslationEntryType.VOTING_CATEGORY, entryId, EnvironmentContext.getLanguage() );
	}
}
