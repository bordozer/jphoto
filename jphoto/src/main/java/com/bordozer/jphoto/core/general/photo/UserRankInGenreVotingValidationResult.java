package com.bordozer.jphoto.core.general.photo;

public class UserRankInGenreVotingValidationResult extends ValidationResult {

    private boolean uiVotingIsInaccessible;

    public boolean isUiVotingIsInaccessible() {
        return uiVotingIsInaccessible;
    }

    public void setUiVotingIsInaccessible(final boolean uiVotingIsInaccessible) {
        this.uiVotingIsInaccessible = uiVotingIsInaccessible;
    }
}
