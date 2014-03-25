package admin.controllers.genres.translations;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping( "genres/translations" )
public class GenresTranslationsController {

	public static final String MODEL_NAME = "genresTranslationsModel";
	public static final String VIEW = "/admin/genres/translations/GenresTranslations";

	@ModelAttribute( MODEL_NAME )
	public GenresTranslationsModel prepareModel() {
		return new GenresTranslationsModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String showGenreList( final @ModelAttribute( MODEL_NAME ) GenresTranslationsModel model ) {
		return VIEW;
	}
}
