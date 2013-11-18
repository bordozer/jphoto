package kwaqua.tools.utils;

public class SystemVars {

	public static long getDefaultImplicitTimeout() {
		return Long.parseLong( System.getProperty( "webdriver.timeouts.implicitlywait" ) );
	}

	public static long getControlWaitTimeout() {
		return Long.parseLong( System.getProperty( "webdriver.timeouts.controlwait" ) );
	}
}
