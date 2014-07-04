package ui.services.breadcrumbs.items;

import admin.jobs.enums.SavedJobType;
import core.services.system.Services;
import core.services.translator.Language;

public class AdminSavedJobTypeBreadcrumbs extends AbstractBreadcrumb {

	private SavedJobType savedJobType;

	public AdminSavedJobTypeBreadcrumbs( final SavedJobType savedJobType, final Services services ) {
		super( services );
		this.savedJobType = savedJobType;
	}

	@Override
	public String getValue( final Language language ) {
		return getTranslatorService().translate( savedJobType.getName(), language );
	}
}
