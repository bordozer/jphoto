package kwaqua.core.mainMenu;

import org.openqa.selenium.WebDriver;

public abstract class MainMenuItem extends AbstractMainMenuItem {

	public MainMenuItem( final MainMenuTitle title, final WebDriver webDriver ) {
		super( title, webDriver );
	}

	@Override
	final protected String getSelector() {
		return String.format( "//div[@id='cssmenu']/ul/li/a/span[text()='%s']/..", title.getTitle() );
	}
}
