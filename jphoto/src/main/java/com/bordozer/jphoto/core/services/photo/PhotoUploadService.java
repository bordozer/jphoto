package com.bordozer.jphoto.core.services.photo;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;

import java.util.List;

public interface PhotoUploadService {

    List<Integer> getUploadedTodayPhotosIds(final User user);

    List<Integer> getUploadedThisWeekPhotosIds(final User user);

    List<Photo> getUploadedThisWeekPhotos(final User user);

    long getUploadedTodayPhotosSummarySize(final User user);

    long getUploadedThisWeekPhotosSummarySize(final User user);
}
