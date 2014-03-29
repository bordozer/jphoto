package core.services.translator.message;

import core.services.security.Services;
import core.services.translator.Language;

import java.util.Date;

public class FormattedDateTimeUnit extends AbstractTranslatableMessageUnit {

	private Date time;

	public FormattedDateTimeUnit( final Date time, final Services services ) {
		super( services );
		this.time = time;
	}

	@Override
	public String getValue( final Language language ) {
		return getDateUtilsService().formatDateTime( time );
	}
}
