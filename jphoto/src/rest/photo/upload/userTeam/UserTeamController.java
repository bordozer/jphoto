package rest.photo.upload.userTeam;

import core.enums.UserTeamMemberType;
import core.general.user.userTeam.UserTeam;
import core.general.user.userTeam.UserTeamMember;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.user.UserTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ui.context.EnvironmentContext;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/users/{userId}/team/" )
public class UserTeamController {

	@Autowired
	private UserTeamService userTeamService;

	@Autowired
	private TranslatorService translatorService;

	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	@ResponseBody
	public List<UserTeamMemberDTO> userTeam( final @PathVariable( "userId" ) int userId, final HttpServletRequest request ) {

		return getUserTeamMemberDTOs( userId );
	}

	@RequestMapping( method = RequestMethod.POST, value = "/", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	@ResponseBody
	public List<UserTeamMemberDTO> createUserTeamMember( @RequestBody final UserTeamMemberDTO userTeamMemberDTO, final @PathVariable( "userId" ) int userId, final HttpServletRequest request ) {

		final UserTeamMember teamMember = new UserTeamMember();
		teamMember.setUser( EnvironmentContext.getCurrentUser() );
		teamMember.setName( userTeamMemberDTO.getUserTeamMemberName() );
		teamMember.setTeamMemberType( UserTeamMemberType.PHOTOGRAPH ); // TODO
//		teamMember.setTeamMemberUser(  ); // TODO

		userTeamService.save( teamMember );

		return getUserTeamMemberDTOs( userId );
	}

	@RequestMapping( method = RequestMethod.PUT, value = "/{userTeamMemberId}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	@ResponseBody
	public List<UserTeamMemberDTO> saveUserTeamMember( @RequestBody final UserTeamMemberDTO userTeamMemberDTO, final @PathVariable( "userId" ) int userId, final @PathVariable( "userTeamMemberId" ) int userTeamMemberId, final HttpServletRequest request ) {

		final UserTeamMember teamMember = new UserTeamMember();
		teamMember.setUser( EnvironmentContext.getCurrentUser() );
		teamMember.setId( userTeamMemberDTO.getUserTeamMemberId() );
		teamMember.setName( userTeamMemberDTO.getUserTeamMemberName() );
		teamMember.setTeamMemberType( UserTeamMemberType.PHOTOGRAPH ); // TODO
//		teamMember.setTeamMemberUser(  ); // TODO

		userTeamService.save( teamMember );

		return getUserTeamMemberDTOs( userId );
	}

	private List<UserTeamMemberDTO> getUserTeamMemberDTOs( final int userId ) {
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
