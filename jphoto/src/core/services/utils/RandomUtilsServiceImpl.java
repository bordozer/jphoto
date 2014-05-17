package core.services.utils;

import core.enums.FavoriteEntryType;
import core.enums.PhotoActionAllowance;
import core.exceptions.BaseRuntimeException;
import core.general.photo.Photo;
import core.general.user.User;
import core.general.user.UserMembershipType;
import core.interfaces.Identifiable;
import core.services.photo.PhotoService;
import core.services.security.SecurityService;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import utils.UserUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;

public class RandomUtilsServiceImpl implements RandomUtilsService {

	public static String[] PHOTO_BACKGROUND_COLORS = { "", "848484", "A4A4A4" , "BDBDBD" , "D8D8D8" , "E6E6E6" , "F2F2F2" , "FAFAFA" , "848484" , "6E6E6E", "585858", "424242", "2E2E2E", "1C1C1C", "000000" };

	@Autowired
	private PhotoService photoService;

	@Autowired
	private SecurityService securityService;

	@Override
	public PhotoActionAllowance getRandomPhotoAllowance() {
		final PhotoActionAllowance[] allowances = new PhotoActionAllowance[]{
			PhotoActionAllowance.CANDIDATES_AND_MEMBERS
			, PhotoActionAllowance.CANDIDATES_AND_MEMBERS
			, PhotoActionAllowance.CANDIDATES_AND_MEMBERS
			, PhotoActionAllowance.CANDIDATES_AND_MEMBERS
			, PhotoActionAllowance.CANDIDATES_AND_MEMBERS
			, PhotoActionAllowance.CANDIDATES_AND_MEMBERS
			, PhotoActionAllowance.CANDIDATES_AND_MEMBERS
			, PhotoActionAllowance.CANDIDATES_AND_MEMBERS
			, PhotoActionAllowance.CANDIDATES_AND_MEMBERS
			, PhotoActionAllowance.CANDIDATES_AND_MEMBERS
			, PhotoActionAllowance.CANDIDATES_AND_MEMBERS
			, PhotoActionAllowance.CANDIDATES_AND_MEMBERS
			, PhotoActionAllowance.CANDIDATES_AND_MEMBERS
			, PhotoActionAllowance.CANDIDATES_AND_MEMBERS
			, PhotoActionAllowance.CANDIDATES_AND_MEMBERS
			, PhotoActionAllowance.CANDIDATES_AND_MEMBERS
			, PhotoActionAllowance.CANDIDATES_AND_MEMBERS
			, PhotoActionAllowance.CANDIDATES_AND_MEMBERS
			, PhotoActionAllowance.CANDIDATES_AND_MEMBERS
			, PhotoActionAllowance.CANDIDATES_AND_MEMBERS
			, PhotoActionAllowance.MEMBERS_ONLY
			, PhotoActionAllowance.MEMBERS_ONLY
			, PhotoActionAllowance.MEMBERS_ONLY
			, PhotoActionAllowance.MEMBERS_ONLY
			, PhotoActionAllowance.MEMBERS_ONLY
			, PhotoActionAllowance.MEMBERS_ONLY
			, PhotoActionAllowance.MEMBERS_ONLY
			, PhotoActionAllowance.MEMBERS_ONLY
			, PhotoActionAllowance.MEMBERS_ONLY
			, PhotoActionAllowance.MEMBERS_ONLY
			, PhotoActionAllowance.MEMBERS_ONLY
			, PhotoActionAllowance.MEMBERS_ONLY
			, PhotoActionAllowance.ACTIONS_DENIED
		};

		final int randomIndex = getRandomInt( 0, allowances.length - 1 );
		return allowances[ randomIndex ];
	}

	public String getPhotoBackgroundRandomColor() {
		final int randomIndex = getRandomInt( 0, PHOTO_BACKGROUND_COLORS.length - 1 );
		return PHOTO_BACKGROUND_COLORS[ randomIndex ];
	}

	@Override
	public int getRandomInt( final int minValue, final int maxValue ) {
		return ( int ) getRandomLong( minValue, maxValue );
	}

	@Override
	public long getRandomLong( final long minValue, final long maxValue ) {
		if ( maxValue == 0 ) {
			return 0;
		}

		return minValue + ( long ) ( Math.random() * ( maxValue - minValue + 1 ) );
	}

	@Override
	public Date getRandomDate( final Date time1, final Date time2 ) {
		return new Date( getRandomLong( time1.getTime(), time2.getTime() ) ); // TODO: works incorrect
	}

	@Override
	public Date getRandomDate( final Date time, final int minutes ) {
		final Calendar calendar = Calendar.getInstance();
		calendar.add( Calendar.MINUTE, getRandomInt( 1, minutes ) );
		return calendar.getTime();
	}

	@Override
	public boolean getRandomBoolean() {
		return getRandomInt( 0, 1 ) == 0;
	}

