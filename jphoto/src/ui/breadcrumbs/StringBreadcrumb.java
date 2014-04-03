package ui.breadcrumbs;

import core.services.system.Services;
import core.services.translator.Language;

public class StringBreadcrumb extends AbstractBreadcrumb {

	private String breadcrumb;

	public StringBreadcrumb( final String breadcrumb, final Services services ) {
		super( services );
		this.breadcrumb = breadcrumb;
	}

	@Override
	public String getValue( final Language language ) {
		return breadcrumb;
	}
}
