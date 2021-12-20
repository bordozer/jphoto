package com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.filesystem;

import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.LocalCategory;
import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.user.UserMembershipType;

import java.util.List;

public abstract class AbstractPhotographStrategy {

    public abstract List<LocalCategory> getSupportedGenres();

    public abstract List<UserMembershipType> getSupportedMembershipType();

    public final boolean doesThisGenreFitsTheUser(final Genre genre, final UserMembershipType membershipType) {
        return getSupportedMembershipType().contains(membershipType) && doesSupportGenre(genre);
    }

    public boolean doesSupportGenre(final Genre genre) {
        for (final LocalCategory localCategory : getSupportedGenres()) {
            if (localCategory.getName().equalsIgnoreCase(genre.getName())) {
                return true;
            }
        }
        return false;
    }
}
