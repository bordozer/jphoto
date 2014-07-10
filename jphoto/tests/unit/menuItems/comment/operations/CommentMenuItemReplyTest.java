package menuItems.comment.operations;

import common.AbstractTestCase;
import ui.services.menu.entry.items.comment.operations.CommentMenuItemReply;
import core.general.photo.ValidationResult;
import core.general.user.User;
import core.services.security.SecurityService;
import core.services.system.ServicesImpl;
import menuItems.comment.AbstractCommentMenuItemTest_;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CommentMenuItemReplyTest extends AbstractCommentMenuItemTest_ {

	@Test
	public void menuIsInaccessibleIfUserCanNotCommentThePhotoTest() {
		final User accessor = testData.getAccessor();
		final boolean userCanCommentPhoto = false;
		final boolean accessorInTheBlackListOfCommentAuthor = false;

		final ReplyParameters parameters = new ReplyParameters( accessor, userCanCommentPhoto, accessorInTheBlackListOfCommentAuthor );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemReply( testData.getComment(), parameters.getAccessor(), getServicesReply( parameters ) ).isAccessibleFor() );
	}

	@Test
	public void menuIsAccessibleIfUserCanCommentThePhotoButInTheBlackListOfCommentAuthorTest() {
		final User accessor = testData.getAccessor();
		final boolean userCanCommentPhoto = true;
		final boolean accessorInTheBlackListOfCommentAuthor = true;

		final ReplyParameters parameters = new ReplyParameters( accessor, userCanCommentPhoto, accessorInTheBlackListOfCommentAuthor );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentMenuItemReply( testData.getComment(), parameters.getAccessor(), getServicesReply( parameters ) ).isAccessibleFor() );
	}

	@Test
	public void menuIsAccessibleIfUserCanCommentThePhotoAndNotInTheBlackListOfCommentAuthorTest() {
		final User accessor = testData.getAccessor();
		final boolean userCanCommentPhoto = true;
		final boolean accessorInTheBlackListOfCommentAuthor = true;

		final ReplyParameters parameters = new ReplyParameters( accessor, userCanCommentPhoto, accessorInTheBlackListOfCommentAuthor );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentMenuItemReply( testData.getComment(), parameters.getAccessor(), getServicesReply( parameters ) ).isAccessibleFor() );
	}

	private ServicesImpl getServicesReply( final ReplyParameters parameters ) {
		final ServicesImpl services = getServices( parameters.getAccessor() );

		services.setSecurityService( getSecurityServiceReply( parameters ) );

		return services;
	}

	private SecurityService getSecurityServiceReply( final ReplyParameters parameters ) {
		final User accessor = parameters.getAccessor();

		final SecurityService securityService = EasyMock.createMock( SecurityService.class );

		EasyMock.expect( securityService.userOwnThePhotoComment( accessor, testData.getComment() ) ).andReturn( testData.getComment().getCommentAuthor().getId() == accessor.getId() ).anyTimes();

		EasyMock.expect( securityService.userOwnThePhoto( accessor, testData.getPhoto() ) ).andReturn( testData.getPhoto().getUserId() == accessor.getId() ).anyTimes();

		EasyMock.expect( securityService.isSuperAdminUser( accessor.getId() ) ).andReturn( SUPER_ADMIN_2.getId() == accessor.getId() || SUPER_ADMIN_1.getId() == accessor.getId() ).anyTimes();
		EasyMock.expect( securityService.isSuperAdminUser( accessor ) ).andReturn( SUPER_ADMIN_2.getId() == accessor.getId() || SUPER_ADMIN_1.getId() == accessor.getId() ).anyTimes();

		final ValidationResult validationResult = new ValidationResult();
		validationResult.setValidationPassed( parameters.isUserCanCommentPhoto() );

		EasyMock.expect( securityService.validateUserCanCommentPhoto( accessor, testData.getPhoto(), AbstractTestCase.MENU_LANGUAGE ) ).andReturn( validationResult ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( securityService );

		return securityService;
	}

	private class ReplyParameters {

		private final User accessor;
		private final boolean userCanCommentPhoto;
		private boolean accessorInTheBlackListOfCommentAuthor;

		public ReplyParameters( final User accessor, final boolean userCanCommentPhoto, final boolean isAccessorInTheBlackListOfCommentAuthor ) {
			this.accessor = accessor;
			this.userCanCommentPhoto = userCanCommentPhoto;
			this.accessorInTheBlackListOfCommentAuthor = isAccessorInTheBlackListOfCommentAuthor;
		}

		public User getAccessor() {
			return accessor;
		}

		public boolean isUserCanCommentPhoto() {
			return userCanCommentPhoto;
		}

		public boolean isAccessorInTheBlackListOfCommentAuthor() {
			return accessorInTheBlackListOfCommentAuthor;
		}
	}
}
