package kwaqua.core.mainMenu;

import org.openqa.selenium.WebDriver;

public abstract class MainMenuItem extends AbstractMainMenuItem {

	public MainMenuItem( final MainMenuTitle title, final WebDriver webDriver ) {
		super( title, webDriver );
	}

	@Override
	final protected String getSelector() {
		return String.format( "//div[contains(@class,'mainMenu')]/div/div[contains(@class,'menuItem')]/ul/li/a[contains(text(),\"%s\")]", title.getTitle() );
	}
}
