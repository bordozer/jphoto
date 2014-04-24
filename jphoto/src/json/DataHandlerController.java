package json;

import core.general.photo.Photo;
import core.services.conversion.PreviewGenerationService;
import core.services.photo.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@RequestMapping( "" )
@Controller
public class DataHandlerController {

	@Autowired
	private PhotoService photoService;

	@Autowired
	private PreviewGenerationService previewGenerationService;

	@RequestMapping( method = RequestMethod.GET, value = "photos/{photoId}/nude-content/{isNudeContent}/", produces = "application/json" )
	@ResponseBody
	public boolean setPhotoNudeContext( final @PathVariable( "photoId" ) int photoId, final @PathVariable( "isNudeContent" ) boolean isNudeContent ) {

		final Photo photo = photoService.load( photoId );
		photo.setContainsNudeContent( isNudeContent );

		return photoService.save( photo );
	}

	@RequestMapping( method = RequestMethod.GET, value = "photos/{photoId}/preview/", produces = "application/json" )
	@ResponseBody
	public boolean generatePreview( final @PathVariable( "photoId" ) int photoId ) throws IOException, InterruptedException {

		previewGenerationService.generatePreview( photoId );

		return true;
	}
}
