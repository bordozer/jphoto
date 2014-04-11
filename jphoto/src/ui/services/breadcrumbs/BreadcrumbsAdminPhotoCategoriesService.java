package ui.services.breadcrumbs;

import core.general.genre.Genre;
import ui.elements.PageTitleData;

public interface BreadcrumbsAdminPhotoCategoriesService {

	PageTitleData getGenreListBreadcrumbs();

	PageTitleData getGenreNewBreadcrumbs();

	PageTitleData getGenreEditBreadcrumbs( Genre genre );
}
