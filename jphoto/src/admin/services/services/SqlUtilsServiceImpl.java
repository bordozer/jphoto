package admin.services.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.sql.DataSource;
import java.util.List;

public class SqlUtilsServiceImpl implements SqlUtilsService {

	protected NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	public void execSQL( final String sql ) {
		jdbcTemplate.update( sql, new MapSqlParameterSource() );
	}

	@Override
	public void renameTable( final String tableToRename, final String newName ) {
		String sql = String.format( "alter table %s rename to %s", tableToRename, newName );
		jdbcTemplate.update( sql, new MapSqlParameterSource() );
	}

	@Override
	public void renameColumn( final String table, final String columnToRename, final String columnType, final String newName ) {
		final String sql = String.format( "alter table %s change column `%s` `%s` %s %s", table, columnToRename, newName, columnType, newName );
		jdbcTemplate.update( sql, new MapSqlParameterSource() );
	}

	@Override
	public <T> List<T> query( final String sql, final SqlParameterSource paramSource, final RowMapper<T> rowMapper ) {
		return jdbcTemplate.query( sql, paramSource, rowMapper );
	}

	@Autowired
	public void setDataSource( final DataSource dataSource ) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate( dataSource );
	}
}
