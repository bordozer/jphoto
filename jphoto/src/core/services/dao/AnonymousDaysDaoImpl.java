package core.services.dao;

import core.general.anonym.AnonymousDay;
import core.services.utils.DateUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AnonymousDaysDaoImpl extends BaseDaoImpl implements AnonymousDaysDao {

	public static final String TABLE_ANONYMOUS_DAY = "anonymousDay";
	public static final String TABLE_ANONYMOUS_DAY_COL_DATE = "anonymousDayDate";

	@Autowired
	private DateUtilsService dateUtilsService;

	@Override
	public boolean addAnonymousDay( final AnonymousDay day ) {
		final String sql = String.format( "INSERT INTO %s ( %s ) VALUES( :dayDate );", TABLE_ANONYMOUS_DAY, TABLE_ANONYMOUS_DAY_COL_DATE );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "dayDate", day.getDate() );

		return jdbcTemplate.update( sql, paramSource ) > 0;
	}

	@Override
	public List<AnonymousDay> loadAll() {
		final String sql = String.format( "SELECT * FROM %s ORDER BY %s DESC;", TABLE_ANONYMOUS_DAY, TABLE_ANONYMOUS_DAY_COL_DATE );
		return jdbcTemplate.query( sql, new MapSqlParameterSource(), new AnonymousDayRowMapper() );
	}

	@Override
	public void deleteAnonymousDay( final AnonymousDay day ) {
		final String sql = String.format( "DELETE FROM %s WHERE %s = :dayDate;", TABLE_ANONYMOUS_DAY, TABLE_ANONYMOUS_DAY_COL_DATE );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "dayDate", day.getDate() );

		jdbcTemplate.update( sql, paramSource );
	}

	@Override
	public boolean isDayAnonymous( final AnonymousDay day ) {
		final String sql = String.format( "SELECT 1 FROM %s WHERE %s = :dayDate;", TABLE_ANONYMOUS_DAY, TABLE_ANONYMOUS_DAY_COL_DATE );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "dayDate", day.getDate() );

		return existsInt( sql, paramSource );
	}

	private class AnonymousDayRowMapper implements RowMapper<AnonymousDay> {

		@Override
		public AnonymousDay mapRow( final ResultSet rs, final int rowNum ) throws SQLException {
			return new AnonymousDay( rs.getTimestamp( TABLE_ANONYMOUS_DAY_COL_DATE ), dateUtilsService );
		}
	}
}
