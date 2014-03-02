package utils;

import org.apache.commons.lang.StringUtils;

public class TranslatorUtils {

	public static String translate( final String nerd ) {
		return translateWithParameters( nerd );
	}

	public static String translate( final String nerd, final String param ) {
		return translateWithParameters( nerd, param );
	}

	public static String translate( final String nerd, final long param ) {
		return translateWithParameters( nerd, String.valueOf( param ) );
	}

	public static String translate( final String nerd, final String param1, final String param2 ) {
		return translateWithParameters( nerd, param1, param2 );
	}

	public static String translate( final String nerd, final String param1, final String param2, final String param3 ) {
		return translateWithParameters( nerd, param1, param2, param3 );
	}

	public static String translate( final String nerd, final String param1, final String param2, final String param3, final String param4 ) {
		return translateWithParameters( nerd, param1, param2, param3, param4 );
	}

	public static String translate( final String nerd, final int param1, final int param2 ) {
		return translateWithParameters( nerd, String.valueOf( param1 ), String.valueOf( param2 ) );
	}

	public static String translateWithParameters( final String nerd, final String... params ) {
		/*if ( StringUtils.isEmpty( nerd ) ) {
			return StringUtils.EMPTY;
		}*/

		int i = 1;
		String result = nerd;
		for ( String param : params ) {
			result = result.replace( String.format( "$%d", i++ ), param );
		}
		return markAsTranslated( result );
	}

	private static String markAsTranslated( final String nerd ) {
		final String translatedSign = StringUtils.EMPTY; //ApplicationContextHelper.getSystemVarsService().getTranslatedSign(); // TODO
		return String.format( "%s%s", nerd, StringUtils.isNotEmpty( translatedSign ) ? translatedSign : StringUtils.EMPTY );
	}
}
