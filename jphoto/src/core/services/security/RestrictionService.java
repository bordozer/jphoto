package core.services.security;

import core.enums.RestrictionType;
import core.general.photo.Photo;
import core.general.user.User;

import java.util.Date;

public interface RestrictionService {

	void restrictUser( final User user, final RestrictionType restrictionType, final Date timeFrom, final Date timeTo );

	void restrictUser( final User user, final Date timeFrom, final Date timeTo );

	void restrictUserPhotoUploading( final User user, final Date timeFrom, final Date timeTo );

	void restrictUserCommenting( final User user, final Date timeFrom, final Date timeTo );

	void restrictUserPhotoAppraisal( final User user, final Date timeFrom, final Date timeTo );

	void restrictUserVotingForRanksInGenres( final User user, final Date timeFrom, final Date timeTo );

	void restrictPhotoToBePhotoOfTheDay( final Photo photo, final Date timeFrom, final Date timeTo );

	void restrictPhotoToBeInTheBestPhotosOfGenre( final Photo photo, final Date timeFrom, final Date timeTo );

	void restrictPhotoCommenting( final Photo photo, final Date timeFrom, final Date timeTo );

	boolean isRestrictedOn( final int entryId, final RestrictionType restrictionType, final Date time );
}
