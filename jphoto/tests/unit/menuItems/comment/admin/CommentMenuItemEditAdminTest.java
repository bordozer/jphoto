package menuItems.comment.admin;

import core.general.configuration.ConfigurationKey;
import core.general.menus.AbstractEntryMenuItem;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.comment.admin.CommentMenuItemEditAdmin;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.security.Services;
import core.services.security.ServicesImpl;
import core.services.system.ConfigurationService;
import menuItems.comment.AbstractCommentMenuItemTest_;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommentMenuItemEditAdminTest extends AbstractCommentMenuItemTest_ {

	@Test
	public void notLoggedUserCanNotSeeEditCommentAdminSubMenuItemTest() {
		final User user = User.NOT_LOGGED_USER;
		final Services services = getServicesForTest( user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemEditAdmin( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanNotSeeEditCommentAdminSubMenuItemTest() {
		final User user = testData.getAccessor();
		final Services services = getServicesForTest( user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemEditAdmin( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanNotSeeEditCommentAdminSubMenuItemTest() {
		final User user = testData.getPhotoAuthor();
		final Services services = getServicesForTest( user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemEditAdmin( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void commentAuthorCanNotSeeEditCommentAdminSubMenuItemTest() {
		final User user = testData.getCommentAuthor();
		final Services services = getServicesForTest( user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemEditAdmin( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void deletedCommentCanNotBeEditedTest() {
		final User user = SUPER_ADMIN_1;
		final Services services = getServicesForTest( user );

		final PhotoComment comment = testData.getComment();
		comment.setCommentDeleted( true );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemEditAdmin( comment, user, services ).isAccessibleFor() );
	}

	@Test
	public void adminCanNotSeeEditCommentAdminSubMenuItemIfItIsSwitchedOFFTest() {
		final User user = SUPER_ADMIN_1;
		final Services services = getServicesForTest( user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemEditAdmin( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void adminCanSeeEditCommentAdminSubMenuItemIfItIsSwitchedONTest() {
		final User user = SUPER_ADMIN_1;
		final Services services = getServicesForTest( user, true );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentMenuItemEditAdmin( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void commandTest() {
		final User user = SUPER_ADMIN_1;
		final Services services = getServices( user );

		final AbstractEntryMenuItemCommand command = new CommentMenuItemEditAdmin( testData.getComment(), user, services ).getMenuItemCommand();

		assertEquals( WRONG_COMMAND, command.getMenuText(), "Edit comment" );
		assertEquals( WRONG_COMMAND, command.getMenuCommand(), String.format( "editComment( %d ); return false;", testData.getComment().getId() ) );
	}

	@Test
	public void cssClassTest() {
		final User user = User.NOT_LOGGED_USER; // does not matter
		final Services services = getServices( user );
		final CommentMenuItemEditAdmin menuItem = new CommentMenuItemEditAdmin( testData.getComment(), user, services );

		assertEquals( WRONG_COMMAND, menuItem.getMenuCssClass(), AbstractEntryMenuItem.MENU_ITEM_CSS_CLASS_ADMIN );
	}

	private Services getServicesForTest( final User user ) {
		return getServicesForTest( user, false );
	}

	private Services getServicesForTest( final User user, final Boolean adminCanEditComments ) {
		final ServicesImpl services = getServices( user );

		services.setConfigurationService( getConfigurationService( adminCanEditComments ) );

		return services;
	}

	private ConfigurationService getConfigurationService( final Boolean adminCanEditComments ) {
		final ConfigurationService configurationService = EasyMock.createMock( ConfigurationService.class );

		EasyMock.expect( configurationService.getBoolean( ConfigurationKey.ADMIN_CAN_EDIT_PHOTO_COMMENTS ) ).andReturn( adminCanEditComments ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( configurationService );
		return configurationService;
	}
}
