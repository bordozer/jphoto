package core.services.security;

import core.enums.RestrictionType;
import core.general.photo.Photo;
import core.general.restriction.EntryRestriction;
import core.general.user.User;
import core.interfaces.BaseEntityService;
import core.interfaces.Restrictable;

import java.util.Date;
import java.util.List;

public interface RestrictionService extends BaseEntityService<EntryRestriction> {

	void restrictEntry( final Restrictable entry, final RestrictionType restrictionType, final Date timeFrom, final Date timeTo );

	boolean isRestrictedOn( final int entryId, final RestrictionType restrictionType, final Date time );

	List<EntryRestriction> loadUserRestrictions( final int userId );

	List<EntryRestriction> loadPhotoRestrictions( final int photoId );

	boolean deactivate( final int entryId, final User cancellingUser, final Date cancellingTime );
}
