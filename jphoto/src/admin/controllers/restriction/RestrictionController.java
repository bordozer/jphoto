package admin.controllers.restriction;

import core.general.user.User;
import core.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping( "restriction/members/{userId}" )
@Controller
public class RestrictionController {

	@Autowired
	private UserService userService;

	private static final String MODEL_NAME = "restrictionModel";

	private static final String VIEW = "admin/restriction/UserRestriction";

	@ModelAttribute( MODEL_NAME )
	public RestrictionModel prepareModel( final @PathVariable( "userId" ) int userId ) {
		final User user = userService.load( userId );

		final RestrictionModel model = new RestrictionModel( userId );

		model.setUserName( user.getNameEscaped() );

		return model;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String showUserRestrictions( final @ModelAttribute( MODEL_NAME ) RestrictionModel model ) {
		return VIEW;
	}
}
