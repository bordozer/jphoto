package ui.controllers.content.nude;

public class NudeContentModel {

	private String redirectToIfAcceptUrl;
	private String redirectToIfDeclineUrl;
	private boolean viewingNudeContent;

	public String getRedirectToIfAcceptUrl() {
		return redirectToIfAcceptUrl;
	}

	public void setRedirectToIfAcceptUrl( final String redirectToIfAcceptUrl ) {
		this.redirectToIfAcceptUrl = redirectToIfAcceptUrl;
	}

	public String getRedirectToIfDeclineUrl() {
		return redirectToIfDeclineUrl;
	}

	public void setRedirectToIfDeclineUrl( final String redirectToIfDeclineUrl ) {
		this.redirectToIfDeclineUrl = redirectToIfDeclineUrl;
	}

	public boolean isViewingNudeContent() {
		return viewingNudeContent;
	}

	public void setViewingNudeContent( final boolean viewingNudeContent ) {
		this.viewingNudeContent = viewingNudeContent;
	}
}
