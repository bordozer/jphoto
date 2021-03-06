package com.bordozer.jphoto.core.services.dao;

import com.bordozer.jphoto.core.general.user.UserGenreRankHistoryEntry;
import com.bordozer.jphoto.core.general.user.UserRankInGenreVoting;
import com.bordozer.jphoto.core.general.user.UserRankPhotoVote;

import java.util.List;

public interface UserRankDao {

    boolean saveVotingForUserRankInGenre(final UserRankInGenreVoting rankInGenreVoting);

    boolean isUserVotedLastTimeForThisRankInGenre(final int voterId, final int forUserId, final int genreId, final int rank);

    int getUserRankInGenre(final int userId, final int genreId);

    boolean saveUserRankForGenre(final int userId, final int genreId, final int rank);

    int setUserLastVotingResult(final int voterId, final int userId, final int genreId);

    public int getUserVotePointsForRankInGenre(final int userId, final int genreId);

    List<UserRankPhotoVote> getUsersIdsWhoVotedForUserRankInGenre(int userId, int genreId);

    List<UserGenreRankHistoryEntry> getUserGenreRankHistoryEntries(int userId, int genreId);
}
