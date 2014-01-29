package core.services.dao;

import core.enums.PrivateMessageType;
import core.general.message.PrivateMessage;

import java.util.List;

public interface PrivateMessageDao extends BaseEntityDao<PrivateMessage> {

	List<PrivateMessage> loadMessagesFromUser( final int fromUserId );

	List<PrivateMessage> loadMessagesToUser( final int toUserId, final PrivateMessageType privateMessageType );

	int getNewPrivateMessagesCount( final int userId, final PrivateMessageType privateMessageType );

	int getPrivateMessagesCount( final int userId, final PrivateMessageType privateMessageType );

	int markPrivateMessageAsRead( final int privateMessageId );
}
