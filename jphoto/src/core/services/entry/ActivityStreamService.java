package core.services.entry;

import core.enums.FavoriteEntryType;
import core.general.activity.AbstractActivityStreamEntry;
import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.general.photo.PhotoPreview;
import core.general.user.User;
import core.general.user.UserPhotoVote;
import core.general.user.UserRankInGenreVoting;
import core.interfaces.BaseEntityService;
import core.interfaces.IdsSqlSelectable;

import java.util.Date;
import java.util.List;

public interface ActivityStreamService extends BaseEntityService<AbstractActivityStreamEntry>, IdsSqlSelectable {

	// TODO: Transactional
	boolean saveUserRegistration( final User user );

	// TODO: Transactional
	boolean savePhotoUpload( final Photo photo );

	// TODO: Transactional
	boolean savePhotoVoting( final User voter, final Photo photo, final List<UserPhotoVote> userPhotoVotes, final Date currentTime );

	// TODO: Transactional
	boolean savePhotoComment( final PhotoComment comment );

	// TODO: Transactional
	boolean savePhotoPreview( final PhotoPreview preview );

	// TODO: Transactional
	boolean saveFavoriteAction( final int userId, final int favoriteEntryId, final Date time, final FavoriteEntryType entryType );

	// TODO: Transactional
	boolean saveVotingForUserRankInGenre( final UserRankInGenreVoting rankInGenreVoting );

	List<AbstractActivityStreamEntry> getActivityForPeriod( final Date dateFrom, final Date dateTo );

	List<AbstractActivityStreamEntry> getLastActivities( final int qty );

	List<AbstractActivityStreamEntry> getUserActivities( final int userId );

	List<AbstractActivityStreamEntry> getUserLastActivities( final int userId, final int qty );

	void deleteEntriesOlderThen( final Date timeFrame );
}
