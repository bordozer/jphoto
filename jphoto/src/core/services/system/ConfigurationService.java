package core.services.system;

import core.enums.PhotoActionAllowance;
import core.general.configuration.Configuration;
import core.general.configuration.ConfigurationKey;
import core.general.configuration.SystemConfiguration;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ConfigurationService {

	String BEAN_NAME = "configurationService";

	List<SystemConfiguration> getAllSystemConfigurations();

	Map<String, Configuration> getSystemConfigurationParametersMap( final SystemConfiguration systemConfiguration );

	void reloadSystemConfiguration( final int systemConfigurationId );

	Configuration getConfiguration( final ConfigurationKey key );

	String getString( final ConfigurationKey key );

	int getInt( final ConfigurationKey key );

	long getLong( final ConfigurationKey key );

	Date getDate( final ConfigurationKey key );

	float getFloat( final ConfigurationKey key );

	boolean getBoolean( final ConfigurationKey key );

	List<String> getListString( final ConfigurationKey key );

	List<Integer> getListInt( final ConfigurationKey key );

	List<PhotoActionAllowance> getAccessiblePhotoCommentAllowance();

	List<PhotoActionAllowance> getAccessiblePhotoVotingAllowance();
}
