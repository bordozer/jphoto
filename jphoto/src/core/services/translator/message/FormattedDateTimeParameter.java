package core.services.translator.message;

import core.services.system.Services;
import core.services.translator.Language;

import java.util.Date;

public class FormattedDateTimeParameter extends AbstractTranslatableMessageParameter {

	private Date time;

	public FormattedDateTimeParameter( final Date time, final Services services ) {
		super( services );
		this.time = time;
	}

	@Override
	public String getValue( final Language language ) {
		return getDateUtilsService().formatDateTime( time );
	}
}
