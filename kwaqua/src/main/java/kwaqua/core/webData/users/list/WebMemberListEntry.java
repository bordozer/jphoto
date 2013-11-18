package kwaqua.core.webData.users.list;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class WebMemberListEntry {

	private final int id;
	private final String name;

	public WebMemberListEntry( final WebElement userListTrElement ) {
		id = Integer.parseInt(  userListTrElement.findElement( By.xpath( "/td[1]" ) ).getText().trim() );
		name = userListTrElement.findElement( By.xpath( "/td[3]" ) ).getText().trim();
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
