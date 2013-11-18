package kwaqua;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public final class Configurator {

	private static final Logger LOGGER = LoggerFactory.getLogger( Configurator.class );

	private Configurator() {
	}

	public static void applyConfiguration() {
		new Configurator().apply();
	}

	private void apply() {
		for ( Map.Entry<String, String> parameter : loadParameters() ) {
			System.setProperty( parameter.getKey(), parameter.getValue() );
		}
	}

	private Set<Map.Entry<String, String>> loadParameters() {
		final Map<String, String> parameters = new HashMap<>();

		loadCustomParameters( parameters );

		loadSystemParameters( parameters );

		return parameters.entrySet();
	}

	private void loadCustomParameters( Map<String, String> parameters ) {
		try {
			parameters.putAll( fromPropertyFile( "kwaqua" ) );
		} catch ( MissingResourceException ex ) {
			final String message = "kwaqua.properties file cannot be found";

			LOGGER.error( message );

			throw new ConfigurationException( message, ex );
		}
	}

	private void loadSystemParameters( final Map<String, String> parameters ) {
		final Properties properties = System.getProperties();
		for ( String key : properties.stringPropertyNames() ) {
			final String value = properties.getProperty( key );
			parameters.put( key, value );
		}
	}

	private Map<? extends String, ? extends String> fromPropertyFile( final String resourcePath ) throws MissingResourceException {
		final ResourceBundle properties = ResourceBundle.getBundle( resourcePath );

		final Map<String, String> parameters = new HashMap<>();
		for ( String key : properties.keySet() ) {
			final String value = properties.getString( key );
			parameters.put( key, value );
		}
		return parameters;
	}
}
