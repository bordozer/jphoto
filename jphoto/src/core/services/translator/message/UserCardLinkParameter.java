package core.services.translator.message;

import core.general.user.User;
import core.services.security.Services;
import core.services.translator.Language;

public class UserCardLinkParameter extends AbstractTranslatableMessageParameter {

	private User user;

	protected UserCardLinkParameter( final User user, final Services services ) {
		super( services );
		this.user = user;
	}

	@Override
	public String getValue( final Language language ) {
		return getEntityLinkUtilsService().getUserCardLink( user, language );
	}
}
