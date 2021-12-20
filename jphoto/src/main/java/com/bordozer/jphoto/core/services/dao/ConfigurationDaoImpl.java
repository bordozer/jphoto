package com.bordozer.jphoto.core.services.dao;

import com.bordozer.jphoto.core.general.configuration.Configuration;
import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.configuration.SystemConfiguration;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newLinkedHashMap;

@Component
public class ConfigurationDaoImpl extends BaseEntityDaoImpl<SystemConfiguration> implements ConfigurationDao {

    public static final String TABLE_SYSTEM_CONFIGURATION = "systemConfiguration";
    public static final String TABLE_SYSTEM_CONFIGURATION_COLUMN_NAME = "name";
    public static final String TABLE_SYSTEM_CONFIGURATION_COLUMN_DESCRIPTION = "description";
    public static final String TABLE_SYSTEM_CONFIGURATION_IS_DEFAULT = "isdefault";
    public static final String TABLE_SYSTEM_CONFIGURATION_IS_ACTIVE = "isactive";

    public static final String TABLE_SYSTEM_CONFIGURATION_KEYS = "systemConfigurationKeys";
    public static final String TABLE_CONFIGURATION_COLUMN_SYSTEM_CONFIGURATION_ID = "systemConfigurationId";
    public static final String TABLE_CONFIGURATION_COLUMN_CONFIGURATION_KEY_ID = "configurationKeyId";
    public static final String TABLE_CONFIGURATION_COLUMN_VALUE = "value";

    public static final Map<Integer, String> fields = newLinkedHashMap();

    static {
        fields.put(1, TABLE_SYSTEM_CONFIGURATION_COLUMN_NAME);
        fields.put(2, TABLE_SYSTEM_CONFIGURATION_COLUMN_DESCRIPTION);
        fields.put(3, TABLE_SYSTEM_CONFIGURATION_IS_DEFAULT);
        fields.put(4, TABLE_SYSTEM_CONFIGURATION_IS_ACTIVE);
    }

    @Override
    protected String getTableName() {
        return TABLE_SYSTEM_CONFIGURATION;
    }

    @Override
    public boolean saveToDB(final SystemConfiguration systemConfiguration) {
        return createOrUpdateEntry(systemConfiguration, fields, fields);
    }

    @Override
    protected RowMapper<SystemConfiguration> getRowMapper() {
        return new SystemConfigurationMapper();
    }

    @Override
    public List<SystemConfiguration> getAllSystemConfigurations() {
        final String sql = String.format("SELECT * FROM %s ORDER BY %s", TABLE_SYSTEM_CONFIGURATION, ENTITY_ID);
        return jdbcTemplate.query(sql, new MapSqlParameterSource(), new SystemConfigurationMapper());
    }

    @Override
    public List<Configuration> loadConfigurations(final int systemConfigurationId) {
        final String sql = String.format("SELECT * FROM %s WHERE %s=:systemConfigurationId", TABLE_SYSTEM_CONFIGURATION_KEYS, TABLE_CONFIGURATION_COLUMN_SYSTEM_CONFIGURATION_ID);

        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("systemConfigurationId", systemConfigurationId);

        return jdbcTemplate.query(sql, paramSource, new ConfigurationMapper());
    }

    @Override
    public Configuration loadConfiguration(final int systemConfigurationId, final ConfigurationKey configurationKey) {
        final String sql = String.format("SELECT * FROM %s WHERE %s=:systemConfigurationId AND %s=:configurationKeyId;"
                , TABLE_SYSTEM_CONFIGURATION_KEYS, TABLE_CONFIGURATION_COLUMN_SYSTEM_CONFIGURATION_ID, TABLE_CONFIGURATION_COLUMN_CONFIGURATION_KEY_ID);

        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("systemConfigurationId", systemConfigurationId);
        paramSource.addValue("configurationKeyId", configurationKey.getId());

        return jdbcTemplate.queryForObject(sql, paramSource, new ConfigurationMapper());
    }

    @Override
    public void deleteSystemConfigurationKeys(final int systemConfigurationId) {
        final String sql = String.format("DELETE FROM %s WHERE %s=:systemConfigurationId", TABLE_SYSTEM_CONFIGURATION_KEYS, TABLE_CONFIGURATION_COLUMN_SYSTEM_CONFIGURATION_ID);

        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("systemConfigurationId", systemConfigurationId);

        jdbcTemplate.update(sql, paramSource);
    }

