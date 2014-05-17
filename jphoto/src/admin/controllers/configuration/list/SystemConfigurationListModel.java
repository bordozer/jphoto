package admin.controllers.configuration.list;

import core.general.base.AbstractGeneralPageModel;
import core.general.configuration.SystemConfiguration;

import java.util.List;

public class SystemConfigurationListModel extends AbstractGeneralPageModel {

	private List<SystemConfiguration> systemConfigurations;

	public List<SystemConfiguration> getSystemConfigurations() {
		return systemConfigurations;
	}

	public void setSystemConfigurations( final List<SystemConfiguration> systemConfigurations ) {
		this.systemConfigurations = systemConfigurations;
	}
}
