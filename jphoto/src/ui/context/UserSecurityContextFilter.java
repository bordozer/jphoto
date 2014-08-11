package ui.context;

import core.general.user.User;
import core.services.security.RestrictionService;
import core.services.utils.DateUtilsService;
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

		usersSecurityService.assertLoggedUserRequestSecurityPassed( currentUser, request );

		restrictionService.isUserLoginRestricted( currentUser.getId(), dateUtilsService.getCurrentTime() );

		filterChain.doFilter( request, response );
	}
}
