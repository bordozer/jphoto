package com.bordozer.jphoto.core.services.photo;

import com.bordozer.jphoto.core.general.cache.CacheKey;
import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.photo.PhotoPreview;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.dao.PhotoPreviewDao;
import com.bordozer.jphoto.core.services.entry.ActivityStreamService;
import com.bordozer.jphoto.core.services.security.SecurityService;
import com.bordozer.jphoto.core.services.system.CacheService;
import com.bordozer.jphoto.core.services.system.ConfigurationService;
import com.bordozer.jphoto.core.services.utils.UserPhotoFilePathUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("photoPreviewService")
public class PhotoPreviewServiceImpl implements PhotoPreviewService {

    @Autowired
    private PhotoPreviewDao photoPreviewDao;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private ActivityStreamService activityStreamService;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private UserPhotoFilePathUtilsService userPhotoFilePathUtilsService;

    @Override
    public boolean hasUserAlreadySeenThisPhoto(final int photoId, final int userId) {
        return photoPreviewDao.hasUserAlreadySeenThisPhoto(photoId, userId);
    }

    @Override
    public boolean save(final PhotoPreview entry) {
        final boolean isNew = entry.isNew();
        final int photoId = entry.getPhoto().getId();
        final Photo photo = photoService.load(photoId);

        final User viewer = entry.getUser();

        if (securityService.userOwnThePhoto(viewer, photo)) {
            return true;
        }

        final boolean isSuccessful = photoPreviewDao.saveToDB(entry);

        if (isSuccessful) {
            cacheService.expire(CacheKey.PHOTO_INFO, photoId);
        }

        if (isNew && configurationService.getBoolean(ConfigurationKey.SYSTEM_ACTIVITY_LOG_PHOTO_PREVIEWS)) {
            activityStreamService.savePhotoPreview(entry);
        }

        return isSuccessful;
    }

    @Override
    public PhotoPreview load(final int id) {
        return photoPreviewDao.load(id);
    }

    @Override
    public boolean delete(final int entryId) {
        return photoPreviewDao.delete(entryId);
    }

    @Override
    public boolean exists(final int entryId) {
        return photoPreviewDao.exists(entryId);
    }

    @Override
    public boolean exists(final PhotoPreview entry) {
        return photoPreviewDao.exists(entry);
    }

    @Override
    public PhotoPreview load(final int photoId, final int userId) {
        return photoPreviewDao.load(photoId, userId);
    }

    @Override
    public void deletePreviews(final int photoId) {
        photoPreviewDao.deletePreviews(photoId);
    }

    @Override
    public int getPreviewCount(final int photoId) {
        return photoPreviewDao.getPreviewCount(photoId);
    }

    @Override
    public int getPreviewCount() {
        return photoPreviewDao.getPreviewCount();
    }

    @Override
    public List<PhotoPreview> getPreviews(final int photoId) {
        return photoPreviewDao.getPreviews(photoId);
    }

    @Override
    public List<Integer> getLastUsersWhoViewedUserPhotos(final int userId, final int qty) {
        return photoPreviewDao.getLastUsersWhoViewedUserPhotos(userId, qty);
    }
}
