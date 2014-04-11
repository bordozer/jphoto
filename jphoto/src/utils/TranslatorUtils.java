package utils;

import admin.controllers.translator.custom.TranslationEntryType;
import ui.context.ApplicationContextHelper;
import ui.context.EnvironmentContext;
import core.general.activity.AbstractActivityStreamEntry;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.translator.message.TranslatableMessage;

/*
* For using in web/WEB-INF/tags.tld ONLY
*/
public class TranslatorUtils {

	public static String translate( final String nerd ) {
		return getTranslatorService().translate( nerd, getLanguage() );
	}

	public static String translate( final String nerd, final String param ) {
		return getTranslatorService().translate( nerd, getLanguage(), param );
	}

	public static String translate( final String nerd, final long param ) {
		return getTranslatorService().translate( nerd, getLanguage(), String.valueOf( param ) );
	}

	public static String translate( final String nerd, final String param1, final String param2 ) {
		return getTranslatorService().translate( nerd, getLanguage(), param1, param2 );
	}

	public static String translate( final String nerd, final String param1, final String param2, final String param3 ) {
		return getTranslatorService().translate( nerd, getLanguage(), param1, param2, param3 );
	}

	public static String translate( final String nerd, final String param1, final String param2, final String param3, final String param4 ) {
		return getTranslatorService().translate( nerd, getLanguage(), param1, param2, param3, param4 );
	}

	public static String translate( final String nerd, final int param1, final int param2 ) {
		return getTranslatorService().translate( nerd, getLanguage(), String.valueOf( param1 ), String.valueOf( param2 ) );
	}

	public static String translateWithParameters( final String nerd, final String... params ) {
		return getTranslatorService().translate( nerd, getLanguage(), params );
	}

	public static String translateGenre( final int entryId ) {
		return getTranslatorService().translateCustom( TranslationEntryType.GENRE, entryId, getLanguage() );
	}

	public static String translateVotingCategory( final int entryId ) {
		return getTranslatorService().translateCustom( TranslationEntryType.VOTING_CATEGORY, entryId, getLanguage() );
	}

	public static String translateActivityStreamEntry( final AbstractActivityStreamEntry activityStreamEntry ) {
		return activityStreamEntry.getActivityText( getLanguage() );
	}

	public static String translatableMessage( final TranslatableMessage translatableMessage ) {
		return translatableMessage.build( getLanguage() );
	}

	private static Language getLanguage() {
		return EnvironmentContext.getLanguage();
	}

	private static TranslatorService getTranslatorService() {
		return ApplicationContextHelper.getTranslatorService();
	}
}
