package com.bordozer.jphoto.ui.services.validation;

import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.services.system.ConfigurationService;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.ui.context.ApplicationContextHelper;
import com.bordozer.jphoto.ui.context.EnvironmentContext;

public class PhotoRequirement {

    private TranslatorService translatorService;

    public PhotoRequirement(final TranslatorService translatorService) {
        this.translatorService = translatorService;
    }

    public String getNameRequirement() {

        final ConfigurationService configurationService = ApplicationContextHelper.getBean(ConfigurationService.BEAN_NAME);
        final StringBuilder builder = new StringBuilder();

        builder.append(translatorService.translate("The name of your photo. Max length is $1 symbols", getLanguage()
                , configurationService.getString(ConfigurationKey.SYSTEM_PHOTO_NAME_MAX_LENGTH)));

        return builder.toString();
    }

    public String getKeywordsRequirement() {
        final StringBuilder builder = new StringBuilder();

        builder.append(translatorService.translate("Comma (,) separated keywords.", getLanguage()));

        return builder.toString();
    }

    public String getDescriptionRequirement() {
        final StringBuilder builder = new StringBuilder();

        builder.append(translatorService.translate("Any additional information about the photo", getLanguage()));

        return builder.toString();
    }

    private Language getLanguage() {
        return EnvironmentContext.getLanguage();
    }
}
