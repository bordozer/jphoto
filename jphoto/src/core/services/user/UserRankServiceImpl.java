package core.services.user;

import controllers.users.genreRank.VotingModel;
import core.exceptions.BaseRuntimeException;
import core.general.cache.CacheEntryFactory;
import core.general.cache.CacheKey;
import core.general.cache.entries.RankInGenrePointsEntry;
import core.general.configuration.Configuration;
import core.general.configuration.ConfigurationKey;
import core.general.configuration.SystemConfiguration;
import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.user.*;
import core.services.dao.UserRankDao;
import core.services.entry.ActivityStreamService;
import core.services.entry.GenreService;
import core.services.photo.PhotoService;
import core.services.security.SecurityService;
import core.services.system.CacheService;
import core.services.system.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import ui.userRankIcons.UserRankIconContainer;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newLinkedHashMap;

public class UserRankServiceImpl implements UserRankService {

	@Autowired
	private UserRankDao userRankDao;

	@Autowired
	private UserService userService;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private CacheService<RankInGenrePointsEntry> cacheService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private GenreService genreService;

	@Autowired
	private ActivityStreamService activityStreamService;

	@Override
	public boolean saveVotingForUserRankInGenre( final UserRankInGenreVoting rankInGenreVoting, final User votingUser ) {
		if ( rankInGenreVoting.getPoints() == 0 ) {
			throw new BaseRuntimeException( "ZERO Voting For User Rank In Genre" );
		}

		final boolean isSaved = userRankDao.saveVotingForUserRankInGenre( rankInGenreVoting );

		final Photo votedFromPhoto = rankInGenreVoting.getPhoto();
		if ( isSaved && ( votedFromPhoto == null || ! securityService.isPhotoAuthorNameMustBeHidden( votedFromPhoto, votingUser ) ) ) {
			activityStreamService.saveVotingForUserRankInGenre( rankInGenreVoting );
		}

		return isSaved;
	}

	@Override
	public boolean isUserVotedLastTimeForThisRankInGenre( final int voterId, final int forUserId, final int genreId, final int rank ) {
		return userRankDao.isUserVotedLastTimeForThisRankInGenre( voterId, forUserId, genreId, rank );
	}

	@Override
	public int getUserRankInGenre( final int userId, final int genreId ) {
		return userRankDao.getUserRankInGenre( userId, genreId );
	}

	@Override
	public boolean saveUserRankForGenre( final int userId, final int genreId, final int rank ) {
		return userRankDao.saveUserRankForGenre( userId, genreId, rank );
	}

	@Override
	public int setUserLastVotingResult( final int voterId, final int userId, final int genreId ) {
		return userRankDao.setUserLastVotingResult( voterId, userId, genreId );
	}

	@Override
	public int getUserRankInGenreVotingPoints( final int userId, final int genreId ) {
		final int userRankInGenre = getUserRankInGenre( userId, genreId );
		return userRankInGenre > 0 ? userRankInGenre : 1;
	}

	@Override
	public int getUserVotePointsForRankInGenre( final int userId, final int genreId ) {
		return userRankDao.getUserVotePointsForRankInGenre( userId, genreId );
	}

	@Override
	public List<UserRankPhotoVote> getUsersWhoVotedForUserRankInGenre( final int userId, final int genreId ) {
		return userRankDao.getUsersIdsWhoVotedForUserRankInGenre( userId, genreId );
	}

	@Override
	public List<UserGenreRankHistoryEntry> getUserGenreRankHistoryEntries( final int userId, final int genreId ) {
		return userRankDao.getUserGenreRankHistoryEntries( userId, genreId );
	}

	@Override
	public VotingModel getVotingModel( final int userId, final int genreId, final User voter ) {
		final int voterId = voter.getId();

		final int userCurrentRankInGenre = getUserRankInGenre( userId, genreId );

		final int loggedUserRankInGenreVotingPoints = getUserRankInGenreVotingPoints( voterId, genreId );

		final VotingModel votingModel = new VotingModel();
		votingModel.setLoggedUserVotingPoints( loggedUserRankInGenreVotingPoints );
		votingModel.setUserRankInGenre( userCurrentRankInGenre );

		final boolean userAlreadyVoted = isUserVotedLastTimeForThisRankInGenre( voterId, userId, genreId, userCurrentRankInGenre );
		votingModel.setUserAlreadyVoted( userAlreadyVoted );

		final User user = userService.load( userId );
		final Genre genre = genreService.load( genreId );
		votingModel.setValidationResult( securityService.getUserRankInGenreVotingValidationResult( user, voter, genre ) );

		if ( userAlreadyVoted ) {
			votingModel.setLastVotingPoints( setUserLastVotingResult( voterId, userId, genreId ) );
		}

		votingModel.setUserHasEnoughPhotosInGenre( isUserHavingEnoughPhotosInGenre( userId, genreId ) );

		return votingModel;
	}

