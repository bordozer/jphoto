package core.context;

import core.services.user.UsersSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserSecurityContextFilter extends OncePerRequestFilter implements Filter {

	@Autowired
	private UsersSecurityService usersSecurityService;

	@Override
	protected void doFilterInternal( final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain ) throws ServletException, IOException {

		usersSecurityService.assertLoggedUserRequestSecurityPassed( EnvironmentContext.getCurrentUser(), request );

		filterChain.doFilter( request, response );
	}
}
