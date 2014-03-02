package menuItems.command;

import common.AbstractTestCase;
import core.general.menus.AbstractEntryMenuItemComplaintCommand;
import core.general.menus.EntryMenuType;
import core.general.menus.PopupMenuAssignable;
import core.general.menus.comment.ComplaintReasonType;
import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.general.user.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AbstractEntryMenuItemComplaintCommandTest extends AbstractTestCase {

	@Before
	public void setup() {
		super.setup();
	}

	@Test
	public void PhotoMenuItemComplaintTest() {

		final Photo photo = new Photo();
		photo.setId( 555 );

		final EntryMenuType comment = EntryMenuType.COMMENT;
		final ComplaintReasonType complaintReasonType = ComplaintReasonType.COMMENT_CUSTOM_COMPLAINT;

		doAssert( photo, comment, complaintReasonType );
	}

	@Test
	public void CommentMenuItemComplaintCustomTest() {

		final PhotoComment photoComment = new PhotoComment();
		photoComment.setId( 666 );

		final EntryMenuType comment = EntryMenuType.COMMENT;
		final ComplaintReasonType complaintReasonType = ComplaintReasonType.COMMENT_CUSTOM_COMPLAINT;

		doAssert( photoComment, comment, complaintReasonType );
	}

	@Test
	public void CommentMenuItemComplaintSpamTest() {

		final PhotoComment photoComment = new PhotoComment();
		photoComment.setId( 888 );

		final EntryMenuType comment = EntryMenuType.COMMENT;
		final ComplaintReasonType complaintReasonType = ComplaintReasonType.COMMENT_SPAM;

		doAssert( photoComment, comment, complaintReasonType );
	}

	private <T extends PopupMenuAssignable>void doAssert( final T menuEntry, final EntryMenuType comment, final ComplaintReasonType complaintReasonType ) {

		final User accessor = new User( 222 );
		accessor.setName( "Just a User" );

		final AbstractEntryMenuItemComplaintCommand menuItem = new AbstractEntryMenuItemComplaintCommand<T>( menuEntry, accessor, comment, complaintReasonType ) {

			@Override
			public String getMenuText() {
				return null; // Does not matter
			}
		};

		assertEquals( menuItem.getMenuCommand()
			, String.format( "sendComplaintMessage( %d, %d, %d, %d ); return false;", comment.getId(), menuEntry.getId(), accessor.getId(), complaintReasonType.getId() )
		);
	}
}