	@Override
	public boolean isUserHavingEnoughPhotosInGenre( final int userId, final int genreId ) {
		final int minPhotosQtyForGenreRankVoting = configurationService.getInt( ConfigurationKey.RANK_VOTING_MIN_PHOTOS_QTY_IN_GENRE );
		final int userPhotosInGenre = photoService.getPhotoQtyByUserAndGenre( userId, genreId );
		return userPhotosInGenre >= minPhotosQtyForGenreRankVoting;
	}

	@Override
	public int getUserHighestPositiveMarkInGenre( final int userId, final int genreId ) {
		if ( userId == User.NOT_LOGGED_USER.getId() ) {
			return 0;
		}

		final int userRankInGenre = getUserRankInGenre( userId, genreId );

		if ( userRankInGenre < 0 ) {
			return 0;
		}

		final User user = userService.load( userId );

		if ( user.getUserStatus() == UserStatus.CANDIDATE ) {
			return configurationService.getInt( ConfigurationKey.CANDIDATES_PHOTO_VOTING_HIGHEST_MARK );
		}

		final int minPositiveMark = configurationService.getInt( ConfigurationKey.PHOTO_VOTING_LOWEST_POSITIVE_MARK );
		if ( userRankInGenre < minPositiveMark ) {
			return minPositiveMark;
		}

		final int maxMark = configurationService.getInt( ConfigurationKey.PHOTO_VOTING_HIGHEST_POSITIVE_MARK );

		return userRankInGenre < maxMark ? userRankInGenre : maxMark;
	}

	@Override
	public int getUserLowestNegativeMarkInGenre( final int userId, final int genreId ) {

		if ( userId == User.NOT_LOGGED_USER.getId() ) {
			return 0;
		}

		final int userRankInGenre = getUserRankInGenre( userId, genreId ); // positive

		if ( userRankInGenre < 0 ) {
			return 0;
		}

		final User user = userService.load( userId );

		if ( user.getUserStatus() == UserStatus.CANDIDATE ) {
			return configurationService.getInt( ConfigurationKey.CANDIDATES_PHOTO_VOTING_LOWEST_MARK );
		}

		final int minNegativeMark = configurationService.getInt( ConfigurationKey.PHOTO_VOTING_HIGHEST_NEGATIVE_MARK ); // negative
		if ( -userRankInGenre > minNegativeMark ) {
			return minNegativeMark;
		}

		final int maxNegativeMark = configurationService.getInt( ConfigurationKey.PHOTO_VOTING_LOWEST_NEGATIVE_MARK );

		return -userRankInGenre < maxNegativeMark ? maxNegativeMark : -userRankInGenre;
	}

	@Override
	public Map<Integer, Integer> getUserGenreRankPointsMap() {
		final int base = configurationService.getInt( ConfigurationKey.RANK_VOTING_POINTS_BASE_STEP );
		final float coefficient = configurationService.getFloat( ConfigurationKey.RANK_VOTING_POINTS_COEFFICIENT );

		return getUserGenreRankPointsMap( base, coefficient );
	}

	@Override
	public Map<Integer, Integer> getUserGenreRankPointsMap( final SystemConfiguration systemConfiguration ) {
		final Configuration basePointsConfig = systemConfiguration.getConfiguration( ConfigurationKey.RANK_VOTING_POINTS_BASE_STEP );
		final Configuration coeffConfig = systemConfiguration.getConfiguration( ConfigurationKey.RANK_VOTING_POINTS_COEFFICIENT );

		return getUserGenreRankPointsMap( basePointsConfig.getValueInt(), coeffConfig.getValueFloat() );
	}

