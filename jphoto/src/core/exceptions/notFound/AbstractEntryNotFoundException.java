package core.exceptions.notFound;

import core.exceptions.BaseRuntimeException;
import utils.TranslatorUtils;

public abstract class AbstractEntryNotFoundException extends BaseRuntimeException {

	public AbstractEntryNotFoundException( final String _entryId, final String entityName ) {
		super( TranslatorUtils.translate( "$1 with ID = '$2' not found", entityName, _entryId ) );
	}
}
