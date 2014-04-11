package ui.dtos;

public class PrivateMessageSendingDTO {

	private int fromUserId;
	private int toUserId;

	private String privateMessageText;

	public int getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId( final int fromUserId ) {
		this.fromUserId = fromUserId;
	}

	public int getToUserId() {
		return toUserId;
	}

	public void setToUserId( final int toUserId ) {
		this.toUserId = toUserId;
	}

	public String getPrivateMessageText() {
		return privateMessageText;
	}

	public void setPrivateMessageText( final String privateMessageText ) {
		this.privateMessageText = privateMessageText;
	}
}
