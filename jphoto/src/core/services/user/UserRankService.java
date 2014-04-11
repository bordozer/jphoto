package core.services.user;

import core.general.configuration.SystemConfiguration;
import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.user.User;
import core.general.user.UserGenreRankHistoryEntry;
import core.general.user.UserRankInGenreVoting;
import core.general.user.UserRankPhotoVote;
import ui.controllers.users.genreRank.VotingModel;
import ui.userRankIcons.UserRankIconContainer;

import java.util.List;
import java.util.Map;

public interface UserRankService {

	boolean saveVotingForUserRankInGenre( final UserRankInGenreVoting rankInGenreVoting, final User votingUser );

	boolean isUserVotedLastTimeForThisRankInGenre( final int voterId, final int forUserId, final int genreId, final int rank );

	int getUserRankInGenre( final int userId, final int genreId );

	boolean saveUserRankForGenre( final int userId, final int genreId, final int rank );

	int setUserLastVotingResult( int voterId, int userId, int genreId );

	VotingModel getVotingModel( final int userId, final int genreId, final User votingUser );

	int getUserRankInGenreVotingPoints( final int userId, final int genreId );

	int getUserVotePointsForRankInGenre( final int userId, final int genreId );

	List<UserRankPhotoVote> getUsersWhoVotedForUserRankInGenre( final int userId, final int genreId );

	List<UserGenreRankHistoryEntry> getUserGenreRankHistoryEntries( int userId, int genreId );

	int getUserHighestPositiveMarkInGenre( final int userId, final int genreId );

	int getUserLowestNegativeMarkInGenre( final int userId, final int genreId );

	Map<Integer, Integer> getUserGenreRankPointsMap();

	Map<Integer, Integer> getUserGenreRankPointsMap( final SystemConfiguration systemConfiguration );

	Map<Integer, Integer> getUserGenreRankPointsMap( final int basePoints, final float coefficient );

	int calculateUserRankInGenre( final int userId, final int genreId );

	int getVotePointsToGetNextRankInGenre( final int userCurrentVotePointsForRankInGenre );

	boolean isUserHavingEnoughPhotosInGenre( final int userId, final int genreId );

	UserRankIconContainer getUserRankIconContainer( final User user, final Genre genre );

	UserRankIconContainer getUserRankIconContainer( final User user, final Genre genre, final int rankInGenre );

	UserRankIconContainer getUserRankIconContainer( final User user, final Photo photo );
}
