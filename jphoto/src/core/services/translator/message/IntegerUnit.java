package core.services.translator.message;

import core.services.security.Services;
import core.services.translator.Language;

public class IntegerUnit extends AbstractTranslatableMessageUnit {

	private int value;

	protected IntegerUnit( final int value, final Services services ) {
		super( services );
		this.value = value;
	}

	@Override
	public String translate( final Language language ) {
		return String.valueOf( value );
	}
}
