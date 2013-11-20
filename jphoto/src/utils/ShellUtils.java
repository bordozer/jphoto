package utils;

import core.log.LogHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShellUtils {

	final static LogHelper log = new LogHelper( ShellUtils.class );

	public static String executeCommand( final String cmd ) throws InterruptedException, IOException {
		log.debug( cmd );

		final Runtime run = Runtime.getRuntime();
		final Process pr;

		try {
			pr = run.exec( cmd );
		} catch ( IOException e ) {
			log.error( e );
			return "";
		}

		pr.waitFor();

		final BufferedReader buf = new BufferedReader( new InputStreamReader( pr.getInputStream() ) );
		String line = "";
		final StringBuilder builder = new StringBuilder();

		while ( ( line = buf.readLine() ) != null ) {
			builder.append( line );
		}

		return builder.toString();
	}
}
