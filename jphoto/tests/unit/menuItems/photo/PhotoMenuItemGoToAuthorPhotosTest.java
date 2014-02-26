package menuItems.photo;

import core.general.menus.AbstractEntryMenuItem;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.photo.items.PhotoMenuItemGoToAuthorPhotos;
import core.general.user.User;
import core.services.photo.PhotoService;
import core.services.security.ServicesImpl;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PhotoMenuItemGoToAuthorPhotosTest extends AbstractPhotoMenuItemTest_ {

	@Test
	public void commandTest() {
		final GoToParameters parameters = new GoToParameters( testData.getAccessor(), 7 );

		final ServicesImpl services = getServicesGoTo( parameters );

		final PhotoMenuItemGoToAuthorPhotos menuItem = new PhotoMenuItemGoToAuthorPhotos( testData.getPhoto(), parameters.getAccessor(), services );
		final AbstractEntryMenuItemCommand command = menuItem.getMenuItemCommand();

		final User photoAuthor = testData.getPhotoAuthor();

		assertEquals( WRONG_COMMAND, command.getMenuText(), String.format( "%s: all photos ( %d )", photoAuthor.getNameEscaped(), parameters.getPhotosQty() ) );

		assertEquals( WRONG_COMMAND, command.getMenuCommand(), String.format( "goToMemberPhotos( %d );", photoAuthor.getId() ) );

		assertEquals( WRONG_COMMAND, menuItem.getMenuCssClass(), AbstractEntryMenuItem.MENU_ITEM_CSS_CLASS_DEFAULT );
	}

	private ServicesImpl getServicesGoTo( final GoToParameters parameters ) {
		final ServicesImpl services = getServices( testData, parameters.getAccessor() );
		services.setPhotoService( getPhotoService( parameters ) );

		return services;
	}

	private PhotoService getPhotoService( final GoToParameters parameters ) {
		final PhotoService photoService = EasyMock.createMock( PhotoService.class );

		EasyMock.expect( photoService.getPhotoQtyByUser( testData.getPhotoAuthor().getId() ) ).andReturn( parameters.getPhotosQty() ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( photoService );

		return photoService;
	}
}