    @Override
    public boolean save(final int systemConfigurationId, final Configuration configuration) {
        final String sql = String.format("INSERT INTO %s ( %s, %s, %s ) VALUES ( :systemConfigurationId, :configurationKeyId, :value );"
                , TABLE_SYSTEM_CONFIGURATION_KEYS
                , TABLE_CONFIGURATION_COLUMN_SYSTEM_CONFIGURATION_ID
                , TABLE_CONFIGURATION_COLUMN_CONFIGURATION_KEY_ID
                , TABLE_CONFIGURATION_COLUMN_VALUE
        );

        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("systemConfigurationId", systemConfigurationId);
        paramSource.addValue("configurationKeyId", configuration.getConfigurationKey().getId());
        paramSource.addValue("value", configuration.getValue());

        return jdbcTemplate.update(sql, paramSource) > 0;
    }

    @Override
    public int getDefaultConfigurationId() {
        final String sql = String.format("SELECT %s FROM %s WHERE %s=1;", ENTITY_ID, TABLE_SYSTEM_CONFIGURATION, TABLE_SYSTEM_CONFIGURATION_IS_DEFAULT);

        return getIntValueOrZero(sql, new MapSqlParameterSource());
    }

    @Override
    public int getActiveConfigurationId() {
        final String sql = String.format("SELECT %s FROM %s WHERE %s=1;", ENTITY_ID, TABLE_SYSTEM_CONFIGURATION, TABLE_SYSTEM_CONFIGURATION_IS_ACTIVE);

        return getIntValueOrZero(sql, new MapSqlParameterSource());
    }

    @Override
    public void activateSystemConfiguration(final int systemConfigurationId, final boolean isActive) {
        final String sql = String.format("UPDATE %s SET %s=:isActive WHERE %s=:id;", TABLE_SYSTEM_CONFIGURATION, TABLE_SYSTEM_CONFIGURATION_IS_ACTIVE, ENTITY_ID);

        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("id", systemConfigurationId);
        paramSource.addValue("isActive", isActive ? "1" : "0");

        jdbcTemplate.update(sql, paramSource);
    }

    @Override
    protected MapSqlParameterSource getParameters(final SystemConfiguration entry) {
        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue(TABLE_SYSTEM_CONFIGURATION_COLUMN_NAME, entry.getName());
        paramSource.addValue(TABLE_SYSTEM_CONFIGURATION_COLUMN_DESCRIPTION, entry.getDescription());
        paramSource.addValue(TABLE_SYSTEM_CONFIGURATION_IS_DEFAULT, entry.isDefaultConfiguration() ? "1" : "0");
        paramSource.addValue(TABLE_SYSTEM_CONFIGURATION_IS_ACTIVE, entry.isActiveConfiguration() ? "1" : "0");

        return paramSource;
    }

    private class ConfigurationMapper implements RowMapper<Configuration> {

        @Override
        public Configuration mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            final int configurationKeyId = rs.getInt(TABLE_CONFIGURATION_COLUMN_CONFIGURATION_KEY_ID);
            final ConfigurationKey configurationKey = ConfigurationKey.getById(configurationKeyId);

            final String value = rs.getString(TABLE_CONFIGURATION_COLUMN_VALUE);

            return new Configuration(configurationKey, value);
        }
    }

    private class SystemConfigurationMapper implements RowMapper<SystemConfiguration> {

        @Override
        public SystemConfiguration mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            final SystemConfiguration result = new SystemConfiguration();

            result.setId(rs.getInt(ENTITY_ID));
            result.setName(rs.getString(TABLE_SYSTEM_CONFIGURATION_COLUMN_NAME));
            result.setDescription(rs.getString(TABLE_SYSTEM_CONFIGURATION_COLUMN_DESCRIPTION));
            result.setDefaultConfiguration(rs.getBoolean(TABLE_SYSTEM_CONFIGURATION_IS_DEFAULT));
            result.setActiveConfiguration(rs.getBoolean(TABLE_SYSTEM_CONFIGURATION_IS_ACTIVE));

            return result;
        }
    }
}
