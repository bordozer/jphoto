package core.services.user;

import core.general.user.EmailNotificationType;
import core.general.user.User;
import core.general.user.UserMembershipType;
import core.general.user.UserStatus;
import core.general.user.userTeam.UserTeam;
import core.general.user.userTeam.UserTeamMember;
import core.services.translator.Language;
import core.services.utils.DateUtilsService;
import core.services.utils.RandomUtilsService;
import core.services.utils.SystemVarsService;
import org.springframework.beans.factory.annotation.Autowired;
import utils.fakeUser.AbstractFakeMember;
import utils.fakeUser.FakeMakeUpMaster;
import utils.fakeUser.FakeModel;
import utils.fakeUser.FakePhotographer;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

public class FakeUserServiceImpl implements FakeUserService {

	private final static int userMemberShipArray[] = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 3 };
	private final static int teamMembersQtyArray[] = { 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 4, 5 };

	@Autowired
	private UserService userService;

	@Autowired
	private RandomUtilsService randomUtilsService;

	@Autowired
	private DateUtilsService dateUtilsService;

	@Autowired
	private SystemVarsService systemVarsService;

	@Override
	public User getRandomUser() {
		return initUserFromFakeMember( getRandomFakeMember() );
	}

	@Override
	public User getRandomUser( final AbstractFakeMember fakeMember ) {
		return initUserFromFakeMember( fakeMember );
	}

	@Override
	public AbstractFakeMember getRandomFakeMember() {
		final int randomMembershipTypeId = userMemberShipArray[randomUtilsService.getRandomInt( 0, userMemberShipArray.length - 1 )];

		return getRandomFakeMember( UserMembershipType.getById( randomMembershipTypeId ) );
	}

	public AbstractFakeMember getRandomFakeMember( final UserMembershipType membershipType ) {
		AbstractFakeMember fakeMember = null;

		switch ( membershipType ) {
			case AUTHOR:
				fakeMember = new FakePhotographer();
				break;
			case MODEL:
				fakeMember = new FakeModel();
				break;
			case MAKEUP_MASTER:
				fakeMember = new FakeMakeUpMaster();
				break;
			default:
				throw new IllegalArgumentException( String.format( "Incorrect Membership Type Id: %s", membershipType ) );
		}

		fakeMember.setRandomUtilsService( randomUtilsService );

		return fakeMember;
	}

	@Override
	public UserTeam getRandomUserTeam( final User user ) {
		return new UserTeam( user, getRandomUserTeamMembers( user, getRandomFakeMember() ) );
	}

	@Override
	public UserTeam getRandomUserTeam( final User user, final AbstractFakeMember fakeMember ) {
		return new UserTeam( user, getRandomUserTeamMembers( user, fakeMember ) );
	}

	private List<UserTeamMember> getRandomUserTeamMembers( final User user, final AbstractFakeMember fakeMember ) {

		fakeMember.setRandomUtilsService( randomUtilsService );
		fakeMember.setFakeUserService( this );

		final int teamMembersQty = randomUtilsService.getRandomIntegerArrayElement( teamMembersQtyArray );

		final List<UserTeamMember> userTeamMembers = newArrayList();

		final List<User> users = userService.loadAll();

		if ( users.size() == 0 ) {
			return Collections.emptyList();
		}

		for ( int i = 0; i < teamMembersQty; i++ ) {
			final UserTeamMember userTeamMember = fakeMember.getRandomUserTeamMember( user, users );

			if ( userTeamMember == null ) {
				continue;
			}

			userTeamMembers.add( userTeamMember );
		}

		return userTeamMembers;
	}

	private User initUserFromFakeMember( final AbstractFakeMember fakeMember ) {
		final int randomInt = randomUtilsService.getRandomInt( 14, 99999 );

		final User user = new User();

		final String userName = fakeMember.getName();
		user.setLogin( userName );
		user.setName( userName );
		user.setGender( fakeMember.getGender() );

		user.setMembershipType( fakeMember.getMembershipType() );

		user.setEmail( getRandomEmail() );

		user.setPhotosOnPage( -1 );

		final int age = randomUtilsService.getRandomInt( 14, 75 );
		final Date yearsOffsetFromCurrentDate = dateUtilsService.getYearsOffsetFromCurrentDate( -age );
		final Date monthsOffsetFromCurrentDate = dateUtilsService.getMonthsOffset( yearsOffsetFromCurrentDate, -age );
		final Date daysOffsetFromCurrentDate = dateUtilsService.getDatesOffset( monthsOffsetFromCurrentDate, -age );
		user.setDateOfBirth( daysOffsetFromCurrentDate );

		user.setHomeSite( String.format( "http://www.user%s.someserver.com", randomInt ) );

		user.setUserStatus( UserStatus.CANDIDATE );
		user.setRegistrationTime( dateUtilsService.getCurrentTime() );

		final List<EmailNotificationType> emailNotificationTypes = Arrays.asList( EmailNotificationType.values() );
		final int emailOptionsRandomQty = randomUtilsService.getRandomInt( 0, EmailNotificationType.values().length - 1 );
		final List<EmailNotificationType> randomEmailNotificationTypes = randomUtilsService.getRandomNUniqueListElements( emailNotificationTypes, emailOptionsRandomQty );
		user.setEmailNotificationTypes( newHashSet( randomEmailNotificationTypes ) );

		user.setDefaultPhotoCommentsAllowance( randomUtilsService.getRandomPhotoAllowance() );
		user.setDefaultPhotoVotingAllowance( randomUtilsService.getRandomPhotoAllowance() );

		user.setLanguage( getRandomLanguage() );

		return user;
	}

	private Language getRandomLanguage() {
		return randomUtilsService.getRandomGenericSetElement( newHashSet( systemVarsService.getActiveLanguages() ) );
	}

	private String getRandomEmail() {
		String randomEmail;
		boolean isUserFound;
		int i = 0;
		do {
			randomEmail = String.format( "%s%s@jphoto.ua", "email", randomUtilsService.getRandomInt( 14, 99999 ) );
			isUserFound = userService.loadByEmail( randomEmail ) != null;

			if ( i > 100 ) {
				return "CAN_NOT_CREATE_EMAIL";
			}
			i++;
		} while ( isUserFound );
		return randomEmail;
	}
}
