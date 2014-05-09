package json.photo.upload;

import core.general.genre.Genre;
import core.general.user.User;
import core.services.entry.GenreService;
import core.services.photo.PhotoUploadService;
import core.services.system.ConfigurationService;
import core.services.system.Services;
import core.services.translator.TranslatorService;
import core.services.user.UserRankService;
import core.services.user.UserService;
import core.services.utils.DateUtilsService;
import core.services.utils.ImageFileUtilsService;
import json.photo.upload.description.AbstractPhotoUploadAllowance;
import json.photo.upload.description.UploadDescriptionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ui.context.EnvironmentContext;

@Controller
@RequestMapping( "users/{userId}/photo-upload-allowance/{genreId}" )
public class PhotoUploadAllowanceController {

	@Autowired
	private UserService userService;

	@Autowired
	private GenreService genreService;

	@Autowired
	private PhotoUploadService photoUploadService;

	@Autowired
	private Services services;

	@RequestMapping( method = RequestMethod.GET, value = "/", produces = "application/json" )
	@ResponseBody
	public PhotoUploadAllowanceDTO photoUploadAllowance( final @PathVariable( "userId" ) int userId, final @PathVariable( "genreId" ) int genreId ) {

		final User user = userService.load( userId );
		final Genre genre = genreService.load( genreId );

		final PhotoUploadAllowanceDTO photoUploadAllowanceDTO = new PhotoUploadAllowanceDTO();
		photoUploadAllowanceDTO.setUseId( userId );
		photoUploadAllowanceDTO.setGenreId( genreId );

		if ( genre != null ) {
			final AbstractPhotoUploadAllowance photoUploadAllowance = getPhotoUploadAllowance( user, genre );
			photoUploadAllowanceDTO.setPhotoUploadAllowance( photoUploadAllowance.getUploadAllowance() );
		}

		return photoUploadAllowanceDTO;
	}

	private AbstractPhotoUploadAllowance getPhotoUploadAllowance( final User user, final Genre genre ) {

		final AbstractPhotoUploadAllowance uploadAllowance = UploadDescriptionFactory.getInstance( user, EnvironmentContext.getCurrentUser(), EnvironmentContext.getLanguage(), services );

		uploadAllowance.setUploadThisWeekPhotos( photoUploadService.getUploadedThisWeekPhotos( user.getId() ) );
		uploadAllowance.setGenre( genre );

		return uploadAllowance;
	}
}
