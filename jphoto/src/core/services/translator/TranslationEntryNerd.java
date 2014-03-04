package core.services.translator;

import core.services.utils.SystemVarsService;

public class TranslationEntryNerd extends TranslationEntry {

	public TranslationEntryNerd( final String nerd, final SystemVarsService systemVarsService ) {
		super( nerd, Language.NERD, nerd, systemVarsService );
	}

	@Override
	protected String getStartPrefix() {
		return systemVarsService.getTranslatorUntranslatedStartPrefix();
	}

	@Override
	protected String getEndPrefix() {
		return systemVarsService.getTranslatorUntranslatedEndPrefix();
	}
}
