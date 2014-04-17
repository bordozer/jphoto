package json.admin.user.lock;

import core.services.user.UserService;
import core.services.utils.EntityLinkUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@RequestMapping( "admin/members/{userId}/lock/history" )
@Controller
public class UserLockHistoryController {

//	@Autowired
//	private UserService userService;

//	@Autowired
//	private EntityLinkUtilsService entityLinkUtilsService;

	@RequestMapping( method = RequestMethod.GET, value = "/", produces = "application/json" )
	@ResponseBody
	public List<UserLockHistoryDTO> userCardVotingAreas( final @PathVariable( "userId" ) int userId ) {
		/*final UserLockModel model = new UserLockModel( userId );

		final User user = userService.load( userId );

		model.setUserName( user.getNameEscaped() );
		model.setUserCardLink( entityLinkUtilsService.getUserCardLink( user, EnvironmentContext.getLanguage() ) );*/

		final List<UserLockHistoryDTO> result = newArrayList();

		result.add( new UserLockHistoryDTO( 1 ) );
		result.add( new UserLockHistoryDTO( 2 ) );
		result.add( new UserLockHistoryDTO( 3 ) );

		return result;
	}
}
