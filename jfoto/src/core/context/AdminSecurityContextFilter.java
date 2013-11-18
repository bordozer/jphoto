package core.context;

import core.exceptions.AccessDeniedException;
import core.services.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminSecurityContextFilter extends OncePerRequestFilter implements Filter {

	@Autowired
	private SecurityService securityService;

	@Override
	protected void doFilterInternal( final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain ) throws ServletException, IOException {

		if ( ! securityService.isSuperAdminUser( EnvironmentContext.getCurrentUser().getId() ) ) {
			throw new AccessDeniedException( "Admin only area" );
		}

		filterChain.doFilter( request, response );
	}
}
