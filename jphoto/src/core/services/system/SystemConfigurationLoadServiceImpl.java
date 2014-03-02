package core.services.system;

import core.exceptions.BaseRuntimeException;
import core.general.configuration.Configuration;
import core.general.configuration.ConfigurationKey;
import core.general.configuration.SystemConfiguration;
import core.services.dao.ConfigurationDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class SystemConfigurationLoadServiceImpl implements SystemConfigurationLoadService {

	@Autowired
	private ConfigurationDao configurationDao;

	@Autowired
	private CacheService cacheService;

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

		return result;
	}

	@Override
	public SystemConfiguration load( final int systemConfigurationId ) {
		final SystemConfiguration systemConfiguration = configurationDao.load( systemConfigurationId );
		final List<Configuration> configurations = configurationDao.loadConfigurations( systemConfigurationId );

		if ( systemConfiguration == null ) {
			return null;
		}

		if ( systemConfiguration.isDefaultConfiguration() ) {
			for ( final Configuration configuration : configurations ) {
				configuration.setGotFromDefaultSystemConfiguration( true );
			}
		} else {
			final SystemConfiguration defaultSystemConfiguration = getDefaultSystemConfiguration();
			final List<Configuration> defaultConfigurations = defaultSystemConfiguration.getConfigurations();
			for ( final Configuration defaultConfiguration : defaultConfigurations ) {
				if ( ! containsConfiguration( configurations, defaultConfiguration ) ) {
					configurations.add( defaultConfiguration );
				}
			}
		}

		systemConfiguration.setConfigurations( getConfigurationSorted( configurations ) );

		return systemConfiguration;
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

	@Override
	public boolean delete( final int entryId ) {
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
}
