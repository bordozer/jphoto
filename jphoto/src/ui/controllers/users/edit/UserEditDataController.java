package ui.controllers.users.edit;

import ui.context.EnvironmentContext;
import core.enums.PhotoActionAllowance;
import core.enums.UserGender;
import core.general.configuration.ConfigurationKey;
import core.general.user.EmailNotificationType;
import core.general.user.User;
import core.general.user.UserMembershipType;
import core.general.user.UserStatus;
import core.services.utils.UrlUtilsService;
import ui.services.breadcrumbs.BreadcrumbsUserService;
import core.services.security.SecurityService;
import core.services.system.ConfigurationService;
import core.services.translator.Language;
import core.services.translator.TranslatorService;
import core.services.user.FakeUserService;
import core.services.user.UserService;
import core.services.utils.DateUtilsService;
import core.services.utils.SystemVarsService;
import core.services.utils.UrlUtilsServiceImpl;
import core.services.validation.DataRequirementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import utils.NumberUtils;
import utils.UserUtils;

import javax.validation.Valid;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

@SessionAttributes( { "userEditDataModel" } )
@Controller
@RequestMapping( UrlUtilsServiceImpl.USERS_URL )
public class UserEditDataController {

	public static final String DEFAULT_USER_PASSWORD = "eM2&gCyRuJX";

	private static final String VIEW = "users/edit/UserEditData";

	@Autowired
	private UserService userService;

	@Autowired
	private UserEditDataValidator userEditDataValidator;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private FakeUserService fakeUserService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private SystemVarsService systemVarsService;
	
	@Autowired
	private DateUtilsService dateUtilsService;
	
	@Autowired
	private BreadcrumbsUserService breadcrumbsUserService;

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private DataRequirementService dataRequirementService;

	@Autowired
	private UrlUtilsService urlUtilsService;

	@InitBinder
	protected void initBinder( final WebDataBinder binder ) {
		binder.setValidator( userEditDataValidator );
	}

