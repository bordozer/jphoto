package com.bordozer.jphoto.ui.services.breadcrumbs;

import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.activity.ActivityType;
import com.bordozer.jphoto.ui.elements.PageTitleData;
import com.bordozer.jphoto.ui.services.breadcrumbs.items.BreadcrumbsBuilder;
import com.bordozer.jphoto.ui.services.breadcrumbs.items.TranslatableStringBreadcrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.bordozer.jphoto.ui.services.breadcrumbs.items.BreadcrumbsBuilder.portalPage;

@Service("commonBreadcrumbsService")
public class CommonBreadcrumbsServiceImpl implements CommonBreadcrumbsService {

    @Autowired
    private Services services;

    @Override
    public PageTitleData getActivityStreamBreadcrumbs(final ActivityType activityType) {

        final TranslatableStringBreadcrumb activityStreamText = new TranslatableStringBreadcrumb("Breadcrumbs: Activity stream", services);

        final String title = BreadcrumbsBuilder.pageTitle(activityStreamText, services).build();
        final String header = BreadcrumbsBuilder.pageHeader(activityStreamText, services).build();

        final BreadcrumbsBuilder breadcrumbs = portalPage(services);

        if (activityType == null) {
            breadcrumbs.add(activityStreamText).build();
        } else {
            breadcrumbs.activityStream().translatableString(activityType.getName());
        }

        return new PageTitleData(title, header, breadcrumbs.build());
    }

    @Override
    public PageTitleData genGenreListBreadcrumbs() {

        final TranslatableStringBreadcrumb text = new TranslatableStringBreadcrumb("Breadcrumbs: Genre list", services);

        final String title = BreadcrumbsBuilder.pageTitle(text, services).build();
        final String header = BreadcrumbsBuilder.pageHeader(text, services).build();

        final BreadcrumbsBuilder breadcrumbs = portalPage(services).translatableString("Breadcrumbs: Genre list");

        return new PageTitleData(title, header, breadcrumbs.build());
    }
}
