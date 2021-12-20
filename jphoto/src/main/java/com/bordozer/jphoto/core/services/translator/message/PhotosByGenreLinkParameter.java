package com.bordozer.jphoto.core.services.translator.message;

import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;

public class PhotosByGenreLinkParameter extends AbstractTranslatableMessageParameter {

    private Genre genre;

    protected PhotosByGenreLinkParameter(final Genre genre, final Services services) {
        super(services);
        this.genre = genre;
    }

    @Override
    public String getValue(final Language language) {
        return getEntityLinkUtilsService().getPhotosByGenreLink(genre, language);
    }
}