	@ModelAttribute( "userEditDataModel" )
	public UserEditDataModel prepareModel() {
		final UserEditDataModel model = new UserEditDataModel();

		model.setMinLoginLength( configurationService.getInt( ConfigurationKey.SYSTEM_LOGIN_MIN_LENGTH ) );
		model.setMaxLoginLength( configurationService.getInt( ConfigurationKey.SYSTEM_LOGIN_MAX_LENGTH ) );

		model.setMinUserNameLength( configurationService.getInt( ConfigurationKey.SYSTEM_USER_NAME_MIN_LENGTH ) );
		model.setMaxUserNameLength( configurationService.getInt( ConfigurationKey.SYSTEM_USER_NAME_MAX_LENGTH ) );

		model.setDataRequirementService( dataRequirementService );

		model.setUsedLanguages( systemVarsService.getActiveLanguages() );

		return model;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/new/" )
	public String newUser( final @ModelAttribute( "userEditDataModel" ) UserEditDataModel model ) {
		model.clear();

		if( UserUtils.isCurrentUserLoggedUser() ) {
			return getRedirectToUserListView();
		}

		model.setNew( true );

		final User user = fakeUserService.getRandomUser(); // TODO: for debug only!

		initModelFromUser( model, user );

		model.setBeingChangedUser( user );

		model.setPassword( DEFAULT_USER_PASSWORD );
		model.setConfirmPassword( DEFAULT_USER_PASSWORD );

		model.setPageTitleData( breadcrumbsUserService.getUserRegistrationBreadcrumbs() );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{userId}/edit/" )
	public String editUser( final @PathVariable( "userId" ) String _userId, final @ModelAttribute( "userEditDataModel" ) UserEditDataModel model ) {

		securityService.assertUserExists( _userId );

		model.clear();

		final int userId = NumberUtils.convertToInt( _userId );

		final User beingChangedUser = userService.load( userId );

		securityService.assertUserCanEditUserData( EnvironmentContext.getCurrentUser(), beingChangedUser );

		initModelFromUser( model, beingChangedUser );
		beingChangedUser.setUserStatus( UserStatus.CANDIDATE );
		beingChangedUser.setRegistrationTime( dateUtilsService.getCurrentTime() );

		model.setBeingChangedUser( beingChangedUser );

		model.setPageTitleData( breadcrumbsUserService.getUserEditBreadcrumbs( beingChangedUser ) );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.POST, value = "/{userId}/save/" )
	public String saveUser( final @PathVariable( "userId" ) int userId, final @Valid @ModelAttribute( "userEditDataModel" ) UserEditDataModel model, final BindingResult result ) {

		final boolean isNew = model.isNew();
		if ( !isNew ) {
			securityService.assertUserCanEditUserData( EnvironmentContext.getCurrentUser(), model.getBeingChangedUser() );
		} else {
			if( UserUtils.isCurrentUserLoggedUser() ) {
				return getRedirectToUserListView();
			}
		}

		model.setBindingResult( result );

		if ( result.hasErrors() ) {
			return VIEW;
		}

		final User user = new User();

		initUserFromModel( user, model );

		final boolean isSaved;
		if ( isNew ) {
			isSaved = userService.createUser( user, model.getPassword() );
		} else {
			isSaved = userService.save( user );
		}

		if ( !isSaved ) {
			result.reject( translatorService.translate( "Registration error", EnvironmentContext.getLanguage() ), translatorService.translate( "Error saving data to DB", EnvironmentContext.getLanguage() ) );
			return VIEW;
		}

		updateUserUILanguageIfLoggedUserChangedHisLanguage( user );

		return getRedirectToUserListView();
	}

	private void updateUserUILanguageIfLoggedUserChangedHisLanguage( final User user ) {
		final User currentUser = EnvironmentContext.getCurrentUser();
		if ( UserUtils.isTheUserThatWhoIsCurrentUser( user ) ) {
			currentUser.setLanguage( user.getLanguage() );
			currentUser.setPhotoLines( user.getPhotoLines() );
			currentUser.setPhotosInLine( user.getPhotosInLine() );
		}
	}

	private String getRedirectToUserListView() {
		return String.format( "redirect:%s/%s/", urlUtilsService.getBaseURLWithPrefix(), UrlUtilsServiceImpl.USERS_URL );
	}

	private void setCommentAllowance( final UserEditDataModel model, final User user ) {
		model.setAccessibleCommentAllowances( configurationService.getAccessiblePhotoCommentAllowance() );
		model.setDefaultPhotoCommentsAllowanceId( userService.getUserPhotoCommentAllowance( user ).getId() );
	}

	private void setVotingAllowance( final UserEditDataModel model, final User user ) {
		model.setAccessibleVotingAllowances( configurationService.getAccessiblePhotoVotingAllowance() );
		model.setDefaultPhotoVotingAllowanceId( userService.getUserPhotoVotingAllowance( user ).getId() );
	}

	private void initModelFromUser( final UserEditDataModel model, final User user ) {
		model.setUserId( user.getId() );
		model.setLogin( user.getLogin() );
		model.setName( user.getName() );
		model.setEmail( user.getEmail() );
		model.setDateOfBirth( dateUtilsService.formatDate( user.getDateOfBirth() ) );
		model.setHomeSite( user.getHomeSite() );
		model.setSelfDescription( user.getSelfDescription() );
		model.setMembershipTypeId( user.getMembershipType().getId() );
		model.setUserGenderId( user.getGender().getId() );
		model.setPhotosInLine( user.getPhotosInLine() );
		model.setPhotoLines( user.getPhotoLines() );
		model.setShowNudeContent( user.isShowNudeContent() );
		model.setUserUILanguageId( user.getLanguage().getId() );

		final Set<String> notificationOptionIds = newHashSet();
		for ( final EmailNotificationType emailNotificationType : user.getEmailNotificationTypes() ) {
			notificationOptionIds.add( String.valueOf( emailNotificationType.getId() ) );
		}
		model.setEmailNotificationOptionIds( notificationOptionIds );

		setCommentAllowance( model, user );

		setVotingAllowance( model, user );
	}

	private void initUserFromModel( final User user, final UserEditDataModel model ) {
		user.setId( model.getUserId() );
		user.setLogin( model.getLogin() );
		user.setName( model.getName() );
		user.setEmail( model.getEmail() );
		user.setDateOfBirth( dateUtilsService.parseDate( model.getDateOfBirth() ) );
		user.setHomeSite( model.getHomeSite() );
		user.setSelfDescription( model.getSelfDescription() );
		user.setMembershipType( UserMembershipType.getById( model.getMembershipTypeId() ) );
		user.setGender( UserGender.getById( model.getUserGenderId() ) );
		user.setPhotosInLine( model.getPhotosInLine() );
		user.setPhotoLines( model.getPhotoLines() );
		user.setShowNudeContent( model.isShowNudeContent() );
		user.setLanguage( Language.getById( model.getUserUILanguageId() ) );

		final Set<String> emailNotificationOptionIds = model.getEmailNotificationOptionIds();
		if ( emailNotificationOptionIds != null ) {
			final Set<EmailNotificationType> emailNotificationTypes = newHashSet();
			for ( final String id : emailNotificationOptionIds ) {
				emailNotificationTypes.add( EmailNotificationType.getById( Integer.parseInt( id ) ) );
			}
			user.setEmailNotificationTypes( emailNotificationTypes );
		}

		user.setDefaultPhotoCommentsAllowance( PhotoActionAllowance.getById( model.getDefaultPhotoCommentsAllowanceId() ) );
		user.setDefaultPhotoVotingAllowance( PhotoActionAllowance.getById( model.getDefaultPhotoVotingAllowanceId() ) );

		final User beingChangedUser = model.getBeingChangedUser();
		user.setUserStatus( beingChangedUser.getUserStatus() );
		user.setRegistrationTime( beingChangedUser.getRegistrationTime() );
	}
}
