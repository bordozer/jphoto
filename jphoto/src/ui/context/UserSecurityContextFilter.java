package ui.context;

import core.enums.RestrictionType;
import core.general.restriction.EntryRestriction;
import core.general.user.User;
import core.services.security.RestrictionService;
import core.services.utils.DateUtilsService;
import core.services.utils.UrlUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;
import ui.services.security.UsersSecurityService;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserSecurityContextFilter extends OncePerRequestFilter implements Filter {

	@Autowired
	private UsersSecurityService usersSecurityService;

	@Autowired
	private RestrictionService restrictionService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Override
	protected void doFilterInternal( final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain ) throws ServletException, IOException {

		final User currentUser = EnvironmentContext.getCurrentUser();

		restrictionService.assertUserLoginIsNotRestricted( currentUser.getId(), dateUtilsService.getCurrentTime() );

		usersSecurityService.assertLoggedUserRequestSecurityPassed( currentUser, request );

		filterChain.doFilter( request, response );
	}
}
