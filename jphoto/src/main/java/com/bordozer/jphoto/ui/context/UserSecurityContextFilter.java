package com.bordozer.jphoto.ui.context;

import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.security.RestrictionService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.ui.services.security.UsersSecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Order(2)
@Component
public class UserSecurityContextFilter extends OncePerRequestFilter {

    @Autowired
    private UsersSecurityService usersSecurityService;

    @Autowired
    private RestrictionService restrictionService;

    @Autowired
    private DateUtilsService dateUtilsService;

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {

        final User currentUser = EnvironmentContext.getCurrentUser();

        restrictionService.assertUserLoginIsNotRestricted(currentUser, dateUtilsService.getCurrentTime());

        usersSecurityService.assertLoggedUserRequestSecurityPassed(currentUser, request);

        filterChain.doFilter(request, response);
    }
}
