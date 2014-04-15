package core.services.entry;

import core.enums.FavoriteEntryType;
import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.general.photo.PhotoPreview;
import core.general.user.User;
import core.general.user.UserPhotoVote;
import core.general.user.UserRankInGenreVoting;
import core.general.user.UserStatus;
import core.interfaces.BaseEntityService;
import core.interfaces.IdsSqlSelectable;
import core.services.system.Services;
import ui.activity.AbstractActivityStreamEntry;
import ui.activity.ActivityType;

import java.util.Date;
import java.util.List;

public interface ActivityStreamService extends BaseEntityService<AbstractActivityStreamEntry>, IdsSqlSelectable {

	boolean saveUserRegistration( final User user );

	boolean savePhotoUpload( final Photo photo );

	boolean savePhotoVoting( final User voter, final Photo photo, final List<UserPhotoVote> userPhotoVotes, final Date currentTime );

	boolean savePhotoComment( final PhotoComment comment );

	boolean savePhotoPreview( final PhotoPreview preview );

	boolean saveFavoriteAction( final int userId, final int favoriteEntryId, final Date time, final FavoriteEntryType entryType );

	boolean saveVotingForUserRankInGenre( final UserRankInGenreVoting rankInGenreVoting );

	boolean saveUserStatusChange( User user, UserStatus oldStatus, UserStatus newStatus, Date activityTime, Services services );

	boolean saveUserRankInGenreChanged( User user, Genre genre, int oldRank, int newRank, Date activityTime, Services services );

	List<AbstractActivityStreamEntry> getActivityForPeriod( final Date dateFrom, final Date dateTo );

	List<AbstractActivityStreamEntry> getLastActivities( final int qty );

	List<AbstractActivityStreamEntry> getUserActivities( final int userId );

	List<AbstractActivityStreamEntry> getUserLastActivities( final int userId, final int qty );

	void deleteEntriesOlderThen( final List<ActivityType> activityTypes, final Date timeFrame );
}
