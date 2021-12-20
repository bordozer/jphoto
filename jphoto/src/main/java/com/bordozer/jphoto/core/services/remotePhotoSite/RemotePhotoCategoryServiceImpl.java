package com.bordozer.jphoto.core.services.remotePhotoSite;

import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.LocalCategory;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.PhotosImportSource;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategoriesMappingStrategy;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategory;
import com.bordozer.jphoto.admin.controllers.jobs.edit.photosImport.strategies.web.RemotePhotoSiteCategoryToGenreMapping;
import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.log.LogHelper;
import com.bordozer.jphoto.core.services.entry.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("remotePhotoCategoryService")
public class RemotePhotoCategoryServiceImpl implements RemotePhotoCategoryService {

    @Autowired
    private GenreService genreService;

    private final LogHelper log = new LogHelper();

    @Override
    public LocalCategory getMappedGenreDiscEntry(final RemotePhotoSiteCategory remotePhotoSiteCategory) {

        for (final RemotePhotoSiteCategoryToGenreMapping photoCategoryMapping : RemotePhotoSiteCategoriesMappingStrategy.getStrategyFor(remotePhotoSiteCategory).getMapping()) {
            if (photoCategoryMapping.getRemotePhotoSiteCategory() == remotePhotoSiteCategory) {
                return photoCategoryMapping.getLocalCategory();
            }
        }

        return null;
    }

    @Override
    public Genre getMappedGenreOrOther(final RemotePhotoSiteCategory remotePhotoSiteCategory) {

        final LocalCategory localCategory = getMappedGenreDiscEntry(remotePhotoSiteCategory);
        if (localCategory != null) {
            final Genre genre = getGenreBy(localCategory);
            if (genre != null) {
                return genre;
            }
        }

        log.warn(String.format("Remote photo site category %s does not mach any genre", remotePhotoSiteCategory));

        return getGenreBy(LocalCategory.OTHER);
    }

    @Override
    public Genre getMappedGenreOrOther(final PhotosImportSource photosImportSource, final String genreName) {

        final Genre genre = getMappedGenreOrNull(photosImportSource, genreName);
        if (genre != null) {
            return genre;
        }

        return getGenreBy(LocalCategory.OTHER);
    }

    @Override
    public Genre getMappedGenreOrNull(final RemotePhotoSiteCategory remotePhotoSiteCategory) {

        final LocalCategory localCategory = getMappedGenreDiscEntry(remotePhotoSiteCategory);
        if (localCategory != null) {
            return genreService.loadByName(localCategory.getKey());
        }

        return null;
    }

    @Override
    public Genre getMappedGenreOrNull(final PhotosImportSource photosImportSource, final String genreName) {
        final RemotePhotoSiteCategory[] remotePhotoSiteCategories = RemotePhotoSiteCategory.getRemotePhotoSiteCategories(photosImportSource);

        for (final RemotePhotoSiteCategory remotePhotoSiteCategory : remotePhotoSiteCategories) {
            if (remotePhotoSiteCategory.getKey().equals(genreName)) {
                return getMappedGenreOrNull(remotePhotoSiteCategory);
            }
        }

        return null;
    }

    private Genre getGenreBy(final LocalCategory localCategory) {
        final Genre genre = genreService.loadByName(localCategory.getKey());
        if (genre != null) {
            return genre;
        }

        return null;
    }
}
