package core.services.security;

import core.enums.RestrictionType;
import core.general.photo.Photo;
import core.general.user.User;

import java.util.Date;

public interface RestrictionService {

	void lockUser( final User user, final Date timeFrom, final Date timeTo );

	void lockUserPhotoUploading( final User user, final Date timeFrom, final Date timeTo );

	void lockUserCommenting( final User user, final Date timeFrom, final Date timeTo );

	void lockUserPhotoAppraisal( final User user, final Date timeFrom, final Date timeTo );

	void lockUserVotingForRanksInGenres( final User user, final Date timeFrom, final Date timeTo );

	void lockPhotoToBePhotoOfTheDay( final Photo photo, final Date timeFrom, final Date timeTo );

	void lockPhotoToBeInTheBestPhotosOfGenre( final Photo photo, final Date timeFrom, final Date timeTo );

	void lockPhotoCommenting( final Photo photo, final Date timeFrom, final Date timeTo );

	boolean isRestrictedNow( final int entryId, final RestrictionType restrictionType, final Date time );
}
