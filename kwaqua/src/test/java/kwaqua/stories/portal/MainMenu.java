package kwaqua.stories.portal;

import kwaqua.KwaquaStory;
import kwaqua.core.mainMenu.MainMenuTitle;
import kwaqua.pages.MemberListPage;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;

import static junit.framework.Assert.assertTrue;

public class MainMenu extends KwaquaStory {

	@Given( "I open Portal page" )
	public void openPortalPage() {
		go2PortalPage();
	}

	@Given( "I click 'Members' in the main menu" )
	public void clickMainMenu(  ) {
		clickMainMenu( MainMenuTitle.MEMBERS );
	}

	@Then( "Member list is shown" )
	public void memberListIsShown() {
		assertTrue( getPages().isCurrentPageAt( MemberListPage.class ) );
	}

	@Given( "I click Members > Models in the main menu" )
	public void clickMainMenuSubMenu() {
		clickMainMenu( MainMenuTitle.MEMBERS, MainMenuTitle.MEMBERS_MODELS );
	}
}
