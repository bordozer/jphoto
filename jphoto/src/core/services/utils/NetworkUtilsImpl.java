package core.services.utils;

import javax.servlet.http.HttpServletRequest;

public class NetworkUtilsImpl implements NetworkUtils {

	@Override
	public String getClientIP( final HttpServletRequest request ) {

		final String ipAddress = request.getHeader( "X-FORWARDED-FOR" );
		if ( ipAddress == null ) {
			return request.getRemoteAddr();
		}

		return ipAddress;
	}
}
