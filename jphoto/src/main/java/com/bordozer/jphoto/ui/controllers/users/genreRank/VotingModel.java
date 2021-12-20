package com.bordozer.jphoto.ui.controllers.users.genreRank;

import com.bordozer.jphoto.core.general.photo.UserRankInGenreVotingValidationResult;

public class VotingModel {

    private int userRankInGenre;
    private int loggedUserVotingPoints;
    private boolean userAlreadyVoted;
    private int lastVotingPoints;

    private boolean userHasEnoughPhotosInGenre;

    private UserRankInGenreVotingValidationResult validationResult;

    public int getUserRankInGenre() {
        return userRankInGenre;
    }

    public void setUserRankInGenre(final int userRankInGenre) {
        this.userRankInGenre = userRankInGenre;
    }

    public int getLoggedUserVotingPoints() {
        return loggedUserVotingPoints;
    }

    public void setLoggedUserVotingPoints(final int loggedUserVotingPoints) {
        this.loggedUserVotingPoints = loggedUserVotingPoints;
    }

    public boolean isUserAlreadyVoted() {
        return userAlreadyVoted;
    }

    public void setUserAlreadyVoted(final boolean userAlreadyVoted) {
        this.userAlreadyVoted = userAlreadyVoted;
    }

    public int getLastVotingPoints() {
        return lastVotingPoints;
    }

    public void setLastVotingPoints(final int lastVotingPoints) {
        this.lastVotingPoints = lastVotingPoints;
    }

    public boolean isUserHasEnoughPhotosInGenre() {
        return userHasEnoughPhotosInGenre;
    }

    public void setUserHasEnoughPhotosInGenre(final boolean userHasEnoughPhotosInGenre) {
        this.userHasEnoughPhotosInGenre = userHasEnoughPhotosInGenre;
    }

    public UserRankInGenreVotingValidationResult getValidationResult() {
        return validationResult;
    }

    public void setValidationResult(final UserRankInGenreVotingValidationResult validationResult) {
        this.validationResult = validationResult;
    }
}
