package com.bordozer.jphoto.core.services.entry;

import com.bordozer.jphoto.core.enums.FavoriteEntryType;
import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.photo.PhotoComment;
import com.bordozer.jphoto.core.general.photo.PhotoPreview;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.UserPhotoVote;
import com.bordozer.jphoto.core.general.user.UserRankInGenreVoting;
import com.bordozer.jphoto.core.general.user.UserStatus;
import com.bordozer.jphoto.core.interfaces.BaseEntityService;
import com.bordozer.jphoto.core.interfaces.IdsSqlSelectable;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.activity.AbstractActivityStreamEntry;
import com.bordozer.jphoto.ui.activity.ActivityType;

import java.util.Date;
import java.util.List;

public interface ActivityStreamService extends BaseEntityService<AbstractActivityStreamEntry>, IdsSqlSelectable {

    boolean saveUserRegistration(final User user);

    boolean savePhotoUpload(final Photo photo);

    boolean savePhotoVoting(final User voter, final Photo photo, final List<UserPhotoVote> userPhotoVotes, final Date currentTime);

    boolean savePhotoComment(final PhotoComment comment);

    boolean savePhotoPreview(final PhotoPreview preview);

    boolean saveFavoriteAction(final int userId, final int favoriteEntryId, final Date time, final FavoriteEntryType entryType);

    boolean saveVotingForUserRankInGenre(final UserRankInGenreVoting rankInGenreVoting);

    boolean saveUserStatusChange(User user, UserStatus oldStatus, UserStatus newStatus, Date activityTime, Services services);

    boolean saveUserRankInGenreChanged(User user, Genre genre, int oldRank, int newRank, Date activityTime, Services services);

    List<AbstractActivityStreamEntry> getActivityForPeriod(final Date dateFrom, final Date dateTo);

    List<AbstractActivityStreamEntry> getLastActivities(final int qty);

    List<AbstractActivityStreamEntry> getUserActivities(final int userId);

    List<AbstractActivityStreamEntry> getUserLastActivities(final int userId, final int qty);

    void deleteEntriesOlderThen(final List<ActivityType> activityTypes, final Date timeFrame);
}
