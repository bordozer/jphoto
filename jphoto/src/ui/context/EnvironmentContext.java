package ui.context;

import core.exceptions.BaseRuntimeException;
import core.general.user.User;
import core.services.translator.Language;
import org.springframework.mobile.device.DeviceType;

public class EnvironmentContext {

	private static final ThreadLocal<Environment> environment = new InheritableThreadLocal<Environment>();

	private EnvironmentContext() {
	}

	public static Environment getEnvironment() {
		if ( environment == null ) {
			throw new BaseRuntimeException( "Error reading thread-local Environment" );
		}

		return environment.get();
	}

	public static User getCurrentUser() {
		return getEnvironment().getCurrentUser();
	}

	public static Language getLanguage() {
		return getEnvironment().getCurrentUser().getLanguage();
	}

	public static int getCurrentUserId() {
		return getCurrentUser().getId();
	}

	public static DeviceType getDeviceType() {
		return getEnvironment().getDeviceType();
	}

	public static boolean isShowNudeContext() {
		return getEnvironment().isShowNudeContent();
	}

	public static String getHiMessage() {
		return getEnvironment().getHiMessage();
	}

	public static void switchUser( final User user ) {
		final Environment env = getEnvironment();

		env.setCurrentUser( user );
		env.setShowNudeContent( false );

		setEnv( env );
	}

	public static void clear() {
		environment.remove();
	}

	public static void setEnv( final Environment env ) {
		environment.set( env );
	}
}
