package json.photo.upload;

import core.general.user.User;
import core.services.entry.GenreService;
import core.services.photo.PhotoUploadService;
import core.services.system.ConfigurationService;
import core.services.translator.TranslatorService;
import core.services.user.UserRankService;
import core.services.utils.DateUtilsService;
import core.services.utils.ImageFileUtilsService;
import json.photo.upload.description.AbstractPhotoUploadAllowance;
import json.photo.upload.description.UploadDescriptionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ui.context.EnvironmentContext;

@Controller
@RequestMapping( "users/{userId}" )
public class PhotoUploadAllowanceController {

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private GenreService genreService;

	@Autowired
	private PhotoUploadService photoUploadService;

	@Autowired
	private UserRankService userRankService;

	@Autowired
	private ImageFileUtilsService imageFileUtilsService;

	private AbstractPhotoUploadAllowance setPhotoUploadAllowance( final int genreId, final User user ) {
		final AbstractPhotoUploadAllowance uploadAllowance = UploadDescriptionFactory.getInstance( user, EnvironmentContext.getCurrentUser() );

		uploadAllowance.setConfigurationService( configurationService );
		uploadAllowance.setPhotoUploadService( photoUploadService );
		uploadAllowance.setUserRankService( userRankService );
		uploadAllowance.setDateUtilsService( dateUtilsService );
		uploadAllowance.setImageFileUtilsService( imageFileUtilsService );
		uploadAllowance.setTranslatorService( translatorService );

		uploadAllowance.setUploadThisWeekPhotos( photoUploadService.getUploadedThisWeekPhotos( user.getId() ) );
		uploadAllowance.setGenre( genreService.load( genreId ) );

		return uploadAllowance;
	}
}
