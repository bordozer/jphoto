package menuItems.comment;

import core.general.configuration.ConfigurationKey;
import core.general.menus.comment.items.CommentMenuItemReply;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.general.user.UserStatus;
import core.services.entry.FavoritesService;
import core.services.security.Services;
import core.services.security.ServicesImpl;
import core.services.system.ConfigurationService;
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
		final User accessor = testData.getAccessor();
		accessor.setUserStatus( UserStatus.CANDIDATE );

		final boolean candidatesCanCommentPhotos = false;

		final ServicesImpl services = getServices( testData, accessor );
		services.setConfigurationService( getConfigurationService( candidatesCanCommentPhotos ) );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemReply( testData.getComment(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void menuIsInaccessibleIfAccessorIsInTheBlackListOfCommentAuthorTest() {
		final User accessor = testData.getAccessor();

//		final boolean isUserInBlackListOfUser = true;

		final ServicesImpl services = getServices( testData, accessor );
//		services.setFavoritesService( getFavoritesService( accessor, isUserInBlackListOfUser ) );

		assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, new CommentMenuItemReply( testData.getComment(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void menuIsAccessibleIfAccessorIsNotInTheBlackListOfCommentAuthorTest() {
		final User accessor = testData.getAccessor();

//		final boolean isUserInBlackListOfUser = false;

		final ServicesImpl services = getServices( testData, accessor );
//		services.setFavoritesService( getFavoritesService( accessor, isUserInBlackListOfUser ) );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentMenuItemReply( testData.getComment(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void menuIsAccessibleIfAccessorIsCandidateAndVotingIsAllowedForCandidatesTest() {
		final User accessor = testData.getAccessor();
		accessor.setUserStatus( UserStatus.CANDIDATE );

		final boolean candidatesCanCommentPhotos = true;
//		final boolean isUserInBlackListOfUser = false;

		final ServicesImpl services = getServices( testData, accessor );
		services.setConfigurationService( getConfigurationService( candidatesCanCommentPhotos ) );
//		services.setFavoritesService( getFavoritesService( accessor, isUserInBlackListOfUser ) );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentMenuItemReply( testData.getComment(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void photoAuthorCanReplyCommentTest() {
		final User accessor = testData.getPhotoAuthor();
//		final boolean isUserInBlackListOfUser = false;

		final ServicesImpl services = getServices( testData, accessor );
//		services.setFavoritesService( getFavoritesService( accessor, isUserInBlackListOfUser ) );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentMenuItemReply( testData.getComment(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void usualUserCanReplyCommentTest() {
		final User accessor = testData.getAccessor();
//		final boolean isUserInBlackListOfUser = false;

		final ServicesImpl services = getServices( testData, accessor );
//		services.setFavoritesService( getFavoritesService( accessor, isUserInBlackListOfUser ) );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentMenuItemReply( testData.getComment(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void adminCanReplyCommentTest() {
		final User accessor = SUPER_ADMIN_1;
//		final boolean isUserInBlackListOfUser = false;

		final ServicesImpl services = getServices( testData, accessor );
//		services.setFavoritesService( getFavoritesService( accessor, isUserInBlackListOfUser ) );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentMenuItemReply( testData.getComment(), accessor, services ).isAccessibleFor() );
	}

	@Test
	public void everyoneCanReplyOnDeletedCommentTest() {
		final User accessor = testData.getAccessor();
//		final boolean isUserInBlackListOfUser = false;

		final ServicesImpl services = getServices( testData, accessor );
//		services.setFavoritesService( getFavoritesService( accessor, isUserInBlackListOfUser ) );

		final PhotoComment comment = testData.getComment();
		comment.setCommentDeleted( true );

		assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, new CommentMenuItemReply( comment, accessor, services ).isAccessibleFor() );
	}

	private ConfigurationService getConfigurationService( final boolean candidatesCanCommentPhotos ) {
		final ConfigurationService configurationService = EasyMock.createMock( ConfigurationService.class );

		EasyMock.expect( configurationService.getBoolean( ConfigurationKey.CANDIDATES_CAN_COMMENT_PHOTOS ) ).andReturn( candidatesCanCommentPhotos ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( configurationService );

		return configurationService;
	}

	/*private FavoritesService getFavoritesService( final User accessor, final boolean isUserInBlackListOfUser ) {
		final FavoritesService favoritesService = EasyMock.createMock( FavoritesService.class );

		EasyMock.expect( favoritesService.isUserInBlackListOfUser( testData.getCommentAuthor().getId(), accessor.getId() ) ).andReturn( isUserInBlackListOfUser ).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.replay( favoritesService );

		return favoritesService;
	}*/
}
