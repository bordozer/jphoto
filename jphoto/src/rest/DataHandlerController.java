package rest;

import core.general.genre.Genre;
import core.general.photo.Photo;
import core.services.conversion.PreviewGenerationService;
import core.services.entry.GenreService;
import core.services.photo.PhotoService;
import core.services.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ui.context.EnvironmentContext;

import java.io.IOException;

@RequestMapping( "" )
@Controller
public class DataHandlerController {

	@Autowired
	private PhotoService photoService;

	@Autowired
	private GenreService genreService;

	@Autowired
	private PreviewGenerationService previewGenerationService;

	@Autowired
	private SecurityService securityService;

	@RequestMapping( method = RequestMethod.GET, value = "photos/{photoId}/nude-content/{isNudeContent}/", produces = "application/json" )
	@ResponseBody
	public boolean setPhotoNudeContext( final @PathVariable( "photoId" ) int photoId, final @PathVariable( "isNudeContent" ) boolean isNudeContent ) {

		final Photo photo = photoService.load( photoId );

		assertUserCanEditPhoto( photo );

		photo.setContainsNudeContent( isNudeContent );

		return photoService.save( photo );
	}

	@RequestMapping( method = RequestMethod.GET, value = "photos/{photoId}/preview/", produces = "application/json" )
	@ResponseBody
	public boolean generatePreview( final @PathVariable( "photoId" ) int photoId ) throws IOException, InterruptedException {

		assertSuperAdminAccess();

		return previewGenerationService.generatePreviewSync( photoId ) != null;
	}

	@RequestMapping( method = RequestMethod.DELETE, value = "photos/{photoId}/" )
	@ResponseBody
	public boolean deletePhoto( final @PathVariable( "photoId" ) int photoId ) throws IOException, InterruptedException {

		final Photo photo = photoService.load( photoId );

		securityService.assertUserCanDeletePhoto( EnvironmentContext.getCurrentUser(), photo );

		return photoService.delete( photoId );
	}

	@RequestMapping( method = RequestMethod.GET, value = "photos/{photoId}/move-to-genre/{genreId}/", produces = "application/json" )
	@ResponseBody
	public boolean movePhotoToGenrePreview( final @PathVariable( "photoId" ) int photoId, final @PathVariable( "genreId" ) int genreId ) {

		final Photo photo = photoService.load( photoId );

		assertUserCanEditPhoto( photo );

		if ( ! photoService.movePhotoToGenreWithNotification( photoId, genreId, EnvironmentContext.getCurrentUser() ) ) {
			return false;
		}

		final Genre genre = genreService.load( genreId );
		photo.setContainsNudeContent( genre.isContainsNudeContent() || ( photo.isContainsNudeContent() && genre.isCanContainNudeContent() ) );

		return photoService.save( photo );
	}

	private void assertSuperAdminAccess() {
		securityService.assertSuperAdminAccess( EnvironmentContext.getCurrentUser() );
	}

	private void assertUserCanEditPhoto( final Photo photo ) {
		securityService.assertUserCanEditPhoto( EnvironmentContext.getCurrentUser(), photo );
	}
}
