package core.services.security;

import core.enums.RestrictionType;
import core.general.photo.Photo;
import core.general.restriction.EntryRestriction;
import core.general.user.User;
import core.interfaces.Restrictable;
import core.services.dao.RestrictionDao;
import core.services.utils.DateUtilsService;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import ui.context.EnvironmentContext;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class RestrictionServiceImpl implements RestrictionService {

	@Autowired
	private RestrictionDao restrictionDao;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Override
	public void restrictEntry( final Restrictable entry, final RestrictionType restrictionType, final Date timeFrom, final Date timeTo ) {

		final EntryRestriction<Restrictable> restriction = new EntryRestriction<>( entry, restrictionType );

		restriction.setRestrictionTimeFrom( timeFrom );
		restriction.setRestrictionTimeTo( timeTo );

		restriction.setActive( true );
		restriction.setCreatingTime( dateUtilsService.getCurrentTime() );
		restriction.setCreator( EnvironmentContext.getCurrentUser() );

		restrictionDao.saveToDB( restriction );
	}

	@Override
	public boolean isRestrictedOn( final int entryId, final RestrictionType restrictionType, final Date time ) {
		final List<EntryRestriction> restrictions = restrictionDao.loadRestrictions( entryId, restrictionType );

		if ( restrictions == null || restrictions.size() == 0 ) {
			return false;
		}

		final Date currentTime = dateUtilsService.getCurrentTime();

		final List<EntryRestriction> activeRestrictions = newArrayList( restrictions );
		CollectionUtils.filter( activeRestrictions, new Predicate<EntryRestriction>() {
			@Override
			public boolean evaluate( final EntryRestriction restriction ) {

				if ( restriction.isCancelled() ) {
					// cancelled
					return false;
				}

				if ( restriction.getRestrictionTimeFrom().getTime() > currentTime.getTime() ) {
					// time from has not came yet
					return false;
				}

				if ( restriction.getRestrictionTimeTo().getTime() < currentTime.getTime() ) {
					// expired
					return false;
				}

				return true;
			}
		} );

		return activeRestrictions != null && activeRestrictions.size() > 0;
	}

	@Override
	public List<EntryRestriction> loadUserRestrictions( final int userId ) {
		return loadRestrictions( userId, RestrictionType.FOR_USERS );
	}

	@Override
	public List<EntryRestriction> loadPhotoRestrictions( final int photoId ) {
		return loadRestrictions( photoId, RestrictionType.FOR_PHOTOS );
	}

	@Override
	public boolean deactivate( final int entryId, final User cancellingUser, final Date cancellingTime ) {
		final EntryRestriction entryRestriction = load( entryId );

		entryRestriction.setActive( false );
		entryRestriction.setCanceller( cancellingUser );
		entryRestriction.setCancellingTime( cancellingTime );

		return save( entryRestriction );
	}

	@Override
	public boolean save( final EntryRestriction entry ) {
		return restrictionDao.saveToDB( entry );
	}

	@Override
	public EntryRestriction load( final int entryId ) {
		return restrictionDao.load( entryId );
	}

	@Override
	public boolean delete( final int restrictionHistoryEntryId ) {
		return restrictionDao.delete( restrictionHistoryEntryId );
	}

	@Override
	public boolean exists( final int entryId ) {
		return restrictionDao.exists( entryId );
	}

	@Override
	public boolean exists( final EntryRestriction entry ) {
		return restrictionDao.exists( entry );
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
