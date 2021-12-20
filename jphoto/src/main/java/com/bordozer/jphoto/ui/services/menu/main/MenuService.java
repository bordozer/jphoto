package com.bordozer.jphoto.ui.services.menu.main;

import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.ui.elements.MenuItem;

import java.util.List;
import java.util.Map;

public interface MenuService {

    String MAIN_MENU_UPLOAD_PHOTO = "Main menu: Upload photo";
    String MAIN_MENU_MEMBERS = "Main menu: Members";
    String MAIN_MENU_REGISTER = "Main menu: Register";
    String MAIN_MENU_PROFILE_SETTINGS = "Main menu: Profile settings";
    String MAIN_MENU_MY_AVATAR = "Main menu: My Avatar";
    String MAIN_MENU_LATEST_POPULAR_PHOTOS = "Main menu: Latest popular photos";
    String MAIN_MENU_ABSOLUTE_BEST = "Main menu: Absolute best";
    String MAIN_MENU_MEMBERSHIP_TYPE_NERD = "Main menu: %s: %s";

    String MAIN_MENU_RESTRICTION_LIST = "Breadcrumbs: Restriction list";
    String MAIN_MENU_ADMIN_ROOT = "Main menu: Administration";
    String MAIN_MENU_ADMIN_JOBS = "Main menu: Jobs";

    Map<MenuItem, List<MenuItem>> getMenuElements(final User user);
}
