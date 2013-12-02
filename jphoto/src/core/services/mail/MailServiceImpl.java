package core.services.mail;

import com.sun.mail.smtp.SMTPTransport;
import core.log.LogHelper;
import core.services.utils.DateUtilsService;
import core.services.utils.SystemVarsService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.Security;
import java.util.Properties;

import static com.google.common.collect.Lists.newArrayList;

public class MailServiceImpl implements MailService {

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private SystemVarsService systemVarsService;

	protected final LogHelper log = new LogHelper( MailServiceImpl.class );

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

		msg.setFrom( new InternetAddress( mailBean.getFromAddress(), false ) );
		msg.setRecipients( Message.RecipientType.TO, convertToInternetAddress( mailBean.getToAddresses() ) );

		if ( mailBean.getCcAddresses() != null && mailBean.getCcAddresses().length > 0 ) {
			msg.setRecipients( Message.RecipientType.CC, convertToInternetAddress( mailBean.getCcAddresses() ) );
		}

		if ( mailBean.getBccAddresses() != null && mailBean.getBccAddresses().length > 0 ) {
			msg.setRecipients( Message.RecipientType.BCC, convertToInternetAddress( mailBean.getBccAddresses() ) );
		}

		msg.setSubject( mailBean.getSubject() );
		msg.setText( mailBean.getBody(), "utf-8" );
		msg.setSentDate( dateUtilsService.getCurrentTime() );

		/*
		// TODO: switch on!!!
		final SMTPTransport transport = ( SMTPTransport ) session.getTransport( "smtps" );

		transport.connect( systemVarsService.getMailServer(), systemVarsService.getMailUser(), systemVarsService.getMailPassword() );
		transport.sendMessage( msg, msg.getAllRecipients() );
		transport.close();*/

		log.debug( String.format( "%s has sent to %s the email '%s'", mailBean.getFromAddress(), mailBean.getToAddresses(), mailBean.getBody() ) );
	}

	@Override
	public void sendNoException( final MailBean mailBean ) {
		try {
			send( mailBean );
		} catch ( MessagingException e ) {
			log.error( String.format( "Can not send email: %s", mailBean ), e );
		}
	}

	private InternetAddress[] convertToInternetAddress( final String[] addresses ) throws AddressException {
		final InternetAddress[] result = new InternetAddress[ addresses.length ];

		for ( int i = 0; i < addresses.length; i++ ) {
			result[i] = new InternetAddress( addresses[i], false );
		}

		return result;
	}
}
