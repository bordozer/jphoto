package com.bordozer.jphoto.core.services.photo;

import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.photo.PhotoComment;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.dao.PhotoCommentDao;
import com.bordozer.jphoto.core.services.entry.ActivityStreamService;
import com.bordozer.jphoto.core.services.entry.GenreService;
import com.bordozer.jphoto.core.services.notification.NotificationService;
import com.bordozer.jphoto.core.services.security.SecurityService;
import com.bordozer.jphoto.core.services.system.ConfigurationService;
import com.bordozer.jphoto.core.services.user.UserRankService;
import com.bordozer.jphoto.core.services.user.UserService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.ui.controllers.comment.edit.PhotoCommentInfo;
import com.bordozer.jphoto.ui.services.menu.entry.EntryMenuService;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenu;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenuData;
import com.bordozer.jphoto.utils.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

@Service("photoCommentService")
public class PhotoCommentServiceImpl implements PhotoCommentService {

    private final Map<Integer, Date> usersLastCommentTime = newHashMap();

    @Autowired
    private PhotoCommentDao photoCommentDao;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private UserRankService userRankService;

    @Autowired
    private UserService userService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private PhotoVotingService photoVotingService;

    @Autowired
    private EntryMenuService entryMenuService;

    @Autowired
    private DateUtilsService dateUtilsService;

    @Autowired
    private ActivityStreamService activityStreamService;

    @Override
    public boolean save(final PhotoComment entry) {

        final boolean isNew = entry.isNew();

        final boolean isSaved = getPhotoCommentDao().saveToDB(entry);

        if (!isSaved) {
            return false;
        }
        saveLastUserCommentTime(entry.getCommentAuthor());

        if (isNew) {
            new Thread() {
                @Override
                public void run() {
                    notificationService.newCommentNotification(entry);
                }
            }.start();
            activityStreamService.savePhotoComment(entry);
        }

        return true;
    }

    @Override
    public PhotoComment load(final int commentId) {
        return getPhotoCommentDao().load(commentId);
    }

    @Override
    public List<PhotoComment> loadAll(final int photoId) {
        return loadByIds(getPhotoCommentDao().loadAllIds(photoId));
    }

    @Override
    public List<Integer> loadRootCommentsIds(final int photoId) {
        return getPhotoCommentDao().loadRootCommentsIds(photoId);
    }

    @Override
    public List<PhotoComment> loadCommentsWithoutParent(final int photoId) {
        return loadByIds(getPhotoCommentDao().loadRootCommentsIds(photoId));
    }

    @Override
    public List<PhotoComment> loadAnswersOnComment(final int commentId) {
        return loadByIds(getPhotoCommentDao().loadAnswersOnCommentIds(commentId));
    }

    @Override
    public void setCommentReadTime(final int commentId, final Date time) {
        getPhotoCommentDao().setCommentReadTime(commentId, time);
    }

    @Override
    public int getUnreadCommentsQty(final int userId) {
        return getPhotoCommentDao().getUnreadCommentsQty(userId);
    }

    @Override
    public List<Integer> loadUserCommentsIds(final int userId) {
        return getPhotoCommentDao().loadUserCommentsIds(userId);
    }

    @Override
    public List<Integer> loadCommentsToUserPhotosIds(final int userId) {
        return getPhotoCommentDao().loadCommentsToUserPhotosIds(userId);
    }

    @Override
    public List<Integer> loadUnreadCommentsToUserIds(final int userId) {
        return getPhotoCommentDao().loadUnreadCommentsToUserIds(userId);
    }

    private List<PhotoComment> loadByIds(final List<Integer> ids) {
        final List<PhotoComment> comments = newArrayList();
        for (final Integer commentId : ids) {
            comments.add(load(commentId));
        }
        return comments;
    }

    @Override
    public boolean delete(final int entryId) {
        return getPhotoCommentDao().delete(entryId);
    }

    @Override
    public int getPhotoCommentsCount(final int photoId) {
        return getPhotoCommentDao().getPhotoCommentsCount(photoId);
    }

    @Override
    public int getPhotoCommentsCount() {
        return getPhotoCommentDao().getPhotoCommentsCount();
    }

    private void saveLastUserCommentTime(final User commentAuthor) {

        if (securityService.isSuperAdminUser(commentAuthor.getId())) {
            return; // Does not matter if Super Admin votes before
        }

        synchronized (usersLastCommentTime) {
            usersLastCommentTime.put(commentAuthor.getId(), getCurrentTime());
        }
    }

    @Override
    public Date getUserLastCommentTime(final int userId) {
        if (!usersLastCommentTime.containsKey(userId)) {
            return dateUtilsService.getEmptyTime();
        }
        return usersLastCommentTime.get(userId);
    }

    @Override
    public Date getUserNextCommentTime(final int userId) {
        if (dateUtilsService.isEmptyTime(getUserLastCommentTime(userId))) {
            return getCurrentTime();
        }
        final User load = userService.load(userId);
        final int commentDelayMils = getUserDelayBetweenCommentsSec(load) * 1000;
        return new Date(getUserLastCommentTime(userId).getTime() + commentDelayMils);
    }

