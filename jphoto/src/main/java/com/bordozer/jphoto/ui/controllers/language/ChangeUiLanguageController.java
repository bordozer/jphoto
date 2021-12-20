package com.bordozer.jphoto.ui.controllers.language;

import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping(value = "language")
public class ChangeUiLanguageController {

    @Autowired
    private TranslatorService translatorService;

    @RequestMapping(method = RequestMethod.GET, value = "{languageCode}/")
    @ResponseStatus(value = HttpStatus.OK)
    public void commentsToUser(final @PathVariable("languageCode") String languageCode) {
        final Language language = Language.getByCode(languageCode);

        EnvironmentContext.getCurrentUser().setLanguage(language);

        EnvironmentContext.getEnvironment().setHiMessage(translatorService.translate("The UI language has been changed for this session only. "
                + "Set preferred a language in your profile settings for permanent basement", language));
    }
}
