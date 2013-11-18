package kwaqua.steps;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.pages.Pages;
import net.thucydides.core.steps.ScenarioSteps;
import org.openqa.selenium.Alert;

public class CommonSteps extends ScenarioSteps {

	public CommonSteps( final Pages pages ) {
		super( pages );
	}

	@Step
	public String getAlertMessage() {
		final Alert alert = getDriver().switchTo().alert();
		return alert.getText();
	}

	@Step
	public void closeBrowser() {
		getDriver().close();
	}

	@Step
	public void closeAlertMessage() {
		getDriver().switchTo().alert().accept();
	}
}
