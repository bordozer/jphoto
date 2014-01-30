package core.services.dao;

import core.enums.PrivateMessageType;
import core.general.message.PrivateMessage;

import java.util.List;

public interface PrivateMessageDao extends BaseEntityDao<PrivateMessage> {

	List<PrivateMessage> loadReceivedPrivateMessages( final int toUserId, final PrivateMessageType privateMessageType );

	List<PrivateMessage> loadSentPrivateMessages( final int fromUserId );

	int getNewReceivedPrivateMessagesCount( final int userId, final PrivateMessageType privateMessageType );

	int getReceivedPrivateMessagesCount( final int userId, final PrivateMessageType privateMessageType );

	int getSentPrivateMessagesCount( final int userId );

	int markPrivateMessageAsRead( final int privateMessageId );
}
