package core.general.activity;

import core.general.user.User;
import core.services.security.Services;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import utils.NumberUtils;
import utils.TranslatorUtils;

public class ActivityUserRegistration extends AbstractActivityStreamEntry {

	private User registeredUser;

	public ActivityUserRegistration( final String activityXML, final Services services ) throws DocumentException  {
		super( ActivityType.USER_REGISTRATION, services );

		final Document document = DocumentHelper.parseText( activityXML );

		final Element rootElement = document.getRootElement();
		activityOfUserId = NumberUtils.convertToInt( rootElement.element( ACTIVITY_XML_TAG_ID ).getText() );
		registeredUser = services.getUserService().load( activityOfUserId );
	}

	public ActivityUserRegistration( final User registeredUser, final Services services ) {
		super( ActivityType.USER_REGISTRATION, services );

		this.registeredUser = registeredUser;
		this.activityTime = registeredUser.getRegistrationTime();

		activityOfUserId = registeredUser.getId();
	}

	@Override
	public String buildActivityXML() {
		return getXML( ACTIVITY_XML_TAG_ID, registeredUser.getId() );
	}

	@Override
	public String getActivityDescription() {
		return services.getTranslatorService().translate( "registered" );
	}

	@Override
	public String toString() {
		return String.format( "%s: %s", getActivityType(), registeredUser );
	}
}
