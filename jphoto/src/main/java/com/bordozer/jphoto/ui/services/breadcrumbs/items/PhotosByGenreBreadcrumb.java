package com.bordozer.jphoto.ui.services.breadcrumbs.items;

import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;

public class PhotosByGenreBreadcrumb extends AbstractBreadcrumb {

    private Genre genre;

    public PhotosByGenreBreadcrumb(final Genre genre, final Services services) {
        super(services);
        this.genre = genre;
    }

    @Override
    public String getValue(final Language language) {
        return getEntityLinkUtilsService().getPhotosByGenreLink(genre, language);
    }
}
