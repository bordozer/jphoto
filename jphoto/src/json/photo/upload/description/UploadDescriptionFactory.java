package json.photo.upload.description;

import core.general.user.User;
import core.general.user.UserStatus;

public class UploadDescriptionFactory {

	public static AbstractPhotoUploadAllowance getInstance( final User user, final User accessor ) {

		final UserStatus userStatus = user.getUserStatus();

		switch ( userStatus ) {
			case CANDIDATE:
				return new CandidatePhotoUploadAllowance( user, accessor );
			case MEMBER:
				return new MemberPhotoUploadAllowance( user, accessor );
		}

		throw new IllegalArgumentException( String.format( "Illegal user status: %s", userStatus ) );
	}
}
