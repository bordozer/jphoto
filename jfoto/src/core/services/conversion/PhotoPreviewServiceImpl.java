package core.services.conversion;

import core.general.configuration.ConfigurationKey;
import core.general.photo.Photo;
import core.general.user.User;
import core.general.cache.CacheKey;
import core.general.photo.PhotoPreview;
import core.services.entry.ActivityStreamService;
import core.services.system.CacheService;
import core.services.security.SecurityService;
import core.services.dao.PhotoPreviewDao;
import core.services.photo.PhotoService;
import core.services.system.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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

	@Override
	public boolean hasUserAlreadySeenThisPhoto( final int photoId, final int userId ) {
		return photoPreviewDao.hasUserAlreadySeenThisPhoto( photoId, userId );
	}

	@Override
	public boolean save( final PhotoPreview entry ) {
		final boolean isNew = entry.isNew();
		final int photoId = entry.getPhoto().getId();
		final Photo photo = photoService.load( photoId );

		final User viewer = entry.getUser();

		if ( securityService.userOwnThePhoto( viewer, photo ) ) {
			return true;
		}

		final boolean isSuccessful = photoPreviewDao.saveToDB( entry );

		if ( isSuccessful ) {
			cacheService.expire( CacheKey.PHOTO_INFO, photoId );
		}

		if ( isNew && configurationService.getBoolean( ConfigurationKey.SYSTEM_ACTIVITY_LOG_PHOTO_PREVIEWS ) ) {
			activityStreamService.savePhotoPreview( entry );
		}

		return isSuccessful;
	}

	@Override
	public PhotoPreview load( final int id ) {
		return photoPreviewDao.load( id );
	}

	@Override
	public boolean delete( final int entryId ) {
		return photoPreviewDao.delete( entryId );
	}

	@Override
	public boolean exists( final int entryId ) {
		return photoPreviewDao.exists( entryId );
	}

	@Override
	public boolean exists( final PhotoPreview entry ) {
		return photoPreviewDao.exists( entry );
	}

	@Override
	public PhotoPreview load( final int photoId, final int userId ) {
		return photoPreviewDao.load( photoId, userId );
	}

	@Override
	public void deletePreviews( final int photoId ) {
		photoPreviewDao.deletePreviews( photoId );
	}

	@Override
	public int getPreviewCount( final int photoId ) {
		return photoPreviewDao.getPreviewCount( photoId );
	}

	@Override
	public List<PhotoPreview> getPreviews( final int photoId ) {
		return photoPreviewDao.getPreviews( photoId );
	}

	@Override
	public List<Integer> getLastUsersWhoViewedUserPhotos( final int userId, final int qty ) {
		return photoPreviewDao.getLastUsersWhoViewedUserPhotos( userId, qty );
	}
}
