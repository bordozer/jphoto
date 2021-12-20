package com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.filesystem;

import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;

import java.util.List;
import java.util.Set;

public class UserGeneratorFactory {

    public AbstractUserGenerator getInstance(final int userId, final Set<Genre> genres, final Services services, final List<User> beingProcessedUsers) {
        if (userId > 0) {
            final User user = services.getUserService().load(userId);

            return new OneUserGenerator(user, services);
        }

        return new RandomUserGenerator(genres, beingProcessedUsers, services);
    }
}
