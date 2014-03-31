package core.services.translator.message;

import core.general.photo.PhotoVotingCategory;
import core.services.security.Services;
import core.services.translator.Language;

public class PhotoVotingCategoryParameter extends AbstractTranslatableMessageParameter {

	private PhotoVotingCategory photoVotingCategory;

	protected PhotoVotingCategoryParameter( final PhotoVotingCategory photoVotingCategory, final Services services ) {
		super( services );

		this.photoVotingCategory = photoVotingCategory;
	}

	@Override
	public String getValue( final Language language ) {
		return getTranslatorService().translatePhotoVotingCategory( photoVotingCategory, language );
	}
}
