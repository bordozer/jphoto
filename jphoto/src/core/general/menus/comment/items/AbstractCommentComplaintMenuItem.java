package core.general.menus.comment.items;

import core.general.photo.PhotoComment;
import core.general.user.User;
import core.general.menus.comment.AbstractCommentMenuItem;
import core.services.security.Services;

public abstract class AbstractCommentComplaintMenuItem extends AbstractCommentMenuItem {

	public AbstractCommentComplaintMenuItem( final PhotoComment photoComment, final User accessor, final Services services ) {
		super( photoComment, accessor, services );
	}

	@Override
	public boolean isAccessibleFor( final PhotoComment photoComment, final User accessor ) {
		return isUserWhoIsCallingMenuLogged( accessor )
			   && !isSuperAdminUser( accessor.getId() )
			   && !isCommentLeftByUserWhoIsCallingMenu( photoComment, accessor );
	}
}
