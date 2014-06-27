package kwaqua.core.mainMenu;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;


import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class MainSubMenuItem extends AbstractMainMenuItem {

	private final MainMenuTitle mainMenuTitle;

	public MainSubMenuItem( final MainMenuTitle mainMenuTitle, final MainMenuTitle submenuTitle, final WebDriver webDriver ) {
		super( submenuTitle, webDriver );
		this.mainMenuTitle = mainMenuTitle;
	}

	@Override
	public List<AbstractMainMenuItem> getSubMenuItems() {
		return newArrayList();
	}

	@Override
	final protected String getSelector() {
		return String.format( "//div[contains(@class,'mainMenu')]/div/div[contains(@class,'menuItem')]/ul/li/a[contains(text(),\"%s\")]/../ul/li//a[contains(text(),\"%s\")]"
			, mainMenuTitle, title.getTitle() );
	}

	@Override
	public void click() {
		final Actions builder = new Actions( webDriver ); // TODO: exception

		final Actions hoverOverRegistrar = builder.moveToElement( parentMenuItem.getElement() );
		hoverOverRegistrar.perform();
	}
}
