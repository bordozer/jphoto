package menuItems;

import common.AbstractTestCase;
import core.general.menus.AbstractEntryMenuItem;
import core.general.menus.EntryMenu;
import core.general.menus.EntryMenuType;
import core.general.photo.PhotoComment;
import org.junit.Test;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;

public class EntryMenuTest extends AbstractTestCase {

	private static final String WRONG_MENU_TITLE = "Wrong menu title";

	@Test
	public void commentMenuTest() {
		final PhotoComment comment = new PhotoComment();
		comment.setId( 912 );

		final List<? extends AbstractEntryMenuItem> menuEntries = newArrayList();

		final EntryMenu entryMenu = new EntryMenu( comment, EntryMenuType.COMMENT, menuEntries, MENU_LANGUAGE, getServices() );

		assertEquals( WRONG_MENU_TITLE, String.format( "Comment: #%d", comment.getId() ), entryMenu.getMenuTitle() );
	}

	@Test
	public void commentMenuIfCommentIsDeletedTest() {
		final PhotoComment comment = new PhotoComment();
		comment.setId( 912 );
		comment.setCommentDeleted( true );

		final List<? extends AbstractEntryMenuItem> menuEntries = newArrayList();

		final EntryMenu entryMenu = new EntryMenu( comment, EntryMenuType.COMMENT, menuEntries, MENU_LANGUAGE, getServices() );

		assertEquals( WRONG_MENU_TITLE, translated( String.format( "Comment: #%d ( deleted )", comment.getId()) ), entryMenu.getMenuTitle() );
	}
}
