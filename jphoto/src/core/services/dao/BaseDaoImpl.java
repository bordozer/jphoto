package core.services.dao;

import core.services.utils.DateUtilsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;

public class BaseDaoImpl {

	@Autowired
	public DateUtilsService dateUtilsService;

	protected NamedParameterJdbcTemplate jdbcTemplate;

	protected <T> T getEntryOrNull( final String sql, final MapSqlParameterSource paramSource, final RowMapper<T> rowMapper ) {
		final List<T> entries = jdbcTemplate.<T>query( sql, paramSource, rowMapper );

		if ( entries.size() == 0 ) {
			return null;
		}

		return entries.get( 0 );
	}

	protected boolean hasEntries( final String sql, final MapSqlParameterSource paramSource, final RowMapper rowMapper ) {
		return getEntryOrNull( sql, paramSource, rowMapper) != null;
	}

	protected boolean existsInt( final String sql, final MapSqlParameterSource paramSource ) {
		return getIntValueOrZero( sql, paramSource ) > 0;
	}

	protected int getIntValueOrZero( final String sql, final MapSqlParameterSource paramSource ) {
		try {
			return jdbcTemplate.queryForObject( sql, paramSource, Integer.class );
		} catch ( EmptyResultDataAccessException e ) {
			return 0;
		}
	}

	protected long getLongValueOrZero( final String sql, final MapSqlParameterSource paramSource ) {
		try {
			return jdbcTemplate.queryForObject( sql, paramSource, Long.class );
		} catch ( EmptyResultDataAccessException e ) {
			return 0;
		}
	}

	protected Date getDateValueOrZero( final String sql, final MapSqlParameterSource paramSource ) {
		try {
			return jdbcTemplate.queryForObject( sql, paramSource, Date.class );
		} catch ( EmptyResultDataAccessException e ) {
			return dateUtilsService.getEmptyTime();
		}
	}

	protected String getValueOrEmptyString( final String sql, final MapSqlParameterSource paramSource ) {
		try {
			return jdbcTemplate.queryForObject( sql, paramSource, String.class );
		} catch ( EmptyResultDataAccessException e ) {
			return StringUtils.EMPTY;
		}
	}

	protected boolean hasEntry( final String sql, final MapSqlParameterSource paramSource ) {
		try {
			return jdbcTemplate.queryForObject( sql, paramSource, Integer.class ) > 0;
		} catch ( EmptyResultDataAccessException e ) {
			return false;
		}
	}

	@Autowired
	public void setDataSource( final DataSource dataSource ) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate( dataSource );
	}

}
