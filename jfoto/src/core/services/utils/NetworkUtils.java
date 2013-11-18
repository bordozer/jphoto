package core.services.utils;

import javax.servlet.http.HttpServletRequest;

public interface NetworkUtils {

	String getClientIP( final HttpServletRequest request );
}
