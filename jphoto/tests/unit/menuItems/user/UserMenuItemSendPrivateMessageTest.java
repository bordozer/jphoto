package menuItems.user;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.user.items.UserMenuItemSendPrivateMessage;
import core.general.user.User;
import core.services.entry.FavoritesService;
import core.services.security.SecurityService;
import core.services.security.ServicesImpl;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserMenuItemSendPrivateMessageTest extends AbstractUserMenuItemTest_ {

	@Test
	public void notLoggedUserCanNotSeeMenuTest() {
		final User accessor = User.NOT_LOGGED_USER;

		final ServicesImpl services = getServices( testData );
		services.setSecurityService( getSecurityService( accessor ) );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new UserMenuItemSendPrivateMessage( testData.getUser(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void userCanNotSeeSendMessageInOwnMenuTest() {
		final ServicesImpl services = getServices( testData );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new UserMenuItemSendPrivateMessage( testData.getUser(), testData.getUser(), services ).isAccessibleFor() );
	}

	@Test
	public void userCanNotSeeMenuIfHeIsInBlackListTest() {
		final User accessor = testData.getAccessor();

		final ServicesImpl services = getServices( testData );
		services.setSecurityService( getSecurityService( accessor ) );
		services.setFavoritesService( getFavoritesService( true ) );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new UserMenuItemSendPrivateMessage( testData.getUser(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void userCanSeeMenuIfHeIsNotInBlackListTest() {
		final User accessor = testData.getAccessor();

		final ServicesImpl services = getServices( testData );
		services.setSecurityService( getSecurityService( accessor ) );
		services.setFavoritesService( getFavoritesService( false ) );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new UserMenuItemSendPrivateMessage( testData.getUser(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void menuAccessibleForSuperAdminTest() {
		final User accessor = SUPER_MEGA_ADMIN;

		final ServicesImpl services = getServices( testData );
		services.setSecurityService( getSecurityService( accessor ) );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new UserMenuItemSendPrivateMessage( testData.getUser(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void commandTest() {
		final User accessor = SUPER_MEGA_ADMIN;

		final ServicesImpl services = getServices( testData );

		final AbstractEntryMenuItemCommand command = new UserMenuItemSendPrivateMessage( testData.getUser(), accessor, services ).getMenuItemCommand();

		assertEquals( WRONG_COMMAND, command.getMenuText(), String.format( "Send private message to %s", testData.getUser().getNameEscaped() ) );
		assertEquals( WRONG_COMMAND, command.getMenuCommand(), String.format( "sendPrivateMessage( %d, %d, '%s' );", accessor.getId(), testData.getUser().getId(), testData.getUser().getNameEscaped() ) );
	}

	private SecurityService getSecurityService( final User user ) {
		final SecurityService userService = EasyMock.createMock( SecurityService.class );

		EasyMock.expect( userService.isSuperAdminUser( user.getId() ) ).andReturn( user.getId() == SUPER_MEGA_ADMIN.getId() ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( userService );

		return userService;
	}

	private FavoritesService getFavoritesService( final boolean isInBlackList ) {
		final FavoritesService favoritesService = EasyMock.createMock( FavoritesService.class );

		EasyMock.expect( favoritesService.isUserInBlackListOfUser( testData.getUser().getId(), testData.getAccessor().getId() ) ).andReturn( isInBlackList ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( favoritesService );

		return favoritesService;
	}
}
