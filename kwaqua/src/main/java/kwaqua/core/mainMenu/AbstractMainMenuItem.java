package kwaqua.core.mainMenu;

import com.google.common.base.Function;
import kwaqua.tools.utils.SystemVars;
import kwaqua.tools.utils.TimeoutUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class AbstractMainMenuItem {

	protected static final long FIND_ELEMENT_TIMEOUT_IN_MILLISECONDS = SystemVars.getControlWaitTimeout();

	protected final WebDriver webDriver;

	protected final MainMenuTitle title;

	protected abstract String getSelector();

	public AbstractMainMenuItem( final MainMenuTitle title, final WebDriver webDriver ) {
		this.title = title;
		this.webDriver = webDriver;
	}

	public MainMenuTitle getTitle() {
		return title;
	}

	public void click() {
		getElement().click();
	}

	public WebElement getElement() {
		return findBy( getSelector() );
	}

	protected final WebElement findBy( final String xpath ) {
		return findBy( By.xpath( xpath ), FIND_ELEMENT_TIMEOUT_IN_MILLISECONDS, TimeUnit.MILLISECONDS );
	}

	/*protected final WebElement findBy( final By xpath ) {
		return findBy( xpath, FIND_ELEMENT_TIMEOUT_IN_MILLISECONDS, TimeUnit.MILLISECONDS );
	}

	protected final WebElement findBy( final String xpath, final long timeout ) {
		return findBy( By.xpath( xpath ), timeout, TimeUnit.MILLISECONDS );
	}

	protected final WebElement findBy( final By xpath, final long timeout ) {
		return findBy( xpath, timeout, TimeUnit.MILLISECONDS );
	}

	protected final WebElement findBy( final String xpath, final long timeout, final TimeUnit timeUnit ) {
		return findBy( By.xpath( xpath ), timeout, timeUnit );
	}*/

	protected final WebElement findBy( final By xpath, final long timeout, final TimeUnit timeUnit ) {

		final List<WebElement> elements = new WebDriverWait( webDriver, FIND_ELEMENT_TIMEOUT_IN_MILLISECONDS / 1000 ).until( new Function<WebDriver, List<WebElement>>() {
			@Override
			public List<WebElement> apply( final WebDriver webDriver ) {
				TimeoutUtils.changeImplicitTimeout( timeout, webDriver, timeUnit );

				return webDriver.findElements( xpath );
			}
		} );

		TimeoutUtils.resetImplicitTimeout( webDriver );

		return elements.size() == 0 ? null : elements.get( 0 );
	}

	@Override
	public String toString() {
		return String.format( "Main menu: %s", title.getTitle() );
	}
}
