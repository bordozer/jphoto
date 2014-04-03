package ui.breadcrumbs;

import core.services.system.Services;
import core.services.translator.Language;

import java.sql.Time;

public class FormattedDateBreadcrumb extends AbstractBreadcrumb {

	private Time date;

	public FormattedDateBreadcrumb( final Time date, final Services services ) {
		super( services );
		this.date = date;
	}

	@Override
	public String getValue( final Language language ) {
		return getDateUtilsService().formatDate( date );
	}
}
