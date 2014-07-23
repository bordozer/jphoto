package rest.users.picker;

import core.general.user.User;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ui.context.EnvironmentContext;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/users" )
public class UserListController {

	@Autowired
	private UserService userService;

	@Autowired
	private TranslatorService translatorService;

	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	@ResponseBody
	public UserPickerDTO getUsers() {

		final List<UserDTO> userDTOs = newArrayList();

		final List<User> users = userService.loadAll().subList( 0, 10 );
		for ( final User user : users ) {
			final UserDTO dto = new UserDTO();
			dto.setUserId( user.getId() );
			dto.setUserName( user.getNameEscaped() );
			dto.setUserMembershipTypeName( translatorService.translate( user.getMembershipType().getName(), getLanguage() ) );

			userDTOs.add( dto );
		}

		final UserPickerDTO userPickerDTO = new UserPickerDTO();
		userPickerDTO.setUserDTOs( userDTOs );

		return userPickerDTO;
	}

	private Language getLanguage() {
		return EnvironmentContext.getLanguage();
	}
}
