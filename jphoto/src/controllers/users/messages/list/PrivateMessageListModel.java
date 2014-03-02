package controllers.users.messages.list;

import core.enums.PrivateMessageType;
import core.general.base.AbstractGeneralModel;
import core.general.message.PrivateMessage;
import core.general.user.User;

import java.util.List;
import java.util.Map;

public class PrivateMessageListModel extends AbstractGeneralModel {

	private User forUser;
	private List<PrivateMessage> privateMessages;
	private PrivateMessageType privateMessageType;
	private Map<PrivateMessageType, MessageTypeData> messagesByType;

	private List<UsersWhoCommunicatedWithUser> usersWhoCommunicatedWithUser;

	private int messagingWithUserId;

	private List<String> selectedMessagesIds;

	private boolean showPaging;

	public User getForUser() {
		return forUser;
	}

	public void setForUser( final User forUser ) {
		this.forUser = forUser;
	}

	public void setPrivateMessages( final List<PrivateMessage> privateMessages ) {
		this.privateMessages = privateMessages;
	}

	public List<PrivateMessage> getPrivateMessages() {
		return privateMessages;
	}

	public PrivateMessageType getPrivateMessageType() {
		return privateMessageType;
	}

	public void setPrivateMessageType( final PrivateMessageType privateMessageType ) {
		this.privateMessageType = privateMessageType;
	}

	public List<UsersWhoCommunicatedWithUser> getUsersWhoCommunicatedWithUser() {
		return usersWhoCommunicatedWithUser;
	}

	public void setUsersWhoCommunicatedWithUser( final List<UsersWhoCommunicatedWithUser> usersWhoCommunicatedWithUser ) {
		this.usersWhoCommunicatedWithUser = usersWhoCommunicatedWithUser;
	}

	public int getMessagingWithUserId() {
		return messagingWithUserId;
	}

	public void setMessagingWithUserId( final int messagingWithUserId ) {
		this.messagingWithUserId = messagingWithUserId;
	}

	public List<String> getSelectedMessagesIds() {
		return selectedMessagesIds;
	}

	public void setSelectedMessagesIds( final List<String> selectedMessagesIds ) {
		this.selectedMessagesIds = selectedMessagesIds;
	}

	public Map<PrivateMessageType, MessageTypeData> getMessagesByType() {
		return messagesByType;
	}

	public void setMessagesByType( final Map<PrivateMessageType, MessageTypeData> messagesByType ) {
		this.messagesByType = messagesByType;
	}

	public boolean isShowPaging() {
		return showPaging;
	}

	public void setShowPaging( final boolean showPaging ) {
		this.showPaging = showPaging;
	}
}
