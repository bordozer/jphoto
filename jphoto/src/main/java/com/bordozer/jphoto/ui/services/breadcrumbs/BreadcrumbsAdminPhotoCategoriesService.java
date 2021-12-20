package com.bordozer.jphoto.ui.services.breadcrumbs;

import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.ui.elements.PageTitleData;

public interface BreadcrumbsAdminPhotoCategoriesService {

    PageTitleData getGenreListBreadcrumbs();

    PageTitleData getGenreNewBreadcrumbs();

    PageTitleData getGenreDataEditBreadcrumbs(Genre genre);
}
