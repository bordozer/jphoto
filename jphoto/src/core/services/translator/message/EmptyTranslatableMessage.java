package core.services.translator.message;

import core.services.translator.Language;

public class EmptyTranslatableMessage extends TranslatableMessage {

	public EmptyTranslatableMessage() {
		super( "", null );
	}

	@Override
	public String build( final Language language ) {
		return "";
	}
}
