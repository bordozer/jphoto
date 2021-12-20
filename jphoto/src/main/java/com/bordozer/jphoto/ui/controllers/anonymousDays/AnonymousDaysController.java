package com.bordozer.jphoto.ui.controllers.anonymousDays;

import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.services.entry.AnonymousDaysService;
import com.bordozer.jphoto.core.services.system.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("anonymousDays")
public class AnonymousDaysController {
    public static final String MODEL_NAME = "anonymousDaysModel";

    private static final String VIEW = "/anonymousDays/AnonymousDays";

    @Autowired
    private AnonymousDaysService anonymousDaysService;

    @Autowired
    private ConfigurationService configurationService;

    @ModelAttribute(MODEL_NAME)
    public AnonymousDaysModel prepareModel() {
        return new AnonymousDaysModel();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String showGenreList(final @ModelAttribute(MODEL_NAME) AnonymousDaysModel model) {

        model.setAnonymousDays(anonymousDaysService.loadAll());
        model.setAnonymousPeriod(configurationService.getInt(ConfigurationKey.PHOTO_UPLOAD_ANONYMOUS_PERIOD));

        return VIEW;
    }
}
