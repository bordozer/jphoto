package core.services.photo;

import core.general.data.UserRating;
import core.general.photo.Photo;
import core.general.user.User;
import core.general.user.UserPhotoVote;
import ui.controllers.users.card.MarksByCategoryInfo;

import java.util.Date;
import java.util.List;

public interface PhotoVotingService {

	// Transactional
	boolean saveUserPhotoVoting( final User user, final Photo photo, final Date votingTime, final List<UserPhotoVote> userPhotoVotes );

	void deletePhotoVoting( final int photoId );

	boolean isUserVotedForPhoto( final User user, final Photo photo );

	List<UserPhotoVote> getUserVotesForPhoto( final User user, final Photo photo );

	List<UserPhotoVote> getPhotoVotes( final Photo photo );

	List<UserPhotoVote> getUserVotes( final User user );

	List<MarksByCategoryInfo> getPhotoSummaryVoicesByPhotoCategories( final Photo photo );

	List<MarksByCategoryInfo> getUserSummaryVoicesByPhotoCategories( final User user );

	int getSummaryPhotoMark( final Photo photo );

	int getPhotoMarksForPeriod( final int photoId, final Date timeFrom, final Date timeTo );

	List<UserRating> getUserRatingForPeriod( final Date timeFrom, final Date timeTo, final int limit );
}
