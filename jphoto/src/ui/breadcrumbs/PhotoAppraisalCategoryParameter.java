package ui.breadcrumbs;

import core.general.photo.PhotoVotingCategory;
import core.services.system.Services;
import core.services.translator.Language;

public class PhotoAppraisalCategoryParameter extends AbstractBreadcrumb {

	private PhotoVotingCategory photoVotingCategory;

	public PhotoAppraisalCategoryParameter( final PhotoVotingCategory photoVotingCategory, final Services services ) {
		super( services );
		this.photoVotingCategory = photoVotingCategory;
	}

	@Override
	public String getValue( final Language language ) {
		return getTranslatorService().translatePhotoVotingCategory( photoVotingCategory, language );
	}
}
