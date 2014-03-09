package admin.controllers.translator;

import core.services.pageTitle.PageTitleAdminUtilsService;
import core.services.translator.Language;
import core.services.translator.NerdKey;
import core.services.translator.TranslationData;
import core.services.translator.TranslatorService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

@Controller
@RequestMapping( "translator" )
public class TranslatorController {

	private static final String MODEL_NAME = "translatorModel";
	private static final String VIEW = "admin/translator/Translator";

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private PageTitleAdminUtilsService pageTitleAdminUtilsService;

	@ModelAttribute( MODEL_NAME )
	public TranslatorModel prepareModel() {
		return new TranslatorModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String getTranslated( final @ModelAttribute( MODEL_NAME ) TranslatorModel model) {

		final Map<NerdKey, TranslationData> translationsMap = translatorService.getTranslationsMap();
		model.setLetters( getLetters( translationsMap ) );
		model.setTranslationsMap( translationsMap );

		model.setTranslationMode( TranslationMode.TRANSLATED );

		model.setPageTitleData( pageTitleAdminUtilsService.getTranslatorTitle() );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{letter}/" )
	public String getTranslatedByFirstLetter( final @PathVariable( "letter" ) String letter, final @ModelAttribute( MODEL_NAME ) TranslatorModel model) {

		final Map<NerdKey, TranslationData> translatedMap = translatorService.getTranslationsMap();

		model.setLetters( getLetters( translatedMap ) );
		model.setTranslationsMap( filterByFirstLetter( translatedMap, letter ) );

		model.setFilterByLetter( letter );
		model.setTranslationMode( TranslationMode.TRANSLATED );

		model.setPageTitleData( pageTitleAdminUtilsService.getTranslatorTitle() );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/untranslated/" )
	public String getUntranslated( final @ModelAttribute( MODEL_NAME ) TranslatorModel model) {

		final Map<NerdKey, TranslationData> untranslatedMap = translatorService.getUntranslatedMap();

		model.setLetters( getLetters( untranslatedMap ) );
		model.setTranslationsMap( untranslatedMap );

		model.setTranslationMode( TranslationMode.UNTRANSLATED );

		model.setUrlPrefix( "untranslated" );

		model.setPageTitleData( pageTitleAdminUtilsService.getTranslatorTitle() );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/untranslated/{letter}/" )
	public String getUntranslatedByFirstLetter( final @PathVariable( "letter" ) String letter, final @ModelAttribute( MODEL_NAME ) TranslatorModel model) {

		final Map<NerdKey, TranslationData> untranslatedMap = translatorService.getUntranslatedMap();

		model.setLetters( getLetters( untranslatedMap ) );
		model.setTranslationsMap( filterByFirstLetter( untranslatedMap, letter ) );

		model.setFilterByLetter( letter );
		model.setTranslationMode( TranslationMode.UNTRANSLATED );

		model.setUrlPrefix( "untranslated" );

		model.setPageTitleData( pageTitleAdminUtilsService.getTranslatorTitle() );

		return VIEW;
	}

	private Map<NerdKey, TranslationData> filterByFirstLetter( final Map<NerdKey, TranslationData> translationsMap, final String letter ) {

		final HashMap<NerdKey,TranslationData> map = newHashMap( translationsMap );

		for ( final NerdKey nerdKey : translationsMap.keySet() ) {
			final TranslationData translationData = translationsMap.get( nerdKey );
			final String firstLetter = translationData.getTranslationEntry( Language.NERD ).getNerd().substring( 0, 1 );
			if ( ! firstLetter.equalsIgnoreCase( letter ) ) {
				map.remove( nerdKey );
			}
		}

		return map;
	}

	private List<String> getLetters( final Map<NerdKey, TranslationData> translationsMap ) {
		final List<String> result = newArrayList();

		for ( final NerdKey nerdKey : translationsMap.keySet() ) {
			final TranslationData translationData = translationsMap.get( nerdKey );
			final String nerd = translationData.getTranslationEntry( Language.NERD ).getNerd();

			if ( StringUtils.isEmpty( nerd ) ) {
				continue;
			}

			final String letter = nerd.substring( 0, 1 ).toUpperCase();
			if ( ! result.contains( letter ) ) {
				result.add( letter );
			}
		}

		Collections.sort( result, new Comparator<String>() {
			@Override
			public int compare( final String o1, final String o2 ) {
				return o1.compareTo( o2 );
			}
		} );

		return result;
	}
}
