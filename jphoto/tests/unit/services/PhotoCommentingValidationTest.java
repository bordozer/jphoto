package services;

import common.AbstractTestCase;
import core.enums.PhotoActionAllowance;
import core.general.configuration.ConfigurationKey;
import core.general.photo.Photo;
import core.general.user.User;
import core.general.user.UserStatus;
import core.services.entry.FavoritesService;
import core.services.photo.PhotoService;
import core.services.security.SecurityServiceImpl;
import core.services.system.ConfigurationService;
import core.services.translator.Language;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

public class PhotoCommentingValidationTest extends AbstractTestCase {

	@Before
	public void setup() {
		super.setup();
	}

	@Test
	public void notLoggedUserCanNotLeaveCommentTest() {

		final Photo photo = new Photo();
		photo.setId( 333 );

		final SecurityServiceImpl securityService = getSecurityService();

		assertTrue( "Not Logged User Can leave comment", securityService.validateUserCanCommentPhoto( NOT_LOGGED_USER, photo, Language.EN ).isValidationFailed() );
	}

	@Test
	public void userFromBlackListCanNotLeaveCommentTest() {

		final int photoAuthorId = 777;
		final int photoAuthorBlackListUserId = 1000;

		final User photoAuthorBlackListUser = new User( photoAuthorBlackListUserId );

		final Photo photo = new Photo();
		photo.setId( 333 );
		photo.setUserId( photoAuthorId );

		final FavoritesService favoritesService = EasyMock.createMock( FavoritesService.class );
		EasyMock.expect( favoritesService.isUserInBlackListOfUser( photoAuthorId, photoAuthorBlackListUserId ) ).andReturn( true ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( favoritesService );


		final SecurityServiceImpl securityService = getSecurityService();
		securityService.setFavoritesService( favoritesService );

		assertTrue( "User from photo author's black list Can Leave Comment", securityService.validateUserCanCommentPhoto( photoAuthorBlackListUser, photo, Language.EN ).isValidationFailed() );
	}

	@Test
	public void candidatesCanNotLeaveCommentsIfItIsDeniedOnTheSystemLevelTest() {

		final int photoAuthorId = 777;
		final int candidateUserId = 1000;

		final User candidateUser = new User( candidateUserId );
		candidateUser.setUserStatus( UserStatus.CANDIDATE );

		final Photo photo = new Photo();
		photo.setId( 333 );
		photo.setUserId( photoAuthorId );

		final FavoritesService favoritesService = EasyMock.createMock( FavoritesService.class );
		EasyMock.expect( favoritesService.isUserInBlackListOfUser( photoAuthorId, candidateUserId ) ).andReturn( false ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( favoritesService );

		final ConfigurationService configurationService = EasyMock.createMock( ConfigurationService.class );
		EasyMock.expect( configurationService.getBoolean( ConfigurationKey.CANDIDATES_CAN_COMMENT_PHOTOS ) ).andReturn( false ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( configurationService );

		final SecurityServiceImpl securityService = getSecurityService();
		securityService.setFavoritesService( favoritesService );
		securityService.setConfigurationService( configurationService );

		assertTrue( "Candidates Can Leave Comment If It Is Denied On The System Level", securityService.validateUserCanCommentPhoto( candidateUser, photo, Language.EN ).isValidationFailed() );
	}

	@Test
	public void commentingIsDeniedByAuthorTest() {

		final int photoAuthorId = 777;
		final int userId = 1000;

		final User user = new User( userId );
		user.setUserStatus( UserStatus.MEMBER );

		final Photo photo = new Photo();
		photo.setId( 333 );
		photo.setUserId( photoAuthorId );

		final FavoritesService favoritesService = EasyMock.createMock( FavoritesService.class );
		EasyMock.expect( favoritesService.isUserInBlackListOfUser( photoAuthorId, userId ) ).andReturn( false ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( favoritesService );

		final ConfigurationService configurationService = EasyMock.createMock( ConfigurationService.class );
		EasyMock.expect( configurationService.getBoolean( ConfigurationKey.CANDIDATES_CAN_COMMENT_PHOTOS ) ).andReturn( false ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( configurationService );

		final PhotoService photoService = EasyMock.createMock( PhotoService.class );
		EasyMock.expect( photoService.getPhotoCommentAllowance( photo ) ).andReturn( PhotoActionAllowance.ACTIONS_DENIED ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( photoService );

		final SecurityServiceImpl securityService = getSecurityService();
		securityService.setFavoritesService( favoritesService );
		securityService.setConfigurationService( configurationService );
		securityService.setPhotoService( photoService );

		assertTrue( "Commenting Is Denied By Author but user can leave comment", securityService.validateUserCanCommentPhoto( user, photo, Language.EN ).isValidationFailed() );
	}

	@Test
	public void commentingForCandidatesIsDeniedByAuthorTest() {

		final int photoAuthorId = 777;
		final int candidateUserId = 1000;

		final User candidateUser = new User( candidateUserId );
		candidateUser.setUserStatus( UserStatus.CANDIDATE );

		final Photo photo = new Photo();
		photo.setId( 333 );
		photo.setUserId( photoAuthorId );

		final FavoritesService favoritesService = EasyMock.createMock( FavoritesService.class );
		EasyMock.expect( favoritesService.isUserInBlackListOfUser( photoAuthorId, candidateUserId ) ).andReturn( false ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( favoritesService );

		final ConfigurationService configurationService = EasyMock.createMock( ConfigurationService.class );
		EasyMock.expect( configurationService.getBoolean( ConfigurationKey.CANDIDATES_CAN_COMMENT_PHOTOS ) ).andReturn( true ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( configurationService );

		final PhotoService photoService = EasyMock.createMock( PhotoService.class );
		EasyMock.expect( photoService.getPhotoCommentAllowance( photo ) ).andReturn( PhotoActionAllowance.MEMBERS_ONLY ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( photoService );

		final SecurityServiceImpl securityService = getSecurityService();
		securityService.setFavoritesService( favoritesService );
		securityService.setConfigurationService( configurationService );
		securityService.setPhotoService( photoService );

		assertTrue( "Commenting Is Denied By Author but user can leave comment", securityService.validateUserCanCommentPhoto( candidateUser, photo, Language.EN ).isValidationFailed() );
	}

	private SecurityServiceImpl getSecurityService() {
		final SecurityServiceImpl securityService = new SecurityServiceImpl();
		securityService.setSystemVarsService( systemVarsServiceMock );
		securityService.setTranslatorService( translatorService );

		return securityService;
	}
}
