package admin.controllers.user.lock;

import core.general.user.User;
import core.services.user.UserService;
import core.services.utils.EntityLinkUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping( "members/{userId}/lock" )
@Controller
public class UserLockController {

	@Autowired
	private EntityLinkUtilsService entityLinkUtilsService;

	@Autowired
	private UserService userService;

	private static final String MODEL_NAME = "userLockModel";

	private static final String VIEW = "admin/user/lock/UserLock";

	@ModelAttribute( MODEL_NAME )
	public UserLockModel prepareModel( final @PathVariable( "userId" ) int userId ) {
		final User user = userService.load( userId );

		final UserLockModel model = new UserLockModel( userId );

		model.setUserName( user.getNameEscaped() );

		return model;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String showActivityStream( final @ModelAttribute( MODEL_NAME ) UserLockModel model ) {
		return VIEW;
	}
}
