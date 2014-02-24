package menuItems.comment;

import core.general.configuration.ConfigurationKey;
import core.general.menus.comment.items.CommentMenuItemReply;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.general.user.UserStatus;
import core.services.security.Services;
import core.services.security.ServicesImpl;
import core.services.system.ConfigurationService;
import core.services.system.ConfigurationServiceImpl;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CommentMenuItemReplyTest extends AbstractCommentMenuItemTest_ {

	@Test
	public void notLoggedUserCanNotReplyCommentTest() {
		final User user = User.NOT_LOGGED_USER;
		final Services services = getServices( testData, user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemReply( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void commentAuthorCanNotReplyCommentTest() {
		final User user = testData.getCommentAuthor();
		final Services services = getServices( testData, user );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemReply( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void menuIsInaccessibleIfAccessorIsCandidateAndVotingIsNotAllowedForCandidatesTest() {
		final User user = testData.getAccessor();
		user.setUserStatus( UserStatus.CANDIDATE );

		final boolean candidatesCanCommentPhotos = false;

		final ServicesImpl services = getServices( testData, user );
		services.setConfigurationService( getConfigurationService( candidatesCanCommentPhotos ) );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemReply( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void menuIsAccessibleIfAccessorIsCandidateAndVotingIsAllowedForCandidatesTest() {
		final User user = testData.getAccessor();
		user.setUserStatus( UserStatus.CANDIDATE );

		final boolean candidatesCanCommentPhotos = true;

		final ServicesImpl services = getServices( testData, user );
		services.setConfigurationService( getConfigurationService( candidatesCanCommentPhotos ) );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentMenuItemReply( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanReplyCommentTest() {
		final User user = testData.getPhotoAuthor();
		final Services services = getServices( testData, user );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentMenuItemReply( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanReplyCommentTest() {
		final User user = testData.getAccessor();
		final Services services = getServices( testData, user );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentMenuItemReply( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void adminCanReplyCommentTest() {
		final User user = SUPER_ADMIN_1;
		final Services services = getServices( testData, user );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentMenuItemReply( testData.getComment(), user, services ).isAccessibleFor() );
	}

	@Test
	public void everyoneCanReplyOnDeletedCommentTest() {
		final User user = testData.getAccessor();
		final Services services = getServices( testData, user );

		final PhotoComment comment = testData.getComment();
		comment.setCommentDeleted( true );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentMenuItemReply( comment, user, services ).isAccessibleFor() );
	}

	protected ConfigurationService getConfigurationService( final Boolean candidatesCanCommentPhotos ) {
		final ConfigurationService configurationService = EasyMock.createMock( ConfigurationService.class );

		EasyMock.expect( configurationService.getBoolean( ConfigurationKey.CANDIDATES_CAN_COMMENT_PHOTOS ) ).andReturn( candidatesCanCommentPhotos ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( configurationService );

		return configurationService;
	}
}
