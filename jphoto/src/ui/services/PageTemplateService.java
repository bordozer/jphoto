package ui.services;

import ui.elements.PageModel;

public interface PageTemplateService {

	String BEAN_NAME = "pageTemplateService";

	String renderPageHeader( final PageModel pageModel );

	String renderPageFooter();

}
