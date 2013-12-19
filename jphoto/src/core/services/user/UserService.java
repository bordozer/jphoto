package core.services.user;

import core.enums.PhotoActionAllowance;
import core.general.user.User;
import core.general.user.UserAvatar;
import core.general.user.UserStatus;
import core.interfaces.AllEntriesLoadable;
import core.interfaces.BaseEntityService;
import core.interfaces.IdsSqlSelectable;
import sql.SqlSelectResult;
import sql.builder.SqlIdsSelectQuery;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface UserService extends BaseEntityService<User>, AllEntriesLoadable<User>, IdsSqlSelectable {

	String BEAN_NAME = "userService";

	SqlSelectResult<User> loadByIds( final SqlIdsSelectQuery selectIdsQuery );

	// Transactional
	boolean createUser( User user, String password );

	User loadByName( final String name );

	User loadByLogin( final String userLogin );

	User loadByEmail( final String userEmail );

	boolean saveAvatar( final int userId, final File file ) throws IOException;

	UserAvatar getUserAvatar( final int userId );

	boolean setUserMembership( final int userId, final UserStatus userStatus );

	int getUserCount();

	List<User> searchByPartOfName( final String searchString );

	PhotoActionAllowance getUserPhotoCommentAllowance( final User user );

	PhotoActionAllowance getUserPhotoVotingAllowance( final User user );
}
