package kwaqua.core.mainMenu;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;


import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class MainSubMenuItem extends AbstractMainMenuItem {

	private final MainMenuTitle parentMenuTitle;

	public MainSubMenuItem( final MainMenuTitle title, final MainMenuTitle parentMenuTitle, final WebDriver webDriver ) {
		super( title, webDriver );
		this.parentMenuTitle = parentMenuTitle;
	}

	@Override
	public List<AbstractMainMenuItem> getSubMenuItems() {
		return newArrayList();
	}

	@Override
	final protected String getSelector() {
		return String.format( "//div[contains(@class,'mainMenu')]/div/div[contains(@class,'menuItem')]/ul/li/a[contains(text(),\"%s\")]/../ul/li//a[contains(text(),\"%s\")]"
			, parentMenuTitle, title.getTitle() );
	}

	@Override
	public void click() {
		final Actions builder = new Actions( webDriver ); // TODO: exception

		final Actions hoverOverRegistrar = builder.moveToElement( parentMenuItem.getElement() );
		hoverOverRegistrar.perform();
	}
}
