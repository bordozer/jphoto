package core.services.dao;

import core.enums.PrivateMessageType;
import core.general.message.PrivateMessage;
import core.general.user.User;
import core.services.utils.DateUtilsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newLinkedHashMap;

public class PrivateMessageDaoImpl extends BaseEntityDaoImpl<PrivateMessage> implements PrivateMessageDao {

	public final static String TABLE_PRIVATE_MESSAGE = "privateMessage";

	public final static String TABLE_PRIVATE_MESSAGE_COL_FROM_USER_ID = "fromUserId";
	public final static String TABLE_PRIVATE_MESSAGE_COL_TO_USER_ID = "toUserId";
	public final static String TABLE_PRIVATE_MESSAGE_COL_MESSAGE_TYPE_ID = "messageTypeId";
	public final static String TABLE_PRIVATE_MESSAGE_COL_MESSAGE_TEXT = "messageText";
	public final static String TABLE_PRIVATE_MESSAGE_COL_CREATE_TIME = "createTime";
	public final static String TABLE_PRIVATE_MESSAGE_COL_READ_TIME = "readTime";
	public final static String TABLE_PRIVATE_MESSAGE_COL_OUT_MESSAGE_ID = "outPrivateMessageId";

	private static final Map<Integer, String> fields = newLinkedHashMap();
	private static final Map<Integer, String> updatableFields = newLinkedHashMap();

	public static final int SYSTEM_NOTIFIER = -1;

	@Autowired
	private UserDao userDao;

	@Autowired
	private DateUtilsService dateUtilsService;

	static {
		fields.put( 1, TABLE_PRIVATE_MESSAGE_COL_FROM_USER_ID );
		fields.put( 2, TABLE_PRIVATE_MESSAGE_COL_TO_USER_ID );
		fields.put( 3, TABLE_PRIVATE_MESSAGE_COL_MESSAGE_TYPE_ID );
		fields.put( 4, TABLE_PRIVATE_MESSAGE_COL_MESSAGE_TEXT );
		fields.put( 5, TABLE_PRIVATE_MESSAGE_COL_CREATE_TIME );
		fields.put( 6, TABLE_PRIVATE_MESSAGE_COL_READ_TIME );
		fields.put( 7, TABLE_PRIVATE_MESSAGE_COL_OUT_MESSAGE_ID );
	}

	static {
		updatableFields.put( 6, TABLE_PRIVATE_MESSAGE_COL_READ_TIME );
	}

	@Override
	protected String getTableName() {
		return TABLE_PRIVATE_MESSAGE;
	}

	@Override
	public boolean saveToDB( final PrivateMessage privateMessage ) {
		return createOrUpdateEntry( privateMessage, fields, updatableFields );
	}

	@Override
	public List<PrivateMessage> loadReceivedPrivateMessages( final int toUserId, final PrivateMessageType privateMessageType ) {
		return getMessageForUserColumn( toUserId, privateMessageType, TABLE_PRIVATE_MESSAGE_COL_TO_USER_ID );
	}

	@Override
	public List<PrivateMessage> loadSentPrivateMessages( final int fromUserId ) {
		return getMessageForUserColumn( fromUserId, PrivateMessageType.USER_PRIVATE_MESSAGE_OUT, TABLE_PRIVATE_MESSAGE_COL_FROM_USER_ID );
	}

	@Override
	public int getNewReceivedPrivateMessagesCount( final int userId, final PrivateMessageType privateMessageType ) {
		final String sql = String.format( "SELECT COUNT(%s) FROM %s WHERE %s=:userId AND %s=:messageTypeId AND %s IS NULL;"
				, ENTITY_ID, TABLE_PRIVATE_MESSAGE, TABLE_PRIVATE_MESSAGE_COL_TO_USER_ID, TABLE_PRIVATE_MESSAGE_COL_MESSAGE_TYPE_ID, TABLE_PRIVATE_MESSAGE_COL_READ_TIME );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "userId", userId );
		paramSource.addValue( "messageTypeId", privateMessageType.getId() );

