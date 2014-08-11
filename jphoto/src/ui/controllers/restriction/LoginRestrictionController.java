package ui.controllers.restriction;

import core.exceptions.AccessDeniedException;
import core.general.restriction.EntryRestriction;
import core.services.security.RestrictionService;
import core.services.system.Services;
import core.services.user.UserService;
import core.services.utils.EntityLinkUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ui.context.EnvironmentContext;
import ui.services.breadcrumbs.BreadcrumbsUserService;
import utils.NumberUtils;
import utils.UserUtils;

@Controller
@RequestMapping( "/restriction/{restrictionEntryId}/" )
public class LoginRestrictionController {

	private static final String VIEW = "users/restriction/LoginRestriction";

	private static final String MODEL_NAME = "loginRestrictionModel";

	@Autowired
	private RestrictionService restrictionService;

	@Autowired
	private BreadcrumbsUserService breadcrumbsUserService;

	@Autowired
	private UserService userService;

	@Autowired
	private EntityLinkUtilsService entityLinkUtilsService;

	@Autowired
	private Services services;

	@ModelAttribute( MODEL_NAME )
	private LoginRestrictionModel prepareModel( final @PathVariable( "restrictionEntryId" ) String _restrictionEntryId ) {

		final int restrictionEntryId = NumberUtils.convertToInt( _restrictionEntryId );
		if ( restrictionEntryId == 0 ) {
			throw new AccessDeniedException( "Wrong parameter" ); // TODO:
		}

		return new LoginRestrictionModel( restrictionEntryId );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String showAvatar( final @ModelAttribute( MODEL_NAME ) LoginRestrictionModel model ) {

		if ( ! UserUtils.isCurrentUserLoggedUser() ) {
			return String.format( "redirect:/photos/" );
		}

		final EntryRestriction restriction = restrictionService.load( model.getRestrictionEntryId() );

		/*final TranslatableMessage translatableMessage = new TranslatableMessage( "You are logged out because $2 on $3 restricted you in this rights. The restriction is active from $4 till $5.", services )
			.translatableString( restriction.getRestrictionType().getName() )
			.addUserCardLinkParameter( restriction.getCreator() )
			.dateTimeFormatted( restriction.getCreatingTime() )
			.dateTimeFormatted( restriction.getRestrictionTimeFrom() )
			.dateTimeFormatted( restriction.getRestrictionTimeTo() )
			;
		model.setLoginRestrictionMessage( translatableMessage.build( EnvironmentContext.getLanguage() ) );*/
		model.setRestriction( restriction );

		model.setPageTitleData( breadcrumbsUserService.getUserLoginRestrictionBreadCrumbs( EnvironmentContext.getCurrentUser() ) );

		return VIEW;
	}
}
