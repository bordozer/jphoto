package com.bordozer.jphoto.core.general.user;

import com.bordozer.jphoto.core.general.genre.Genre;

import java.util.Date;

public class UserGenreRankHistoryEntry {

    private final User user;
    private final Genre genre;
    private final int rank;
    private final Date assignTime;

    public UserGenreRankHistoryEntry(final User user, final Genre genre, final int rank, final Date assignTime) {
        this.user = user;
        this.genre = genre;
        this.rank = rank;
        this.assignTime = assignTime;
    }

    public User getUser() {
        return user;
    }

    public Genre getGenre() {
        return genre;
    }

    public int getRank() {
        return rank;
    }

    public Date getAssignTime() {
        return assignTime;
    }
}
