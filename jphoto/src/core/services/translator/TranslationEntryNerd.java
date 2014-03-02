package core.services.translator;

public class TranslationEntryNerd extends TranslationEntry {

	public TranslationEntryNerd( final String nerd ) {
		super( nerd, Language.NERD, nerd );
	}

	@Override
	public String getValue() {
		return String.format( "[%s]", nerd );
	}
}
