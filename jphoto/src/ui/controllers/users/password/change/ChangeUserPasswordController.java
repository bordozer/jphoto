package ui.controllers.users.password.change;

import core.general.user.User;
import core.services.security.SecurityService;
import core.services.user.UserService;
import core.services.user.UsersSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ui.context.EnvironmentContext;
import ui.services.breadcrumbs.BreadcrumbsUserService;
import ui.services.validation.DataRequirementService;
import utils.NumberUtils;

import javax.validation.Valid;

@Controller
@RequestMapping( "members/{userId}/password" )
public class ChangeUserPasswordController {

	private static final String MODEL_NAME = "changeUserPasswordModel";
	private static final String VIEW = "users/password/change/ChangeUserPassword";
	private static final String VIEW_DONE = "users/password/change/ChangeUserPasswordDone";

	@Autowired
	private BreadcrumbsUserService breadcrumbsUserService;

	@Autowired
	private UserService userService;

	@Autowired
	private UsersSecurityService usersSecurityService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private ChangeUserPasswordValidator changeUserPasswordValidator;

	@Autowired
	private DataRequirementService dataRequirementService;

	@InitBinder
	protected void initBinder( final WebDataBinder binder ) {
		binder.setValidator( changeUserPasswordValidator );
	}

	@ModelAttribute( MODEL_NAME )
	public ChangeUserPasswordModel prepareModel( final @PathVariable( "userId" ) String _userId ) {

		final int userId = NumberUtils.convertToInt( _userId );

		final User user = userService.load( userId );

		securityService.assertUserCanEditUserData( EnvironmentContext.getCurrentUser(), user );

		final ChangeUserPasswordModel model = new ChangeUserPasswordModel( user );

		model.setPageTitleData( breadcrumbsUserService.getChangeUserPasswordBreadcrumbs( user ) );
		model.setDataRequirementService( dataRequirementService );

		return model;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String showForm( final @ModelAttribute( MODEL_NAME ) ChangeUserPasswordModel model ) {
		model.clear();

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.POST, value = "/" )
	public String changePassword( final @Valid @ModelAttribute( MODEL_NAME ) ChangeUserPasswordModel model, final BindingResult result ) {

		model.setBindingResult( result );

		if ( result.hasErrors() ) {
			return VIEW;
		}

		usersSecurityService.changeUserPassword( model.getUser(), model.getPassword() );

		return VIEW_DONE;
	}
}
