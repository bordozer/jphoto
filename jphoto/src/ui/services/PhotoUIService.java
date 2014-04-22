package ui.services;

import core.general.photo.Photo;
import core.general.photo.PhotoInfo;
import core.general.photo.PhotoPreviewWrapper;
import core.general.user.User;

public interface PhotoUIService {

	PhotoInfo getPhotoInfo( final Photo photo, final User accessor );

	PhotoPreviewWrapper getPhotoPreviewWrapper( final Photo photo, final User user );
}
