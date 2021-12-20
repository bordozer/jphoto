package com.bordozer.jphoto.ui.services.breadcrumbs;

import com.bordozer.jphoto.core.services.utils.EntityLinkUtilsService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("pageTitleUtilsService")
public class PageTitleUtilsServiceImpl implements PageTitleUtilsService {

    @Autowired
    private EntityLinkUtilsService entityLinkUtilsService;

    @Value("${app.projectName}")
    private String projectName;

    @Override
    public String getDataString(String root, String... strings) {

        final StringBuilder builder = new StringBuilder(root).append(" / ");

        int i = 0;

        for (String string : strings) {
            builder.append(string);
            if (i++ < strings.length - 1) {
                builder.append(" / ");
            }
        }

        return builder.toString();
    }

    @Override
    public String getTitleDataString(final String... strings) {
        return getDataString(projectName, strings);
    }

    @Override
    public String getBreadcrumbsDataString(final String... strings) {
        return getDataString(entityLinkUtilsService.getPortalPageLink(EnvironmentContext.getLanguage()), strings);
    }

    @Override
    public String getBreadcrumbsDataString(final List<String> list) {
        final String[] elements = new String[list.size()];

        int i = 0;
        for (final String element : list) {
            elements[i++] = element;
        }

        return getDataString(entityLinkUtilsService.getPortalPageLink(EnvironmentContext.getLanguage()), elements);
    }
}
