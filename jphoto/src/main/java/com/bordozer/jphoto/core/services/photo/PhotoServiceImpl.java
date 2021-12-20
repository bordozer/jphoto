package com.bordozer.jphoto.core.services.photo;

import com.bordozer.jphoto.core.enums.PhotoActionAllowance;
import com.bordozer.jphoto.core.exceptions.BaseRuntimeException;
import com.bordozer.jphoto.core.exceptions.SaveToDBException;
import com.bordozer.jphoto.core.general.base.PagingModel;
import com.bordozer.jphoto.core.general.cache.CacheKey;
import com.bordozer.jphoto.core.general.cache.keys.UserGenreCompositeKey;
import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.photoTeam.PhotoTeam;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.UserPhotosByGenre;
import com.bordozer.jphoto.core.general.user.userAlbums.UserPhotoAlbum;
import com.bordozer.jphoto.core.log.LogHelper;
import com.bordozer.jphoto.core.services.conversion.PreviewGenerationService;
import com.bordozer.jphoto.core.services.dao.PhotoDao;
import com.bordozer.jphoto.core.services.entry.ActivityStreamService;
import com.bordozer.jphoto.core.services.entry.GenreService;
import com.bordozer.jphoto.core.services.entry.PrivateMessageService;
import com.bordozer.jphoto.core.services.notification.NotificationService;
import com.bordozer.jphoto.core.services.security.SecurityService;
import com.bordozer.jphoto.core.services.system.CacheService;
import com.bordozer.jphoto.core.services.system.ConfigurationService;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.core.services.translator.message.TranslatableMessage;
import com.bordozer.jphoto.core.services.user.UserPhotoAlbumService;
import com.bordozer.jphoto.core.services.user.UserService;
import com.bordozer.jphoto.core.services.user.UserTeamService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.core.services.utils.ImageFileUtilsService;
import com.bordozer.jphoto.core.services.utils.UserPhotoFilePathUtilsService;
import com.bordozer.jphoto.core.services.utils.sql.PhotoListQueryBuilder;
import com.bordozer.jphoto.sql.SqlSelectIdsResult;
import com.bordozer.jphoto.sql.builder.SqlIdsSelectQuery;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

@Service("photoService")
public class PhotoServiceImpl implements PhotoService {

    private static final String CACHE_KEY_PHOTO_SERVICE_TOTAL_PHOTOS = "photoService.total.photos";

    @Autowired
    private PhotoDao photoDao;

    @Autowired
    private GenreService genreService;

    @Autowired
    private PhotoVotingService photoVotingService;

    @Autowired
    private PhotoCommentService photoCommentService;

    @Autowired
    private PhotoCommentArchService photoCommentArchService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private PhotoPreviewService photoPreviewService;

    @Autowired
    private PhotoRatingService photoRatingService;

    @Autowired
    private PhotoAwardService photoAwardService;

    @Autowired
    private UserTeamService userTeamService;

    @Autowired
    private UserPhotoAlbumService userPhotoAlbumService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private DateUtilsService dateUtilsService;

    @Autowired
    private ActivityStreamService activityStreamService;

    @Autowired
    private Services services;

    @Autowired
    private UserService userService;

    @Autowired
    private PreviewGenerationService previewGenerationService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private PrivateMessageService privateMessageService;

    @Autowired
    private ImageFileUtilsService imageFileUtilsService;

    @Autowired
    private UserPhotoFilePathUtilsService userPhotoFilePathUtilsService;

    private final LogHelper log = new LogHelper();

