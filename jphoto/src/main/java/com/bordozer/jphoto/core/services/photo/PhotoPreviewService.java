package com.bordozer.jphoto.core.services.photo;

import com.bordozer.jphoto.core.general.photo.PhotoPreview;
import com.bordozer.jphoto.core.interfaces.BaseEntityService;

import java.util.List;

public interface PhotoPreviewService extends BaseEntityService<PhotoPreview> {

    String BEAN_NAME = "photoPreviewService";

    boolean hasUserAlreadySeenThisPhoto(final int photoId, final int userId);

    PhotoPreview load(final int photoId, final int userId);

    void deletePreviews(final int photoId);

    int getPreviewCount(final int photoId);

    int getPreviewCount();

    List<PhotoPreview> getPreviews(final int photoId);

    List<Integer> getLastUsersWhoViewedUserPhotos(final int userId, final int qty);
}
