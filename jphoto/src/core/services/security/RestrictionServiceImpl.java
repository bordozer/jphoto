package core.services.security;

import core.enums.RestrictionType;
import core.exceptions.RestrictionException;
import core.general.restriction.EntryRestriction;
import core.general.user.User;
import core.interfaces.Restrictable;
import core.services.dao.RestrictionDao;
import core.services.system.Services;
import core.services.translator.message.TranslatableMessage;
import core.services.utils.DateUtilsService;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import ui.context.EnvironmentContext;
import ui.services.security.UsersSecurityService;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class RestrictionServiceImpl implements RestrictionService {

	@Autowired
	private RestrictionDao restrictionDao;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private UsersSecurityService usersSecurityService;

	@Autowired
	private Services services;

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
	public EntryRestriction getUserPhotoAppraisalRestrictionOn( final int userId, final Date time ) {
		return getEntryRestriction( getRestrictionsOn( userId, RestrictionType.USER_PHOTO_APPRAISAL, time ) );
	}

	@Override
	public EntryRestriction getUserPhotoCommentingRestrictionOn( final int userId, final Date time ) {
		return getEntryRestriction( getRestrictionsOn( userId, RestrictionType.USER_COMMENTING, time ) );
	}

	@Override
	public EntryRestriction getUserRankVotingRestrictionOn( final int userId, final Date time ) {
		return getEntryRestriction( getRestrictionsOn( userId, RestrictionType.USER_VOTING_FOR_RANK_IN_GENRE, time ) );
	}

	@Override
	public EntryRestriction getPhotoOfTheDayRestrictionOn( final int photoId, final Date time ) {
		return getEntryRestriction( getRestrictionsOn( photoId, RestrictionType.PHOTO_TO_BE_PHOTO_OF_THE_DAY, time ) );
	}

	@Override
	public EntryRestriction getPhotoAppraisalRestrictionOn( final int photoId, final Date time ) {
		return getEntryRestriction( getRestrictionsOn( photoId, RestrictionType.PHOTO_APPRAISAL, time ) );
	}

	@Override
	public EntryRestriction getPhotoCommentingRestrictionOn( final int photoId, final Date time ) {
		return getEntryRestriction( getRestrictionsOn( photoId, RestrictionType.PHOTO_COMMENTING, time ) );
	}

	@Override
	public EntryRestriction getPhotoBeingInTopRestrictedOn( final int photoId, final Date time ) {
		return getEntryRestriction( getRestrictionsOn( photoId, RestrictionType.PHOTO_TO_BE_BEST_IN_GENRE, time ) );
	}

	@Override
	public boolean isUserLoginRestrictedOn( final int userId, final Date time ) {
		return isRestrictedOn( userId, RestrictionType.USER_LOGIN, time );
	}

	@Override
	public boolean isUserPhotoAppraisalRestrictedOn( final int userId, final Date time ) {
		return isRestrictedOn( userId, RestrictionType.USER_PHOTO_APPRAISAL, time );
	}

	@Override
	public boolean isPhotoOfTheDayRestrictedOn( final int photoId, final Date time ) {
		return isRestrictedOn( photoId, RestrictionType.PHOTO_TO_BE_PHOTO_OF_THE_DAY, time );
	}

	@Override
	public boolean isPhotoShowingInTopBestRestrictedOn( final int photoId, final Date time ) {
		return isRestrictedOn( photoId, RestrictionType.PHOTO_TO_BE_BEST_IN_GENRE, time );
	}

	@Override
	public boolean isPhotoShowingInPhotoGalleryRestrictedOn( final int photoId, final Date time ) {
		return isRestrictedOn( photoId, RestrictionType.PHOTO_SHOWING_IN_PHOTO_GALLERY, time );
	}

	@Override
	public void assertUserLoginIsNotRestricted( final User user, final Date time ) {
		final List<EntryRestriction> activeRestrictions = getRestrictionsOn( user.getId(), RestrictionType.USER_LOGIN, time );
		if ( activeRestrictions != null && activeRestrictions.size() > 0 ) {
			final EntryRestriction restriction = activeRestrictions.get( 0 );

			usersSecurityService.resetEnvironmentAndLogOutUser( EnvironmentContext.getCurrentUser() );

			throw new RestrictionException( restriction.getId(), String.format( "User #%s - login is restricted", user ) );
		}
	}

	@Override
	public List<EntryRestriction> loadAll() {
		return restrictionDao.loadAll();
	}

	@Override
	public List<EntryRestriction> load( final List<RestrictionType> restrictionTypes ) {
		return restrictionDao.load( restrictionTypes );
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

	@Override
	public TranslatableMessage getUserRestrictionMessage( final EntryRestriction restriction ) {
		return new TranslatableMessage( "You are restricted in $1 because $2 on $3 restricted you in this rights. The restriction is active from $4 till $5.", services )
			.translatableString( restriction.getRestrictionType().getName() )
			.string( restriction.getCreator().getNameEscaped() )
			.dateTimeFormatted( restriction.getCreatingTime() )
			.dateTimeFormatted( restriction.getRestrictionTimeFrom() )
			.dateTimeFormatted( restriction.getRestrictionTimeTo() )
			;
	}

	@Override
	public TranslatableMessage getPhotoRestrictionMessage( final EntryRestriction restriction ) {
		return new TranslatableMessage( "$1 is disabled for the photo by $2 on $3. The restriction is active from $4 till $5.", services )
			.translatableString( restriction.getRestrictionType().getName() )
			.string( restriction.getCreator().getNameEscaped() )
			.dateTimeFormatted( restriction.getCreatingTime() )
			.dateTimeFormatted( restriction.getRestrictionTimeFrom() )
			.dateTimeFormatted( restriction.getRestrictionTimeTo() )
			;
	}

	@Override
	public List<EntryRestriction> getPhotoAllRestrictionsOn( final int photoId, final Date currentTime ) {
		final List<EntryRestriction> result = newArrayList();

		for ( final RestrictionType restrictionType : RestrictionType.FOR_PHOTOS ) {
			final List<EntryRestriction> restrictionsOn = getRestrictionsOn( photoId, restrictionType, currentTime );
			if ( restrictionsOn != null && restrictionsOn.size() > 0 ) {
				result.add( restrictionsOn.get( 0 ) );
			}
		}
		return result;
	}

	private  List<EntryRestriction> getRestrictionsOn( final int entryId, final RestrictionType restrictionType, final Date time ) {

		final List<EntryRestriction> restrictions = restrictionDao.loadRestrictions( entryId, restrictionType );

		if ( restrictions == null || restrictions.size() == 0 ) {
			return newArrayList();
		}

		final List<EntryRestriction> activeRestrictions = newArrayList( restrictions );
		CollectionUtils.filter( activeRestrictions, new Predicate<EntryRestriction>() {
			@Override
			public boolean evaluate( final EntryRestriction restriction ) {

				if ( restriction.isCancelled() ) {
					// cancelled
					return false;
				}

				if ( restriction.getRestrictionTimeFrom().getTime() > time.getTime() ) {
					// time from has not came yet
					return false;
				}

				if ( restriction.getRestrictionTimeTo().getTime() < time.getTime() ) {
					// expired
					return false;
				}

				return true;
			}
		} );

		return activeRestrictions;
	}

	@Override
	public boolean isRestrictedOn( final int entryId, final RestrictionType restrictionType, final Date time ) {
		final List<EntryRestriction> activeRestrictions = getRestrictionsOn( entryId, restrictionType, time );
		return activeRestrictions != null && activeRestrictions.size() > 0;
	}

	private List<EntryRestriction> loadRestrictions( final int entryId, final List<RestrictionType> restrictionTypes ) {
		final List<EntryRestriction> result = newArrayList();

		for ( final RestrictionType restrictionType : restrictionTypes ) {
			result.addAll( restrictionDao.loadRestrictions( entryId, restrictionType ) ); // TODO: load all in one query
		}

		return result;
	}

	/*private void savePhotoRestriction( final Photo photo, final Date timeFrom, final Date timeTo, final RestrictionType photoToBePhotoOfTheDay ) {
		final EntryRestriction<Photo> restriction = new EntryRestriction<>( photo, photoToBePhotoOfTheDay );
		restriction.setRestrictionTimeFrom( timeFrom );
		restriction.setRestrictionTimeTo( timeTo );

		restrictionDao.saveToDB( restriction );
	}*/

	private EntryRestriction getEntryRestriction( final List<EntryRestriction> restrictions ) {
		if ( restrictions == null || restrictions.size() == 0 ) {
			return null;
		}

		return restrictions.get( 0 );
	}
}
