package com.bordozer.jphoto.ui.services;

import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.ConfigurationService;
import com.bordozer.jphoto.ui.context.EnvironmentContext;
import com.bordozer.jphoto.utils.PhotoUtils;
import com.bordozer.jphoto.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("utilsService")
public class UtilsServiceImpl implements UtilsService {

    @Autowired
    private ConfigurationService configurationService;

    @Override
    public int getPhotosOnPage(final User user) {

        if (user.getPhotosOnPage() > 0) {
            return user.getPhotosOnPage();
        }

        final boolean isMobileDevice = PhotoUtils.isMobileDevice(EnvironmentContext.getDeviceType()); // TODO: EnvironmentContext in service

        final int systemPhotosOnPage = configurationService.getConfiguration(ConfigurationKey.PHOTO_LIST_PHOTOS_ON_PAGE).getValueInt();
        final int systemPhotosOnPageForMobileDevices = configurationService.getConfiguration(ConfigurationKey.PHOTO_LIST_PHOTOS_ON_PAGE_FOR_MOBILE_DEVICES).getValueInt();

        if (!UserUtils.isLoggedUser(user)) {
            return isMobileDevice ? systemPhotosOnPageForMobileDevices : systemPhotosOnPage;
        }

        return isMobileDevice ? systemPhotosOnPageForMobileDevices : systemPhotosOnPage;
    }
}
