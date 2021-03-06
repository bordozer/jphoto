package com.bordozer.jphoto.core.services.dao;

import com.bordozer.jphoto.core.general.data.PhotoMarksForPeriod;
import com.bordozer.jphoto.core.general.data.UserRating;
import com.bordozer.jphoto.core.general.genre.GenreVotingCategories;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.photo.PhotoVotingCategory;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.UserPhotoVote;
import com.bordozer.jphoto.ui.controllers.users.card.MarksByCategoryInfo;

import java.util.Date;
import java.util.List;

public interface PhotoVotingDao extends BaseEntityDao<PhotoVotingCategory> {

    List<PhotoVotingCategory> loadAll();

    PhotoVotingCategory loadByName(final String name);

    GenreVotingCategories getGenreVotingCategories(final int genreId);

    boolean saveUserPhotoVoting(final User user, final Photo photo, final List<UserPhotoVote> userPhotoVotes);

    void deletePhotoVotes(final int photoId);

    void deletePhotoVotesSummary(final int photoId);

    boolean isUserVotedForPhoto(final User user, final Photo photo);

    List<UserPhotoVote> getUserVotesForPhoto(User user, Photo photo);

    List<UserPhotoVote> getPhotoVotes(final Photo photo);

    List<UserPhotoVote> getUserVotes(final User user);

    List<MarksByCategoryInfo> getUserSummaryVoicesByPhotoCategories(final Photo photo);

    List<MarksByCategoryInfo> getUserSummaryVoicesByPhotoCategories(final User user);

    void updatePhotoSummaryMarksByVotingCategories(final int photoId, final List<UserPhotoVote> userPhotoVotes);

    int getPhotoMarksForPeriod(final int photoId, final Date timeFrom, final Date timeTo);

    List<UserRating> getUserRatingForPeriod(final Date timeFrom, final Date timeTo, int limit);

    List<PhotoMarksForPeriod> getSummaryPhotoVotingForPeriodSortedBySummaryMarkDesc(final Date timeFrom, final Date timeTo);
}
