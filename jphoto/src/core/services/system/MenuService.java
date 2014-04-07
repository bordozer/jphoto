package core.services.system;

import core.enums.PrivateMessageType;
import core.general.user.User;
import elements.menus.MenuItem;

import java.util.List;
import java.util.Map;

public interface MenuService {

	String MAIN_MENU_UPLOAD_PHOTO = "Main menu: Upload photo";
	String MAIN_MENU_MEMBERS = "Main menu: Members";
	String MAIN_MENU_REGISTER = "Main menu: Register";
	String MAIN_MENU_PROFILE_SETTINGS = "Main menu: Profile settings";
	String MAIN_MENU_MY_AVATAR = "Main menu: My Avatar";
	String MAIN_MENU_PRIVATE_MESSAGES_RECEIVED = PrivateMessageType.USER_PRIVATE_MESSAGE_IN.getName();
	String MAIN_MENU_PRIVATE_MESSAGES_SENT = PrivateMessageType.USER_PRIVATE_MESSAGE_OUT.getName();
	String MAIN_MENU_ABSOLUTE_BEST = "Main menu: Absolute best";

	Map<MenuItem, List<MenuItem>> getMenuElements( final User user );
}
