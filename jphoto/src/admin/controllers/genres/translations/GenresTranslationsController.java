package admin.controllers.genres.translations;

import core.context.EnvironmentContext;
import core.general.genre.Genre;
import core.services.entry.GenreService;
import core.services.pageTitle.PageTitleAdminUtilsService;
import core.services.security.SecurityService;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.utils.UrlUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newLinkedHashMap;

@SessionAttributes( { GenresTranslationsController.MODEL_NAME } )
@Controller
@RequestMapping( "genres/translations" )
public class GenresTranslationsController {

	public static final String MODEL_NAME = "genresTranslationsModel";
	public static final String VIEW = "/admin/genres/translations/GenresTranslations";

	private static final Language DEFAULT_LANGUAGE = Language.RU;

	@Autowired
	private GenreService genreService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private PageTitleAdminUtilsService pageTitleAdminUtilsService;

	@Autowired
	private UrlUtilsService urlUtilsService;

	@Autowired
	private TranslatorService translatorService;

	@ModelAttribute( MODEL_NAME )
	public GenresTranslationsModel prepareModel() {
		final GenresTranslationsModel model = new GenresTranslationsModel();

		model.setLanguages( Language.getUILanguages() );
		model.setSelectedLanguageId( DEFAULT_LANGUAGE.getId() );
		model.setSelectedLanguageIdOld( DEFAULT_LANGUAGE.getId() );

		return model;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String loadGenresTranslations( final @ModelAttribute( MODEL_NAME ) GenresTranslationsModel model ) {

		securityService.assertSuperAdminAccess( EnvironmentContext.getCurrentUser() );

		final Map<IdLanguageKey, GenreTranslationEntry> allTranslationEntriesMap = getAllTranslationEntriesMap();
		model.setAllTranslationEntriesMap( allTranslationEntriesMap );

		model.setTranslationEntriesMap( getTranslationEntriesMap( allTranslationEntriesMap, DEFAULT_LANGUAGE ) );
		model.setGenreMap( getGenresMap() );

		model.setPageTitleData( pageTitleAdminUtilsService.getGenresTranslationsTitleData() );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.POST, value = "/" )
	public String submit( final @ModelAttribute( MODEL_NAME ) GenresTranslationsModel model ) {

		// TODO: save request values to model.getAllTranslationEntriesMap()
		for ( final int genreId : model.getTranslationEntriesMap().keySet() ) {

		}

		model.setTranslationEntriesMap( getTranslationEntriesMap( model.getAllTranslationEntriesMap(), Language.getById( model.getSelectedLanguageId() ) ) );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.POST, value = "save/" )
	public String saveTranslations( final @ModelAttribute( MODEL_NAME ) GenresTranslationsModel model ) {

		final Map<IdLanguageKey, GenreTranslationEntry> entriesMap = model.getAllTranslationEntriesMap();

		for ( final IdLanguageKey idLanguageKey : entriesMap.keySet() ) {
			final GenreTranslationEntry translationEntry = entriesMap.get( idLanguageKey );
			translatorService.save( TranslationEntryType.GENRE, translationEntry.getEntryId(), idLanguageKey.getLanguage(), translationEntry.getTranslation() );
		}

		return String.format( "redirect:%s/genres/", urlUtilsService.getAdminBaseURLWithPrefix() );
	}

	private Map<Integer, GenreTranslationEntry> getTranslationEntriesMap( final Map<IdLanguageKey, GenreTranslationEntry> allTranslationEntriesMap, final Language language ) {

		final Map<Integer, GenreTranslationEntry> translationEntriesMap = newLinkedHashMap();

		for ( final IdLanguageKey idLanguageKey : allTranslationEntriesMap.keySet() ) {
			if ( idLanguageKey.getLanguage() == language ) {
				translationEntriesMap.put( idLanguageKey.getId(), allTranslationEntriesMap.get( idLanguageKey ) );
			}
		}

		return translationEntriesMap;
	}

	private Map<IdLanguageKey, GenreTranslationEntry> getAllTranslationEntriesMap() {
		final List<Genre> genres = genreService.loadAll();

		final Map<IdLanguageKey, GenreTranslationEntry> translationEntriesMap = newLinkedHashMap();
		for ( final Genre genre : genres ) {
			for ( final Language language : Language.getUILanguages() ) {
				translationEntriesMap.put( new IdLanguageKey( genre.getId(), language ), new GenreTranslationEntry( genre.getId(), language, genre.getName() ) );
			}
		}
		return translationEntriesMap;
	}

	private Map<Integer, Genre> getGenresMap() {
		final List<Genre> genres = genreService.loadAll();
		final Map<Integer, Genre> genreMap = newLinkedHashMap();
		for ( final Genre genre : genres ) {
			genreMap.put( genre.getId(), genre );
		}
		return genreMap;
	}
}
