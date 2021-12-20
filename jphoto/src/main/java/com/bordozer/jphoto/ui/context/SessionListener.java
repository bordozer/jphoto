package com.bordozer.jphoto.ui.context;

import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.log.LogHelper;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Date;

@Component
public class SessionListener implements HttpSessionListener {

    final LogHelper log = new LogHelper();

    @Override
    public void sessionCreated(final HttpSessionEvent httpSessionEvent) {
        final DateUtilsService dateUtilsService = ApplicationContextHelper.getDateUtilsService();

        final HttpSession session = httpSessionEvent.getSession();

        final int sessionTimeoutMinutes = ApplicationContextHelper.getConfigurationService().getInt(ConfigurationKey.SYSTEM_SESSION_TIMEOUT_IN_MINUTES);
        final int sessionTimeoutSeconds = sessionTimeoutMinutes * 60;
        session.setMaxInactiveInterval(sessionTimeoutSeconds);

        final String info = String.format("Session is STARTED: id = '%s', creation time='%s' ( session timeout: %d minutes )"
                , session.getId(), dateUtilsService.formatDateTime(new Date(session.getCreationTime())), sessionTimeoutMinutes);

        log.info(info);
    }

    @Override
    public void sessionDestroyed(final HttpSessionEvent httpSessionEvent) {
        final DateUtilsService dateUtilsService = ApplicationContextHelper.getDateUtilsService();

        final HttpSession session = httpSessionEvent.getSession();

        EnvironmentContext.clear();

        final String info = String.format("Session is DESTROYED: id = '%s', last accessed time='%s'"
                , session.getId(), dateUtilsService.formatDateTime(new Date(session.getLastAccessedTime())));
        log.info(info);
    }
}
