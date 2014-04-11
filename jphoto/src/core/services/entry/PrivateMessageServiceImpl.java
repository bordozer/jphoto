package core.services.entry;

import core.enums.PrivateMessageType;
import core.general.message.PrivateMessage;
import core.general.user.User;
import core.log.LogHelper;
import core.services.dao.PrivateMessageDao;
import core.services.security.SecurityService;
import core.services.utils.DateUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import sql.SqlSelectIdsResult;
import sql.builder.SqlIdsSelectQuery;

import java.util.List;

public class PrivateMessageServiceImpl implements PrivateMessageService {

	@Autowired
	private PrivateMessageDao privateMessageDao;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private SecurityService securityService;

	private final LogHelper log = new LogHelper( PrivateMessageServiceImpl.class );

	@Override
	public boolean save( final PrivateMessage entry ) {
		return privateMessageDao.saveToDB( entry );
	}

	@Override
	public PrivateMessage load( final int messageId ) {
		return privateMessageDao.load( messageId );
	}

	@Override
	public boolean delete( final int entryId ) {
		return privateMessageDao.delete( entryId );
	}

	@Override
	public List<PrivateMessage> loadReceivedPrivateMessages( final int toUserId, final PrivateMessageType privateMessageType ) {
		return privateMessageDao.loadReceivedPrivateMessages( toUserId, privateMessageType );
	}

	@Override
	public List<PrivateMessage> loadSentPrivateMessages( final int fromUserId ) {
		return privateMessageDao.loadSentPrivateMessages( fromUserId );
	}

	@Override
	public int getNewReceivedPrivateMessagesCount( final int userId, final PrivateMessageType privateMessageType ) {
		return privateMessageDao.getNewReceivedPrivateMessagesCount( userId, privateMessageType );
	}

	@Override
	public int getReceivedPrivateMessagesCount( final int userId, final PrivateMessageType privateMessageType ) {
		return privateMessageDao.getReceivedPrivateMessagesCount( userId, privateMessageType );
	}

	@Override
	public int getSentPrivateMessagesCount( final int userId ) {
		return privateMessageDao.getSentPrivateMessagesCount( userId );
	}

	@Override
	public void markPrivateMessageAsRead( final PrivateMessage privateMessage ) {
		privateMessageDao.markPrivateMessageAsRead( privateMessage.getId() );
		privateMessageDao.markPrivateMessageAsRead( privateMessage.getOutPrivateMessageId() );
	}

	@Override
	public void markPrivateMessagesAsRead( final List<PrivateMessage> messages ) {
		privateMessageDao.markPrivateMessagesAsRead( messages );
	}

	@Override
	public boolean sendPrivateMessage( final User fromUser, final User toUser, final PrivateMessageType messageType, final String privateMessageText ) {

		final PrivateMessage privateMessageOut = new PrivateMessage();
		privateMessageOut.setFromUser( fromUser );
		privateMessageOut.setToUser( toUser );
		privateMessageOut.setMessageText( privateMessageText );
		privateMessageOut.setPrivateMessageType( messageType );
		privateMessageOut.setCreationTime( dateUtilsService.getCurrentTime() );

		final boolean isSuccessfulOut = privateMessageDao.saveToDB( privateMessageOut );

		log.debug( String.format( "%s has sent to %s the text '%s'", fromUser, toUser, privateMessageText ) );

		return isSuccessfulOut;
	}

	@Override
	public boolean sendSystemNotificationMessage( final User toUser, final String privateMessageText ) {
		return sendPrivateMessage( null, toUser, PrivateMessageType.SYSTEM_NOTIFICATIONS, privateMessageText );
	}

	@Override
	public boolean sendActivityNotificationMessage( final User toUser, final String privateMessageText ) {
		return sendPrivateMessage( null, toUser, PrivateMessageType.ACTIVITY_NOTIFICATIONS, privateMessageText );
	}

	@Override
	public void sendNotificationAboutErrorToAdmins( final String message ) {
		for ( final User adminUser : securityService.getSuperAdminUsers() ) {
			sendAdminNotificationMessage( adminUser, message );
		}
	}

	private boolean sendAdminNotificationMessage( final User toUser, final String privateMessageText ) {
		return sendPrivateMessage( null, toUser, PrivateMessageType.ADMIN_NOTIFICATIONS, privateMessageText );
	}

	@Override
	public int getPrivateMessagesCount() {
		return privateMessageDao.getPrivateMessagesCount();
	}

	@Override
	public void delete( final List<Integer> ids ) {
		privateMessageDao.delete( ids );
	}

	@Override
	public void deleteAll( final int userId, final PrivateMessageType messageType ) {
		privateMessageDao.deleteAll( userId, messageType );
	}

	@Override
	public boolean exists( final int entryId ) {
		return privateMessageDao.exists( entryId );
	}

	@Override
	public boolean exists( final PrivateMessage entry ) {
		return privateMessageDao.exists( entry );
	}

	@Override
	public SqlSelectIdsResult load( final SqlIdsSelectQuery selectIdsQuery ) {
		return privateMessageDao.load( selectIdsQuery );
	}
}
