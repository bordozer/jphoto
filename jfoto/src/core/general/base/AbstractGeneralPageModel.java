package core.general.base;

import elements.PageModel;
import elements.PageTitleData;

public class AbstractGeneralPageModel {

	private PageModel pageModel = new PageModel();

	public PageModel getPageModel() {
		return pageModel;
	}

	public void setPageModel( PageModel pageModel ) {
		this.pageModel = pageModel;
	}

	public void setPageTitleData( PageTitleData pageTitleData ) {
		pageModel.setPageTitleData( pageTitleData );
	}
}
