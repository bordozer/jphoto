package kwaqua.core.mainMenu;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

public class MainSubMenuItem extends AbstractMainMenuItem {

	protected MainMenuItem mainMenuItem;

	public MainSubMenuItem( final MainMenuItem mainMenuItem, final MainMenuTitle title, final WebDriver webDriver ) {
		super( title, webDriver );
		this.mainMenuItem = mainMenuItem;
	}

	@Override
	final protected String getSelector() {
		return String.format( "//div[@id='cssmenu']/ul/li/a/span[text()='%s']/../../ul/li/a/span[contains(text(), '%s')]/..", mainMenuItem.getTitle().getTitle(), title.getTitle() );
	}

	@Override
	public void click() {
		final Actions hover = new Actions( webDriver );

		hover.moveToElement( mainMenuItem.getElement(), 10, 10 )
			.click()
			.perform();
	}

	public AbstractMainMenuItem getMainMenuItem() {
		return mainMenuItem;
	}
}
