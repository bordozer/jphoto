package ui.controllers.photos.edit;

import core.general.photo.Photo;
import core.services.photo.PhotoService;
import core.services.security.SecurityService;
import core.services.utils.UrlUtilsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ui.context.EnvironmentContext;
import ui.services.breadcrumbs.BreadcrumbsPhotoService;
import utils.NumberUtils;

@Controller
@RequestMapping( UrlUtilsServiceImpl.PHOTOS_URL )
public class PhotoEditDataController {

	public static final String MODEL_NAME = "photoEditDataModel";

	private static final String VIEW = "photos/edit/PhotoEditData";

	private static final String ONLY_LOGGED_USER_CAN_UPLOAD_A_PHOTO_MESSAGE = "Only logged user can upload a photo";

	@Autowired
	private SecurityService securityService;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private BreadcrumbsPhotoService breadcrumbsPhotoService;

	@ModelAttribute( MODEL_NAME )
	public PhotoEditDataModel prepareModel() {
		final PhotoEditDataModel model = new PhotoEditDataModel();

//		model.setDataRequirementService( dataRequirementService );

		return model;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/new/" )
	public String newPhoto( final @ModelAttribute( MODEL_NAME ) PhotoEditDataModel model ) {

		model.setNew( true );

		model.setPageTitleData( breadcrumbsPhotoService.getUploadPhotoBreadcrumbs( EnvironmentContext.getCurrentUser() ) );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{photoId}/edit/" )
	public String editPhoto( final @PathVariable( "photoId" ) String _photoId, final @ModelAttribute( MODEL_NAME ) PhotoEditDataModel model ) {

		assertPhotoExistsAndCurrentUserCanEditIt( _photoId );

		final int photoId = NumberUtils.convertToInt( _photoId );

		final Photo photo = photoService.load( photoId );

		model.setNew( false );
		model.setPhoto( photo );

		model.setPageTitleData( breadcrumbsPhotoService.getPhotoEditDataBreadcrumbs( photo ) );

		return VIEW;
	}

	private void assertPhotoExistsAndCurrentUserCanEditIt( final String _photoId ) {

		securityService.assertPhotoExists( _photoId );

		final int photoId = NumberUtils.convertToInt( _photoId );
		final Photo photo = photoService.load( photoId );

		securityService.assertUserCanEditPhoto( EnvironmentContext.getCurrentUser(), photo );
	}
}
