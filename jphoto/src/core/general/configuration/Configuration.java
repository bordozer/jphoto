package core.general.configuration;

import org.apache.commons.lang.StringUtils;
import utils.NumberUtils;

import java.util.Arrays;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class Configuration implements Cloneable {

	public static final String VALUE_SEPARATOR = ",";
	public static final String YES_VALUE = "1";

	private final ConfigurationKey configurationKey;
	private String value;

	private boolean gotFromDefaultSystemConfiguration;
	private Configuration defaultSystemConfiguration;
	private boolean missedInDB = false;

	public Configuration( final ConfigurationKey configurationKey, final String value ) {
		this.configurationKey = configurationKey;
		this.value = value;
	}

	public ConfigurationKey getConfigurationKey() {
		return configurationKey;
	}

	public String getValue() {
		return value;
	}

	public void setValue( final String value ) {
		this.value = value;
	}

	public int getValueInt() {
		return NumberUtils.convertToInt( value );
	}

	public long getValueLong() {
		return NumberUtils.convertToLong( value );
	}

	public float getValueFloat() {
		return NumberUtils.convertToFloat( value );
	}

	public boolean getValueYesNo() {
		return value.equals( Configuration.YES_VALUE );
	}

	public boolean isGotFromDefaultSystemConfiguration() {
		return gotFromDefaultSystemConfiguration;
	}

	public void setGotFromDefaultSystemConfiguration( final boolean gotFromDefaultSystemConfiguration ) {
		this.gotFromDefaultSystemConfiguration = gotFromDefaultSystemConfiguration;
	}

	public Configuration getDefaultSystemConfiguration() {
		return defaultSystemConfiguration;
	}

	public void setDefaultSystemConfiguration( final Configuration defaultSystemConfiguration ) {
		this.defaultSystemConfiguration = defaultSystemConfiguration;
	}

	public List<String> getValueListString() {
		final String[] strings = value.split( VALUE_SEPARATOR );

		final List<String> result = newArrayList();
		for ( final String string : strings ) {
			final String element = StringUtils.trim( string );
			if ( StringUtils.isNotEmpty( element ) ) {
				result.add( element );
			}
		}

		return result;
	}

	public List<Integer> getValueListInt() {
		final List<String> strings = Arrays.asList( value.split( VALUE_SEPARATOR ) );

		final List<Integer> integers = newArrayList();
		for ( final String string : strings ) {
			integers.add( Integer.valueOf( string ) );
		}

		return integers;
	}

	public boolean isMissedInDB() {
		return missedInDB;
	}

	public void setMissedInDB( final boolean missedInDB ) {
		this.missedInDB = missedInDB;
	}

	@Override
	public int hashCode() {
		return configurationKey.hashCode() + 31 * value.hashCode();
	}

	@Override
	public Configuration clone() {
		return new Configuration( configurationKey, value );
	}

	@Override
	public boolean equals( final Object obj ) {
		if ( obj == null ) {
			return false;
		}

		if ( obj == this ) {
			return true;
		}

		if ( !( obj instanceof Configuration ) ) {
			return false;
		}

		final Configuration configuration = ( Configuration ) obj;
		return configuration.getConfigurationKey().equals( configurationKey ) && configuration.getValue().equals( value );
	}


	@Override
	public String toString() {
		return String.format( "%s: %s", configurationKey.getId(), value );
	}
}
