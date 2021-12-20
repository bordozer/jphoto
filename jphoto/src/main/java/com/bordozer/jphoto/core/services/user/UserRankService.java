package com.bordozer.jphoto.core.services.user;

import com.bordozer.jphoto.core.general.configuration.SystemConfiguration;
import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.UserGenreRankHistoryEntry;
import com.bordozer.jphoto.core.general.user.UserRankInGenreVoting;
import com.bordozer.jphoto.core.general.user.UserRankPhotoVote;
import com.bordozer.jphoto.ui.controllers.users.genreRank.VotingModel;
import com.bordozer.jphoto.ui.userRankIcons.UserRankIconContainer;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface UserRankService {

    boolean saveVotingForUserRankInGenre(final UserRankInGenreVoting rankInGenreVoting, final User votingUser);

    boolean isUserVotedLastTimeForThisRankInGenre(final int voterId, final int forUserId, final int genreId, final int rank);

    int getUserRankInGenre(final int userId, final int genreId);

    boolean saveUserRankForGenre(final int userId, final int genreId, final int rank);

    int setUserLastVotingResult(int voterId, int userId, int genreId);

    VotingModel getVotingModel(final int userId, final int genreId, final User votingUser, final Date time);

    int getUserRankInGenreVotingPoints(final int userId, final int genreId);

    int getUserVotePointsForRankInGenre(final int userId, final int genreId);

    List<UserRankPhotoVote> getUsersWhoVotedForUserRankInGenre(final int userId, final int genreId);

    List<UserGenreRankHistoryEntry> getUserGenreRankHistoryEntries(int userId, int genreId);

    int getUserHighestPositiveMarkInGenre(final int userId, final int genreId);

    int getUserLowestNegativeMarkInGenre(final int userId, final int genreId);

    Map<Integer, Integer> getUserGenreRankPointsMap();

    Map<Integer, Integer> getUserGenreRankPointsMap(final SystemConfiguration systemConfiguration);

    Map<Integer, Integer> getUserGenreRankPointsMap(final int basePoints, final float coefficient);

    int calculateUserRankInGenre(final int userId, final int genreId);

    int getVotePointsToGetNextRankInGenre(final int userCurrentVotePointsForRankInGenre);

    boolean isUserHavingEnoughPhotosInGenre(final int userId, final int genreId);

    UserRankIconContainer getUserRankIconContainer(final User user, final Genre genre);

    UserRankIconContainer getUserRankIconContainer(final User user, final Genre genre, final int rankInGenre);

    UserRankIconContainer getUserRankIconContainer(final User user, final Photo photo);
}
