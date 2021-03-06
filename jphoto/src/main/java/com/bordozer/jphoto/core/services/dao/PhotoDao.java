package com.bordozer.jphoto.core.services.dao;

import com.bordozer.jphoto.core.general.photo.Photo;

import java.util.List;

public interface PhotoDao extends BaseEntityDao<Photo> {

    int getPhotoQty();

    int getPhotoQtyByGenre(final int genreId);

    int getPhotoQtyByUser(final int userId);

    int getPhotoQtyByUserAndGenre(final int userId, final int genreId);

    int getLastUserPhotoId(final int userId);

    int getLastGenrePhotoId(final int genreId);

    List<Integer> getUserPhotosIds(final int userId);

    boolean isUserPhotoImported(final int userId, final int importId);
}
