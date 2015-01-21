package ui.controllers.comment.view;

import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.photo.PhotoCommentArchService;
import core.services.photo.PhotoCommentService;
import core.services.photo.PhotoService;
import core.services.security.SecurityService;
import core.services.utils.DateUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ui.context.EnvironmentContext;
import ui.controllers.comment.edit.PhotoCommentInfo;

@Controller
@RequestMapping( "photo/{photoId}/comment/{commentId}" )
public class PhotoCommentViewController {

	private static final String MODEL_NAME = "photoCommentViewModel";

	private static final String VIEW = "comments/view/PhotoComment";

	@Autowired
	private PhotoCommentService photoCommentService;

	@Autowired
	private PhotoCommentArchService photoCommentArchService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private PhotoService photoService;

	@ModelAttribute( MODEL_NAME )
	public PhotoCommentViewModel prepareModel( final @PathVariable( "photoId" ) int photoId, final @PathVariable( "commentId" ) int commentId ) {

		final Photo photo = photoService.load( photoId );

		final PhotoComment photoComment = getPhotoCommentService( photo ).load( commentId );
		final PhotoCommentInfo photoCommentInfo = loadRootCommentChildren( photo, photoComment );

		return new PhotoCommentViewModel( photoCommentInfo );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String photoCard( final @ModelAttribute( MODEL_NAME ) PhotoCommentViewModel model ) {
		return VIEW;
	}

	private PhotoCommentInfo loadRootCommentChildren( final Photo photo, final PhotoComment rootComment ) {
		final PhotoCommentInfo child = getPhotoCommentService( photo ).getPhotoCommentInfoWithChild( rootComment, EnvironmentContext.getCurrentUser() );

		if ( ! photo.isArchived() ) {
			markCommentAsReadIfNecessary( photo, rootComment );
		}

		return child;
	}

	private void markCommentAsReadIfNecessary( final Photo photo, final PhotoComment comment ) {
		final User currentUser = EnvironmentContext.getCurrentUser();
		if ( dateUtilsService.isEmptyTime(  comment.getReadTime() ) && securityService.userOwnThePhoto( currentUser, photo ) ) {
			getPhotoCommentService( photo ).setCommentReadTime( comment.getId(), dateUtilsService.getCurrentTime() );
		}
	}

	private PhotoCommentService getPhotoCommentService( final Photo photo ) {
		return photo.isArchived() ? photoCommentArchService : photoCommentService;
	}
}
