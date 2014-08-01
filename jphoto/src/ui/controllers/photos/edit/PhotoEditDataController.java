package ui.controllers.photos.edit;

import admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import core.enums.PhotoActionAllowance;
import core.enums.YesNo;
import core.exceptions.SaveToDBException;
import core.general.photo.Photo;
import core.general.photoTeam.PhotoTeam;
import core.general.photoTeam.PhotoTeamMember;
import core.general.user.EmailNotificationType;
import core.general.user.User;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.general.user.userTeam.UserTeamMember;
import core.services.entry.AnonymousDaysService;
import core.services.entry.GenreService;
import core.services.photo.PhotoService;
import core.services.photo.PhotoUploadService;
import core.services.security.SecurityService;
import core.services.system.ConfigurationService;
import core.services.system.Services;
import core.services.translator.TranslatorService;
import core.services.user.UserPhotoAlbumService;
import core.services.user.UserService;
import core.services.user.UserTeamService;
import core.services.utils.*;
import rest.photo.upload.category.allowance.AbstractPhotoUploadAllowance;
import rest.photo.upload.category.allowance.UploadDescriptionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ui.context.EnvironmentContext;
import ui.services.breadcrumbs.BreadcrumbsPhotoService;
import ui.translatable.GenericTranslatableList;
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

	private static final String VIEW_UPLOAD_FILE = "photos/edit/PhotoUpload";
	private static final String VIEW_EDIT_DATA = "photos/edit/PhotoEditData";

	private static final String ONLY_LOGGED_USER_CAN_UPLOAD_A_PHOTO_MESSAGE = "Only logged user can upload a photo";

	@Autowired
	private SecurityService securityService;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private UserService userService;

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

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private UserTeamService userTeamService;

	@Autowired
	private UserPhotoAlbumService userPhotoAlbumService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private AnonymousDaysService anonymousDaysService;

	@Autowired
	private PhotoUploadService photoUploadService;

	@Autowired
	private ImageFileUtilsService imageFileUtilsService;

	@Autowired
	private Services services;

	@Autowired
	PhotoEditFileValidator photoEditFileValidator;

	@Autowired
	PhotoEditDataValidator photoEditDataValidator;

	@InitBinder
	protected void initBinder( final WebDataBinder binder ) {
		binder.setValidator( photoEditDataValidator );
	}

	@ModelAttribute( MODEL_NAME )
	public PhotoEditDataModel prepareModel() {
		return new PhotoEditDataModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "/new/" )
	public String photoFileUpload( final @ModelAttribute( MODEL_NAME ) PhotoEditDataModel model ) {

		securityService.assertCurrentUserIsLogged( ONLY_LOGGED_USER_CAN_UPLOAD_A_PHOTO_MESSAGE );

		model.clear();

		final User photoAuthor = EnvironmentContext.getCurrentUser();

		model.setPhotoAuthor( photoAuthor );

		model.setNew( true );
		model.setPhoto( new Photo() );
		model.setPhotoUploadAllowance( getPhotoUploadAllowance( photoAuthor ) );

		model.setPageTitleData( breadcrumbsPhotoService.getUploadPhotoBreadcrumbs( EnvironmentContext.getCurrentUser() ) );

		return VIEW_UPLOAD_FILE;
	}

	@RequestMapping( method = RequestMethod.POST, value = "/new/" )
	public String photoUploaded( final @ModelAttribute( MODEL_NAME ) PhotoEditDataModel model, final BindingResult result/*, final PhotoEditFileValidator photoEditFileValidator*/ ) throws IOException {

		// TODO: uncomment validation!!! -->
		photoEditFileValidator.validate( model, result );
		model.setBindingResult( result );

		if ( result.hasErrors() ) {
			return VIEW_UPLOAD_FILE;
		}
		// TODO: uncomment validation!!! <--

		final MultipartFile photoFile = model.getPhotoFile();
		final File tempPhotoFile = tempFileUtilsService.getTempFileWithOriginalExtension( model.getPhotoAuthor(), photoFile.getOriginalFilename() );
		photoFile.transferTo( tempPhotoFile );
		model.setTempPhotoFile( tempPhotoFile );

		model.getPhoto().setFileSize( tempPhotoFile.length() );
		model.setPhotoDimension( imageFileUtilsService.getImageDimension( tempPhotoFile ) );

		final User photoAuthor = model.getPhotoAuthor();

		setAllowancesTranslatableLists( model );
		model.setCommentsAllowance( userService.getUserPhotoCommentAllowance( photoAuthor ) ); // From user defaults
		model.setVotingAllowance( userService.getUserPhotoVotingAllowance( photoAuthor ) );    // From user defaults

		final Set<EmailNotificationType> emailNotificationTypes = photoAuthor.getEmailNotificationTypes();
		model.setSendNotificationEmailAboutNewPhotoComment( emailNotificationTypes.contains( EmailNotificationType.COMMENT_TO_USER_PHOTO ) ? YesNo.YES.getId() : YesNo.NO.getId() );

		model.setUserTeamMembers( getUserTeamMembers() );
		model.setUserPhotoAlbums( getUserPhotoAlbums() );

		model.setUploadDateIsAnonymousDay( anonymousDaysService.isDayAnonymous( dateUtilsService.getCurrentTime() ) );
		model.setAnonymousPosting( false );

		model.setPageTitleData( breadcrumbsPhotoService.getUploadPhotoBreadcrumbs( photoAuthor ) );

		return VIEW_EDIT_DATA;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{photoId}/edit/" )
	public String editPhoto( final @PathVariable( "photoId" ) String _photoId, final @ModelAttribute( MODEL_NAME ) PhotoEditDataModel model ) {

		assertPhotoExistsAndCurrentUserCanEditIt( _photoId );

		final int photoId = NumberUtils.convertToInt( _photoId );

		final Photo photo = photoService.load( photoId );

		model.clear();
		model.setNew( false );

		initModelFromPhoto( model, photo );

		model.setPageTitleData( breadcrumbsPhotoService.getPhotoEditDataBreadcrumbs( photo ) );

		return VIEW_EDIT_DATA;
	}

	@RequestMapping( method = RequestMethod.POST, value = "/save/" )
	public String editPhoto( @Valid final @ModelAttribute( MODEL_NAME ) PhotoEditDataModel model, final BindingResult result, final HttpServletRequest request ) throws SaveToDBException, IOException {

		model.setBindingResult( result );

		if ( result.hasErrors() ) {
			return VIEW_EDIT_DATA;
		}

		final Photo photo = new Photo();
		initPhotoFromModel( photo, model );

		final PhotoTeam photoTeam = getPhotoTeam( photo, model.getUserTeamMemberIds() );
		final List<UserPhotoAlbum> photoAlbums = getPhotoAlbums( model.getPhotoAlbumIds() );

		if ( model.isNew() ) {
			photoService.uploadNewPhoto( photo, model.getTempPhotoFile(), photoTeam, photoAlbums );
		} else {
			photoService.updatePhoto( photo, photoTeam, photoAlbums );
		}

		if ( model.isNew() ) {
			return String.format( "redirect:%s", urlUtilsService.getPhotoCardLink( photo.getId() ) );
		}

		return String.format( "redirect:%s", urlUtilsService.getAllPhotosLink() ); // TODO: figure out where from the request come
	}

	@RequestMapping( method = RequestMethod.GET, value = "{photoId}/delete/" )
	public String deletePhoto( final @PathVariable( "photoId" ) String _photoId, final @ModelAttribute( MODEL_NAME ) PhotoEditDataModel model, final HttpServletRequest request ) {

		securityService.assertPhotoExists( _photoId );

		final int photoId = NumberUtils.convertToInt( _photoId );

		final Photo photo = photoService.load( photoId );

		final User currentUser = EnvironmentContext.getCurrentUser();
		securityService.assertUserCanDeletePhoto( currentUser, photo );

		photoService.delete( photoId );

		final boolean isDeletionFromPhotoCard = request.getHeader( "Referer" ).equals( urlUtilsService.getPhotoCardLink( photoId ) );
		if ( isDeletionFromPhotoCard ) {
			return String.format( "redirect:%s", urlUtilsService.getAllPhotosLink() );
		}

		return String.format( "redirect:%s", request.getHeader( "Referer" ) );
	}

	private AbstractPhotoUploadAllowance getPhotoUploadAllowance( final User user ) {

		final AbstractPhotoUploadAllowance uploadAllowance = UploadDescriptionFactory.getInstance( user, EnvironmentContext.getCurrentUser(), EnvironmentContext.getLanguage(), services );

		uploadAllowance.setUploadThisWeekPhotos( photoUploadService.getUploadedThisWeekPhotos( user.getId() ) );
		uploadAllowance.setSkipGenreSelectionInfo( true );

		return uploadAllowance;
	}

	private List<UserPhotoAlbum> getPhotoAlbums( final List<String> photoAlbumIds ) {

		if ( photoAlbumIds == null ) {
			return newArrayList();
		}

		final List<UserPhotoAlbum> result = newArrayList();

		for ( final String photoAlbumId : photoAlbumIds ) {
			result.add( userPhotoAlbumService.load( Integer.parseInt( photoAlbumId ) ) );
		}

		return result;
	}

	private PhotoTeam getPhotoTeam( final Photo photo, final List<String> userTeamMembers ) {

		final List<PhotoTeamMember> photoTeamMembers = newArrayList();

		if ( userTeamMembers == null ) {
			return new PhotoTeam( photo, photoTeamMembers );
		}

		for ( final String userTeamMemberId : userTeamMembers ) {
			final UserTeamMember userTeamMember = userTeamService.load( Integer.parseInt( userTeamMemberId ) );
			final PhotoTeamMember photoTeamMember = new PhotoTeamMember();
			photoTeamMember.setUserTeamMember( userTeamMember );
			photoTeamMembers.add( photoTeamMember );
		}
		return new PhotoTeam( photo, photoTeamMembers );
	}

	private void initModelFromPhoto( final PhotoEditDataModel model, final Photo photo ) {

		model.setPhoto( photo );
		model.setPhotoAuthor( userService.load( photo.getUserId() ) );

		model.setPhotoName( photo.getName() );
		model.setSelectedGenreId( photo.getGenreId() );
		model.setPhotoKeywords( photo.getKeywords() );
		model.setPhotoDescription( photo.getDescription() );
		model.setContainsNudeContent( photo.isContainsNudeContent() );
		model.setBgColor( photo.getBgColor() );


		model.setCommentsAllowance( photoService.getPhotoCommentAllowance( photo ) );
		model.setSendNotificationEmailAboutNewPhotoComment( photo.isNotificationEmailAboutNewPhotoComment() ? YesNo.YES.getId() : YesNo.NO.getId() );

		model.setVotingAllowance( photoService.getPhotoVotingAllowance( photo ) );


		setAllowancesTranslatableLists( model );

		model.setUserTeamMembers( userTeamService.loadAllForEntry( photo.getUserId() ) );

		final PhotoTeam photoTeam = userTeamService.getPhotoTeam( photo.getId() );
		final List<String> photoTeamMemberIds = newArrayList();
		final List<UserTeamMember> userTeamMembers = newArrayList();

		for ( final PhotoTeamMember photoTeamMember : photoTeam.getPhotoTeamMembers() ) {
			photoTeamMemberIds.add( String.valueOf( photoTeamMember.getUserTeamMember().getId() ) );
			userTeamMembers.add( photoTeamMember.getUserTeamMember() );
		}

		model.setUserTeamMemberIds( photoTeamMemberIds );

		final List<UserPhotoAlbum> userPhotoAlbums = userPhotoAlbumService.loadPhotoAlbums( photo.getId() );
		final List<String> photoAlbumsIds = newArrayList();

		for ( final UserPhotoAlbum photoAlbum : userPhotoAlbums ) {
			photoAlbumsIds.add( String.valueOf( photoAlbum.getId() ) );
		}

		model.setPhotoAlbumIds( photoAlbumsIds );
		model.setUserPhotoAlbums( getUserPhotoAlbums() );

		model.setAnonymousPosting( photo.isAnonymousPosting() );
		model.setUploadDateIsAnonymousDay( anonymousDaysService.isDayAnonymous( photo.getUploadTime() ) );
	}

	private void initPhotoFromModel( final Photo photo, final PhotoEditDataModel model ) {
		photo.setId( model.getPhoto().getId() );
		photo.setName( model.getPhotoName() );
		photo.setUserId( model.getPhotoAuthor().getId() );
		photo.setGenreId( model.getSelectedGenreId() );
		photo.setKeywords( model.getPhotoKeywords() );
		photo.setDescription( model.getPhotoDescription() );
		photo.setUploadTime( dateUtilsService.getCurrentTime() );

		photo.setContainsNudeContent( model.isContainsNudeContent() || genreService.load( model.getSelectedGenreId() ).isContainsNudeContent() );

		photo.setBgColor( model.getBgColor() );
		photo.setCommentsAllowance( model.getCommentsAllowance() );
		photo.setNotificationEmailAboutNewPhotoComment( model.getSendNotificationEmailAboutNewPhotoComment() == YesNo.YES.getId() );
		photo.setVotingAllowance( model.getVotingAllowance() );
		photo.setAnonymousPosting( model.isAnonymousPosting() );

		photo.setPhotoImageSourceType( PhotosImportSource.FILE_SYSTEM );
	}

	private void assertPhotoExistsAndCurrentUserCanEditIt( final String _photoId ) {

		securityService.assertPhotoExists( _photoId );

		final int photoId = NumberUtils.convertToInt( _photoId );
		final Photo photo = photoService.load( photoId );

		securityService.assertUserCanEditPhoto( EnvironmentContext.getCurrentUser(), photo );
	}

	private void setAllowancesTranslatableLists( final PhotoEditDataModel model ) {

		final List<PhotoActionAllowance> accessiblePhotoCommentAllowance = configurationService.getAccessiblePhotoCommentAllowance();
		model.setAccessibleCommentAllowancesTranslatableList( new GenericTranslatableList<PhotoActionAllowance>( accessiblePhotoCommentAllowance, EnvironmentContext.getLanguage(), translatorService ) );

		final List<PhotoActionAllowance> accessiblePhotoVotingAllowance = configurationService.getAccessiblePhotoVotingAllowance();
		model.setAccessibleVotingAllowancesTranslatableList( new GenericTranslatableList<PhotoActionAllowance>( accessiblePhotoVotingAllowance, EnvironmentContext.getLanguage(), translatorService ) );
	}

	private List<UserTeamMember> getUserTeamMembers() {
		return userTeamService.loadAllForEntry( EnvironmentContext.getCurrentUser().getId() );
	}

	private List<UserPhotoAlbum> getUserPhotoAlbums() {
		return userPhotoAlbumService.loadAllForEntry( EnvironmentContext.getCurrentUser().getId() );
	}
}
