package ui.services.page.icons;

import core.services.system.Services;
import core.services.translator.Language;

public class UntranslatedMessagesTitleIcon extends AbstractTitleIcon {

	private Language language;
	private int untranslatedMessagesCount;

	public UntranslatedMessagesTitleIcon( final Language language, final int untranslatedMessagesCount, final Services services ) {
		super( services );
		this.language = language;
		this.untranslatedMessagesCount = untranslatedMessagesCount;
	}

	@Override
	protected String getIconPath() {
		return String.format( "languages/%s", language.getIcon() );
	}

	@Override
	protected String getIconTitle() {
		return getTranslatorService().translate( "$1: there are $2 untranslated nerds are found", getLanguage(), getTranslatorService().translate( language.getName(), language ), String.valueOf( untranslatedMessagesCount ) );
	}

	@Override
	protected String getIconUrl() {
		return String.format( "%s" + "language/%s/", getUrlUtilsService().getAdminUntranslatedLink(), language.getCode() );
	}

	@Override
	protected String getIconText() {
		return String.format( "+%d", untranslatedMessagesCount );
	}
}
