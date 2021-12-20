package com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web;

import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.LocalCategory;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.naturelight.NaturelightCategory;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.photos35.Photo35Category;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.photosight.PhotosightCategory;

public interface RemotePhotoSiteCategory {

    int getId();

    String getName();

    String getKey();

    public static RemotePhotoSiteCategory[] getRemotePhotoSiteCategories(final PhotosImportSource photosImportSource) {

        switch (photosImportSource) {
            case FILE_SYSTEM:
                return LocalCategory.values();
            case PHOTOSIGHT:
                return PhotosightCategory.values();
            case PHOTO35:
                return Photo35Category.values();
            case NATURELIGHT:
                return NaturelightCategory.values();
        }

        throw new IllegalArgumentException(String.format("Unsupported photos import source: %s", photosImportSource));
    }

    public static RemotePhotoSiteCategory getById(final PhotosImportSource photosImportSource, final int id) {
        switch (photosImportSource) {
            case FILE_SYSTEM:
                return LocalCategory.getById(id);
            case PHOTOSIGHT:
                return PhotosightCategory.getById(id);
            case PHOTO35:
                return Photo35Category.getById(id);
            case NATURELIGHT:
                return NaturelightCategory.getById(id);
        }

        throw new IllegalArgumentException(String.format("Unsupported photos import source: %s", photosImportSource));
    }
}
