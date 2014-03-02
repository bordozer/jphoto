package menuItems.photo.complaint;

import core.general.menus.EntryMenuOperationType;
import core.general.menus.EntryMenuType;
import core.general.menus.comment.ComplaintReasonType;
import core.general.menus.photo.complain.PhotoComplaintCopyrightMenuItem;
import core.general.user.User;
import menuItems.photo.AbstractPhotoMenuItemTest_;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PhotoComplaintCopyrightMenuItemTest extends AbstractPhotoMenuItemTest_ {

	@Test
	public void menuCommandTextTest() {
		final User accessor = testData.getAccessor();

		final PhotoComplaintCopyrightMenuItem menuItem = new PhotoComplaintCopyrightMenuItem( testData.getPhoto(), accessor, getServices( accessor ) );

		assertEquals( menuItem.getMenuItemCommand().getMenuText(), translated( "Complaint copyright" ) );
	}

	@Test
	public void menuCommandTest() {
		final User accessor = testData.getAccessor();

		final PhotoComplaintCopyrightMenuItem menuItem = new PhotoComplaintCopyrightMenuItem( testData.getPhoto(), accessor, getServices( accessor ) );

		assertEquals( menuItem.getMenuItemCommand().getMenuCommand()
			, String.format( "sendComplaintMessage( %d, %d, %d, %d ); return false;"
				, EntryMenuType.PHOTO.getId()
				, testData.getPhoto().getId()
				, accessor.getId()
				, ComplaintReasonType.PHOTO_COPYRIGHT_COMPLAINT.getId()
			)
		);

		assertEquals( menuItem.getEntryMenuType(), EntryMenuOperationType.PHOTO_COMPLAINT_COPYRIGHT );
	}
}
