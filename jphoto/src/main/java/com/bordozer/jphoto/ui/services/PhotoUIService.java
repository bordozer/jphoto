package com.bordozer.jphoto.ui.services;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.photo.PhotoInfo;
import com.bordozer.jphoto.core.general.photo.PhotoPreviewWrapper;
import com.bordozer.jphoto.core.general.user.User;

public interface PhotoUIService {

    PhotoInfo getPhotoInfo(final Photo photo, final User accessor);

    PhotoPreviewWrapper getPhotoPreviewWrapper(final Photo photo, final User user);
}