    @Override
    public int getUserDelayBetweenCommentsSec(final User user) {

        if (securityService.isSuperAdminUser(user.getId())) {
            return 0;
        }

        return configurationService.getInt(ConfigurationKey.COMMENTS_DELAY_AFTER_COMMENT_SEC);
    }

    @Override
    public long getUserDelayToNextComment(final int userId) {
        if (!usersLastCommentTime.containsKey(userId)) {
            return 0;
        }
        long commentDelay = getUserNextCommentTime(userId).getTime() - getCurrentTime().getTime();
        commentDelay = (long) (NumberUtils.round(commentDelay / 1000, 0) * 1000);

        return commentDelay;
    }

    @Override
    public boolean isUserCanCommentPhotos(final int userId) {
        return getUserDelayToNextComment(userId) <= 0;
    }

    @Override
    public PhotoCommentInfo getPhotoCommentInfo(final PhotoComment photoComment, final User accessor) {
        return getPhotoCommentInfo(photoComment, entryMenuService.getCommentFullMenuItems(), accessor);
    }

    @Override
    public PhotoCommentInfo getPhotoCommentInfo(final PhotoComment photoComment, final List<EntryMenuData> entryMenuDataList, final User accessor) {

        final Photo photo = photoService.load(photoComment.getPhotoId());

        final PhotoCommentInfo photoCommentInfo = new PhotoCommentInfo(photo, photoComment);
        final User photoAuthor = userService.load(photo.getUserId());
        photoCommentInfo.setPhotoAuthor(photoAuthor);

        final int commentAuthorId = photoComment.getCommentAuthor().getId();
        photoCommentInfo.setCommentAuthorAvatar(userService.getUserAvatar(commentAuthorId));

        final int genreId = photo.getGenreId();
        photoCommentInfo.setCommentAuthorRankInGenre(userRankService.getUserRankInGenre(commentAuthorId, genreId));
        final Genre genre = genreService.load(genreId);
        photoCommentInfo.setGenre(genre);

        final int minPhotosQtyForGenreRankVoting = configurationService.getInt(ConfigurationKey.RANK_VOTING_MIN_PHOTOS_QTY_IN_GENRE);
        final int userPhotosInGenre = photoService.getPhotosCountByUserAndGenre(commentAuthorId, genreId);

        photoCommentInfo.setUserHasEnoughPhotosInGenre(userPhotosInGenre >= minPhotosQtyForGenreRankVoting);

        photoCommentInfo.setCommentAuthorVotes(photoVotingService.getUserVotesForPhoto(photoComment.getCommentAuthor(), photo));

        final EntryMenu entryMenu = entryMenuService.getCommentMenu(photoComment, accessor, entryMenuDataList);
        photoCommentInfo.setEntryMenu(entryMenu);

        photoCommentInfo.setAuthorNameMustBeHidden(securityService.isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod(photoComment, accessor));

        photoCommentInfo.setUserRankIconContainer(userRankService.getUserRankIconContainer(photoComment.getCommentAuthor(), genre));

        return photoCommentInfo;
    }

    @Override
    public PhotoCommentInfo getPhotoCommentInfoWithChild(final PhotoComment photoComment, final User accessor) {
        return getPhotoCommentInfoWithChild(photoComment, entryMenuService.getCommentFullMenuItems(), accessor);
    }

    @Override
    public PhotoCommentInfo getPhotoCommentInfoWithChild(final PhotoComment photoComment, final List<EntryMenuData> entryMenuDataList, final User accessor) {
        final PhotoCommentInfo photoCommentInfo = getPhotoCommentInfo(photoComment, entryMenuDataList, accessor);

        photoCommentInfo.setChildrenComments(loadChildrenPhotoComments(photoComment, entryMenuDataList, accessor));

        return photoCommentInfo;
    }

    @Override
    public void deletePhotoComments(final int photoId) {
        getPhotoCommentDao().deletePhotoComments(photoId);
    }

    @Override
    public void markAllUnreadCommentAsRead(final int userId) {
        getPhotoCommentDao().markAllUnreadCommentAsRead(userId);
    }

    @Override
    public boolean exists(final int entryId) {
        return getPhotoCommentDao().exists(entryId);
    }

    @Override
    public boolean exists(final PhotoComment entry) {
        return getPhotoCommentDao().exists(entry);
    }

    private List<PhotoCommentInfo> loadChildrenPhotoComments(final PhotoComment parentComment, final List<EntryMenuData> entryMenuDataList, final User accessor) {
        final List<PhotoComment> children = loadAnswersOnComment(parentComment.getId());
        final List<PhotoCommentInfo> commentInfos = newArrayList();

        for (final PhotoComment child : children) {
            final PhotoCommentInfo photoCommentInfo = getPhotoCommentInfo(child, entryMenuDataList, accessor);
            photoCommentInfo.setChildrenComments(loadChildrenPhotoComments(child, entryMenuDataList, accessor));

            commentInfos.add(photoCommentInfo);
        }

        return commentInfos;
    }

    protected PhotoCommentDao getPhotoCommentDao() {
        return photoCommentDao;
    }

    private Date getCurrentTime() {
        return dateUtilsService.getCurrentTime();
    }
}
