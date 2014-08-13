package admin.controllers.restriction.list;

import admin.controllers.restriction.entry.RestrictionController;
import core.services.translator.TranslatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ui.context.EnvironmentContext;
import ui.services.breadcrumbs.BreadcrumbsAdminService;
import ui.translatable.GenericTranslatableEntry;
import ui.translatable.GenericTranslatableList;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@RequestMapping( "restrictions" )
@Controller
public class RestrictionListController {

	private static final String MODEL_NAME = "restrictionListModel";

	private static final String VIEW = "admin/restriction/list/RestrictionList";

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private BreadcrumbsAdminService breadcrumbsAdminService;

	@ModelAttribute( MODEL_NAME )
	public RestrictionListModel prepareModel() {
		return new RestrictionListModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String showRestrictions( final @ModelAttribute( MODEL_NAME ) RestrictionListModel model ) {

		final List<GenericTranslatableEntry> restrictions = newArrayList();
		restrictions.addAll( GenericTranslatableList.restrictionUserTranslatableList( EnvironmentContext.getLanguage(), translatorService ).getEntries() );
		restrictions.addAll( GenericTranslatableList.restrictionPhotosTranslatableList( EnvironmentContext.getLanguage(), translatorService ).getEntries() );

		model.setRestrictionStatuses( RestrictionController.convertToJSON( GenericTranslatableList.restrictionStatusList( EnvironmentContext.getLanguage(), translatorService ).getEntries() ) );

		model.setRestrictionTypes( RestrictionController.convertToJSON( restrictions ) );

		model.setPageTitleData( breadcrumbsAdminService.getRestrictionListBreadcrumbs() );

		return VIEW;
	}
}
