package com.bordozer.jphoto.core.services.dao;

import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.UsersSecurity;

import java.util.Date;

public interface UsersSecurityDao {

    boolean createEntry(final int userId, final String cryptPassword);

    boolean save(final UsersSecurity usersSecurity);

    UsersSecurity load(final User user);

    void delete(final int userId);

    Date getLastUserActivityTime(final int userId);

    void saveLastUserActivityTime(final int userId, final Date time);

    void changeUserPassword(final int userId, final String cryptPassword);
}
