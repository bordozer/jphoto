package rest.photo.upload.category.allowance;

import core.general.user.User;
import core.general.user.UserStatus;
import core.services.system.Services;
import core.services.translator.Language;

public class UploadDescriptionFactory {

	public static AbstractPhotoUploadAllowance getInstance( final User user, final User accessor, final Language language, final Services services ) {

		final UserStatus userStatus = user.getUserStatus();

		switch ( userStatus ) {
			case CANDIDATE:
				return new CandidatePhotoUploadAllowance( user, accessor, language, services );
			case MEMBER:
				return new MemberPhotoUploadAllowance( user, accessor, language, services );
		}

		throw new IllegalArgumentException( String.format( "Illegal user status: %s", userStatus ) );
	}
}
