package core.services.system;

import core.general.configuration.SystemConfiguration;
import core.interfaces.BaseEntityService;

import java.util.List;

public interface SystemConfigurationLoadService extends BaseEntityService<SystemConfiguration> {

	List<SystemConfiguration> getAllSystemConfigurations();

	int getDefaultSystemConfigurationId();

	SystemConfiguration getDefaultSystemConfiguration();

	int getActiveSystemConfigurationId();

	void activateSystemConfiguration( final int systemConfigurationId );

	void deactivateSystemConfiguration( final int systemConfigurationId );
}
