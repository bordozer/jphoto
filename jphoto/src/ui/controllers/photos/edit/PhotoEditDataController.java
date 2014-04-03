package ui.controllers.photos.edit;

import ui.controllers.photos.edit.description.AbstractPhotoUploadAllowance;
import ui.controllers.photos.edit.description.UploadDescriptionFactory;
import core.context.EnvironmentContext;
import core.enums.YesNo;
import core.exceptions.SaveToDBException;
import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.photoTeam.PhotoTeam;
import core.general.photoTeam.PhotoTeamMember;
import core.general.user.EmailNotificationType;
import core.general.user.User;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.general.user.userTeam.UserTeamMember;
import core.log.LogHelper;
import core.services.conversion.PreviewGenerationService;
import core.services.entry.AnonymousDaysService;
import core.services.entry.GenreService;
import core.services.photo.PhotoService;
import core.services.photo.PhotoUploadService;
import core.services.security.SecurityService;
import core.services.system.ConfigurationService;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.user.UserPhotoAlbumService;
import core.services.user.UserRankService;
import core.services.user.UserService;
import core.services.user.UserTeamService;
import core.services.utils.*;
import core.services.validation.DataRequirementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ui.services.breadcrumbs.BreadcrumbsPhotoService;
import utils.NumberUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;

@SessionAttributes( {PhotoEditDataController.MODEL_NAME} )
@Controller
@RequestMapping( UrlUtilsServiceImpl.PHOTOS_URL )
public class PhotoEditDataController {

	public static final String MODEL_NAME = "photoEditDataModel";

	private static final String DATA_VIEW = "photos/edit/PhotoEditData";
	private static final String FILE_UPLOAD_VIEW = "photos/edit/PhotoEditFileUpload";

	private static final String ONLY_LOGGED_USER_CAN_UPLOAD_A_PHOTO_MESSAGE = "Only logged user can upload a photo";

	@Autowired
	PhotoEditDataValidator photoEditDataValidator;

	@Autowired
	private UserService userService;

	@Autowired
	private GenreService genreService;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private PreviewGenerationService previewGenerationService;

	@Autowired
	private PhotoUploadService photoUploadService;

	@Autowired
	private UserRankService userRankService;

	@Autowired
	private UserTeamService userTeamService;

	@Autowired
	private UserPhotoAlbumService userPhotoAlbumService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private AnonymousDaysService anonymousDaysService;

	@Autowired
	private SystemVarsService systemVarsService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private UserPhotoFilePathUtilsService userPhotoFilePathUtilsService;

	@Autowired
	private BreadcrumbsPhotoService breadcrumbsPhotoService;

	@Autowired
	private ImageFileUtilsService imageFileUtilsService;

	@Autowired
	private RandomUtilsService randomUtilsService;

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private DataRequirementService dataRequirementService;

	private final LogHelper log = new LogHelper( PhotoEditDataController.class );

	@InitBinder
	protected void initBinder( final WebDataBinder binder ) {
		binder.setValidator( photoEditDataValidator );
	}

