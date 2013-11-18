package kwaqua.core.mainMenu;

import org.openqa.selenium.WebDriver;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class MainMenuFactory {

	private final WebDriver webDriver;

	public MainMenuFactory( final WebDriver webDriver ) {
		this.webDriver = webDriver;
	}

	public AbstractMainMenuItem getPhotosMenu() {
		final MainMenuTitle menuTitle = MainMenuTitle.PHOTOS;
		
		return new MainMenuItem( menuTitle, webDriver ) {
			@Override
			public List<AbstractMainMenuItem> getSubMenuItems() {
				final List<AbstractMainMenuItem> result = newArrayList();
				result.add( new MainSubMenuItem( MainMenuTitle.PHOTOS_ALL, menuTitle, webDriver ) );
				result.add( new MainSubMenuItem( MainMenuTitle.PHOTOS_TODAY, menuTitle, webDriver ) );
				result.add( new MainSubMenuItem( MainMenuTitle.PHOTOS_YESTERDAY, menuTitle, webDriver ) );
				result.add( new MainSubMenuItem( MainMenuTitle.PHOTOS_WEEK, menuTitle, webDriver ) );
				result.add( new MainSubMenuItem( MainMenuTitle.PHOTOS_THIS_MONTH, menuTitle, webDriver ) );
				result.add( new MainSubMenuItem( MainMenuTitle.PHOTOS_AUTHORS, menuTitle, webDriver ) );
				result.add( new MainSubMenuItem( MainMenuTitle.PHOTOS_MODELS, menuTitle, webDriver ) );
				result.add( new MainSubMenuItem( MainMenuTitle.PHOTOS_MAKEUP_MASTERS, menuTitle, webDriver ) );

				return result;
			}
		};
	}

	public AbstractMainMenuItem getMembersMenu() {
		final MainMenuTitle menuTitle = MainMenuTitle.MEMBERS;

		return new MainMenuItem( menuTitle, webDriver ) {
			@Override
			public List<AbstractMainMenuItem> getSubMenuItems() {
				final List<AbstractMainMenuItem> result = newArrayList();
				result.add( new MainSubMenuItem( MainMenuTitle.MEMBERS_ALL, menuTitle, webDriver ) );
				result.add( new MainSubMenuItem( MainMenuTitle.MEMBERS_AUTHORS, menuTitle, webDriver ) );
				result.add( new MainSubMenuItem( MainMenuTitle.MEMBERS_MODELS, menuTitle, webDriver ) );
				result.add( new MainSubMenuItem( MainMenuTitle.MEMBERS_MAKEUP_MASTER, menuTitle, webDriver ) );

				return result;
			}
		};
	}
}
