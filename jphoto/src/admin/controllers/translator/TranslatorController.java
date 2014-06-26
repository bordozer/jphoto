package admin.controllers.translator;

import core.services.translator.*;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ui.services.breadcrumbs.BreadcrumbsAdminService;

@Controller
@RequestMapping("translator")
public class TranslatorController {

	private static final String MODEL_NAME = "translatorModel";
	private static final String VIEW = "admin/translator/Translator";

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private BreadcrumbsAdminService breadcrumbsAdminService;

	@ModelAttribute(MODEL_NAME)
	public TranslatorModel prepareModel() {
		return new TranslatorModel();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/")
	public String getTranslatedRoot( final @ModelAttribute(MODEL_NAME) TranslatorModel model ) {
		return getView( model, TranslationMode.TRANSLATED, TranslationMapLoadStrategy.getTranslatedMapLoadStrategy( translatorService ) );
	}

	@RequestMapping(method = RequestMethod.GET, value = "/translated/")
	public String getTranslated( final @ModelAttribute(MODEL_NAME) TranslatorModel model ) {
		return getView( model, TranslationMode.TRANSLATED, TranslationMapLoadStrategy.getTranslatedMapLoadStrategy( translatorService ) );
	}

	@RequestMapping(method = RequestMethod.GET, value = "translated/language/{languageCode}/")
	public String getTranslated( final @PathVariable( "languageCode" ) String languageCode, final @ModelAttribute(MODEL_NAME) TranslatorModel model ) {
		final Language language = Language.getByCode( languageCode );
		model.setLanguage( language );
		return getView( model, TranslationMode.TRANSLATED, TranslationMapLoadStrategy.getTranslatedMapLoadStrategy( translatorService ).filter( language ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "translated/letter/{letter}/" )
	public String getTranslated1( final @PathVariable( "letter" ) String letter, final @ModelAttribute( MODEL_NAME ) TranslatorModel model ) {
		model.setFilterByLetter( letter );
		return getView( model, TranslationMode.TRANSLATED, TranslationMapLoadStrategy.getTranslatedMapLoadStrategy( translatorService ).filter( letter ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "translated/language/{languageCode}/letter/{letter}/" )
	public String getTranslated( final @PathVariable( "languageCode" ) String languageCode, final @PathVariable( "letter" ) String letter, final @ModelAttribute( MODEL_NAME ) TranslatorModel model ) {
		final Language language = Language.getByCode( languageCode );
		model.setLanguage( language );
		model.setFilterByLetter( letter );
		return getView( model, TranslationMode.TRANSLATED, TranslationMapLoadStrategy.getTranslatedMapLoadStrategy( translatorService ).filter( letter ).filter( language ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/untranslated/" )
	public String getUntranslated( final @ModelAttribute( MODEL_NAME ) TranslatorModel model ) {
		return getView( model, TranslationMode.UNTRANSLATED, TranslationMapLoadStrategy.getUntranslatedMapLoadStrategy( translatorService ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/untranslated/language/{languageCode}/" )
	public String getUntranslated( final @PathVariable( "languageCode" ) String languageCode, final @ModelAttribute( MODEL_NAME ) TranslatorModel model ) {
		final Language language = Language.getByCode( languageCode );
		model.setLanguage( language );
		return getView( model, TranslationMode.UNTRANSLATED, TranslationMapLoadStrategy.getUntranslatedMapLoadStrategy( translatorService ).filter( language ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/untranslated/letter/{letter}/" )
	public String getUntranslated1( final @PathVariable( "letter" ) String letter, final @ModelAttribute( MODEL_NAME ) TranslatorModel model ) {
		model.setFilterByLetter( letter );
		return getView( model, TranslationMode.UNTRANSLATED, TranslationMapLoadStrategy.getUntranslatedMapLoadStrategy( translatorService ).filter( letter ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/untranslated/language/{languageCode}/letter/{letter}/" )
	public String getUntranslated( final @PathVariable( "languageCode" ) String languageCode, final @PathVariable( "letter" ) String letter, final @ModelAttribute( MODEL_NAME ) TranslatorModel model ) {
		final Language language = Language.getByCode( languageCode );
		model.setLanguage( language );
		model.setFilterByLetter( letter );
		return getView( model, TranslationMode.UNTRANSLATED, TranslationMapLoadStrategy.getUntranslatedMapLoadStrategy( translatorService ).filter( letter ).filter( language ) );
	}

	private String getView( final TranslatorModel model, final TranslationMode mode, final TranslationMapLoadStrategy loader ) {
		model.setTranslationsMap( loader.getTranslationMap() );

		model.setLetters( loader.getLetters() );
		model.setTranslationMode( mode );

		model.setPageTitleData( breadcrumbsAdminService.getTranslatorBreadcrumbs() );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/reload/" )
	public void reloadTranslationsAjax() throws DocumentException {
		translatorService.reloadTranslations();
	}
}
