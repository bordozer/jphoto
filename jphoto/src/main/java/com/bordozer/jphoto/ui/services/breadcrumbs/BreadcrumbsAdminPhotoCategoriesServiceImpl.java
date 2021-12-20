package com.bordozer.jphoto.ui.services.breadcrumbs;

import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.utils.EntityLinkUtilsService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.ui.elements.PageTitleData;
import com.bordozer.jphoto.ui.services.breadcrumbs.items.BreadcrumbsBuilder;
import com.bordozer.jphoto.ui.services.breadcrumbs.items.TranslatableStringBreadcrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("breadcrumbsAdminPhotoCategoriesService")
public class BreadcrumbsAdminPhotoCategoriesServiceImpl implements BreadcrumbsAdminPhotoCategoriesService {

    public static final String BREADCRUMBS_ADMINISTRATION = "Breadcrumbs: Administration";

    @Autowired
    private EntityLinkUtilsService entityLinkUtilsService;

    @Autowired
    private Services services;

    @Override
    public PageTitleData getGenreListBreadcrumbs() {
        final String breadcrumbs = admin()
                .translatableString(EntityLinkUtilsService.BREADCRUMBS_PHOTO_CATEGORIES)
                .build();

        return new PageTitleData(pageTitle(), pageHeader(), breadcrumbs);
    }

    @Override
    public PageTitleData getGenreNewBreadcrumbs() {
        final String breadcrumbs = admin()
                .string(entityLinkUtilsService.getAdminGenresRootLink(getLanguage()))
                .translatableString("Breadcrumbs: Create new photo category")
                .build();

        return new PageTitleData(pageTitle(), pageHeader(), breadcrumbs);
    }

    @Override
    public PageTitleData getGenreDataEditBreadcrumbs(final Genre genre) {
        final Language language = getLanguage();

        final String breadcrumbs = admin()
                .string(entityLinkUtilsService.getAdminGenresRootLink(language))
                .string(entityLinkUtilsService.getPhotosByGenreLink(genre, language))
                .translatableString("Breadcrumbs: Edit photo category")
                .build();

        return new PageTitleData(pageTitle(), pageHeader(), breadcrumbs);
    }

    private Language getLanguage() {
        return EnvironmentContext.getLanguage();
    }

    private String pageTitle() {
        return BreadcrumbsBuilder.pageTitle(new TranslatableStringBreadcrumb(BREADCRUMBS_ADMINISTRATION, services), services).build();
    }

    private String pageHeader() {
        return admin()
                .build();
    }

    private BreadcrumbsBuilder admin() {
        return new BreadcrumbsBuilder(services)
                .translatableString(BREADCRUMBS_ADMINISTRATION);
    }
}
