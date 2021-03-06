package com.bordozer.jphoto.core.services.photo;

import com.bordozer.jphoto.core.general.data.TimeRange;
import com.bordozer.jphoto.core.general.data.UserRating;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.UserPhotoVote;
import com.bordozer.jphoto.ui.controllers.users.card.MarksByCategoryInfo;

import java.util.Date;
import java.util.List;

public interface PhotoVotingService {

    // Transactional
    boolean saveUserPhotoVoting(final User user, final Photo photo, final Date votingTime, final List<UserPhotoVote> userPhotoVotes);

    void deletePhotoVoting(final int photoId);

    boolean isUserVotedForPhoto(final User user, final Photo photo);

    List<UserPhotoVote> getUserVotesForPhoto(final User user, final Photo photo);

    List<UserPhotoVote> getPhotoVotes(final Photo photo);

    List<UserPhotoVote> getUserVotes(final User user);

    List<MarksByCategoryInfo> getPhotoSummaryVoicesByPhotoCategories(final Photo photo);

    List<MarksByCategoryInfo> getUserSummaryVoicesByPhotoCategories(final User user);

    int getSummaryPhotoMark(final Photo photo);

    int getPhotoMarksForPeriod(final int photoId, final Date timeFrom, final Date timeTo);

    List<UserRating> getUserRatingForPeriod(final Date timeFrom, final Date timeTo, final int limit);

    TimeRange getTopBestDateRange();

    TimeRange getPortalPageBestDateRange();
}
