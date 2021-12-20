package com.bordozer.jphoto.core.general.photoTeam;

import com.bordozer.jphoto.core.general.user.userTeam.UserTeamMember;

public class PhotoTeamMember {

    private UserTeamMember userTeamMember;
    private String description;

    public UserTeamMember getUserTeamMember() {
        return userTeamMember;
    }

    public void setUserTeamMember(final UserTeamMember userTeamMember) {
        this.userTeamMember = userTeamMember;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("%s", userTeamMember);
    }

    @Override
    public int hashCode() {
        return 31 * userTeamMember.getId();
    }

    @Override
    public boolean equals(Object o) {

        if (o == null) {
            return false;
        }

        if (o == this) {
            return true;
        }

        if (getClass() != o.getClass()) {
            return false;
        }

        final PhotoTeamMember teamMember = (PhotoTeamMember) o;
        return teamMember.getUserTeamMember().equals(userTeamMember);
    }
}
