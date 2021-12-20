package com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.filesystem;

import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.user.User;

public abstract class AbstractUserGenerator {

    public abstract User getUser(final Genre genre);

}
