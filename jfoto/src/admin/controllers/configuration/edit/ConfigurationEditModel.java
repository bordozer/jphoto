package admin.controllers.configuration.edit;

import core.general.configuration.ConfigurationKey;
import core.general.configuration.ConfigurationTab;
import core.general.configuration.Configuration;
import core.general.configuration.SystemConfiguration;
import elements.PageModel;
import org.springframework.validation.BindingResult;

import java.util.Map;

import static com.google.common.collect.Maps.newLinkedHashMap;

public class ConfigurationEditModel {

	public static final String CONFIGURATION_INFO_DIV_ID = "configurationInfo";
	public static final String CONFIGURATION_EDIT_INPUT_ID = "configurationEdit";

	private SystemConfiguration systemConfiguration;
	private Map<String, Configuration> configurationMap = newLinkedHashMap();
	private ConfigurationTab configurationTab;
	private String configurationTabKey;
	private String resetConfigurationKey;
	
	private Map<Integer, Integer> rankInGenrePointsMap;

	private PageModel pageModel = new PageModel();

	private BindingResult bindingResult;

	private boolean saveConfiguration;

	public SystemConfiguration getSystemConfiguration() {
		return systemConfiguration;
	}

	public void setSystemConfiguration( final SystemConfiguration systemConfiguration ) {
		this.systemConfiguration = systemConfiguration;
	}

	public Map<String, Configuration> getConfigurationMap() {
		return configurationMap;
	}

	public void setConfigurationMap( final Map<String, Configuration> configurationMap ) {
		this.configurationMap = configurationMap;
	}

	public String getConfigurationTabKey() {
		return configurationTabKey;
	}

	public void setConfigurationTabKey( final String configurationTabKey ) {
		this.configurationTabKey = configurationTabKey;
	}

	public String getResetConfigurationKey() {
		return resetConfigurationKey;
	}

	public void setResetConfigurationKey( final String resetConfigurationKey ) {
		this.resetConfigurationKey = resetConfigurationKey;
	}

	public ConfigurationTab getConfigurationTab() {
		return configurationTab;
	}

	public void setConfigurationTab( final ConfigurationTab configurationTab ) {
		this.configurationTab = configurationTab;
	}

	public PageModel getPageModel() {
		return pageModel;
	}

	public void setPageModel( final PageModel pageModel ) {
		this.pageModel = pageModel;
	}

	public BindingResult getBindingResult() {
		return bindingResult;
	}

	public void setBindingResult( final BindingResult bindingResult ) {
		this.bindingResult = bindingResult;
	}

	public String getSystemConfigurationName() {
		return systemConfiguration.getName();
	}

	public void setSystemConfigurationName( final String systemConfigurationName ) {
		systemConfiguration.setName( systemConfigurationName );
	}

	public String getDescription() {
		return systemConfiguration.getDescription();
	}

	public void setDescription( final String description ) {
		systemConfiguration.setDescription( description );
	}

	public boolean isSaveConfiguration() {
		return saveConfiguration;
	}

	public void setSaveConfiguration( final boolean saveConfiguration ) {
		this.saveConfiguration = saveConfiguration;
	}

	public Map<Integer, Integer> getRankInGenrePointsMap() {
		return rankInGenrePointsMap;
	}

	public void setRankInGenrePointsMap( final Map<Integer, Integer> rankInGenrePointsMap ) {
		this.rankInGenrePointsMap = rankInGenrePointsMap;
	}

	public Configuration getConfiguration( final ConfigurationKey configurationKey ) {
		final String configurationKeyId = String.valueOf( configurationKey.getId() );
		return configurationMap.get( configurationKeyId );
	}
}
