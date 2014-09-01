package rest.users.albums;

import core.general.user.userAlbums.UserPhotoAlbum;
import core.services.translator.Language;
import core.services.user.UserPhotoAlbumService;
import core.services.user.UserService;
import core.services.utils.UrlUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ui.context.EnvironmentContext;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/users/{userId}/albums" )
public class UserAlbumsController {

	@Autowired
	private UserPhotoAlbumService userPhotoAlbumService;

	@Autowired
	private UrlUtilsService urlUtilsService;

	@Autowired
	private UserService userService;

	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	@ResponseBody
	public List<UserAlbumDTO> userTeam( final @PathVariable( "userId" ) int userId ) {
		return getUserAlbumDTOs( userId );
	}

	@RequestMapping( method = RequestMethod.POST, value = "/", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	@ResponseBody
	public UserAlbumDTO createUserTeamMember( @RequestBody final UserAlbumDTO dto ) {
		doSaveUserAlbum( dto );
		return dto;
	}

	@RequestMapping( method = RequestMethod.PUT, value = "/{userAlbumId}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	@ResponseBody
	public UserAlbumDTO saveUserTeamMember( @RequestBody final UserAlbumDTO dto ) {
		doSaveUserAlbum( dto );
		return dto;
	}

	@RequestMapping( method = RequestMethod.DELETE, value = "/{userAlbumId}" )
	@ResponseBody
	public boolean deleteUserTeamMember( final @PathVariable( "userAlbumId" ) int userAlbumId ) {
		return userPhotoAlbumService.delete( userAlbumId );
	}

	private List<UserAlbumDTO> getUserAlbumDTOs( final int userId ) {
		final ArrayList<UserAlbumDTO> result = newArrayList();

		final List<UserPhotoAlbum> albums = userPhotoAlbumService.loadAllForEntry( userId );

		for ( final UserPhotoAlbum album : albums ) {
			final UserAlbumDTO dto = new UserAlbumDTO();
			dto.setUserAlbumId( album.getId() );
			dto.setUserId( userId );
			dto.setAlbumName( album.getName() ); // TODO: escaping
			dto.setAlbumLink( urlUtilsService.getUserPhotoAlbumPhotosLink( userId, album.getId() ) );
			dto.setAlbumPhotosQty( getPhotosQty( album ) );

			result.add( dto );
		}

		return result;
	}

	private void doSaveUserAlbum( final UserAlbumDTO dto ) {
		final UserPhotoAlbum album = new UserPhotoAlbum();
		album.setId( dto.getUserAlbumId() );
		album.setName( dto.getAlbumName() );
		album.setUser( userService.load( dto.getUserId() ) );
		album.setDescription( "" ); // TODO

		userPhotoAlbumService.save( album );

		dto.setUserAlbumId( album.getId() );
		dto.setAlbumPhotosQty( getPhotosQty( album ) );
	}

	private int getPhotosQty( final UserPhotoAlbum album ) {
		return userPhotoAlbumService.getUserPhotoAlbumPhotosQty( album.getId() );
	}

	private Language getLanguage() {
		return EnvironmentContext.getLanguage();
	}
}
