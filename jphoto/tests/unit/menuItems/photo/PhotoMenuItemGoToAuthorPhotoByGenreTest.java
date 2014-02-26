package menuItems.photo;

import core.general.genre.Genre;
import core.general.menus.AbstractEntryMenuItem;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.photo.items.PhotoMenuItemGoToAuthorPhotoByGenre;
import core.general.user.User;
import core.services.entry.GenreService;
import core.services.photo.PhotoService;
import core.services.security.ServicesImpl;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PhotoMenuItemGoToAuthorPhotoByGenreTest extends AbstractPhotoMenuItemTest_ {

	@Test
	public void commandTest() {
		final GoToParameters parameters = new GoToParameters( testData.getAccessor(), 7 );

		final ServicesImpl services = getServicesGoTo( parameters );

		final PhotoMenuItemGoToAuthorPhotoByGenre menuItem = new PhotoMenuItemGoToAuthorPhotoByGenre( testData.getPhoto(), parameters.getAccessor(), services );
		final AbstractEntryMenuItemCommand command = menuItem.getMenuItemCommand();

		final User photoAuthor = testData.getPhotoAuthor();
		final Genre genre = testData.getGenre();

		assertEquals( WRONG_COMMAND, command.getMenuText(), String.format( "%s: photos in category '%s' ( %s )", photoAuthor.getNameEscaped(), genre.getName(), parameters.getPhotosQty() ) );

		assertEquals( WRONG_COMMAND, command.getMenuCommand(), String.format( "goToMemberPhotosByGenre( %d, %d );", photoAuthor.getId(), genre.getId() ) );

		assertEquals( WRONG_COMMAND, menuItem.getMenuCssClass(), AbstractEntryMenuItem.DEFAULT_CSS_CLASS );
	}

	private ServicesImpl getServicesGoTo( final GoToParameters parameters ) {
		final ServicesImpl services = getServices( testData, parameters.getAccessor() );
		services.setGenreService( getGenreService() );
		services.setPhotoService( getPhotoService( parameters ) );

		return services;
	}

	private GenreService getGenreService() {
		final GenreService genreService = EasyMock.createMock( GenreService.class );

		EasyMock.expect( genreService.load( testData.getPhoto().getGenreId() ) ).andReturn( testData.getGenre() ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( genreService );

		return genreService;
	}

	private PhotoService getPhotoService( final GoToParameters parameters ) {
		final PhotoService photoService = EasyMock.createMock( PhotoService.class );

		EasyMock.expect( photoService.getPhotoQtyByUserAndGenre( testData.getPhotoAuthor().getId(), testData.getPhoto().getGenreId() ) ).andReturn( parameters.getPhotosQty() ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( photoService );

		return photoService;
	}
}
