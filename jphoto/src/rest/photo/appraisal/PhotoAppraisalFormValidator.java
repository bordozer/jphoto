package rest.photo.appraisal;

import core.services.translator.TranslatorService;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ui.context.EnvironmentContext;
import utils.UserUtils;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class PhotoAppraisalFormValidator implements Validator {

	@Autowired
	private TranslatorService translatorService;

	@Override
	public boolean supports( final Class<?> clazz ) {
		return clazz.isAssignableFrom( PhotoAppraisalDTO.class );
	}

	@Override
	public void validate( final Object target, final Errors errors ) {
		final PhotoAppraisalDTO dto = ( PhotoAppraisalDTO ) target;

		validateUser( dto.getUserId(), errors );

		validateAtLeastOneSectionIsSelected( dto.getPhotoAppraisalForm().getAppraisalSections(), errors );
	}

	private void validateUser( final int userId, final Errors errors ) {
		if ( ! UserUtils.isTheUserThatWhoIsCurrentUser( userId ) ) {
			final String errorMessage = translatorService.translate( "Photo appraisal: You are not the user who has loaded appraisal form. The user must have changed since then.", EnvironmentContext.getLanguage() );
			errors.rejectValue( "userId", "error code", errorMessage );
		}
	}

	private void validateAtLeastOneSectionIsSelected( final List<AppraisalSection> sections, final Errors errors ) {
		final List<AppraisalSection> notZeroSections = newArrayList( sections );
		CollectionUtils.filter( notZeroSections, new Predicate<AppraisalSection>() {
			@Override
			public boolean evaluate( final AppraisalSection appraisalSection ) {
				return appraisalSection.getSelectedCategoryId() != 0 && appraisalSection.getSelectedMark() != 0;
			}
		} );

		if ( notZeroSections.size() == 0 ) {
			final String errorMessage = translatorService.translate( "Photo appraisal: Select at least one pair of appraisal category and mark", EnvironmentContext.getLanguage() );
			errors.rejectValue( "userId", "error code", errorMessage );
		}
	}
}
