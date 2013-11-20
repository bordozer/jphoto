package core.services.system;

import elements.PageModel;
import elements.PageTitleData;

public interface PageTemplateService {

	String BEAN_NAME = "pageTemplateService";

	String renderPageHeader( final PageModel pageModel );

	String renderPageFooter();

}
