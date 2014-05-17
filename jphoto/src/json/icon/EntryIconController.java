package json.icon;

import core.enums.FavoriteEntryType;
import core.services.entry.FavoritesService;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.utils.DateUtilsService;
import core.services.utils.UrlUtilsService;
import json.photo.appraisal.ValidationException;
import json.photo.appraisal.ValidationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ui.context.EnvironmentContext;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping( "bookmarks/{userId}/{bookmarkEntryId}/{bookmarkEntryTypeId}" )
@Controller
public class EntryIconController {

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private FavoritesService favoritesService;

	@Autowired
	private UrlUtilsService urlUtilsService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private EntryIconValidator entryIconValidator;

	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	@ResponseBody
	public BookmarkEntryDTO renderEntryIcon(
		final @PathVariable( "userId" ) int userId
		, final @PathVariable( "bookmarkEntryId" ) int bookmarkEntryId
		, final @PathVariable( "bookmarkEntryTypeId" ) int bookmarkEntryTypeId
	) {
		return getBookmarkEntryDTO( userId, bookmarkEntryId, bookmarkEntryTypeId );
	}

	@RequestMapping( method = RequestMethod.POST, value = "/", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	@ResponseBody
	public BookmarkEntryDTO doEntryIconAction( @RequestBody final BookmarkEntryDTO entryDTO ) {

		ValidationHelper.validate( entryDTO, entryIconValidator );

		final int userId = entryDTO.getUserId();

		final FavoriteEntryType bookmarkEntryType = FavoriteEntryType.getById( entryDTO.getBookmarkEntryTypeId() );
		final String typeName = translatorService.translate( bookmarkEntryType.getName(), getLanguage() );

		String saveCallbackMessage = "";
		if ( favoritesService.isEntryInFavorites( userId, entryDTO.getBookmarkEntryId(), entryDTO.getBookmarkEntryTypeId() ) ) {
			favoritesService.removeEntryFromFavorites( userId, entryDTO.getBookmarkEntryId(), bookmarkEntryType );
			saveCallbackMessage = translatorService.translate( "Successfully removed from $1", getLanguage(), typeName );
		} else {
			favoritesService.addEntryToFavorites( userId, entryDTO.getBookmarkEntryId(), dateUtilsService.getCurrentTime(), bookmarkEntryType );
			saveCallbackMessage = translatorService.translate( "Successfully added to $1", getLanguage(), typeName );
		}

		final BookmarkEntryDTO bookmarkEntryDTO = getBookmarkEntryDTO( userId, entryDTO.getBookmarkEntryId(), entryDTO.getBookmarkEntryTypeId() );
		bookmarkEntryDTO.setSaveCallbackMessage( saveCallbackMessage );

		return bookmarkEntryDTO;
	}

	@ExceptionHandler( ValidationException.class )
	@ResponseStatus( HttpStatus.UNPROCESSABLE_ENTITY )
	@ResponseBody
	public List<FieldError> processValidationError( final ValidationException validationException ) {
		return validationException.getBindingResult().getFieldErrors();
	}

	private BookmarkEntryDTO getBookmarkEntryDTO( final int userId, final int bookmarkEntryId, final int bookmarkEntryTypeId ) {

		final FavoriteEntryType bookmarkEntryType = FavoriteEntryType.getById( bookmarkEntryTypeId );
		final boolean isInBookmark = favoritesService.isEntryInFavorites( userId, bookmarkEntryId, bookmarkEntryTypeId );

		String title;
		String icon;
		if ( isInBookmark ) {
			title = translatorService.translate( bookmarkEntryType.getRemoveText(), getLanguage() );
			icon = String.format( "%s/favorites/%s", urlUtilsService.getSiteImagesPath(), bookmarkEntryType.getRemoveIcon() );
		} else {
			title = translatorService.translate( bookmarkEntryType.getAddText(), getLanguage() );
			icon = String.format( "%s/favorites/%s", urlUtilsService.getSiteImagesPath(), bookmarkEntryType.getAddIcon() );
		}

		final BookmarkEntryDTO entryDTO = new BookmarkEntryDTO( userId, bookmarkEntryId, bookmarkEntryTypeId, title, icon );
		entryDTO.setAdding( ! isInBookmark );

		return entryDTO;
	}

	private Language getLanguage() {
		return EnvironmentContext.getLanguage();
	}
}
