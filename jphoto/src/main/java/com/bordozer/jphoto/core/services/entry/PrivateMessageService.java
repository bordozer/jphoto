package com.bordozer.jphoto.core.services.entry;

import com.bordozer.jphoto.core.enums.PrivateMessageType;
import com.bordozer.jphoto.core.general.message.PrivateMessage;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.interfaces.BaseEntityService;
import com.bordozer.jphoto.core.interfaces.IdsSqlSelectable;

import java.util.List;

public interface PrivateMessageService extends BaseEntityService<PrivateMessage>, IdsSqlSelectable {

    String BEAN_NAME = "privateMessageService";

    List<PrivateMessage> loadReceivedPrivateMessages(final int toUserId, final PrivateMessageType privateMessageType);

    List<PrivateMessage> loadSentPrivateMessages(final int fromUserId);

    int getNewReceivedPrivateMessagesCount(final int userId, final PrivateMessageType privateMessageType);

    int getReceivedPrivateMessagesCount(final int userId, final PrivateMessageType privateMessageType);

    int getSentPrivateMessagesCount(final int userId);

    void markPrivateMessageAsRead(final PrivateMessage privateMessage);

    void markPrivateMessagesAsRead(final List<PrivateMessage> messages);

    // Transactional
    boolean sendPrivateMessage(final User fromUser, final User toUser, final PrivateMessageType messageType, final String privateMessageText);

    // Transactional
    boolean sendSystemNotificationMessage(final User toUser, final String privateMessageText);

    // Transactional
    boolean sendActivityNotificationMessage(final User toUser, final String privateMessageText);

    // Transactional
    void sendNotificationAboutErrorToAdmins(final String message);

    int getPrivateMessagesCount();

    void delete(final List<Integer> ids);

    void deleteAll(final int userId, final PrivateMessageType messageType);
}
