package com.bordozer.jphoto.core.services.photo;

import com.bordozer.jphoto.core.general.photo.PhotoAward;

import java.util.List;

public interface PhotoAwardService {

    void calculatePhotoAwards(final int photoId);

    List<PhotoAward> getPhotoAwards(final int photoId);

    void deletePhotoAwards(final int photoId);
}
