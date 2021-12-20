package com.bordozer.jphoto.ui.controllers.users.team.card;

import com.bordozer.jphoto.core.general.base.AbstractGeneralModel;
import com.bordozer.jphoto.core.general.user.userTeam.UserTeamMember;
import com.bordozer.jphoto.ui.elements.PhotoList;

import java.util.List;

public class UserTeamMemberCardModel extends AbstractGeneralModel {

    private UserTeamMember userTeamMember;
    private List<PhotoList> photoLists;

    public UserTeamMember getUserTeamMember() {
        return userTeamMember;
    }

    public void setUserTeamMember(final UserTeamMember userTeamMember) {
        this.userTeamMember = userTeamMember;
    }

    public List<PhotoList> getPhotoLists() {
        return photoLists;
    }

    public void setPhotoLists(final List<PhotoList> photoLists) {
        this.photoLists = photoLists;
    }
}
