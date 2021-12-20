package com.bordozer.jphoto.core.services.translator.message;

import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;

public class UserCardLinkParameter extends AbstractTranslatableMessageParameter {

    private User user;

    protected UserCardLinkParameter(final User user, final Services services) {
        super(services);
        this.user = user;
    }

    @Override
    public String getValue(final Language language) {
        return getEntityLinkUtilsService().getUserCardLink(user, language);
    }
}
