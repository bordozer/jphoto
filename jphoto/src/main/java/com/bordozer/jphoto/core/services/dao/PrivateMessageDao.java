package com.bordozer.jphoto.core.services.dao;

import com.bordozer.jphoto.core.enums.PrivateMessageType;
import com.bordozer.jphoto.core.general.message.PrivateMessage;

import java.util.List;

public interface PrivateMessageDao extends BaseEntityDao<PrivateMessage> {

    List<PrivateMessage> loadReceivedPrivateMessages(final int toUserId, final PrivateMessageType privateMessageType);

    List<PrivateMessage> loadSentPrivateMessages(final int fromUserId);

    int getNewReceivedPrivateMessagesCount(final int userId, final PrivateMessageType privateMessageType);

    int getReceivedPrivateMessagesCount(final int userId, final PrivateMessageType privateMessageType);

    int getSentPrivateMessagesCount(final int userId);

    int markPrivateMessageAsRead(final int privateMessageId);

    boolean markPrivateMessagesAsRead(final List<PrivateMessage> messages);

    int getPrivateMessagesCount();

    void delete(final List<Integer> ids);

    void deleteAll(int userId, final PrivateMessageType messageType);
}
