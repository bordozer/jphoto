package core.general.activity;

import core.general.user.User;
import core.services.security.Services;
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
	public String getDisplayActivityDescription() {
		return services.getTranslatorService().translate( "registered" );
	}

	@Override
	public String toString() {
		return String.format( "%s: %s", getActivityType(), activityOfUser );
	}
}
