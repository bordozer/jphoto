package utils;


import java.util.*;

public class PasswordUtils {

	private static char[] lowerCasedChars = {
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
			'z'
	};
	private static char[] upperCasedChars = {
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
			'Z'
	};
	private static char[] numbers = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };
	private static char[] specialChars = { '@', '#', '$', '%', '^', '&', '+', '=' };

	public static String generatePassword( int length ) {
		final Random r = new java.util.Random();

		final StringBuilder sb = new StringBuilder();

		sb.append( lowerCasedChars[ r.nextInt( lowerCasedChars.length ) ] );
		sb.append( upperCasedChars[ r.nextInt( upperCasedChars.length ) ] );
		sb.append( numbers[ r.nextInt( numbers.length ) ] );
		sb.append( specialChars[ r.nextInt( specialChars.length ) ] );

		for ( int i = 0; i < length - 6; i++ ) {
			if ( i % 2 == 0) {
				sb.append( lowerCasedChars[ r.nextInt( lowerCasedChars.length ) ] );
			} else {
				sb.append( upperCasedChars[ r.nextInt( lowerCasedChars.length ) ] );
			}
		}

		sb.append( upperCasedChars[ r.nextInt( upperCasedChars.length ) ] );
		sb.append( upperCasedChars[ r.nextInt( upperCasedChars.length ) ] );

		return sb.toString();
	}
}
