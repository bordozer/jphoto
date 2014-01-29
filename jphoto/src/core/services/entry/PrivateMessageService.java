package core.services.entry;

import core.dtos.AjaxResultDTO;
import core.dtos.PrivateMessageSendingDTO;
import core.enums.PrivateMessageType;
import core.general.message.PrivateMessage;
import core.general.user.User;
import core.interfaces.BaseEntityService;

import java.util.List;

public interface PrivateMessageService extends BaseEntityService<PrivateMessage> {

	String BEAN_NAME = "privateMessageService";

	List<PrivateMessage> loadMessagesFromUser( final int fromUserId );

	List<PrivateMessage> loadMessagesToUser( final int toUserId, final PrivateMessageType privateMessageType );

	int getNewPrivateMessagesCount( final int userId, final PrivateMessageType privateMessageType );

	int getPrivateMessagesCount( final int userId, final PrivateMessageType privateMessageType );

	int markPrivateMessageAsRead( final int privateMessageId );

	AjaxResultDTO sendPrivateMessageAjax( final PrivateMessageSendingDTO messageDTO );

	boolean send( final User fromUser, final User toUser, final PrivateMessageType messageType, final String privateMessageText );

	boolean sendSystemNotificationMessage( final User toUser, final String privateMessageText );

	boolean sendActivityNotificationMessage( final User toUser, final String privateMessageText );

	boolean sendAdminNotificationMessage( final User toUser, final String privateMessageText );
}
