package rest.photo.upload.userTeam;

import core.general.user.userTeam.UserTeam;
import core.general.user.userTeam.UserTeamMember;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.user.UserTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ui.context.EnvironmentContext;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Controller
@RequestMapping( "/users/{userId}/team/" )
public class UserTeamController {

	@Autowired
	private UserTeamService userTeamService;

	@Autowired
	private TranslatorService translatorService;

	@RequestMapping( method = RequestMethod.GET, value = "/", produces = "application/json" )
	@ResponseBody
	public List<UserTeamMemberDTO> photoUploadAllowance( final @PathVariable( "userId" ) int userId, final HttpServletRequest request ) {

		final UserTeam userTeam = userTeamService.loadUserTeam( userId );

		final List<UserTeamMemberDTO> result = newArrayList();

		for ( final UserTeamMember userTeamMember : userTeam.getUserTeamMembers() ) {
			final UserTeamMemberDTO dto = new UserTeamMemberDTO( userTeamMember.getId() );
			dto.setUserTeamMemberName( userTeamMember.getTeamMemberName() );

			result.add( dto );
		}

		return result;
	}

	private Language getLanguage() {
		return EnvironmentContext.getLanguage();
	}
}
