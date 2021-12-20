package com.bordozer.jphoto.ui.controllers.portalpage;

import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.ui.elements.PageTitleData;
import com.bordozer.jphoto.ui.services.breadcrumbs.items.BreadcrumbsBuilder;
import lombok.SneakyThrows;
import org.jabsorb.JSONRPCServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class PortalPageController {

    public static final String VIEW = "portalpage/PortalPage";
    public static final String MODEL_NAME = "portalPageModel";

    @Autowired
    private TranslatorService translatorService;
    @Autowired
    private JSONRPCServlet jsonrpcServlet;

    @Value("${app.projectName}")
    private String projectName;

    @ModelAttribute(MODEL_NAME)
    public PortalPageModel prepareModel() {

        final PortalPageModel model = new PortalPageModel();
        model.setTranslatorService(translatorService);

        return model;
    }

    @RequestMapping("/")
    public String portalPage(@ModelAttribute(MODEL_NAME) PortalPageModel model) {

        final String title = translatorService.translate(BreadcrumbsBuilder.BREADCRUMBS_PORTAL_PAGE, EnvironmentContext.getLanguage());
        model.getPageModel().setPageTitleData(new PageTitleData(projectName, title, title));

        return VIEW;
    }

    @RequestMapping(value = "/require-config.js", produces = "text/javascript")
    public String require() {
        return "js/RequireConfig";
    }

    @SneakyThrows
    @RequestMapping(value = "/JSON-RPC", produces = "text/javascript")
    public void jsonRpc(final HttpServletRequest request, final HttpServletResponse response) {
        jsonrpcServlet.service(request, response);
    }
}
