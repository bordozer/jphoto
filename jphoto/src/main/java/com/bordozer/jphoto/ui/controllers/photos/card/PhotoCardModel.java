package com.bordozer.jphoto.ui.controllers.photos.card;

import com.bordozer.jphoto.core.general.base.AbstractGeneralModel;
import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.img.Dimension;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.photo.PhotoInfo;
import com.bordozer.jphoto.core.general.photo.ValidationResult;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.UserPhotoVote;
import com.bordozer.jphoto.ui.controllers.users.genreRank.VotingModel;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenu;

import java.util.Date;
import java.util.List;

public class PhotoCardModel extends AbstractGeneralModel {

    private Photo photo;

    private User user;
    private Genre genre;

    private long commentDelay;
    private Date userNextCommentTime;
    private int usedDelayBetweenComments;

    private List<UserPhotoVote> userPhotoVotes;
    private PhotoInfo photoInfo;

    private int minCommentLength;
    private int maxCommentLength;

    private int votingUserMinAccessibleMarkForGenre;
    private int votingUserMaxAccessibleMarkForGenre;

    private ValidationResult commentingValidationResult;
    private ValidationResult votingValidationResult;

    private VotingModel votingModel;

    private EntryMenu entryMenu;

    private List<Integer> rootCommentsIds;

    private Dimension shownDimension;

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(final Genre genre) {
        this.genre = genre;
    }

    public long getCommentDelay() {
        return commentDelay;
    }

    public void setCommentDelay(final long commentDelay) {
        this.commentDelay = commentDelay;
    }

    public int getUsedDelayBetweenComments() {
        return usedDelayBetweenComments;
    }

    public void setUsedDelayBetweenComments(final int usedDelayBetweenComments) {
        this.usedDelayBetweenComments = usedDelayBetweenComments;
    }

    public Date getUserNextCommentTime() {
        return userNextCommentTime;
    }

    public void setUserNextCommentTime(final Date userNextCommentTime) {
        this.userNextCommentTime = userNextCommentTime;
    }

    public List<UserPhotoVote> getUserPhotoVotes() {
        return userPhotoVotes;
    }

    public void setUserPhotoVotes(final List<UserPhotoVote> userPhotoVotes) {
        this.userPhotoVotes = userPhotoVotes;
    }

    public PhotoInfo getPhotoInfo() {
        return photoInfo;
    }

    public void setPhotoInfo(final PhotoInfo photoInfo) {
        this.photoInfo = photoInfo;
    }

    public int getMinCommentLength() {
        return minCommentLength;
    }

    public void setMinCommentLength(final int minCommentLength) {
        this.minCommentLength = minCommentLength;
    }

    public int getMaxCommentLength() {
        return maxCommentLength;
    }

    public void setMaxCommentLength(final int maxCommentLength) {
        this.maxCommentLength = maxCommentLength;
    }

    public int getVotingUserMinAccessibleMarkForGenre() {
        return votingUserMinAccessibleMarkForGenre;
    }

    public void setVotingUserMinAccessibleMarkForGenre(final int votingUserMinAccessibleMarkForGenre) {
        this.votingUserMinAccessibleMarkForGenre = votingUserMinAccessibleMarkForGenre;
    }

    public int getVotingUserMaxAccessibleMarkForGenre() {
        return votingUserMaxAccessibleMarkForGenre;
    }

    public void setVotingUserMaxAccessibleMarkForGenre(final int votingUserMaxAccessibleMarkForGenre) {
        this.votingUserMaxAccessibleMarkForGenre = votingUserMaxAccessibleMarkForGenre;
    }

    public ValidationResult getCommentingValidationResult() {
        return commentingValidationResult;
    }

    public void setCommentingValidationResult(final ValidationResult commentingValidationResult) {
        this.commentingValidationResult = commentingValidationResult;
    }

    public ValidationResult getVotingValidationResult() {
        return votingValidationResult;
    }

    public void setVotingValidationResult(final ValidationResult votingValidationResult) {
        this.votingValidationResult = votingValidationResult;
    }

    public VotingModel getVotingModel() {
        return votingModel;
    }

    public void setVotingModel(final VotingModel votingModel) {
        this.votingModel = votingModel;
    }

    public EntryMenu getEntryMenu() {
        return entryMenu;
    }

    public void setEntryMenu(final EntryMenu entryMenu) {
        this.entryMenu = entryMenu;
    }

    public List<Integer> getRootCommentsIds() {
        return rootCommentsIds;
    }

    public void setRootCommentsIds(final List<Integer> rootCommentsIds) {
        this.rootCommentsIds = rootCommentsIds;
    }

    public Dimension getShownDimension() {
        return shownDimension;
    }

    public void setShownDimension(final Dimension shownDimension) {
        this.shownDimension = shownDimension;
    }

    @Override
    public void clear() {
        super.clear();
        photo = null;
    }
}
