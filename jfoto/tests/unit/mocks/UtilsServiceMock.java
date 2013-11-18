package mocks;

import core.general.user.User;
import core.services.utils.UtilsService;

public class UtilsServiceMock implements UtilsService {

	@Override
	public int getPhotosInLine( final User user ) {
		return -1;
	}

	@Override
	public int getPhotosOnPage( final User user ) {
		return 2;
	}
}
