package com.bordozer.jphoto.ui.services.breadcrumbs.items;

import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;

public class UserCardLinkBreadcrumb extends AbstractBreadcrumb {

    private User user;

    public UserCardLinkBreadcrumb(final User user, final Services services) {
        super(services);
        this.user = user;
    }

    public UserCardLinkBreadcrumb(final int userId, final Services services) {
        super(services);
        this.user = services.getUserService().load(userId);
    }

    @Override
    public String getValue(final Language language) {
        return getEntityLinkUtilsService().getUserCardLink(user, language);
    }
}
