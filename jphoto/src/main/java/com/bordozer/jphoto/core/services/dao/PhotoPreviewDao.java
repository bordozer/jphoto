package com.bordozer.jphoto.core.services.dao;

import com.bordozer.jphoto.core.general.photo.PhotoPreview;

import java.util.List;

public interface PhotoPreviewDao extends BaseEntityDao<PhotoPreview> {

    boolean hasUserAlreadySeenThisPhoto(final int photoId, final int userId);

    PhotoPreview load(final int photoId, final int userId);

    void deletePreviews(final int photoId);

    int getPreviewCount(final int photoId);

    int getPreviewCount();

    List<PhotoPreview> getPreviews(final int photoId);

    List<Integer> getLastUsersWhoViewedUserPhotos(final int userId, final int qty);
}
