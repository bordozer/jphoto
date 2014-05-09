package core.services.utils;

import core.enums.FavoriteEntryType;
import core.enums.PhotoActionAllowance;
import core.general.photo.Photo;
import core.general.user.User;
import core.general.user.UserMembershipType;
import core.interfaces.Identifiable;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface RandomUtilsService {

	String getPhotoBackgroundRandomColor();

	int getRandomInt( final int minValue, final int maxValue );

	long getRandomLong( final long minValue, final long maxValue );

	Date getRandomDate( final Date time1, final Date time2 );

	Date getRandomDate( final Date time, final int minutes );

	boolean getRandomBoolean();

	String getRandomStringArrayElement( String[] array );

	int getRandomIntegerArrayElement( int[] array );

	Photo getRandomPhotoButNotOfUser( final User exceptUser, final List<Integer> photosIds );

	User getRandomUserButNotThisOne( final User exceptUser, final List<User> users );

	User getRandomUserButNotThisOne( final User exceptUser, final UserMembershipType exceptMembershipType, List<User> users );

	Photo getRandomPhotoId( final List<Integer> photosIds );

	Photo getRandomPhoto( final List<Photo> photos );

	Photo getRandomPhotoOfUser( final int userId );

	User getRandomUser( final List<User> users );

	FavoriteEntryType getRandomFavoriteType( final List<FavoriteEntryType> favoriteEntries );

	<T> T getRandomGenericListElement( final List<T> items );

	<T> T getRandomGenericSetElement( final Set<T> items );

	<T extends Identifiable> List<T> getRandomNUniqueListElements( final List<T> items );

	<T extends Identifiable> List<T> getRandomNUniqueListElements( final List<T> items, final int qty );

	<T extends Identifiable> Set<T> getRandomNUniqueSetElements( final Set<T> items );

	<T> T getRandomGenericArrayElement( final T[] array );

	PhotoActionAllowance getRandomPhotoAllowance();
}
