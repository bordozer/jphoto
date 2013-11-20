package kwaqua.pages;

import kwaqua.core.webData.users.list.WebMemberList;
import net.thucydides.core.annotations.At;
import net.thucydides.core.annotations.DefaultUrl;
import net.thucydides.core.pages.PageObject;
import org.openqa.selenium.WebDriver;

@DefaultUrl( "http://localhost:8083/jphoto/app/members/" )
@At( urls = {"#HOST/jphoto/app/members/.*"} )
public class MemberListPage extends PageObject {

	public MemberListPage( final WebDriver driver ) {
		super( driver );
	}

	public WebMemberList getWebMemberList() {
		return new WebMemberList( getDriver() );
	}
}
