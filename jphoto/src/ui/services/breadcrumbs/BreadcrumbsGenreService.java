package ui.services.breadcrumbs;

import core.general.genre.Genre;
import elements.PageTitleData;

public interface BreadcrumbsGenreService {

	PageTitleData getGenreListBreadcrumbs();

	PageTitleData getGenreNewBreadcrumbs();

	PageTitleData getGenreEditBreadcrumbs( Genre genre );
}
