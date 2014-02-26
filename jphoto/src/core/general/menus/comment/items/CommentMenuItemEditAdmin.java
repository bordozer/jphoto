package core.general.menus.comment.items;

import core.general.configuration.ConfigurationKey;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.EntryMenuOperationType;
import core.general.menus.comment.AbstractCommentMenuItem;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.security.Services;
import utils.TranslatorUtils;

public class CommentMenuItemEditAdmin extends AbstractCommentMenuItem {

	public CommentMenuItemEditAdmin( final PhotoComment photoComment, final User accessor, final Services services ) {
		super( photoComment, accessor, services );
	}

	@Override
	public EntryMenuOperationType getEntryMenuType() {
		return EntryMenuOperationType.ADMIN_MENU_ITEM_EDIT;
	}

	@Override
	public AbstractEntryMenuItemCommand getMenuItemCommand() {
		return new AbstractEntryMenuItemCommand( getEntryMenuType() ) {

			@Override
			public String getMenuText() {
				return TranslatorUtils.translate(  "Edit comment" );
			}

			@Override
			public String getMenuCommand() {
				return String.format( "editComment( %d ); return false;", getId() );
			}
		};
	}

	@Override
	public boolean isAccessibleFor() {
		return !menuEntry.isCommentDeleted()
			   && getSecurityService().isSuperAdminUser( accessor )
			   && services.getConfigurationService().getBoolean( ConfigurationKey.ADMIN_CAN_EDIT_PHOTO_COMMENTS );
	}

	@Override
	public String getMenuCssClass() {
		return MENU_ITEM_CSS_CLASS_ADMIN;
	}
}
