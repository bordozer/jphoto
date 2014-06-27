package kwaqua.core.mainMenu;

import org.openqa.selenium.WebDriver;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class MainMenuFactory {

	private final WebDriver webDriver;

	public MainMenuFactory( final WebDriver webDriver ) {
		this.webDriver = webDriver;
	}

	public MainMenuItem getPhotosMenu() {

		final MainMenuTitle menuTitle = MainMenuTitle.PHOTOS;

		final MainMenuItem mainMenuItem = new MainMenuItem( menuTitle, webDriver );

		final List<MainSubMenuItem> submenus = newArrayList();
		submenus.add( new MainSubMenuItem( mainMenuItem, MainMenuTitle.PHOTOS_ALL, webDriver ) );
		submenus.add( new MainSubMenuItem( mainMenuItem, MainMenuTitle.PHOTOS_TODAY, webDriver ) );
		submenus.add( new MainSubMenuItem( mainMenuItem, MainMenuTitle.PHOTOS_YESTERDAY, webDriver ) );
		submenus.add( new MainSubMenuItem( mainMenuItem, MainMenuTitle.PHOTOS_WEEK, webDriver ) );
		submenus.add( new MainSubMenuItem( mainMenuItem, MainMenuTitle.PHOTOS_THIS_MONTH, webDriver ) );
		submenus.add( new MainSubMenuItem( mainMenuItem, MainMenuTitle.PHOTOS_AUTHORS, webDriver ) );
		submenus.add( new MainSubMenuItem( mainMenuItem, MainMenuTitle.PHOTOS_MODELS, webDriver ) );
		submenus.add( new MainSubMenuItem( mainMenuItem, MainMenuTitle.PHOTOS_MAKEUP_MASTERS, webDriver ) );

		mainMenuItem.setSubMenuItems( submenus );

		return mainMenuItem;
	}

	public MainMenuItem getMembersMenu() {

		final MainMenuTitle menuTitle = MainMenuTitle.MEMBERS;

		final MainMenuItem mainMenuItem = new MainMenuItem( menuTitle, webDriver );

		final List<MainSubMenuItem> submenus = newArrayList();
		submenus.add( new MainSubMenuItem( mainMenuItem, MainMenuTitle.MEMBERS_ALL, webDriver ) );
		submenus.add( new MainSubMenuItem( mainMenuItem, MainMenuTitle.MEMBERS_AUTHORS, webDriver ) );
		submenus.add( new MainSubMenuItem( mainMenuItem, MainMenuTitle.MEMBERS_MODELS, webDriver ) );
		submenus.add( new MainSubMenuItem( mainMenuItem, MainMenuTitle.MEMBERS_MAKEUP_MASTER, webDriver ) );

		mainMenuItem.setSubMenuItems( submenus );

		return mainMenuItem;
	}
}
