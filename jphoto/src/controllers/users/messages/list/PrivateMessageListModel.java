package controllers.users.messages.list;

import core.enums.PrivateMessageType;
import core.general.base.AbstractGeneralModel;
import core.general.user.User;
import core.general.message.PrivateMessage;

import java.util.List;

public class PrivateMessageListModel extends AbstractGeneralModel {

	private User forUser;
	private List<PrivateMessage> privateMessages;
	private PrivateMessageType privateMessageType;

	private List<UsersWhoCommunicatedWithUser> usersWhoCommunicatedWithUser;

	private int messagingWithUserId;

	private List<String> selectedMessagesIds;

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
}
