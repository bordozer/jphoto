package com.bordozer.jphoto.utils;

import com.bordozer.jphoto.core.general.genre.Genre;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceType;
import org.springframework.mobile.device.DeviceUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class PhotoUtils {

    public static boolean isPhotoContentTypeSupported(final List allowedExtensions, final String contentType) {
        return allowedExtensions.contains(contentType);
    }

    public static int getGenreMinBallForBest(final Genre genre, final int defaultMinMarksToBeInTheBestPhotoOfGenre) {
        final int minMarksForBest = genre.getMinMarksForBest();
        return minMarksForBest > 0 ? minMarksForBest : defaultMinMarksToBeInTheBestPhotoOfGenre;
    }

    public static String formatPhotoCommentText(final String text) {
        return StringUtilities.escapeHtml(text).replace("\n", "<br />");
    }

    public static DeviceType getDeviceType(final HttpServletRequest request) {
        final Device currentDevice = DeviceUtils.getCurrentDevice(request);
        if (currentDevice == null) {
            return DeviceType.NORMAL;
        }
        if (currentDevice.isTablet()) {
            return DeviceType.TABLET;
        }
        if (currentDevice.isMobile()) {
            return DeviceType.MOBILE;
        }
        return DeviceType.NORMAL;
    }

    public static boolean isMobileDevice(final DeviceType deviceType) {
        return deviceType == DeviceType.MOBILE;
    }
}
