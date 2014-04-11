package core.services.utils;

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
	public int getPhotosInLine( final User user ) {

		final boolean isMobileDevice = PhotoUtils.isMobileDevice( EnvironmentContext.getDeviceType() ); // TODO: EnvironmentContext in service

		final int systemPhotosInLine = configurationService.getConfiguration( ConfigurationKey.PHOTO_LIST_PHOTOS_IN_LINE ).getValueInt();
		final int systemPhotosInLineForMobileDevices = configurationService.getConfiguration( ConfigurationKey.PHOTO_LIST_PHOTOS_IN_LINE_FOR_MOBILE_DEVICES ).getValueInt();

		if ( ! UserUtils.isLoggedUser( user ) ) {
			return isMobileDevice ? systemPhotosInLineForMobileDevices : systemPhotosInLine;
		}

		int photosInLine = user.getPhotosInLine();

		if ( photosInLine > 0 ) {
			return photosInLine;
		}

		return isMobileDevice ? systemPhotosInLineForMobileDevices : systemPhotosInLine;
	}

	@Override
	public int getPhotosOnPage( final User user ) {
		return getPhotosInLine( user ) * user.getPhotoLines();
	}
}
