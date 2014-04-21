package json;

import core.general.menus.EntryMenu;
import core.general.menus.EntryMenuType;
import core.services.menu.EntryMenuService;
import core.services.photo.PhotoCommentService;
import core.services.photo.PhotoService;
import core.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ui.context.EnvironmentContext;

@RequestMapping( "menu/{entryTypeId}/{entryId}" )
@Controller
public class MenuEntryController {

	@Autowired
	private EntryMenuService entryMenuService;

	@Autowired
	private UserService userService;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private PhotoCommentService photoCommentService;

	@RequestMapping( method = RequestMethod.GET, value = "/", produces = "application/json" )
	@ResponseBody
	public EntryMenu menuEntry( final @PathVariable( "entryTypeId" ) int entryTypeId, final @PathVariable( "entryId" ) int entryId ) {
		final EntryMenuType menuType = EntryMenuType.getById( entryTypeId );
		switch ( menuType ) {
			case USER:
				return entryMenuService.getUserMenu( userService.load( entryId ), EnvironmentContext.getCurrentUser() );
			case PHOTO:
				return entryMenuService.getPhotoMenu( photoService.load( entryId ), EnvironmentContext.getCurrentUser() );
			case COMMENT:
				return entryMenuService.getCommentMenu( photoCommentService.load( entryId ), EnvironmentContext.getCurrentUser() );
		}
		throw new IllegalArgumentException( String.format( "Illegal EntryMenuType id: %d", entryTypeId ) );
	}
}
