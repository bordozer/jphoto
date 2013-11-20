package core.context;

import core.log.LogHelper;
import core.services.utils.SystemVarsService;
import core.services.utils.DateUtilsService;

import javax.servlet.http.*;
import java.util.Date;

public class SessionListener implements HttpSessionListener {

	final LogHelper log = new LogHelper( SessionListener.class );

	@Override
	public void sessionCreated( final HttpSessionEvent httpSessionEvent ) {
		final DateUtilsService dateUtilsService = ApplicationContextHelper.getDateUtilsService();
		final SystemVarsService systemVarsService = ApplicationContextHelper.getSystemVarsService();

		final HttpSession session = httpSessionEvent.getSession();

		final int sessionTimeoutMins = systemVarsService.getSessionTimeoutInMinutes() * 60;
		session.setMaxInactiveInterval( sessionTimeoutMins );

		final String info = String.format( "Session is STARTED: id = '%s', creation time='%s' ( session timeout: %d minutes )"
			, session.getId(), dateUtilsService.formatDateTime( new Date( session.getCreationTime() ) ), sessionTimeoutMins );

		log.info( info );
	}

	@Override
	public void sessionDestroyed( final HttpSessionEvent httpSessionEvent ) {
		final DateUtilsService dateUtilsService = ApplicationContextHelper.getDateUtilsService();

		final HttpSession session = httpSessionEvent.getSession();

		EnvironmentContext.clear();

		final String info = String.format( "Session is DESTROYED: id = '%s', last accessed time='%s'"
			, session.getId(), dateUtilsService.formatDateTime( new Date( session.getLastAccessedTime() ) ) );
		log.info( info );
	}
}