		return getIntValueOrZero( sql, paramSource );
	}

	@Override
	public int getReceivedPrivateMessagesCount( final int userId, final PrivateMessageType privateMessageType ) {
		final String sql = String.format( "SELECT COUNT(%s) FROM %s WHERE %s=:userId AND %s=:messageTypeId;"
				, ENTITY_ID, TABLE_PRIVATE_MESSAGE, TABLE_PRIVATE_MESSAGE_COL_TO_USER_ID, TABLE_PRIVATE_MESSAGE_COL_MESSAGE_TYPE_ID );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "userId", userId );
		paramSource.addValue( "messageTypeId", privateMessageType.getId() );

		return getIntValueOrZero( sql, paramSource );
	}

	@Override
	public int getSentPrivateMessagesCount( final int userId ) {
		final String sql = String.format( "SELECT COUNT(%s) FROM %s WHERE %s=:userId AND %s=:messageTypeId;"
				, ENTITY_ID, TABLE_PRIVATE_MESSAGE, TABLE_PRIVATE_MESSAGE_COL_FROM_USER_ID, TABLE_PRIVATE_MESSAGE_COL_MESSAGE_TYPE_ID );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "userId", userId );
		paramSource.addValue( "messageTypeId", PrivateMessageType.USER_PRIVATE_MESSAGE_OUT.getId() );

		return getIntValueOrZero( sql, paramSource );
	}

	@Override
	public int markPrivateMessageAsRead( final int privateMessageId ) {
		final String sql = String.format( "UPDATE %s SET %s=:currentTime WHERE %s=:id;", TABLE_PRIVATE_MESSAGE, TABLE_PRIVATE_MESSAGE_COL_READ_TIME, ENTITY_ID );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "id", privateMessageId );
		paramSource.addValue( "currentTime", dateUtilsService.getCurrentTime() );

		return jdbcTemplate.update( sql, paramSource );
	}

	@Override
	public boolean markPrivateMessagesAsRead( final List<PrivateMessage> messages ) {
		final StringBuilder builder = new StringBuilder(  );
		for ( final PrivateMessage message : messages ) {
			if ( ! message.isRead() ) {
				builder.append( message.getId() ).append( ", " );
			}
		}

		if ( builder.length() == 0 ) {
			return true;
		}

		final String ids = builder.subSequence( 0, builder.length() - 2 ).toString();

		final String sql = String.format( "UPDATE %s SET %s=:currentTime WHERE %s IN ( %s );", TABLE_PRIVATE_MESSAGE, TABLE_PRIVATE_MESSAGE_COL_READ_TIME, ENTITY_ID, ids );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "currentTime", dateUtilsService.getCurrentTime() );

		return jdbcTemplate.update( sql, paramSource ) > 0;
	}

	@Override
	public int getPrivateMessagesCount() {
		return 0; // TODO: implement
	}

	@Override
	public void delete( final List<Integer> ids ) {

		if ( ids.size() == 0 ) {
			return;
		}

		final String sql = String.format( "DELETE FROM %s WHERE %s IN ( %s );", TABLE_PRIVATE_MESSAGE, ENTITY_ID, StringUtils.join( ids, "," ) );

		jdbcTemplate.update( sql, new MapSqlParameterSource() );
	}

	@Override
	public void deleteAll( final int userId, final PrivateMessageType messageType ) {
		String colUser = TABLE_PRIVATE_MESSAGE_COL_TO_USER_ID;
		if ( messageType == PrivateMessageType.USER_PRIVATE_MESSAGE_OUT ) {
			colUser = TABLE_PRIVATE_MESSAGE_COL_FROM_USER_ID;
		}

		final String sql = String.format( "DELETE FROM %s WHERE %s=:userId AND %s=:messageTypeId;", TABLE_PRIVATE_MESSAGE, colUser, TABLE_PRIVATE_MESSAGE_COL_MESSAGE_TYPE_ID );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "userId", userId );
		paramSource.addValue( "messageTypeId", messageType.getId() );

		jdbcTemplate.update( sql, paramSource );
	}

	private List<PrivateMessage> getMessageForUserColumn( final int toUserId, final PrivateMessageType privateMessageType, final String userColumn ) {
		final String sql = String.format( "SELECT * FROM %s WHERE %s=:toUserId AND %s=:messageTypeId ORDER BY %s DESC;"
			, TABLE_PRIVATE_MESSAGE, userColumn, TABLE_PRIVATE_MESSAGE_COL_MESSAGE_TYPE_ID, TABLE_PRIVATE_MESSAGE_COL_CREATE_TIME );

		final MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue( "toUserId", toUserId );
		paramSource.addValue( "messageTypeId", privateMessageType.getId() );

		return jdbcTemplate.query( sql, paramSource, new PrivateMessageMapper() );
	}

	@Override
	protected MapSqlParameterSource getParameters( final PrivateMessage entry ) {
		final MapSqlParameterSource paramSource = new MapSqlParameterSource();

		final User fromUser = entry.getFromUser();
		if ( fromUser != null ) {
			paramSource.addValue( TABLE_PRIVATE_MESSAGE_COL_FROM_USER_ID, fromUser.getId() );
		} else {
			paramSource.addValue( TABLE_PRIVATE_MESSAGE_COL_FROM_USER_ID, SYSTEM_NOTIFIER );
		}

		paramSource.addValue( TABLE_PRIVATE_MESSAGE_COL_TO_USER_ID, entry.getToUser().getId() );
		paramSource.addValue( TABLE_PRIVATE_MESSAGE_COL_MESSAGE_TYPE_ID, entry.getPrivateMessageType().getId() );
		paramSource.addValue( TABLE_PRIVATE_MESSAGE_COL_MESSAGE_TEXT, entry.getMessageText() );
		paramSource.addValue( TABLE_PRIVATE_MESSAGE_COL_CREATE_TIME, entry.getCreationTime() );
		paramSource.addValue( TABLE_PRIVATE_MESSAGE_COL_READ_TIME, entry.getReadTime() );
		paramSource.addValue( TABLE_PRIVATE_MESSAGE_COL_OUT_MESSAGE_ID, entry.getOutPrivateMessageId() );

		return paramSource;
	}

	@Override
	protected RowMapper<PrivateMessage> getRowMapper() {
		return new PrivateMessageMapper();
	}

	private class PrivateMessageMapper implements RowMapper<PrivateMessage> {

		@Override
		public PrivateMessage mapRow( final ResultSet rs, final int rowNum ) throws SQLException {
			final PrivateMessage result = new PrivateMessage();

			result.setId( rs.getInt( ENTITY_ID ) );

			result.setFromUser( userDao.load( rs.getInt( TABLE_PRIVATE_MESSAGE_COL_FROM_USER_ID ) ) );
			result.setToUser( userDao.load( rs.getInt( TABLE_PRIVATE_MESSAGE_COL_TO_USER_ID ) ) );

			result.setPrivateMessageType( PrivateMessageType.getById( rs.getInt( TABLE_PRIVATE_MESSAGE_COL_MESSAGE_TYPE_ID ) ) );
			result.setMessageText( rs.getString( TABLE_PRIVATE_MESSAGE_COL_MESSAGE_TEXT ) );

			result.setCreationTime( rs.getTimestamp( TABLE_PRIVATE_MESSAGE_COL_CREATE_TIME ) );
			result.setReadTime( rs.getTimestamp( TABLE_PRIVATE_MESSAGE_COL_READ_TIME ) );

			result.setOutPrivateMessageId( rs.getInt( TABLE_PRIVATE_MESSAGE_COL_OUT_MESSAGE_ID ) );

			return result;
		}
	}
}
