package core.services.user;

import core.general.photoTeam.PhotoTeam;
import core.general.user.userTeam.UserTeam;
import core.general.user.userTeam.UserTeamMember;
import core.interfaces.AllEntriesByIdLoadable;
import core.interfaces.BaseEntityService;
import core.interfaces.IdsSqlSelectable;

public interface UserTeamService extends BaseEntityService<UserTeamMember>, IdsSqlSelectable, AllEntriesByIdLoadable<UserTeamMember> {

	String BEAN_NAME = "userTeamService";

	UserTeam loadUserTeam( final int userId );

	UserTeamMember loadUserTeamMemberByName( final int userId, final String name );

	int getTeamMemberPhotosQty( final int userTeamMemberId );

	boolean savePhotoTeam( final PhotoTeam photoTeam );

	PhotoTeam getPhotoTeam( final int photoId );

	void deletePhotoTeam( final int photoId );

	boolean isTeamMemberAssignedToPhoto( final int photoId, final int teamMemberId );
}
