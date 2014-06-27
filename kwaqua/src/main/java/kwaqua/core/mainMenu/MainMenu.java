package kwaqua.core.mainMenu;

import org.openqa.selenium.WebDriver;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class MainMenu {

	private final WebDriver webDriver;

	public MainMenu( final WebDriver webDriver ) {
		this.webDriver = webDriver;
	}

	private MainMenuItem getMenu( final MainMenuTitle menuTitle ) {

		final List<MainMenuItem> menuItems = getMainMenu();

		for ( final MainMenuItem mainMenuItem : menuItems ) {
			if ( mainMenuItem.getTitle() == menuTitle ) {
				return mainMenuItem;
			}
		}

		return null;
	}

	private List<MainMenuItem> getMainMenu() {

		final MainMenuFactory menuFactory = new MainMenuFactory( webDriver );

		final List<MainMenuItem> menuItems = newArrayList();

		menuItems.add( menuFactory.getPhotosMenu() );
		menuItems.add( menuFactory.getMembersMenu() );
		// TODO: add remain menus

		return menuItems;
	}

	private AbstractMainMenuItem getMenu( final MainMenuTitle menuTitle, final MainMenuTitle subMenuTitle ) {
		final MainMenuItem menu = getMenu( menuTitle );

		if ( menu == null ) {
			return null;
		}

		final List<MainSubMenuItem> subMenuItems = menu.getSubMenuItems();
		for ( final MainSubMenuItem subMenuItem : subMenuItems ) {
			if ( subMenuItem.getTitle() == subMenuTitle ) {
				return subMenuItem;
			}
		}

		return null;
	}

	public void click( final MainMenuTitle menuTitle ) {
		final AbstractMainMenuItem menu = getMenu( menuTitle );

		if ( menu == null  ) {
			throw new IllegalArgumentException( String.format( "Main menu item not found: %s", menuTitle ) );
		}

		menu.click();
	}

	public void click( final MainMenuTitle menuTitle, final MainMenuTitle subMenuTitle  ) {

		final AbstractMainMenuItem subMenu = getMenu( menuTitle, subMenuTitle );

		if ( subMenu == null  ) {
			throw new IllegalArgumentException( String.format( "Sub menu item not found: '%s'>'%s'", menuTitle, subMenuTitle ) );
		}

		subMenu.click();
	}
}
