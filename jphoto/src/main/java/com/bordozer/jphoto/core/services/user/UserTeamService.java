package com.bordozer.jphoto.core.services.user;

import com.bordozer.jphoto.core.general.photoTeam.PhotoTeam;
import com.bordozer.jphoto.core.general.user.userTeam.UserTeam;
import com.bordozer.jphoto.core.general.user.userTeam.UserTeamMember;
import com.bordozer.jphoto.core.interfaces.AllEntriesByIdLoadable;
import com.bordozer.jphoto.core.interfaces.BaseEntityService;
import com.bordozer.jphoto.core.interfaces.IdsSqlSelectable;

public interface UserTeamService extends BaseEntityService<UserTeamMember>, IdsSqlSelectable, AllEntriesByIdLoadable<UserTeamMember> {

    String BEAN_NAME = "userTeamService";

    UserTeam loadUserTeam(final int userId);

    UserTeamMember loadUserTeamMemberByName(final int userId, final String name);

    int getTeamMemberPhotosQty(final int userTeamMemberId);

    boolean savePhotoTeam(final PhotoTeam photoTeam);

    PhotoTeam getPhotoTeam(final int photoId);

    void deletePhotoTeam(final int photoId);

    boolean isTeamMemberAssignedToPhoto(final int photoId, final int teamMemberId);
}
