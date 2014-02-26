package menuItems.photo;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.photo.items.PhotoMenuItemInfo;
import core.general.user.User;
import core.services.security.ServicesImpl;
import core.services.utils.UrlUtilsService;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PhotoMenuItemInfoTest extends AbstractPhotoMenuItemTest_ {

	private static final String PHOTO_URL = "PHOTO_URL";

	@Test
	public void anybodyCanSeeMenuTest() {

		final ServicesImpl services = getServices( User.NOT_LOGGED_USER );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoMenuItemInfo( testData.getPhoto(), User.NOT_LOGGED_USER, services ).isAccessibleFor() );
		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoMenuItemInfo( testData.getPhoto(), testData.getPhotoAuthor(), services ).isAccessibleFor() );
		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoMenuItemInfo( testData.getPhoto(), testData.getAccessor(), services ).isAccessibleFor() );
		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoMenuItemInfo( testData.getPhoto(), SUPER_ADMIN_1, services ).isAccessibleFor() );
	}

	@Test
	public void commandTest() {
		final User accessor = User.NOT_LOGGED_USER;

		final AbstractEntryMenuItemCommand command = new PhotoMenuItemInfo( testData.getPhoto(), accessor, getServicesInfo() ).getMenuItemCommand();

		assertEquals( WRONG_COMMAND, command.getMenuText(), "Photo info" );
		assertEquals( WRONG_COMMAND, command.getMenuCommand(), String.format( "openPopupWindowCustom( '%s', 'photoInfo_%d', 460, 800, 100, 100 );", PHOTO_URL, testData.getPhoto().getId() ) );
	}

	private ServicesImpl getServicesInfo() {
		final ServicesImpl services = new ServicesImpl();

		services.setUrlUtilsService( getUrlUtilsService() );

		return services;
	}

	private UrlUtilsService getUrlUtilsService() {
		final UrlUtilsService urlUtilsService = EasyMock.createMock( UrlUtilsService.class );

		EasyMock.expect( urlUtilsService.getPhotoInfoLink( testData.getPhoto().getId() ) ).andReturn( PHOTO_URL ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( urlUtilsService );

		return urlUtilsService;
	}
}
