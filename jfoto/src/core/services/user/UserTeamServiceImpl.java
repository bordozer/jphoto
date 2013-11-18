package core.services.user;

import core.dtos.UserPickerDTO;
import core.general.user.User;
import core.general.user.userTeam.UserTeam;
import core.general.user.userTeam.UserTeamMember;
import core.general.photoTeam.PhotoTeam;
import core.services.dao.UserTeamMemberDao;
import org.springframework.beans.factory.annotation.Autowired;
import sql.SqlSelectIdsResult;
import sql.builder.SqlIdsSelectQuery;
import core.services.utils.EntityLinkUtilsService;
import utils.StringUtilities;
import core.services.utils.UserPhotoFilePathUtilsService;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class UserTeamServiceImpl implements UserTeamService {

	@Autowired
	private UserService userService;

	@Autowired
	private UserTeamMemberDao userTeamMemberDao;

	@Autowired
	private UserPhotoFilePathUtilsService userPhotoFilePathUtilsService;

	@Autowired
	private EntityLinkUtilsService entityLinkUtilsService;

	@Override
	public boolean save( final UserTeamMember entry ) {
		return userTeamMemberDao.saveToDB( entry );
	}

	@Override
	public boolean delete( final int entryId ) {
		return userTeamMemberDao.delete( entryId ); // TODO: how to be with photos which have this team member?
	}

	@Override
	public UserTeamMember load( final int id ) {
		return userTeamMemberDao.load( id );
	}

	@Override
	public SqlSelectIdsResult load( final SqlIdsSelectQuery selectIdsQuery ) {
		return userTeamMemberDao.load( selectIdsQuery );
	}

	@Override
	public UserTeam loadUserTeam( final int userId ) {
		return new UserTeam( userService.load( userId ), loadAllForEntry( userId ) );
	}

	@Override
	public List<UserTeamMember> loadAllForEntry( final int userId ) {
		final List<Integer> membersIds = userTeamMemberDao.loadUserTeamMembersIds( userId );

		final List<UserTeamMember> members = newArrayList();

		for ( final int membersId : membersIds ) {
			members.add( userTeamMemberDao.load( membersId ) );
		}

		return members;
	}

	@Override
	public UserTeamMember loadUserTeamMemberByName( final int userId, final String name ) {
		return userTeamMemberDao.loadUserTeamMemberByName( userId, name );
	}

	@Override
	public int getTeamMemberPhotosQty( final int userTeamMemberId ) {
		return userTeamMemberDao.getTeamMemberPhotosQty( userTeamMemberId );
	}

	@Override
	public List<UserPickerDTO> userLinkAjax( final String searchString ) {

		/*final int userId = NumberUtils.convertToInt( searchString );
		if ( userId > 0 ) {
			final User user = userService.load( userId );
			if ( user == null ) {
				return newArrayList();
			}
			return newArrayList( EntityLinkUtilsServiceImpl.getUserCardLink( user ) );
		}*/

		final List<User> users = userService.searchByPartOfName( searchString );

		final List<UserPickerDTO> userPickerDTOs = newArrayList();

		if ( users.size() == 0 ) {
			return newArrayList();
		}

		for ( final User user : users ) {
			final UserPickerDTO userPickerDTO = new UserPickerDTO();

			userPickerDTO.setUserId( String.valueOf( user.getId() ) );
			userPickerDTO.setUserName( user.getName() );
			userPickerDTO.setUserNameEscaped( StringUtilities.escapeHtml( user.getName() ) );
			userPickerDTO.setUserCardLink( entityLinkUtilsService.getUserCardLink( user ) );
			userPickerDTO.setUserAvatarUrl( userPhotoFilePathUtilsService.getUserAvatarFileUrl( user.getId() ) );
			userPickerDTO.setUserGender( user.getGender().getNameTranslated() );

			userPickerDTOs.add( userPickerDTO );
		}

		return userPickerDTOs;
	}

	@Override
	public boolean savePhotoTeam( final PhotoTeam photoTeam ) {
		return userTeamMemberDao.savePhotoTeam( photoTeam );
	}

	@Override
	public PhotoTeam getPhotoTeam( final int photoId ) {
		return userTeamMemberDao.getPhotoTeam( photoId );
	}

	@Override
	public void deletePhotoTeam( final int photoId ) {
		userTeamMemberDao.deletePhotoTeam( photoId );
	}

	@Override
	public boolean exists( final int entryId ) {
		return userTeamMemberDao.exists( entryId );
	}

	@Override
	public boolean exists( final UserTeamMember entry ) {
		return userTeamMemberDao.exists( entry );
	}

	@Override
	public boolean isTeamMemberAssignedToPhoto( final int photoId, final int teamMemberId ) {
		return userTeamMemberDao.isTeamMemberAssignedToPhoto( photoId, teamMemberId );
	}
}
