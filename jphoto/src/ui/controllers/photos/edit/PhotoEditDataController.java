package ui.controllers.photos.edit;

import core.general.genre.Genre;
import core.general.photo.Photo;
import core.services.entry.GenreService;
import core.services.photo.PhotoService;
import core.services.security.SecurityService;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.utils.TempFileUtilsService;
import core.services.utils.UrlUtilsService;
import core.services.utils.UrlUtilsServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ui.context.EnvironmentContext;
import ui.services.breadcrumbs.BreadcrumbsPhotoService;
import utils.NumberUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@SessionAttributes( {PhotoEditDataController.MODEL_NAME} )
@Controller
@RequestMapping( UrlUtilsServiceImpl.PHOTOS_URL )
public class PhotoEditDataController {

	public static final String MODEL_NAME = "photoEditDataModel";

	private static final String VIEW_UPLOAD_FILE = "photos/edit/PhotoUpload";
	private static final String VIEW_EDIT_DATA = "photos/edit/PhotoEditData";

	private static final String ONLY_LOGGED_USER_CAN_UPLOAD_A_PHOTO_MESSAGE = "Only logged user can upload a photo";

	@Autowired
	private SecurityService securityService;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private BreadcrumbsPhotoService breadcrumbsPhotoService;

	@Autowired
	private TempFileUtilsService tempFileUtilsService;

	@Autowired
	private UrlUtilsService urlUtilsService;

	@Autowired
	private GenreService genreService;

	@Autowired
	private TranslatorService translatorService;

	@ModelAttribute( MODEL_NAME )
	public PhotoEditDataModel prepareModel() {
		return new PhotoEditDataModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "/new/" )
	public String photoFileUpload( final @ModelAttribute( MODEL_NAME ) PhotoEditDataModel model ) {

		securityService.assertCurrentUserIsLogged( ONLY_LOGGED_USER_CAN_UPLOAD_A_PHOTO_MESSAGE );

		model.clear();

		model.setNew( true );
		model.setPhoto( new Photo() );

		model.setPageTitleData( breadcrumbsPhotoService.getUploadPhotoBreadcrumbs( EnvironmentContext.getCurrentUser() ) );

		return VIEW_UPLOAD_FILE;
	}

	@RequestMapping( method = RequestMethod.POST, value = "/new/" )
	public String photoUploaded( final @ModelAttribute( MODEL_NAME ) PhotoEditDataModel model ) throws IOException {

		final MultipartFile photoFile = model.getPhotoFile();

		if ( photoFile == null || StringUtils.isEmpty( photoFile.getOriginalFilename() ) ) {
			return VIEW_UPLOAD_FILE;
		}

		final File tempFile = tempFileUtilsService.getTempFileWithOriginalExtension( EnvironmentContext.getCurrentUser(), photoFile.getOriginalFilename() );

		photoFile.transferTo( tempFile );
		model.setTempPhotoFile( tempFile );
		model.setGenreWrappers( getGenreWrappers() );

		model.setPageTitleData( breadcrumbsPhotoService.getUploadPhotoBreadcrumbs( EnvironmentContext.getCurrentUser() ) );

		return VIEW_EDIT_DATA;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{photoId}/edit/" )
	public String editPhoto( final @PathVariable( "photoId" ) String _photoId, final @ModelAttribute( MODEL_NAME ) PhotoEditDataModel model ) {

		assertPhotoExistsAndCurrentUserCanEditIt( _photoId );

		final int photoId = NumberUtils.convertToInt( _photoId );

		final Photo photo = photoService.load( photoId );

		model.clear();
		model.setNew( false );
		model.setPhoto( photo );
		model.setGenreWrappers( getGenreWrappers() );

		model.setPageTitleData( breadcrumbsPhotoService.getPhotoEditDataBreadcrumbs( photo ) );

		return VIEW_EDIT_DATA;
	}

	@RequestMapping( method = RequestMethod.POST, value = "/save/" )
	public String editPhoto( final @ModelAttribute( MODEL_NAME ) PhotoEditDataModel model ) {

//		model.setPhoto(  ); // TODO: set saved photo

//		return String.format( "redirect:%s", urlUtilsService.getPhotoCardLink( model.getPhoto().getId() ) );
		return VIEW_EDIT_DATA;
	}

	private void assertPhotoExistsAndCurrentUserCanEditIt( final String _photoId ) {

		securityService.assertPhotoExists( _photoId );

		final int photoId = NumberUtils.convertToInt( _photoId );
		final Photo photo = photoService.load( photoId );

		securityService.assertUserCanEditPhoto( EnvironmentContext.getCurrentUser(), photo );
	}

	private List<GenreWrapper> getGenreWrappers() {
		final Language language = EnvironmentContext.getLanguage();

		final List<GenreWrapper> result = newArrayList();

		final List<Genre> genres = genreService.loadAllSortedByNameForLanguage( language );
		for ( final Genre genre : genres ) {
			final GenreWrapper wrapper = new GenreWrapper( genre );
			wrapper.setGenreNameTranslated( translatorService.translateGenre( genre, language ) );

			result.add( wrapper );
		}

		return result;
	}
}
