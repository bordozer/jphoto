package rest.photo.list.bigPreview;

import core.general.photo.Photo;
import core.general.user.User;
import core.services.translator.Language;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import rest.photo.list.AbstractPhotoListEntryController;
import ui.context.EnvironmentContext;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping( "photos/{photoId}" )
@Controller
public class PhotoListBigPreviewEntryController extends AbstractPhotoListEntryController {

	@RequestMapping( method = RequestMethod.GET, value = "/big-preview/", produces = APPLICATION_JSON_VALUE )
	@ResponseBody
	public PhotoBigEntryDTO photoListEntry( final @PathVariable( "photoId" ) int photoId ) {

		final Photo photo = photoService.load( photoId );
		final User currentUser = EnvironmentContext.getCurrentUser();
		final Language language = getLanguage();
//		final boolean doesPreviewHasToBeHidden = securityService.isPhotoAuthorNameMustBeHidden( photo, currentUser );

		final PhotoBigEntryDTO dto = new PhotoBigEntryDTO();

		dto.setPhotoName( photo.getName() ); // TODO: escaping!
		dto.setPhotoImage( getPhotoPreview( photo, currentUser, false, language, userPhotoFilePathUtilsService.getPhotoImageUrl( photo ) ) );
		dto.setPhotoUploadDate( getPhotoUploadDate( photo, language ) );
		dto.setPhotoCategory( getPhotoCategory( photo.getGenreId(), language ) );
		dto.setPhotoLink( entityLinkUtilsService.getPhotoCardLink( photo, language ) );
		dto.setPhotoCardLink( urlUtilsService.getPhotoCardLink( photo.getId() ) );
		dto.setPhotoDescription( photo.getDescription() ); // TODO: escaping!

		final User photoAuthor = userService.load( photo.getUserId() );
		dto.setPhotoAuthorLink( entityLinkUtilsService.getUserCardLink( photoAuthor, language ) );

		return dto;
	}

}
