package com.bordozer.jphoto.core.general.user;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.photo.PhotoVotingCategory;

import java.util.Date;

public class UserPhotoVote {

    private final User user;
    private final Photo photo;
    private final PhotoVotingCategory photoVotingCategory;
    private int mark;
    private int maxAccessibleMark;
    private Date votingTime;

    public UserPhotoVote(final User user, final Photo photo, final PhotoVotingCategory photoVotingCategory) {
        this.user = user;
        this.photo = photo;
        this.photoVotingCategory = photoVotingCategory;
    }

    public User getUser() {
        return user;
    }

    public Photo getPhoto() {
        return photo;
    }

    public PhotoVotingCategory getPhotoVotingCategory() {
        return photoVotingCategory;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(final int mark) {
        this.mark = mark;
    }

    public int getMaxAccessibleMark() {
        return maxAccessibleMark;
    }

    public void setMaxAccessibleMark(final int maxAccessibleMark) {
        this.maxAccessibleMark = maxAccessibleMark;
    }

    public Date getVotingTime() {
        return votingTime;
    }

    public void setVotingTime(final Date votingTime) {
        this.votingTime = votingTime;
    }

    @Override
    public String toString() {
        return String.format("%s: %s ( %s )", user, photoVotingCategory, mark);
    }
}
