package menuItems.photo;

import core.general.menus.AbstractEntryMenuItem;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.photo.items.PhotoMenuItemGoToAuthorPhotoByAlbum;
import core.general.user.User;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.services.security.ServicesImpl;
import core.services.user.UserPhotoAlbumService;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PhotoMenuItemGoToAuthorPhotoByAlbumTest extends AbstractPhotoMenuItemTest_ {

	private UserPhotoAlbum userPhotoAlbum;

	@Before
	public void setup() {
		super.setup();

		userPhotoAlbum = getUserPhotoAlbum();
	}

	@Test
	public void commandTest() {
		final GoToParameters parameters = new GoToParameters( testData.getAccessor(), 7 );

		final ServicesImpl services = getServicesGoTo( parameters );

		final PhotoMenuItemGoToAuthorPhotoByAlbum menuItem = new PhotoMenuItemGoToAuthorPhotoByAlbum( testData.getPhoto(), parameters.getAccessor(), services, userPhotoAlbum );
		final AbstractEntryMenuItemCommand command = menuItem.getMenuItemCommand();

		final User photoAuthor = testData.getPhotoAuthor();

		assertEquals( WRONG_COMMAND, command.getMenuText(), String.format( "%s: photos from album '%s' ( %s )", photoAuthor.getNameEscaped(), userPhotoAlbum.getName(), parameters.getPhotosQty() ) );

		assertEquals( WRONG_COMMAND, command.getMenuCommand(), String.format( "goToMemberPhotosByAlbum( %d, %d );", photoAuthor.getId(), userPhotoAlbum.getId() ) );

		assertEquals( WRONG_COMMAND, menuItem.getMenuCssClass(), AbstractEntryMenuItem.MENU_ITEM_CSS_CLASS_DEFAULT );
	}

	private ServicesImpl getServicesGoTo( final GoToParameters parameters ) {
		final ServicesImpl services = getServices( testData, parameters.getAccessor() );
		services.setUserPhotoAlbumService( getUserPhotoAlbumService( parameters ) );

		return services;
	}

	private UserPhotoAlbumService getUserPhotoAlbumService( final GoToParameters parameters ) {
		final UserPhotoAlbumService userPhotoAlbumService = EasyMock.createMock( UserPhotoAlbumService.class );

		EasyMock.expect( userPhotoAlbumService.getUserPhotoAlbumPhotosQty( userPhotoAlbum.getId() ) ).andReturn( parameters.getPhotosQty() ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( userPhotoAlbumService );

		return userPhotoAlbumService;
	}

	private UserPhotoAlbum getUserPhotoAlbum() {
		final UserPhotoAlbum album = new UserPhotoAlbum();

		album.setId( 881 );
		album.setName( "Photo album" );

		return album;
	}
}
