package com.bordozer.jphoto.ui.controllers.content.nude;

import com.bordozer.jphoto.core.services.utils.UrlUtilsService;
import com.bordozer.jphoto.ui.context.Environment;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/nudeContent")
public class NudeContentController {

    private static final String MODEL_NAME = "nudeContentModel";

    @Autowired
    private UrlUtilsService urlUtilsService;

    @ModelAttribute(MODEL_NAME)
    public NudeContentModel prepareModel(final HttpServletRequest request) {
        final NudeContentModel model = new NudeContentModel();

        model.setRedirectToIfAcceptUrl(request.getParameter("redirectToIfAcceptUrl"));
        model.setRedirectToIfDeclineUrl(request.getParameter("redirectToIfDeclineUrl"));
        model.setViewingNudeContent(request.getParameter("IConfirmShowingNudeContent") != null);

        return model;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/")
    public String processNudeContentShowingChoice(final @ModelAttribute(MODEL_NAME) NudeContentModel model, final HttpServletRequest request) {
        if (!model.isViewingNudeContent()) {
            //return String.format( "redirect:%s", model.getRedirectToIfDeclineUrl() );
            return String.format("redirect:%s", urlUtilsService.getAllPhotosLink());
        }

        final Environment env = EnvironmentContext.getEnvironment();
        env.setShowNudeContent(true);
        EnvironmentContext.setEnv(env);

        return String.format("redirect:%s%s", urlUtilsService.getBaseURL(), model.getRedirectToIfAcceptUrl());
    }
}
