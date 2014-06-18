package menuItems.comment.goTo;

import common.AbstractTestCase;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.comment.goTo.CommentMenuItemGoToAuthorPhotoByGenre;
import core.general.user.User;
import core.services.entry.GenreService;
import core.services.photo.PhotoService;
import core.services.system.ServicesImpl;
import menuItems.comment.AbstractCommentMenuItemTest_;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CommentMenuItemGoToAuthorPhotoByGenreTest extends AbstractCommentMenuItemTest_ {

	@Test
	public void photoQty_Is_getPhotoQtyByUserAndGenre_method1Test() {
		final int photosQty = 0;
		assertEquals( EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, getMenuEntry( new Parameters( testData.getAccessor(), photosQty ) ).getPhotoQty(), photosQty );
	}

	@Test
	public void photoQty_Is_getPhotoQtyByUserAndGenre_method2Test() {
		final int photosQty = 7;
		assertEquals( EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, getMenuEntry( new Parameters( testData.getAccessor(), photosQty ) ).getPhotoQty(), photosQty );
	}

	@Test
	public void commandTest() {
		final int photosQty = 4;

		final CommentMenuItemGoToAuthorPhotoByGenre menuEntry = getMenuEntry( new Parameters( testData.getAccessor(), photosQty ) );
		final AbstractEntryMenuItemCommand menuItemCommand = menuEntry.getMenuItemCommand();

		assertEquals( EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, menuItemCommand.getMenuText(), translated( String.format( "PhotoMenuItem: %s: photos in category '%s' ( %s )", testData.getCommentAuthor().getNameEscaped(), translatorService.translateGenre( testData.getGenre(), AbstractTestCase.MENU_LANGUAGE ), photosQty ) ) );
		assertEquals( EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, menuItemCommand.getMenuCommand(), String.format( "goToMemberPhotosByGenre( %d, %d );", testData.getCommentAuthor().getId(), testData.getGenre().getId() ) );
	}

	private CommentMenuItemGoToAuthorPhotoByGenre getMenuEntry( final Parameters parameters ) {
		return new CommentMenuItemGoToAuthorPhotoByGenre( testData.getComment(), parameters.getAccessor(), getServicesForTest( parameters ) );
	}

	private ServicesImpl getServicesForTest( final Parameters parameters ) {
		final ServicesImpl services = getServices( parameters.getAccessor() );

		services.setGenreService( getGenreServices() );
		services.setPhotoService( getPhotoServiceForTest( parameters ) );

		return services;
	}

	private GenreService getGenreServices() {
		final GenreService genreService = EasyMock.createMock( GenreService.class );

		EasyMock.expect( genreService.load( testData.getPhoto().getGenreId() ) ).andReturn( testData.getGenre() ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( genreService );

		return genreService;
	}

	private PhotoService getPhotoServiceForTest( final Parameters parameters ) {
		final PhotoService photoService = EasyMock.createMock( PhotoService.class );

		EasyMock.expect( photoService.load( testData.getPhoto().getId() ) ).andReturn( testData.getPhoto() ).anyTimes();
		EasyMock.expect( photoService.getPhotoQtyByUserAndGenre( testData.getCommentAuthor().getId(), testData.getGenre().getId() ) ).andReturn( parameters.getPhotosQty() ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( photoService );

		return photoService;
	}

	private class Parameters {

		private final User accessor;
		private final int photosQty;

		public Parameters( final User accessor, final int photosQty ) {
			this.accessor = accessor;
			this.photosQty = photosQty;
		}

		public User getAccessor() {
			return accessor;
		}

		public int getPhotosQty() {
			return photosQty;
		}
	}
}
