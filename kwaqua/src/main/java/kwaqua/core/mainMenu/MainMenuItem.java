package kwaqua.core.mainMenu;

import org.openqa.selenium.WebDriver;

import java.util.List;

public class MainMenuItem extends AbstractMainMenuItem {

	private List<MainSubMenuItem> subMenuItems;

	public MainMenuItem( final MainMenuTitle title, final WebDriver webDriver ) {
		super( title, webDriver );
	}

	@Override
	final protected String getSelector() {
		return String.format( "//div[@id='cssmenu']/ul/li/a/span[text()='%s']/..", title.getTitle() );
	}

	public AbstractMainMenuItem getSubMenuItem( final MainMenuTitle subMenuTitle ) {
		final List<MainSubMenuItem> subMenuItems = getSubMenuItems();
		for ( final AbstractMainMenuItem subMenuItem : subMenuItems ) {
			if ( subMenuItem.getTitle() == subMenuTitle ) {
				return subMenuItem;
			}
		}

		return null;
	}

	public List<MainSubMenuItem> getSubMenuItems() {
		return subMenuItems;
	}

	public void setSubMenuItems( final List<MainSubMenuItem> subMenuItems ) {
		this.subMenuItems = subMenuItems;
	}
}