    @Override
    @Caching(evict = {
            @CacheEvict(value = CACHE_KEY_PHOTO_SERVICE_TOTAL_PHOTOS, allEntries = true),
    })
    public boolean save(final Photo entry) {

        final boolean isNew = entry.isNew();

        final boolean isSaved = photoDao.saveToDB(entry);

        if (isSaved && isNew) {
            new Thread() {

                @Override
                public void run() {
                    notificationService.newPhotoNotification(entry);
                }
            }.start();
        }

        cacheService.expire(CacheKey.PHOTO, entry.getId());
        cacheService.expire(CacheKey.PHOTO_INFO, entry.getId());

        expireUserPhotosCountByGenreCache(entry.getUserId());

        if (isNew) {
            activityStreamService.savePhotoUpload(entry);
        }

        return isSaved;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = CACHE_KEY_PHOTO_SERVICE_TOTAL_PHOTOS, allEntries = true),
    })
    public void uploadNewPhoto(final Photo photo, final File photoImageFile, final PhotoTeam photoTeam, final List<UserPhotoAlbum> photoAlbums) throws SaveToDBException, IOException {

        final User photoAuthor = userService.load(photo.getUserId());

        final File copiedToUserFolderFile = userPhotoFilePathUtilsService.copyPhotoImageFileToUserFolder(photoAuthor, photoImageFile);
        photo.setPhotoImageFile(copiedToUserFolderFile);

        try {
            final File preview = previewGenerationService.generatePreviewSync(photoAuthor, copiedToUserFolderFile);
            createPhotoDBEntry(photo, copiedToUserFolderFile, preview, photoTeam, photoAlbums);

            log.debug(String.format("User %s has been uploaded photo '%s'", userService.load(photo.getUserId()).getName(), photo.getName()));
            if (preview == null) {
                throw new IOException(String.format("Can not generate photo preview for '%s'", copiedToUserFolderFile.getCanonicalPath()));
            }
        } catch (final InterruptedException e) {
            FileUtils.deleteQuietly(copiedToUserFolderFile);
            throw new BaseRuntimeException(String.format("Can not copy file to user folder: '%s'", photoImageFile.getAbsolutePath()));
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = CACHE_KEY_PHOTO_SERVICE_TOTAL_PHOTOS, allEntries = true),
    })
    public void uploadNewPhoto(final Photo photo, final File photoImageFile, final String photoImageUrl, final PhotoTeam photoTeam, final List<UserPhotoAlbum> photoAlbums) throws SaveToDBException, IOException {

        final User photoAuthor = userService.load(photo.getUserId());

        try {
            final File preview = previewGenerationService.generatePreviewSync(photoAuthor, photoImageFile);

            photo.setPhotoImageUrl(photoImageUrl);

            createPhotoDBEntry(photo, photoImageFile, preview, photoTeam, photoAlbums);
            if (preview == null) {
                throw new IOException(String.format("Can not generate photo preview for '%s'", photoImageFile.getCanonicalPath()));
            }
        } catch (final InterruptedException e) {
            throw new BaseRuntimeException(String.format("Can not copy file to user folder: '%s'", photoImageFile.getAbsolutePath()));
        }
    }

    @Override
    public void updatePhoto(final Photo photo, final PhotoTeam photoTeam, final List<UserPhotoAlbum> photoAlbums) throws SaveToDBException {

        if (!save(photo)) {
            throw new SaveToDBException(String.format("Can not save photo: %s", photo));
        }

        if (!userTeamService.savePhotoTeam(photoTeam)) {
            throw new SaveToDBException(String.format("Can not save photo team: %s", photoTeam));
        }

        if (!userPhotoAlbumService.savePhotoAlbums(photo, photoAlbums)) {
            throw new SaveToDBException(String.format("Can not save photo albums: %s", photoAlbums));
        }
    }

    @Override
    public Photo load(final int photoId) {
        return photoDao.load(photoId);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = CACHE_KEY_PHOTO_SERVICE_TOTAL_PHOTOS, allEntries = true),
    })
    public boolean delete(final int entryId) {

        final Photo photo = load(entryId); // Must be loaded before photo photoDao.delete()

        photoPreviewService.deletePreviews(entryId);

        photoCommentService.deletePhotoComments(entryId);

        photoCommentArchService.deletePhotoComments(entryId);

        photoVotingService.deletePhotoVoting(entryId);

        photoAwardService.deletePhotoAwards(entryId);

        photoRatingService.deletePhotoRatings(entryId);

        userTeamService.deletePhotoTeam(entryId);

        userPhotoAlbumService.deletePhotoFromAllAlbums(entryId);

        final boolean isDeleted = photoDao.delete(entryId);

        if (isDeleted) {
            userPhotoFilePathUtilsService.deletePhotoFileWithPreview(photo);

            cacheService.expire(CacheKey.PHOTO, entryId);
            cacheService.expire(CacheKey.PHOTO_INFO, entryId);

            expireUserPhotosCountByGenreCache(photo.getUserId());
        }

        return isDeleted;
    }

    @Override
    public SqlSelectIdsResult load(final SqlIdsSelectQuery selectIdsQuery) {
        return photoDao.load(selectIdsQuery);
    }

    @Override
    public List<Photo> load(final List<Integer> photoIds) {
        final List<Photo> result = newArrayList();

        for (final int photoId : photoIds) {
            result.add(load(photoId));
        }

        return result;
    }

    @Override
    @Cacheable(value = CACHE_KEY_PHOTO_SERVICE_TOTAL_PHOTOS)
    public int getPhotosCount() {
        return photoDao.getPhotoQty();
    }

    @Override
    public int getPhotosCountByGenre(final int genreId) {
        return photoDao.getPhotoQtyByGenre(genreId);
    }

    @Override
    public int getPhotosCountByUser(final int userId) {
        return photoDao.getPhotoQtyByUser(userId);
    }

    @Override
    public List<Integer> getUserPhotosIds(final int userId) {
        return photoDao.getUserPhotosIds(userId);
    }

    @Override
    public int getPhotosCountByUserAndGenre(final int userId, final int genreId) {
        return photoDao.getPhotoQtyByUserAndGenre(userId, genreId);
    }

    @Override
    public Set<Genre> getUserPhotoGenres(final int userId) {
        final Set<Genre> result = newHashSet();

        final List<Genre> genres = genreService.loadAll();
        for (final Genre genre : genres) {
            final int photosInGenre = getPhotosCountByUserAndGenre(userId, genre.getId());
            if (photosInGenre > 0) {
                result.add(genre);
            }
        }

        return result;
    }

    @Override
    public int getPhotosCountByGenreForPeriod(final Genre genre, final Date timeFrom, final Date timeTo) {
        final SqlIdsSelectQuery query = new PhotoListQueryBuilder(dateUtilsService).filterByGenre(genre).filterByUploadTime(timeFrom, timeTo).getQuery(); // TODO: write test!

        final SqlSelectIdsResult idsResult = load(query);

        return idsResult.getRecordQty();
    }

    @Override
    public int getLastGenrePhotoId(final int genreId) {
        return photoDao.getLastGenrePhotoId(genreId);
    }

    @Override
    public Date getPhotoAnonymousPeriodExpirationTime(final Photo photo) {
        final int anonymousPeriod = configurationService.getConfiguration(ConfigurationKey.PHOTO_UPLOAD_ANONYMOUS_PERIOD).getValueInt();
        return dateUtilsService.getDatesOffset(photo.getUploadTime(), anonymousPeriod);
    }

    @Override
    public PhotoActionAllowance getPhotoCommentAllowance(final Photo photo) {
        final PhotoActionAllowance allowance = photo.getCommentsAllowance();

        if (allowance == PhotoActionAllowance.CANDIDATES_AND_MEMBERS && !configurationService.getBoolean(ConfigurationKey.CANDIDATES_CAN_COMMENT_PHOTOS)) {
            return PhotoActionAllowance.MEMBERS_ONLY;
        }

        return allowance;
    }

    @Override
    public PhotoActionAllowance getPhotoVotingAllowance(final Photo photo) {
        final PhotoActionAllowance allowance = photo.getVotingAllowance();

        if (allowance == PhotoActionAllowance.CANDIDATES_AND_MEMBERS && !configurationService.getBoolean(ConfigurationKey.CANDIDATES_CAN_VOTE_FOR_PHOTOS)) {
            return PhotoActionAllowance.MEMBERS_ONLY;
        }

        return allowance;
    }

    @Override
    public boolean exists(final int entryId) {
        return photoDao.exists(entryId);
    }

    @Override
    public boolean exists(final Photo entry) {
        return photoDao.exists(entry);
    }

    @Override
    public List<UserPhotosByGenre> getUserPhotosByGenres(final int userId) {
        final Set<Genre> genres = getUserPhotoGenres(userId);
        final List<UserPhotosByGenre> userPhotosByGenres = newArrayList();
        for (final Genre genre : genres) {
            final UserPhotosByGenre photosByGenre = new UserPhotosByGenre(genre);
            final int photoQtyByUserAndGenre = getPhotosCountByUserAndGenre(userId, genre.getId());
            photosByGenre.setPhotosQty(photoQtyByUserAndGenre);

            userPhotosByGenres.add(photosByGenre);
        }

        Collections.sort(userPhotosByGenres, new Comparator<UserPhotosByGenre>() {
            @Override
            public int compare(final UserPhotosByGenre o1, final UserPhotosByGenre o2) {
                return o1.getGenre().getName().compareTo(o2.getGenre().getName());
            }
        });

        return userPhotosByGenres;
    }

    @Override
    public List<Integer> getLastVotedPhotosIds(final User user, final int photosQty, final User accessor) {
        // TODO: write test!!!! IS NOT CHECKED!!!
        final SqlIdsSelectQuery selectQuery = new PhotoListQueryBuilder(dateUtilsService).filterByVotedUser(user).sortByVotingTimeDesc().forPage(1, photosQty).getQuery();

        return load(selectQuery).getIds();
    }

    @Override
    public boolean movePhotoToGenreWithNotification(final int photoId, final int genreId, final User userWhoIsMoving) {
        final Genre genre = genreService.load(genreId);

        if (genre == null) {
            return false;
        }

        final Photo photo = load(photoId);

		/*if ( ! securityService.userCanEditPhoto( userWhoIsMoving, photo ) ) {
			return false;
		}*/

        photo.setGenreId(genreId);

        if (!securityService.userOwnThePhoto(userWhoIsMoving, photoId)) { // TODO: assertSuperAdminAccess?

            final User photoAuthor = userService.load(photo.getUserId());

            final TranslatableMessage translatableMessage = new TranslatableMessage("$1 is moved your photo '$2' to genre '$3'", services).userCardLink(userWhoIsMoving).addPhotoCardLinkParameter(photo).addPhotosByGenreLinkParameter(genre);

            privateMessageService.sendSystemNotificationMessage(photoAuthor, translatableMessage.build(photoAuthor.getLanguage()));
        }

        return save(photo);
    }

    @Override
    public int getLastUserPhotoId(final int userId) {
        return photoDao.getLastUserPhotoId(userId);
    }

    @Override
    public boolean isUserPhotoImported(final int userId, final int importId) {
        return photoDao.isUserPhotoImported(userId, importId);
    }

    private void createPhotoDBEntry(final Photo photo, final File photoImageFile, final File preview, final PhotoTeam photoTeam, final List<UserPhotoAlbum> photoAlbums) throws SaveToDBException {

        photo.setFileSize(photoImageFile.length());
        photo.setPhotoPreviewName(preview.getName());

        try {
            photo.setImageDimension(imageFileUtilsService.getImageDimension(photoImageFile));
        } catch (IOException e) {
            throw new BaseRuntimeException(String.format("Can not get image dimension: '%s'", photoImageFile.getAbsolutePath()));
        }

        if (!save(photo)) {
            throw new SaveToDBException(String.format("Can not save photo: %s", photo));
        }

        if (!userTeamService.savePhotoTeam(photoTeam)) {
            delete(photo.getId());
            throw new SaveToDBException(String.format("Can not save photo team: %s", photoTeam));
        }

        if (!userPhotoAlbumService.savePhotoAlbums(photo, photoAlbums)) {
            delete(photo.getId());
            throw new SaveToDBException(String.format("Can not save photo albums: %s", photoAlbums));
        }
    }

    private PagingModel getPagingModel(final int photosQty) {
        final PagingModel pagingModel = new PagingModel(services);
        pagingModel.setCurrentPage(1);
        pagingModel.setItemsOnPage(photosQty);

        return pagingModel;
    }

    private void expireUserPhotosCountByGenreCache(final int userId) {
        for (final Genre genre : genreService.loadAll()) {
            cacheService.expire(CacheKey.USER_GENRE_PHOTOS_QTY, new UserGenreCompositeKey(userId, genre.getId()));
        }
    }
}
