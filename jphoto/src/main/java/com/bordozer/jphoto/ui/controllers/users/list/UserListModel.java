package com.bordozer.jphoto.ui.controllers.users.list;

import com.bordozer.jphoto.core.general.base.AbstractGeneralModel;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.UserMembershipType;

import java.util.List;
import java.util.Map;

public class UserListModel extends AbstractGeneralModel {

    private List<User> userList;
    private List<UserMembershipType> userMembershipType;
    private Map<Integer, UserListData> userListDataMap;

    private String userListTitle;

    private UserMembershipType membershipType;
    private boolean showPaging = true;

    private boolean showEditIcons;

    public List<UserMembershipType> getUserMembershipType() {
        return userMembershipType;
    }

    public void setUserMembershipType(List<UserMembershipType> userMembershipType) {
        this.userMembershipType = userMembershipType;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public String getUserListTitle() {
        return userListTitle;
    }

    public void setUserListTitle(String userListTitle) {
        this.userListTitle = userListTitle;
    }

    public void setMembershipType(UserMembershipType membershipType) {
        this.membershipType = membershipType;
    }

    public UserMembershipType getMembershipType() {
        return membershipType;
    }

    public boolean isShowPaging() {
        return showPaging;
    }

    public void setShowPaging(final boolean showPaging) {
        this.showPaging = showPaging;
    }

    public boolean isShowEditIcons() {
        return showEditIcons;
    }

    public void setShowEditIcons(final boolean showEditIcons) {
        this.showEditIcons = showEditIcons;
    }

    public Map<Integer, UserListData> getUserListDataMap() {
        return userListDataMap;
    }

    public void setUserListDataMap(final Map<Integer, UserListData> userListDataMap) {
        this.userListDataMap = userListDataMap;
    }
}
