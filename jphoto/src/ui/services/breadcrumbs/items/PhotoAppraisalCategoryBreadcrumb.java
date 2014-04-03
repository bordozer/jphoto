package ui.services.breadcrumbs.items;

import core.general.photo.PhotoVotingCategory;
import core.services.system.Services;
import core.services.translator.Language;

public class PhotoAppraisalCategoryBreadcrumb extends AbstractBreadcrumb {

	private PhotoVotingCategory photoVotingCategory;

	public PhotoAppraisalCategoryBreadcrumb( final PhotoVotingCategory photoVotingCategory, final Services services ) {
		super( services );
		this.photoVotingCategory = photoVotingCategory;
	}

	@Override
	public String getValue( final Language language ) {
		return getTranslatorService().translatePhotoVotingCategory( photoVotingCategory, language );
	}
}
