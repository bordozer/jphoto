package core.services.security;

import core.enums.RestrictionType;
import core.general.restriction.EntryRestriction;
import core.general.user.User;
import core.interfaces.BaseEntityService;
import core.interfaces.Restrictable;
import core.services.translator.message.TranslatableMessage;

import java.util.Date;
import java.util.List;

public interface RestrictionService extends BaseEntityService<EntryRestriction> {

	void restrictEntry( final Restrictable entry, final RestrictionType restrictionType, final Date timeFrom, final Date timeTo );

	EntryRestriction getUserPhotoAppraisalRestrictionOn( int userId, Date time );

	EntryRestriction getUserPhotoCommentingRestrictionOn( int userId, Date time );

	EntryRestriction getUserRankVotingRestrictionOn( int userId, Date time );

	EntryRestriction getPhotoOfTheDayRestrictionOn( int photoId, Date time );

	EntryRestriction getPhotoAppraisalRestrictionOn( int photoId, Date time );

	EntryRestriction getPhotoCommentingRestrictionOn( int photoId, Date time );

	EntryRestriction getPhotoBeingInTopRestrictedOn( int photoId, Date time );

	boolean isUserLoginRestrictedOn( final int userId, final Date time );

	boolean isUserPhotoAppraisalRestrictedOn( final int userId, final Date time );

	boolean isPhotoOfTheDayRestrictedOn( final int photoId, final Date time );

	boolean isPhotoBeingInTopRestrictedOn( final int photoId, final Date time );

	boolean isPhotoShowingInPhotoGalleryRestrictedOn( final int photoId, final Date time );

	void assertUserLoginIsNotRestricted( final User user, final Date time );

	List<EntryRestriction> loadAll();

	List<EntryRestriction> load( final List<RestrictionType> restrictionTypes );

	List<EntryRestriction> loadUserRestrictions( final int userId );

	List<EntryRestriction> loadPhotoRestrictions( final int photoId );

	boolean deactivate( final int entryId, final User cancellingUser, final Date cancellingTime );

	TranslatableMessage getUserRestrictionMessage( EntryRestriction restriction );

	TranslatableMessage getPhotoRestrictionMessage( EntryRestriction restriction );

	List<EntryRestriction> getPhotoAllRestrictionsOn( final int photoId, final Date currentTime );
}
