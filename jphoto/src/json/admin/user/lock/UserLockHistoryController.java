package json.admin.user.lock;

import core.general.user.User;
import core.services.user.UserService;
import core.services.utils.EntityLinkUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ui.context.EnvironmentContext;

@RequestMapping( "admin/members/{userId}/lock/history" )
@Controller
public class UserLockHistoryController {

	@Autowired
	private UserService userService;

	@Autowired
	private EntityLinkUtilsService entityLinkUtilsService;

	@RequestMapping( method = RequestMethod.GET, value = "/", produces = "application/json" )
	@ResponseBody
	public UserLockModel userCardVotingAreas( final @PathVariable( "userId" ) int userId ) {
		final UserLockModel model = new UserLockModel( userId );

		final User user = userService.load( userId );

		model.setUserName( user.getNameEscaped() );
		model.setUserCardLink( entityLinkUtilsService.getUserCardLink( user, EnvironmentContext.getLanguage() ) );

		return model;
	}
}
