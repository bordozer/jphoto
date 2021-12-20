package com.bordozer.jphoto.core.general.user;

import com.bordozer.jphoto.core.interfaces.Cacheable;

public class UserGenreRank implements Cacheable {

    private final int userId;
    private final int genreId;
    private int userRankInGenre;

    public UserGenreRank(final int userId, final int genreId) {
        this.userId = userId;
        this.genreId = genreId;
    }

    public int getUserId() {
        return userId;
    }

    public int getGenreId() {
        return genreId;
    }

    public int getUserRankInGenre() {
        return userRankInGenre;
    }

    public void setUserRankInGenre(final int userRankInGenre) {
        this.userRankInGenre = userRankInGenre;
    }
}
