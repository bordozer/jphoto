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
import core.services.dao.ActivityStreamDao;
import core.services.system.Services;
import core.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import sql.SqlSelectIdsResult;
import sql.builder.SqlIdsSelectQuery;
import ui.activity.*;

import java.util.Date;
import java.util.List;

public class ActivityStreamServiceImpl implements ActivityStreamService {

	@Autowired
	private ActivityStreamDao activityStreamDao;

	@Autowired
	private UserService userService;

	@Autowired
	private Services services;

	@Override
	public boolean saveUserRegistration( final User user ) {
		return save( new ActivityUserRegistration( user, services ) );
	}

	@Override
	public boolean savePhotoUpload( final Photo photo ) {
		return save( new ActivityPhotoUpload( photo, services ) );
	}

	@Override
	public boolean savePhotoVoting( final User voter, final Photo photo, final List<UserPhotoVote> userPhotoVotes, final Date currentTime ) {
		return save( new ActivityPhotoVoting( voter, photo, userPhotoVotes, currentTime, services ) );
	}

	@Override
	public boolean savePhotoComment( final PhotoComment comment ) {
		return save( new ActivityPhotoComment( comment, services ) );
	}

	@Override
	public boolean savePhotoPreview( final PhotoPreview preview ) {
		return save( new ActivityPhotoPreview( preview, services ) );
	}

	@Override
	public boolean saveFavoriteAction( final int userId, final int favoriteEntryId, final Date time, final FavoriteEntryType entryType ) {
		return save( new ActivityFavoriteAction( userService.load( userId ), favoriteEntryId, time, entryType, services ) );
	}

	@Override
	public boolean saveVotingForUserRankInGenre( final UserRankInGenreVoting rankInGenreVoting ) {
		return save( new ActivityVotingForUserRankInGenre( rankInGenreVoting, services ) );
	}

	@Override
	public boolean saveUserStatusChange( final User user, final UserStatus oldStatus, final UserStatus newStatus, final Date activityTime, final Services services ) {
		return save( new ActivityUserStatusChange( user, oldStatus, newStatus, activityTime, services ) );
	}

	@Override
	public boolean saveUserRankInGenreChanged( final User user, final Genre genre, final int oldRank, final int newRank, final Date activityTime, final Services services ) {
		return save( new ActivityUserRankInGenreChanged( user, genre, oldRank, newRank, activityTime, services ) );
	}

	@Override
	public boolean save( final AbstractActivityStreamEntry entry ) {
		return activityStreamDao.saveToDB( entry );
	}

	@Override
	public AbstractActivityStreamEntry load( final int id ) {
		return activityStreamDao.load( id );
	}

	@Override
	public boolean delete( final int entryId ) {
		return activityStreamDao.delete( entryId );
	}

	@Override
	public boolean exists( final int entryId ) {
		return activityStreamDao.exists( entryId );
	}

	@Override
	public boolean exists( final AbstractActivityStreamEntry entry ) {
		return activityStreamDao.exists( entry );
	}

	@Override
	public SqlSelectIdsResult load( final SqlIdsSelectQuery selectIdsQuery ) {
		return activityStreamDao.load( selectIdsQuery );
	}

	@Override
	public List<AbstractActivityStreamEntry> getActivityForPeriod( final Date dateFrom, final Date dateTo ) {
		return activityStreamDao.getActivityForPeriod( dateFrom, dateTo );
	}

	@Override
	public List<AbstractActivityStreamEntry> getLastActivities( final int qty ) {
		return activityStreamDao.getLastActivities( qty );
	}

	@Override
	public List<AbstractActivityStreamEntry> getUserActivities( final int userId ) {
		return activityStreamDao.getUserActivities( userId );
	}

	@Override
	public List<AbstractActivityStreamEntry> getUserLastActivities( final int userId, final int qty ) {
		return activityStreamDao.getUserLastActivities( userId, qty );
	}

	@Override
	public void deleteEntriesOlderThen( final Date timeFrame ) {
		activityStreamDao.deleteEntriesOlderThen( timeFrame );
	}
}
