package utils;

import common.AbstractTestCase;
import core.context.Environment;
import core.context.EnvironmentContext;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.translator.Language;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class UserUtilsTest extends AbstractTestCase {

	public UserUtilsTest() {
	}

	@Before
	public void setup() {
		super.setup();
	}

	// TODO: use SecurityService!
	/*@Test
	public void userCanNotVoteForOwnPhoto() {
		final int userId = 111;
		final User user = new User();
		user.setId( userId );
		user.setName( "User name" );

		final int photoId = 444;
		final Photo photo = new Photo();
		photo.setId( photoId );
		photo.setUserId( userId );
		photo.setName( "Photo name" );

		assertFalse( "Condition must be FALSE but true", UserUtils.isPhotoVotingAccessibleForUser( user, photo ) );
	}*/

	// TODO: use SecurityService!
	/*@Test
	public void userCanVoteForPhotoOfAnotherUser() {
		final User user2 = new User();
		user2.setId( 222 );
		user2.setName( "User name 2" );

		final int userId = 111;
		final User user = new User();
		user.setId( userId );
		user.setName( "User name" );

		final int photoId = 444;
		final Photo photo = new Photo();
		photo.setId( photoId );
		photo.setUserId( userId );
		photo.setName( "Photo name" );

		assertTrue( "Condition must be TRUE but false", UserUtils.isPhotoVotingAccessibleForUser( user2, photo ) );
	}*/

	// TODO: use SecurityService!
	/*@Test
	public void notLoggedUserCanNotVoteForPhoto() {
		final int photoId = 444;
		final Photo photo = new Photo();
		photo.setId( photoId );
		photo.setUserId( 111 );
		photo.setName( "Photo name" );

		assertFalse( "Condition must be FALSE but true", UserUtils.isPhotoVotingAccessibleForUser( User.NOT_LOGGED_USER, photo ) );
	}*/

	@Test
	public void userPhotoOwning() {
		final User user2 = new User();
		user2.setId( 222 );
		user2.setName( "User name 2" );

		final int userId = 111;
		final User user = new User();
		user.setId( userId );
		user.setName( "User name" );

		final int photoId = 444;
		final Photo photo = new Photo();
		photo.setId( photoId );
		photo.setUserId( userId );
		photo.setName( "Photo name" );

		assertTrue( "Condition must be TRUE but false", UserUtils.isUserOwnThePhoto( user, photo ) );
		assertFalse( "Condition must be FALSE but true", UserUtils.isUserOwnThePhoto( user2, photo ) );
	}

	@Test
	public void usersEquality() {
		final User user2 = new User();
		user2.setId( 222 );
		user2.setName( "User name 2" );

		final User user = new User();
		user.setId( 111 );
		user.setName( "User name" );

		final User user3 = new User();
		user3.setId( 111 );
		user3.setName( "User name" );

		assertTrue( "Condition must be TRUE but false", UserUtils.isUsersEqual( user, user ) );
		assertFalse( "Condition must be FALSE but true", UserUtils.isUsersEqual( user, user2 ) );
		assertTrue( "Condition must be TRUE but false", UserUtils.isUsersEqual( user, user3 ) );
	}

	@Test
	public void usersEquality1() {
		final User user2 = new User();
		user2.setId( 222 );
		user2.setName( "User name 2" );

		final User user = new User();
		user.setId( 111 );
		user.setName( "User name" );

		emulateUserLogin( User.NOT_LOGGED_USER );
		assertFalse( "Condition must be FALSE but true", UserUtils.isCurrentUserLoggedUser() );

		emulateUserLogin( user2 );

		assertTrue( "Condition must be TRUE but false", UserUtils.isCurrentUserLoggedUser() );

		assertTrue( "Condition must be TRUE but false", UserUtils.isLoggedUser( user2 ) );
		assertTrue( "Condition must be TRUE but false", UserUtils.isLoggedUser( user ) );
		assertFalse( "Condition must be FALSE but true", UserUtils.isLoggedUser( User.NOT_LOGGED_USER ) );

		assertTrue( "Condition must be TRUE but false", UserUtils.isUserEqualsToCurrentUser( user2 ) );
		assertFalse( "Condition must be FALSE but true", UserUtils.isUserEqualsToCurrentUser( user ) );
	}

	// TODO: use SecurityService!
	/*@Test
	public void accessToEditPhoto() {
		final int photoOwnerUserId = 111;
		final User photoOwnerUser = new User();
		photoOwnerUser.setId( photoOwnerUserId );

		final int justUserId = 222;
		final User justUser = new User();
		justUser.setId( justUserId );

		final int photoId = 444;
		final Photo photo = new Photo();
		photo.setId( photoId );
		photo.setUserId( photoOwnerUserId );
		photo.setName( "Photo name" );

		assertTrue( "Condition must be TRUE but false", UserUtils.isLoggedUser( photoOwnerUser ) && UserUtils.isUserOwnThePhoto( photoOwnerUser, photo ) );
		assertFalse( "Condition must be FALSE but true", UserUtils.isLoggedUser( justUser ) && UserUtils.isUserOwnThePhoto( justUser, photo ) );
	}*/

	// TODO: use SecurityService!
	/*@Test
	public void accessToVoteForPhoto() {
		final int photoOwnerUserId = 111;

		final int justUserId = 222;

		final int photoId = 444;
		final Photo photo = new Photo();
		photo.setId( photoId );
		photo.setUserId( photoOwnerUserId );
		photo.setName( "Photo name" );

		assertTrue( "Condition must be TRUE but false", UserUtils.isPhotoVotingAccessibleForUser( justUserId, photo ) );
		assertFalse( "Condition must be FALSE but true", UserUtils.isPhotoVotingAccessibleForUser( photoOwnerUserId, photo ) );
	}*/

	// TODO: use SecurityService!
	/*@Test
	public void isLoggedUserCanEditUserData() {
		final int dataOwnerId = 111;
		final User dataOwner = new User();
		dataOwner.setId( dataOwnerId );

		final int justUserId = 222;
		final User justUser = new User();
		justUser.setId( justUserId );

		emulateUserLogin( User.NOT_LOGGED_USER );
		assertFalse( "Condition must be FALSE but true", UserUtils.isLoggedUserCanEditUserData( dataOwner ) );

		emulateUserLogin( dataOwner );
		assertTrue( "Condition must be TRUE but false", UserUtils.isLoggedUserCanEditUserData( dataOwner ) );

		emulateUserLogin( justUser );
		assertFalse( "Condition must be FALSE but true", UserUtils.isLoggedUserCanEditUserData( dataOwner ) );
	}*/

	private void emulateUserLogin( final User dataOwner ) {
		EnvironmentContext.setEnv( new Environment( dataOwner, systemVarsServiceMock.getLanguage() ) );
	}
}
