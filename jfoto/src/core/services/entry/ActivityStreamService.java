package core.services.entry;

import core.enums.FavoriteEntryType;
import core.general.activity.AbstractActivityStreamEntry;
import core.general.favorite.FavoriteEntry;
import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.general.photo.PhotoPreview;
import core.general.user.User;
import core.general.user.UserRankInGenreVoting;
import core.interfaces.BaseEntityService;
import core.interfaces.IdsSqlSelectable;

import java.util.Date;
import java.util.List;

public interface ActivityStreamService extends BaseEntityService<AbstractActivityStreamEntry>, IdsSqlSelectable {

	boolean saveUserRegistration( final User user );

	boolean savePhotoUpload( final Photo photo );

	boolean savePhotoVoting( final User voter, final Photo photo, final Date currentTime );

	boolean savePhotoComment( final PhotoComment comment );

	boolean savePhotoPreview( final PhotoPreview preview );

	boolean saveFavoriteAction( final int userId, final int favoriteEntryId, final Date time, final FavoriteEntryType entryType );

	boolean saveVotingForUserRankInGenre( final UserRankInGenreVoting rankInGenreVoting );

	List<AbstractActivityStreamEntry> getActivityForPeriod( final Date dateFrom, final Date dateTo );

	List<AbstractActivityStreamEntry> getLastActivities( final int qty );

	List<AbstractActivityStreamEntry> getUserActivities( final int userId );

	List<AbstractActivityStreamEntry> getUserLastActivities( final int userId, final int qty );

	void deleteEntriesOlderThen( final Date timeFrame );
}
