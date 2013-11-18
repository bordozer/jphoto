package admin.controllers.jobs.edit.photosImport.strategies.filesystem;

import core.general.genre.Genre;
import core.general.user.User;

public abstract class AbstractUserGenerator {

	public abstract User getUser( final Genre genre );

}
