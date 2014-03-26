package menuItems.photo.goTo;

import core.general.genre.Genre;
import core.general.menus.AbstractEntryMenuItem;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.photo.goTo.PhotoMenuItemGoToAuthorPhotoByGenre;
import core.general.user.User;
import core.services.entry.GenreService;
import core.services.photo.PhotoService;
import core.services.security.ServicesImpl;
import menuItems.photo.AbstractPhotoMenuItemTest_;
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

		assertEquals( EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuText(), translated( String.format( "%s: photos in category '%s' ( %s )", photoAuthor.getNameEscaped(), translatorService.translateGenre( testData.getGenre() ), parameters.getPhotosQty() ) ) );

		assertEquals( EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuCommand(), String.format( "goToMemberPhotosByGenre( %d, %d );", photoAuthor.getId(), genre.getId() ) );

		assertEquals( EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, menuItem.getMenuCssClass(), AbstractEntryMenuItem.MENU_ITEM_CSS_CLASS_DEFAULT );
	}

	private ServicesImpl getServicesGoTo( final GoToParameters parameters ) {
		final ServicesImpl services = getServices( parameters.getAccessor() );
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
