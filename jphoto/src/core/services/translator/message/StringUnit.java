package core.services.translator.message;

import core.services.security.Services;
import core.services.translator.Language;

public class StringUnit extends AbstractTranslatableMessageUnit {

	private final String value;

	public StringUnit( final String value, final Services services ) {
		super( services );

		this.value = value;
	}

	@Override
	public String getValue( final Language language ) {
		return getTranslatorService().translate( value, language );
	}
}
