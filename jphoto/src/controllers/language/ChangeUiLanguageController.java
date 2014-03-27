package controllers.language;

import core.context.EnvironmentContext;
import core.services.translator.Language;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping( value = "language" )
public class ChangeUiLanguageController {

	@RequestMapping( method = RequestMethod.GET, value = "{languageCode}/" )
	@ResponseStatus( value = HttpStatus.OK )
	public void commentsToUser( final @PathVariable( "languageCode" ) String languageCode ) {
		final Language language = Language.getByCode( languageCode );

		EnvironmentContext.getCurrentUser().setLanguage( language );
	}
}
