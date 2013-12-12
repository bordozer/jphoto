package utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.math3.util.Precision;

public class NumberUtils {

	public static float round( final double number, final int scale ) {
		return ( float ) Precision.round( number, scale );
	}

	public static float round( final long number, final int scale ) {
		return Precision.round( number, scale );
	}

	public static int round( final float number ) {
		return ( int ) Precision.round( number, 0 );
	}

	public static float round( final int number, final int scale ) {
		return Precision.round( number, scale );
	}

	public static int floor( final double number ) {
		return ( int ) Math.floor( number );
	}

	public static double ceil( final double number ) {
		return Math.ceil( number );
	}

	public static boolean isNumeric( final String str ) {

		if ( StringUtils.isEmpty( str ) ) {
			return false;
		}

		try {
			double d = Double.parseDouble( str );
		} catch ( NumberFormatException nfe ) {
			return false;
		}
		return true;
	}

	public static boolean isInteger( final String str ) {

		try {
			Integer.parseInt( str );
		} catch ( NumberFormatException e ) {
			return false;
		}

		return true;
	}

	public static int convertToInt( final String number ) {
		if ( !isNumeric( number ) ) {
			return 0;
		}
		return Integer.parseInt( number );
	}

	public static long convertToLong( final String number ) {
		if ( !isNumeric( number ) ) {
			return 0;
		}
		return Long.parseLong( number );
	}

	public static float convertToFloat( final String number ) {
		if ( !isNumeric( number ) ) {
			return 0F;
		}
		return Float.parseFloat( number );
	}
}
