package com.bordozer.jphoto.ui.services.breadcrumbs.items;

import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.ui.services.menu.main.MenuService;

public class AdminRootBreadcrumbs extends AbstractBreadcrumb {

    public AdminRootBreadcrumbs(final Services services) {
        super(services);
    }

    @Override
    public String getValue(final Language language) {
        return getTranslatorService().translate(MenuService.MAIN_MENU_ADMIN_ROOT, language);
    }
}
