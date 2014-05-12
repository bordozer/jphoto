package json.icon;

import core.enums.FavoriteEntryType;
import core.general.favorite.FavoriteEntry;
import core.services.entry.FavoritesService;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.utils.DateUtilsService;
import core.services.utils.UrlUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ui.context.EnvironmentContext;

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

	@RequestMapping( method = RequestMethod.GET, value = "/", produces = "application/json" )
	@ResponseBody
	public BookmarkEntryDTO renderEntryIcon(
		final @PathVariable( "userId" ) int userId
		, final @PathVariable( "bookmarkEntryId" ) int bookmarkEntryId
		, final @PathVariable( "bookmarkEntryTypeId" ) int bookmarkEntryTypeId
	) {
		return getBookmarkEntryDTO( userId, bookmarkEntryId, bookmarkEntryTypeId );
	}

	@RequestMapping( method = RequestMethod.POST, value = "/", produces = "application/json" )
	@ResponseBody
	public BookmarkEntryDTO doEntryIconAction(
		final @PathVariable( "userId" ) int userId
		, final @PathVariable( "bookmarkEntryId" ) int bookmarkEntryId
		, final @PathVariable( "bookmarkEntryTypeId" ) int bookmarkEntryTypeId
	) {

		final FavoriteEntryType bookmarkEntryType = FavoriteEntryType.getById( bookmarkEntryTypeId );
		final String typeName = translatorService.translate( bookmarkEntryType.getName(), getLanguage() );

		String saveCallbackMessage = "";
		if ( favoritesService.isEntryInFavorites( userId, bookmarkEntryId, bookmarkEntryTypeId ) ) {
			favoritesService.removeEntryFromFavorites( userId, bookmarkEntryId, bookmarkEntryType );
			saveCallbackMessage = translatorService.translate( "Successfully removed from $1", getLanguage(), typeName );
		} else {
			favoritesService.addEntryToFavorites( userId, bookmarkEntryId, dateUtilsService.getCurrentTime(), bookmarkEntryType );
			saveCallbackMessage = translatorService.translate( "Successfully added to $1", getLanguage(), typeName );
		}

		final BookmarkEntryDTO bookmarkEntryDTO = getBookmarkEntryDTO( userId, bookmarkEntryId, bookmarkEntryTypeId );
		bookmarkEntryDTO.setSaveCallbackMessage( saveCallbackMessage );

		return bookmarkEntryDTO;
	}

	private BookmarkEntryDTO getBookmarkEntryDTO( final int userId, final int bookmarkEntryId, final int bookmarkEntryTypeId ) {

		final FavoriteEntryType bookmarkEntryType = FavoriteEntryType.getById( bookmarkEntryTypeId );
		final FavoriteEntry bookmarkEntry = favoritesService.getFavoriteEntry( userId, bookmarkEntryId, bookmarkEntryType );

		String title;
		String icon;
		if ( bookmarkEntry == null ) {
			title = translatorService.translate( bookmarkEntryType.getAddText(), getLanguage() );
			icon = String.format( "%s/favorites/%s", urlUtilsService.getSiteImagesPath(), bookmarkEntryType.getAddIcon() );
		} else {
			title = translatorService.translate( bookmarkEntryType.getRemoveText(), getLanguage() );
			icon = String.format( "%s/favorites/%s", urlUtilsService.getSiteImagesPath(), bookmarkEntryType.getRemoveIcon() );
		}

		return new BookmarkEntryDTO( userId, bookmarkEntryId, bookmarkEntryTypeId, title, icon );
	}

	private Language getLanguage() {
		return EnvironmentContext.getLanguage();
	}

	/*private class BookmarkedEntryDTO extends BookmarkEntryDTO {

	}*/
}
