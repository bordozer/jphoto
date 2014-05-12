package json.icon;

import core.enums.FavoriteEntryType;
import core.general.favorite.FavoriteEntry;
import core.services.entry.FavoritesService;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
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

	@RequestMapping( method = RequestMethod.GET, value = "/", produces = "application/json" )
	@ResponseBody
	public BookmarkEntryDTO userCardVotingAreas(
		final @PathVariable( "userId" ) int userId
		, final @PathVariable( "bookmarkEntryId" ) int bookmarkEntryId
		, final @PathVariable( "bookmarkEntryTypeId" ) int bookmarkEntryTypeId
	) {

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
}
