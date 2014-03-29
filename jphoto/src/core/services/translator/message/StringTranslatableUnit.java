package core.services.translator.message;

import core.services.security.Services;
import core.services.translator.Language;

public class StringTranslatableUnit extends AbstractTranslatableMessageUnit {

	private String value;

	protected StringTranslatableUnit( final String value, final Services services ) {
		super( services );

		this.value = value;
	}

	@Override
	public String getValue( final Language language ) {
		return getTranslatorService().translate( value, language );
	}
}
