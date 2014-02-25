package menuItems.photo;

import core.enums.UserTeamMemberType;
import core.general.configuration.ConfigurationKey;
import core.general.menus.photo.items.PhotoMenuItemGoToAuthorPhotoByTeamMember;
import core.general.photoTeam.PhotoTeamMember;
import core.general.user.User;
import core.general.user.userTeam.UserTeamMember;
import core.services.security.SecurityService;
import core.services.security.ServicesImpl;
import core.services.system.ConfigurationService;
import core.services.user.UserTeamService;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PhotoMenuItemGoToAuthorPhotoByTeamMemberTest extends AbstractPhotoMenuItemTest_ {

	private PhotoTeamMember photoTeamMember;
	private UserTeamMember userTeamMember;

	@Before
	public void setup() {
		super.setup();

		photoTeamMember = getPhotoTeamMember();
		userTeamMember = getUserTeamMember();
	}

	@Test
	public void notLoggedUserCanSeeMenuIfThereIsMoreThenOnePhotosWithThisMemberTest() {
		final User accessor = User.NOT_LOGGED_USER;

		final int teamMemberPhotosQty = 2;
		final boolean isPhotoAuthorNameMustBeHidden = false;

		final ServicesImpl services = getServices( testData, accessor );
		services.setSecurityService( getSecurityService( accessor, isPhotoAuthorNameMustBeHidden ) );
		services.setUserTeamService( getUserTeamService( teamMemberPhotosQty ) );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoMenuItemGoToAuthorPhotoByTeamMember( testData.getPhoto(), accessor, services, photoTeamMember ).isAccessibleFor() );
	}

	@Test
	public void notLoggedUserCanNotSeeMenuIfThereIsLessThenOnePhotoWithThisMemberTest() {
		final User accessor = User.NOT_LOGGED_USER;
		final int teamMemberPhotosQty = 1;
		final boolean isPhotoAuthorNameMustBeHidden = false;

		final ServicesImpl services = getServices( testData, accessor );
		services.setSecurityService( getSecurityService( accessor, isPhotoAuthorNameMustBeHidden ) );
		services.setUserTeamService( getUserTeamService( teamMemberPhotosQty ) );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new PhotoMenuItemGoToAuthorPhotoByTeamMember( testData.getPhoto(), accessor, services, photoTeamMember ).isAccessibleFor() );
	}

	@Test
	public void adminCanSeeMenuIfThereIsMoreThenOnePhotosWithThisMemberTest() {
		final User accessor = SUPER_ADMIN_1;
		final int teamMemberPhotosQty = 2;

		final ServicesImpl services = getServices( testData, accessor );
		services.setUserTeamService( getUserTeamService( teamMemberPhotosQty ) );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoMenuItemGoToAuthorPhotoByTeamMember( testData.getPhoto(), accessor, services, photoTeamMember ).isAccessibleFor() );
	}

	@Test
	public void adminCanNotSeeMenuIfThereIsLessThenTwoPhotosWithThisMemberTest() {
		final User accessor = SUPER_ADMIN_1;
		final int teamMemberPhotosQty = 2;

		final ServicesImpl services = getServices( testData, accessor );
		services.setUserTeamService( getUserTeamService( teamMemberPhotosQty ) );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoMenuItemGoToAuthorPhotoByTeamMember( testData.getPhoto(), accessor, services, photoTeamMember ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanNOTSeeMenuIfShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOFFTest() {
		final User accessor = testData.getPhotoAuthor();
		final boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn = false; // sick!
		final int teamMemberPhotosQty = 2;

		final ServicesImpl services = getServices( testData, accessor );
		services.setConfigurationService( getConfigurationService( showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn ) );
		services.setUserTeamService( getUserTeamService( teamMemberPhotosQty ) );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new PhotoMenuItemGoToAuthorPhotoByTeamMember( testData.getPhoto(), accessor, services, photoTeamMember ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanNOTSeeMenuIfShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedONAndThereIsLessThenTwoPhotosWithThisMemberTest() {
		final User accessor = testData.getPhotoAuthor();
		final boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn = true; // sick!
		final int teamMemberPhotosQty = 1; // sick!

		final ServicesImpl services = getServices( testData, accessor );
		services.setConfigurationService( getConfigurationService( showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn ) );
		services.setUserTeamService( getUserTeamService( teamMemberPhotosQty ) );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new PhotoMenuItemGoToAuthorPhotoByTeamMember( testData.getPhoto(), accessor, services, photoTeamMember ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanSeeMenuIfShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOnTest() {
		final User accessor = testData.getPhotoAuthor();
		final boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn = true;
		final boolean isPhotoAuthorNameMustBeHidden = false; // sick!
		final int teamMemberPhotosQty = 2; // sick!

		final ServicesImpl services = getServices( testData, accessor );
		services.setConfigurationService( getConfigurationService( showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn ) );
		services.setSecurityService( getSecurityService( accessor, isPhotoAuthorNameMustBeHidden ) );
		services.setUserTeamService( getUserTeamService( teamMemberPhotosQty ) );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoMenuItemGoToAuthorPhotoByTeamMember( testData.getPhoto(), accessor, services, photoTeamMember ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanNotSeeMenuThePhotoWithinAnonymousPeriodTest() {
		final User accessor = testData.getAccessor();
		final boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn = false;
		final boolean isPhotoAuthorNameMustBeHidden = true; // sick!
		final int teamMemberPhotosQty = 2;

		final ServicesImpl services = getServices( testData, accessor );
		services.setConfigurationService( getConfigurationService( showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn ) );
		services.setSecurityService( getSecurityService( accessor, isPhotoAuthorNameMustBeHidden ) );
		services.setUserTeamService( getUserTeamService( teamMemberPhotosQty ) );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new PhotoMenuItemGoToAuthorPhotoByTeamMember( testData.getPhoto(), accessor, services, photoTeamMember ).isAccessibleFor() );
	}

	@Test
	public void menuIsNotShownIfThereIsLessThenOnePhotosWithThisMemberTest() {
		final User accessor = testData.getAccessor();
		final boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn = false;
		final boolean isPhotoAuthorNameMustBeHidden = false;
		final int teamMemberPhotosQty = 1;

		final ServicesImpl services = getServices( testData, accessor );
		services.setConfigurationService( getConfigurationService( showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn ) );
		services.setSecurityService( getSecurityService( accessor, isPhotoAuthorNameMustBeHidden ) );
		services.setUserTeamService( getUserTeamService( teamMemberPhotosQty ) );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new PhotoMenuItemGoToAuthorPhotoByTeamMember( testData.getPhoto(), accessor, services, photoTeamMember ).isAccessibleFor() );
	}

	@Test
	public void menuIsShownIfThereIsMoreThenOnePhotosWithThisMemberTest() {
		final User accessor = testData.getAccessor();
		final boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn = false;
		final boolean isPhotoAuthorNameMustBeHidden = false;
		final int teamMemberPhotosQty = 2;

		final ServicesImpl services = getServices( testData, accessor );
		services.setConfigurationService( getConfigurationService( showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn ) );
		services.setSecurityService( getSecurityService( accessor, isPhotoAuthorNameMustBeHidden ) );
		services.setUserTeamService( getUserTeamService( teamMemberPhotosQty ) );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoMenuItemGoToAuthorPhotoByTeamMember( testData.getPhoto(), accessor, services, photoTeamMember ).isAccessibleFor() );
	}

	private ConfigurationService getConfigurationService( final boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn ) {
		final ConfigurationService configurationService = EasyMock.createMock( ConfigurationService.class );

		EasyMock.expect( configurationService.getBoolean( ConfigurationKey.SYSTEM_SHOW_UI_MENU_GO_TO_PHOTOS_FOR_OWN_ENTRIES ) ).andReturn( showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( configurationService );

		return configurationService;
	}

	private SecurityService getSecurityService( final User accessor, final boolean isPhotoAuthorNameMustBeHidden ) {

		final SecurityService securityService = EasyMock.createMock( SecurityService.class );

		EasyMock.expect( securityService.isPhotoAuthorNameMustBeHidden( testData.getPhoto(), accessor ) ).andReturn( isPhotoAuthorNameMustBeHidden ).anyTimes();

		EasyMock.expect( securityService.isSuperAdminUser( accessor.getId() ) ).andReturn( SUPER_ADMIN_2.getId() == accessor.getId() || SUPER_ADMIN_1.getId() == accessor.getId() ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( securityService );

		return securityService;
	}

	private UserTeamService getUserTeamService( final int teamMemberPhotosQty ) {
		final UserTeamService userTeamService = EasyMock.createMock( UserTeamService.class );

		EasyMock.expect( userTeamService.getTeamMemberPhotosQty( userTeamMember.getId() ) ).andReturn( teamMemberPhotosQty ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( userTeamService );

		return userTeamService;
	}

	public PhotoTeamMember getPhotoTeamMember() {
		final PhotoTeamMember photoTeamMember = new PhotoTeamMember();
		photoTeamMember.setUserTeamMember( getUserTeamMember() );

		return photoTeamMember;
	}

	private UserTeamMember getUserTeamMember() {
		final UserTeamMember userTeamMember = new UserTeamMember();

		userTeamMember.setId( 951 );
		userTeamMember.setName( "Team Member" );
		userTeamMember.setTeamMemberUser( getTeamMemberUser() );
		userTeamMember.setTeamMemberType( UserTeamMemberType.MODEL );

		return userTeamMember;
	}

	private User getTeamMemberUser() {
		final User user = new User();
		user.setName( "Photo team member user" );
		return user;
	}
}
