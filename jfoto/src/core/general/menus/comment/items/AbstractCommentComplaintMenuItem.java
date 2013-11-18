package core.general.menus.comment.items;

import core.general.photo.PhotoComment;
import core.general.user.User;
import core.general.menus.comment.AbstractCommentMenuItem;

public abstract class AbstractCommentComplaintMenuItem extends AbstractCommentMenuItem {

	@Override
	public boolean isAccessibleForComment( final PhotoComment photoComment, final User userWhoIsCallingMenu ) {
		return isUserWhoIsCallingMenuLogged( userWhoIsCallingMenu )
			   && !isSuperAdminUser( userWhoIsCallingMenu.getId() )
			   && !isCommentLeftByUserWhoIsCallingMenu( photoComment, userWhoIsCallingMenu );
	}
}
