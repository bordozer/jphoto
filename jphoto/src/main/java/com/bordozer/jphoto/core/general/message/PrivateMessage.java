package com.bordozer.jphoto.core.general.message;

import com.bordozer.jphoto.core.enums.PrivateMessageType;
import com.bordozer.jphoto.core.general.base.AbstractBaseEntity;
import com.bordozer.jphoto.core.general.user.User;

import java.util.Date;

public class PrivateMessage extends AbstractBaseEntity {

    private User fromUser;
    private User toUser;
    private PrivateMessageType privateMessageType;
    private String messageText;
    private Date creationTime;
    private Date readTime;
    private int outPrivateMessageId;

    public PrivateMessage() {
    }

    public PrivateMessage(final PrivateMessage privateMessage) {
        fromUser = privateMessage.getFromUser();
        toUser = privateMessage.getToUser();
        privateMessageType = privateMessage.getPrivateMessageType();
        messageText = privateMessage.getMessageText();
        creationTime = privateMessage.getCreationTime();
        readTime = privateMessage.getReadTime();
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(final User fromUser) {
        this.fromUser = fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(final User toUser) {
        this.toUser = toUser;
    }

    public PrivateMessageType getPrivateMessageType() {
        return privateMessageType;
    }

    public void setPrivateMessageType(final PrivateMessageType privateMessageType) {
        this.privateMessageType = privateMessageType;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(final String messageText) {
        this.messageText = messageText;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(final Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(final Date readTime) {
        this.readTime = readTime;
    }

    public boolean isRead() {
        return readTime != null && readTime.getTime() > 0; // TODO: use DateUtilsService.isEmptyTime()
    }

    public int getOutPrivateMessageId() {
        return outPrivateMessageId;
    }

    public void setOutPrivateMessageId(final int outPrivateMessageId) {
        this.outPrivateMessageId = outPrivateMessageId;
    }
}
