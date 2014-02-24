package services;

import common.AbstractTestCase;
import core.general.cache.entries.RankInGenrePointsEntry;
import core.general.configuration.ConfigurationKey;
import core.general.user.User;
import core.general.user.UserStatus;
import core.services.dao.UserRankDao;
import core.services.system.CacheServiceImpl;
import core.services.system.ConfigurationService;
import core.services.user.UserRankServiceImpl;
import core.services.user.UserService;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static junit.framework.Assert.assertTrue;

public class UserRankServiceTest extends AbstractTestCase {

	public static final int USER_ID = 777;
	public static final int GENRE_ID = 555;

	@Before
	public void setup() {
		super.setup();
	}

	@Test
	public void showGenreRankPointsMapTest1( ) {
		final int baseStep = 5;
		final float coefficient = 0.1f;

		showRankPoints( baseStep, coefficient );
	}

	@Test
	public void showGenreRankPointsMapTest2( ) {
		final int baseStep = 10;
		final float coefficient = 0.2f;

		showRankPoints( baseStep, coefficient );
	}

	private void showRankPoints( final int baseStep, final float coefficient ) {
		final User user = new User();
		user.setId( 777 );

		final ConfigurationService configurationService = EasyMock.createMock( ConfigurationService.class );

		EasyMock.expect( configurationService.getInt( ConfigurationKey.RANK_VOTING_POINTS_BASE_STEP ) ).andReturn( baseStep ).anyTimes();
		EasyMock.expect( configurationService.getFloat( ConfigurationKey.RANK_VOTING_POINTS_COEFFICIENT ) ).andReturn( coefficient ).anyTimes();
		EasyMock.expect( configurationService.getInt( ConfigurationKey.RANK_VOTING_MAX_GENRE_RANK ) ).andReturn( 20 ).anyTimes();
		EasyMock.expect( configurationService.getInt( ConfigurationKey.RANK_VOTING_FIRST_RANK_POINTS ) ).andReturn( 5 ).anyTimes();
		EasyMock.expect( configurationService.getInt( ConfigurationKey.CACHE_LENGTH_RANK_IN_GENRE_POINTS ) ).andReturn( 4 ).anyTimes();
		EasyMock.expect( configurationService.getBoolean( ConfigurationKey.CACHE_USE_CACHE ) ).andReturn( true ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( configurationService );

		final UserRankServiceImpl userRankService = getUserRankService( configurationService );

		final Map<Integer,Integer> pointsMap = userRankService.getUserGenreRankPointsMap();

		int i = 0;
		for ( final int rank : pointsMap.keySet() ) {
			System.out.print( String.format( "%s, ", pointsMap.get( rank ) ) );
		}
		System.out.println();
	}

	private UserRankServiceImpl getUserRankService( final ConfigurationService configurationService ) {
		final UserRankServiceImpl userRankService = new UserRankServiceImpl();
		userRankService.setConfigurationService( configurationService );

		final CacheServiceImpl<RankInGenrePointsEntry> cacheService = new CacheServiceImpl<>();
		cacheService.setConfigurationService( configurationService );
		cacheService.setDateUtilsService( dateUtilsService );
		userRankService.setCacheService( cacheService );
		return userRankService;
	}

	@Test
	public void getUserGenreRankPointsMapTest1() {
		final int[] rankPoints = { 5, 11, 17, 24, 31, 39, 48, 58, 69, 81, 94, 108, 124, 141, 160, 181, 204, 229, 257, 288 };
		final int baseStep = 5;
		final float coefficient = 0.1f;

		checkRankMap( rankPoints, baseStep, coefficient );
	}

	@Test
	public void getUserGenreRankPointsMapTest2() {
		final int[] rankPoints = { 5, 16, 29, 45, 64, 87, 114, 147, 186, 233, 290, 358, 440, 538, 656, 797, 966, 1169, 1413, 1706 };
		final int baseStep = 10;
		final float coefficient = 0.2f;

		checkRankMap( rankPoints, baseStep, coefficient );
	}

	private void checkRankMap( final int[] rankPoints, final int baseStep, final float coefficient ) {
		final User user = new User();
		user.setId( 777 );

		final ConfigurationService configurationService = EasyMock.createMock( ConfigurationService.class );
		EasyMock.expect( configurationService.getInt( ConfigurationKey.RANK_VOTING_POINTS_BASE_STEP ) ).andReturn( baseStep ).anyTimes();
		EasyMock.expect( configurationService.getFloat( ConfigurationKey.RANK_VOTING_POINTS_COEFFICIENT ) ).andReturn( coefficient ).anyTimes();
		EasyMock.expect( configurationService.getInt( ConfigurationKey.RANK_VOTING_MAX_GENRE_RANK ) ).andReturn( 20 ).anyTimes();
		EasyMock.expect( configurationService.getInt( ConfigurationKey.RANK_VOTING_FIRST_RANK_POINTS ) ).andReturn( 5 ).anyTimes();
		EasyMock.expect( configurationService.getInt( ConfigurationKey.CACHE_LENGTH_RANK_IN_GENRE_POINTS ) ).andReturn( 4 ).anyTimes();
		EasyMock.expect( configurationService.getBoolean( ConfigurationKey.CACHE_USE_CACHE ) ).andReturn( true ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( configurationService );

		final UserRankServiceImpl userRankService = getUserRankService( configurationService );

		final Map<Integer,Integer> pointsMap = userRankService.getUserGenreRankPointsMap();

		int i = 0;
		for ( final int rank : pointsMap.keySet() ) {
			assertTrue( String.format( "Rank is not correct: %d != %d", rankPoints[ i ], pointsMap.get( rank ) ), rankPoints[ i++ ] == pointsMap.get( rank ) );
		}
	}

	@Test
	public void calculateUserRankInGenreTest() {

		final int userId = 777;
		final int genreId = 555;

		final class PointsFinder {

			private final List<Integer> rankPoints = newArrayList( 0, 5, 11, 17, 24, 31, 39, 48, 58, 69, 81, 94, 108, 124, 141, 160, 181, 204, 229, 257, 288 );

			private int getExpectedRank( final int pointsForRankInGenre ) {
				final int absPoints = Math.abs( pointsForRankInGenre );
				final int sign = pointsForRankInGenre > 0 ? 1 : -1;

				for ( int i = 0; i < rankPoints.size(); i++ ) {
					if ( absPoints >= rankPoints.get( i ) && absPoints < rankPoints.get( i + 1 ) ) {
						return i * sign;
					}
				}
				return Integer.MIN_VALUE;
			}
		}

		final PointsFinder finder = new PointsFinder();

		for ( int points = -237; points <= 237; points++ ) {

			final ConfigurationService configurationService = EasyMock.createMock( ConfigurationService.class );
			EasyMock.expect( configurationService.getInt( ConfigurationKey.RANK_VOTING_POINTS_BASE_STEP ) ).andReturn( 5 ).anyTimes();
			EasyMock.expect( configurationService.getFloat( ConfigurationKey.RANK_VOTING_POINTS_COEFFICIENT ) ).andReturn( 0.1f ).anyTimes();
			EasyMock.expect( configurationService.getInt( ConfigurationKey.RANK_VOTING_MAX_GENRE_RANK ) ).andReturn( 20 ).anyTimes();
			EasyMock.expect( configurationService.getInt( ConfigurationKey.RANK_VOTING_FIRST_RANK_POINTS ) ).andReturn( 5 ).anyTimes();
			EasyMock.expect( configurationService.getInt( ConfigurationKey.CACHE_LENGTH_RANK_IN_GENRE_POINTS ) ).andReturn( 4 ).anyTimes();
			EasyMock.expect( configurationService.getBoolean( ConfigurationKey.CACHE_USE_CACHE ) ).andReturn( true ).anyTimes();
			EasyMock.expectLastCall();
			EasyMock.replay( configurationService );

			final UserRankDao userRankDao = EasyMock.createMock( UserRankDao.class );
			EasyMock.expect( userRankDao.getUserVotePointsForRankInGenre( userId, genreId ) ).andReturn( points ).anyTimes();
			EasyMock.expectLastCall();
			EasyMock.replay( userRankDao );

			final UserRankServiceImpl userRankService = getUserRankService( configurationService );
			userRankService.setUserRankDao( userRankDao );

			final int newUserRank = userRankService.calculateUserRankInGenre( userId, genreId );

			final int expectedUserRank = finder.getExpectedRank( points );
			assertTrue( String.format( "Calculated new user rank is %d but expected is %d", newUserRank, expectedUserRank ), newUserRank == expectedUserRank );
		}
	}

	@Test
	public void candidateNegativeMarksWhenRankIsLessThenAbsMinNegativeMark() {
		final int minMark = -1;
		final int userRankInGenre = 0;

		final MarksSet marksSet = new MarksSet();
		marksSet.setCandidatesVotingMinMark( minMark );
		marksSet.setUserRankInGenre( userRankInGenre );

		final User user = new User();
		user.setUserStatus( UserStatus.CANDIDATE );

		final UserRankServiceImpl userRankService = getUserRankService( marksSet, user );

		final int actualResult = userRankService.getUserLowestNegativeMarkInGenre( USER_ID, GENRE_ID );
		assertTrue( String.format( "Assertion fails. Expected result is %d but actual result is %d", minMark, actualResult ), actualResult == minMark );
	}

	@Test
	public void candidateNegativeMarksWhenRankIsMoreThenAbsMinNegativeMark() {
		final int minMark = -1;
		final int userRankInGenre = 3;

		final MarksSet marksSet = new MarksSet();
		marksSet.setCandidatesVotingMinMark( minMark );
		marksSet.setUserRankInGenre( userRankInGenre );

		final User user = new User();
		user.setUserStatus( UserStatus.CANDIDATE );

		final UserRankServiceImpl userRankService = getUserRankService( marksSet, user );

		final int actualResult = userRankService.getUserLowestNegativeMarkInGenre( USER_ID, GENRE_ID );
		assertTrue( String.format( "Assertion fails. Expected result is %d but actual result is %d", minMark, actualResult ), actualResult == minMark );
	}

	@Test
	public void candidatePositiveMarkWhenRankIsLessThenMinPositiveMark() {
		final int maxMark = 2;
		final int userRankInGenre = 0;

		final MarksSet marksSet = new MarksSet();
		marksSet.setCandidatesVotingMaxMark( maxMark );
		marksSet.setUserRankInGenre( userRankInGenre );

		final User user = new User();
		user.setUserStatus( UserStatus.CANDIDATE );

		final UserRankServiceImpl userRankService = getUserRankService( marksSet, user );

		final int actualResult = userRankService.getUserHighestPositiveMarkInGenre( USER_ID, GENRE_ID );
		assertTrue( String.format( "Assertion fails. Expected result is %d but actual result is %d", maxMark, actualResult ), actualResult == maxMark );
	}

	@Test
	public void candidatePositiveMarkWhenRankIsMoreThenMinPositiveMark() {
		final int maxMark = 2;
		final int userRankInGenre = 5;

		final MarksSet marksSet = new MarksSet();
		marksSet.setCandidatesVotingMaxMark( maxMark );
		marksSet.setUserRankInGenre( userRankInGenre );

		final User user = new User();
		user.setUserStatus( UserStatus.CANDIDATE );

		final UserRankServiceImpl userRankService = getUserRankService( marksSet, user );

		final int actualResult = userRankService.getUserHighestPositiveMarkInGenre( USER_ID, GENRE_ID );
		assertTrue( String.format( "Assertion fails. Expected result is %d but actual result is %d", maxMark, actualResult ), actualResult == maxMark );
	}

	@Test
	public void memberMarksWhenRankIsLessThenAbsMinimalNegativeMark() {
		final int minMark = -2;

		final MarksSet marksSet = new MarksSet();
		marksSet.setMembersMinNegativeMark( minMark );
		marksSet.setUserRankInGenre( 0 );

		final User user = new User();
		user.setUserStatus( UserStatus.MEMBER );

		final UserRankServiceImpl userRankService = getUserRankService( marksSet, user );

		final int actualResult = userRankService.getUserLowestNegativeMarkInGenre( USER_ID, GENRE_ID );
		assertTrue( String.format( "Assertion fails. Expected result is %d but actual result is %d", minMark, actualResult ), actualResult == minMark );
	}

	@Test
	public void memberNegativeMarksWhenRankIsMoreThenAbsMinimalNegativeMarkButLessThenAbsMaxNegativeMark() {
		final int membersMinNegativeMark = -2;
		final int membersMaxNegativeMark = -4;
		final int userRankInGenre = 3;

		final MarksSet marksSet = new MarksSet();
		marksSet.setMembersMinNegativeMark( membersMinNegativeMark );
		marksSet.setMembersMaxNegativeMark( membersMaxNegativeMark );
		marksSet.setUserRankInGenre( userRankInGenre );

		final User user = new User();
		user.setUserStatus( UserStatus.MEMBER );

		final UserRankServiceImpl userRankService = getUserRankService( marksSet, user );

		final int actualResult = userRankService.getUserLowestNegativeMarkInGenre( USER_ID, GENRE_ID );
		assertTrue( String.format( "Assertion fails. Expected result is %d but actual result is %d", -userRankInGenre, actualResult ), actualResult == -userRankInGenre );
	}

	@Test
	public void memberNegativeMarksWhenRankIsMoreThenAbsMaxNegativeMark() {
		final int membersMinNegativeMark = -2;
		final int membersMaxNegativeMark = -4;
		final int userRankInGenre = 5;

		final MarksSet marksSet = new MarksSet();
		marksSet.setMembersMinNegativeMark( membersMinNegativeMark );
		marksSet.setMembersMaxNegativeMark( membersMaxNegativeMark );
		marksSet.setUserRankInGenre( userRankInGenre );

		final User user = new User();
		user.setUserStatus( UserStatus.MEMBER );

		final UserRankServiceImpl userRankService = getUserRankService( marksSet, user );

		final int actualResult = userRankService.getUserLowestNegativeMarkInGenre( USER_ID, GENRE_ID );
		assertTrue( String.format( "Assertion fails. Expected result is %d but actual result is %d", membersMaxNegativeMark, actualResult ), actualResult == membersMaxNegativeMark );
	}

	@Test
	public void memberPositiveMarksWhenRankIsLessThenMinPositiveMark() {
		final int membersMinPositiveMark = 2;
		final int membersMaxPositiveMark = 5;
		final int userRankInGenre = 1;

		final MarksSet marksSet = new MarksSet();
		marksSet.setMembersMinPositiveMark( membersMinPositiveMark );
		marksSet.setMembersMaxPositiveMark( membersMaxPositiveMark );
		marksSet.setUserRankInGenre( userRankInGenre );

		final User user = new User();
		user.setUserStatus( UserStatus.MEMBER );

		final UserRankServiceImpl userRankService = getUserRankService( marksSet, user );

		final int actualResult = userRankService.getUserHighestPositiveMarkInGenre( USER_ID, GENRE_ID );
		assertTrue( String.format( "Assertion fails. Expected result is %d but actual result is %d", membersMinPositiveMark, actualResult ), actualResult == membersMinPositiveMark );
	}

	@Test
	public void memberPositiveMarksWhenRankIsMoreThenMinPositiveMarkButLessThenMaxPositiveMark() {
		final int membersMinPositiveMark = 2;
		final int membersMaxPositiveMark = 5;
		final int userRankInGenre = 3;

		final MarksSet marksSet = new MarksSet();
		marksSet.setMembersMinPositiveMark( membersMinPositiveMark );
		marksSet.setMembersMaxPositiveMark( membersMaxPositiveMark );
		marksSet.setUserRankInGenre( userRankInGenre );

		final User user = new User();
		user.setUserStatus( UserStatus.MEMBER );

		final UserRankServiceImpl userRankService = getUserRankService( marksSet, user );

		final int actualResult = userRankService.getUserHighestPositiveMarkInGenre( USER_ID, GENRE_ID );
		assertTrue( String.format( "Assertion fails. Expected result is %d but actual result is %d", userRankInGenre, actualResult ), actualResult == userRankInGenre );
	}

	@Test
	public void memberPositiveMarksWhenRankIsMoreThenMaxPositiveMark() {
		final int membersMinPositiveMark = 2;
		final int membersMaxPositiveMark = 5;
		final int userRankInGenre = 7;

		final MarksSet marksSet = new MarksSet();
		marksSet.setMembersMinPositiveMark( membersMinPositiveMark );
		marksSet.setMembersMaxPositiveMark( membersMaxPositiveMark );
		marksSet.setUserRankInGenre( userRankInGenre );

		final User user = new User();
		user.setUserStatus( UserStatus.MEMBER );

		final UserRankServiceImpl userRankService = getUserRankService( marksSet, user );

		final int actualResult = userRankService.getUserHighestPositiveMarkInGenre( USER_ID, GENRE_ID );
		assertTrue( String.format( "Assertion fails. Expected result is %d but actual result is %d", membersMaxPositiveMark, actualResult ), actualResult == membersMaxPositiveMark );
	}

	private UserRankServiceImpl getUserRankService( final MarksSet marksSet, final User user ) {
		final UserRankDao userRankDao = getUserRankDao( marksSet );
		final UserService userService = getUserService( user );

		final UserRankServiceImpl userRankService = new UserRankServiceImpl();
		userRankService.setConfigurationService( getConfigurationService( marksSet ) );
		userRankService.setUserRankDao( userRankDao );
		userRankService.setUserService( userService );
		return userRankService;
	}

	private UserRankDao getUserRankDao( final MarksSet marksSet ) {
		final UserRankDao userRankDao = EasyMock.createMock( UserRankDao.class );
		EasyMock.expect( userRankDao.getUserRankInGenre( EasyMock.anyInt(), EasyMock.anyInt() ) ).andReturn( marksSet.getUserRankInGenre() ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( userRankDao );
		return userRankDao;
	}

	private UserService getUserService( final User user ) {
		final UserService userService = EasyMock.createMock( UserService.class );
		EasyMock.expect( userService.load( EasyMock.anyInt() ) ).andReturn( user ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( userService );
		return userService;
	}

	private ConfigurationService getConfigurationService( final MarksSet marksSet ) {
		final ConfigurationService configurationService =  EasyMock.createMock( ConfigurationService.class );

		EasyMock.expect( configurationService.getInt( ConfigurationKey.CANDIDATES_PHOTO_VOTING_LOWEST_MARK ) ).andReturn( marksSet.getCandidatesVotingMinMark() ).anyTimes();
		EasyMock.expect( configurationService.getInt( ConfigurationKey.CANDIDATES_PHOTO_VOTING_HIGHEST_MARK ) ).andReturn( marksSet.getCandidatesVotingMaxMark() ).anyTimes();

		EasyMock.expect( configurationService.getInt( ConfigurationKey.PHOTO_VOTING_HIGHEST_NEGATIVE_MARK ) ).andReturn( marksSet.getMembersMinNegativeMark() ).anyTimes();
		EasyMock.expect( configurationService.getInt( ConfigurationKey.PHOTO_VOTING_LOWEST_NEGATIVE_MARK ) ).andReturn( marksSet.getMembersMaxNegativeMark() ).anyTimes();

		EasyMock.expect( configurationService.getInt( ConfigurationKey.PHOTO_VOTING_LOWEST_POSITIVE_MARK ) ).andReturn( marksSet.getMembersMinPositiveMark() ).anyTimes();
		EasyMock.expect( configurationService.getInt( ConfigurationKey.PHOTO_VOTING_HIGHEST_POSITIVE_MARK ) ).andReturn( marksSet.getMembersMaxPositiveMark() ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( configurationService );

		return configurationService;
	}

	private class MarksSet {

		private int userId;
		private int userRankInGenre;

		private int candidatesVotingMinMark;
		private int candidatesVotingMaxMark;

		private int membersMinNegativeMark;
		private int membersMaxNegativeMark;

		private int membersMinPositiveMark;
		private int membersMaxPositiveMark;

		public int getUserId() {
			return userId;
		}

		public void setUserId( final int userId ) {
			this.userId = userId;
		}

		public int getUserRankInGenre() {
			return userRankInGenre;
		}

		public void setUserRankInGenre( final int userRankInGenre ) {
			this.userRankInGenre = userRankInGenre;
		}

		public int getCandidatesVotingMinMark() {
			return candidatesVotingMinMark;
		}

		public void setCandidatesVotingMinMark( final int candidatesVotingMinMark ) {
			this.candidatesVotingMinMark = candidatesVotingMinMark;
		}

		public int getCandidatesVotingMaxMark() {
			return candidatesVotingMaxMark;
		}

		public void setCandidatesVotingMaxMark( final int candidatesVotingMaxMark ) {
			this.candidatesVotingMaxMark = candidatesVotingMaxMark;
		}

		public int getMembersMinNegativeMark() {
			return membersMinNegativeMark;
		}

		public void setMembersMinNegativeMark( final int membersMinNegativeMark ) {
			this.membersMinNegativeMark = membersMinNegativeMark;
		}

		public int getMembersMaxNegativeMark() {
			return membersMaxNegativeMark;
		}

		public void setMembersMaxNegativeMark( final int membersMaxNegativeMark ) {
			this.membersMaxNegativeMark = membersMaxNegativeMark;
		}

		public int getMembersMinPositiveMark() {
			return membersMinPositiveMark;
		}

		public void setMembersMinPositiveMark( final int membersMinPositiveMark ) {
			this.membersMinPositiveMark = membersMinPositiveMark;
		}

		public int getMembersMaxPositiveMark() {
			return membersMaxPositiveMark;
		}

		public void setMembersMaxPositiveMark( final int membersMaxPositiveMark ) {
			this.membersMaxPositiveMark = membersMaxPositiveMark;
		}
	}
}
