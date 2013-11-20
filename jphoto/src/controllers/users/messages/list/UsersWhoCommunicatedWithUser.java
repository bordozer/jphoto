package controllers.users.messages.list;

import core.general.user.User;

public class UsersWhoCommunicatedWithUser {

	private final User withUser;

	private int receivedMessagesCount;
	private int sentMessagesCount;

	public UsersWhoCommunicatedWithUser( final User withUser ) {
		this.withUser = withUser;
	}

	public User getWithUser() {
		return withUser;
	}

	public int getReceivedMessagesCount() {
		return receivedMessagesCount;
	}

	public void setReceivedMessagesCount( final int receivedMessagesCount ) {
		this.receivedMessagesCount = receivedMessagesCount;
	}

	public int getSentMessagesCount() {
		return sentMessagesCount;
	}

	public void setSentMessagesCount( final int sentMessagesCount ) {
		this.sentMessagesCount = sentMessagesCount;
	}

	@Override
	public String toString() {
		return String.format( "%s ( %d / %d )", withUser, receivedMessagesCount, sentMessagesCount );
	}

	@Override
	public boolean equals( final Object obj ) {
		if ( obj == null ) {
			return false;
		}

		if ( obj == this ) {
			return true;
		}

		if ( !( obj instanceof UsersWhoCommunicatedWithUser ) ) {
			return false;
		}

		final UsersWhoCommunicatedWithUser communicator = ( UsersWhoCommunicatedWithUser ) obj;
		return communicator.getWithUser() == withUser;
	}

	@Override
	public int hashCode() {
		return withUser.getId();
	}
}
