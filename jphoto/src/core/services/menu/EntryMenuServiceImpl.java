package core.services.menu;

import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.general.menus.*;
import core.services.security.Services;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class EntryMenuServiceImpl implements EntryMenuService {

	@Autowired
	private Services services;

	@Override
	public EntryMenu getUserMenu( final User user, final User accessor ) {
		return getUserMenu( user, accessor, getUserFullMenuItems() );
	}

	@Override
	public EntryMenu getUserMenu( final User user, final User accessor, final List<EntryMenuOperationType> entryMenuOperationTypes ) {
		return getGenericMenu( user, accessor, entryMenuOperationTypes, EntryMenuType.USER );
	}

	@Override
	public EntryMenu getPhotoMenu( final Photo photo, final User accessor ) {
		return getPhotoMenu( photo, accessor, getPhotoFullMenuItems() );
	}

	@Override
	public EntryMenu getPhotoMenu( final Photo photo, final User accessor, final List<EntryMenuOperationType> entryMenuOperationTypes ) {
		return getGenericMenu( photo, accessor, entryMenuOperationTypes, EntryMenuType.PHOTO );
	}

	@Override
	public EntryMenu getCommentMenu( final PhotoComment photoComment, final User accessor, final List<EntryMenuOperationType> entryMenuOperationTypes ) {
		return getGenericMenu( photoComment, accessor, entryMenuOperationTypes, EntryMenuType.COMMENT );
	}

	@Override
	public List<EntryMenuOperationType> getPhotoFullMenuItems() {
		final List<EntryMenuOperationType> menuItems = newArrayList();

		menuItems.add( EntryMenuOperationType.PHOTO_INFO );

		menuItems.add( EntryMenuOperationType.SEPARATOR );

		menuItems.add( EntryMenuOperationType.MENU_ITEM_EDIT );
		menuItems.add( EntryMenuOperationType.MENU_ITEM_DELETE );

		menuItems.add( EntryMenuOperationType.SEPARATOR );

		menuItems.add( EntryMenuOperationType.GO_TO_USER_PHOTOS );
		menuItems.add( EntryMenuOperationType.GO_TO_USER_PHOTOS_BY_GENRE );

		menuItems.add( EntryMenuOperationType.SEPARATOR );

		menuItems.add( EntryMenuOperationType.GO_TO_USER_PHOTOS_BY_TEAM_MEMBER );

		menuItems.add( EntryMenuOperationType.SEPARATOR );

		menuItems.add( EntryMenuOperationType.GO_TO_USER_PHOTOS_BY_ALBUM );

		menuItems.add( EntryMenuOperationType.SEPARATOR );

		menuItems.add( EntryMenuOperationType.SEND_PRIVATE_MESSAGE );

		return menuItems;
	}

	@Override
	public List<EntryMenuOperationType> getCommentFullMenuItems() {
		final List<EntryMenuOperationType> menuItems = newArrayList();

		menuItems.add( EntryMenuOperationType.COMMENT_REPLY );

		menuItems.add( EntryMenuOperationType.SEPARATOR );

		menuItems.add( EntryMenuOperationType.MENU_ITEM_EDIT );
		menuItems.add( EntryMenuOperationType.MENU_ITEM_DELETE );

		menuItems.add( EntryMenuOperationType.SEPARATOR );

		menuItems.add( EntryMenuOperationType.BLACK_LIST_ADD );
		menuItems.add( EntryMenuOperationType.BLACK_LIST_REMOVE );

		menuItems.add( EntryMenuOperationType.SEPARATOR );

		menuItems.addAll( getCommentComplaintOnlyMenuItems() );

		return menuItems;
	}

	@Override
	public List<EntryMenuOperationType> getCommentComplaintOnlyMenuItems() {
		final List<EntryMenuOperationType> menuItems = newArrayList();

		menuItems.add( EntryMenuOperationType.COMMENT_COMPLAINT_SPAM );
		menuItems.add( EntryMenuOperationType.COMMENT_COMPLAINT_SWORD_WORDS );
		menuItems.add( EntryMenuOperationType.COMMENT_COMPLAINT_CUSTOM );

		menuItems.add( EntryMenuOperationType.SEPARATOR );

		menuItems.add( EntryMenuOperationType.GO_TO_USER_PHOTOS );
		menuItems.add( EntryMenuOperationType.GO_TO_USER_PHOTOS_BY_GENRE );

		menuItems.add( EntryMenuOperationType.SEPARATOR );

		menuItems.add( EntryMenuOperationType.SEND_PRIVATE_MESSAGE );

		menuItems.add( EntryMenuOperationType.SEPARATOR );

		menuItems.add( EntryMenuOperationType.COMMENT_ADMIN_SUB_MENU );

		return menuItems;
	}

	private List<EntryMenuOperationType> getUserFullMenuItems() {
		final List<EntryMenuOperationType> menuItems = newArrayList();

		menuItems.add( EntryMenuOperationType.GO_TO_USER_PHOTOS );
		menuItems.add( EntryMenuOperationType.SEPARATOR );
		menuItems.add( EntryMenuOperationType.SEND_PRIVATE_MESSAGE );

		return menuItems;
	}

	private EntryMenu getGenericMenu( final PopupMenuAssignable menuEntry, final User accessor, final List<EntryMenuOperationType> entryMenuOperationTypes, final EntryMenuType menuType ) {

		final List<AbstractEntryMenuItem<? extends PopupMenuAssignable>> menuItems = newArrayList();

		for ( final EntryMenuOperationType entryMenuOperationType : entryMenuOperationTypes ) {
			menuItems.addAll( MenuCreationFactory.getAllMenuEntries( menuEntry, accessor, entryMenuOperationType, menuType, services ) );
		}

		CollectionUtils.filter( menuItems, new Predicate<AbstractEntryMenuItem<? extends PopupMenuAssignable>>() {
			@Override
			public boolean evaluate( final AbstractEntryMenuItem<? extends PopupMenuAssignable> abstractEntryMenuItem ) {
				return abstractEntryMenuItem.isAccessibleFor( menuEntry, accessor );
			}
		} );

		/*CollectionUtils.filter( menuItems, new Predicate<AbstractEntryMenuItem<T>>() {
			@Override
			public boolean evaluate( final AbstractEntryMenuItem<T> commentMenuItem ) {
				return commentMenuItem.isAccessibleFor( menuEntry, accessor );
			}
		} );*/

		removeSpareSeparators( menuItems );

		return new EntryMenu( menuEntry, menuType, menuItems );
	}

	private void removeSpareSeparators( final List<? extends AbstractEntryMenuItem> menuItems ) {

		if ( menuItems == null || menuItems.size() == 0) {
			return;
		}

		final Iterator<? extends AbstractEntryMenuItem> iterator = menuItems.iterator();
		EntryMenuOperationType entryMenuType = null;
		while ( iterator.hasNext() ) {
			final AbstractEntryMenuItem menuItem = iterator.next();
			if ( entryMenuType != null && entryMenuType == EntryMenuOperationType.SEPARATOR && menuItem.getEntryMenuType() == EntryMenuOperationType.SEPARATOR ) {
				iterator.remove();
				continue;
			}
			entryMenuType = menuItem.getEntryMenuType();
		}

		if ( menuItems.get( 0 ).getEntryMenuType() == EntryMenuOperationType.SEPARATOR ) {
			menuItems.remove( 0 );
		}

		if ( menuItems.size() == 0) {
			return;
		}

		final int lastIndex = menuItems.size() - 1;
		if ( menuItems.get( lastIndex ).getEntryMenuType() == EntryMenuOperationType.SEPARATOR ) {
			menuItems.remove( lastIndex );
		}
	}
}
