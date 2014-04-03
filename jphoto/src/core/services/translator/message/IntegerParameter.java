package core.services.translator.message;

import core.services.system.Services;
import core.services.translator.Language;

public class IntegerParameter extends AbstractTranslatableMessageParameter {

	private int value;

	protected IntegerParameter( final int value, final Services services ) {
		super( services );
		this.value = value;
	}

	@Override
	public String getValue( final Language language ) {
		return String.valueOf( value );
	}
}
