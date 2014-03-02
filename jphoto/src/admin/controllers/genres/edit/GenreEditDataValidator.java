package admin.controllers.genres.edit;

import core.general.genre.Genre;
import core.services.entry.GenreService;
import core.services.translator.TranslatorService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import utils.FormatUtils;

import java.util.List;

public class GenreEditDataValidator implements Validator {

	@Autowired
	private GenreService genreService;

	@Autowired
	private TranslatorService translatorService;

	@Override
	public boolean supports( final Class<?> clazz ) {
		return GenreEditDataModel.class.equals( clazz );
	}

	@Override
	public void validate( final Object target, final Errors errors ) {
		final GenreEditDataModel model = ( GenreEditDataModel ) target;
		final Genre genre = model.getGenre();

		validateName( errors, genre );

		validateAllowedVotingCategories( errors, model.getAllowedVotingCategoryIDs() );
	}

	private void validateName( final Errors errors, final Genre genre ) {
		String genreName = genre.getName();

		if ( StringUtils.isEmpty( genreName ) ) {
			errors.rejectValue( GenreEditDataModel.GENRE_EDIT_DATA_NAME_FORM_CONTROL, translatorService.translate( "$1 should not be empty."
				, FormatUtils.getFormattedFieldName( "Name" ) ) );
		}

		final Genre checkGenre = genreService.loadIdByName( genreName );
		if ( checkGenre != null && checkGenre.getId() > 0 && checkGenre.getId() != genre.getId()) {
			errors.rejectValue( GenreEditDataModel.GENRE_EDIT_DATA_NAME_FORM_CONTROL
				, translatorService.translate( "$1 ($2) already exists!", FormatUtils.getFormattedFieldName( "Name" ), genreName ), genreName );
		}
	}

	private void validateAllowedVotingCategories( final Errors errors, final List<String> allowedVotingCategoryIDs ) {
		if ( allowedVotingCategoryIDs == null || allowedVotingCategoryIDs.size() < 3 ) {
			errors.rejectValue( GenreEditDataModel.GENRE_EDIT_DATA_ALLOWED_VOTING_CATEGORIES_FORM_CONTROL, translatorService.translate( "Check at least 3 $1."
				, FormatUtils.getFormattedFieldName( "allowed voting category" ) ) );
		}
	}
}