	@Override
	public Map<Integer, Integer> getUserGenreRankPointsMap( final int basePoints, final float coefficient ) {
		final Map<Integer, Integer> ranksPoints = newLinkedHashMap();

		final int maxRankInGenre = configurationService.getInt( ConfigurationKey.RANK_VOTING_MAX_GENRE_RANK );

		for ( int rank = 1; rank <= maxRankInGenre; rank++ ) {
			ranksPoints.put( rank, getRankUpperPoints( basePoints, coefficient, rank ) );
		}

		return ranksPoints;
	}

	@Override
	public int calculateUserRankInGenre( final int userId, final int genreId ) {
		final Map<Integer, Integer> ranksPoints = getUserGenreRankPointsMap();

		final int userPointsForRankInGenre = getUserVotePointsForRankInGenre( userId, genreId );

		if ( userPointsForRankInGenre == 0 ) {
			return 0;
		}

		final int absPoints = Math.abs( userPointsForRankInGenre );
		final int sign = userPointsForRankInGenre > 0 ? 1 : -1;
		int previousRank = 0;

		for ( final int rank : ranksPoints.keySet() ) {
			final int rankPoints = ranksPoints.get( rank );

			if ( rankPoints == absPoints ) {
				return rank * sign;
			}

			if ( rankPoints > absPoints ) {
				return previousRank * sign;
			}
			previousRank = rank;
		}
		return 0;
	}

	@Override
	public int getVotePointsToGetNextRankInGenre( final int userCurrentVotePointsForRankInGenre ) {
		final Map<Integer, Integer> userGenreRankPointsMap = getUserGenreRankPointsMap();

		for ( final int rank : userGenreRankPointsMap.keySet() ) {
			final int points = userGenreRankPointsMap.get( rank );
			if ( userCurrentVotePointsForRankInGenre < points  ) {
				return points;
			}
		}

		return 0;
	}

	@Override
	public UserRankIconContainer getUserRankIconContainer( final User user, final Genre genre ) {
		// current user rank in genre current
		return getUserRankIconContainer( user, genre, getUserRankInGenre( user.getId(), genre.getId() ) );
	}

	@Override
	public UserRankIconContainer getUserRankIconContainer( final User user, final Genre genre, final int rankInGenre ) {
		// current user rank in genre custom
		return new UserRankIconContainer( user, genre, rankInGenre, this, configurationService );
	}

	@Override
	public UserRankIconContainer getUserRankIconContainer( final User user, final Photo photo ) {
		// user rank in genre when photo was uploaded
		return new UserRankIconContainer( user, photo, this, configurationService, genreService );
	}

	private int getRankUpperPoints( final int base, final float coefficient, final int rank ) {

		return cacheService.getEntry( CacheKey.RANK_IN_GENRE_POINTS_CACHE_ENTRY, rank, new CacheEntryFactory<RankInGenrePointsEntry>() {
			@Override
			public RankInGenrePointsEntry createEntry() {
				return loadRankUpperPoints( base, coefficient, rank );
			}
		} ).getUpperPoints();
	}

	private RankInGenrePointsEntry loadRankUpperPoints( final int base, final float coefficient, final int rank ) {
		final float k = coefficient + 1;

		int i = 1;
		final int firstRankPoints = configurationService.getInt( ConfigurationKey.RANK_VOTING_FIRST_RANK_POINTS );
		int previousLevelUpperPoints = firstRankPoints;
		int levelUpperPoints = firstRankPoints;

		while ( i < rank ) {
			levelUpperPoints = Math.round( previousLevelUpperPoints * k ) + base;
			previousLevelUpperPoints = levelUpperPoints;
			i++;
		}

		return new RankInGenrePointsEntry( rank, levelUpperPoints );
	}

	public void setUserRankDao( final UserRankDao userRankDao ) {
		this.userRankDao = userRankDao;
	}

	public void setUserService( final UserService userService ) {
		this.userService = userService;
	}

	public void setConfigurationService( final ConfigurationService configurationService ) {
		this.configurationService = configurationService;
	}

	public void setCacheService( final CacheService<RankInGenrePointsEntry> cacheService ) {
		this.cacheService = cacheService;
	}

	public void setPhotoService( final PhotoService photoService ) {
		this.photoService = photoService;
	}

	public void setSecurityService( final SecurityService securityService ) {
		this.securityService = securityService;
	}

	public void setGenreService( final GenreService genreService ) {
		this.genreService = genreService;
	}

	public void setActivityStreamService( final ActivityStreamService activityStreamService ) {
		this.activityStreamService = activityStreamService;
	}
}
