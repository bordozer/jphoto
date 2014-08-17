package rest.menu.entry;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
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
import ui.services.menu.entry.EntryMenuService;
import ui.services.menu.entry.items.*;

import java.util.List;

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

		entryMenuDTO.setEntryMenuTypeName( entryMenu.getEntryMenuType().getName() );
		entryMenuDTO.setEntryMenuTitle( entryMenu.getMenuTitle() );
		entryMenuDTO.setEntryMenuHeight( String.valueOf( entryMenu.getMenuHeight() ) );

		entryMenuDTO.setEntryMenuItemDTOs( getMenuItemDTOs( entryId, entryMenu ) );

		return entryMenuDTO;
	}

	private List<EntryMenuItemDTO> getMenuItemDTOs( final int entryId, final EntryMenu entryMenu ) {

		final List<? extends AbstractEntryMenuItem> menuItems = entryMenu.getEntryMenuItems();

		return Lists.transform( menuItems, new Function<AbstractEntryMenuItem, EntryMenuItemDTO>() {

			@Override
			public EntryMenuItemDTO apply( final AbstractEntryMenuItem entryMenuItem ) {

				final EntryMenuItemDTO menuItemDTO = new EntryMenuItemDTO();

				menuItemDTO.setMenuTypeSeparator( entryMenuItem.getEntryMenuType() == EntryMenuOperationType.SEPARATOR );
				menuItemDTO.setMenuCssClassBG( entryMenuItem.getMenuCssClass() );
				menuItemDTO.setCallbackMessage( entryMenuItem.getCallbackMessage() );
				menuItemDTO.setHasSumMenu( entryMenuItem.isSubMenu() );

				if ( entryMenuItem instanceof SubmenuAccessible ) {
					final SubmenuAccessible submenuAccessible = ( SubmenuAccessible ) entryMenuItem;
					final EntryMenu entrySubMenu = submenuAccessible.getEntrySubMenu();
					menuItemDTO.setEntrySubMenuItemDTOs( getMenuItemDTOs( entryId, entrySubMenu ) );
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
