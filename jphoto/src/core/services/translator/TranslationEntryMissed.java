package core.services.translator;

import core.services.utils.SystemVarsService;

public class TranslationEntryMissed extends TranslationEntry {

	public TranslationEntryMissed( final String nerd, final Language language, final SystemVarsService systemVarsService ) {
		super( nerd, language, nerd, systemVarsService );
	}

	@Override
	protected String getStartPrefix() {
		return systemVarsService.getTranslatorUntranslatedStartPrefix();
	}

	@Override
	protected String getEndPrefix() {
		return systemVarsService.getTranslatorUntranslatedEndPrefix();
	}

	@Override
	public TranslationEntryType getTranslationEntryType() {
		return TranslationEntryType.MISSED_LANGUAGE;
	}

	@Override
	public String getLanguageCode() {
		if ( systemVarsService.getShowLanguageCodeAfterTranslation() ) {
			return String.format( "%s<sup>!</sup>", super.getLanguageCode() );
		}
		return super.getLanguageCode();
	}
}
