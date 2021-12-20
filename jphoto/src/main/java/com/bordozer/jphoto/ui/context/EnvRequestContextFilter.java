package com.bordozer.jphoto.ui.context;

import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.user.UserService;
import com.bordozer.jphoto.ui.controllers.users.login.UserLoginController;
import com.bordozer.jphoto.ui.services.security.UsersSecurityService;
import com.bordozer.jphoto.utils.PhotoUtils;
import com.bordozer.jphoto.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@Order(1)
@Component
public class EnvRequestContextFilter extends OncePerRequestFilter {

    private static final String ENVIRONMENT_SESSION_KEY = Environment.class.getName();

    @Autowired
    private UserService userService;
    @Autowired
    private UsersSecurityService usersSecurityService;

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        final HttpSession session = request.getSession();
        Environment environment;

        if (session.isNew()) {
            environment = createSessionEnvironment(session);

            setEnvironmentUserFromCookie(request, environment);
        } else {
            environment = getSessionEnvironment(session);
            if (environment == null) {
                environment = createSessionEnvironment(session); // Again because session could be alive after the tomcat restart
            }
        }

        environment.setDeviceType(PhotoUtils.getDeviceType(request));

        EnvironmentContext.setEnv(environment);

        if (UserUtils.isCurrentUserLoggedUser()) {
            usersSecurityService.saveLastUserActivityTimeNow(EnvironmentContext.getCurrentUserId());
        }

        filterChain.doFilter(request, response);
    }

    private void setEnvironmentUserFromCookie(final HttpServletRequest request, final Environment environment) {
        final Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return;
        }

        for (final Cookie cookie : cookies) {
            if (!cookie.getName().equals(UserLoginController.USER_LOGIN_COOKIE)) {
                continue;
            }

            final String userLogin = cookie.getValue();

            final User user = userService.loadByLogin(userLogin);
            if (user != null) {
                environment.setCurrentUser(user);
                log.debug("Login '{}' (user #{}) has been found in cookie and auto-logged in", user.getLogin(), user.getId());
            }

            break;
        }
    }

    private Environment createSessionEnvironment(final HttpSession session) {
        final User sessionUser = userService.getNotLoggedTemporaryUser();

        final Environment environment = new Environment(sessionUser);

        session.setAttribute(ENVIRONMENT_SESSION_KEY, environment);

        return environment;
    }

    private Environment getSessionEnvironment(final HttpSession session) {
        return (Environment) session.getAttribute(ENVIRONMENT_SESSION_KEY);
    }
}
