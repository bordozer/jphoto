package utils;

import core.log.LogHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShellUtils {

	final static LogHelper log = new LogHelper( ShellUtils.class );

	public static String executeCommandSync( final String[] cmd ) throws InterruptedException, IOException {

//		log.debug( cmd );

		final Process pr;

		try {
			final Runtime run = Runtime.getRuntime();
			pr = run.exec( cmd );
		} catch ( final IOException e ) {
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
