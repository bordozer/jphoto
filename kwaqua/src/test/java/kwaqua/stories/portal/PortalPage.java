package kwaqua.stories.portal;

import kwaqua.KwaquaStory;
import kwaqua.pages.PortalPagePage;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;

import static junit.framework.Assert.assertTrue;

public class PortalPage extends KwaquaStory {

	@Given( "I open Portal page" )
	public void openPortalPage() {
		go2PortalPage();
	}

	@Then( "Portal page is shown" )
	public void portalPageIsShown() {
		assertTrue( getPages().isCurrentPageAt( PortalPagePage.class ) );
	}
}
