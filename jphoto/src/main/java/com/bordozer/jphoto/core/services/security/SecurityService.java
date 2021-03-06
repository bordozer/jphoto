package com.bordozer.jphoto.core.services.security;

import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.photo.PhotoComment;
import com.bordozer.jphoto.core.general.photo.UserRankInGenreVotingValidationResult;
import com.bordozer.jphoto.core.general.photo.ValidationResult;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.translator.Language;
import com.bordozer.jphoto.ui.dtos.AnonymousSettingsDTO;

import java.util.Date;
import java.util.List;

public interface SecurityService {

    String BEAN_NAME = "securityService";

    boolean userCanEditPhoto(final User user, final Photo photo);

    boolean userCanDeletePhoto(final User user, final Photo photo);

    boolean userCanVoteForPhoto(final User user, final Photo photo);

    boolean userCanSeeUserRankVoteHistory(final User user, final User userWhoIsSeeing);

    boolean userOwnThePhoto(final User user, final int photoId);

    boolean userOwnThePhoto(final User user, final Photo photo);

    boolean userOwnThePhotoComment(User user, PhotoComment photoComment);

    boolean userCanEditPhotoComment(final User user, final PhotoComment photoComment);

    boolean userCanDeletePhotoComment(final int userId, final int commentId);

    boolean userCanDeletePhotoComment(final User user, final PhotoComment photoComment);

    boolean userCanEditUserData(final User user, final User dataOwnerUser);

    void assertUserExists(final String _userId);

    void assertPhotoExists(final String _photoId);

    void assertPhotoFileExists(final Photo photo);

    void assertUserCanEditPhoto(final User user, final Photo photo);

    void assertUserEqualsToCurrentUser(final int userId);

    void assertUserEqualsToCurrentUser(final User user);

    void assertUserCanSeeUserRankVoteHistory(final User user, final User userWhoIsSeeing);

    void assertCurrentUserIsLogged(final String message);

    void assertUserCanDeletePhoto(final User user, final Photo photo);

    void assertUserCanDeletePhotoComment(final int userId, final int commentId);

    void assertUserCanEditUserData(final User user, final User dataOwnerUser);

    void assertGenreExists(final String _genreId);

    ValidationResult validateUserCanCommentPhoto(final User user, final Photo photo, final Date time, final Language language);

    ValidationResult validateUserCanVoteForPhoto(final User user, final Photo photo, final Date time, final Language language);

    UserRankInGenreVotingValidationResult getUserRankInGenreVotingValidationResult(final User user, final User voter, final Genre genre, final Date time, final Language language);

    boolean isSuperAdminUser(final int userId);

    boolean isSuperAdminUser(final User user);

    List<User> getSuperAdminUsers();

    void assertSuperAdminAccess(final User currentUser);

    boolean isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod(final PhotoComment photoComment, final User accessor);

    boolean isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod(final PhotoComment photoComment, final User accessor, final Date onTime);

    boolean isPhotoAuthorNameMustBeHidden(final Photo photo, final User accessor);

    boolean isPhotoAuthorNameMustBeHidden(final Photo photo, final User accessor, final Date onTime);

    boolean isPhotoWithingAnonymousPeriod(Photo photo);

    boolean isPhotoWithingAnonymousPeriod(Photo photo, Date onTime);

    boolean forceAnonymousPosting(final int userId, final int genreId, final Date time);

    AnonymousSettingsDTO forceAnonymousPostingAjax(final int userId, final int genreId, final Language language);
}
