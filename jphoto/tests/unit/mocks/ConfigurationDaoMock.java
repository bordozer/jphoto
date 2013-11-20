package mocks;

import core.general.configuration.Configuration;
import core.general.configuration.ConfigurationKey;
import core.general.configuration.SystemConfiguration;
import core.services.dao.ConfigurationDao;
import sql.SqlSelectIdsResult;
import sql.SqlSelectResult;
import sql.builder.SqlIdsSelectQuery;
import sql.builder.SqlSelectQuery;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class ConfigurationDaoMock implements ConfigurationDao {

	@Override
	public List<SystemConfiguration> getAllSystemConfigurations() {
		return newArrayList();
	}

	@Override
	public List<Configuration> loadConfigurations( final int systemConfigurationId ) {
		return newArrayList();
	}

	@Override
	public void deleteSystemConfigurationKeys( final int systemConfigurationId ) {
	}

	@Override
	public Configuration loadConfiguration( final int systemConfigurationId, final ConfigurationKey configurationKey ) {
		return null;
	}

	@Override
	public boolean save( final int systemConfigurationId, final Configuration configuration ) {
		return false;
	}

	@Override
	public int getDefaultConfigurationId() {
		return 0;
	}

	@Override
	public int getActiveConfigurationId() {
		return 0;
	}

	@Override
	public void activateSystemConfiguration( final int systemConfigurationId, final boolean isActive ) {
	}

	@Override
	public boolean saveToDB( final SystemConfiguration entry ) {
		return false;
	}

	@Override
	public SystemConfiguration load( final int entryId ) {
		return null;
	}

	@Override
	public SqlSelectResult<SystemConfiguration> load( final SqlSelectQuery selectQuery ) {
		return null;
	}

	@Override
	public boolean delete( final int entryId ) {
		return false;
	}

	@Override
	public boolean exists( final SystemConfiguration entry ) {
		return false;
	}

	@Override
	public boolean exists( final int entryId ) {
		return false;
	}

	@Override
	public SqlSelectIdsResult load( final SqlIdsSelectQuery selectIdsQuery ) {
		return null;
	}
}
