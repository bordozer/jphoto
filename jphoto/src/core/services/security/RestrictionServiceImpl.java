package core.services.security;

import core.enums.RestrictionType;
import core.general.photo.Photo;
import core.general.restriction.EntryRestriction;
import core.general.user.User;
import core.services.dao.RestrictionDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public class RestrictionServiceImpl implements RestrictionService {

	@Autowired
	private RestrictionDao restrictionDao;

	@Override
	public void lockUser( final User user, final Date timeFrom, final Date timeTo ) {
		saveUserRestriction( user, timeFrom, timeTo, RestrictionType.USER_LOGIN );
	}

	@Override
	public void lockUserPhotoUploading( final User user, final Date timeFrom, final Date timeTo ) {
		saveUserRestriction( user, timeFrom, timeTo, RestrictionType.USER_PHOTO_UPLOADING );
	}

	@Override
	public void lockUserCommenting( final User user, final Date timeFrom, final Date timeTo ) {
		saveUserRestriction( user, timeFrom, timeTo, RestrictionType.USER_COMMENTING );
	}

	@Override
	public void lockUserPhotoAppraisal( final User user, final Date timeFrom, final Date timeTo ) {
		saveUserRestriction( user, timeFrom, timeTo, RestrictionType.USER_PHOTO_APPRAISAL );
	}

	@Override
	public void lockUserVotingForRanksInGenres( final User user, final Date timeFrom, final Date timeTo ) {
		saveUserRestriction( user, timeFrom, timeTo, RestrictionType.USER_VOTING_FOR_RANK_IN_GENRE );
	}

	@Override
	public void lockPhotoToBePhotoOfTheDay( final Photo photo, final Date timeFrom, final Date timeTo ) {
		savePhotoRestriction( photo, timeFrom, timeTo, RestrictionType.PHOTO_TO_BE_PHOTO_OF_THE_DAY );
	}

	@Override
	public void lockPhotoToBeInTheBestPhotosOfGenre( final Photo photo, final Date timeFrom, final Date timeTo ) {
		savePhotoRestriction( photo, timeFrom, timeTo, RestrictionType.PHOTO_TO_BE_BEST_IN_GENRE );
	}

	@Override
	public void lockPhotoCommenting( final Photo photo, final Date timeFrom, final Date timeTo ) {
		savePhotoRestriction( photo, timeFrom, timeTo, RestrictionType.PHOTO_COMMENTING );
	}

	@Override
	public boolean isRestrictedNow( final int entryId, final RestrictionType restrictionType, final Date time ) {
		final List<EntryRestriction> restrictions = restrictionDao.loadRestrictions( entryId, restrictionType );

		return restrictions != null && restrictions.get( restrictions.size() - 1 ).getRestrictionTimeTo().getTime() >= time.getTime();
	}

	private void saveUserRestriction( final User user, final Date timeFrom, final Date timeTo, final RestrictionType userLogin ) {
		final EntryRestriction<User> restriction = new EntryRestriction<>( user, userLogin );
		restriction.setRestrictionTimeFrom( timeFrom );
		restriction.setRestrictionTimeTo( timeTo );

		restrictionDao.saveToDB( restriction );
	}

	private void savePhotoRestriction( final Photo photo, final Date timeFrom, final Date timeTo, final RestrictionType photoToBePhotoOfTheDay ) {
		final EntryRestriction<Photo> restriction = new EntryRestriction<>( photo, photoToBePhotoOfTheDay );
		restriction.setRestrictionTimeFrom( timeFrom );
		restriction.setRestrictionTimeTo( timeTo );

		restrictionDao.saveToDB( restriction );
	}
}
