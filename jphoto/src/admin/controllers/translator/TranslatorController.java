package admin.controllers.translator;

import core.services.pageTitle.PageTitleAdminUtilsService;
import core.services.translator.TranslatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping( "translator" )
public class TranslatorController {

	private static final String MODEL_NAME = "translatorModel";
	private static final String VIEW = "admin/translator/Translator";

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private PageTitleAdminUtilsService pageTitleAdminUtilsService;

	@ModelAttribute( MODEL_NAME )
	public TranslatorModel prepareModel() {
		return new TranslatorModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String getTranslated( final @ModelAttribute( MODEL_NAME ) TranslatorModel model) {

		model.setTranslationsMap( translatorService.getTranslationsMap() );

		model.setPageTitleData( pageTitleAdminUtilsService.getTranslatorTitle() );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/untranslated/" )
	public String getUntranslated( final @ModelAttribute( MODEL_NAME ) TranslatorModel model) {

		model.setTranslationsMap( translatorService.getUntranslatedMap() );

		model.setPageTitleData( pageTitleAdminUtilsService.getTranslatorTitle() );

		return VIEW;
	}
}
