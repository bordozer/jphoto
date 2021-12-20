package com.bordozer.jphoto.ui.services.page.icons;

import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.core.services.utils.UrlUtilsService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractTitleIcon {

    protected final Services services;

    protected abstract String getIconPath();

    protected abstract String getIconTitle();

    protected abstract String getIconUrl();

    public AbstractTitleIcon(final Services services) {
        this.services = services;
    }

    public final String getIcon() {
        return String.format("<a href=\"%s\" title=\"%s\">%s <sup>%s</sup></a>"
                , getIconUrl()
                , getIconTitle()
                , getIconImage()
                , getIconText()
        );
    }

    protected String getIconText() {
        return StringUtils.EMPTY;
    }

    private String getIconImage() {
        return String.format("<img src=\"%s/%s\" height=\"16\" width=\"16\" />"
                , getUrlUtilsService().getSiteImagesPath()
                , getIconPath()
        );
    }

    protected UrlUtilsService getUrlUtilsService() {
        return services.getUrlUtilsService();
    }

    protected TranslatorService getTranslatorService() {
        return services.getTranslatorService();
    }

    protected Language getLanguage() {
        return EnvironmentContext.getLanguage();
    }

    protected User getCurrentUser() {
        return EnvironmentContext.getCurrentUser();
    }
}
