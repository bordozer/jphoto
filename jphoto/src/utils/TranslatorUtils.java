package utils;

import core.context.ApplicationContextHelper;

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
}
