package ui.services.page.icons;

import core.services.system.Services;

public class UnreadCommentsCountTitleIcon extends AbstractTitleIcon {

	private int unreadCommentsCount;

	public UnreadCommentsCountTitleIcon( final int unreadCommentsCount, final Services services ) {
		super( services );
		this.unreadCommentsCount = unreadCommentsCount;
	}

	@Override
	protected String getIconPath() {
		return "icons16/newComments16.png";
	}

	@Override
	protected String getIconTitle() {
		return getTranslatorService().translate( "You have $1 new comment(s)", getLanguage(), String.valueOf( unreadCommentsCount ) );
	}

	@Override
	protected String getIconUrl() {
		return getUrlUtilsService().getReceivedUnreadComments( getCurrentUser().getId() );
	}

	@Override
	protected String getIconText() {
		return String.format( "+%d", unreadCommentsCount );
	}
}
