package services;

import common.AbstractTestCase;
import core.enums.PhotoActionAllowance;
import core.enums.RestrictionType;
import core.general.configuration.ConfigurationKey;
import core.general.photo.Photo;
import core.general.restriction.EntryRestriction;
import core.general.user.User;
import core.general.user.UserStatus;
import core.services.entry.FavoritesService;
import core.services.photo.PhotoService;
import core.services.security.RestrictionService;
import core.services.security.SecurityServiceImpl;
import core.services.system.ConfigurationService;
import core.services.translator.Language;
import core.services.translator.message.TranslatableMessage;
import mocks.UserMock;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

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

		assertTrue( "Not Logged User Can leave comment", securityService.validateUserCanCommentPhoto( NOT_LOGGED_USER, photo, dateUtilsService.getCurrentTime(), Language.EN ).isValidationFailed() );
	}

	@Test
	public void userFromBlackListCanNotLeaveCommentTest() {

		final int photoAuthorId = 777;
		final int photoAuthorBlackListUserId = 1000;

		final User photoAuthorBlackListUser = new User( photoAuthorBlackListUserId );

		final Photo photo = new Photo();
		photo.setId( 333 );
		photo.setUserId( photoAuthorId );

		final SecurityServiceImpl securityService = getSecurityService();
		securityService.setFavoritesService( getFavoritesService( photoAuthorId, photoAuthorBlackListUserId, true ) );

		assertTrue( "User from photo author's black list Can Leave Comment", securityService.validateUserCanCommentPhoto( photoAuthorBlackListUser, photo, dateUtilsService.getCurrentTime(), Language.EN ).isValidationFailed() );
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

		final FavoritesService favoritesService = getFavoritesService( photoAuthorId, candidateUserId, false );

		final ConfigurationService configurationService = getConfigurationService( false );

		final SecurityServiceImpl securityService = getSecurityService();
		securityService.setFavoritesService( favoritesService );
		securityService.setConfigurationService( configurationService );

		assertTrue( "Candidates Can Leave Comment If It Is Denied On The System Level", securityService.validateUserCanCommentPhoto( candidateUser, photo, dateUtilsService.getCurrentTime(), Language.EN ).isValidationFailed() );
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

		final FavoritesService favoritesService = getFavoritesService( photoAuthorId, userId, false );

		final ConfigurationService configurationService = getConfigurationService( false );

		final PhotoService photoService = EasyMock.createMock( PhotoService.class );
		EasyMock.expect( photoService.getPhotoCommentAllowance( photo ) ).andReturn( PhotoActionAllowance.ACTIONS_DENIED ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( photoService );

		final SecurityServiceImpl securityService = getSecurityService();
		securityService.setFavoritesService( favoritesService );
		securityService.setConfigurationService( configurationService );
		securityService.setPhotoService( photoService );

		assertTrue( "Commenting Is Denied By Author but user can leave comment", securityService.validateUserCanCommentPhoto( user, photo, dateUtilsService.getCurrentTime(), Language.EN ).isValidationFailed() );
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

		final FavoritesService favoritesService = getFavoritesService( photoAuthorId, candidateUserId, false );

		final ConfigurationService configurationService = getConfigurationService( true );

		final PhotoService photoService = EasyMock.createMock( PhotoService.class );
		EasyMock.expect( photoService.getPhotoCommentAllowance( photo ) ).andReturn( PhotoActionAllowance.MEMBERS_ONLY ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( photoService );

		final SecurityServiceImpl securityService = getSecurityService();
		securityService.setFavoritesService( favoritesService );
		securityService.setConfigurationService( configurationService );
		securityService.setPhotoService( photoService );

		assertTrue( "Commenting Is Denied By Author but user can leave comment", securityService.validateUserCanCommentPhoto( candidateUser, photo, dateUtilsService.getCurrentTime(), Language.EN ).isValidationFailed() );
	}

	@Test
	public void commentingIsRestrictedForUserTest() {

		final int photoAuthorId = 777;
		final int candidateUserId = 1000;

		final User candidateUser = new User( candidateUserId );
		candidateUser.setUserStatus( UserStatus.CANDIDATE );

		final Photo photo = new Photo();
		photo.setId( 333 );
		photo.setUserId( photoAuthorId );

		final FavoritesService favoritesService = getFavoritesService( photoAuthorId, candidateUserId, false );

		final ConfigurationService configurationService = getConfigurationService( true );

		final RestrictionService restrictionService = EasyMock.createMock( RestrictionService.class );
		final EntryRestriction restriction = new EntryRestriction<Photo>( photo, RestrictionType.PHOTO_COMMENTING );
		EasyMock.expect( restrictionService.getUserPhotoCommentingRestrictionOn( EasyMock.anyInt(), EasyMock.<Date>anyObject() ) ).andReturn( restriction ).anyTimes();
		EasyMock.expect( restrictionService.getUserRestrictionMessage( restriction ) ).andReturn( new TranslatableMessage( "Commenting is restricted for user", getServices() ) ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( restrictionService );

		final SecurityServiceImpl securityService = getSecurityService();
		securityService.setFavoritesService( favoritesService );
		securityService.setConfigurationService( configurationService );
		securityService.setPhotoService( getPhotoService( photo ) );
		securityService.setRestrictionService( restrictionService );

		assertTrue( "Commenting mustn't be accessible but it is", securityService.validateUserCanCommentPhoto( candidateUser, photo, dateUtilsService.getCurrentTime(), Language.EN ).isValidationFailed() );
	}

	@Test
	public void commentingIsRestrictedForPhotoTest() {

		final int photoAuthorId = 777;
		final int candidateUserId = 1000;

		final User candidateUser = new User( candidateUserId );
		candidateUser.setUserStatus( UserStatus.CANDIDATE );

		final Photo photo = new Photo();
		photo.setId( 333 );
		photo.setUserId( photoAuthorId );

		final FavoritesService favoritesService = getFavoritesService( photoAuthorId, candidateUserId, false );

		final RestrictionService restrictionService = EasyMock.createMock( RestrictionService.class );
		final EntryRestriction restriction = new EntryRestriction<Photo>( photo, RestrictionType.PHOTO_COMMENTING );
		EasyMock.expect( restrictionService.getUserPhotoCommentingRestrictionOn( EasyMock.anyInt(), EasyMock.<Date>anyObject() ) ).andReturn( null ).anyTimes();
		EasyMock.expect( restrictionService.getPhotoCommentingRestrictionOn( EasyMock.anyInt(), EasyMock.<Date>anyObject() ) ).andReturn( restriction ).anyTimes();
		EasyMock.expect( restrictionService.getPhotoRestrictionMessage( restriction ) ).andReturn( new TranslatableMessage( "Commenting is restricted for photo", getServices() ) ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( restrictionService );

		final SecurityServiceImpl securityService = getSecurityService();
		securityService.setFavoritesService( favoritesService );
		securityService.setConfigurationService( getConfigurationService( true ) );
		securityService.setPhotoService( getPhotoService( photo ) );
		securityService.setRestrictionService( restrictionService );

		assertTrue( "Commenting mustn't be accessible but it is", securityService.validateUserCanCommentPhoto( candidateUser, photo, dateUtilsService.getCurrentTime(), Language.EN ).isValidationFailed() );
	}

	@Test
	public void commentingAccessibleTest() {

		final int photoAuthorId = 777;
		final int candidateUserId = 1000;

		final User candidateUser = new User( candidateUserId );
		candidateUser.setUserStatus( UserStatus.CANDIDATE );

		final Photo photo = new Photo();
		photo.setId( 333 );
		photo.setUserId( photoAuthorId );

		final RestrictionService restrictionService = EasyMock.createMock( RestrictionService.class );
		EasyMock.expect( restrictionService.getUserPhotoCommentingRestrictionOn( EasyMock.anyInt(), EasyMock.<Date>anyObject() ) ).andReturn( null ).anyTimes();
		EasyMock.expect( restrictionService.getPhotoCommentingRestrictionOn( EasyMock.anyInt(), EasyMock.<Date>anyObject() ) ).andReturn( null ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( restrictionService );

		final SecurityServiceImpl securityService = getSecurityService();
		securityService.setFavoritesService( getFavoritesService( photoAuthorId, candidateUserId, false ) );
		securityService.setConfigurationService( getConfigurationService( true ) );
		securityService.setPhotoService( getPhotoService( photo ) );
		securityService.setRestrictionService( restrictionService );

		assertTrue( "Commenting must be accessible but it is not", securityService.validateUserCanCommentPhoto( candidateUser, photo, dateUtilsService.getCurrentTime(), Language.EN ).isValidationPassed() );
	}

	@Test
	public void archivedPhotoCanNotBeCommentedByAdminTest() {

		final Photo photo = new Photo();
		photo.setId( 333 );
		photo.setArchived( true );

		final SecurityServiceImpl securityService = getSecurityService();

		assertTrue( "Archived Photo Can Not Be Commented", securityService.validateUserCanCommentPhoto( SUPER_ADMIN_1, photo, dateUtilsService.getCurrentTime(), Language.EN ).isValidationFailed() );
	}

	@Test
	public void archivedPhotoCanNotBeCommentedByPhotoAuthorTest() {

		final UserMock photoAuthor = new UserMock();

		final Photo photo = new Photo();
		photo.setId( 333 );
		photo.setUserId( photoAuthor.getId() );
		photo.setArchived( true );

		final SecurityServiceImpl securityService = getSecurityService();

		assertTrue( "Archived Photo Can Not Be Commented", securityService.validateUserCanCommentPhoto( photoAuthor, photo, dateUtilsService.getCurrentTime(), Language.EN ).isValidationFailed() );
	}

	@Test
	public void archivedPhotoCanNotBeCommentedByUsualUserTest() {

		final UserMock photoAuthor = new UserMock();

		final Photo photo = new Photo();
		photo.setId( 333 );
		photo.setArchived( true );

		final SecurityServiceImpl securityService = getSecurityService();

		assertTrue( "Archived Photo Can Not Be Commented", securityService.validateUserCanCommentPhoto( photoAuthor, photo, dateUtilsService.getCurrentTime(), Language.EN ).isValidationFailed() );
	}

	private FavoritesService getFavoritesService( final int photoAuthorId, final int candidateUserId, final boolean value ) {
		final FavoritesService favoritesService = EasyMock.createMock( FavoritesService.class );
		EasyMock.expect( favoritesService.isUserInBlackListOfUser( photoAuthorId, candidateUserId ) ).andReturn( value ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( favoritesService );
		return favoritesService;
	}

	private ConfigurationService getConfigurationService( final boolean value ) {
		final ConfigurationService configurationService = EasyMock.createMock( ConfigurationService.class );
		EasyMock.expect( configurationService.getBoolean( ConfigurationKey.CANDIDATES_CAN_COMMENT_PHOTOS ) ).andReturn( value ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( configurationService );
		return configurationService;
	}

	private PhotoService getPhotoService( final Photo photo ) {
		final PhotoService photoService = EasyMock.createMock( PhotoService.class );
		EasyMock.expect( photoService.getPhotoCommentAllowance( photo ) ).andReturn( PhotoActionAllowance.CANDIDATES_AND_MEMBERS ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( photoService );
		return photoService;
	}

	private SecurityServiceImpl getSecurityService() {
		final SecurityServiceImpl securityService = new SecurityServiceImpl();
		securityService.setSystemVarsService( systemVarsServiceMock );
		securityService.setTranslatorService( translatorService );

		return securityService;
	}
}
