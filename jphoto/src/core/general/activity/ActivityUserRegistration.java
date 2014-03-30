package core.general.activity;

import core.general.user.User;
import core.services.security.Services;
import core.services.translator.message.TranslatableMessage;
import org.dom4j.Document;

public class ActivityUserRegistration extends AbstractActivityStreamEntry {

	public ActivityUserRegistration( final User user, final Services services ) {
		super( user, user.getRegistrationTime(), ActivityType.USER_REGISTRATION, services );
	}

	@Override
	public Document getActivityXML() {
		return getEmptyDocument();
	}

	@Override
	protected TranslatableMessage getActivityTranslatableText() {
		return new TranslatableMessage( "$1", services ).addStringTranslatableParameter( "registered" );
	}

	@Override
	public String toString() {
		return String.format( "%s: %s", getActivityType(), activityOfUser );
	}
}
