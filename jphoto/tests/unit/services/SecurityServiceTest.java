package services;

import common.AbstractTestCase;
import core.context.Environment;
import core.context.EnvironmentContext;
import core.exceptions.AccessDeniedException;
import core.exceptions.NudeContentException;
import core.exceptions.notFound.GenreNotFoundException;
import core.exceptions.notFound.PhotoNotFoundException;
import core.exceptions.notFound.UserNotFoundException;
import core.general.configuration.ConfigurationKey;
import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.entry.GenreService;
import core.services.photo.PhotoCommentService;
import core.services.photo.PhotoService;
import core.services.security.SecurityService;
import core.services.security.SecurityServiceImpl;
import core.services.system.ConfigurationService;
import core.services.user.UserService;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class SecurityServiceTest extends AbstractTestCase {

	public static final String MUST_BE_TRUE_BUT_FALSE = "Must be TRUE but FALSE";
	public static final String MUST_BE_FALSE_BUT_TRUE = "Must be FALSE but TRUE";

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

		assertTrue( String.format( MUST_BE_TRUE_BUT_FALSE ), securityService.userCanEditPhoto( photoAuthor, photo ) );
		assertFalse( String.format( MUST_BE_FALSE_BUT_TRUE ), securityService.userCanEditPhoto( justUser, photo ) );
		assertFalse( String.format( MUST_BE_FALSE_BUT_TRUE ), securityService.userCanEditPhoto( User.NOT_LOGGED_USER, photo ) );
		assertFalse( String.format( MUST_BE_FALSE_BUT_TRUE ), securityService.userCanEditPhoto( SUPER_MEGA_ADMIN, photo ) );
		assertFalse( String.format( MUST_BE_FALSE_BUT_TRUE ), getSecurityService( ADMIN_CAN_EDIT_USER_DATA ).userCanEditPhoto( SUPER_MEGA_ADMIN, photo ) );
		assertFalse( String.format( MUST_BE_FALSE_BUT_TRUE ), getSecurityService( ADMIN_CAN_DELETE_PHOTO ).userCanEditPhoto( SUPER_MEGA_ADMIN, photo ) );
		assertTrue( String.format( MUST_BE_TRUE_BUT_FALSE ), getSecurityService( ADMIN_CAN_EDIT_PHOTO ).userCanEditPhoto( SUPER_MEGA_ADMIN, photo ) );
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

		assertTrue( String.format( MUST_BE_TRUE_BUT_FALSE ), securityService.userCanDeletePhoto( photoAuthor, photo ) );
		assertFalse( String.format( MUST_BE_FALSE_BUT_TRUE ), securityService.userCanDeletePhoto( justUser, photo ) );
		assertFalse( String.format( MUST_BE_FALSE_BUT_TRUE ), securityService.userCanDeletePhoto( User.NOT_LOGGED_USER, photo ) );
		assertFalse( String.format( MUST_BE_FALSE_BUT_TRUE ), securityService.userCanDeletePhoto( SUPER_MEGA_ADMIN, photo ) );
		assertFalse( String.format( MUST_BE_FALSE_BUT_TRUE ), getSecurityService( ADMIN_CAN_EDIT_PHOTO ).userCanDeletePhoto( SUPER_MEGA_ADMIN, photo ) );
		assertFalse( String.format( MUST_BE_FALSE_BUT_TRUE ), getSecurityService( ADMIN_CAN_EDIT_USER_DATA ).userCanDeletePhoto( SUPER_MEGA_ADMIN, photo ) );
		assertTrue( String.format( MUST_BE_TRUE_BUT_FALSE ), getSecurityService( ADMIN_CAN_DELETE_PHOTO ).userCanDeletePhoto( SUPER_MEGA_ADMIN, photo ) );
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

		assertTrue( String.format( MUST_BE_TRUE_BUT_FALSE ), securityService.userCanVoteForPhoto( justUser, photo ) );
		assertFalse( String.format( MUST_BE_FALSE_BUT_TRUE ), securityService.userCanVoteForPhoto( photoAuthor, photo ) );
		assertFalse( String.format( MUST_BE_FALSE_BUT_TRUE ), securityService.userCanVoteForPhoto( User.NOT_LOGGED_USER, photo ) );
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

		assertTrue( String.format( MUST_BE_TRUE_BUT_FALSE ), securityService.userOwnThePhoto( photoAuthor, photo ) );
		assertFalse( String.format( MUST_BE_FALSE_BUT_TRUE ), securityService.userOwnThePhoto( justUser, photo ) );
		assertFalse( String.format( MUST_BE_FALSE_BUT_TRUE ), securityService.userOwnThePhoto( User.NOT_LOGGED_USER, photo ) );
	}

	@Test
	public void userCanEditUserDataTest() {

		final User photoAuthor = new User();
		photoAuthor.setId( 111 );

		final User justUser = new User();
		justUser.setId( 222 );

		final SecurityService securityService = getSecurityService();

		assertTrue( String.format( MUST_BE_TRUE_BUT_FALSE ), securityService.userCanEditUserData( photoAuthor, photoAuthor ) );
		assertFalse( String.format( MUST_BE_FALSE_BUT_TRUE ), securityService.userCanEditUserData( justUser, photoAuthor ) );
		assertFalse( String.format( MUST_BE_FALSE_BUT_TRUE ), securityService.userCanEditUserData( User.NOT_LOGGED_USER, photoAuthor ) );
		assertFalse( String.format( MUST_BE_FALSE_BUT_TRUE ), securityService.userCanEditUserData( SUPER_MEGA_ADMIN, photoAuthor ) );
		assertFalse( String.format( MUST_BE_FALSE_BUT_TRUE ), getSecurityService( ADMIN_CAN_DELETE_PHOTO ).userCanEditUserData( SUPER_MEGA_ADMIN, photoAuthor ) );
		assertFalse( String.format( MUST_BE_FALSE_BUT_TRUE ), getSecurityService( ADMIN_CAN_EDIT_PHOTO ).userCanEditUserData( SUPER_MEGA_ADMIN, photoAuthor ) );
		assertTrue( String.format( MUST_BE_TRUE_BUT_FALSE ), getSecurityService( ADMIN_CAN_EDIT_USER_DATA ).userCanEditUserData( SUPER_MEGA_ADMIN, photoAuthor ) );
	}

	@Test
	public void userCanSeeUserRankVoteHistory() {
		final User historyOwner = new User();
		historyOwner.setId( 111 );

		final User visitor = new User();
		visitor.setId( 222 );

		final SecurityService securityService = getSecurityService();
		assertTrue( String.format( MUST_BE_TRUE_BUT_FALSE ), securityService.userCanSeeUserRankVoteHistory( historyOwner, historyOwner ) );
		assertFalse( String.format( MUST_BE_FALSE_BUT_TRUE ), securityService.userCanSeeUserRankVoteHistory( historyOwner, visitor ) );
		assertFalse( String.format( MUST_BE_FALSE_BUT_TRUE ), securityService.userCanSeeUserRankVoteHistory( historyOwner, User.NOT_LOGGED_USER ) );
		assertTrue( String.format( MUST_BE_TRUE_BUT_FALSE ), securityService.userCanSeeUserRankVoteHistory( historyOwner, SUPER_MEGA_ADMIN ) );
	}

	@Test
	public void userCanDeletePhotoComment() {
		final User commentAuthor = new User();
		commentAuthor.setId( 111 );

		final User justUser = new User();
		justUser.setId( 222 );

		final User photoAuthor = new User();
		photoAuthor.setId( 333 );

		final Photo photo = new Photo();
		photo.setId( 777 );
		photo.setUserId( 111 );
		photo.setUserId( photoAuthor.getId() );

		final PhotoComment comment = new PhotoComment();
		comment.setId( 123 );
		comment.setCommentAuthor( commentAuthor );
		comment.setPhotoId( photo.getId() );

		final PhotoCommentService photoCommentService = EasyMock.createMock( PhotoCommentService.class );
		EasyMock.expect( photoCommentService.load( EasyMock.anyInt() ) ).andReturn( comment ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( photoCommentService );

		final SecurityServiceImpl securityService = getSecurityService();
		securityService.setPhotoCommentService( photoCommentService );
		securityService.setPhotoService( getPhotoService( photo ) );

		securityService.setUserService( getUserService( commentAuthor ) );
		assertTrue( String.format( MUST_BE_TRUE_BUT_FALSE ), securityService.userCanDeletePhotoComment( commentAuthor, comment ) );

		securityService.setUserService( getUserService( photoAuthor ) );
		assertTrue( String.format( MUST_BE_TRUE_BUT_FALSE ), securityService.userCanDeletePhotoComment( photoAuthor, comment ) );

		securityService.setUserService( getUserService( justUser ) );
		assertFalse( String.format( MUST_BE_FALSE_BUT_TRUE ), securityService.userCanDeletePhotoComment( justUser, comment ) );

		securityService.setUserService( getUserService( User.NOT_LOGGED_USER ) );
		assertFalse( String.format( MUST_BE_FALSE_BUT_TRUE ), securityService.userCanDeletePhotoComment( User.NOT_LOGGED_USER, comment ) );

		securityService.setUserService( getUserService( SUPER_MEGA_ADMIN ) );
		assertTrue( String.format( MUST_BE_TRUE_BUT_FALSE ), securityService.userCanDeletePhotoComment( SUPER_MEGA_ADMIN, comment ) );
	}

	@Test
	public void isSuperAdminUser() {
		final SecurityServiceImpl securityService = getSecurityService();

		assertTrue( String.format( MUST_BE_TRUE_BUT_FALSE ), securityService.isSuperAdminUser( SUPER_MEGA_ADMIN.getId() ) );
		assertTrue( String.format( MUST_BE_TRUE_BUT_FALSE ), securityService.isSuperAdminUser( SUPER_ADMIN.getId() ) );
		assertFalse( String.format( MUST_BE_FALSE_BUT_TRUE ), securityService.isSuperAdminUser( 111 ) );
	}

	@Test
	public void getSuperAdminUsers() {
		final SecurityServiceImpl securityService = getSecurityService();

		final UserService userService = EasyMock.createMock( UserService.class );
		EasyMock.expect( userService.load( SUPER_MEGA_ADMIN.getId() ) ).andReturn( SUPER_MEGA_ADMIN ).anyTimes();
		EasyMock.expect( userService.load( SUPER_ADMIN.getId() ) ).andReturn( SUPER_ADMIN ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( userService );

		securityService.setUserService( userService );

		final List<User> superAdminUsers = securityService.getSuperAdminUsers();

		assertTrue( String.format( MUST_BE_TRUE_BUT_FALSE ), superAdminUsers.size() == 2 );
		assertTrue( String.format( MUST_BE_TRUE_BUT_FALSE ), superAdminUsers.get( 0 ).getId() == SUPER_MEGA_ADMIN.getId() );
		assertTrue( String.format( MUST_BE_TRUE_BUT_FALSE ), superAdminUsers.get( 1 ).getId() == SUPER_ADMIN.getId() );
	}

	@Test
	public void isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod() {

		final User commentAuthor = new User( 222 );
		final User photoAuthor = new User( 444 );
		final User justUser = new User( 555 );

		final Photo photo = new Photo();
		photo.setId( 777 );
		photo.setUserId( 111 );
		photo.setUserId( photoAuthor.getId() );
		photo.setAnonymousPosting( true );
		photo.setUploadTime( dateUtilsService.getCurrentTime() );

		final PhotoComment comment = new PhotoComment();
		comment.setId( 123 );
		comment.setCommentAuthor( commentAuthor );
		comment.setPhotoId( photo.getId() );

		final SecurityServiceImpl securityService = getSecurityService();

		final UserService userService = EasyMock.createMock( UserService.class );
		EasyMock.expect( userService.load( SUPER_MEGA_ADMIN.getId() ) ).andReturn( SUPER_MEGA_ADMIN ).anyTimes();
		EasyMock.expect( userService.load( commentAuthor.getId() ) ).andReturn( commentAuthor ).anyTimes();
		EasyMock.expect( userService.load( photoAuthor.getId() ) ).andReturn( photoAuthor ).anyTimes();
		EasyMock.expect( userService.load( justUser.getId() ) ).andReturn( justUser ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( userService );

		securityService.setUserService( userService );

		final PhotoService photoService = EasyMock.createMock( PhotoService.class );
		EasyMock.expect( photoService.load( photo.getId() ) ).andReturn( photo ).anyTimes();
		EasyMock.expect( photoService.isPhotoAuthorNameMustBeHidden( EasyMock.<Photo>anyObject(), EasyMock.<User>anyObject() ) ).andReturn( false ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( photoService );

		securityService.setPhotoService( photoService );

		assertFalse( String.format( MUST_BE_FALSE_BUT_TRUE ), securityService.isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod( comment, commentAuthor ) );
		assertFalse( String.format( MUST_BE_FALSE_BUT_TRUE ), securityService.isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod( comment, photoAuthor ) );
		assertFalse( String.format( MUST_BE_FALSE_BUT_TRUE ), securityService.isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod( comment, SUPER_MEGA_ADMIN ) );
		assertFalse( String.format( MUST_BE_FALSE_BUT_TRUE ), securityService.isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod( comment, justUser ) );

		final PhotoService photoService1 = EasyMock.createMock( PhotoService.class );
		EasyMock.expect( photoService1.load( photo.getId() ) ).andReturn( photo ).anyTimes();
		EasyMock.expect( photoService1.isPhotoAuthorNameMustBeHidden( EasyMock.<Photo>anyObject(), EasyMock.<User>anyObject() ) ).andReturn( true ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( photoService1 );

		securityService.setPhotoService( photoService1 );

		assertFalse( String.format( MUST_BE_FALSE_BUT_TRUE ), securityService.isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod( comment, commentAuthor ) );
		assertFalse( String.format( MUST_BE_FALSE_BUT_TRUE ), securityService.isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod( comment, photoAuthor ) );
		assertTrue( String.format( MUST_BE_TRUE_BUT_FALSE ), securityService.isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod( comment, SUPER_MEGA_ADMIN ) );
		assertTrue( String.format( MUST_BE_TRUE_BUT_FALSE ), securityService.isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod( comment, justUser ) );
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
		return getSecurityService( ADMIN_CAN_NOTHING );
	}

	private SecurityServiceImpl getSecurityService( final ConfigKeys configKeys ) {
		final SecurityServiceImpl securityService = new SecurityServiceImpl();
		securityService.setSystemVarsService( systemVarsServiceMock );
		securityService.setConfigurationService( getConfigurationService( configKeys ) );

		return securityService;
	}

	private ConfigurationService getConfigurationService( final ConfigKeys configKeys ) {
		final ConfigurationService configurationService = EasyMock.createMock( ConfigurationService.class );

		EasyMock.expect( configurationService.getBoolean( ConfigurationKey.ADMIN_CAN_EDIT_OTHER_PHOTOS ) ).andReturn( configKeys.isAdminCanEditPhotosOfOtherUsers() ).anyTimes();
		EasyMock.expect( configurationService.getBoolean( ConfigurationKey.ADMIN_CAN_DELETE_OTHER_PHOTOS ) ).andReturn( configKeys.isAdminCanDeletePhotosOfOtherUsers() ).anyTimes();
		EasyMock.expect( configurationService.getBoolean( ConfigurationKey.ADMIN_CAN_EDIT_OTHER_USER_DATA ) ).andReturn( configKeys.isAdminCanEditOtherUserData() ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( configurationService );

		return configurationService;
	}

	private UserService getUserService( final User user ) {
		final UserService userService = EasyMock.createMock( UserService.class );
		EasyMock.expect( userService.load( user.getId() ) ).andReturn( user ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( userService );

		return userService;
	}

	private PhotoService getPhotoService( final Photo photo ) {
		final PhotoService photoService = EasyMock.createMock( PhotoService.class );
		EasyMock.expect( photoService.load( photo.getId() ) ).andReturn( photo ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( photoService );
		return photoService;
	}

	private final ConfigKeys ADMIN_CAN_NOTHING = new ConfigKeys();

	private final ConfigKeys ADMIN_CAN_EDIT_PHOTO = new ConfigKeys() {
		@Override
		boolean isAdminCanEditPhotosOfOtherUsers() {
			return true;
		}
	};

	private final ConfigKeys ADMIN_CAN_DELETE_PHOTO = new ConfigKeys() {
		@Override
		boolean isAdminCanDeletePhotosOfOtherUsers() {
			return true;
		}
	};

	private final ConfigKeys ADMIN_CAN_EDIT_USER_DATA = new ConfigKeys() {
		@Override
		boolean isAdminCanEditOtherUserData() {
			return true;
		}
	};

	private class ConfigKeys {

		boolean isAdminCanEditPhotosOfOtherUsers() {
			return false;
		}

		boolean isAdminCanDeletePhotosOfOtherUsers() {
			return false;
		}

		boolean isAdminCanEditOtherUserData() {
			return false;
		}
	}
}