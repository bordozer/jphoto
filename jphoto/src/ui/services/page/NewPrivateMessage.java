package ui.services.page;

import core.enums.PrivateMessageType;

public class NewPrivateMessage {
	private final PrivateMessageType privateMessageType;
	private final int newMessagesCount;
	private String link;
	private String hint;

	public NewPrivateMessage( final PrivateMessageType privateMessageType, final int newMessagesCount ) {
		this.privateMessageType = privateMessageType;
		this.newMessagesCount = newMessagesCount;
	}

	public PrivateMessageType getPrivateMessageType() {
		return privateMessageType;
	}

	public int getNewMessagesCount() {
		return newMessagesCount;
	}

	public String getLink() {
		return link;
	}

	public void setLink( final String link ) {
		this.link = link;
	}

	public String getHint() {
		return hint;
	}

	public void setHint( final String hint ) {
		this.hint = hint;
	}
}
