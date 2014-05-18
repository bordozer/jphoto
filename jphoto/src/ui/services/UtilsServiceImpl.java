package ui.services;

import core.general.configuration.ConfigurationKey;
import core.general.user.User;
import core.services.system.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import ui.context.EnvironmentContext;
import utils.PhotoUtils;
import utils.UserUtils;

public class UtilsServiceImpl implements UtilsService {

	@Autowired
	private ConfigurationService configurationService;

	@Override
	public int getPhotosOnPage( final User user ) {

		if ( user.getPhotosOnPage() > 0 ) {
			return user.getPhotosOnPage();
		}

		final boolean isMobileDevice = PhotoUtils.isMobileDevice( EnvironmentContext.getDeviceType() ); // TODO: EnvironmentContext in service

		final int systemPhotosOnPage = configurationService.getConfiguration( ConfigurationKey.PHOTO_LIST_PHOTOS_ON_PAGE ).getValueInt();
		final int systemPhotosOnPageForMobileDevices = configurationService.getConfiguration( ConfigurationKey.PHOTO_LIST_PHOTOS_ON_PAGE_FOR_MOBILE_DEVICES ).getValueInt();

		if ( ! UserUtils.isLoggedUser( user ) ) {
			return isMobileDevice ? systemPhotosOnPageForMobileDevices : systemPhotosOnPage;
		}

		return isMobileDevice ? systemPhotosOnPageForMobileDevices : systemPhotosOnPage;
	}
}
