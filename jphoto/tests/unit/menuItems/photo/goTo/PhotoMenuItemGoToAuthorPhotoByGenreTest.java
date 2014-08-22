package menuItems.photo.goTo;

import common.AbstractTestCase;
import core.general.genre.Genre;
import ui.services.menu.entry.items.AbstractEntryMenuItem;
import ui.services.menu.entry.items.AbstractEntryMenuItemCommand;
import ui.services.menu.entry.items.photo.goTo.PhotoMenuItemGoToAuthorPhotoByGenre;
import core.general.user.User;
import core.services.entry.GenreService;
import core.services.photo.PhotoService;
import core.services.system.ServicesImpl;
import menuItems.photo.AbstractPhotoMenuItemTest_;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PhotoMenuItemGoToAuthorPhotoByGenreTest extends AbstractPhotoMenuItemTest_ {

	@Test
	public void commandTest() {
		final User accessor = new User();
		final GoToParameters parameters = new GoToParameters( testData.getAccessor(), 7 );

		final ServicesImpl services = getServicesGoTo( parameters );

		final PhotoMenuItemGoToAuthorPhotoByGenre menuItem = new PhotoMenuItemGoToAuthorPhotoByGenre( testData.getPhoto(), parameters.getAccessor(), services );
		final AbstractEntryMenuItemCommand command = menuItem.getMenuItemCommand();

		final User photoAuthor = testData.getPhotoAuthor();
		final Genre genre = testData.getGenre();

		assertEquals( EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuText(), translated( String.format( "PhotoMenuItem: %s: photos in category '%s' ( %s )", photoAuthor.getNameEscaped(), translatorService.translateGenre( testData.getGenre(), AbstractTestCase.MENU_LANGUAGE ), parameters.getPhotosQty() ) ) );

		assertEquals( EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuCommand(), String.format( "goToMemberPhotosByGenre( %d, %d );", photoAuthor.getId(), genre.getId() ) );

		assertEquals( EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, menuItem.getMenuCssClass(), AbstractEntryMenuItem.MENU_ITEM_CSS_CLASS );
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

		EasyMock.expect( photoService.getPhotosCountByUserAndGenre( testData.getPhotoAuthor().getId(), testData.getPhoto().getGenreId() ) ).andReturn( parameters.getPhotosQty() ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( photoService );

		return photoService;
	}
}
