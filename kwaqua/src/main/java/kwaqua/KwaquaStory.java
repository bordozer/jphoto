package kwaqua;

import kwaqua.core.mainMenu.MainMenu;
import kwaqua.core.mainMenu.MainMenuTitle;
import kwaqua.pages.PortalPagePage;
import kwaqua.steps.CommonSteps;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.pages.Pages;
import net.thucydides.jbehave.ThucydidesJUnitStory;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.openqa.selenium.WebDriver;

public class KwaquaStory extends ThucydidesJUnitStory {

	@Steps
	CommonSteps system;
	
	static {
		Configurator.applyConfiguration();
	}

	@AfterScenario
	public void closeBrowser() {
		system.closeBrowser();
	}

	protected Pages getPages() {
		return system.getPages();
	}

	private WebDriver getWebDriver() {
		return system.getDriver();
	}

	@Step
	public void go2PortalPage() {
		final PortalPagePage portalPage = getPages().getPage( PortalPagePage.class );
		portalPage.open();
	}

	public MainMenu getMainMenu() {
		return new MainMenu( getWebDriver() );
	}

	protected void clickMainMenu( final MainMenuTitle mainMenuTitle ) {
		getMainMenu().click( mainMenuTitle );
	}

	protected void clickMainMenu( final MainMenuTitle mainMenuTitle, final MainMenuTitle subMenuTitle ) {
		getMainMenu().click( mainMenuTitle, subMenuTitle );
	}

	protected void clickMainMenuPhotos() {
		clickMainMenu( MainMenuTitle.PHOTOS );
	}

	protected void clickMainMenuMembers() {
		clickMainMenu( MainMenuTitle.MEMBERS );
	}

	@Override
	public InjectableStepsFactory stepsFactory() {
		return new SingleTestStepsFactory( this.getClass(), configuration(), getRootPackage(), getClassLoader() );
	}
}
