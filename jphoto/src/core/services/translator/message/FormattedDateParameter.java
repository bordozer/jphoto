package core.services.translator.message;

import core.services.system.Services;
import core.services.translator.Language;

import java.util.Date;

public class FormattedDateParameter extends AbstractTranslatableMessageParameter {

	private Date date;

	public FormattedDateParameter( final Date date, final Services services ) {
		super( services );
		this.date = date;
	}

	@Override
	public String getValue( final Language language ) {
		return getDateUtilsService().formatDate( date );
	}
}
