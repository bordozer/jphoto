package menuItems.photo;

import core.services.entry.GenreService;
import core.services.photo.PhotoService;
import org.easymock.EasyMock;

public class PhotoMenuItemGoToAuthorPhotosTest extends AbstractPhotoMenuItemTest_ {

	private GenreService getGenreService() {
		final GenreService genreService = EasyMock.createMock( GenreService.class );

		EasyMock.expect( genreService.load( testData.getPhoto().getGenreId() ) ).andReturn( testData.getGenre() ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( genreService );

		return genreService;
	}

	private PhotoService getPhotoService( final int genrePhotosQty ) {
		final PhotoService photoService = EasyMock.createMock( PhotoService.class );

		EasyMock.expect( photoService.getPhotoQtyByUser( testData.getPhotoAuthor().getId() ) ).andReturn( genrePhotosQty ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( photoService );

		return photoService;
	}
}
