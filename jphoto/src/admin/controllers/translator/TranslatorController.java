package admin.controllers.translator;

import core.services.translator.*;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ui.services.breadcrumbs.BreadcrumbsAdminService;

import java.util.Map;

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

		initModel( model, TranslationMode.TRANSLATED, TranslationMapLoadStrategy.getInstance( translatorService, TranslationMode.TRANSLATED ) );

		return VIEW;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/translated/")
	public String getTranslated( final @ModelAttribute(MODEL_NAME) TranslatorModel model ) {

		initModel( model, TranslationMode.TRANSLATED, TranslationMapLoadStrategy.getInstance( translatorService, TranslationMode.TRANSLATED ) );

		return VIEW;
	}

	@RequestMapping(method = RequestMethod.GET, value = "translated/language/{languageCode}/")
	public String getTranslated( final @PathVariable( "languageCode" ) String lang, final @ModelAttribute(MODEL_NAME) TranslatorModel model ) {

		initModel( model.filter( Language.getByCode( lang ) ), TranslationMode.TRANSLATED, TranslationMapLoadStrategy.getInstance( translatorService, TranslationMode.TRANSLATED ) );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "translated/letter/{letter}/" )
	public String getTranslated1( final @PathVariable( "letter" ) String letter, final @ModelAttribute( MODEL_NAME ) TranslatorModel model ) {

		initModel( model.filter( letter ), TranslationMode.TRANSLATED, TranslationMapLoadStrategy.getInstance( translatorService, TranslationMode.TRANSLATED ) );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "translated/language/{languageCode}/letter/{letter}/" )
	public String getTranslated( final @PathVariable( "languageCode" ) String lang, final @PathVariable( "letter" ) String letter, final @ModelAttribute( MODEL_NAME ) TranslatorModel model ) {

		initModel( model.filter( Language.getByCode( lang ) ).filter( letter ), TranslationMode.TRANSLATED, TranslationMapLoadStrategy.getInstance( translatorService, TranslationMode.TRANSLATED ) );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/untranslated/" )
	public String getUntranslated( final @ModelAttribute( MODEL_NAME ) TranslatorModel model ) {

		initModel( model, TranslationMode.UNTRANSLATED, TranslationMapLoadStrategy.getInstance( translatorService, TranslationMode.UNTRANSLATED ) );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/untranslated/language/{languageCode}/" )
	public String getUntranslated( final @PathVariable( "languageCode" ) String lang, final @ModelAttribute( MODEL_NAME ) TranslatorModel model ) {

		initModel( model.filter( Language.getByCode( lang ) ), TranslationMode.UNTRANSLATED, TranslationMapLoadStrategy.getInstance( translatorService, TranslationMode.UNTRANSLATED ) );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/untranslated/letter/{letter}/" )
	public String getUntranslated1( final @PathVariable( "letter" ) String letter, final @ModelAttribute( MODEL_NAME ) TranslatorModel model ) {

		initModel( model.filter( letter ), TranslationMode.UNTRANSLATED, TranslationMapLoadStrategy.getInstance( translatorService, TranslationMode.UNTRANSLATED ) );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/untranslated/language/{languageCode}/letter/{letter}/" )
	public String getUntranslated( final @PathVariable( "languageCode" ) String lang, final @PathVariable( "letter" ) String letter, final @ModelAttribute( MODEL_NAME ) TranslatorModel model ) {

		initModel( model.filter( Language.getByCode( lang ) ).filter( letter ), TranslationMode.UNTRANSLATED, TranslationMapLoadStrategy.getInstance( translatorService, TranslationMode.UNTRANSLATED ) );

		return VIEW;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/unused/")
	public String getUnusedTranslations( final @ModelAttribute(MODEL_NAME) TranslatorModel model ) {

		initModel( model, TranslationMode.UNUSED_TRANSLATIONS, TranslationMapLoadStrategy.getInstance( translatorService, TranslationMode.TRANSLATED ).filterUnused() );

		return VIEW;
	}

	private void initModel( final TranslatorModel model, final TranslationMode mode, final TranslationMapLoadStrategy loadStrategy ) {

		final TranslationMapLoadStrategy loader = loadStrategy;

		if ( model.getLanguage() != null ) {
			loader.filter( model.getLanguage() );
		}

		if ( StringUtils.isNotEmpty( model.getFilterByLetter() ) ) {
			loader.filter( model.getFilterByLetter() );
		}

		model.setTranslationsMap( loader.getTranslationMap() );

		model.setLetters( loader.getLetters() );
		model.setTranslationMode( mode );

		final Map<TranslationMode, Integer> map = newHashMap();
		map.put( TranslationMode.TRANSLATED, translatorService.getTranslationsMap().size() );
		map.put( TranslationMode.UNTRANSLATED, translatorService.getUntranslatedMap().size() );
		map.put( TranslationMode.UNUSED_TRANSLATIONS, translatorService.getUnusedTranslationsMap().size() );

		model.setModeRecordsCount( map );

		model.setPageTitleData( breadcrumbsAdminService.getTranslatorBreadcrumbs() );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/reload/" )
	public void reloadTranslationsAjax() throws DocumentException {
		translatorService.reloadTranslations();
	}
}
