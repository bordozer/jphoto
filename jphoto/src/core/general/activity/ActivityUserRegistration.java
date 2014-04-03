package core.general.activity;

import core.general.user.User;
import core.services.system.Services;
import core.services.translator.message.TranslatableMessage;
import org.dom4j.Document;

public class ActivityUserRegistration extends AbstractActivityStreamEntry {

	public ActivityUserRegistration( final User user, final Services services ) {
		super( user, user.getRegistrationTime(), ActivityType.USER_REGISTRATION, services );

		initActivityTranslatableText();
	}

	@Override
	public Document getActivityXML() {
		return getEmptyDocument();
	}

	@Override
	protected TranslatableMessage getActivityTranslatableText() {
		return new TranslatableMessage( "user registration activity stream: registered", services );
	}

	@Override
	public String toString() {
		return String.format( "%s: %s", getActivityType(), activityOfUser );
	}
}
