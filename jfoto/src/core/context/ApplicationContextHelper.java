package core.context;

import core.exceptions.BaseRuntimeException;
import core.services.security.SecurityService;
import core.services.system.ConfigurationService;
import core.services.utils.*;
import org.springframework.context.ApplicationContext;

public class ApplicationContextHelper {

	private static ApplicationContext springContext = null;

	private ApplicationContextHelper() {
	}

	public static ApplicationContext getSpringContext() {
		return springContext;
	}

	public static void setSpringContext( final ApplicationContext springContext ) {
		ApplicationContextHelper.springContext = springContext;
	}

	public static <T> T getBean( final String name ) {
		T bean = ( T ) springContext.getBean( name );
		if ( bean == null ) {
			throw new BaseRuntimeException( String.format( "No bean named %s has been configured; see Spring config ", name ) );
		}
		return bean;
	}

	public static UrlUtilsService getUrlUtilsService() {
		return getBean( UrlUtilsServiceImpl.BEAN_NAME );
	}

	public static EntityLinkUtilsService getEntityLinkUtilsService() {
		return getBean( EntityLinkUtilsService.BEAN_NAME );
	}

	public static SystemVarsService getSystemVarsService() {
		return getBean( SystemVarsService.BEAN_NAME );
	}

	public static ImageFileUtilsService getImageFileUtilsService() {
		return getBean( ImageFileUtilsService.BEAN_NAME );
	}

	public static DateUtilsService getDateUtilsService() {
		return getBean( DateUtilsService.BEAN_NAME );
	}

	public static ConfigurationService getConfigurationService() {
		return getBean( ConfigurationService.BEAN_NAME );
	}

	public static SecurityService getSecurityService() {
		return getBean( SecurityService.BEAN_NAME );
	}
}
