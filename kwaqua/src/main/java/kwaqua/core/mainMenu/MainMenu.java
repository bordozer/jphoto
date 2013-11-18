package kwaqua.core.mainMenu;

import org.openqa.selenium.WebDriver;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class MainMenu {

	private final List<AbstractMainMenuItem> menuItems;

	public MainMenu( final WebDriver webDriver ) {

		final MainMenuFactory menuFactory = new MainMenuFactory( webDriver );

		menuItems = newArrayList();

		menuItems.add( menuFactory.getPhotosMenu() );
		menuItems.add( menuFactory.getMembersMenu() );
	}

	private AbstractMainMenuItem getMenu( final MainMenuTitle menuTitle ) {
		for ( final AbstractMainMenuItem mainMenuItem : menuItems ) {
			if ( mainMenuItem.getTitle() == menuTitle ) {
				return mainMenuItem;
			}
		}

		return null;
	}

	private AbstractMainMenuItem getMenu( final MainMenuTitle menuTitle, final MainMenuTitle subMenuTitle ) {
		final AbstractMainMenuItem menu = getMenu( menuTitle );

		if ( menu == null ) {
			return null;
		}

		final List<AbstractMainMenuItem> subMenuItems = menu.getSubMenuItems();
		for ( final AbstractMainMenuItem subMenuItem : subMenuItems ) {
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
