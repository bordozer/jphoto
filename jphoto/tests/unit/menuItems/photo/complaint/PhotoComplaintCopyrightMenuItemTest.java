package menuItems.photo.complaint;

import ui.services.menu.entry.items.EntryMenuOperationType;
import ui.services.menu.entry.items.EntryMenuType;
import ui.services.menu.entry.items.comment.ComplaintReasonType;
import ui.services.menu.entry.items.photo.complain.PhotoComplaintCopyrightMenuItem;
import core.general.user.User;
import menuItems.photo.AbstractPhotoMenuItemTest_;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PhotoComplaintCopyrightMenuItemTest extends AbstractPhotoMenuItemTest_ {

	@Test
	public void menuCommandTextTest() {
		final User accessor = testData.getAccessor();

		final PhotoComplaintCopyrightMenuItem menuItem = new PhotoComplaintCopyrightMenuItem( testData.getPhoto(), accessor, getServices( accessor ) );

		assertEquals( menuItem.getMenuItemCommand().getMenuText(), translated( "PhotoMenuItem: Complaint copyright" ) );
	}

	@Test
	public void menuCommandTest() {
		final User accessor = testData.getAccessor();

		final PhotoComplaintCopyrightMenuItem menuItem = new PhotoComplaintCopyrightMenuItem( testData.getPhoto(), accessor, getServices( accessor ) );

		assertEquals( menuItem.getMenuItemCommand().getMenuCommand()
			, String.format( "sendComplaintMessage( %d, %d, %d, %d );"
				, EntryMenuType.PHOTO.getId()
				, testData.getPhoto().getId()
				, accessor.getId()
				, ComplaintReasonType.PHOTO_COPYRIGHT_COMPLAINT.getId()
			)
		);

		assertEquals( menuItem.getEntryMenuType(), EntryMenuOperationType.PHOTO_COMPLAINT_COPYRIGHT );
	}
}
