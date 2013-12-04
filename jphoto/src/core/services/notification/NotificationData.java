package core.services.notification;

public class NotificationData {

	private final String subject;
	private final String message;

	public NotificationData( final String subject, final String message ) {
		this.subject = subject;
		this.message = message;
	}

	public String getSubject() {
		return subject;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return String.format( "%s: %s", subject, message );
	}
}
