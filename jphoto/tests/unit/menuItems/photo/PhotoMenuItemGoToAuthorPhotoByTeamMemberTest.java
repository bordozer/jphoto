package menuItems.photo;

import core.enums.UserTeamMemberType;
import core.general.menus.AbstractEntryMenuItem;
import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.photo.items.PhotoMenuItemGoToAuthorPhotoByTeamMember;
import core.general.photoTeam.PhotoTeamMember;
import core.general.user.User;
import core.general.user.userTeam.UserTeamMember;
import core.services.security.ServicesImpl;
import core.services.user.UserTeamService;
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

		assertEquals( WRONG_COMMAND
			, command.getMenuText()
			, String.format( "%s: photos with %s %s ( %d )", photoAuthor.getNameEscaped(), userTeamMember.getTeamMemberType().getNameTranslated().toLowerCase(), userTeamMember.getTeamMemberName(), parameters.getPhotosQty() )
		);

		assertEquals( WRONG_COMMAND, command.getMenuCommand(), String.format( "goToMemberPhotosByTeamMember( %d, %d );", photoAuthor.getId(), userTeamMember.getId() ) );

		assertEquals( WRONG_COMMAND, menuItem.getMenuCssClass(), AbstractEntryMenuItem.DEFAULT_CSS_CLASS );
	}

	private ServicesImpl getServicesGoTo( final GoToParameters parameters ) {
		final ServicesImpl services = getServices( testData, parameters.getAccessor() );
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
