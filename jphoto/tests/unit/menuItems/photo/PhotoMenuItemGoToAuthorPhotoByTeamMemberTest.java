package menuItems.photo;

import core.enums.UserTeamMemberType;
import core.general.menus.photo.items.PhotoMenuItemGoToAuthorPhotoByTeamMember;
import core.general.photoTeam.PhotoTeamMember;
import core.general.user.User;
import core.general.user.userTeam.UserTeamMember;
import core.services.security.ServicesImpl;
import core.services.user.UserTeamService;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

public class PhotoMenuItemGoToAuthorPhotoByTeamMemberTest extends AbstractGoToAuthorPhotosTest_ {

	private PhotoTeamMember photoTeamMember;
	private UserTeamMember userTeamMember;

	@Before
	public void setup() {
		super.setup();

		photoTeamMember = getPhotoTeamMember();
		userTeamMember = getUserTeamMember();
	}

	@Test
	public void notLoggedUserCanSeeMenuIfThereIsMoreThenOnePhotosTest() {

		final GoToParameters goToParameters = new GoToParameters( User.NOT_LOGGED_USER, 2 );

		assertT( new PhotoMenuItemGoToAuthorPhotoByTeamMember( testData.getPhoto(), goToParameters.getAccessor(), getServicesGoTo( goToParameters ), photoTeamMember ).isAccessibleFor() );
	}

	@Test
	public void notLoggedUserCanNotSeeMenuIfThereIsLessThenOnePhotoTest() {

		final GoToParameters goToParameters = new GoToParameters( User.NOT_LOGGED_USER, 1 );

		assertF( new PhotoMenuItemGoToAuthorPhotoByTeamMember( testData.getPhoto(), goToParameters.getAccessor(), getServicesGoTo( goToParameters ), photoTeamMember ).isAccessibleFor() );
	}

	@Test
	public void adminCanSeeMenuIfThereIsMoreThenOnePhotosTest() {

		final GoToParameters goToParameters = new GoToParameters( SUPER_ADMIN_1, 2 );

		assertT( new PhotoMenuItemGoToAuthorPhotoByTeamMember( testData.getPhoto(), goToParameters.getAccessor(), getServicesGoTo( goToParameters ), photoTeamMember ).isAccessibleFor() );
	}

	@Test
	public void adminCanNotSeeMenuIfThereIsLessThenTwoPhotosTest() {

		final GoToParameters goToParameters = new GoToParameters( SUPER_ADMIN_1, 2 );

		assertT( new PhotoMenuItemGoToAuthorPhotoByTeamMember( testData.getPhoto(), goToParameters.getAccessor(), getServicesGoTo( goToParameters ), photoTeamMember ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanNOTSeeMenuIfShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOFFTest() {

		final GoToParameters goToParameters = new GoToParameters( testData.getPhotoAuthor(), 2 );

		assertF( new PhotoMenuItemGoToAuthorPhotoByTeamMember( testData.getPhoto(), goToParameters.getAccessor(), getServicesGoTo( goToParameters ), photoTeamMember ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanNOTSeeMenuIfShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedONAndThereIsLessThenTwoPhotosTest() {

		final GoToParameters goToParameters = new GoToParameters( testData.getPhotoAuthor(), 1 );
		goToParameters.setShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn( true );

		assertF( new PhotoMenuItemGoToAuthorPhotoByTeamMember( testData.getPhoto(), goToParameters.getAccessor(), getServicesGoTo( goToParameters ), photoTeamMember ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanSeeMenuIfShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOnTest() {
		final GoToParameters goToParameters = new GoToParameters( testData.getPhotoAuthor(), 2 );
		goToParameters.setShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn( true );

		assertT( new PhotoMenuItemGoToAuthorPhotoByTeamMember( testData.getPhoto(), goToParameters.getAccessor(), getServicesGoTo( goToParameters ), photoTeamMember ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanNotSeeMenuThePhotoWithinAnonymousPeriodTest() {
		final GoToParameters goToParameters = new GoToParameters( testData.getAccessor(), 2 );
		goToParameters.setPhotoAuthorNameMustBeHidden( true );

		assertF( new PhotoMenuItemGoToAuthorPhotoByTeamMember( testData.getPhoto(), goToParameters.getAccessor(), getServicesGoTo( goToParameters ), photoTeamMember ).isAccessibleFor() );
	}

	@Test
	public void menuIsNotShownIfThereIsLessThenOnePhotosTest() {
		final GoToParameters goToParameters = new GoToParameters( testData.getAccessor(), 1 );

		assertF( new PhotoMenuItemGoToAuthorPhotoByTeamMember( testData.getPhoto(), goToParameters.getAccessor(), getServicesGoTo( goToParameters ), photoTeamMember ).isAccessibleFor() );
	}

	@Test
	public void menuIsShownIfThereIsMoreThenOnePhotosTest() {

		final GoToParameters goToParameters = new GoToParameters( testData.getAccessor(), 2 );

		assertT( new PhotoMenuItemGoToAuthorPhotoByTeamMember( testData.getPhoto(), goToParameters.getAccessor(), getServicesGoTo( goToParameters ), photoTeamMember ).isAccessibleFor() );
	}

	protected ServicesImpl getServicesGoTo( final GoToParameters goToParameters ) {

		final ServicesImpl services = getServices( testData, goToParameters.getAccessor() );

		services.setConfigurationService( getConfigurationServiceGoTo( goToParameters.isShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn() ) );
		services.setSecurityService( getSecurityServiceGoTo( goToParameters.getAccessor(), goToParameters.isPhotoAuthorNameMustBeHidden() ) );
		services.setUserTeamService( getUserTeamService( goToParameters.getTeamMemberPhotosQty() ) );

		return services;
	}

	protected UserTeamService getUserTeamService( final int teamMemberPhotosQty ) {
		final UserTeamService userTeamService = EasyMock.createMock( UserTeamService.class );

		EasyMock.expect( userTeamService.getTeamMemberPhotosQty( userTeamMember.getId() ) ).andReturn( teamMemberPhotosQty ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( userTeamService );

		return userTeamService;
	}

	private PhotoTeamMember getPhotoTeamMember() {
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

	private class GoToParameters {

		private final User accessor;
		private final int teamMemberPhotosQty;
		private boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn;
		private boolean photoAuthorNameMustBeHidden;

		public GoToParameters( final User accessor, final int teamMemberPhotosQty ) {
			this.accessor = accessor;
			this.teamMemberPhotosQty = teamMemberPhotosQty;
		}

		public User getAccessor() {
			return accessor;
		}

		public int getTeamMemberPhotosQty() {
			return teamMemberPhotosQty;
		}

		public boolean isShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn() {
			return showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn;
		}

		public void setShowGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn( final boolean showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn ) {
			this.showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn = showGoToPhotosMenuItemsForMenuCallerOwnEntriesSwitchedOn;
		}

		public boolean isPhotoAuthorNameMustBeHidden() {
			return photoAuthorNameMustBeHidden;
		}

		public void setPhotoAuthorNameMustBeHidden( final boolean photoAuthorNameMustBeHidden ) {
			this.photoAuthorNameMustBeHidden = photoAuthorNameMustBeHidden;
		}
	}
}
