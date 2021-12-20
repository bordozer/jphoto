package com.bordozer.jphoto.ui.services.security;

import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.UsersSecurity;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public interface UsersSecurityService {

    String BEAN_NAME = "usersSecurityService";

    boolean isUserPasswordCorrect(final User user, final String password);

    UsersSecurity load(final User user);

    void registerUserLogin(final User user, final HttpServletRequest request);

    void resetEnvironmentAndLogOutUser(final User user);

    void assertLoggedUserRequestSecurityPassed(final User user, final HttpServletRequest request);

    Date getLastUserActivityTime(final int userId);

    void saveLastUserActivityTime(final int userId, final Date time);

    void saveLastUserActivityTimeNow(final int userId);

    void createEntry(final int userId, final String uncryptedPassword);

    void validatePasswordCreation(final String password, final String confirmPassword, final Errors errors);

    void changeUserPassword(final User user, final String password);

    String getStoredUserAuthorizationKey(final User user);
}
