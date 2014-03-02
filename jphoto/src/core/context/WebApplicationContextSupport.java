package core.context;

import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public final class WebApplicationContextSupport implements ServletContextListener {

	public void contextInitialized( final ServletContextEvent servletContextEvent ) {
		ApplicationContextHelper.setSpringContext( WebApplicationContextUtils.getWebApplicationContext( servletContextEvent.getServletContext() ) );
	}

	public void contextDestroyed( final ServletContextEvent servletContextEvent ) {
		ApplicationContextHelper.setSpringContext( null );
	}
}
