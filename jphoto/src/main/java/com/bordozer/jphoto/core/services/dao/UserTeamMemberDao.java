package com.bordozer.jphoto.core.services.dao;

import com.bordozer.jphoto.core.general.photoTeam.PhotoTeam;
import com.bordozer.jphoto.core.general.user.userTeam.UserTeamMember;

import java.util.List;

public interface UserTeamMemberDao extends BaseEntityDao<UserTeamMember> {

    List<Integer> loadUserTeamMembersIds(final int userId);

    UserTeamMember loadUserTeamMemberByName(final int userId, final String name);

    boolean savePhotoTeam(final PhotoTeam photoTeam);

    void deletePhotoTeam(final int photoId);

    PhotoTeam getPhotoTeam(final int photoId);

    int getTeamMemberPhotosQty(final int userTeamMemberId);

    boolean isTeamMemberAssignedToPhoto(final int photoId, final int teamMemberId);
}
