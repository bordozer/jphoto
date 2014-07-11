package rest.admin.user.lock;

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

	@RequestMapping( method = RequestMethod.GET, value = "/", produces = "application/json" )
	@ResponseBody
	public List<UserLockHistoryDTO> userCardVotingAreas( final @PathVariable( "userId" ) int userId ) {

		final List<UserLockHistoryDTO> result = newArrayList();

		result.add( new UserLockHistoryDTO( 1 ) );
		result.add( new UserLockHistoryDTO( 2 ) );
		result.add( new UserLockHistoryDTO( 3 ) );

		return result;
	}
}
