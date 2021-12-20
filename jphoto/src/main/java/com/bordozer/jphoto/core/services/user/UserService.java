package com.bordozer.jphoto.core.services.user;

import com.bordozer.jphoto.core.enums.PhotoActionAllowance;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.UserAvatar;
import com.bordozer.jphoto.core.general.user.UserStatus;
import com.bordozer.jphoto.core.interfaces.AllEntriesLoadable;
import com.bordozer.jphoto.core.interfaces.BaseEntityService;
import com.bordozer.jphoto.core.interfaces.IdsSqlSelectable;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.sql.SqlSelectResult;
import com.bordozer.jphoto.sql.builder.SqlIdsSelectQuery;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface UserService extends BaseEntityService<User>, AllEntriesLoadable<User>, IdsSqlSelectable {

    String BEAN_NAME = "userService";

    String ANONYMOUS_USER_NAME = "ANONYMOUS_USER_NAME";

    SqlSelectResult<User> loadByIds(final SqlIdsSelectQuery selectIdsQuery);

    // Transactional
    boolean createUser(User user, String password);

    User loadByName(final String name);

    User loadByLogin(final String userLogin);

    User loadByEmail(final String userEmail);

    boolean saveAvatar(final int userId, final File file) throws IOException;

    boolean deleteAvatar(final int userId) throws IOException;

    UserAvatar getUserAvatar(final int userId);

    boolean setUserStatus(final int userId, final UserStatus userStatus);

    int getUserCount();

    List<User> searchByPartOfName(final String searchString);

    PhotoActionAllowance getUserPhotoCommentAllowance(final User user);

    PhotoActionAllowance getUserPhotoVotingAllowance(final User user);

    User getNotLoggedTemporaryUser();

    User getNotLoggedTemporaryUser(final Language language);

    String getAnonymousUserName(final Language language);
}
