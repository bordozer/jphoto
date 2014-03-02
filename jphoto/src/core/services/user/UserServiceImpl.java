package core.services.user;

import core.enums.PhotoActionAllowance;
import core.general.cache.CacheEntryFactory;
import core.general.cache.CacheKey;
import core.general.configuration.ConfigurationKey;
import core.general.user.User;
import core.general.user.UserAvatar;
import core.general.user.UserStatus;
import core.services.dao.UserDao;
import core.services.entry.ActivityStreamService;
import core.services.system.CacheService;
import core.services.system.ConfigurationService;
import core.services.utils.UserPhotoFilePathUtilsService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sql.SqlSelectIdsResult;
import sql.SqlSelectResult;
import sql.builder.SqlIdsSelectQuery;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

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

	@Override
	public SqlSelectIdsResult load( final SqlIdsSelectQuery selectIdsQuery ) {
		return userDao.load( selectIdsQuery );
	}

	@Override
	public SqlSelectResult<User> loadByIds( final SqlIdsSelectQuery selectIdsQuery ) {
		final SqlSelectIdsResult idsResult = userDao.load( selectIdsQuery );

		final List<User> users = newArrayList();

		for ( final int userId : idsResult.getIds() ) {
			users.add( userDao.load( userId ) );
		}

		final SqlSelectResult<User> selectResult = new SqlSelectResult<User>();
		selectResult.setItems( users );
		selectResult.setRecordQty( idsResult.getRecordQty() );

		return selectResult;
	}

	@Override
	public boolean save( final User entry ) {
		return userDao.saveToDB( entry );
	}

	@Override
	public boolean createUser( final User user, final String password ) {
		if( ! userDao.saveToDB( user ) ) {
			return false;
		}

		usersSecurityService.createEntry( user.getId(), password );

		activityStreamService.saveUserRegistration( user );

		return true;
	}

	@Override
	public User load( final int userId ) {
		return userDao.load( userId );
	}

	@Override
	public User loadByName( final String name ) {
		return userDao.loadByName( name );
	}

	@Override
	public User loadByLogin( final String userLogin ) {
		return userDao.loadByLogin( userLogin );
	}

	@Override
	public User loadByEmail( final String userEmail ) {
		return userDao.loadByEmail( userEmail );
	}

	@Override
	public boolean saveAvatar( final int userId, final File file ) throws IOException {
		if ( ! file.exists() ) {
			throw new IOException( String.format( "Avatar file '%s' does not exist", file.getPath() ) );
		}

		final String userAvatarFileName = userPhotoFilePathUtilsService.getUserAvatarFileName( userId );
		final File destinationFile = new File( userPhotoFilePathUtilsService.getUserAvatarDir( userId ), userAvatarFileName );

		userPhotoFilePathUtilsService.createUserPhotoDirIfNeed( userId );

		FileUtils.copyFile( file, destinationFile );

		cacheService.expire( CacheKey.USER_AVATAR, userId );

		return false;
	}

	@Override
	public UserAvatar getUserAvatar( final int userId ) {
		return cacheService.getEntry( CacheKey.USER_AVATAR, userId, new CacheEntryFactory<UserAvatar>() {
			@Override
			public UserAvatar createEntry() {
				return loadUserAvatar( userId );
			}
		} );
	}

	@Override
	public boolean setUserMembership( final int userId, final UserStatus userStatus ) {
		return userDao.setUserMembership( userId, userStatus );
	}

	@Override
	public int getUserCount() {
		return userDao.getUserCount();
	}

	@Override
	public List<User> searchByPartOfName( final String searchString ) {
		return userDao.searchByPartOfName( searchString );
	}

	@Override
	public List<User> loadAll() {
		// is used for jobs and temporary staff
		return userDao.loadAll();
	}

	@Override
	public PhotoActionAllowance getUserPhotoCommentAllowance( final User user ) {
		final PhotoActionAllowance userCommentsAllowance = user.getDefaultPhotoCommentsAllowance();

		if ( userCommentsAllowance == PhotoActionAllowance.CANDIDATES_AND_MEMBERS && ! configurationService.getBoolean( ConfigurationKey.CANDIDATES_CAN_COMMENT_PHOTOS ) ) {
			return PhotoActionAllowance.MEMBERS_ONLY;
		}

		return userCommentsAllowance;
	}

	@Override
	public PhotoActionAllowance getUserPhotoVotingAllowance( final User user ) {
		final PhotoActionAllowance userVotingAllowance = user.getDefaultPhotoVotingAllowance();

		if ( userVotingAllowance == PhotoActionAllowance.CANDIDATES_AND_MEMBERS && ! configurationService.getBoolean( ConfigurationKey.CANDIDATES_CAN_VOTE_FOR_PHOTOS ) ) {
			return PhotoActionAllowance.MEMBERS_ONLY;
		}

		return userVotingAllowance;
	}

	@Override
	public boolean delete( final int entryId ) {
		return false;
	}

	@Override
	public boolean exists( final int entryId ) {
		return userDao.exists( entryId );
	}

	@Override
	public boolean exists( final User entry ) {
		return userDao.exists( entry );
	}

	private UserAvatar loadUserAvatar( final int userId ) {
		final UserAvatar userAvatar = new UserAvatar( userId );

		if ( userPhotoFilePathUtilsService.isUserHasAvatar( userId ) ) {
			userAvatar.setFile( userPhotoFilePathUtilsService.getUserAvatarFile( userId ) );
			userAvatar.setUserAvatarFileUrl( userPhotoFilePathUtilsService.getUserAvatarFileUrl( userId ) );
		}
		return userAvatar;
	}
}
