package ui.services.breadcrumbs;

import core.context.EnvironmentContext;
import core.general.genre.Genre;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.utils.EntityLinkUtilsService;
import elements.PageTitleData;
import org.springframework.beans.factory.annotation.Autowired;

public class BreadcrumbsGenreServiceImpl implements BreadcrumbsGenreService {

	public static final String GENRE_ROOT = "Photo categories";

	@Autowired
	private PageTitleUtilsService pageTitleUtilsService;

	@Autowired
	private BreadcrumbsAdminService breadcrumbsAdminService;

	@Autowired
	private EntityLinkUtilsService entityLinkUtilsService;
	
	@Autowired
	private TranslatorService translatorService;

	@Override
	public PageTitleData getGenreListData() {
		final String rootTranslated = getGenreRootTranslated();

		final String title = pageTitleUtilsService.getTitleDataString( breadcrumbsAdminService.getAdminTranslatedRoot(), rootTranslated );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( breadcrumbsAdminService.getAdminTranslatedRoot(), rootTranslated );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getGenreNewData() {
		final String rootTranslated = getGenreRootTranslated();
		final String tran = translatorService.translate( "New", EnvironmentContext.getLanguage() );

		final String title = pageTitleUtilsService.getTitleDataString( breadcrumbsAdminService.getAdminTranslatedRoot(), rootTranslated, tran );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( breadcrumbsAdminService.getAdminTranslatedRoot(), entityLinkUtilsService.getAdminGenresRootLink( EnvironmentContext.getLanguage() ), tran );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getGenreEditData( final Genre genre ) {
		final String tran = translatorService.translate( "Edit", EnvironmentContext.getLanguage() );
		final String rootTranslated = getGenreRootTranslated();

		final Language language = EnvironmentContext.getCurrentUser().getLanguage();

		final String title = pageTitleUtilsService.getTitleDataString( breadcrumbsAdminService.getAdminTranslatedRoot(), rootTranslated
			, translatorService.translateGenre( genre, language ), tran );

		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( breadcrumbsAdminService.getAdminTranslatedRoot(), entityLinkUtilsService.getAdminGenresRootLink( EnvironmentContext.getLanguage() )
			, entityLinkUtilsService.getPhotosByGenreLink( genre, language ), tran );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public String getGenreRootTranslated() {
		return translatorService.translate( GENRE_ROOT, EnvironmentContext.getLanguage() );
	}

}
