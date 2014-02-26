package menuItems.photo;

import core.general.menus.photo.items.PhotoMenuItemSendPrivateMessage;
import core.general.user.User;
import core.services.entry.FavoritesService;
import core.services.security.SecurityService;
import core.services.security.ServicesImpl;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PhotoMenuItemSendPrivateMessageTest extends AbstractPhotoMenuItemTest_ {

	@Test
	public void notLoggedUserCanNotSeeMenuTest() {
		final User accessor = User.NOT_LOGGED_USER;

		final ServicesImpl services = getServices( accessor );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new PhotoMenuItemSendPrivateMessage( testData.getPhoto(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanNotSeeMenuForOwnPhotoTest() {
		final User accessor = testData.getPhotoAuthor();

		final ServicesImpl services = getServices( accessor );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new PhotoMenuItemSendPrivateMessage( testData.getPhoto(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanNotSeeMenuIfPhotoWithinAnonymousPeriodTest() {
		final User accessor = testData.getAccessor();
		final boolean isPhotoWithinAnonymousPeriod = true;

		final ServicesImpl services = getServices( accessor );
		services.setSecurityService( getSecurityService( accessor, isPhotoWithinAnonymousPeriod ) );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new PhotoMenuItemSendPrivateMessage( testData.getPhoto(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanNotSeeMenuIfHeIsInThePhotoAuthorBlackListTest() {
		final User accessor = testData.getAccessor();
		final boolean isPhotoWithinAnonymousPeriod = false;
		final boolean isAccessorInPhotoAuthorBlackList = true;

		final ServicesImpl services = getServices( accessor );
		services.setSecurityService( getSecurityService( accessor, isPhotoWithinAnonymousPeriod ) );
		services.setFavoritesService( getFavoritesService( isAccessorInPhotoAuthorBlackList ) );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new PhotoMenuItemSendPrivateMessage( testData.getPhoto(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void adminCanSeeMenuTest() {
		final User accessor = SUPER_ADMIN_1;

		final ServicesImpl services = getServices( accessor );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoMenuItemSendPrivateMessage( testData.getPhoto(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanSeeMenuIfHeIsNotInThePhotoAuthorBlackListTest() {
		final User accessor = testData.getAccessor();
		final boolean isPhotoWithinAnonymousPeriod = false;
		final boolean isAccessorInPhotoAuthorBlackList = false;

		final ServicesImpl services = getServices( accessor );
		services.setSecurityService( getSecurityService( accessor, isPhotoWithinAnonymousPeriod ) );
		services.setFavoritesService( getFavoritesService( isAccessorInPhotoAuthorBlackList ) );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoMenuItemSendPrivateMessage( testData.getPhoto(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void adminCanSeeMenuIfHeIsInThePhotoAuthorBlackListTest() {
		final User accessor = SUPER_ADMIN_1;
		final boolean isPhotoWithinAnonymousPeriod = false;
		final boolean isAccessorInPhotoAuthorBlackList = true;

		final ServicesImpl services = getServices( accessor );
		services.setSecurityService( getSecurityService( accessor, isPhotoWithinAnonymousPeriod ) );
		services.setFavoritesService( getFavoritesService( isAccessorInPhotoAuthorBlackList ) );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoMenuItemSendPrivateMessage( testData.getPhoto(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void adminCanSeeMenuIfPhotoWithinAnonymousPeriodTest() {
		final User accessor = SUPER_ADMIN_1;
		final boolean isPhotoWithinAnonymousPeriod = true;
		final boolean isAccessorInPhotoAuthorBlackList = false;

		final ServicesImpl services = getServices( accessor );
		services.setSecurityService( getSecurityService( accessor, isPhotoWithinAnonymousPeriod ) );
		services.setFavoritesService( getFavoritesService( isAccessorInPhotoAuthorBlackList ) );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new PhotoMenuItemSendPrivateMessage( testData.getPhoto(), accessor, services ).isAccessibleFor() );
	}

	private SecurityService getSecurityService( final User accessor, final boolean isPhotoWithinAnonymousPeriod ) {
		final boolean isAdmin = SUPER_ADMIN_2.getId() == accessor.getId() || SUPER_ADMIN_1.getId() == accessor.getId();

		final SecurityService securityService = EasyMock.createMock( SecurityService.class );

		EasyMock.expect( securityService.isSuperAdminUser( accessor.getId() ) ).andReturn( isAdmin ).anyTimes();
		EasyMock.expect( securityService.isSuperAdminUser( accessor ) ).andReturn( isAdmin ).anyTimes();
		EasyMock.expect( securityService.userOwnThePhoto( accessor, testData.getPhoto().getId() ) ).andReturn( testData.getPhotoAuthor().getId() == accessor.getId() ).anyTimes();
		EasyMock.expect( securityService.isPhotoAuthorNameMustBeHidden( testData.getPhoto(), accessor ) ).andReturn( isPhotoWithinAnonymousPeriod ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( securityService );

		return securityService;
	}

	private FavoritesService getFavoritesService( final boolean isInBlackList ) {
		final FavoritesService favoritesService = EasyMock.createMock( FavoritesService.class );

		EasyMock.expect( favoritesService.isUserInBlackListOfUser( testData.getPhoto().getUserId(), testData.getAccessor().getId() ) ).andReturn( isInBlackList ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( favoritesService );

		return favoritesService;
	}
}
