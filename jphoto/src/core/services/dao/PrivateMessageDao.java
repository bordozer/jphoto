package core.services.dao;

import core.enums.PrivateMessageType;
import core.general.message.PrivateMessage;

import java.util.List;

public interface PrivateMessageDao extends BaseEntityDao<PrivateMessage> {

	List<PrivateMessage> loadMessagesFromUser( final int fromUserId );

	List<PrivateMessage> loadMessagesToUser( final int toUserId, final PrivateMessageType privateMessageType );

	int getNewReceivedPrivateMessagesCount( final int userId, final PrivateMessageType privateMessageType );

	int getReceivedPrivateMessagesCount( final int userId, final PrivateMessageType privateMessageType );

	int getSentPrivateMessagesCount( final int userId );

	int markPrivateMessageAsRead( final int privateMessageId );
}
