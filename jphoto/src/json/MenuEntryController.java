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

@RequestMapping( "menu/{entryMenuTypeId}/{entryId}" )
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
	public EntryMenuDTO menuEntry( final @PathVariable( "entryMenuTypeId" ) int entryMenuTypeId, final @PathVariable( "entryId" ) int entryId ) {

		final EntryMenu entryMenu = getEntryMenuInstance( entryMenuTypeId, entryId );

		final EntryMenuDTO entryMenuDTO = new EntryMenuDTO( entryId );
		entryMenuDTO.setMenuDivId( String.format( "%s-items", entryMenu.getMenuId() ) );
		entryMenuDTO.setMenuId( entryMenu.getMenuId() );

		entryMenuDTO.setEntryMenuTypeName( entryMenu.getEntryMenuType().getName() );
		entryMenuDTO.setEntryMenuTitle( entryMenu.getMenuTitle() );
		entryMenuDTO.setEntryMenuHeight( String.valueOf( entryMenu.getMenuHeight() ) );

		entryMenuDTO.setMenuItemCssClass( "_class_" );
		entryMenuDTO.setMenuItemCommand( "alert( 'Hardcoded menu command' );" );
		entryMenuDTO.setMenuItemCommandText( "_command_text_" );

		return entryMenuDTO;
	}

	private EntryMenu getEntryMenuInstance( final int entryMenuTypeId, final int entryId ) {

		final EntryMenuType menuType = EntryMenuType.getById( entryMenuTypeId );

		switch ( menuType ) {
			case USER:
				return entryMenuService.getUserMenu( userService.load( entryId ), EnvironmentContext.getCurrentUser() );
			case PHOTO:
				return entryMenuService.getPhotoMenu( photoService.load( entryId ), EnvironmentContext.getCurrentUser() );
			case COMMENT:
				return entryMenuService.getCommentMenu( photoCommentService.load( entryId ), EnvironmentContext.getCurrentUser() );
		}

		throw new IllegalArgumentException( String.format( "Illegal EntryMenuType id: %d", entryMenuTypeId ) );
	}
}
