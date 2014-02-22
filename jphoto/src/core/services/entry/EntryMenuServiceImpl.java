package core.services.entry;

import core.general.menus.comment.items.CommentAdminSubMenuItem;
import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.general.menus.*;
import core.general.menus.comment.items.*;
import core.general.menus.photo.items.*;
import core.general.menus.user.items.UserMenuItemGoToPhotos;
import core.general.menus.user.items.UserMenuItemSendPrivateMessage;
import core.general.photoTeam.PhotoTeamMember;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.services.security.Services;
import core.services.user.UserPhotoAlbumService;
import core.services.user.UserTeamService;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class EntryMenuServiceImpl implements EntryMenuService {

	@Autowired
	private UserTeamService userTeamService;

	@Autowired
	private UserPhotoAlbumService userPhotoAlbumService;

	@Autowired
	private Services services;

	@Override
	public EntryMenu getCommentMenu( final PhotoComment photoComment, final User accessor, final List<EntryMenuOperationType> entryMenuOperationTypes ) {
		final int photoCommentId = photoComment.getId();

		final List<AbstractEntryMenuItem<PhotoComment>> menuItems = newArrayList();

		for ( final EntryMenuOperationType entryMenuOperationType : entryMenuOperationTypes ) {
			menuItems.add( createCommentMenuItemInstance( photoComment, accessor, entryMenuOperationType ) );
		}

		CollectionUtils.filter( menuItems, new Predicate<AbstractEntryMenuItem<PhotoComment>>() {
			@Override
			public boolean evaluate( final AbstractEntryMenuItem<PhotoComment> commentMenuItem ) {
				return commentMenuItem.isAccessibleFor( photoComment, accessor );
			}
		} );

		removeSpareSeparators( menuItems );

		return new EntryMenu( photoCommentId, EntryMenuType.COMMENT, menuItems );
	}

	@Override
	public EntryMenu getPhotoMenu( final Photo photo, final User accessor ) {
		return getPhotoMenu( photo, accessor, getPhotoFullMenuItems() );
	}

	@Override
	public EntryMenu getPhotoMenu( final Photo photo, final User accessor, final List<EntryMenuOperationType> entryMenuOperationTypes ) {
		final int photoId = photo.getId();

		final List<AbstractEntryMenuItem<Photo>> menuItems = newArrayList();

		for ( final EntryMenuOperationType entryMenuOperationType : entryMenuOperationTypes ) {
			switch ( entryMenuOperationType ) {
				case GO_TO_USER_PHOTOS_BY_TEAM_MEMBER:
					final List<PhotoTeamMember> photoTeamMembers = userTeamService.getPhotoTeam( photoId ).getPhotoTeamMembers();
					for ( final PhotoTeamMember photoTeamMember : photoTeamMembers ) {
						menuItems.add( new PhotoMenuItemGoToAuthorPhotoByTeamMember( photo, accessor, services, photoTeamMember ) );
					}
					break;
				case GO_TO_USER_PHOTOS_BY_ALBUM:
					final List<UserPhotoAlbum> userPhotoAlbums = userPhotoAlbumService.loadPhotoAlbums( photoId );
					for ( final UserPhotoAlbum userPhotoAlbum : userPhotoAlbums ) {
						menuItems.add( new PhotoMenuItemGoToAuthorPhotoByAlbum( photo, accessor, services, userPhotoAlbum ) );
					}
					break;
				default:
					menuItems.add( createPhotoMenuItemInstance( photo, accessor, entryMenuOperationType ) );
			}
		}

		CollectionUtils.filter( menuItems, new Predicate<AbstractEntryMenuItem<Photo>>() {
			@Override
			public boolean evaluate( final AbstractEntryMenuItem<Photo> commentMenuItem ) {
				return commentMenuItem.isAccessibleFor( photo, accessor );
			}
		} );

		removeSpareSeparators( menuItems );

		return new EntryMenu( photoId, EntryMenuType.PHOTO, menuItems );
	}

	@Override
	public EntryMenu getUserMenu( final User user, final User accessor ) {
		return getUserMenu( user, accessor, getUserFullMenuItems() );
	}

	@Override
	public EntryMenu getUserMenu( final User user, final User accessor, final List<EntryMenuOperationType> entryMenuOperationTypes ) {

		final List<AbstractEntryMenuItem<User>> menuItems = newArrayList();

		for ( final EntryMenuOperationType entryMenuOperationType : entryMenuOperationTypes ) {
			menuItems.add( createUserMenuItemInstance( user, accessor, entryMenuOperationType ) );
		}

		CollectionUtils.filter( menuItems, new Predicate<AbstractEntryMenuItem<User>>() {
			@Override
			public boolean evaluate( final AbstractEntryMenuItem<User> userMenuItem ) {
				return userMenuItem.isAccessibleFor( user, accessor );
			}
		} );

		removeSpareSeparators( menuItems );

		return new EntryMenu( user.getId(), EntryMenuType.USER, menuItems );
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

	private List<EntryMenuOperationType> getUserFullMenuItems() {
		final List<EntryMenuOperationType> menuItems = newArrayList();

		menuItems.add( EntryMenuOperationType.GO_TO_USER_PHOTOS );
		menuItems.add( EntryMenuOperationType.SEPARATOR );
		menuItems.add( EntryMenuOperationType.SEND_PRIVATE_MESSAGE );

		return menuItems;
	}

	private AbstractEntryMenuItem<PhotoComment> createCommentMenuItemInstance( final PhotoComment photoComment, final User accessor, final EntryMenuOperationType entryMenuOperationType ) {
		switch ( entryMenuOperationType ) {
			case SEPARATOR:
				return new MenuItemSeparator<>( photoComment, accessor, services );
			case MENU_ITEM_EDIT:
				return new CommentMenuItemEdit( photoComment, accessor, services );
			case COMMENT_REPLY:
				return new CommentMenuItemReply( photoComment, accessor, services );
			case MENU_ITEM_DELETE:
				return new CommentMenuItemDelete( photoComment, accessor, services );
			case COMMENT_COMPLAINT_CUSTOM:
				return new CommentMenuItemComplaintCustom( photoComment, accessor, services );
			case COMMENT_COMPLAINT_SPAM:
				return new CommentMenuItemComplaintSpam( photoComment, accessor, services );
			case COMMENT_COMPLAINT_SWORD_WORDS:
				return new CommentMenuItemComplaintSwordWords( photoComment, accessor, services );
			case BLACK_LIST_ADD:
				return new CommentMenuItemBlackListAdd( photoComment, accessor, services );
			case BLACK_LIST_REMOVE:
				return new CommentMenuItemBlackListRemove( photoComment, accessor, services );
			case GO_TO_USER_PHOTOS:
				return new CommentMenuItemGoToCommentAuthorPhotos( photoComment, accessor, services );
			case GO_TO_USER_PHOTOS_BY_GENRE:
				return new CommentMenuItemGoToAuthorPhotoByGenre( photoComment, accessor, services );
			case SEND_PRIVATE_MESSAGE:
				return new CommentMenuItemSendPrivateMessage( photoComment, accessor, services );
			case COMMENT_ADMIN_SUB_MENU:
				return new CommentAdminSubMenuItem( photoComment, accessor, services );
			case ADMIN_SUB_MENU_LOCK_USER:
				return new CommentAdminSubMenuItemLockUser( photoComment, accessor, services );
		}

		throw new IllegalArgumentException( String.format( "Illegal comment EntryMenuOperationType: %s", entryMenuOperationType ) );
	}

	private AbstractEntryMenuItem<Photo> createPhotoMenuItemInstance( final Photo photo, final User accessor, final EntryMenuOperationType entryMenuOperationType ) {
		switch ( entryMenuOperationType ) {
			case SEPARATOR:
				return new MenuItemSeparator<>( photo, accessor, services );
			case MENU_ITEM_EDIT:
				return new PhotoMenuItemEdit( photo, accessor, services );
			case MENU_ITEM_DELETE:
				return new PhotoMenuItemDelete( photo, accessor, services );
			case GO_TO_USER_PHOTOS:
				return new PhotoMenuItemGoToAuthorPhotos( photo, accessor, services );
			case GO_TO_USER_PHOTOS_BY_GENRE:
				return new PhotoMenuItemGoToAuthorPhotoByGenre( photo, accessor, services );
			case SEND_PRIVATE_MESSAGE:
				return new PhotoMenuItemSendPrivateMessage( photo, accessor, services );
			case PHOTO_INFO:
				return new PhotoMenuItemInfo( photo, accessor, services );
		}

		throw new IllegalArgumentException( String.format( "Illegal photo EntryMenuOperationType: %s", entryMenuOperationType ) );
	}

	private AbstractEntryMenuItem<User> createUserMenuItemInstance( final User user, final User accessor, final EntryMenuOperationType entryMenuOperationType ) {
		switch ( entryMenuOperationType ) {
			case SEPARATOR:
				return new MenuItemSeparator<>( user, accessor, services );
			case GO_TO_USER_PHOTOS:
				return new UserMenuItemGoToPhotos( user, accessor, services );
			case SEND_PRIVATE_MESSAGE:
				return new UserMenuItemSendPrivateMessage( user, accessor, services );
		}

		throw new IllegalArgumentException( String.format( "Illegal user EntryMenuOperationType: %s", entryMenuOperationType ) );
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
