package rest.photo.upload.nudeContent;

import core.general.genre.Genre;
import core.general.photo.Photo;
import core.services.entry.GenreService;
import core.services.photo.PhotoService;
import core.services.translator.TranslatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ui.context.EnvironmentContext;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "photo-upload-nude-content/genre/{genreId}/photo/{photoId}/" )
public class PhotoUploadNudeContent {

	@Autowired
	private GenreService genreService;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private TranslatorService translatorService;

	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	@ResponseBody
	public PhotoUploadNudeContentDTO photoUploadNudeContent( final @PathVariable( "genreId" ) int genreId, final @PathVariable( "photoId" ) int photoId ) {

		final Genre genre = genreService.load( genreId );

		final PhotoUploadNudeContentDTO dto = new PhotoUploadNudeContentDTO();

		dto.setGenreCanContainsNude( genre.isCanContainNudeContent() );
		dto.setGenreObviouslyContainsNude( genre.isContainsNudeContent() );

		dto.setYesTranslated( translatorService.translate( "Photo edit: Category always contains nude", EnvironmentContext.getLanguage() ) );
		dto.setNoTranslated( translatorService.translate( "Photo edit: Category can not contains nude", EnvironmentContext.getLanguage() ) );

		if ( photoId > 0 ) {
			final Photo photo = photoService.load( photoId );
			dto.setPhotoContainsNude( photo.isContainsNudeContent() );
		}

		return dto;
	}

	@RequestMapping( method = RequestMethod.POST, value = "/", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	@ResponseBody
	public PhotoUploadNudeContentDTO updatePhotoUploadNudeContent( @RequestBody final PhotoUploadNudeContentDTO dto, final @PathVariable( "genreId" ) int genreId, final @PathVariable( "photoId" ) int photoId ) {
		return dto;
	}
}
