package json;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import core.general.menus.*;
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

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

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

		final EntryMenuDTO entryMenuDTO = new EntryMenuDTO( entryMenuTypeId, entryId );
		entryMenuDTO.setMenuDivId( String.format( "%s-items", entryMenu.getMenuId() ) );
		entryMenuDTO.setMenuId( entryMenu.getMenuId() );

		entryMenuDTO.setEntryMenuTypeName( entryMenu.getEntryMenuType().getName() );
		entryMenuDTO.setEntryMenuTitle( entryMenu.getMenuTitle() );
		entryMenuDTO.setEntryMenuHeight( String.valueOf( entryMenu.getMenuHeight() ) );

//		entryMenuDTO.setMenuItemCssClass( "_class_" );
//		entryMenuDTO.setMenuItemCommand( "alert( 'Hardcoded menu command' );" );
//		entryMenuDTO.setMenuItemCommandText( "_command_text_" );

		entryMenuDTO.setEntryMenuItemDTOs( getMenuItemDTOs( entryId, entryMenu, 0 ) );

		return entryMenuDTO;
	}

	private List<EntryMenuItemDTO> getMenuItemDTOs( final int entryId, final EntryMenu entryMenu, final int deep ) {

		final List<? extends AbstractEntryMenuItem> menuItems = entryMenu.getEntryMenuItems();

		return Lists.transform( menuItems, new Function<AbstractEntryMenuItem, EntryMenuItemDTO>() {

			public int counter = 0;

			@Override
			public EntryMenuItemDTO apply( final AbstractEntryMenuItem entryMenuItem ) {

				final String menuItemId = String.format( "context-menu-item-%d-%d-%d-%d", entryMenu.getEntryMenuType().getId(), entryId, counter++, deep );

				final EntryMenuItemDTO menuItemDTO = new EntryMenuItemDTO( menuItemId );

				menuItemDTO.setMenuTypeSeparator( entryMenuItem.getEntryMenuType() == EntryMenuOperationType.SEPARATOR );
				menuItemDTO.setMenuCssClass( String.format( "%s-%d-%d", entryMenuItem.getMenuCssClass(), deep, counter ) );
				menuItemDTO.setHasSumMenu( entryMenuItem.isSubMenu() );

				if ( entryMenuItem instanceof SubmenuAccesible ) {
					final SubmenuAccesible submenuAccesible = ( SubmenuAccesible ) entryMenuItem;
					final EntryMenu entrySubMenu = submenuAccesible.getEntrySubMenu();
					menuItemDTO.setEntrySubMenuItemDTOs( getMenuItemDTOs( entryId, entrySubMenu, deep + 1 ) );
				}

				final AbstractEntryMenuItemCommand menuItemCommand = entryMenuItem.getMenuItemCommand();
				menuItemDTO.setMenuCommand( menuItemCommand.getMenuCommand() );
				menuItemDTO.setMenuCommandIcon( entryMenuItem.getCommandIcon() );
				menuItemDTO.setMenuCommandText( menuItemCommand.getMenuText() );

				return menuItemDTO;
			}
		} );
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
