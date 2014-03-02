package controllers.photos.groupoperations;

import controllers.photos.groupoperations.handlers.AbstractGroupOperationHandler;
import core.general.photo.group.PhotoGroupOperationType;
import core.services.pageTitle.PageTitlePhotoUtilsService;
import core.services.security.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@SessionAttributes( PhotoGroupOperationController.MODEL_NAME )
@Controller
@RequestMapping( "photos/groupOperations" )
public class PhotoGroupOperationController {

	public static final String MODEL_NAME = "photoGroupOperationModel";

	private static final String VIEW = "photos/groupoperations/GroupOperations";
	private static final String VIEW_DONE = "photos/groupoperations/GroupOperationsDone";
	private static final String VIEW_ERROR = "photos/groupoperations/GroupOperationsError";

	@Autowired
	private PhotoGroupOperationValidator photoGroupOperationValidator;
	
	@Autowired
	private PageTitlePhotoUtilsService pageTitlePhotoUtilsService;

	@Autowired
	private Services services;

	@InitBinder
	protected void initBinder( final WebDataBinder binder ) {
		binder.setValidator( photoGroupOperationValidator );
	}

	@ModelAttribute( MODEL_NAME )
	public PhotoGroupOperationModel prepareModel() {
		return new PhotoGroupOperationModel();
	}

	@RequestMapping( method = RequestMethod.POST, value = "/" )
	public String showSelectedPhotos( final @ModelAttribute( MODEL_NAME ) PhotoGroupOperationModel model, final HttpServletRequest request ) {

		model.clear();

		final String groupOperationId = model.getPhotoGroupOperationId();

		final BindingResult result = new BindException( model, MODEL_NAME );
		photoGroupOperationValidator.validateGroupOperationSelected( model, result );
		photoGroupOperationValidator.validateAtLeastOnePhotoSelected( model, result );
		model.setBindingResult( result );

		if ( result.hasErrors() ) {
			model.setPageTitleData( pageTitlePhotoUtilsService.getPhotoGroupOperationErrorTitleData() );
			return VIEW_ERROR;
		}

		final PhotoGroupOperationType groupOperationType = PhotoGroupOperationType.getById( Integer.parseInt( groupOperationId ) );
		model.setGroupOperationType( groupOperationType );

		model.setReturnUrl( request.getHeader( "referer" ) );

		model.setPageTitleData( pageTitlePhotoUtilsService.getPhotoGroupOperationTitleData( groupOperationType ) );

		final AbstractGroupOperationHandler groupOperationHandler = AbstractGroupOperationHandler.getInstance( groupOperationType, model, services );
		groupOperationHandler.fillModel();

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.POST, value = "/confirm/" )
	public String addToAlbum( @Valid final @ModelAttribute( MODEL_NAME ) PhotoGroupOperationModel model, final BindingResult result ) {

		model.setBindingResult( result );
		if ( result.hasErrors() ) {
			model.setPageTitleData( pageTitlePhotoUtilsService.getPhotoGroupOperationErrorTitleData() );
			return VIEW;
		}

		final AbstractGroupOperationHandler groupOperationHandler = AbstractGroupOperationHandler.getInstance( model.getPhotoGroupOperationType(), model, services );
		model.setOperationResults( groupOperationHandler.performGroupOperations() );

		return VIEW_DONE;
	}
}
