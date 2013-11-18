package kwaqua.core.webData.users.list;

import net.thucydides.core.annotations.findby.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class WebMemberList {

	private final List<WebMemberListEntry> members;

	public WebMemberList( final WebDriver webDriver ) {
		members = newArrayList();

		final List<WebElement> elements = webDriver.findElements( By.xpath( "//table[@id='user_list_table']/tbody/tr" ) );
		for ( final WebElement trElement : elements ) {
			members.add( new WebMemberListEntry( trElement ) );
		}
	}

	public List<WebMemberListEntry> getMembers() {
		return members;
	}

	public boolean hasMember( final int id ) {
		for ( final WebMemberListEntry member : members ) {
			if ( member.getId() == id ) {
				return true;
			}
		}

		return false;
	}

	public boolean hasMember( final String name ) {
		for ( final WebMemberListEntry member : members ) {
			if ( member.getName().equals( name ) ) {
				return true;
			}
		}

		return false;
	}
}