	@Override
	public String getRandomStringArrayElement( String[] array ) {
		final int randomCommentIndex = getRandomInt( 0, array.length - 1 );
		return array[randomCommentIndex];
	}

	@Override
	public int getRandomIntegerArrayElement( int[] array ) {
		final int randomCommentIndex = getRandomInt( 0, array.length - 1 );
		return array[randomCommentIndex];
	}

	@Override
	public Photo getRandomPhotoButNotOfUser( final User exceptUser, final List<Integer> photosIds ) {
		final int maxIterations = 1000;
		int counter = 0;
		while ( true ) {
			counter++;

			if ( counter > maxIterations ) {
				break;
			}

			final Photo randomPhoto = getRandomPhotoId( photosIds );
			if ( ! securityService.userOwnThePhoto( exceptUser, randomPhoto ) ) {
				return randomPhoto;
			}
		}
		return null;
	}

	@Override
	public User getRandomUserButNotThisOne( final User exceptUser, final List<User> users ) {
		final int maxIterations = 1000;
		int counter = 0;
		while ( true ) {
			counter++;

			if ( counter > maxIterations ) {
				break;
			}

			final User randomUser = getRandomGenericListElement( users );
			if ( ! UserUtils.isUsersEqual( randomUser, exceptUser ) ) {
				return randomUser;
			}
		}
		return null;
	}

	@Override
	public User getRandomUserButNotThisOne( final User exceptUser, final UserMembershipType membershipType, List<User> users ) {
		final int maxIterations = 1000;
		int counter = 0;

		while ( true ) {
			counter++;

			if ( counter > maxIterations ) {
				break;
			}

			final User user = getRandomUserButNotThisOne( exceptUser, users );
			if ( user != null && user.getMembershipType() == membershipType ) {
				return user;
			}
		}

		return null;
	}

	@Override
	public Photo getRandomPhotoId( final List<Integer> photosIds ) {
		final Integer randomPhotoId = getRandomGenericListElement( photosIds );

		if ( randomPhotoId == null ) {
			return null;
		}

		return photoService.load( randomPhotoId );
	}

	@Override
	public Photo getRandomPhoto( final List<Photo> photos ) {
		return getRandomGenericListElement( photos );
	}

	@Override
	public Photo getRandomPhotoOfUser( final int userId ) {
		return getRandomPhotoId( photoService.getUserPhotosIds( userId ) );
	}

	@Override
	public  <T> T getRandomGenericListElement( final List<T> items ) {
		if ( items == null || items.size() == 0 ) {
			return null;
		}

		final int randomUserIndex = getRandomInt( 0, items.size() - 1 );
		return items.get( randomUserIndex );
	}

	@Override
	public <T> T getRandomGenericSetElement( final Set<T> items ) {
		if ( items.size() == 0 ) {
			return null;
		}

		final int randomUserIndex = getRandomInt( 0, items.size() - 1 );
		int i = 0;

		for ( final T item : items ) {
			if ( i == randomUserIndex ) {
				return item;
			}
			i++;
		}

		throw new BaseRuntimeException( String.format( "Random element of Set can not be found: %s", items ) );
	}

	@Override
	public User getRandomUser( final List<User> users ) {
		return getRandomGenericListElement( users );
	}

	@Override
	public FavoriteEntryType getRandomFavoriteType( final List<FavoriteEntryType> favoriteEntries ) {
		final int randomUserIndex = getRandomInt( 0, favoriteEntries.size() - 1 );
		return favoriteEntries.get( randomUserIndex );
	}

	@Override
	public <T extends Identifiable> List<T> getRandomNUniqueListElements( final List<T> items ) {
		return getRandomNUniqueListElements( items, getRandomInt( 0, items.size() - 1 ) );
	}

	@Override
	public <T extends Identifiable> List<T> getRandomNUniqueListElements( final List<T> items, final int qty ) {

		final List<T> clone = newArrayList( items );

		final List<Integer> chosenIds = newArrayList();
		int i = 0;
		while( i < qty ) {
			final T randomUserTeamMember = getRandomGenericListElement( items );
			if ( chosenIds.contains( randomUserTeamMember.getId() ) ) {
				continue;
			}

			chosenIds.add( randomUserTeamMember.getId() );
			i++;
		}

		CollectionUtils.filter( clone, new Predicate<T>() {
			@Override
			public boolean evaluate( final T element ) {
				return chosenIds.contains( element.getId() );
			}
		} );

		return clone;
	}

	@Override
	public <T extends Identifiable> Set<T> getRandomNUniqueSetElements( final Set<T> items ) {
		return newHashSet( getRandomNUniqueListElements( newArrayList( items ) ) );
	}

	@Override
	public <T> T getRandomGenericArrayElement( final T[] array ) {
		final int length = array.length;
		return array[ getRandomInt( 0, length - 1 )];
	}

	public void setPhotoService( final PhotoService photoService ) {
		this.photoService = photoService;
	}

	public void setSecurityService( final SecurityService securityService ) {
		this.securityService = securityService;
	}
}
