package core.services.translator.message;

import core.general.user.User;
import core.services.security.Services;
import core.services.translator.Language;

public class LinkToUserCardUnit extends AbstractTranslatableMessageUnit {

	private User user;

	protected LinkToUserCardUnit( final User user, final Services services ) {
		super( services );
		this.user = user;
	}

	@Override
	public String translate( final Language language ) {
		return getEntityLinkUtilsService().getUserCardLink( user, language );
	}
}
