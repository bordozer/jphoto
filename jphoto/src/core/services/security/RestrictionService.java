package core.services.security;

import core.enums.RestrictionType;
import core.general.photo.Photo;
import core.general.restriction.EntryRestriction;
import core.general.user.User;
import core.interfaces.BaseEntityService;

import java.util.Date;
import java.util.List;

public interface RestrictionService extends BaseEntityService<EntryRestriction> {

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

	List<EntryRestriction> loadUserRestrictions( final int userId );

	List<EntryRestriction> loadPhotoRestrictions( final int userId );

	boolean deactivate( int entryId, final User cancellingUser, final Date cancellingTime );
}
