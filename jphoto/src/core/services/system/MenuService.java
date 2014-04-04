package core.services.system;

import core.general.user.User;
import elements.menus.MenuItem;

import java.util.List;
import java.util.Map;

public interface MenuService {

	String MAIN_MENU_UPLOAD_PHOTO = "Main menu: Upload photo";
	String MAIN_MENU_MEMBERS = "Main menu: Members";
	String MAIN_MENU_REGISTER = "Main menu: Register";

	Map<MenuItem, List<MenuItem>> getMenuElements( final User user );
}
