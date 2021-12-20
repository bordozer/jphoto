package com.bordozer.jphoto.core.general.photoTeam;

import com.bordozer.jphoto.core.general.photo.Photo;

import java.util.List;

public class PhotoTeam {

    private final Photo photo;
    private final List<PhotoTeamMember> photoTeamMembers;

    public PhotoTeam(final Photo photo, final List<PhotoTeamMember> photoTeamMembers) {
        this.photo = photo;
        this.photoTeamMembers = photoTeamMembers;
    }

    public Photo getPhoto() {
        return photo;
    }

    public List<PhotoTeamMember> getPhotoTeamMembers() {
        return photoTeamMembers;
    }
}
