package core.services.system;

import core.enums.PhotoActionAllowance;
import core.exceptions.NoSuchConfigurationException;
import core.general.configuration.Configuration;
import core.general.configuration.ConfigurationKey;
import core.general.configuration.ConfigurationTab;
import core.general.configuration.SystemConfiguration;
import core.log.LogHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newLinkedHashMap;

public class ConfigurationServiceImpl implements ConfigurationService {

	private SystemConfiguration systemConfiguration;

	@Autowired
	private SystemConfigurationLoadService systemConfigurationLoadService;

	private LogHelper log = new LogHelper( ConfigurationServiceImpl.class );

	@Override
	public Map<String, Configuration> getSystemConfigurationParametersMap( final SystemConfiguration systemConfiguration ) {
		final SystemConfiguration defaultSystemConfiguration = systemConfigurationLoadService.getDefaultSystemConfiguration();
		final Map<String, Configuration> configurationMap = newLinkedHashMap();

		final List<Configuration> savedConfigurations = defaultSystemConfiguration.getConfigurations();

		addNewConfigurationKeys( savedConfigurations );

		for ( final ConfigurationTab configurationTab : ConfigurationTab.values() ) {
			for ( final Configuration configuration : savedConfigurations ) {
				final ConfigurationKey configurationKey = configuration.getConfigurationKey();
				if ( configurationKey != null && configurationKey.getTab() == configurationTab ) {
					final Configuration beingChangedConfiguration = systemConfiguration.getConfiguration( configurationKey );
					final String configurationKeyId = String.valueOf( configurationKey.getId() );
					if ( ! systemConfiguration.isDefaultConfiguration() && beingChangedConfiguration != null && ! beingChangedConfiguration.getValue().equals( configuration.getValue() ) ) {
						beingChangedConfiguration.setDefaultSystemConfiguration( configuration );
						configurationMap.put( configurationKeyId, beingChangedConfiguration );
					} else {
						configurationMap.put( configurationKeyId, configuration );
					}
				}
			}
		}

		return configurationMap;
	}

	@Override
	public Configuration getConfiguration( final ConfigurationKey key ) {
		assertConfigurationKeyExists( key );
		return find( key );
	}

	@Override
	public String getString( final ConfigurationKey key ) {
		return getConfiguration( key ).getValue();
	}

	@Override
	public int getInt( final ConfigurationKey key ) {
		return getConfiguration( key ).getValueInt();
	}

	@Override
	public long getLong( final ConfigurationKey key ) {
		return getConfiguration( key ).getValueLong();
	}

	@Override
	public float getFloat( final ConfigurationKey key ) {
		return getConfiguration( key ).getValueFloat();
	}

	@Override
	public boolean getBoolean( final ConfigurationKey key ) {
		return getConfiguration( key ).getValueYesNo();
	}

	@Override
	public List<String> getListString( final ConfigurationKey key ) {
		return getConfiguration( key ).getValueListString();
	}

	@Override
	public List<Integer> getListInt( final ConfigurationKey key ) {
		return getConfiguration( key ).getValueListInt();
	}

	@Override
	public List<SystemConfiguration> getAllSystemConfigurations() {
		return systemConfigurationLoadService.getAllSystemConfigurations();
	}

	@Override
	public void reloadSystemConfiguration( final int systemConfigurationId ) {
		synchronized ( systemConfiguration ) {
			systemConfiguration = systemConfigurationLoadService.load( systemConfigurationId );
		}
	}

	private Configuration find( final ConfigurationKey key ) {
		return systemConfiguration.findConfiguration( key );
	}

	@Override
	public List<PhotoActionAllowance> getAccessiblePhotoCommentAllowance() {
		final List<PhotoActionAllowance> accessibleCommentAllowance = newArrayList();

		accessibleCommentAllowance.add( PhotoActionAllowance.ACTIONS_DENIED );
		if ( getBoolean( ConfigurationKey.CANDIDATES_CAN_COMMENT_PHOTOS ) ) {
			accessibleCommentAllowance.add( PhotoActionAllowance.CANDIDATES_AND_MEMBERS );
		}
		accessibleCommentAllowance.add( PhotoActionAllowance.MEMBERS_ONLY );

		return accessibleCommentAllowance;
	}

	@Override
	public List<PhotoActionAllowance> getAccessiblePhotoVotingAllowance() {
		final List<PhotoActionAllowance> accessibleVotingAllowance = newArrayList();

		accessibleVotingAllowance.add( PhotoActionAllowance.ACTIONS_DENIED );
		if ( getBoolean( ConfigurationKey.CANDIDATES_CAN_VOTE_FOR_PHOTOS ) ) {
			accessibleVotingAllowance.add( PhotoActionAllowance.CANDIDATES_AND_MEMBERS );
		}
		accessibleVotingAllowance.add( PhotoActionAllowance.MEMBERS_ONLY );

		return accessibleVotingAllowance;
	}

	private void loadSystemConfiguration() {
		//		synchronized ( systemConfiguration ) {
		final int activeConfigurationId = systemConfigurationLoadService.getActiveSystemConfigurationId();

		if ( activeConfigurationId > 0 ) {
			systemConfiguration = systemConfigurationLoadService.load( activeConfigurationId );

			log.info( String.format( "Active configuration %s is loaded", activeConfigurationId ) );

			return;
		}

		systemConfiguration = getHardcodedSystemConfiguration();
		systemConfigurationLoadService.save( systemConfiguration );
		//		}
	}

	private void addNewConfigurationKeys( final List<Configuration> savedConfigurations ) {
		final List<ConfigurationKey> newConfigurationKeys = newArrayList( ConfigurationKey.values() );
		final Iterator<ConfigurationKey> iterator = newConfigurationKeys.iterator();
		while( iterator.hasNext() ) {
			final ConfigurationKey configurationKey = iterator.next();

			for ( final Configuration configuration : savedConfigurations ) {
				if ( configuration.getConfigurationKey() == configurationKey ) {
					iterator.remove();
					break;
				}
			}
		}

		for ( final ConfigurationKey configurationKey : newConfigurationKeys ) {
			savedConfigurations.add( new Configuration( configurationKey, "" ) );
		}
	}

	private void assertConfigurationKeyExists( final ConfigurationKey key ) {
		final Configuration configuration = find( key );

		if ( configuration == null ) {
			throw new NoSuchConfigurationException( String.format( "Configuration key '%s' (id=%s) not found", key, key.getId() ) );
		}
	}

	private SystemConfiguration getHardcodedSystemConfiguration() {
		final List<Configuration> configurations = newArrayList();
		for ( final ConfigurationKey configurationKey : ConfigurationKey.values() ) {
			final Configuration configuration = new Configuration( configurationKey, configurationKey.getDefaultValue() );
			configurations.add( configuration );
		}

		final SystemConfiguration systemConfiguration = new SystemConfiguration();
		systemConfiguration.setDefaultConfiguration( true );
		systemConfiguration.setActiveConfiguration( true );
		systemConfiguration.setName( "Default system configuration" );
		systemConfiguration.setDescription( "Created automatically from hardcoded template" );
		systemConfiguration.setConfigurations( configurations );

		log.info( "Hardcoded configuration is loaded" );

		return systemConfiguration;
	}

	public void setSystemConfiguration( final SystemConfiguration systemConfiguration ) {
		this.systemConfiguration = systemConfiguration;
	}
}
