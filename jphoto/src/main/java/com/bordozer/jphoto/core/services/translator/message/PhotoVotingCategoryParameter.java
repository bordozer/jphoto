package com.bordozer.jphoto.core.services.translator.message;

import com.bordozer.jphoto.core.general.photo.PhotoVotingCategory;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;

public class PhotoVotingCategoryParameter extends AbstractTranslatableMessageParameter {

    private PhotoVotingCategory photoVotingCategory;

    protected PhotoVotingCategoryParameter(final PhotoVotingCategory photoVotingCategory, final Services services) {
        super(services);

        this.photoVotingCategory = photoVotingCategory;
    }

    @Override
    public String getValue(final Language language) {
        return getTranslatorService().translatePhotoVotingCategory(photoVotingCategory, language);
    }
}
