package core.services.entry;

import core.dtos.AjaxResultDTO;
import core.dtos.PrivateMessageSendingDTO;
import core.enums.PrivateMessageType;
import core.general.message.PrivateMessage;
import core.general.user.User;
import core.interfaces.BaseEntityService;
import core.interfaces.IdsSqlSelectable;

import java.util.List;

public interface PrivateMessageService extends BaseEntityService<PrivateMessage>, IdsSqlSelectable {

	String BEAN_NAME = "privateMessageService";

	List<PrivateMessage> loadReceivedPrivateMessages( final int toUserId, final PrivateMessageType privateMessageType );

	List<PrivateMessage> loadSentPrivateMessages( final int fromUserId );

	int getNewReceivedPrivateMessagesCount( final int userId, final PrivateMessageType privateMessageType );

	int getReceivedPrivateMessagesCount( final int userId, final PrivateMessageType privateMessageType );

	int getSentPrivateMessagesCount( final int userId );

	int markPrivateMessageAsRead( final int privateMessageId );

	AjaxResultDTO sendPrivateMessageAjax( final PrivateMessageSendingDTO messageDTO );

	boolean send( final User fromUser, final User toUser, final PrivateMessageType messageType, final String privateMessageText );

	boolean sendSystemNotificationMessage( final User toUser, final String privateMessageText );

	boolean sendActivityNotificationMessage( final User toUser, final String privateMessageText );

	boolean sendAdminNotificationMessage( final User toUser, final String privateMessageText );
}
