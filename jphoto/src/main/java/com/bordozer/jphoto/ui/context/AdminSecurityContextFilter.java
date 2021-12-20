package com.bordozer.jphoto.ui.context;

import com.bordozer.jphoto.core.exceptions.AccessDeniedException;
import com.bordozer.jphoto.core.services.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AdminSecurityContextFilter extends OncePerRequestFilter implements Filter {

    @Autowired
    private SecurityService securityService;

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {

        if (!securityService.isSuperAdminUser(EnvironmentContext.getCurrentUser().getId())) {
            throw new AccessDeniedException("Admin only area");
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        final String path = request.getRequestURI();
        return !path.startsWith("/admin");
    }
}
