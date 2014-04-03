package core.services.dao;

import ui.controllers.users.card.MarksByCategoryInfo;
import core.general.data.PhotoMarksForPeriod;
import core.general.data.UserRating;
import core.general.genre.GenreVotingCategories;
import core.general.photo.Photo;
import core.general.photo.PhotoVotingCategory;
import core.general.user.User;
import core.general.user.UserPhotoVote;

import java.util.Date;
import java.util.List;

public interface PhotoVotingDao extends BaseEntityDao<PhotoVotingCategory> {

	List<PhotoVotingCategory> loadAll();

	PhotoVotingCategory loadByName( final String name );

	GenreVotingCategories getGenreVotingCategories( final int genreId );

	boolean saveUserPhotoVoting( final User user, final Photo photo, final List<UserPhotoVote> userPhotoVotes );

	void deletePhotoVotes( final int photoId );

	void deletePhotoVotesSummary( final int photoId );

	boolean isUserVotedForPhoto( final User user, final Photo photo );

	List<UserPhotoVote> getUserVotesForPhoto( User user, Photo photo );

	List<UserPhotoVote> getPhotoVotes( final Photo photo );

	List<UserPhotoVote> getUserVotes( final User user );

	List<MarksByCategoryInfo> getUserSummaryVoicesByPhotoCategories( final Photo photo );

	List<MarksByCategoryInfo> getUserSummaryVoicesByPhotoCategories( final User user );

	void updatePhotoSummaryMarksByVotingCategories( final int photoId, final List<UserPhotoVote> userPhotoVotes );

	int getPhotoMarksForPeriod( final int photoId, final Date timeFrom, final Date timeTo );

	List<UserRating> getUserRatingForPeriod( final Date timeFrom, final Date timeTo, int limit );

	List<PhotoMarksForPeriod> getSummaryPhotoVotingForPeriodSortedBySummaryMarkDesc( final Date timeFrom, final Date timeTo );
}
