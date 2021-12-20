package com.bordozer.jphoto.ui.services.breadcrumbs.items;

import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;

public class PhotosByUserAndGenreBreadcrumb extends AbstractBreadcrumb {

    private User user;
    private Genre genre;

    public PhotosByUserAndGenreBreadcrumb(final User user, final Genre genre, final Services services) {
        super(services);
        this.user = user;
        this.genre = genre;
    }

    @Override
    public String getValue(final Language language) {
        return getEntityLinkUtilsService().getPhotosByUserByGenreLink(user, genre, language);
    }
}
