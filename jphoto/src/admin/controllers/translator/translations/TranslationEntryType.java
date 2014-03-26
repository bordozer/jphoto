package admin.controllers.translator.translations;

public enum TranslationEntryType {
		GENRE( 1 )
	, VOTING_CATEGORY( 2 )
	;

	private final int id;

	private TranslationEntryType( final int id ) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static TranslationEntryType getById( final int id ) {
		for ( final TranslationEntryType entry : values() ) {
			if ( entry.getId() == id ) {
				return entry;
			}
		}

		throw new IllegalArgumentException( String.format( "Illegal TranslationEntryType: %d", id ) );
	}
}
