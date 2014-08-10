package core.services.security;

import core.enums.RestrictionType;
import core.general.photo.Photo;
import core.general.restriction.EntryRestriction;
import core.general.user.User;
import core.services.dao.RestrictionDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class RestrictionServiceImpl implements RestrictionService {

	@Autowired
	private RestrictionDao restrictionDao;

	@Override
	public void restrictUser( final User user, final RestrictionType restrictionType, final Date timeFrom, final Date timeTo ) {
		final EntryRestriction<User> restriction = new EntryRestriction<>( user, restrictionType );
		restriction.setRestrictionTimeFrom( timeFrom );
		restriction.setRestrictionTimeTo( timeTo );

		restrictionDao.saveToDB( restriction );
	}

	@Override
	public void restrictUser( final User user, final Date timeFrom, final Date timeTo ) {
		restrictUser( user, RestrictionType.USER_LOGIN, timeFrom, timeTo );
	}

	@Override
	public void restrictUserPhotoUploading( final User user, final Date timeFrom, final Date timeTo ) {
		restrictUser( user, RestrictionType.USER_PHOTO_UPLOADING, timeFrom, timeTo );
	}

	@Override
	public void restrictUserCommenting( final User user, final Date timeFrom, final Date timeTo ) {
		restrictUser( user, RestrictionType.USER_COMMENTING, timeFrom, timeTo );
	}

	@Override
	public void restrictUserPhotoAppraisal( final User user, final Date timeFrom, final Date timeTo ) {
		restrictUser( user, RestrictionType.USER_PHOTO_APPRAISAL, timeFrom, timeTo );
	}

	@Override
	public void restrictUserVotingForRanksInGenres( final User user, final Date timeFrom, final Date timeTo ) {
		restrictUser( user, RestrictionType.USER_VOTING_FOR_RANK_IN_GENRE, timeFrom, timeTo );
	}

	@Override
	public void restrictPhotoToBePhotoOfTheDay( final Photo photo, final Date timeFrom, final Date timeTo ) {
		savePhotoRestriction( photo, timeFrom, timeTo, RestrictionType.PHOTO_TO_BE_PHOTO_OF_THE_DAY );
	}

	@Override
	public void restrictPhotoToBeInTheBestPhotosOfGenre( final Photo photo, final Date timeFrom, final Date timeTo ) {
		savePhotoRestriction( photo, timeFrom, timeTo, RestrictionType.PHOTO_TO_BE_BEST_IN_GENRE );
	}

	@Override
	public void restrictPhotoCommenting( final Photo photo, final Date timeFrom, final Date timeTo ) {
		savePhotoRestriction( photo, timeFrom, timeTo, RestrictionType.PHOTO_COMMENTING );
	}

	@Override
	public boolean isRestrictedOn( final int entryId, final RestrictionType restrictionType, final Date time ) {
		final List<EntryRestriction> restrictions = restrictionDao.loadRestrictions( entryId, restrictionType );

		return restrictions != null && restrictions.get( restrictions.size() - 1 ).getRestrictionTimeTo().getTime() >= time.getTime();
	}

	@Override
	public List<EntryRestriction> loadUserRestrictions( final int userId ) {
		return loadRestrictions( userId, RestrictionType.FOR_USERS );
	}

	@Override
	public List<EntryRestriction> loadPhotoRestrictions( final int userId ) {
		return loadRestrictions( userId, RestrictionType.FOR_PHOTOS );
	}

	private List<EntryRestriction> loadRestrictions( final int userId, final List<RestrictionType> restrictionTypes ) {
		final List<EntryRestriction> result = newArrayList();

		for ( final RestrictionType restrictionType : restrictionTypes ) {
			result.addAll( restrictionDao.loadRestrictions( userId, restrictionType ) );
		}

		return result;
	}

	private void savePhotoRestriction( final Photo photo, final Date timeFrom, final Date timeTo, final RestrictionType photoToBePhotoOfTheDay ) {
		final EntryRestriction<Photo> restriction = new EntryRestriction<>( photo, photoToBePhotoOfTheDay );
		restriction.setRestrictionTimeFrom( timeFrom );
		restriction.setRestrictionTimeTo( timeTo );

		restrictionDao.saveToDB( restriction );
	}
}
