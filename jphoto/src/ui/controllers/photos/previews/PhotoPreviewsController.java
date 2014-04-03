package ui.controllers.photos.previews;

import core.context.EnvironmentContext;
import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.conversion.PhotoPreviewService;
import core.services.entry.GenreService;
import ui.services.breadcrumbs.BreadcrumbsPhotoService;
import core.services.photo.PhotoService;
import core.services.security.SecurityService;
import core.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import utils.NumberUtils;

@Controller
@RequestMapping( value = "photos/{photoId}/previews/" )
public class PhotoPreviewsController {

	private static final String MODEL_NAME = "photoPreviewsModel";
	private static final String VIEW = "photos/previews/PreviewList";

	@Autowired
	private UserService userService;

	@Autowired
	private GenreService genreService;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private PhotoPreviewService photoPreviewService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private BreadcrumbsPhotoService breadcrumbsPhotoService;

	@ModelAttribute( MODEL_NAME )
	public PhotoPreviewsModel prepareModel() {
		return new PhotoPreviewsModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String commentsToUser( final @PathVariable( "photoId" ) String _photoId, final @ModelAttribute( MODEL_NAME ) PhotoPreviewsModel model ) {

		securityService.assertPhotoExists( _photoId );

		final int photoId = NumberUtils.convertToInt( _photoId );

		final Photo photo = photoService.load( photoId );

		model.setPhoto( photo );

		model.setPhotoPreviews( photoPreviewService.getPreviews( photoId ) );

		final User user = userService.load( photo.getUserId() );
		final Genre genre = genreService.load( photo.getGenreId() );

		model.setPhotoAuthor( user );
		model.setGenre( genre );

		model.setPhotoPreviewWrapper( photoService.getPhotoPreviewWrapper( photo, EnvironmentContext.getCurrentUser() ) );

		model.setPageTitleData( breadcrumbsPhotoService.getUserPhotoPreviewsBreadcrumbs( photo, EnvironmentContext.getCurrentUser() ) );

		return VIEW;
	}
}
