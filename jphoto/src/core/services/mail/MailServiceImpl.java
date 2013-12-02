package core.services.mail;

import com.sun.mail.smtp.SMTPTransport;
import core.services.utils.DateUtilsService;
import core.services.utils.SystemVarsService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.security.Security;
import java.util.Properties;

public class MailServiceImpl implements MailService {

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private SystemVarsService systemVarsService;

	@Override
	public void send( final MailBean mailBean ) throws MessagingException {

		Security.addProvider( new com.sun.net.ssl.internal.ssl.Provider() );

		final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

		final Properties props = System.getProperties();
		props.setProperty( "mail.smtps.host", "smtp.gmail.com" );
		props.setProperty( "mail.smtp.socketFactory.class", SSL_FACTORY );
		props.setProperty( "mail.smtp.socketFactory.fallback", "false" );
		props.setProperty( "mail.smtp.port", "465" );
		props.setProperty( "mail.smtp.socketFactory.port", "465" );
		props.setProperty( "mail.smtps.auth", "true" );

		props.put( "mail.smtps.quitwait", "false" );

		final Session session = Session.getInstance( props, null );

		final MimeMessage msg = new MimeMessage( session );

		msg.setFrom( mailBean.getFromAddress() );
		msg.setRecipients( Message.RecipientType.TO, mailBean.getToAddresses() );

		if ( mailBean.getCcAddresses().length > 0 ) {
			msg.setRecipients( Message.RecipientType.CC, mailBean.getCcAddresses() );
		}

		msg.setSubject( mailBean.getSubject() );
		msg.setText( mailBean.getBody(), "utf-8" );
		msg.setSentDate( dateUtilsService.getCurrentTime() );

		final SMTPTransport transport = ( SMTPTransport ) session.getTransport( "smtps" );

		transport.connect( systemVarsService.getMailServer(), systemVarsService.getMailUser(), systemVarsService.getMailPassword() );
		transport.sendMessage( msg, msg.getAllRecipients() );
		transport.close();
	}
}
