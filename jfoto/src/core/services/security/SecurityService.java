package core.services.security;

import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.general.photo.UserRankInGenreVotingValidationResult;
import core.general.user.User;
import core.general.photo.ValidationResult;

public interface SecurityService {

	String BEAN_NAME = "securityService";

	boolean userCanEditPhoto( final User user, final Photo photo );

	boolean userCanDeletePhoto( final User user, final Photo photo );

	boolean userCanVoteForPhoto( final User user, final Photo photo );

	boolean userCanSeeUserRankVoteHistory( final User user, final User userWhoIsSeeing );

	boolean userOwnThePhoto( final User user, final Photo photo );

	boolean userCanEditUserData( final User user, final User dataOwnerUser );

	void assertUserExists( final String _userId );

	void assertPhotoExists( final String _photoId );

	void assertUserCanEditPhoto( final User user, final Photo photo );

	void assertUserEqualsToCurrentUser( final int userId );

	void assertUserEqualsToCurrentUser( final User user );

	void assertUserCanSeeUserRankVoteHistory( final User user, final User userWhoIsSeeing );

	void assertCurrentUserIsLogged( final String message );

	void assertUserCanDeletePhoto( final User user, final Photo photo );

	void assertUserCanEditUserData( final User user, final User dataOwnerUser );

	void assertGenreExists( final String _genreId );

	void assertUserWantSeeNudeContent( final User user, final Photo photo, final String url );

	boolean isPhotoHasToBeHiddenBecauseOfNudeContent( final Photo photo, final User user );

	ValidationResult getPhotoCommentingValidationResult( final User user, final Photo photo );

	ValidationResult getPhotoVotingValidationResult( final User user, final Photo photo );

	UserRankInGenreVotingValidationResult getUserRankInGenreVotingValidationResult( final User user, final User voter, final Genre genre );

	boolean isSuperAdminUser( final int userId );

	void assertSuperAdminAccess( final User currentUser );

	boolean isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod( final PhotoComment photoComment, final User user );

	boolean isPhotoAuthorNameMustBeHidden( final Photo photo, final User accessor );
}
