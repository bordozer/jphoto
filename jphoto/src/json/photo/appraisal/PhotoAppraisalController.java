package json.photo.appraisal;

import core.general.photo.Photo;
import core.general.user.User;
import core.services.photo.PhotoService;
import core.services.photo.PhotoVotingService;
import json.photo.PhotoEntryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ui.context.EnvironmentContext;

@RequestMapping( "photos/{photoId}" )
@Controller
public class PhotoAppraisalController {

	@Autowired
	private PhotoService photoService;

	@Autowired
	private PhotoVotingService photoVotingService;

	@RequestMapping( method = RequestMethod.GET, value = "/appraisal/", produces = "application/json" )
	@ResponseBody
	public PhotoAppraisalDTO userCardVotingAreas( final @PathVariable( "photoId" ) int photoId ) {

		final PhotoAppraisalDTO photoAppraisalDTO = new PhotoAppraisalDTO( photoId );

		final User currentUser = EnvironmentContext.getCurrentUser();
		final Photo photo = photoService.load( photoId );

		photoAppraisalDTO.setUserHasAlreadyAppraisedPhoto( photoVotingService.isUserVotedForPhoto( currentUser, photo ) );

		return photoAppraisalDTO;
	}
}
