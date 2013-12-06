package core.services.mail;

import com.sun.mail.smtp.SMTPTransport;
import core.general.configuration.ConfigurationKey;
import core.log.LogHelper;
import core.services.system.ConfigurationService;
import core.services.utils.DateUtilsService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.Security;
import java.util.Properties;

public class MailServiceImpl implements MailService {

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private ConfigurationService configurationService;

	protected final LogHelper log = new LogHelper( MailServiceImpl.class );

	@Override
	public void send( final MailBean mailBean ) throws MessagingException {

		if ( ! configurationService.getBoolean( ConfigurationKey.EMAILING_ENABLED ) ) {
			return;
		}

		Security.addProvider( new com.sun.net.ssl.internal.ssl.Provider() );

		final String transportProtocol = configurationService.getString( ConfigurationKey.EMAILING_TRANSPORT_PROTOCOL );

		final Properties props = System.getProperties();
		props.setProperty( "mail.transportProtocol.host", configurationService.getString( ConfigurationKey.EMAILING_SMTP_SERVER ) );
		props.setProperty( "mail.transportProtocol.port", configurationService.getString( ConfigurationKey.EMAILING_SMTP_SERVER_PORT ) );

		props.setProperty( "mail.transport.protocol", transportProtocol );
		props.setProperty( "mail.transportProtocol.connectiontimeout", configurationService.getString( ConfigurationKey.EMAILING_SMTP_SERVER_TIMEOUT ) );
		props.put( "mail.transportProtocol.quitwait", "false" );

		props.setProperty( "mail.transportProtocol.auth", "true" );
		props.setProperty( "mail.transportProtocol.user", configurationService.getString( ConfigurationKey.EMAILING_SMTP_SERVER_USER ) );
		props.setProperty( "mail.transportProtocol.password", configurationService.getString( ConfigurationKey.EMAILING_SMTP_SERVER_PASSWORD ) );

		props.setProperty( "mail.debug", String.valueOf( configurationService.getBoolean( ConfigurationKey.EMAILING_SMTP_DEBUG_MODE ) ) );

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

		final SMTPTransport transport = ( SMTPTransport ) session.getTransport( transportProtocol );

		transport.connect( configurationService.getString( ConfigurationKey.EMAILING_SMTP_SERVER )
			, configurationService.getInt( ConfigurationKey.EMAILING_SMTP_SERVER_PORT )
			, configurationService.getString( ConfigurationKey.EMAILING_SMTP_SERVER_USER )
			, configurationService.getString( ConfigurationKey.EMAILING_SMTP_SERVER_PASSWORD )
		);
		transport.sendMessage( msg, msg.getAllRecipients() );
		transport.close();

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
