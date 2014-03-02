package menuItems.photo.goTo;

import core.enums.UserTeamMemberType;
import core.general.menus.AbstractEntryMenuItem;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.photo.goTo.PhotoMenuItemGoToAuthorPhotoByTeamMember;
import core.general.photoTeam.PhotoTeamMember;
import core.general.user.User;
import core.general.user.userTeam.UserTeamMember;
import core.services.security.ServicesImpl;
import core.services.user.UserTeamService;
import menuItems.photo.AbstractPhotoMenuItemTest_;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
	public void commandTest() {
		final GoToParameters parameters = new GoToParameters( testData.getAccessor(), 7 );

		final ServicesImpl services = getServicesGoTo( parameters );

		final PhotoMenuItemGoToAuthorPhotoByTeamMember menuItem = new PhotoMenuItemGoToAuthorPhotoByTeamMember( testData.getPhoto(), parameters.getAccessor(), services, photoTeamMember );
		final AbstractEntryMenuItemCommand command = menuItem.getMenuItemCommand();

		final User photoAuthor = testData.getPhotoAuthor();

		assertEquals( EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuText(), translated( String.format( "%s: photos with %s %s ( %d )", photoAuthor.getNameEscaped(), userTeamMember.getTeamMemberType().getNameTranslated().toLowerCase(), userTeamMember.getTeamMemberName(), parameters.getPhotosQty() ) ) );

		assertEquals( EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, command.getMenuCommand(), String.format( "goToMemberPhotosByTeamMember( %d, %d );", photoAuthor.getId(), userTeamMember.getId() ) );

		assertEquals( EXPECTED_AND_ACTUAL_RESULTS_ARE_DIFFERENT, menuItem.getMenuCssClass(), AbstractEntryMenuItem.MENU_ITEM_CSS_CLASS_DEFAULT );
	}

	private ServicesImpl getServicesGoTo( final GoToParameters parameters ) {
		final ServicesImpl services = getServices( parameters.getAccessor() );
		services.setUserTeamService( getUserTeamService( parameters.getPhotosQty() ) );

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
}
