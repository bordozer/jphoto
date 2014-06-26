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

import java.util.*;

import static com.google.common.collect.Maps.newHashMap;

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
		return getView( model, TranslationMode.TRANSLATED, TranslationMapLoadStrategy.getTranslatedMapLoader( translatorService ) );
	}

	@RequestMapping(method = RequestMethod.GET, value = "/translated/")
	public String getTranslated( final @ModelAttribute(MODEL_NAME) TranslatorModel model ) {
		return getView( model, TranslationMode.TRANSLATED, TranslationMapLoadStrategy.getTranslatedMapLoader( translatorService ) );
	}

	@RequestMapping(method = RequestMethod.GET, value = "translated//language/{languageCode}/")
	public String getTranslated( final @PathVariable( "languageCode" ) String languageCode, final @ModelAttribute(MODEL_NAME) TranslatorModel model ) {
		return getView( model, TranslationMode.TRANSLATED, TranslationMapLoadStrategy.getTranslatedMapLoader( translatorService ).filter( Language.getByCode( languageCode ) ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "translated//letter/{letter}/" )
	public String getTranslated1( final @PathVariable( "letter" ) String letter, final @ModelAttribute( MODEL_NAME ) TranslatorModel model ) {
		return getView( model, TranslationMode.TRANSLATED, TranslationMapLoadStrategy.getTranslatedMapLoader( translatorService ).filter( letter ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "translated//language/{languageCode}/letter/{letter}/" )
	public String getTranslated( final @PathVariable( "languageCode" ) String languageCode, final @PathVariable( "letter" ) String letter, final @ModelAttribute( MODEL_NAME ) TranslatorModel model ) {
		return getView( model, TranslationMode.TRANSLATED, TranslationMapLoadStrategy.getTranslatedMapLoader( translatorService ).filter( letter ).filter( Language.getByCode( languageCode ) ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/untranslated/" )
	public String getUntranslated( final @ModelAttribute( MODEL_NAME ) TranslatorModel model ) {
		return getView( model, TranslationMode.UNTRANSLATED, TranslationMapLoadStrategy.getUntranslatedMapLoader( translatorService ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/untranslated/language/{languageCode}/" )
	public String getUntranslated( final @PathVariable( "languageCode" ) String languageCode, final @ModelAttribute( MODEL_NAME ) TranslatorModel model ) {
		return getView( model, TranslationMode.UNTRANSLATED, TranslationMapLoadStrategy.getUntranslatedMapLoader( translatorService ).filter( Language.getByCode( languageCode ) ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/untranslated/letter/{letter}/" )
	public String getUntranslated1( final @PathVariable( "letter" ) String letter, final @ModelAttribute( MODEL_NAME ) TranslatorModel model ) {
		return getView( model, TranslationMode.UNTRANSLATED, TranslationMapLoadStrategy.getUntranslatedMapLoader( translatorService ).filter( letter ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/untranslated/language/{languageCode}/letter/{letter}/" )
	public String getUntranslated( final @PathVariable( "languageCode" ) String languageCode, final @PathVariable( "letter" ) String letter, final @ModelAttribute( MODEL_NAME ) TranslatorModel model ) {
		return getView( model, TranslationMode.UNTRANSLATED, TranslationMapLoadStrategy.getUntranslatedMapLoader( translatorService ).filter( letter ).filter( Language.getByCode( languageCode ) ) );
	}

	private String getView( final TranslatorModel model, final TranslationMode mode, final TranslationMapLoadStrategy loader ) {
		model.setTranslationsMap( loader.getTranslationMap() );

		model.setLetters( loader.getLetters() );
		model.setTranslationMode( mode );

		model.setPageTitleData( breadcrumbsAdminService.getTranslatorBreadcrumbs() );

		return VIEW;
	}

	/*@RequestMapping(method = RequestMethod.GET, value = "/")
	public String getTranslated( final @ModelAttribute(MODEL_NAME) TranslatorModel model ) {

		final TranslationMapLoader mapLoader = TranslationMapLoader.getInstance( TranslationMode.TRANSLATED, translatorService );

		final Map<NerdKey, TranslationData> translationsMap = mapLoader.getTranslatedSortedMap();
		model.setLetters( getLetters( translationsMap ) );
		model.setTranslationsMap( translationsMap );

		model.setTranslationMode( TranslationMode.TRANSLATED );

		model.setPageTitleData( breadcrumbsAdminService.getTranslatorBreadcrumbs() );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{letter}/" )
	public String getTranslatedByFirstLetter( final @PathVariable( "letter" ) String letter, final @ModelAttribute( MODEL_NAME ) TranslatorModel model ) {

		final TranslationMapLoader mapLoader = TranslationMapLoader.getInstance( TranslationMode.TRANSLATED, translatorService );

		final Map<NerdKey, TranslationData> translatedMap = mapLoader.getTranslatedSortedMap();

		model.setLetters( getLetters( translatedMap ) );
		model.setTranslationsMap( filterByFirstLetter( translatedMap, letter ) );

		model.setFilterByLetter( letter );
		model.setTranslationMode( TranslationMode.TRANSLATED );

		model.setPageTitleData( breadcrumbsAdminService.getTranslatorBreadcrumbs() );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/untranslated/" )
	public String getUntranslated( final @ModelAttribute( MODEL_NAME ) TranslatorModel model ) {

		final TranslationMapLoader mapLoader = TranslationMapLoader.getInstance( TranslationMode.UNTRANSLATED, translatorService );

		final Map<NerdKey, TranslationData> untranslatedMap = mapLoader.getUntranslatedSortedMap();

		model.setLetters( getLetters( untranslatedMap ) );
		model.setTranslationsMap( untranslatedMap );

		model.setTranslationMode( TranslationMode.UNTRANSLATED );

		model.setUrlPrefix( "untranslated" );

		model.setPageTitleData( breadcrumbsAdminService.getTranslatorBreadcrumbs() );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/untranslated/{languageCode}/" )
	public String getUntranslated_Language( final @PathVariable( "languageCode" ) String languageCode, final @ModelAttribute( MODEL_NAME ) TranslatorModel model ) {

		final TranslationMapLoader mapLoader = TranslationMapLoader.getInstance( TranslationMode.UNTRANSLATED, translatorService );

		final Map<NerdKey, TranslationData> untranslatedMap = mapLoader.getUntranslatedSortedMap();

		model.setLetters( getLetters( untranslatedMap ) );
		model.setTranslationsMap( untranslatedMap );

		model.setLanguage( Language.getByCode( languageCode ) );
		model.setTranslationMode( TranslationMode.UNTRANSLATED );

		model.setUrlPrefix( "untranslated" );

		model.setPageTitleData( breadcrumbsAdminService.getTranslatorBreadcrumbs() );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/untranslated/{letter}/" )
	public String getUntranslatedByFirstLetter( final @PathVariable( "letter" ) String letter, final @ModelAttribute( MODEL_NAME ) TranslatorModel model ) {

		final TranslationMapLoader mapLoader = TranslationMapLoader.getInstance( TranslationMode.UNTRANSLATED, translatorService );

		final Map<NerdKey, TranslationData> untranslatedMap = mapLoader.getUntranslatedSortedMap();

		model.setLetters( getLetters( untranslatedMap ) );
		model.setTranslationsMap( filterByFirstLetter( untranslatedMap, letter ) );

		model.setFilterByLetter( letter );
		model.setTranslationMode( TranslationMode.UNTRANSLATED );

		model.setUrlPrefix( "untranslated" );

		model.setPageTitleData( breadcrumbsAdminService.getTranslatorBreadcrumbs() );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/untranslated/{languageCode}/{letter}/" )
	public String getUntranslated_LanguageByFirstLetter( final @PathVariable( "languageCode" ) String languageCode , final @PathVariable( "letter" ) String letter, final @ModelAttribute( MODEL_NAME ) TranslatorModel model ) {
		return VIEW;
	}*/

	@RequestMapping( method = RequestMethod.GET, value = "/reload/" )
	public void reloadTranslationsAjax() throws DocumentException {
		translatorService.reloadTranslations();
	}
}
