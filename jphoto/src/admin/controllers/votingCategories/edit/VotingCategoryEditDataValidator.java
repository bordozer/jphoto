package admin.controllers.votingCategories.edit;

import core.general.photo.PhotoVotingCategory;
import core.services.entry.VotingCategoryService;
import core.services.translator.TranslatorService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ui.context.EnvironmentContext;
import utils.FormatUtils;

public class VotingCategoryEditDataValidator implements Validator {

	@Autowired
	private VotingCategoryService votingCategoryService;

	@Autowired
	private TranslatorService translatorService;

	@Override
	public boolean supports( final Class<?> clazz ) {
		return VotingCategoryEditDataModel.class.equals( clazz );
	}

	@Override
	public void validate( final Object target, final Errors errors ) {
		final VotingCategoryEditDataModel model = ( VotingCategoryEditDataModel ) target;

		final int votingCategoryId = model.getVotingCategoryId();
		final String votingCategoryName = model.getVotingCategoryName();

		validateName( errors, votingCategoryId, votingCategoryName );
	}

	private void validateName( final Errors errors, final int votingCategoryId, final String votingCategoryName ) {

		if ( StringUtils.isEmpty( votingCategoryName ) ) {
			final String errorCode = translatorService.translate( "$1 should not be empty.", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName( "Name" ) );
			errors.rejectValue( VotingCategoryEditDataModel.VOTING_CATEGORIES_NAME_FORM_CONTROL, errorCode );
		}

		final PhotoVotingCategory checkPhotoVotingCategories = votingCategoryService.loadByName( votingCategoryName );
		if ( checkPhotoVotingCategories != null && checkPhotoVotingCategories.getId() > 0 && checkPhotoVotingCategories.getId() != votingCategoryId ) {
			errors.rejectValue( VotingCategoryEditDataModel.VOTING_CATEGORIES_NAME_FORM_CONTROL
				, translatorService.translate( "$1 ($2) already exists!", EnvironmentContext.getLanguage(), FormatUtils.getFormattedFieldName( "Name" ), votingCategoryName ), votingCategoryName );
		}
	}
}
