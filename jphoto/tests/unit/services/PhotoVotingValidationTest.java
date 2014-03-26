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
import core.services.user.UserService;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

public class PhotoVotingValidationTest extends AbstractTestCase {

	@Before
	public void setup() {
		super.setup();
	}

	@Test
	public void notLoggedUserCanNotVoteTest() {

		final Photo photo = new Photo();
		photo.setId( 333 );

		final SecurityServiceImpl securityService = getSecurityService();

		assertTrue( "Not Logged User Can vote", securityService.validateUserCanVoteForPhoto( User.NOT_LOGGED_USER, photo, Language.EN ).isValidationFailed() );
	}

	@Test
	public void userCanNotVoteForHisOwnPhotoTest() {

		final int photoAuthorId = 777;
		final User photoAuthor = new User( photoAuthorId );

		final Photo photo = new Photo();
		photo.setId( 333 );
		photo.setUserId( photoAuthorId );

		final UserService userService = EasyMock.createMock( UserService.class );
		EasyMock.expect( userService.load( photoAuthorId ) ).andReturn( photoAuthor ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( userService );

		final SecurityServiceImpl securityService = getSecurityService();
		securityService.setUserService( userService );

		assertTrue( "User Can  Vote For His Own Photo", securityService.validateUserCanVoteForPhoto( photoAuthor, photo, Language.EN ).isValidationFailed() );
	}

	@Test
	public void userFromBlackListOfPhotoAuthorCanNotVoteTest() {

		final int photoAuthorId = 777;
		final int blackListUserId = 1000;

		final User photoAuthor = new User( photoAuthorId );
		final User blackListUser = new User( blackListUserId );

		final Photo photo = new Photo();
		photo.setId( 333 );
		photo.setUserId( photoAuthorId );

		final UserService userService = EasyMock.createMock( UserService.class );
		EasyMock.expect( userService.load( photoAuthorId ) ).andReturn( photoAuthor ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( userService );

		final FavoritesService favoritesService = EasyMock.createMock( FavoritesService.class );
		EasyMock.expect( favoritesService.isUserInBlackListOfUser( photoAuthorId, blackListUserId ) ).andReturn( true ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( favoritesService );

		final SecurityServiceImpl securityService = getSecurityService();
		securityService.setUserService( userService );
		securityService.setFavoritesService( favoritesService );

		assertTrue( "User From Black List Of Photo Author Can Vote", securityService.validateUserCanVoteForPhoto( blackListUser, photo, Language.EN ).isValidationFailed() );
	}

	@Test
	public void candidatesCanNotVoteIfItIsDeniedOnTheSystemLevelTest() {

		final int photoAuthorId = 777;
		final int candidateUserId = 1000;

		final User photoAuthor = new User( photoAuthorId );

		final User candidateUser = new User( candidateUserId );
		candidateUser.setUserStatus( UserStatus.CANDIDATE );

		final Photo photo = new Photo();
		photo.setId( 333 );
		photo.setUserId( photoAuthorId );

		final UserService userService = EasyMock.createMock( UserService.class );
		EasyMock.expect( userService.load( photoAuthorId ) ).andReturn( photoAuthor ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( userService );

		final FavoritesService favoritesService = EasyMock.createMock( FavoritesService.class );
		EasyMock.expect( favoritesService.isUserInBlackListOfUser( photoAuthorId, candidateUserId ) ).andReturn( false ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( favoritesService );

		final ConfigurationService configurationService = EasyMock.createMock( ConfigurationService.class );
		EasyMock.expect( configurationService.getBoolean( ConfigurationKey.CANDIDATES_CAN_VOTE_FOR_PHOTOS ) ).andReturn( false ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( configurationService );

		final SecurityServiceImpl securityService = getSecurityService();
		securityService.setUserService( userService );
		securityService.setFavoritesService( favoritesService );
		securityService.setConfigurationService( configurationService );

		assertTrue( "Candidates Can Vote If It Is Denied On The System Level", securityService.validateUserCanVoteForPhoto( candidateUser, photo, Language.EN ).isValidationFailed() );
	}

	@Test
	public void votingIsDeniedByAuthorTest() {

		final int photoAuthorId = 777;
		final int userId = 1000;

		final User photoAuthor = new User( photoAuthorId );

		final User user = new User( userId );
		user.setUserStatus( UserStatus.CANDIDATE );

		final Photo photo = new Photo();
		photo.setId( 333 );
		photo.setUserId( photoAuthorId );

		final UserService userService = EasyMock.createMock( UserService.class );
		EasyMock.expect( userService.load( photoAuthorId ) ).andReturn( photoAuthor ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( userService );

		final FavoritesService favoritesService = EasyMock.createMock( FavoritesService.class );
		EasyMock.expect( favoritesService.isUserInBlackListOfUser( photoAuthorId, userId ) ).andReturn( false ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( favoritesService );

		final ConfigurationService configurationService = EasyMock.createMock( ConfigurationService.class );
		EasyMock.expect( configurationService.getBoolean( ConfigurationKey.CANDIDATES_CAN_VOTE_FOR_PHOTOS ) ).andReturn( true ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( configurationService );

		final PhotoService photoService = EasyMock.createMock( PhotoService.class );
		EasyMock.expect( photoService.getPhotoVotingAllowance( photo ) ).andReturn( PhotoActionAllowance.ACTIONS_DENIED ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( photoService );

		final SecurityServiceImpl securityService = getSecurityService();
		securityService.setUserService( userService );
		securityService.setFavoritesService( favoritesService );
		securityService.setConfigurationService( configurationService );
		securityService.setPhotoService( photoService );

		assertTrue( "Voting Is Denied By Author but user can leave comment", securityService.validateUserCanVoteForPhoto( user, photo, Language.EN ).isValidationFailed() );
	}

	@Test
	public void votingForCandidatesIsDeniedByAuthorTest() {

		final int photoAuthorId = 777;
		final int candidateUserId = 1000;

		final User photoAuthor = new User( photoAuthorId );

		final User candidateUser = new User( candidateUserId );
		candidateUser.setUserStatus( UserStatus.CANDIDATE );

		final Photo photo = new Photo();
		photo.setId( 333 );
		photo.setUserId( photoAuthorId );

		final UserService userService = EasyMock.createMock( UserService.class );
		EasyMock.expect( userService.load( photoAuthorId ) ).andReturn( photoAuthor ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( userService );

		final FavoritesService favoritesService = EasyMock.createMock( FavoritesService.class );
		EasyMock.expect( favoritesService.isUserInBlackListOfUser( photoAuthorId, candidateUserId ) ).andReturn( false ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( favoritesService );

		final ConfigurationService configurationService = EasyMock.createMock( ConfigurationService.class );
		EasyMock.expect( configurationService.getBoolean( ConfigurationKey.CANDIDATES_CAN_VOTE_FOR_PHOTOS ) ).andReturn( true ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( configurationService );

		final PhotoService photoService = EasyMock.createMock( PhotoService.class );
		EasyMock.expect( photoService.getPhotoVotingAllowance( photo ) ).andReturn( PhotoActionAllowance.MEMBERS_ONLY ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( photoService );

		final SecurityServiceImpl securityService = getSecurityService();
		securityService.setUserService( userService );
		securityService.setFavoritesService( favoritesService );
		securityService.setConfigurationService( configurationService );
		securityService.setPhotoService( photoService );

		assertTrue( "Commenting Is Denied By Author but user can leave comment", securityService.validateUserCanVoteForPhoto( candidateUser, photo, Language.EN ).isValidationFailed() );
	}

	private SecurityServiceImpl getSecurityService() {
		final SecurityServiceImpl securityService = new SecurityServiceImpl();

		securityService.setTranslatorService( translatorService );

		return securityService;
	}
}
