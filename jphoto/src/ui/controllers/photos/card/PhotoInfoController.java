package ui.controllers.photos.card;

import core.context.EnvironmentContext;
import core.general.photo.Photo;
import core.services.photo.PhotoService;
import core.services.user.UserRankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping( "photos/{photoId}" )
public class PhotoInfoController {

	private static final String PHOTO_INFO_VIEW = "photos/PhotoInfo";

	@Autowired
	private PhotoService photoService;

	@Autowired
	private UserRankService userRankService;

	@RequestMapping( method = RequestMethod.GET, value = "/info/" )
	public String getPhotoInfo( final @PathVariable( "photoId" ) int photoId, final @ModelAttribute( "photoInfoModel" ) PhotoInfoModel model ) {
		final Photo photo = photoService.load( photoId );
		model.setPhotoInfo( photoService.getPhotoInfo( photo, EnvironmentContext.getCurrentUser() ) );
		model.setVotingModel( userRankService.getVotingModel( photo.getUserId(), photo.getGenreId(), EnvironmentContext.getCurrentUser() ) );

		return PHOTO_INFO_VIEW;
	}
}
