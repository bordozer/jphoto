package core.log;

import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.log4j.LogManager;

public class LogHelper {

	private final Log4JLogger logger;

	public LogHelper( Class klass ) {
		logger = new Log4JLogger( LogManager.getLogger( klass ) );
	}

	public void debug( final String message ) {
		logger.debug( message );
	}

	public void debug( final String message, final Throwable throwable ) {
		logger.debug( message, throwable );
	}

	public void info( final String message ) {
		logger.info( message );
	}

	public void warn( final String message ) {
		logger.warn( message );
	}

	public void warn( final String message, final Throwable throwable ) {
		logger.warn( message, throwable );
	}

	public void error( final String message ) {
		logger.error( message );
	}

	public void error( final Throwable throwable ) {
		logger.error( "Exception", throwable );
	}

	public void error( final String message, final Throwable throwable ) {
		logger.error( message, throwable );
	}
}
