package core.services.mail;

import javax.mail.MessagingException;

public interface MailService {

	void send( final MailBean mailBean ) throws MessagingException;
}
