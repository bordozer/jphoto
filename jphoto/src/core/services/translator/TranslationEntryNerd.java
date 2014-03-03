package core.services.translator;

public class TranslationEntryNerd extends TranslationEntry {

	private final String startPrefix;
	private final String endPrefix;

	public TranslationEntryNerd( final String nerd, final String startPrefix, final String endPrefix ) {
		super( nerd, Language.NERD, nerd );

		this.startPrefix = startPrefix;
		this.endPrefix = endPrefix;
	}

	@Override
	public String getValue() {
		return String.format( "%s%s%s", startPrefix, nerd, endPrefix );
	}
}
