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
				result.add( new MainSubMenuItem( menuTitle, MainMenuTitle.PHOTOS_ALL, webDriver ) );
				result.add( new MainSubMenuItem( menuTitle, MainMenuTitle.PHOTOS_TODAY, webDriver ) );
				result.add( new MainSubMenuItem( menuTitle, MainMenuTitle.PHOTOS_YESTERDAY, webDriver ) );
				result.add( new MainSubMenuItem( menuTitle, MainMenuTitle.PHOTOS_WEEK, webDriver ) );
				result.add( new MainSubMenuItem( menuTitle, MainMenuTitle.PHOTOS_THIS_MONTH, webDriver ) );
				result.add( new MainSubMenuItem( menuTitle, MainMenuTitle.PHOTOS_AUTHORS, webDriver ) );
				result.add( new MainSubMenuItem( menuTitle, MainMenuTitle.PHOTOS_MODELS, webDriver ) );
				result.add( new MainSubMenuItem( menuTitle, MainMenuTitle.PHOTOS_MAKEUP_MASTERS, webDriver ) );

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
				result.add( new MainSubMenuItem( menuTitle, MainMenuTitle.MEMBERS_ALL, webDriver ) );
				result.add( new MainSubMenuItem( menuTitle, MainMenuTitle.MEMBERS_AUTHORS, webDriver ) );
				result.add( new MainSubMenuItem( menuTitle, MainMenuTitle.MEMBERS_MODELS, webDriver ) );
				result.add( new MainSubMenuItem( menuTitle, MainMenuTitle.MEMBERS_MAKEUP_MASTER, webDriver ) );

				return result;
			}
		};
	}
}
