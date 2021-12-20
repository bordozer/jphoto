package com.bordozer.jphoto.core.services.utils;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service("networkUtils")
public class NetworkUtilsImpl implements NetworkUtils {

    @Override
    public String getClientIP(final HttpServletRequest request) {

        final String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            return request.getRemoteAddr();
        }

        return ipAddress;
    }
}
