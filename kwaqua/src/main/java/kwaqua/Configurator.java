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
			final String name = parameter.getKey();
			final String value = parameter.getValue();
			System.setProperty( name, value );
			if ( name.startsWith( "webdriver" ) || name.startsWith( "thucydides" ) ) {
				LOGGER.debug( "Configuration property {} -> {}", name, value );
			}
		}
	}

	private Set<Map.Entry<String, String>> loadParameters() {
		final Map<String, String> parameters = new HashMap<>();
		loadDefaultParameters( parameters );
		loadSystemParameters( parameters );
//		loadCustomParameters( parameters );
		return parameters.entrySet();
	}

	private void loadDefaultParameters( Map<String, String> parameters ) {
		try {
			parameters.putAll( fromPropertyFile( "kwaqua" ) );
		} catch ( MissingResourceException ex ) {
			throw new ConfigurationException( "Missing file with configuration defaults.", ex );
		}
	}

	/*private void loadCustomParameters( Map<String, String> parameters ) {
		try {
			parameters.putAll( fromPropertyFile( "kwaqua" ) );
		} catch ( MissingResourceException ex ) {
			LOGGER.warn( "kwaqua.properties file cannot be found." );
		}
	}*/

	private void loadSystemParameters( Map<String, String> parameters ) {
		final Properties properties = System.getProperties();
		for ( String key : properties.stringPropertyNames() ) {
			final String value = properties.getProperty( key );
			parameters.put( key, value );
		}
	}

	private Map<? extends String, ? extends String> fromPropertyFile( String resourcePath ) throws MissingResourceException {
		final ResourceBundle properties = ResourceBundle.getBundle( resourcePath );

		final Map<String, String> parameters = new HashMap<>();
		for ( String key : properties.keySet() ) {
			final String value = properties.getString( key );
			parameters.put( key, value );
		}
		return parameters;
	}
}
