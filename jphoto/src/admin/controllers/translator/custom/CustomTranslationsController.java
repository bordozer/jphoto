package admin.controllers.translator.custom;

import core.context.EnvironmentContext;
import core.general.genre.Genre;
import core.general.photo.PhotoVotingCategory;
import core.interfaces.CustomTranslatable;
import core.services.entry.GenreService;
import core.services.entry.VotingCategoryService;
import core.services.pageTitle.PageTitleAdminUtilsService;
import core.services.security.SecurityService;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.utils.UrlUtilsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newLinkedHashMap;

@SessionAttributes( { CustomTranslationsController.MODEL_NAME } )
@Controller
@RequestMapping( "translations/custom" )
public class CustomTranslationsController {

	public static final String MODEL_NAME = "customTranslationsModel";
	public static final String VIEW = "/admin/translator/custom/CustomTranslations";

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

	@Autowired
	private VotingCategoryService votingCategoryService;

	@ModelAttribute( MODEL_NAME )
	public CustomTranslationsModel prepareModel() {
		return new CustomTranslationsModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "genres/" )
	public String loadGenresTranslations( final @ModelAttribute( MODEL_NAME ) CustomTranslationsModel model ) {

		securityService.assertSuperAdminAccess( EnvironmentContext.getCurrentUser() );

		initLanguages( model );

		model.setTranslationEntryType( TranslationEntryType.GENRE );
		model.setRedirectToPrefix( "genres" );

		model.setPageTitleData( pageTitleAdminUtilsService.getGenresTranslationsTitleData() );

		final List<Genre> entries = genreService.loadAll();

		final Map<IdLanguageKey, GenreTranslationEntry> allTranslationEntriesMap = getAllTranslationEntriesMap( entries, model.getTranslationEntryType() );
		model.setAllTranslationEntriesMap( allTranslationEntriesMap );

		model.setSelectedLanguageTranslationEntriesMap( getTranslationEntriesMap( allTranslationEntriesMap ) );
		model.setCustomTranslatableEntriesMap( getCustomTranslatableEntriesMap( entries ) );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "voting-categories/" )
	public String loadVotingCategoriesTranslations( final @ModelAttribute( MODEL_NAME ) CustomTranslationsModel model ) {

		securityService.assertSuperAdminAccess( EnvironmentContext.getCurrentUser() );

		initLanguages( model );

		model.setTranslationEntryType( TranslationEntryType.VOTING_CATEGORY );
		model.setRedirectToPrefix( "votingcategories" );

		model.setPageTitleData( pageTitleAdminUtilsService.getVotingCategoriesTranslationsTitleData() );

		final List<PhotoVotingCategory> entries = votingCategoryService.loadAll();

		final Map<IdLanguageKey, GenreTranslationEntry> allTranslationEntriesMap = getAllTranslationEntriesMap( entries, model.getTranslationEntryType() );
		model.setAllTranslationEntriesMap( allTranslationEntriesMap );

		model.setSelectedLanguageTranslationEntriesMap( getTranslationEntriesMap( allTranslationEntriesMap ) );
		model.setCustomTranslatableEntriesMap( getCustomTranslatableEntriesMap( entries ) );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.POST, value = "/" )
	public String submit( final @ModelAttribute( MODEL_NAME ) CustomTranslationsModel model ) {

		model.setSelectedLanguageTranslationEntriesMap( getTranslationEntriesMap( model.getAllTranslationEntriesMap(), Language.getById( model.getSelectedLanguageId() ) ) );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.POST, value = "save/" )
	public String saveTranslations( final @ModelAttribute( MODEL_NAME ) CustomTranslationsModel model ) {

		final Map<IdLanguageKey, GenreTranslationEntry> entriesMap = model.getAllTranslationEntriesMap();

		for ( final IdLanguageKey idLanguageKey : entriesMap.keySet() ) {
			final GenreTranslationEntry translationEntry = entriesMap.get( idLanguageKey );

			if ( StringUtils.isEmpty( translationEntry.getTranslation() ) ) {
				continue;
			}

			translatorService.save( model.getTranslationEntryType(), translationEntry.getEntryId(), idLanguageKey.getLanguage(), translationEntry.getTranslation() );
		}

		return String.format( "redirect:%s/%s/", urlUtilsService.getAdminBaseURLWithPrefix(), model.getRedirectToPrefix() );
	}

	private void initLanguages( final CustomTranslationsModel model ) {
		model.setLanguages( Language.getUILanguages() );
		model.setSelectedLanguageId( DEFAULT_LANGUAGE.getId() );
		model.setSelectedLanguageIdOld( DEFAULT_LANGUAGE.getId() );
	}

	private Map<Integer, GenreTranslationEntry> getTranslationEntriesMap( final Map<IdLanguageKey, GenreTranslationEntry> allTranslationEntriesMap ) {
		return getTranslationEntriesMap( allTranslationEntriesMap, DEFAULT_LANGUAGE );
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

	private Map<IdLanguageKey, GenreTranslationEntry> getAllTranslationEntriesMap( final List<? extends CustomTranslatable> entries, final TranslationEntryType entryType ) {

		final Map<IdLanguageKey, GenreTranslationEntry> translationEntriesMap = newLinkedHashMap();
		for ( final CustomTranslatable entry : entries ) {
			for ( final Language language : Language.getUILanguages() ) {
				final int entryId = entry.getId();

				final String translation = translatorService.translateCustom( entryType, entryId, language );
				translationEntriesMap.put( new IdLanguageKey( entryId, language ), new GenreTranslationEntry( entryId, language, translation ) );
			}
		}
		return translationEntriesMap;
	}

	private Map<Integer, String> getCustomTranslatableEntriesMap( final List<? extends CustomTranslatable> entries ) {
		final Map<Integer, String> genreMap = newLinkedHashMap();
		for ( final CustomTranslatable entry : entries ) {
			genreMap.put( entry.getId(), entry.getName() );
		}
		return genreMap;
	}
}