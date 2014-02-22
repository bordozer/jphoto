package core.services.menu;

import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.general.menus.EntryMenu;
import core.general.menus.EntryMenuOperationType;

import java.util.List;

public interface EntryMenuService {

	EntryMenu getCommentMenu( final PhotoComment photoComment, final User accessor, final List<EntryMenuOperationType> entryMenuOperationTypes );

	EntryMenu getPhotoMenu( final Photo photo, final User accessor );

	EntryMenu getPhotoMenu( final Photo photo, final User accessor, final List<EntryMenuOperationType> entryMenuOperationTypes );

	EntryMenu getUserMenu( final User user, final User accessor );

	EntryMenu getUserMenu( final User user, final User accessor, final List<EntryMenuOperationType> entryMenuOperationTypes );

	List<EntryMenuOperationType> getCommentComplaintOnlyMenuItems();

	List<EntryMenuOperationType> getPhotoFullMenuItems();

	List<EntryMenuOperationType> getCommentFullMenuItems();
}
