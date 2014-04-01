package core.services.translator;

import core.services.utils.SystemVarsService;

public class TranslationEntryNerd extends TranslationEntry {

	public TranslationEntryNerd( final String nerd ) {
		super( nerd, Language.NERD, nerd );
	}

	@Override
	public TranslationEntryType getTranslationEntryType() {
		return TranslationEntryType.NERD_TRANSLATION;
	}

	@Override
	public String getValueWithPrefixes() {
		return String.format( "[%s]", value );
	}
}
