package kwaqua.tools.utils;

import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;

public class TimeoutUtils {

	public static void changeImplicitTimeout( final int timeout, final WebDriver webDriver ) {
		changeImplicitTimeout( timeout, webDriver, TimeUnit.MILLISECONDS );
	}

	public static void changeImplicitTimeout( final long timeout, final WebDriver webDriver, final TimeUnit timeUnit ) {
		webDriver.manage().timeouts().implicitlyWait( timeout, timeUnit );
	}

	public static void resetImplicitTimeout( final WebDriver driver ) {
		driver.manage().timeouts().implicitlyWait( SystemVars.getDefaultImplicitTimeout(), TimeUnit.MILLISECONDS );
	}
}