	@ModelAttribute( MODEL_NAME )
	public PhotoEditDataModel prepareModel() {
		final PhotoEditDataModel model = new PhotoEditDataModel();

		model.setDataRequirementService( dataRequirementService );

		return model;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/new/" )
	public String newPhoto( final @ModelAttribute( MODEL_NAME ) PhotoEditDataModel model ) {

		securityService.assertCurrentUserIsLogged( ONLY_LOGGED_USER_CAN_UPLOAD_A_PHOTO_MESSAGE );

		model.clear();

		model.setNew( true );
		model.setPhotoUploadTime( dateUtilsService.getCurrentTime() );

		model.setCurrentStep( PhotoEditWizardStep.EDIT_PHOTO_DATA );
		model.setNextStep( PhotoEditWizardStep.PHOTO_FILE_UPLOAD );

		initGenreLists( model );
		final User currentUser = EnvironmentContext.getCurrentUser();

		model.setPhotoAuthor( currentUser );

		final int randomInt = randomUtilsService.getRandomInt( 100, 999 );
		model.setName( String.format( "Photo name - %s", randomInt ) );
		model.setKeywords( String.format( "Keywords - %s", randomInt ) );
		model.setDescription( String.format( "Description - %s", randomInt ) );

		final Set<EmailNotificationType> emailNotificationTypes = currentUser.getEmailNotificationTypes();
		model.setNotificationEmailAboutNewPhotoComment( emailNotificationTypes.contains( EmailNotificationType.COMMENT_TO_USER_PHOTO ) ? YesNo.YES.getId() : YesNo.NO.getId() );

		model.setCommentsAllowance( userService.getUserPhotoCommentAllowance( currentUser ) ); // From user defaults
		model.setVotingAllowance( userService.getUserPhotoVotingAllowance( currentUser ) );    // From user defaults
		setAccessibleAllowances( model );

 		setPhotoUploadAllowance( model );

		initUserTeamMembers( model );

		initUserPhotoAlbums( model );

		setAnonymousOptions( model, false );

		model.setPageTitleData( breadcrumbsPhotoService.getUploadPhotoBreadcrumbs( currentUser, model.getCurrentStep() ) );

		return DATA_VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{photoId}/edit/" )
	public String editPhoto( final @PathVariable( "photoId" ) String _photoId, final @ModelAttribute( MODEL_NAME ) PhotoEditDataModel model ) {

		model.clear();

		model.setNew( false );

		assertPhotoExistsAndCurrentUserCanEditIt( _photoId );

		final int photoId = NumberUtils.convertToInt( _photoId );

		final Photo photo = photoService.load( photoId );

		model.setPhotoUploadTime( photo.getUploadTime() );

		model.setCurrentStep( PhotoEditWizardStep.EDIT_PHOTO_DATA );
		model.setNextStep( PhotoEditWizardStep.PHOTO_SAVING );

		initModelFromPhoto( model, photo );

		initGenreLists( model );

		initUserTeamMembers( model );

		initUserPhotoAlbums( model );

		final Genre genre = genreService.load( photo.getGenreId() );

		model.setPageTitleData( breadcrumbsPhotoService.getPhotoEditDataBreadcrumbs( photo ) );

		return DATA_VIEW;
	}

	@RequestMapping( method = RequestMethod.POST, value = "/upload/" )
	public String uploadPhotoFile( @Valid final @ModelAttribute( MODEL_NAME ) PhotoEditDataModel model, final BindingResult result ) {

		assertAccess( model.isNew(), model.getPhotoId() );

		model.setBindingResult( result );

		if ( result.hasErrors() ) {
			return DATA_VIEW;
		}

		model.setCurrentStep( PhotoEditWizardStep.PHOTO_FILE_UPLOAD );
		model.setNextStep( PhotoEditWizardStep.PHOTO_SAVING );
		model.setGenre( genreService.load( model.getGenreId() ) );

		setPhotoUploadAllowance( model );

		final List<UserTeamMember> photoTeamMembers = newArrayList();
		final List<String> photoTeamMemberIds = model.getPhotoTeamMemberIds();
		if ( photoTeamMemberIds != null ) {
			for ( final String memberId : photoTeamMemberIds ) {
				final int photoTeamMemberId = NumberUtils.convertToInt( memberId );
				photoTeamMembers.add( userTeamService.load( photoTeamMemberId ) );
			}
		}
		model.setPhotoTeamMembers( photoTeamMembers );


		final List<UserPhotoAlbum> photoAlbums = newArrayList();
		final List<String> photoAlbumIds = model.getPhotoAlbumIds();
		if ( photoAlbumIds != null ) {
			for ( final String albumId : photoAlbumIds ) {
				final int photoAlbumId = NumberUtils.convertToInt( albumId );
				photoAlbums.add( userPhotoAlbumService.load( photoAlbumId ) );
			}
		}
		model.setPhotoAlbums( photoAlbums );

		model.setPageTitleData( breadcrumbsPhotoService.getUploadPhotoBreadcrumbs( model.getPhotoAuthor(), model.getCurrentStep() ) );

		return FILE_UPLOAD_VIEW;
	}

	@RequestMapping( method = RequestMethod.POST, value = "/save/" )
	public String savePhoto( @Valid final @ModelAttribute( MODEL_NAME ) PhotoEditDataModel model, final BindingResult result ) {

		assertAccess( model.isNew(), model.getPhotoId() );

		model.setCurrentStep( PhotoEditWizardStep.PHOTO_SAVING );

		model.setBindingResult( result );
		initGenreLists( model );

		if ( model.getGenresHaveNudeContent().contains( model.getGenreId() ) ) {
			model.setContainsNudeContent( true );
		}

		if ( result.hasErrors() ) {
			return DATA_VIEW;
		}

		final Photo photo = new Photo();

		initPhotoFromModel( photo, model );

		photo.setUserGenreRank( userRankService.getUserRankInGenre( photo.getUserId(), photo.getGenreId() ) );

		final PhotoTeam photoTeam = getPhotoTeam( photo, model );
		final List<UserPhotoAlbum> photoAlbums = getPhotoAlbums( model );

		final Language language = EnvironmentContext.getLanguage();
		try {
			photoService.savePhotoWithTeamAndAlbums( photo, photoTeam, photoAlbums );
		} catch ( final SaveToDBException e ) {
			photoService.delete( photo.getId() );
			log.error( String.format( "Can not save photo data: %s", photo ), e );
			result.reject( translatorService.translate( "Saving data error", language ), translatorService.translate( "Error saving data to DB", language ) );
			return FILE_UPLOAD_VIEW;
		}

		if ( model.isNew() ) {
			try {
				final File savedFile = saveUploadedFile( photo, model.getFile() );
				photo.setFile( savedFile );

				if ( ! photoService.updatePhotoFileData( photo.getId(), savedFile ) ) {
					photoService.delete( photo.getId() );
					log.error( String.format( "Can not update photo file data: %s", photo ), null );
					result.reject( translatorService.translate( "Saving data error", language ), translatorService.translate( "Can not update photo file data", language ) );
					return FILE_UPLOAD_VIEW;
				}

				previewGenerationService.generatePreview( photo.getId() );

			} catch ( final IOException e ){
				photoService.delete( photo.getId() );
				log.error( "Saving data error" );
				result.reject( translatorService.translate( "Saving data error", language ), translatorService.translate( "Can not copy photo #$1 file", language, String.valueOf( photo.getId() ) ) );
				return FILE_UPLOAD_VIEW;
			} catch ( final InterruptedException e ) {
				photoService.delete( photo.getId() );
				log.error( "Photo preview generation failed" );
				result.reject( translatorService.translate( "Photo preview generation", language ), translatorService.translate( "Photo #$1 preview generation error", language, String.valueOf( photo.getId() ) ) );
				return FILE_UPLOAD_VIEW;
			}
		}

		return String.format( "redirect:/%s/%s/", systemVarsService.getApplicationPrefix(), UrlUtilsServiceImpl.PHOTOS_URL );
	}

	@RequestMapping( method = RequestMethod.GET, value = "{photoId}/delete/" )
	public String deletePhoto( final @PathVariable( "photoId" ) String _photoId, final @ModelAttribute( MODEL_NAME ) PhotoEditDataModel model, final HttpServletRequest request ) {

		securityService.assertPhotoExists( _photoId );

		final int photoId = NumberUtils.convertToInt( _photoId );

		final Photo photo = photoService.load( photoId );

		final User currentUser = EnvironmentContext.getCurrentUser();
		securityService.assertUserCanDeletePhoto( currentUser, photo );

		photoService.delete( photoId );

		return String.format( "redirect:%s", request.getHeader( "referer" ) );
	}

	private void setAccessibleAllowances( final PhotoEditDataModel model ) {
		model.setAccessibleCommentAllowances( configurationService.getAccessiblePhotoCommentAllowance() );
		model.setAccessibleVotingAllowances( configurationService.getAccessiblePhotoVotingAllowance() );
	}

	private PhotoTeam getPhotoTeam( final Photo photo, final PhotoEditDataModel model ) {

		final List<PhotoTeamMember> userTeamMembers = newArrayList();
		final List<String> photoTeamIds = model.getPhotoTeamMemberIds();

		if ( photoTeamIds == null ) {
			return new PhotoTeam( photo, userTeamMembers );
		}

		for ( final String photoTeamId : photoTeamIds ) {
			final int userTeamMemberId = NumberUtils.convertToInt( photoTeamId );
			final UserTeamMember userTeamMember = userTeamService.load( userTeamMemberId );

			if ( userTeamMember == null ) {
				continue;
			}

			final PhotoTeamMember photoTeamMember = new PhotoTeamMember();
			photoTeamMember.setUserTeamMember( userTeamMember );
			photoTeamMember.setDescription( "" ); // TODO:

			userTeamMembers.add( photoTeamMember );
		}

		return new PhotoTeam( photo, userTeamMembers );
	}

	private List<UserPhotoAlbum> getPhotoAlbums( final PhotoEditDataModel model ) {
		final List<UserPhotoAlbum> photoAlbums = newArrayList();
		final List<String> photoAlbumIds = model.getPhotoAlbumIds();

		if ( photoAlbumIds == null ) {
			return photoAlbums;
		}

		for ( final String photoAlbumId : photoAlbumIds ) {
			final int albumId = NumberUtils.convertToInt( photoAlbumId );
			final UserPhotoAlbum photoAlbum = userPhotoAlbumService.load( albumId );
			if ( photoAlbum != null ) {
				photoAlbums.add( photoAlbum );
			}
		}

		return photoAlbums;
	}

	private void initUserTeamMembers( final PhotoEditDataModel model ) {
		model.setUserTeamMembers( userTeamService.loadAllForEntry( EnvironmentContext.getCurrentUser().getId() ) );
	}

	private void initUserPhotoAlbums( final PhotoEditDataModel model ) {
			model.setUserPhotoAlbums( userPhotoAlbumService.loadAllForEntry( EnvironmentContext.getCurrentUser().getId() ) );
	}

	private void setPhotoUploadAllowance( final PhotoEditDataModel model ) {
		final AbstractPhotoUploadAllowance uploadAllowance = UploadDescriptionFactory.getInstance( model.getPhotoAuthor(), EnvironmentContext.getCurrentUser() );

		uploadAllowance.setConfigurationService( configurationService );
		uploadAllowance.setPhotoUploadService( photoUploadService );
		uploadAllowance.setUserRankService( userRankService );
		uploadAllowance.setDateUtilsService( dateUtilsService );
		uploadAllowance.setImageFileUtilsService( imageFileUtilsService );
		uploadAllowance.setTranslatorService( translatorService );

		uploadAllowance.setUploadThisWeekPhotos( photoUploadService.getUploadedThisWeekPhotos( model.getPhotoAuthor().getId() ) );
		uploadAllowance.setGenre( model.getGenre() );

		model.setUploadAllowance( uploadAllowance );
	}

	private void initModelFromPhoto( final PhotoEditDataModel model, final Photo photo ) {
		model.setPhotoAuthor( EnvironmentContext.getCurrentUser() );
		model.setPhotoId( photo.getId() );
		model.setName(  photo.getName() );
		model.setGenreId( photo.getGenreId() );
		model.setKeywords( photo.getKeywords() );
		model.setDescription( photo.getDescription() );
		model.setContainsNudeContent( photo.isContainsNudeContent() );
		model.setBgColor( photo.getBgColor() );

		model.setNotificationEmailAboutNewPhotoComment( photo.isNotificationEmailAboutNewPhotoComment() ? YesNo.YES.getId() : YesNo.NO.getId() );

		model.setCommentsAllowance( photoService.getPhotoCommentAllowance( photo ) );
		model.setVotingAllowance( photoService.getPhotoVotingAllowance( photo ) );
		setAccessibleAllowances( model );

		model.setUserTeamMembers( userTeamService.loadAllForEntry( photo.getUserId() ) );

		final PhotoTeam photoTeam = userTeamService.getPhotoTeam( photo.getId() );
		final List<String> photoTeamMemberIds = newArrayList();
		final List<UserTeamMember> userTeamMembers = newArrayList();

		for ( final PhotoTeamMember photoTeamMember : photoTeam.getPhotoTeamMembers() ) {
			photoTeamMemberIds.add( String.valueOf( photoTeamMember.getUserTeamMember().getId() ) );
			userTeamMembers.add( photoTeamMember.getUserTeamMember() );
		}

		model.setPhotoTeamMemberIds( photoTeamMemberIds );
		model.setPhotoTeamMembers( userTeamMembers );

		final List<UserPhotoAlbum> userPhotoAlbums = userPhotoAlbumService.loadPhotoAlbums( photo.getId() );
		final List<String> photoAlbumsIds = newArrayList();
		final List<UserPhotoAlbum> photoAlbums = newArrayList();

		for ( final UserPhotoAlbum photoAlbum : userPhotoAlbums ) {
			photoAlbumsIds.add( String.valueOf( photoAlbum.getId() ) );
			photoAlbums.add( photoAlbum );
		}

		model.setPhotoAlbumIds( photoAlbumsIds );
		model.setPhotoAlbums( photoAlbums );

		setAnonymousOptions( model, photo.isAnonymousPosting() );
	}

	private void setAnonymousOptions( final PhotoEditDataModel model, final boolean customAnonymousPosting ) {
		model.setAnonymousPosting( customAnonymousPosting );
		model.setAnonymousDay( anonymousDaysService.isDayAnonymous( model.getPhotoUploadTime() ) );
	}

	private void initPhotoFromModel( final Photo photo, final PhotoEditDataModel model ) {
		photo.setId( model.getPhotoId() );
		photo.setName( model.getName() );
		photo.setUserId( model.getPhotoAuthor().getId() );
		photo.setGenreId( model.getGenreId() );
		photo.setKeywords( model.getKeywords() );
		photo.setDescription( model.getDescription() );
		photo.setUploadTime( dateUtilsService.getCurrentTime() );
		photo.setContainsNudeContent( model.isContainsNudeContent() );
		photo.setBgColor( model.getBgColor() );
		photo.setCommentsAllowance( model.getCommentsAllowance() );
		photo.setNotificationEmailAboutNewPhotoComment( model.getNotificationEmailAboutNewPhotoComment() == YesNo.YES.getId() );
		photo.setVotingAllowance( model.getVotingAllowance() );
		photo.setAnonymousPosting( model.isAnonymousPosting() );
	}

	private void initGenreLists( final PhotoEditDataModel model ) {
		final List<Genre> genres = genreService.loadAll();
		model.setGenres( genres );

		final List<Integer> genresCanHaveNudeContent = newArrayList();
		final List<Integer> genresHaveNudeContent = newArrayList();
		for ( final Genre genre : genres ) {
			if ( genre.isCanContainNudeContent() ) {
				genresCanHaveNudeContent.add( genre.getId() );
			}
			if ( genre.isContainsNudeContent() ) {
				genresHaveNudeContent.add( genre.getId() );
			}
		}
		model.setGenresCanHaveNudeContent( genresCanHaveNudeContent );
		model.setGenresHaveNudeContent( genresHaveNudeContent );
	}

	private File saveUploadedFile( final Photo photo, final MultipartFile multipartFile ) throws IOException {
		final User currentUser = EnvironmentContext.getCurrentUser();

		if ( ! userPhotoFilePathUtilsService.isUserPhotoDirExist( currentUser.getId() ) ) {
			userPhotoFilePathUtilsService.createUserPhotoDirIfNeed( currentUser.getId() );
		}

		final File destFile = userPhotoFilePathUtilsService.getUserPhotoFile( currentUser, photo );

		multipartFile.transferTo( destFile );

		return destFile;
	}

	private void assertAccess( final boolean isNew, final int photoId ) {

		securityService.assertCurrentUserIsLogged( ONLY_LOGGED_USER_CAN_UPLOAD_A_PHOTO_MESSAGE );

		if ( !isNew ) {
			assertPhotoExistsAndCurrentUserCanEditIt( String.valueOf( photoId ) );
		}
	}

	private void assertPhotoExistsAndCurrentUserCanEditIt( final String _photoId ) {

		securityService.assertPhotoExists( _photoId );

		final int photoId = NumberUtils.convertToInt( _photoId );
		final Photo photo = photoService.load( photoId );
		securityService.assertUserCanEditPhoto( EnvironmentContext.getCurrentUser(), photo );
	}
}
