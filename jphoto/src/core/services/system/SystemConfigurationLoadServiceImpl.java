package core.services.system;

import core.exceptions.BaseRuntimeException;
import core.general.configuration.Configuration;
import core.general.configuration.ConfigurationKey;
import core.general.configuration.SystemConfiguration;
import core.services.dao.ConfigurationDao;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class SystemConfigurationLoadServiceImpl implements SystemConfigurationLoadService {

	@Autowired
	private ConfigurationDao configurationDao;

	@Override
	public boolean save( final SystemConfiguration entry ) {
		final int systemConfigurationId = entry.getId();

		if ( !configurationDao.saveToDB( entry ) ) {
			return false;
		}

		configurationDao.deleteSystemConfigurationKeys( systemConfigurationId );

		boolean result = true;
		for ( final Configuration configuration : entry.getConfigurations() ) {
			result &= configurationDao.save( entry.getId(), configuration );
		}

		if ( !result ) {
			throw new BaseRuntimeException( "Can not save configuration" );
		}

		return true;
	}

	@Override
	public SystemConfiguration load( final int systemConfigurationId ) {

		final SystemConfiguration systemConfiguration = configurationDao.load( systemConfigurationId );

		if ( systemConfiguration == null ) {
			return null;
		}

		final List<Configuration> configurations = configurationDao.loadConfigurations( systemConfigurationId );

		filterLegacyConfigurationKeys( configurations );

		if ( systemConfiguration.isDefaultConfiguration() ) {

			for ( final Configuration configuration : configurations ) {
				configuration.setGotFromDefaultSystemConfiguration( true );
			}

			configurations.addAll( getMissedInDBConfigurations( configurations ) );

		} else {

			final List<Configuration> defaultConfigurations = getDefaultSystemConfiguration().getConfigurations();

			for ( final Configuration defaultConfiguration : defaultConfigurations ) {
				if ( ! configurations.contains( defaultConfiguration ) ) {
					final Configuration configuration = new Configuration( defaultConfiguration.getConfigurationKey(), defaultConfiguration.getValue() );
					configuration.setGotFromDefaultSystemConfiguration( true );
					configuration.setMissedInDB( defaultConfiguration.isMissedInDB() );
					configuration.setDefaultSystemConfiguration( defaultConfiguration );

					configurations.add( configuration );
				}
			}
		}

		systemConfiguration.setConfigurations( getConfigurationSorted( configurations ) );

		return systemConfiguration;
	}

	@Override
	public boolean delete( final int entryId ) {
		configurationDao.deleteSystemConfigurationKeys( entryId );
		return configurationDao.delete( entryId );
	}

	@Override
	public List<SystemConfiguration> getAllSystemConfigurations() {
		return configurationDao.getAllSystemConfigurations();
	}

	@Override
	public int getDefaultSystemConfigurationId() {
		return configurationDao.getDefaultConfigurationId();
	}

	@Override
	public int getActiveSystemConfigurationId() {
		return configurationDao.getActiveConfigurationId();
	}

	@Override
	public SystemConfiguration getDefaultSystemConfiguration() {
		return load( getDefaultSystemConfigurationId() );
	}

	@Override
	public List<SystemConfiguration> getCustomSystemConfigurations() {
		final List<SystemConfiguration> result = getAllSystemConfigurations();

		CollectionUtils.filter( result, new Predicate<SystemConfiguration>() {
			@Override
			public boolean evaluate( final SystemConfiguration systemConfiguration ) {
				return ! systemConfiguration.isDefaultConfiguration();
			}
		} );

		return result;
	}

	@Override
	public void activateSystemConfiguration( final int systemConfigurationId ) {
		configurationDao.activateSystemConfiguration( systemConfigurationId, true );
	}

	@Override
	public void deactivateSystemConfiguration( final int systemConfigurationId ) {
		configurationDao.activateSystemConfiguration( systemConfigurationId, false );
	}

	@Override
	public boolean exists( final int entryId ) {
		return configurationDao.exists( entryId );
	}

	@Override
	public boolean exists( final SystemConfiguration entry ) {
		return configurationDao.exists( entry );
	}

	private void filterLegacyConfigurationKeys( final List<Configuration> configurations ) {
		CollectionUtils.filter( configurations, new Predicate<Configuration>() {
			@Override
			public boolean evaluate( final Configuration configuration ) {
				return configuration.getConfigurationKey() != null;
			}
		} );
	}

	private List<Configuration> getMissedInDBConfigurations( final List<Configuration> configurations ) {

		final List<Configuration> missedInDBConfigurations = newArrayList();

		for ( final ConfigurationKey configurationKey : ConfigurationKey.values() ) {

			boolean configurationKeyExistsInDB = false;

			for ( final Configuration configuration : configurations ) {
				if ( configuration.getConfigurationKey() == configurationKey ) {
					configurationKeyExistsInDB = true;
					break;
				}
			}

			if ( ! configurationKeyExistsInDB ) {
				final Configuration missedConfiguration = new Configuration( configurationKey, configurationKey.getDefaultValue() );
				missedConfiguration.setMissedInDB( true );

				missedInDBConfigurations.add( missedConfiguration );
			}
		}

		return missedInDBConfigurations;
	}

	private List<Configuration> getConfigurationSorted( final List<Configuration> configurations ) {
		final List<Configuration> sortedConfigurations = newArrayList();

		for ( final ConfigurationKey configurationKey : ConfigurationKey.values() ) {
			for ( final Configuration configuration : configurations ) {
				if ( configuration.getConfigurationKey().equals( configurationKey ) ) {
					sortedConfigurations.add( configuration );
					break;
				}
			}
		}
		return sortedConfigurations;
	}

	private boolean containsConfiguration( final List<Configuration> configurations, final Configuration beingCheckedConfiguration ) {
		for ( final Configuration configuration : configurations ) {
			if ( configuration.getConfigurationKey() == beingCheckedConfiguration.getConfigurationKey() ) {
				return true;
			}
		}
		return false;
	}
}
