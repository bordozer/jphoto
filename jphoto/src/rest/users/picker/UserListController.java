package rest.users.picker;

import core.enums.UserGender;
import core.general.user.User;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.user.UserService;
import core.services.utils.EntityLinkUtilsService;
import core.services.utils.UrlUtilsService;
import core.services.utils.UserPhotoFilePathUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ui.context.EnvironmentContext;
import utils.NumberUtils;
import utils.StringUtilities;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/users" )
public class UserListController {

	@Autowired
	private UserService userService;

	@Autowired
	private EntityLinkUtilsService entityLinkUtilsService;

	@Autowired
	private UserPhotoFilePathUtilsService userPhotoFilePathUtilsService;

	@Autowired
	private UrlUtilsService urlUtilsService;

	@Autowired
	private TranslatorService translatorService;

	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	@ResponseBody
	public UserPickerDTO showUserPicker( final HttpServletRequest request ) {
		final int userId = NumberUtils.convertToInt( request.getParameter( "userId" ) );

		final User user = userService.load( userId );

		final UserPickerDTO userPickerDTO = getUserPickerDTO( user != null ? user.getNameEscaped() : "" );

		if ( user != null ) {
			userPickerDTO.setSelectedUserDTO( getUserDTO( user ) );
		}

		return userPickerDTO;
	}

	@RequestMapping( method = RequestMethod.POST, value = "/", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	@ResponseBody
	public UserPickerDTO search( @RequestBody final UserPickerDTO requestDTO ) {
		final UserPickerDTO dto = getUserPickerDTO( requestDTO.getSearchString() );
		requestDTO.setUserDTOs( requestDTO.getUserDTOs() );

		return dto;
	}

	private UserPickerDTO getUserPickerDTO( final String searchString ) {

		if ( StringUtils.isEmpty( searchString ) ) {
			return getEmptyUserPickerDTO( searchString );
		}

		final List<User> users = userService.searchByPartOfName( searchString );

		if ( users.size() == 0 ) {
			return getEmptyUserPickerDTO( searchString );
		}

		final List<UserDTO> userDTOs = newArrayList();

		for ( final User user : users ) {
			userDTOs.add( getUserDTO( user ) );
		}

		final UserPickerDTO userPickerDTO = new UserPickerDTO();
		userPickerDTO.setUserDTOs( userDTOs );
		userPickerDTO.setSearchString( searchString );

		return userPickerDTO;
	}

	private UserDTO getUserDTO( final User user ) {
		final UserDTO userDTO = new UserDTO();

		userDTO.setUserId( String.valueOf( user.getId() ) );
		userDTO.setUserName( user.getName() );
		userDTO.setUserNameEscaped( StringUtilities.escapeHtml( user.getName() ) );
		userDTO.setUserCardLink( entityLinkUtilsService.getUserCardLink( user, EnvironmentContext.getLanguage() ) );
		userDTO.setUserCardLinkList( entityLinkUtilsService.getUserCardLink( user, "card", EnvironmentContext.getLanguage() ) );
		userDTO.setUserAvatarUrl( userPhotoFilePathUtilsService.getUserAvatarFileUrl( user.getId() ) );
		userDTO.setUserGender( translatorService.translate( user.getGender().getName(), EnvironmentContext.getLanguage() ) );
		userDTO.setUserGenderIcon( String.format( "<img src='%s/icons16/%s' alt='gender' width='16' height='16' />", urlUtilsService.getSiteImagesPath(), ( user.getGender() == UserGender.MALE ? "male.png" : "female.png" ) ) );

		return userDTO;
	}

	private UserPickerDTO getEmptyUserPickerDTO( final String searchString ) {
		final UserPickerDTO userPickerDTO = new UserPickerDTO();
		userPickerDTO.setUserDTOs( newArrayList() );
		userPickerDTO.setSearchString( searchString );

		return userPickerDTO;
	}

	private Language getLanguage() {
		return EnvironmentContext.getLanguage();
	}
}
