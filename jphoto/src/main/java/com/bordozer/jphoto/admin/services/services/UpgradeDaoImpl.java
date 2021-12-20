package com.bordozer.jphoto.admin.services.services;

import com.bordozer.jphoto.admin.upgrade.entities.UpgradeTaskLogEntry;
import com.bordozer.jphoto.admin.upgrade.entities.UpgradeTaskResult;
import com.bordozer.jphoto.admin.upgrade.entities.UpgradeTaskToPerform;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class UpgradeDaoImpl implements UpgradeDao {

    public static final String TABLE_UPGRADE = "upgradeLog";
    public static final String TABLE_UPGRADE_TASK_NAME = "upgradeTaskName";
    public static final String TABLE_UPGRADE_PERFORM_TIME = "performanceTime";
    public static final String TABLE_UPGRADE_TASK_RESULT = "taskResult";

    protected NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private DateUtilsService dateUtilsService;

    @Override
    public void save(final UpgradeTaskToPerform upgradeTaskToPerform) {
        final String sql = String.format("INSERT INTO %s ( %s, %s, %s ) VALUES( :name, :time, :result );"
                , TABLE_UPGRADE, TABLE_UPGRADE_TASK_NAME, TABLE_UPGRADE_PERFORM_TIME, TABLE_UPGRADE_TASK_RESULT);

        final MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("name", upgradeTaskToPerform.getUpgradeTaskName());
        paramSource.addValue("time", dateUtilsService.getCurrentTime());
        paramSource.addValue("result", upgradeTaskToPerform.getUpgradeTaskResult().getId());

        jdbcTemplate.update(sql, paramSource);
    }

    @Override
    public List<UpgradeTaskLogEntry> getPerformedUpgradeTasks() {
        final String sql = String.format("SELECT * FROM %s ORDER BY %s DESC;", TABLE_UPGRADE, TABLE_UPGRADE_PERFORM_TIME);
        return jdbcTemplate.query(sql, new MapSqlParameterSource(), new UpgradeLogTaskMapper());
    }

    private class UpgradeLogTaskMapper implements RowMapper<UpgradeTaskLogEntry> {

        @Override
        public UpgradeTaskLogEntry mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            final UpgradeTaskLogEntry result = new UpgradeTaskLogEntry();

            result.setUpgradeTaskName(rs.getString(TABLE_UPGRADE_TASK_NAME));
            result.setPerformanceTime(rs.getTimestamp(TABLE_UPGRADE_PERFORM_TIME));
            result.setUpgradeTaskResult(UpgradeTaskResult.getById(rs.getInt(TABLE_UPGRADE_TASK_RESULT)));

            return result;
        }
    }

    @Autowired
    public void setDataSource(final DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }
}
