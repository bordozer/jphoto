package ui.elements;

public class PageModel {

	private PageTitleData pageTitleData;
	private boolean showLoginForm = true;

	public PageTitleData getPageTitleData() {
		return pageTitleData;
	}

	public void setPageTitleData( PageTitleData pageTitleData ) {
		this.pageTitleData = pageTitleData;
	}

	public boolean isShowLoginForm() {
		return showLoginForm;
	}

	public void setShowLoginForm( boolean showLoginForm ) {
		this.showLoginForm = showLoginForm;
	}
}
