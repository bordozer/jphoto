package menuItems.comment;

import core.general.menus.AbstractEntryMenuItemCommand;
import core.general.menus.comment.items.CommentMenuItemSendPrivateMessage;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.entry.FavoritesService;
import core.services.security.SecurityService;
import core.services.security.Services;
import core.services.security.ServicesImpl;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CommentMenuItemSendPrivateMessageTest extends AbstractCommentMenuItemTest_ {

	@Test
	public void notLoggedUserCanNotReplyCommentTest() {
		final User accessor = User.NOT_LOGGED_USER;

		final Services services = getServices( testData, accessor );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemSendPrivateMessage( testData.getComment(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void accessorCanNotSeeMenuForHisOwnCommentsTest() {
		final User accessor = testData.getCommentAuthor();

		final Services services = getServices( testData, accessor );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemSendPrivateMessage( testData.getComment(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void adminCanNotSeeMenuTest() {
		final User accessor = SUPER_ADMIN_1;
		final boolean isAccessorInPhotoAuthorBlackList = true;

		final ServicesImpl services = getServices( testData, accessor );
		services.setFavoritesService( getFavoritesService( testData.getCommentAuthor(), accessor, isAccessorInPhotoAuthorBlackList ) );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemSendPrivateMessage( testData.getComment(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorAlwaysCanSeeMenuButHeIsInTheBlackListOfCommentAuthorTest() {
		final User accessor = testData.getPhotoAuthor();
		final boolean isAccessorInPhotoAuthorBlackList = false;

		final ServicesImpl services = getServices( testData, accessor );
		services.setFavoritesService( getFavoritesService( testData.getCommentAuthor(), accessor, isAccessorInPhotoAuthorBlackList ) );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentMenuItemSendPrivateMessage( testData.getComment(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanNotSeeMenuIfHeIsInTheBlackListOfCommentAuthorTest() {
		final User accessor = testData.getPhotoAuthor();
		final boolean isAccessorInPhotoAuthorBlackList = true;

		final ServicesImpl services = getServices( testData, accessor );
		services.setFavoritesService( getFavoritesService( testData.getCommentAuthor(), accessor, isAccessorInPhotoAuthorBlackList ) );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemSendPrivateMessage( testData.getComment(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanNotSeeMenuIfCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriodTest() {
		final User accessor = testData.getAccessor();
		final boolean isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod = true;
		final boolean isAccessorInPhotoAuthorBlackList = false;

		final ServicesImpl services = getServices( testData, accessor );
		services.setFavoritesService( getFavoritesService( testData.getPhotoAuthor(), accessor, isAccessorInPhotoAuthorBlackList ) );
		services.setSecurityService( getSecurityService( accessor, isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod ) );

		final PhotoComment comment = testData.getComment();
		comment.setCommentAuthor( testData.getPhotoAuthor() );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemSendPrivateMessage( comment, accessor, services ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanSeeMenuIfCommentNotOfPhotoAuthorAndPhotoIsWithinAnonymousPeriodTest() {
		final User accessor = testData.getAccessor();
		final boolean isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod = false;
		final boolean isAccessorInPhotoAuthorBlackList = false;

		final ServicesImpl services = getServices( testData, accessor );
		services.setFavoritesService( getFavoritesService( testData.getCommentAuthor(), accessor, isAccessorInPhotoAuthorBlackList ) );
		services.setSecurityService( getSecurityService( accessor, isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod ) );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentMenuItemSendPrivateMessage( testData.getComment(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void adminCanNotSeeMenuIfCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriodTest() {
		final User accessor = SUPER_ADMIN_1;
		final boolean isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod = true;
		final boolean isAccessorInPhotoAuthorBlackList = false;

		final ServicesImpl services = getServices( testData, accessor );
		services.setFavoritesService( getFavoritesService( testData.getPhotoAuthor(), accessor, isAccessorInPhotoAuthorBlackList ) );
		services.setSecurityService( getSecurityService( accessor, isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod ) );

		final PhotoComment comment = testData.getComment();
		comment.setCommentAuthor( testData.getPhotoAuthor() );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemSendPrivateMessage( comment, accessor, services ).isAccessibleFor() );
	}

	@Test
	public void commandTest() {
		final User accessor = SUPER_ADMIN_1;
		final Services services = getServices( testData, accessor );

		final AbstractEntryMenuItemCommand command = new CommentMenuItemSendPrivateMessage( testData.getComment(), accessor, services ).getMenuItemCommand();

		assertEquals( WRONG_COMMAND, command.getMenuText(), String.format( "Send private message to %s", testData.getCommentAuthor().getNameEscaped() ) );
		assertEquals( WRONG_COMMAND, command.getMenuCommand(), String.format( "sendPrivateMessage( %d, %d, '%s' );", accessor.getId(), testData.getCommentAuthor().getId(), testData.getCommentAuthor().getNameEscaped() ) );
	}

	private FavoritesService getFavoritesService( final User blackListOwner, final User accessor, final boolean isInBlackList ) {
		final FavoritesService favoritesService = EasyMock.createMock( FavoritesService.class );

		EasyMock.expect( favoritesService.isUserInBlackListOfUser( blackListOwner.getId(), accessor.getId() ) ).andReturn( isInBlackList ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( favoritesService );

		return favoritesService;
	}

	private SecurityService getSecurityService( final User accessor, final boolean isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod ) {
		final SecurityService securityService = EasyMock.createMock( SecurityService.class );

		EasyMock.expect( securityService.userOwnThePhotoComment( accessor, testData.getComment() ) ).andReturn( testData.getComment().getCommentAuthor().getId() == accessor.getId() ).anyTimes();

		EasyMock.expect( securityService.userOwnThePhoto( accessor, testData.getPhoto() ) ).andReturn( testData.getPhoto().getUserId() == accessor.getId() ).anyTimes();

		EasyMock.expect( securityService.isSuperAdminUser( accessor.getId() ) ).andReturn( SUPER_ADMIN_2.getId() == accessor.getId() || SUPER_ADMIN_1.getId() == accessor.getId() ).anyTimes();
		EasyMock.expect( securityService.isSuperAdminUser( accessor ) ).andReturn( SUPER_ADMIN_2.getId() == accessor.getId() || SUPER_ADMIN_1.getId() == accessor.getId() ).anyTimes();

		EasyMock.expect( securityService.isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod( testData.getComment(), accessor ) ).andReturn( isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( securityService );

		return securityService;
	}
}
