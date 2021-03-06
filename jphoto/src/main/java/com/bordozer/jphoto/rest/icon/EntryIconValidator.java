package com.bordozer.jphoto.rest.icon;

import com.bordozer.jphoto.core.enums.FavoriteEntryType;
import com.bordozer.jphoto.core.services.entry.FavoritesService;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class EntryIconValidator implements Validator {

    @Autowired
    private TranslatorService translatorService;

    @Autowired
    private FavoritesService favoritesService;

    @Override
    public boolean supports(final Class<?> clazz) {
        return clazz.isAssignableFrom(BookmarkEntryDTO.class);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        final BookmarkEntryDTO entryDTO = (BookmarkEntryDTO) target;

        validateUser(entryDTO, errors);

        validateAction(entryDTO, errors);
    }

    private void validateUser(final BookmarkEntryDTO entryDTO, final Errors errors) {
        final Language language = EnvironmentContext.getLanguage();

        if (!UserUtils.isLoggedUser(EnvironmentContext.getCurrentUserId())) {
            final String errorMessage = translatorService.translate("You are not logged in.", language);
            errors.rejectValue("userId", "error code", errorMessage);

            return;
        }

        if (!UserUtils.isTheUserThatWhoIsCurrentUser(entryDTO.getUserId())) {
            final String errorMessage = translatorService.translate("Wrong user. The user must have changed since the page is loaded.", language);
            errors.rejectValue("userId", "error code", errorMessage);
        }
    }

    private void validateAction(final BookmarkEntryDTO entryDTO, final Errors errors) {

        final Language language = EnvironmentContext.getLanguage();

        final FavoriteEntryType bookmarkEntryType = FavoriteEntryType.getById(entryDTO.getBookmarkEntryTypeId());
        final String name = translatorService.translate(bookmarkEntryType.getName(), language);

        final boolean isBookmarked = favoritesService.isEntryInFavorites(entryDTO.getUserId(), entryDTO.getBookmarkEntryId(), entryDTO.getBookmarkEntryTypeId());

        if (isBookmarked && entryDTO.isAdding()) {
            final String errorMessage = translatorService.translate("The entry is already in $1. You mush have already added it.", language, name);
            errors.rejectValue("userId", "error code", errorMessage);
        }

        if (!isBookmarked && !entryDTO.isAdding()) {
            final String errorMessage = translatorService.translate("The entry is not in $1. You mush have already removed it.", language, name);
            errors.rejectValue("userId", "error code", errorMessage);
        }
    }
}
