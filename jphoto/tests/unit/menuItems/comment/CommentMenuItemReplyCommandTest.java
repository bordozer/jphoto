package menuItems.comment;

import core.general.menus.comment.items.CommentMenuItemReply;
import core.general.photo.PhotoComment;
import core.general.user.User;
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
		final User user = testData.getJustUser();
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
	public void replyToJSFunctionTest() {
		final User user = testData.getJustUser(); // Does not matter
		final Services services = getServicesForTest( user, false );

		assertEquals( WRONG_COMMAND, new CommentMenuItemReply( testData.getComment(), user, services ).getMenuItemCommand().getMenuCommand(), String.format( "replyToComment( %d ); return false;", testData.getCommentAuthor().getId() ) );
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
}
