package ui.services.breadcrumbs;

import core.general.genre.Genre;
import core.services.system.Services;
import core.services.translator.Language;
import core.services.utils.EntityLinkUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import ui.context.EnvironmentContext;
import ui.elements.PageTitleData;
import ui.services.breadcrumbs.items.BreadcrumbsBuilder;
import ui.services.breadcrumbs.items.TranslatableStringBreadcrumb;

public class BreadcrumbsAdminPhotoCategoriesServiceImpl implements BreadcrumbsAdminPhotoCategoriesService {

	public static final String BREADCRUMBS_ADMINISTRATION = "Breadcrumbs: Administration";

	@Autowired
	private EntityLinkUtilsService entityLinkUtilsService;

	@Autowired
	private Services services;

	@Override
	public PageTitleData getGenreListBreadcrumbs() {
		final String breadcrumbs = admin()
			.translatableString( EntityLinkUtilsService.BREADCRUMBS_PHOTO_CATEGORIES )
			.build();

		return new PageTitleData( pageTitle(), pageHeader(), breadcrumbs );
	}

	@Override
	public PageTitleData getGenreNewBreadcrumbs() {
		final String breadcrumbs = admin()
			.string( entityLinkUtilsService.getAdminGenresRootLink( getLanguage() ) )
			.translatableString( "Breadcrumbs: Create new photo category" )
			.build();

		return new PageTitleData( pageTitle(), pageHeader(), breadcrumbs );
	}

	@Override
	public PageTitleData getGenreDataEditBreadcrumbs( final Genre genre ) {
		final Language language = getLanguage();

		final String breadcrumbs = admin()
			.string( entityLinkUtilsService.getAdminGenresRootLink( language ) )
			.string( entityLinkUtilsService.getPhotosByGenreLink( genre, language ) )
			.translatableString( "Breadcrumbs: Edit photo category" )
			.build();

		return new PageTitleData( pageTitle(), pageHeader(), breadcrumbs );
	}

	private Language getLanguage() {
		return EnvironmentContext.getLanguage();
	}

	private String pageTitle() {
		return BreadcrumbsBuilder.pageTitle( new TranslatableStringBreadcrumb( BREADCRUMBS_ADMINISTRATION, services ), services ).build();
	}

	private String pageHeader() {
		return admin()
			.build();
	}

	private BreadcrumbsBuilder admin() {
		return new BreadcrumbsBuilder( services )
			.translatableString( BREADCRUMBS_ADMINISTRATION );
	}
}
