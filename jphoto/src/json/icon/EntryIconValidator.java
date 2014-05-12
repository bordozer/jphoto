package json.icon;

import core.services.entry.FavoritesService;
import core.services.translator.TranslatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ui.context.EnvironmentContext;

public class EntryIconValidator implements Validator {

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private FavoritesService favoritesService;

	@Override
	public boolean supports( final Class<?> clazz ) {
		return clazz.isAssignableFrom( BookmarkEntryDTO.class );
	}

	@Override
	public void validate( final Object target, final Errors errors ) {
		final BookmarkEntryDTO entryDTO = ( BookmarkEntryDTO ) target;

		validateAction( entryDTO, errors );
	}

	private void validateAction( final BookmarkEntryDTO entryDTO, final Errors errors ) {

		final boolean isBookmarked = favoritesService.isEntryInFavorites( entryDTO.getUserId(), entryDTO.getBookmarkEntryId(), entryDTO.getBookmarkEntryTypeId() );

		if ( isBookmarked && entryDTO.isAdding() ) {
			final String errorMessage = translatorService.translate( ""
																	 + "", EnvironmentContext.getLanguage() );
			errors.rejectValue( "userId", "error code", errorMessage );
		}
	}
}
