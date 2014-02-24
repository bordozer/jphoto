package menuItems.comment;

import core.general.menus.comment.items.CommentMenuItemReply;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.entry.FavoritesService;
import core.services.security.SecurityService;
import core.services.security.Services;
import core.services.security.ServicesImpl;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CommentMenuItemReplyCommandTest extends AbstractCommentMenuItemTest_ {

	@Test
	public void replyToUsualUserInNotAnonymousPeriodTest() {
		final User user = testData.getCommentAuthor();

		final Services services = getServicesForTest( user, false );

		assertEquals( WRONG_COMMAND, new CommentMenuItemReply( testData.getComment(), user, services ).getMenuItemCommand().getMenuText(), String.format( "Reply to %s", user.getNameEscaped() ) );
	}

	@Test
	public void replyToPhotoAuthorInNotAnonymousPeriodTest() {
		final User user = testData.getPhotoAuthor();

		final Services services = getServicesForTest( user, false );

		final PhotoComment comment = testData.getComment();
		comment.setCommentAuthor( user );

		assertEquals( WRONG_COMMAND, new CommentMenuItemReply( comment, user, services ).getMenuItemCommand().getMenuText(), String.format( "Reply to %s ( photo's author )", user.getNameEscaped() ) );
	}

	@Test
	public void replyToUsualUserInAnonymousPeriodTest() {
		final User user = testData.getAccessor();

		final Services services = getServicesForTest( user, true );

		assertEquals( WRONG_COMMAND, new CommentMenuItemReply( testData.getComment(), user, services ).getMenuItemCommand().getMenuText(), "Reply to photo author ( anonymous )" );
	}

	@Test
	public void replyToPhotoAuthorInAnonymousPeriodTest() {
		final User user = testData.getPhotoAuthor();

		final Services services = getServicesForTest( user, true );

		final PhotoComment comment = testData.getComment();
		comment.setCommentAuthor( user );

		assertEquals( WRONG_COMMAND, new CommentMenuItemReply( comment, user, services ).getMenuItemCommand().getMenuText(), "Reply to photo author ( anonymous )" );
	}

	@Test
	public void commandFunctionIfAccessorIsNOTInTheBlackListOfCommentAuthorTest() {
		final User accessor = testData.getAccessor(); // Does not matter
		final boolean isUserInBlackListOfUser = false; // is NOT in the black list!

		final ServicesImpl services = getServicesForTest( accessor, false );
		services.setFavoritesService( getFavoritesService( accessor, isUserInBlackListOfUser ) );

		assertEquals( WRONG_COMMAND
			, new CommentMenuItemReply( testData.getComment(), accessor, services ).getMenuItemCommand().getMenuCommand()
			, String.format( "replyToComment( %d ); return false;", testData.getComment().getId() ) );
	}

	@Test
	public void commandFunctionIfAccessorIsInTheBlackListOfCommentAuthorTest() {
		final User accessor = testData.getAccessor(); // Does not matter
		final boolean isUserInBlackListOfUser = true; // in the black list!

		final ServicesImpl services = getServicesForTest( accessor, false );
		services.setFavoritesService( getFavoritesService( accessor, isUserInBlackListOfUser ) );

		assertEquals( WRONG_COMMAND
			, new CommentMenuItemReply( testData.getComment(), accessor, services ).getMenuItemCommand().getMenuCommand()
			, String.format( "showInformationMessageNoAutoClose( 'You are in the black list of %s. You can not reply.' )", testData.getCommentAuthor().getNameEscaped() ) );
	}

	private ServicesImpl getServicesForTest( final User user, final Boolean isCommentAuthorNameInAnonymousPeriod ) {
		final ServicesImpl services = getServices( testData, user );

		services.setSecurityService( getSecurityServiceForTest( user, isCommentAuthorNameInAnonymousPeriod ) );

		return services;
	}

	private SecurityService getSecurityServiceForTest( final User user, final Boolean isCommentAuthorNameInAnonymousPeriod ) {
		final SecurityService securityService = EasyMock.createMock( SecurityService.class );

		EasyMock.expect( securityService.isCommentAuthorMustBeHiddenBecauseThisIsCommentOfPhotoAuthorAndPhotoIsWithinAnonymousPeriod( testData.getComment(), user ) ).andReturn( isCommentAuthorNameInAnonymousPeriod ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( securityService );

		return securityService;
	}

	private FavoritesService getFavoritesService( final User accessor, final boolean isUserInBlackListOfUser ) {
		final FavoritesService favoritesService = EasyMock.createMock( FavoritesService.class );

		EasyMock.expect( favoritesService.isUserInBlackListOfUser( testData.getCommentAuthor().getId(), accessor.getId() ) ).andReturn( isUserInBlackListOfUser ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( favoritesService );

		return favoritesService;
	}
}
