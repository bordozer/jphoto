package core.services.pageTitle;

import core.general.genre.Genre;
import core.services.utils.EntityLinkUtilsService;
import elements.PageTitleData;
import org.springframework.beans.factory.annotation.Autowired;
import utils.TranslatorUtils;

public class PageTitleGenreUtilsServiceImpl implements PageTitleGenreUtilsService {

	public static final String GENRE_ROOT = "Genres";

	@Autowired
	private PageTitleUtilsService pageTitleUtilsService;

	@Autowired
	private PageTitleAdminUtilsService pageTitleAdminUtilsService;

	@Autowired
	private EntityLinkUtilsService entityLinkUtilsService;

	@Override
	public PageTitleData getGenreListData() {
		final String rootTranslated = getGenreRootTranslated();

		final String title = pageTitleUtilsService.getTitleDataString( pageTitleAdminUtilsService.getAdminTranslatedRoot(), rootTranslated );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( pageTitleAdminUtilsService.getAdminTranslatedRoot(), rootTranslated );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getGenreNewData() {
		final String rootTranslated = getGenreRootTranslated();
		final String tran = TranslatorUtils.translate( "New" );

		final String title = pageTitleUtilsService.getTitleDataString( pageTitleAdminUtilsService.getAdminTranslatedRoot(), rootTranslated, tran );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( pageTitleAdminUtilsService.getAdminTranslatedRoot(), entityLinkUtilsService.getAdminGenresRootLink(), tran );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public PageTitleData getGenreEditData( final Genre genre ) {
		final String tran = TranslatorUtils.translate( "Edit" );
		final String rootTranslated = getGenreRootTranslated();

		final String title = pageTitleUtilsService.getTitleDataString( pageTitleAdminUtilsService.getAdminTranslatedRoot(), rootTranslated, genre.getName(), tran );
		final String breadcrumbs = pageTitleUtilsService.getBreadcrumbsDataString( pageTitleAdminUtilsService.getAdminTranslatedRoot(), entityLinkUtilsService.getAdminGenresRootLink(), entityLinkUtilsService.getPhotosByGenreLink( genre ), tran );

		return new PageTitleData( title, rootTranslated, breadcrumbs );
	}

	@Override
	public String getGenreRootTranslated() {
		return TranslatorUtils.translate( GENRE_ROOT );
	}

}
