package mocks;

import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.general.photo.UserRankInGenreVotingValidationResult;
import core.general.photo.ValidationResult;
import core.general.user.User;
import core.services.security.SecurityService;

public class SecurityServiceMock implements SecurityService {

	@Override
	public boolean userCanEditPhoto( final User user, final Photo photo ) {
		return false;
	}

	@Override
	public boolean userCanDeletePhoto( final User user, final Photo photo ) {
		return false;
	}

	@Override
	public boolean userCanVoteForPhoto( final User user, final Photo photo ) {
		return false;
	}

	@Override
	public boolean userCanSeeUserRankVoteHistory( final User user, final User userWhoIsSeeing ) {
		return false;
	}

	@Override
	public boolean userOwnThePhoto( final User user, final Photo photo ) {
		return false;
	}

	@Override
	public boolean userCanDeletePhotoComment( final int userId, final int commentId ) {
		return false;
	}

	@Override
	public boolean userCanDeletePhotoComment( final User user, final PhotoComment photoComment ) {
		return false;
	}

	@Override
	public boolean userCanEditUserData( final User user, final User dataOwnerUser ) {
		return false;
	}

	@Override
	public void assertUserExists( final String _userId ) {
	}

	@Override
	public void assertPhotoExists( final String _photoId ) {
	}

	@Override
	public void assertPhotoFileExists( final Photo photo ) {
	}

	@Override
	public void assertUserCanEditPhoto( final User user, final Photo photo ) {
	}

	@Override
	public void assertUserEqualsToCurrentUser( final int userId ) {
	}

	@Override
	public void assertUserEqualsToCurrentUser( final User user ) {
	}

	@Override
	public void assertUserCanSeeUserRankVoteHistory( final User user, final User userWhoIsSeeing ) {
	}

	@Override
	public void assertCurrentUserIsLogged( final String message ) {
	}

	@Override
	public void assertUserCanDeletePhoto( final User user, final Photo photo ) {
	}

	@Override
	public void assertUserCanDeletePhotoComment( final int userId, final int commentId ) {
	}

	@Override
	public void assertUserCanEditUserData( final User user, final User dataOwnerUser ) {
	}

	@Override
	public void assertGenreExists( final String _genreId ) {
	}

	@Override
	public void assertUserWantSeeNudeContent( final User user, final Photo photo, final String url ) {
	}

	@Override
	public boolean isPhotoHasToBeHiddenBecauseOfNudeContent( final Photo photo, final User user ) {
		return false;
	}

	@Override
	public ValidationResult getPhotoCommentingValidationResult( final User user, final Photo photo ) {
		return null;
	}

	@Override
	public ValidationResult getPhotoVotingValidationResult( final User user, final Photo photo ) {
		return null;
	}

	@Override
	public UserRankInGenreVotingValidationResult getUserRankInGenreVotingValidationResult( final User user, final User voter, final Genre genre ) {
		return null;
	}

	@Override
	public boolean isSuperAdminUser( final int userId ) {
		return false;
	}

	@Override
	public void assertSuperAdminAccess( final User currentUser ) {
	}

	@Override
	public boolean isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod( final PhotoComment photoComment, final User user ) {
		return false;
	}

	@Override
	public boolean isPhotoAuthorNameMustBeHidden( final Photo photo, final User accessor ) {
		return false;
	}
}
