package com.bordozer.jphoto.ui.services.page;

import com.bordozer.jphoto.ui.elements.PageModel;

public interface PageTemplateService {

    String BEAN_NAME = "pageTemplateService";

    String renderPageHeader(final PageModel pageModel);

    String renderPageFooter();

}
