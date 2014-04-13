package ui.services.page.icons;

import core.services.system.Services;

public class UntranslatedMessagesTitleIcon extends AbstractTitleIcon {

	private int untranslatedMessagesCount;

	public UntranslatedMessagesTitleIcon( final int untranslatedMessagesCount, final Services services ) {
		super( services );
		this.untranslatedMessagesCount = untranslatedMessagesCount;
	}

	@Override
	protected String getIconPath() {
		return "icons32/translate.png";
	}

	@Override
	protected String getIconTitle() {
		return getTranslatorService().translate( "There are $1 untranslated nerds are found", getLanguage(), String.valueOf( untranslatedMessagesCount ) );
	}

	@Override
	protected String getIconUrl() {
		return getUrlUtilsService().getAdminUntranslatedLink();
	}

	@Override
	protected String getIconText() {
		return String.format( "+%d", untranslatedMessagesCount );
	}
}
