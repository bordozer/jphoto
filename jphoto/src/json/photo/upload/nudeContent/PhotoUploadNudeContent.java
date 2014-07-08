package json.photo.upload.nudeContent;

import core.general.genre.Genre;
import core.services.entry.GenreService;
import core.services.translator.TranslatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ui.context.EnvironmentContext;

@Controller
@RequestMapping( "genres/photo-upload-nude-content/{genreId}/" )
public class PhotoUploadNudeContent {

	@Autowired
	private GenreService genreService;

	@Autowired
	private TranslatorService translatorService;

	@RequestMapping( method = RequestMethod.GET, value = "/", produces = "application/json" )
	@ResponseBody
	public PhotoUploadNudeContentDTO photoUploadNudeContent( final @PathVariable( "genreId" ) int genreId ) {

		final Genre genre = genreService.load( genreId );

		final PhotoUploadNudeContentDTO dto = new PhotoUploadNudeContentDTO();

		dto.setCanContainsNude( genre.isCanContainNudeContent() );
		dto.setContainsNude( genre.isContainsNudeContent() );

		dto.setYesTranslated( translatorService.translate( "Photo edit: Category always contains nude", EnvironmentContext.getLanguage() ) );
		dto.setNoTranslated( translatorService.translate( "Photo edit: Category can not contains nude", EnvironmentContext.getLanguage() ) );

		return dto;
	}
}
