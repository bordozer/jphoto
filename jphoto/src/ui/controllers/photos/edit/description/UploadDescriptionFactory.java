package ui.controllers.photos.edit.description;

import core.general.user.User;
import core.general.user.UserStatus;

public class UploadDescriptionFactory {

	public static AbstractPhotoUploadAllowance getInstance( final User user, final User accessor ) {

		final UserStatus userStatus = user.getUserStatus();

		switch ( userStatus ) {
			case CANDIDATE:
				return new CandidatePhotoUploadHelper( user, accessor );
			case MEMBER:
				return new MemberPhotoUploadHelper( user, accessor );
		}

		throw new IllegalArgumentException( String.format( "Illegal user status: %s", userStatus ) );
	}
}