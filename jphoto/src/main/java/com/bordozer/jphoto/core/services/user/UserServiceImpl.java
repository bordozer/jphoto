package com.bordozer.jphoto.core.services.user;

import com.bordozer.jphoto.core.enums.PhotoActionAllowance;
import com.bordozer.jphoto.core.general.cache.CacheEntryFactory;
import com.bordozer.jphoto.core.general.cache.CacheKey;
import com.bordozer.jphoto.core.general.configuration.ConfigurationKey;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.UserAvatar;
import com.bordozer.jphoto.core.general.user.UserStatus;
import com.bordozer.jphoto.core.services.dao.UserDao;
import com.bordozer.jphoto.core.services.entry.ActivityStreamService;
import com.bordozer.jphoto.core.services.system.CacheService;
import com.bordozer.jphoto.core.services.system.ConfigurationService;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.core.services.translator.TranslatorService;
import com.bordozer.jphoto.core.services.utils.DateUtilsService;
import com.bordozer.jphoto.core.services.utils.UserPhotoFilePathUtilsService;
import com.bordozer.jphoto.sql.SqlSelectIdsResult;
import com.bordozer.jphoto.sql.SqlSelectResult;
import com.bordozer.jphoto.sql.builder.SqlIdsSelectQuery;
import com.bordozer.jphoto.ui.services.security.UsersSecurityService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.collect.Lists.newArrayList;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private CacheService<UserAvatar> cacheService;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private UserPhotoFilePathUtilsService userPhotoFilePathUtilsService;

    @Autowired
    private UsersSecurityService usersSecurityService;

    @Autowired
    private ActivityStreamService activityStreamService;

    @Autowired
    private DateUtilsService dateUtilsService;

    @Autowired
    private TranslatorService translatorService;

    @Value("${app.language.default}")
    private String defaultLanguage;

    private final AtomicInteger sessionUserCounter = new AtomicInteger(1);

    @Override
    public SqlSelectIdsResult load(final SqlIdsSelectQuery selectIdsQuery) {
        return userDao.load(selectIdsQuery);
    }

    @Override
    public SqlSelectResult<User> loadByIds(final SqlIdsSelectQuery selectIdsQuery) {
        final SqlSelectIdsResult idsResult = userDao.load(selectIdsQuery);

        final List<User> users = newArrayList();

        for (final int userId : idsResult.getIds()) {
            users.add(userDao.load(userId));
        }

        final SqlSelectResult<User> selectResult = new SqlSelectResult<User>();
        selectResult.setItems(users);
        selectResult.setRecordQty(idsResult.getRecordQty());

        return selectResult;
    }

    @Override
    public boolean save(final User entry) {
        return userDao.saveToDB(entry);
    }

    @Override
    public boolean createUser(final User user, final String password) {
        if (!userDao.saveToDB(user)) {
            return false;
        }

        usersSecurityService.createEntry(user.getId(), password);

        activityStreamService.saveUserRegistration(user);

        return true;
    }

    @Override
    public User load(final int userId) {
        return userDao.load(userId);
    }

    @Override
    public User loadByName(final String name) {
        return userDao.loadByName(name);
    }

    @Override
    public User loadByLogin(final String userLogin) {
        return userDao.loadByLogin(userLogin);
    }

    @Override
    public User loadByEmail(final String userEmail) {
        return userDao.loadByEmail(userEmail);
    }

    @Override
    public boolean saveAvatar(final int userId, final File file) throws IOException {
        if (!file.exists()) {
            throw new IOException(String.format("Avatar file '%s' does not exist", file.getPath()));
        }

        final String userAvatarFileName = userPhotoFilePathUtilsService.getUserAvatarFileName(userId);
        final File destinationFile = new File(userPhotoFilePathUtilsService.getUserAvatarDir(userId), userAvatarFileName);

        userPhotoFilePathUtilsService.createUserPhotoDirIfNeed(userId);

        FileUtils.copyFile(file, destinationFile);

        cacheService.expire(CacheKey.USER_AVATAR, userId);

        return false;
    }

    @Override
    public boolean deleteAvatar(final int userId) throws IOException {

        final File userAvatarFile = userPhotoFilePathUtilsService.getUserAvatarFile(userId);

        if (userAvatarFile.exists()) {
            final boolean isDeleted = FileUtils.deleteQuietly(userAvatarFile);

            if (isDeleted) {
                cacheService.expire(CacheKey.USER_AVATAR, userId);
                return true;
            }
        }

        return true;
    }

    @Override
    public UserAvatar getUserAvatar(final int userId) {
        return cacheService.getEntry(CacheKey.USER_AVATAR, userId, new CacheEntryFactory<UserAvatar>() {
            @Override
            public UserAvatar createEntry() {
                return loadUserAvatar(userId);
            }
        });
    }

    @Override
    public boolean setUserStatus(final int userId, final UserStatus userStatus) {
        return userDao.setUserStatus(userId, userStatus);
    }

    @Override
    public int getUserCount() {
        return userDao.getUserCount();
    }

    @Override
    public List<User> searchByPartOfName(final String searchString) {
        return userDao.searchByPartOfName(searchString);
    }

    @Override
    public List<User> loadAll() {
        // is used for jobs and temporary staff
        return userDao.loadAll();
    }

    @Override
    public PhotoActionAllowance getUserPhotoCommentAllowance(final User user) {
        final PhotoActionAllowance userCommentsAllowance = user.getDefaultPhotoCommentsAllowance();

        if (userCommentsAllowance == PhotoActionAllowance.CANDIDATES_AND_MEMBERS && !configurationService.getBoolean(ConfigurationKey.CANDIDATES_CAN_COMMENT_PHOTOS)) {
            return PhotoActionAllowance.MEMBERS_ONLY;
        }

        return userCommentsAllowance;
    }

    @Override
    public PhotoActionAllowance getUserPhotoVotingAllowance(final User user) {
        final PhotoActionAllowance userVotingAllowance = user.getDefaultPhotoVotingAllowance();

        if (userVotingAllowance == PhotoActionAllowance.CANDIDATES_AND_MEMBERS && !configurationService.getBoolean(ConfigurationKey.CANDIDATES_CAN_VOTE_FOR_PHOTOS)) {
            return PhotoActionAllowance.MEMBERS_ONLY;
        }

        return userVotingAllowance;
    }

    @Override
    public boolean delete(final int entryId) {
        return false;
    }

    @Override
    public boolean exists(final int entryId) {
        return userDao.exists(entryId);
    }

    @Override
    public boolean exists(final User entry) {
        return userDao.exists(entry);
    }

    @Override
    public User getNotLoggedTemporaryUser() {
        return getNotLoggedTemporaryUser(Language.valueOf(defaultLanguage));
    }

    @Override
    public User getNotLoggedTemporaryUser(final Language language) {
        final User user = new User(-getSessionUserUniqueId());

        user.setLanguage(language);
        user.setRegistrationTime(dateUtilsService.getCurrentTime());
        user.setName("NOT LOGGED USER");
        user.setPhotosOnPage(configurationService.getInt(ConfigurationKey.PHOTO_LIST_PHOTOS_ON_PAGE));

        return user;
    }

    @Override
    public String getAnonymousUserName(final Language language) {
        return translatorService.translate(ANONYMOUS_USER_NAME, language);
    }

    private int getSessionUserUniqueId() {
        synchronized (sessionUserCounter) {
            return sessionUserCounter.getAndIncrement();
        }
    }

    private UserAvatar loadUserAvatar(final int userId) {
        final UserAvatar userAvatar = new UserAvatar(userId);

        if (userPhotoFilePathUtilsService.isUserHasAvatar(userId)) {
            userAvatar.setFile(userPhotoFilePathUtilsService.getUserAvatarFile(userId));
            userAvatar.setUserAvatarFileUrl(userPhotoFilePathUtilsService.getUserAvatarFileUrl(userId));
        }
        return userAvatar;
    }
}
