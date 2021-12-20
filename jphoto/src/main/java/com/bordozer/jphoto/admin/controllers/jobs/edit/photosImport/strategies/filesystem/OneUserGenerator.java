package com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.filesystem;

import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;

public class OneUserGenerator extends AbstractUserGenerator {

    private final User user;

    public OneUserGenerator(final User user, final Services services) {
        this.user = user;
    }

    @Override
    public User getUser(final Genre genre) {
        return user;
    }
}
