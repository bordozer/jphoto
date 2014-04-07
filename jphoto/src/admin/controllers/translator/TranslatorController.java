package admin.controllers.translator;

import ui.services.breadcrumbs.BreadcrumbsAdminService;
import core.services.translator.*;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Maps.newTreeMap;

@Controller
@RequestMapping( "translator" )
public class TranslatorController {

	private static final String MODEL_NAME = "translatorModel";
	private static final String VIEW = "admin/translator/Translator";

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private BreadcrumbsAdminService breadcrumbsAdminService;

	@ModelAttribute( MODEL_NAME )
	public TranslatorModel prepareModel() {
		return new TranslatorModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String getTranslated( final @ModelAttribute( MODEL_NAME ) TranslatorModel model) {

		final Map<NerdKey, TranslationData> translationsMap = getTranslatedSortedMap();
		model.setLetters( getLetters( translationsMap ) );
		model.setTranslationsMap( translationsMap );

		model.setTranslationMode( TranslationMode.TRANSLATED );

		model.setPageTitleData( breadcrumbsAdminService.getTranslatorTitle() );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{letter}/" )
	public String getTranslatedByFirstLetter( final @PathVariable( "letter" ) String letter, final @ModelAttribute( MODEL_NAME ) TranslatorModel model) {

		final Map<NerdKey, TranslationData> translatedMap = getTranslatedSortedMap();

		model.setLetters( getLetters( translatedMap ) );
		model.setTranslationsMap( filterByFirstLetter( translatedMap, letter ) );

		model.setFilterByLetter( letter );
		model.setTranslationMode( TranslationMode.TRANSLATED );

		model.setPageTitleData( breadcrumbsAdminService.getTranslatorTitle() );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/untranslated/" )
	public String getUntranslated( final @ModelAttribute( MODEL_NAME ) TranslatorModel model) {

		final Map<NerdKey, TranslationData> untranslatedMap = getUntranslatedSortedMap();

		model.setLetters( getLetters( untranslatedMap ) );
		model.setTranslationsMap( untranslatedMap );

		model.setTranslationMode( TranslationMode.UNTRANSLATED );

		model.setUrlPrefix( "untranslated" );

		model.setPageTitleData( breadcrumbsAdminService.getTranslatorTitle() );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/untranslated/{letter}/" )
	public String getUntranslatedByFirstLetter( final @PathVariable( "letter" ) String letter, final @ModelAttribute( MODEL_NAME ) TranslatorModel model) {

		final Map<NerdKey, TranslationData> untranslatedMap = getUntranslatedSortedMap();

		model.setLetters( getLetters( untranslatedMap ) );
		model.setTranslationsMap( filterByFirstLetter( untranslatedMap, letter ) );

		model.setFilterByLetter( letter );
		model.setTranslationMode( TranslationMode.UNTRANSLATED );

		model.setUrlPrefix( "untranslated" );

		model.setPageTitleData( breadcrumbsAdminService.getTranslatorTitle() );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/reload/" )
	public void reloadTranslationsAjax() throws DocumentException {
		translatorService.reloadTranslations();
	}

	private Map<NerdKey, TranslationData> getTranslatedSortedMap() {
		return getSortedMap( translatorService.getTranslationsMap() );
	}

	private Map<NerdKey, TranslationData> getUntranslatedSortedMap() {
		return getSortedMap( translatorService.getUntranslatedMap() );
	}

	private Map<NerdKey, TranslationData> getSortedMap( final Map<NerdKey, TranslationData> untranslatedMap ) {
		final TreeMap<NerdKey, TranslationData> sortedMap = new TreeMap<NerdKey, TranslationData>( new Comparator<NerdKey>() {
			@Override
			public int compare( final NerdKey o1, final NerdKey o2 ) {
				return o1.getNerd().compareTo( o2.getNerd() );
			}
		} );

		sortedMap.putAll( untranslatedMap );

		return sortedMap;
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
			final TranslationEntry translationEntry = translationData.getTranslationEntry( Language.NERD );

			if ( translationEntry == null || StringUtils.isEmpty( translationEntry.getNerd() ) ) {
				continue;
			}

			final String letter = translationEntry.getNerd().substring( 0, 1 ).toUpperCase();
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
