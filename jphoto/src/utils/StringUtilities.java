package utils;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

public class StringUtilities {

	public static String toUpperCaseFirst( final String srt ) {
		if ( StringUtils.isEmpty( srt ) ) {
			return StringUtils.EMPTY;
		}
		return String.format( "%s%s", srt.substring( 0, 1 ).toUpperCase(), srt.substring( 1, srt.length() ) );
	}

	public static String escapeHtml( final String text ) {
		return String.format( "%s", StringEscapeUtils.escapeHtml( text ) ); // &sup3;
	}

	public static String escapeJavaScript( final String text ) {
		return String.format( "%s", StringEscapeUtils.escapeJavaScript( text ) ); // &sup2;
	}

//	public static String formatLineBrakes( final String text ) {
//		return text.replace( "&lt;br /&gt;", "<br />" ).replace( "&trade;", "&trade;" );
//	}

	public static String truncateString( final String name, int maxLength ) {
		if ( name.length() > maxLength ) {
			return name.substring( 0, maxLength );
		}
		return name;
	}

	public static String unescapeHtml( final String str ) {
		return StringEscapeUtils.unescapeHtml( str );
	}
}
