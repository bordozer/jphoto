package json.admin.user.lock;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping( "admin/members/{userId}/lock" )
@Controller
public class UserLockController {

	@RequestMapping( method = RequestMethod.GET, value = "/", produces = "application/json" )
	@ResponseBody
	public UserLockModel userCardVotingAreas( final @PathVariable( "userId" ) int userId ) {
		final UserLockModel model = new UserLockModel( userId );
		return model;
	}
}
