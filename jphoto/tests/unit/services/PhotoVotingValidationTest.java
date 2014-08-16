package services;

import common.AbstractTestCase;
import core.enums.PhotoActionAllowance;
import core.enums.RestrictionType;
import core.general.configuration.ConfigurationKey;
import core.general.genre.Genre;
import core.general.photo.Photo;
import core.general.restriction.EntryRestriction;
import core.general.user.User;
import core.general.user.UserStatus;
import core.services.entry.FavoritesService;
import core.services.entry.GenreService;
import core.services.photo.PhotoService;
import core.services.security.RestrictionService;
import core.services.security.SecurityServiceImpl;
import core.services.system.ConfigurationService;
import core.services.translator.Language;
import core.services.translator.message.TranslatableMessage;
import core.services.user.UserRankService;
import core.services.user.UserService;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

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

		assertTrue( "Not Logged User Can vote", securityService.validateUserCanVoteForPhoto( NOT_LOGGED_USER, photo, dateUtilsService.getCurrentTime(), Language.EN ).isValidationFailed() );
	}

	@Test
	public void userCanNotVoteForHisOwnPhotoTest() {

		final int photoAuthorId = 777;
		final User photoAuthor = new User( photoAuthorId );

		final Photo photo = new Photo();
		photo.setId( 333 );
		photo.setUserId( photoAuthorId );

		final SecurityServiceImpl securityService = getSecurityService();
		securityService.setUserService( getUserService( photoAuthorId, photoAuthor ) );

		assertTrue( "User Can  Vote For His Own Photo", securityService.validateUserCanVoteForPhoto( photoAuthor, photo, dateUtilsService.getCurrentTime(), Language.EN ).isValidationFailed() );
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

		final SecurityServiceImpl securityService = getSecurityService();
		securityService.setUserService( getUserService( photoAuthorId, photoAuthor ) );
		securityService.setFavoritesService( getFavoritesService( photoAuthorId, blackListUserId, true ) );

		assertTrue( "User From Black List Of Photo Author Can Vote", securityService.validateUserCanVoteForPhoto( blackListUser, photo, dateUtilsService.getCurrentTime(), Language.EN ).isValidationFailed() );
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

		final SecurityServiceImpl securityService = getSecurityService();
		securityService.setUserService( getUserService( photoAuthorId, photoAuthor ) );
		securityService.setFavoritesService( getFavoritesService( photoAuthorId, candidateUserId, false ) );
		securityService.setConfigurationService( getConfigurationService( false ) );

		assertTrue( "Candidates Can Vote If It Is Denied On The System Level", securityService.validateUserCanVoteForPhoto( candidateUser, photo, dateUtilsService.getCurrentTime(), Language.EN ).isValidationFailed() );
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

		final SecurityServiceImpl securityService = getSecurityService();
		securityService.setUserService( getUserService( photoAuthorId, photoAuthor ) );
		securityService.setFavoritesService( getFavoritesService( photoAuthorId, userId, false ) );
		securityService.setConfigurationService( getConfigurationService( true ) );
		securityService.setPhotoService( getPhotoService( photo, PhotoActionAllowance.ACTIONS_DENIED ) );

		assertTrue( "Voting Is Denied By Author but user can leave comment", securityService.validateUserCanVoteForPhoto( user, photo, dateUtilsService.getCurrentTime(), Language.EN ).isValidationFailed() );
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

		final SecurityServiceImpl securityService = getSecurityService();
		securityService.setUserService( getUserService( photoAuthorId, photoAuthor ) );
		securityService.setFavoritesService( getFavoritesService( photoAuthorId, candidateUserId, false ) );
		securityService.setConfigurationService( getConfigurationService( true ) );
		securityService.setPhotoService( getPhotoService( photo, PhotoActionAllowance.MEMBERS_ONLY ) );

		assertTrue( "Commenting Is Denied By Author but user can leave comment", securityService.validateUserCanVoteForPhoto( candidateUser, photo, dateUtilsService.getCurrentTime(), Language.EN ).isValidationFailed() );
	}

	@Test
	public void votingIsInaccessibleIfUserRankIsNegativeTest() {

		final int photoAuthorId = 777;
		final int candidateUserId = 1000;
		final int userRankInGenre = -1; // Negative!

		final User photoAuthor = new User( photoAuthorId );

		final User candidateUser = new User( candidateUserId );
		candidateUser.setUserStatus( UserStatus.CANDIDATE );

		final Photo photo = new Photo();
		photo.setId( 333 );
		photo.setUserId( photoAuthorId );
		photo.setGenreId( 321 );

		final UserRankService userRankService = EasyMock.createMock( UserRankService.class );
		EasyMock.expect( userRankService.getUserRankInGenre( candidateUserId, photo.getGenreId() ) ).andReturn( userRankInGenre ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( userRankService );

		final GenreService genreService = EasyMock.createMock( GenreService.class );
		EasyMock.expect( genreService.load( photo.getGenreId() ) ).andReturn( new Genre(  ) ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( genreService );

		final SecurityServiceImpl securityService = getSecurityService();
		securityService.setUserService( getUserService( photoAuthorId, photoAuthor ) );
		securityService.setFavoritesService( getFavoritesService( photoAuthorId, candidateUserId, false ) );
		securityService.setConfigurationService( getConfigurationService( true ) );
		securityService.setPhotoService( getPhotoService( photo, PhotoActionAllowance.CANDIDATES_AND_MEMBERS ) );
		securityService.setUserRankService( userRankService );
		securityService.setGenreService( genreService );
		securityService.setEntityLinkUtilsService( entityLinkUtilsService );

		assertTrue( "Commenting must NOT be accessible but it is", securityService.validateUserCanVoteForPhoto( candidateUser, photo, dateUtilsService.getCurrentTime(), Language.EN ).isValidationFailed() );
	}

	@Test
	public void votingIsInaccessibleIfVotingRestrictedForUserTest() {

		final int photoAuthorId = 777;
		final int candidateUserId = 1000;
		final int userRankInGenre = 1;

		final User photoAuthor = new User( photoAuthorId );

		final User candidateUser = new User( candidateUserId );
		candidateUser.setUserStatus( UserStatus.CANDIDATE );

		final Photo photo = new Photo();
		photo.setId( 333 );
		photo.setUserId( photoAuthorId );
		photo.setGenreId( 321 );

		final UserRankService userRankService = EasyMock.createMock( UserRankService.class );
		EasyMock.expect( userRankService.getUserRankInGenre( candidateUserId, photo.getGenreId() ) ).andReturn( userRankInGenre ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( userRankService );

		final RestrictionService restrictionService = EasyMock.createMock( RestrictionService.class );
		final EntryRestriction restriction = new EntryRestriction<User>( candidateUser, RestrictionType.USER_PHOTO_APPRAISAL );
		EasyMock.expect( restrictionService.getUserPhotoAppraisalRestrictionOn( EasyMock.anyInt(), EasyMock.<Date>anyObject() ) ).andReturn( restriction ).anyTimes();
		EasyMock.expect( restrictionService.getUserRestrictionMessage( restriction ) ).andReturn( new TranslatableMessage( "Restriction message", getServices() ) ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( restrictionService );

		final SecurityServiceImpl securityService = getSecurityService();
		securityService.setUserService( getUserService( photoAuthorId, photoAuthor ) );
		securityService.setFavoritesService( getFavoritesService( photoAuthorId, candidateUserId, false ) );
		securityService.setConfigurationService( getConfigurationService( true ) );
		securityService.setPhotoService( getPhotoService( photo, PhotoActionAllowance.CANDIDATES_AND_MEMBERS ) );
		securityService.setRestrictionService( restrictionService );
		securityService.setUserRankService( userRankService );

		assertTrue( "Commenting must NOT be accessible but it is"
			, securityService.validateUserCanVoteForPhoto( candidateUser, photo, dateUtilsService.getCurrentTime(), Language.EN ).isValidationFailed() );
	}

	@Test
	public void votingIsInaccessibleIfVotingRestrictedForPhotoTest() {

		final int photoAuthorId = 777;
		final int candidateUserId = 1000;
		final int userRankInGenre = 1;

		final User photoAuthor = new User( photoAuthorId );

		final User candidateUser = new User( candidateUserId );
		candidateUser.setUserStatus( UserStatus.CANDIDATE );

		final Photo photo = new Photo();
		photo.setId( 333 );
		photo.setUserId( photoAuthorId );
		photo.setGenreId( 321 );

		final UserRankService userRankService = EasyMock.createMock( UserRankService.class );
		EasyMock.expect( userRankService.getUserRankInGenre( candidateUserId, photo.getGenreId() ) ).andReturn( userRankInGenre ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( userRankService );

		final RestrictionService restrictionService = EasyMock.createMock( RestrictionService.class );
		EasyMock.expect( restrictionService.getUserPhotoAppraisalRestrictionOn( EasyMock.anyInt(), EasyMock.<Date>anyObject() ) ).andReturn( null ).anyTimes();
		final EntryRestriction restriction = new EntryRestriction<User>( candidateUser, RestrictionType.PHOTO_APPRAISAL );
		EasyMock.expect( restrictionService.getPhotoAppraisalRestrictionOn( EasyMock.anyInt(), EasyMock.<Date>anyObject() ) ).andReturn( restriction ).anyTimes();
		EasyMock.expect( restrictionService.getPhotoRestrictionMessage( restriction ) ).andReturn( new TranslatableMessage( "Restriction message", getServices() ) ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( restrictionService );

		final SecurityServiceImpl securityService = getSecurityService();
		securityService.setUserService( getUserService( photoAuthorId, photoAuthor ) );
		securityService.setFavoritesService( getFavoritesService( photoAuthorId, candidateUserId, false ) );
		securityService.setConfigurationService( getConfigurationService( true ) );
		securityService.setPhotoService( getPhotoService( photo, PhotoActionAllowance.CANDIDATES_AND_MEMBERS ) );
		securityService.setRestrictionService( restrictionService );
		securityService.setUserRankService( userRankService );

		assertTrue( "Commenting must NOT be accessible but it is"
			, securityService.validateUserCanVoteForPhoto( candidateUser, photo, dateUtilsService.getCurrentTime(), Language.EN ).isValidationFailed() );
	}

	@Test
	public void votingIsAccessibleTest() {

		final int photoAuthorId = 777;
		final int candidateUserId = 1000;
		final int userRankInGenre = 1;

		final User photoAuthor = new User( photoAuthorId );

		final User candidateUser = new User( candidateUserId );
		candidateUser.setUserStatus( UserStatus.CANDIDATE );

		final Photo photo = new Photo();
		photo.setId( 333 );
		photo.setUserId( photoAuthorId );
		photo.setGenreId( 321 );

		final UserRankService userRankService = EasyMock.createMock( UserRankService.class );
		EasyMock.expect( userRankService.getUserRankInGenre( candidateUserId, photo.getGenreId() ) ).andReturn( userRankInGenre ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( userRankService );

		final RestrictionService restrictionService = EasyMock.createMock( RestrictionService.class );
		EasyMock.expect( restrictionService.getUserPhotoAppraisalRestrictionOn( EasyMock.anyInt(), EasyMock.<Date>anyObject() ) ).andReturn( null ).anyTimes();
		EasyMock.expect( restrictionService.getPhotoAppraisalRestrictionOn( EasyMock.anyInt(), EasyMock.<Date>anyObject() ) ).andReturn( null ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( restrictionService );

		final SecurityServiceImpl securityService = getSecurityService();
		securityService.setUserService( getUserService( photoAuthorId, photoAuthor ) );
		securityService.setFavoritesService( getFavoritesService( photoAuthorId, candidateUserId, false ) );
		securityService.setConfigurationService( getConfigurationService( true ) );
		securityService.setPhotoService( getPhotoService( photo, PhotoActionAllowance.CANDIDATES_AND_MEMBERS ) );
		securityService.setRestrictionService( restrictionService );
		securityService.setUserRankService( userRankService );

		assertTrue( "Commenting must be accessible but it is not", securityService.validateUserCanVoteForPhoto( candidateUser, photo, dateUtilsService.getCurrentTime(), Language.EN ).isValidationPassed() );
	}

	private UserService getUserService( final int photoAuthorId, final User photoAuthor ) {
		final UserService userService = EasyMock.createMock( UserService.class );
		EasyMock.expect( userService.load( photoAuthorId ) ).andReturn( photoAuthor ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( userService );
		return userService;
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
		EasyMock.expect( configurationService.getBoolean( ConfigurationKey.CANDIDATES_CAN_VOTE_FOR_PHOTOS ) ).andReturn( value ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( configurationService );
		return configurationService;
	}

	private PhotoService getPhotoService( final Photo photo, final PhotoActionAllowance candidatesAndMembers ) {
		final PhotoService photoService = EasyMock.createMock( PhotoService.class );
		EasyMock.expect( photoService.getPhotoVotingAllowance( photo ) ).andReturn( candidatesAndMembers ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( photoService );
		return photoService;
	}

	private SecurityServiceImpl getSecurityService() {
		final SecurityServiceImpl securityService = new SecurityServiceImpl();

		securityService.setTranslatorService( translatorService );

		return securityService;
	}
}
