package services;

import common.AbstractTestCase;
import core.context.Environment;
import core.context.EnvironmentContext;
import core.exceptions.AccessDeniedException;
import core.exceptions.NudeContentException;
import core.exceptions.notFound.GenreNotFoundException;
import core.exceptions.notFound.PhotoNotFoundException;
import core.exceptions.notFound.UserNotFoundException;
import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.entry.GenreService;
import core.services.photo.PhotoService;
import core.services.security.SecurityService;
import core.services.security.SecurityServiceImpl;
import core.services.system.ConfigurationService;
import core.services.user.UserService;
import core.services.utils.SystemVarsService;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

@ContextConfiguration( locations = {"file:springConfigs/ConfigurationService.xml"} )
public class SecurityServiceTest extends AbstractTestCase {

	@Autowired
	private SystemVarsService systemVarsService;

	@Autowired
	private ConfigurationService configurationService;

	@Before
	public void setup() {
		super.setup();
	}

	@Test
	public void userCanEditPhotoTest() {

		final User photoAuthor = new User();
		photoAuthor.setId( 111 );

		final User justUser = new User();
		justUser.setId( 222 );

		final Photo photo = new Photo();
		photo.setId( 777 );
		photo.setUserId( 111 );

		final SecurityService securityService = getSecurityService();

		assertTrue( String.format( "Must be TRUE but FALSE" ), securityService.userCanEditPhoto( photoAuthor, photo ) );
		assertFalse( String.format( "Must be FALSE but TRUE" ), securityService.userCanEditPhoto( justUser, photo ) );
		assertFalse( String.format( "Must be FALSE but TRUE" ), securityService.userCanEditPhoto( User.NOT_LOGGED_USER, photo ) );
	}

	@Test
	public void userCanDeletePhotoTest() {

		final User photoAuthor = new User();
		photoAuthor.setId( 111 );

		final User justUser = new User();
		justUser.setId( 222 );

		final Photo photo = new Photo();
		photo.setId( 777 );
		photo.setUserId( 111 );

		final SecurityService securityService = getSecurityService();

		assertTrue( String.format( "Must be TRUE but FALSE" ), securityService.userCanDeletePhoto( photoAuthor, photo ) );
		assertFalse( String.format( "Must be FALSE but TRUE" ), securityService.userCanDeletePhoto( justUser, photo ) );
		assertFalse( String.format( "Must be FALSE but TRUE" ), securityService.userCanDeletePhoto( User.NOT_LOGGED_USER, photo ) );
	}

	@Test
	public void userCanVoteForPhotoTest() {

		final User photoAuthor = new User();
		photoAuthor.setId( 111 );

		final User justUser = new User();
		justUser.setId( 222 );

		final Photo photo = new Photo();
		photo.setId( 777 );
		photo.setUserId( 111 );

		final SecurityService securityService = getSecurityService();

		assertTrue( String.format( "Must be TRUE but FALSE" ), securityService.userCanVoteForPhoto( justUser, photo ) );
		assertFalse( String.format( "Must be FALSE but TRUE" ), securityService.userCanVoteForPhoto( photoAuthor, photo ) );
		assertFalse( String.format( "Must be FALSE but TRUE" ), securityService.userCanVoteForPhoto( User.NOT_LOGGED_USER, photo ) );
	}

	@Test
	public void userOwnThePhotoTest() {

		final User photoAuthor = new User();
		photoAuthor.setId( 111 );

		final User justUser = new User();
		justUser.setId( 222 );

		final Photo photo = new Photo();
		photo.setId( 777 );
		photo.setUserId( 111 );

		final SecurityService securityService = getSecurityService();

		assertTrue( String.format( "Must be TRUE but FALSE" ), securityService.userOwnThePhoto( photoAuthor, photo ) );
		assertFalse( String.format( "Must be FALSE but TRUE" ), securityService.userOwnThePhoto( justUser, photo ) );
		assertFalse( String.format( "Must be FALSE but TRUE" ), securityService.userOwnThePhoto( User.NOT_LOGGED_USER, photo ) );
	}

