package core.services.translator.message;

import core.services.security.Services;
import core.services.translator.Language;

public class TranslatableMessageParameter extends AbstractTranslatableMessageParameter {

	private TranslatableMessage translatableMessage;

	protected TranslatableMessageParameter( final TranslatableMessage translatableMessage, final Services services ) {
		super( services );

		this.translatableMessage = translatableMessage;
	}

	@Override
	public String getValue( final Language language ) {
		return translatableMessage.build( language );
	}
}
