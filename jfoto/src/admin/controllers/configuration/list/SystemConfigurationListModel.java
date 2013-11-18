package admin.controllers.configuration.list;

import core.general.configuration.SystemConfiguration;
import elements.PageModel;

import java.util.List;

public class SystemConfigurationListModel {

	private List<SystemConfiguration> systemConfigurations;

	private PageModel pageModel = new PageModel();

	public List<SystemConfiguration> getSystemConfigurations() {
		return systemConfigurations;
	}

	public void setSystemConfigurations( final List<SystemConfiguration> systemConfigurations ) {
		this.systemConfigurations = systemConfigurations;
	}

	public PageModel getPageModel() {
		return pageModel;
	}

	public void setPageModel( final PageModel pageModel ) {
		this.pageModel = pageModel;
	}
}
