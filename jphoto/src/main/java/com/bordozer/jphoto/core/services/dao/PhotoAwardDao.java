package com.bordozer.jphoto.core.services.dao;

import com.bordozer.jphoto.core.general.photo.PhotoAward;

import java.util.List;

public interface PhotoAwardDao {

    boolean doesPhotoHaveThisAward(final PhotoAward photoAward);

    void savePhotoAward(final PhotoAward photoAward);

    List<PhotoAward> getPhotoAwards(final int photoId);

    void deletePhotoAwards(final int photoId);
}