	@Test
	public void userCanEditUserDataTest() {

		final User photoAuthor = new User();
		photoAuthor.setId( 111 );

		final User justUser = new User();
		justUser.setId( 222 );

		final Photo photo = new Photo();
		photo.setId( 777 );
		photo.setUserId( 111 );

		final SecurityService securityService = getSecurityService();

		assertTrue( String.format( "Must be TRUE but FALSE" ), securityService.userCanEditUserData( photoAuthor, photoAuthor ) );
		assertFalse( String.format( "Must be FALSE but TRUE" ), securityService.userCanEditUserData( justUser, photoAuthor ) );
		assertFalse( String.format( "Must be FALSE but TRUE" ), securityService.userCanEditUserData( User.NOT_LOGGED_USER, photoAuthor ) );
	}

	@Test
	public void assertPhotoExistsTest() {

		final Photo photo = new Photo();
		photo.setId( 777 );
		photo.setUserId( 111 );

		final PhotoService photoService = EasyMock.createMock( PhotoService.class );
		EasyMock.expect( photoService.load( 777 ) ).andReturn( photo ).anyTimes();
		EasyMock.expect( photoService.load( 666 ) ).andReturn( null ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( photoService );

		final SecurityServiceImpl securityService = getSecurityService();
		securityService.setPhotoService( photoService );

		try {
			securityService.assertPhotoExists( "777" );
			// Photo exists and found - ok
		} catch ( PhotoNotFoundException e ) {
			assertFalse( "Photo exists but not found", true );
		}

		try {
			securityService.assertPhotoExists( "0" );
			assertFalse( "Photo does not exist but found", true );
		} catch ( PhotoNotFoundException e ) {
			// Photo does not exist and not found - ok
		}

		try {
			securityService.assertPhotoExists( "any non-numeric string" );
			assertFalse( "Photo does not exist but found", true );
		} catch ( PhotoNotFoundException e ) {
			// Photo does not exist and not found - ok
		}

		try {
			securityService.assertPhotoExists( "666" );
			assertFalse( "Photo does not exist but found", true );
		} catch ( PhotoNotFoundException e ) {
			// Photo does not exist and not found - ok
		}
	}

	@Test
	public void assertUserExistsTest() {
		final User user = new User();
		user.setId( 333 );

		final UserService userService = EasyMock.createMock( UserService.class );
		EasyMock.expect( userService.load( 333 ) ).andReturn( user ).anyTimes();
		EasyMock.expect( userService.load( 444 ) ).andReturn( null ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( userService );

		final SecurityServiceImpl securityService = getSecurityService();
		securityService.setUserService( userService );

		try {
			securityService.assertUserExists( "333" );
			// User exists and found - ok
		} catch ( UserNotFoundException e ) {
			assertFalse( "User exists but not found", true );
		}

		try {
			securityService.assertUserExists( "0" );
			assertFalse( "User does not exist but found", true );
		} catch ( UserNotFoundException e ) {
			// User does not exist and not found - ok
		}

		try {
			securityService.assertUserExists( "any non-numeric string" );
			assertFalse( "User does not exist but found", true );
		} catch ( UserNotFoundException e ) {
			// User does not exist and not found - ok
		}

		try {
			securityService.assertUserExists( "444" );
			assertFalse( "User does not exist but found", true );
		} catch ( UserNotFoundException e ) {
			// User does not exist and not found - ok
		}
	}

	@Test
	public void assertGenreExistsTest() {
		final Genre genre = new Genre();
		genre.setId( 999 );

		final GenreService genreService = EasyMock.createMock( GenreService.class );
		EasyMock.expect( genreService.load( 999 ) ).andReturn( genre ).anyTimes();
		EasyMock.expect( genreService.load( 888 ) ).andReturn( null ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( genreService );

		final SecurityServiceImpl securityService = getSecurityService();
		securityService.setGenreService( genreService );

		try {
			securityService.assertGenreExists( "999" );
			// Genre exists and found - ok
		} catch ( GenreNotFoundException e ) {
			assertFalse( "Genre exists but not found", true );
		}

		try {
			securityService.assertGenreExists( "0" );
			assertFalse( "Genre does not exist but found", true );
		} catch ( GenreNotFoundException e ) {
			// Genre does not exist and not found - ok
		}

		try {
			securityService.assertGenreExists( "any non-numeric string" );
			assertFalse( "Genre does not exist but found", true );
		} catch ( GenreNotFoundException e ) {
			// Genre does not exist and not found - ok
		}

		try {
			securityService.assertGenreExists( "888" );
			assertFalse( "Genre does not exist but found", true );
		} catch ( GenreNotFoundException e ) {
			// Genre does not exist and not found - ok
		}
	}

	@Test
	public void assertUserCanEditPhotoTest() {
		final User photoAuthor = new User();
		photoAuthor.setId( 333 );

		final User justUser = new User();
		justUser.setId( 444 );

		final Photo photo = new Photo();
		photo.setId( 777 );
		photo.setUserId( 333 );

		final SecurityServiceImpl securityService = getSecurityService();

		try {
			securityService.assertUserCanEditPhoto( photoAuthor, photo );
			// Photo owner can edit his photo - ok
		} catch ( AccessDeniedException e ) {
			assertFalse( "Photo owner can not edit his photo", true );
		}

		try {
			securityService.assertUserCanEditPhoto( justUser, photo );
			assertFalse( "A user can edit not his photo", true );
		} catch ( AccessDeniedException e ) {
			// A user can not edit not his photo - ok
		}
	}

	@Test
	public void assertUserCanEditUserDataTest() {
		final User photoAuthor = new User();
		photoAuthor.setId( 333 );

		final User justUser = new User();
		justUser.setId( 444 );

		final Photo photo = new Photo();
		photo.setId( 777 );
		photo.setUserId( 333 );

		final SecurityServiceImpl securityService = getSecurityService();

		try {
			securityService.assertUserCanEditUserData( photoAuthor, photoAuthor );
			// User can edit his data - ok
		} catch ( AccessDeniedException e ) {
			assertFalse( "User can not edit his data", true );
		}

		try {
			securityService.assertUserCanEditUserData( justUser, photoAuthor );
			assertFalse( "A user can edit not his data", true );
		} catch ( AccessDeniedException e ) {
			// A user can not edit not his data - ok
		}
	}

	@Test
	public void assertUserCanDeletePhotoTest() {
		final User photoAuthor = new User();
		photoAuthor.setId( 333 );

		final User justUser = new User();
		justUser.setId( 444 );

		final Photo photo = new Photo();
		photo.setId( 777 );
		photo.setUserId( 333 );

		final SecurityServiceImpl securityService = getSecurityService();

		try {
			securityService.assertUserCanDeletePhoto( photoAuthor, photo );
			// Photo owner can delete his photo - ok
		} catch ( AccessDeniedException e ) {
			assertFalse( "Photo owner can not delete his photo", true );
		}

		try {
			securityService.assertUserCanDeletePhoto( justUser, photo );
			assertFalse( "A user can delete not his photo", true );
		} catch ( AccessDeniedException e ) {
			// A user can nit delete not his photo - ok
		}
	}

	@Test
	public void isPhotoHasToBeHiddenBecauseOfNudeContentTest() {

		final Photo photoNoNudeContent = new Photo();
		photoNoNudeContent.setId( 777 );
		photoNoNudeContent.setUserId( 333 );

		final Photo photoWithNudeContent = new Photo();
		photoWithNudeContent.setId( 777 );
		photoWithNudeContent.setUserId( 333 );
		photoWithNudeContent.setContainsNudeContent( true );

		final User photoAuthor = new User();
		photoAuthor.setId( 333 );

		final User justUserNoNudeContent = new User();
		justUserNoNudeContent.setId( 444 );

		final User justUserWithNudeContent = new User();
		justUserWithNudeContent.setId( 555 );
		justUserWithNudeContent.setShowNudeContent( true );

		final SecurityService securityService = getSecurityService();

		EnvironmentContext.setEnv( new Environment( photoAuthor ) );

		assertFalse( String.format( "Photo without nude content but nude preview is shown" ), securityService.isPhotoHasToBeHiddenBecauseOfNudeContent( photoNoNudeContent, photoAuthor ) );
		assertFalse( String.format( "Photo without nude content but nude preview is shown" ), securityService.isPhotoHasToBeHiddenBecauseOfNudeContent( photoNoNudeContent, justUserNoNudeContent ) );
		assertFalse( String.format( "Photo without nude content but nude preview is shown" ), securityService.isPhotoHasToBeHiddenBecauseOfNudeContent( photoNoNudeContent, justUserWithNudeContent ) );
		assertFalse( String.format( "Photo without nude content but nude preview is shown" ), securityService.isPhotoHasToBeHiddenBecauseOfNudeContent( photoNoNudeContent, User.NOT_LOGGED_USER ) );

		assertFalse( String.format( "Photo author can not see nude content of his own photo" ), securityService.isPhotoHasToBeHiddenBecauseOfNudeContent( photoWithNudeContent, photoAuthor ) );
		assertTrue( String.format( "User switched off nude content but can see it" ), securityService.isPhotoHasToBeHiddenBecauseOfNudeContent( photoWithNudeContent, justUserNoNudeContent ) );
		assertFalse( String.format( "User switched on nude content but can not see it" ), securityService.isPhotoHasToBeHiddenBecauseOfNudeContent( photoWithNudeContent, justUserWithNudeContent ) );
		assertTrue( String.format( "Not logged user can see nude content" ), securityService.isPhotoHasToBeHiddenBecauseOfNudeContent( photoWithNudeContent, User.NOT_LOGGED_USER ) );

		final Environment env = new Environment( User.NOT_LOGGED_USER );
		env.setShowNudeContent( true );

		EnvironmentContext.setEnv( env );
		assertFalse( String.format( "Not logged user confirmed nude content can not see it" ), securityService.isPhotoHasToBeHiddenBecauseOfNudeContent( photoWithNudeContent, User.NOT_LOGGED_USER ) );

		assertFalse( String.format( "Super Admin can not see nude content" ), securityService.isPhotoHasToBeHiddenBecauseOfNudeContent( photoWithNudeContent, SUPER_MEGA_ADMIN ) );
	}

	@Test
	public void assertCurrentUserWantSeeNudeContentTest() {

		final Photo photoWithNudeContent = new Photo();
		photoWithNudeContent.setId( 777 );
		photoWithNudeContent.setUserId( 333 );
		photoWithNudeContent.setContainsNudeContent( true );

		final User justUserWithNudeContent = new User();
		justUserWithNudeContent.setId( 555 );
		justUserWithNudeContent.setShowNudeContent( true );

		final User justUserNoNudeContent = new User();
		justUserNoNudeContent.setId( 444 );

		final SecurityServiceImpl securityService = getSecurityService();

		try {
			EnvironmentContext.setEnv( new Environment( justUserWithNudeContent ) );
			securityService.assertUserWantSeeNudeContent( justUserWithNudeContent, photoWithNudeContent, "" );
			//User with switched on nude content can see it - ok
		} catch ( NudeContentException e ) {
			assertFalse( "User with switched on nude content can not see it", true );
		}

		try {
			EnvironmentContext.setEnv( new Environment( justUserNoNudeContent ) );
			securityService.assertUserWantSeeNudeContent( justUserNoNudeContent, photoWithNudeContent, "" );
			assertFalse( "User with switched off nude content can see it", true );
		} catch ( NudeContentException e ) {
			// User with switched off nude content can not see it - ok
		}
	}

	private SecurityServiceImpl getSecurityService() {
		final SecurityServiceImpl securityService = new SecurityServiceImpl();
		securityService.setSystemVarsService( systemVarsService );
		securityService.setConfigurationService( configurationService );

		return securityService;
	}
}