package json.photo;

import core.general.photo.Photo;
import core.services.photo.PhotoService;
import core.services.utils.UserPhotoFilePathUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping( "photos/{photoId}" )
@Controller
public class PhotoEntryController {

	@Autowired
	private PhotoService photoService;

	@Autowired
	private UserPhotoFilePathUtilsService userPhotoFilePathUtilsService;

	@RequestMapping( method = RequestMethod.GET, value = "/", produces = "application/json" )
	@ResponseBody
	public PhotoEntryDTO userCardVotingAreas( final @PathVariable( "photoId" ) int photoId ) {

		final Photo photo = photoService.load( photoId );

		final PhotoEntryDTO photoEntry = new PhotoEntryDTO( photoId );

		photoEntry.setName( photo.getNameEscaped() );
		photoEntry.setPhotoImageUrl( userPhotoFilePathUtilsService.getPhotoPreviewUrl( photo ) );


		return photoEntry;
	}
}
