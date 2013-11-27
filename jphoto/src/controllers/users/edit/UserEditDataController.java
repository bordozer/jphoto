package controllers.users.edit;

import core.enums.PhotoActionAllowance;
import core.enums.UserGender;
import core.context.EnvironmentContext;
import core.general.user.User;
import core.general.user.UserMembershipType;
import core.general.user.UserStatus;
import core.general.configuration.ConfigurationKey;
import core.general.user.EmailNotificationType;
import core.services.system.ConfigurationService;
import core.services.security.SecurityService;
import core.services.user.UserService;
import core.services.utils.DateUtilsService;
import core.services.utils.SystemVarsService;
import core.services.utils.UrlUtilsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.*;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import utils.TranslatorUtils;
import utils.*;
import core.services.user.FakeUserService;
import core.services.pageTitle.PageTitleUserUtilsService;

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
	private PageTitleUserUtilsService pageTitleUserUtilsService;

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

		return model;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/new/" )
	public String newUser( final @ModelAttribute( "userEditDataModel" ) UserEditDataModel model ) {
		model.clear();

		if( UserUtils.isCurrentUserLoggedUser() ) {
			return String.format( "redirect:/%s/%s/", systemVarsService.getApplicationPrefix(), UrlUtilsServiceImpl.USERS_URL );
		}

		model.setNew( true );

		final User user = fakeUserService.getRandomUser(); // TODO: for debug only!

		initModelFromUser( model, user );

		model.setBeingChangedUser( user );

		model.setPassword( DEFAULT_USER_PASSWORD );
		model.setConfirmPassword( DEFAULT_USER_PASSWORD );

		model.setPageTitleData( pageTitleUserUtilsService.getUserNewData() );

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

		model.setPageTitleData( pageTitleUserUtilsService.getUserEditData( beingChangedUser ) );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.POST, value = "/{userId}/save/" )
	public String saveUser( final @PathVariable( "userId" ) int userId, final @Valid @ModelAttribute( "userEditDataModel" ) UserEditDataModel model, final BindingResult result ) {

		final boolean isNew = model.isNew();
		if ( !isNew ) {
			securityService.assertUserCanEditUserData( EnvironmentContext.getCurrentUser(), model.getBeingChangedUser() );
		} else {
			if( UserUtils.isCurrentUserLoggedUser() ) {
				return String.format( "redirect:/%s/%s/", systemVarsService.getApplicationPrefix(), UrlUtilsServiceImpl.USERS_URL );
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
			result.reject( TranslatorUtils.translate( "Registration error" ), TranslatorUtils.translate( "Error saving data to DB" ) );
			return VIEW;
		}

		return String.format( "redirect:/%s/%s/", systemVarsService.getApplicationPrefix(), UrlUtilsServiceImpl.USERS_URL );
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

		final Set<String> notificationOptionIds = newHashSet();
		for ( final EmailNotificationType emailNotificationType : user.getEmailNotificationTypes() ) {
			notificationOptionIds.add( String.valueOf( emailNotificationType.getId() ) );
		}
		model.setEmailNotificationOptionIds( notificationOptionIds );

		setCommentAllowance( model, user );

		setVotingAllowance( model, user );
	}

	private void setCommentAllowance( final UserEditDataModel model, final User user ) {
		model.setAccessibleCommentAllowances( configurationService.getAccessiblePhotoCommentAllowance() );
		model.setDefaultPhotoCommentsAllowanceId( userService.getUserPhotoCommentAllowance( user ).getId() );
	}

	private void setVotingAllowance( final UserEditDataModel model, final User user ) {
		model.setAccessibleVotingAllowances( configurationService.getAccessiblePhotoVotingAllowance() );
		model.setDefaultPhotoVotingAllowanceId( userService.getUserPhotoVotingAllowance( user ).getId() );
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
