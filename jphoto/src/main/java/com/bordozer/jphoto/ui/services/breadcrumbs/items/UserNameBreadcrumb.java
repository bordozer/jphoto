package com.bordozer.jphoto.ui.services.breadcrumbs.items;

import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;

public class UserNameBreadcrumb extends AbstractBreadcrumb {

    private User user;

    public UserNameBreadcrumb(final User user, final Services services) {
        super(services);
        this.user = user;
    }

    public UserNameBreadcrumb(final int userId, final Services services) {
        this(services.getUserService().load(userId), services);
    }

    @Override
    public String getValue(final Language language) {
        return user.getNameEscaped();
    }
}
