package kwaqua.pages;

import net.thucydides.core.annotations.At;
import net.thucydides.core.annotations.DefaultUrl;
import net.thucydides.core.pages.PageObject;
import org.openqa.selenium.WebDriver;

@DefaultUrl( "http://localhost:8083/jphoto/" )
@At( urls = {"#HOST/jphoto/"} )
public class PortalPagePage extends PageObject {

	public PortalPagePage( final WebDriver driver ) {
		super( driver );
	}
}
