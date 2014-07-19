package rest.photo.upload.userTeam;

import core.enums.UserTeamMemberType;
import core.general.photo.Photo;
import core.general.photoTeam.PhotoTeamMember;
import core.general.user.userTeam.UserTeam;
import core.general.user.userTeam.UserTeamMember;
import core.services.photo.PhotoService;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.user.UserTeamService;
import core.services.utils.EntityLinkUtilsService;
import core.services.utils.UrlUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ui.context.EnvironmentContext;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/photos/{photoId}/team/" )
public class UserTeamController {

	@Autowired
	private PhotoService photoService;

	@Autowired
	private UserTeamService userTeamService;

	@Autowired
	private UrlUtilsService urlUtilsService;

	@Autowired
	private EntityLinkUtilsService entityLinkUtilsService;

	@Autowired
	private TranslatorService translatorService;

	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	@ResponseBody
	public List<UserTeamMemberDTO> userTeam( final @PathVariable( "photoId" ) int photoId ) {
		return getUserTeamMemberDTOs( photoId );
	}

	@RequestMapping( method = RequestMethod.POST, value = "/", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	@ResponseBody
	public UserTeamMemberDTO createUserTeamMember( @RequestBody final UserTeamMemberDTO userTeamMemberDTO ) {
		doSaveUserTeamMember( userTeamMemberDTO );
		return userTeamMemberDTO;
	}

	@RequestMapping( method = RequestMethod.PUT, value = "/{userTeamMemberId}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	@ResponseBody
	public UserTeamMemberDTO saveUserTeamMember( @RequestBody final UserTeamMemberDTO userTeamMemberDTO ) {
		doSaveUserTeamMember( userTeamMemberDTO );
		return userTeamMemberDTO;
	}

	@RequestMapping( method = RequestMethod.DELETE, value = "/{userTeamMemberId}" )
	@ResponseBody
	public boolean deleteUserTeamMember( final @PathVariable( "userTeamMemberId" ) int userTeamMemberId ) {
		return userTeamService.delete( userTeamMemberId );
	}

	private void doSaveUserTeamMember( final UserTeamMemberDTO dto ) {

		final UserTeamMember teamMember = new UserTeamMember();

		teamMember.setUser( EnvironmentContext.getCurrentUser() );
		teamMember.setId( dto.getUserTeamMemberId() );
		teamMember.setName( dto.getUserTeamMemberName() );

		final UserTeamMemberType teamMemberType = UserTeamMemberType.getById( dto.getTeamMemberTypeId() );
		teamMember.setTeamMemberType( teamMemberType );
//		teamMember.setTeamMemberUser(  ); // TODO

		userTeamService.save( teamMember );

		dto.setUserTeamMemberId( teamMember.getId() );
		dto.setTeamMemberTypeName( translatorService.translate( teamMemberType.getName(), getLanguage() ) );
		dto.setTeamMemberPhotosQty( userTeamService.getTeamMemberPhotosQty( teamMember.getId() ) );
	}

	private List<UserTeamMemberDTO> getUserTeamMemberDTOs( final int photoId ) {

		final int currentUserId = EnvironmentContext.getCurrentUserId();

		final Photo photo = photoService.load( photoId );
		final boolean isNewPhoto = photo == null;

		final int userId = isNewPhoto ? currentUserId : photo.getUserId();
		final UserTeam userTeam = userTeamService.loadUserTeam( userId );
		final List<PhotoTeamMember> photoTeamMembers = userTeamService.getPhotoTeam( photoId ).getPhotoTeamMembers();

		final List<UserTeamMemberDTO> result = newArrayList();

		for ( final UserTeamMember userTeamMember : userTeam.getUserTeamMembers() ) {
			final UserTeamMemberDTO dto = new UserTeamMemberDTO( userTeamMember.getId() );
			dto.setUserTeamMemberName( userTeamMember.getTeamMemberName() );
			dto.setChecked( !isNewPhoto && isTeamMemberTookParticipationInProcess( userTeamMember, photoTeamMembers ) );
			dto.setUserTeamMemberCardUrl( urlUtilsService.getUserTeamMemberCardLink( currentUserId, userTeamMember.getId() ) );
			dto.setTeamMemberPhotosQty( userTeamService.getTeamMemberPhotosQty( userTeamMember.getId() ) );

			if ( userTeamMember.getTeamMemberUser() != null ) {
				dto.setSiteMemberLink( entityLinkUtilsService.getUserCardLink( userTeamMember.getTeamMemberUser(), getLanguage() ) );
			} else {
				dto.setSiteMemberLink( "" );
			}
			/*final List<GenericTranslatableEntry> entries = GenericTranslatableList.userTeamMemberTypeList( getLanguage(), translatorService ).getEntries();
			final List<TeamMemberTypeDTO> teamMemberTypeDTOs = newArrayList();
			for ( final GenericTranslatableEntry entry : entries ) {
				teamMemberTypeDTOs.add( new TeamMemberTypeDTO( entry.getId(), entry.getName() ) );
			}
			dto.setUserTeamMemberTypes( teamMemberTypeDTOs );*/

			dto.setTeamMemberTypeName( translatorService.translate( userTeamMember.getTeamMemberType().getName(), getLanguage() ) );
			dto.setTeamMemberTypeId( userTeamMember.getTeamMemberType().getId() );

//			dto.setTextNewMemberDefaultName( translatorService.translate( "Photo data / Photo team: New team member default name", getLanguage() ) );

			result.add( dto );
		}

		return result;
	}

	private boolean isTeamMemberTookParticipationInProcess( final UserTeamMember userTeamMember, final List<PhotoTeamMember> photoTeamMembers ) {
		for ( final PhotoTeamMember photoTeamMember : photoTeamMembers ) {
			if (photoTeamMember.getUserTeamMember().equals( userTeamMember ) ) {
				return true;
			}
		}
		return false;
	}

	private Language getLanguage() {
		return EnvironmentContext.getLanguage();
	}
}
