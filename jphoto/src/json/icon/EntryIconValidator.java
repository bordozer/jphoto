package json.icon;

import core.enums.FavoriteEntryType;
import core.services.entry.FavoritesService;
import core.services.translator.Language;
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

		final Language language = EnvironmentContext.getLanguage();

		final FavoriteEntryType bookmarkEntryType = FavoriteEntryType.getById( entryDTO.getBookmarkEntryTypeId() );
		final String name = translatorService.translate( bookmarkEntryType.getName(), language );

		final boolean isBookmarked = favoritesService.isEntryInFavorites( entryDTO.getUserId(), entryDTO.getBookmarkEntryId(), entryDTO.getBookmarkEntryTypeId() );

		if ( isBookmarked && entryDTO.isAdding() ) {
			final String errorMessage = translatorService.translate( "The entry is already in $1. You mush have already added it.", language, name );
			errors.rejectValue( "userId", "error code", errorMessage );
		}

		if ( ! isBookmarked && ! entryDTO.isAdding() ) {
			final String errorMessage = translatorService.translate( "The entry is not in $1. You mush have already removed it.", language, name );
			errors.rejectValue( "userId", "error code", errorMessage );
		}
	}
}
