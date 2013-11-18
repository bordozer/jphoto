package core.services.dao;

import core.general.user.User;
import core.general.user.UsersSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class UsersSecurityDaoImpl extends BaseDaoImpl implements UsersSecurityDao {

	public final static String TABLE_USER_SECURITY = "userSecurity";
	public final static String TABLE_USER_SECURITY_COL_USER_ID = "userId";
	public final static String TABLE_USER_SECURITY_COL_PASSWORD = "userPassword";
	public final static String TABLE_USER_SECURITY_COL_LAST_LOGIN_TIME = "lastLoginTime";
	public final static String TABLE_USER_SECURITY_COL_IP = "lastLoginIP";
	public final static String TABLE_USER_SECURITY_COL_AUTHORIZATION_KEY = "authorizationKey";
	public final static String TABLE_USER_SECURITY_COL_LAST_USER_ACTIVITY_TIME = "lastUserActivityTime";

	@Autowired
	private UserDao userDao;

	@Override
	public boolean createEntry( final int userId, final String cryptPassword ) {
		final String sql = String.format( "INSERT INTO %s ( %s, %s ) VALUES( :userId, :userPassword );", TABLE_USER_SECURITY, TABLE_USER_SECURITY_COL_USER_ID, TABLE_USER_SECURITY_COL_PASSWORD );

		final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue( "userId", userId );
		parameterSource.addValue( "userPassword", cryptPassword );

		return jdbcTemplate.update( sql, parameterSource ) > 0;
	}

	@Override
	public boolean save( final UsersSecurity usersSecurity ) {
		final int userId = usersSecurity.getUser().getId();

		final String sql;
		if ( isSecurityEntryExists( userId ) ) {
			sql = String.format( "UPDATE %s SET %s=:userPassword, %s=:lastLoginTime, %s=:lastLoginIP, %s=:authorizationKey WHERE %s=:userId;"
				, TABLE_USER_SECURITY, TABLE_USER_SECURITY_COL_PASSWORD, TABLE_USER_SECURITY_COL_LAST_LOGIN_TIME, TABLE_USER_SECURITY_COL_IP, TABLE_USER_SECURITY_COL_AUTHORIZATION_KEY, TABLE_USER_SECURITY_COL_USER_ID );
		} else {
			sql = String.format( "INSERT INTO %s ( %s, %s, %s, %s, %s, ) VALUES( :userId, :userPassword, :lastLoginTime, :lastLoginIP, :authorizationKey );"
				, TABLE_USER_SECURITY, TABLE_USER_SECURITY_COL_USER_ID, TABLE_USER_SECURITY_COL_PASSWORD, TABLE_USER_SECURITY_COL_LAST_LOGIN_TIME, TABLE_USER_SECURITY_COL_IP, TABLE_USER_SECURITY_COL_AUTHORIZATION_KEY );
		}

		final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue( TABLE_USER_SECURITY_COL_USER_ID, userId );
		parameterSource.addValue( TABLE_USER_SECURITY_COL_PASSWORD, usersSecurity.getUserPassword() );
		parameterSource.addValue( TABLE_USER_SECURITY_COL_LAST_LOGIN_TIME, usersSecurity.getLastLoginTime() );
		parameterSource.addValue( TABLE_USER_SECURITY_COL_IP, usersSecurity.getLastLoginIp() );
		parameterSource.addValue( TABLE_USER_SECURITY_COL_AUTHORIZATION_KEY, usersSecurity.getAuthorizationKey() );
		parameterSource.addValue( TABLE_USER_SECURITY_COL_LAST_USER_ACTIVITY_TIME, usersSecurity.getLastActivityTime() );

		return jdbcTemplate.update( sql, parameterSource ) > 0;
	}

	@Override
	public UsersSecurity load( final User user ) {
		final String sql = String.format( "SELECT * FROM %s WHERE %s = :userId;", TABLE_USER_SECURITY, TABLE_USER_SECURITY_COL_USER_ID );

		final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue( "userId", user.getId() );

		return getEntryOrNull( sql, parameterSource, new UsersSecurityMapper() );
	}

	@Override
	public void delete( final int userId ) {
		final String sql = String.format( "DELETE FROM %s WHERE %s = :userId;", TABLE_USER_SECURITY, TABLE_USER_SECURITY_COL_USER_ID );

		final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue( "userId", userId );

		jdbcTemplate.update( sql, parameterSource );
	}

	@Override
	public Date getLastUserActivityTime( final int userId ) {
		final String sql = String.format( "SELECT %s FROM %s WHERE %s = :userId;"
			, TABLE_USER_SECURITY_COL_LAST_USER_ACTIVITY_TIME, TABLE_USER_SECURITY, TABLE_USER_SECURITY_COL_USER_ID );

		final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue( "userId", userId );

		return getDateValueOrZero( sql, parameterSource );
	}

	@Override
	public void saveLastUserActivityTime( final int userId, final Date time ) {
		final String sql = String.format( "UPDATE %s SET %s=:time WHERE %s = :userId;"
			, TABLE_USER_SECURITY, TABLE_USER_SECURITY_COL_LAST_USER_ACTIVITY_TIME, TABLE_USER_SECURITY_COL_USER_ID );

		final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue( "userId", userId );
		parameterSource.addValue( "time", time );

		jdbcTemplate.update( sql, parameterSource );
	}

	@Override
	public void changeUserPassword( final int userId, final String cryptPassword ) {
		final String sql = String.format( "UPDATE %s SET %s=:password WHERE %s = :userId;"
			, TABLE_USER_SECURITY, TABLE_USER_SECURITY_COL_PASSWORD, TABLE_USER_SECURITY_COL_USER_ID );

		final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue( "userId", userId );
		parameterSource.addValue( "password", cryptPassword );

		jdbcTemplate.update( sql, parameterSource );
	}

	private boolean isSecurityEntryExists( final int userId ) {
		final String sql = String.format( "SELECT 1 FROM %s WHERE %s = :userId;", TABLE_USER_SECURITY, TABLE_USER_SECURITY_COL_USER_ID );

		final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue( "userId", userId );

		return existsInt( sql, parameterSource );
	}

	private class UsersSecurityMapper implements RowMapper<UsersSecurity> {

		@Override
		public UsersSecurity mapRow( final ResultSet rs, final int rowNum ) throws SQLException {
			final int userId = rs.getInt( TABLE_USER_SECURITY_COL_USER_ID );

			final UsersSecurity result = new UsersSecurity( userDao.load( userId ) );
			result.setUserPassword( rs.getString( TABLE_USER_SECURITY_COL_PASSWORD ) );
			result.setLastLoginTime( rs.getTimestamp( TABLE_USER_SECURITY_COL_LAST_LOGIN_TIME ) );
			result.setLastLoginIp( rs.getString( TABLE_USER_SECURITY_COL_IP ) );
			result.setAuthorizationKey( rs.getString( TABLE_USER_SECURITY_COL_AUTHORIZATION_KEY ) );
			result.setLastActivityTime( rs.getTimestamp( TABLE_USER_SECURITY_COL_LAST_USER_ACTIVITY_TIME ) );

			return result;
		}
	}
}
