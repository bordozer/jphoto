package core.services.entry;

import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.general.menus.EntryMenu;
import core.general.menus.EntryMenuOperationType;

import java.util.List;

public interface EntryMenuService {

	EntryMenu getCommentMenu( final PhotoComment photoComment, final User userWhoIsCallingMenu, final List<EntryMenuOperationType> entryMenuOperationTypes );

	EntryMenu getPhotoMenu( final Photo photo, final User userWhoIsCallingMenu );

	EntryMenu getPhotoMenu( final Photo photo, final User userWhoIsCallingMenu, final List<EntryMenuOperationType> entryMenuOperationTypes );

	EntryMenu getUserMenu( final User user, final User userWhoIsCallingMenu );

	EntryMenu getUserMenu( final User user, final User userWhoIsCallingMenu, final List<EntryMenuOperationType> entryMenuOperationTypes );

	List<EntryMenuOperationType> getCommentComplaintOnlyMenuItems();

	List<EntryMenuOperationType> getCommentFullMenuItems();

	List<EntryMenuOperationType> getPhotoFullMenuItems();
}
