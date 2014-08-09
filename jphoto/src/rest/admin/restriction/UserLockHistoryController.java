package rest.admin.restriction;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping( "admin/members/{userId}/lock/history" )
@Controller
public class UserLockHistoryController {

	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	@ResponseBody
	public List<UserLockHistoryDTO> userCardVotingAreas( final @PathVariable( "userId" ) int userId ) {

		final List<UserLockHistoryDTO> result = newArrayList();

		result.add( new UserLockHistoryDTO( 1 ) );
		result.add( new UserLockHistoryDTO( 2 ) );
		result.add( new UserLockHistoryDTO( 3 ) );

		return result;
	}
}
