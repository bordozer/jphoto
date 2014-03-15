package core.services.system;

import core.general.user.User;
import elements.menus.MenuItem;

import java.util.List;
import java.util.Map;

public interface MenuService {

	Map<MenuItem, List<MenuItem>> getMenuElements( final User user );
}
