package admin.controllers.votingCategories.edit;

import core.general.photo.PhotoVotingCategory;
import core.services.entry.VotingCategoryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import utils.FormatUtils;
import utils.TranslatorUtils;

public class VotingCategoryEditDataValidator implements Validator {

	@Autowired
	private VotingCategoryService votingCategoryService;

	@Override
	public boolean supports( final Class<?> clazz ) {
		return VotingCategoryEditDataModel.class.equals( clazz );
	}

	@Override
	public void validate( final Object target, final Errors errors ) {
		final VotingCategoryEditDataModel model = ( VotingCategoryEditDataModel ) target;

		final PhotoVotingCategory photoVotingCategory = model.getPhotoVotingCategory();

		validateName( errors, photoVotingCategory );
	}

	private void validateName( final Errors errors, final PhotoVotingCategory photoVotingCategory ) {
		final String name = photoVotingCategory.getName();

		if ( StringUtils.isEmpty( name ) ) {
			final String errorCode = TranslatorUtils.translate( "$1 should not be empty.", FormatUtils.getFormattedFieldName( "Name" ) );
			errors.rejectValue( VotingCategoryEditDataModel.VOTING_CATEGORIES_NAME_FORM_CONTROL, errorCode );
		}

		final PhotoVotingCategory checkPhotoVotingCategories = votingCategoryService.loadByName( name );
		if ( checkPhotoVotingCategories != null && checkPhotoVotingCategories.getId() > 0 && checkPhotoVotingCategories.getId() != photoVotingCategory.getId() ) {
			errors.rejectValue( VotingCategoryEditDataModel.VOTING_CATEGORIES_NAME_FORM_CONTROL
				, TranslatorUtils.translate( "$1 ($2) already exists!", FormatUtils.getFormattedFieldName( "Name" ), name ), name );
		}
	}
}
