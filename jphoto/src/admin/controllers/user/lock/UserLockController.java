package admin.controllers.user.lock;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping( "members/{userId}/lock" )
@Controller
public class UserLockController {

	private static final String MODEL_NAME = "userLockModel";

	private static final String VIEW = "admin/user/lock/UserLock";

	@ModelAttribute( MODEL_NAME )
	public UserLockModel prepareModel( final @PathVariable( "userId" ) int userId ) {
		return new UserLockModel( userId );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String showActivityStream( final @ModelAttribute( MODEL_NAME ) UserLockModel model ) {
		return VIEW;
	}
}
