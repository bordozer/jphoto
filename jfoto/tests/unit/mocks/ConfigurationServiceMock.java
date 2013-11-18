package mocks;

import core.enums.PhotoActionAllowance;
import core.general.configuration.Configuration;
import core.general.configuration.ConfigurationKey;
import core.general.configuration.SystemConfiguration;
import core.services.system.ConfigurationService;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ConfigurationServiceMock implements ConfigurationService {

	@Override
	public List<SystemConfiguration> getAllSystemConfigurations() {
		return null;
	}

	@Override
	public Map<String, Configuration> getSystemConfigurationParametersMap( final SystemConfiguration systemConfiguration ) {
		return null;
	}

	@Override
	public void reloadSystemConfiguration( final int systemConfigurationId ) {
	}

	@Override
	public Configuration getConfiguration( final ConfigurationKey key ) {
		return null;
	}

	@Override
	public String getString( final ConfigurationKey key ) {
		return null;
	}

	@Override
	public int getInt( final ConfigurationKey key ) {
		return 0;
	}

	@Override
	public long getLong( final ConfigurationKey key ) {
		return 0;
	}

	@Override
	public Date getDate( final ConfigurationKey key ) {
		return new Date();
	}

	@Override
	public float getFloat( final ConfigurationKey key ) {
		return 0;
	}

	@Override
	public boolean getBoolean( final ConfigurationKey key ) {
		return false;
	}

	@Override
	public List<String> getListString( final ConfigurationKey key ) {
		return null;
	}

	@Override
	public List<Integer> getListInt( final ConfigurationKey key ) {
		return null;
	}

	@Override
	public List<PhotoActionAllowance> getAccessiblePhotoCommentAllowance() {
		return null;
	}

	@Override
	public List<PhotoActionAllowance> getAccessiblePhotoVotingAllowance() {
		return null;
	}
}
