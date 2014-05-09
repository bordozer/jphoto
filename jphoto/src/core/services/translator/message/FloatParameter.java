package core.services.translator.message;

import core.services.system.Services;
import core.services.translator.Language;

public class FloatParameter extends AbstractTranslatableMessageParameter {

	private float value;

	public FloatParameter( final float value, final Services services ) {
		super( services );
		this.value = value;
	}

	@Override
	public String getValue( final Language language ) {
		return String.valueOf( value );
	}
}
