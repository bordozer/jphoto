package ui.services.menu.entry;

import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.general.user.User;
import ui.services.menu.entry.items.EntryMenu;
import ui.services.menu.entry.items.EntryMenuData;

import java.util.List;

public interface EntryMenuService {

	EntryMenu getUserMenu( final User user, final User accessor );

	EntryMenu getUserMenu( final User user, final User accessor, final List<EntryMenuData> entryMenuDataList );

	EntryMenu getPhotoMenu( final Photo photo, final User accessor );

	EntryMenu getPhotoMenu( final Photo photo, final User accessor, final List<EntryMenuData> entryMenuDataList );

	EntryMenu getCommentMenu( final PhotoComment photoComment, final User accessor );

	EntryMenu getCommentMenu( final PhotoComment photoComment, final User accessor, final List<EntryMenuData> entryMenuDataList );

	List<EntryMenuData> getPhotoFullMenuItems();

	List<EntryMenuData> getCommentFullMenuItems();

	List<EntryMenuData> getCommentComplaintOnlyMenuItems();
}
