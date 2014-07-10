package ui.services.menu.entry;

import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.general.user.User;
import ui.services.menu.entry.items.EntryMenu;
import ui.services.menu.entry.items.EntryMenuOperationType;

import java.util.List;

public interface EntryMenuService {

	EntryMenu getUserMenu( final User user, final User accessor );

	EntryMenu getUserMenu( final User user, final User accessor, final List<EntryMenuOperationType> entryMenuOperationTypes );

	EntryMenu getPhotoMenu( final Photo photo, final User accessor );

	EntryMenu getPhotoMenu( final Photo photo, final User accessor, final List<EntryMenuOperationType> entryMenuOperationTypes );

	EntryMenu getCommentMenu( final PhotoComment photoComment, final User accessor );

	EntryMenu getCommentMenu( final PhotoComment photoComment, final User accessor, final List<EntryMenuOperationType> entryMenuOperationTypes );

	List<EntryMenuOperationType> getPhotoFullMenuItems();

	List<EntryMenuOperationType> getCommentFullMenuItems();

	List<EntryMenuOperationType> getCommentComplaintOnlyMenuItems();
}
