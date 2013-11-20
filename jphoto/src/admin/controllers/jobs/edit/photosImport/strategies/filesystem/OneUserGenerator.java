package admin.controllers.jobs.edit.photosImport.strategies.filesystem;

import core.general.genre.Genre;
import core.general.user.User;
import core.services.security.Services;

public class OneUserGenerator extends AbstractUserGenerator {

	private final User user;

	public OneUserGenerator( final User user, final Services services ) {
		this.user = user;
	}

	@Override
	public User getUser( final Genre genre ) {
		return user;
	}
}
