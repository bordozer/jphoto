package core.services.mail;

import javax.mail.internet.InternetAddress;
import java.util.List;

public class MailBean {

	private InternetAddress fromAddress;
	private InternetAddress[] toAddresses;
	private InternetAddress[] ccAddresses;
	private InternetAddress[] bccAddresses;

	private String subject;
	private String body;

	public InternetAddress getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress( final InternetAddress fromAddress ) {
		this.fromAddress = fromAddress;
	}

	public InternetAddress[] getToAddresses() {
		return toAddresses;
	}

	public void setToAddresses( final InternetAddress[] toAddresses ) {
		this.toAddresses = toAddresses;
	}

	public InternetAddress[] getCcAddresses() {
		return ccAddresses;
	}

	public void setCcAddresses( final InternetAddress[] ccAddresses ) {
		this.ccAddresses = ccAddresses;
	}

	public InternetAddress[] getBccAddresses() {
		return bccAddresses;
	}

	public void setBccAddresses( final InternetAddress[] bccAddresses ) {
		this.bccAddresses = bccAddresses;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject( final String subject ) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody( final String body ) {
		this.body = body;
	}
}
