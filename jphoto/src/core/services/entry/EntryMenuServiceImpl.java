package core.services.entry;

import core.context.ApplicationContextHelper;
import core.general.menus.comment.items.CommentAdminSubMenuItem;
import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.general.menus.*;
import core.general.menus.comment.*;
import core.general.menus.comment.items.*;
import core.general.menus.photo.AbstractPhotoMenuItem;
import core.general.menus.photo.items.*;
import core.general.menus.user.AbstractUserMenuItem;
import core.general.menus.user.items.UserMenuItemGoToPhotos;
import core.general.menus.user.items.UserMenuItemSendPrivateMessage;
import core.general.menus.user.items.UserMenuItemSeparator;
import core.general.photoTeam.PhotoTeamMember;
import core.general.user.userAlbums.UserPhotoAlbum;
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
	private CommentMenuItemSeparator commentMenuItemSeparator;

	@Autowired
	private PhotoMenuItemSeparator photoMenuItemSeparator;

	@Autowired
	private UserTeamService userTeamService;

	@Autowired
	private UserPhotoAlbumService userPhotoAlbumService;

	@Autowired
	private UserMenuItemSeparator userMenuItemSeparator;

	@Override
	public EntryMenu getCommentMenu( final PhotoComment photoComment, final User userWhoIsCallingMenu, final List<EntryMenuOperationType> entryMenuOperationTypes ) {
		final int photoCommentId = photoComment.getId();

		final List<AbstractCommentMenuItem> menuItems = newArrayList();

		for ( final EntryMenuOperationType entryMenuOperationType : entryMenuOperationTypes ) {
			menuItems.add( createCommentMenuItemInstance( entryMenuOperationType ) );
		}

		CollectionUtils.filter( menuItems, new Predicate<AbstractCommentMenuItem>() {
			@Override
			public boolean evaluate( final AbstractCommentMenuItem commentMenuItem ) {
				return commentMenuItem.isAccessibleForComment( photoComment, userWhoIsCallingMenu );
			}
		} );

		removeSpareSeparators( menuItems );

		for ( final AbstractEntryMenuItem menuItem : menuItems ) {
			menuItem.createMenuItemCommand( photoCommentId, userWhoIsCallingMenu );
		}

		return new EntryMenu( photoCommentId, EntryMenuType.COMMENT, menuItems );
	}

	@Override
	public EntryMenu getPhotoMenu( final Photo photo, final User userWhoIsCallingMenu ) {
		return getPhotoMenu( photo, userWhoIsCallingMenu, getPhotoFullMenuItems() );
	}

	@Override
	public EntryMenu getPhotoMenu( final Photo photo, final User userWhoIsCallingMenu, final List<EntryMenuOperationType> entryMenuOperationTypes ) {
		final int photoId = photo.getId();

		final List<AbstractPhotoMenuItem> menuItems = newArrayList();

		for ( final EntryMenuOperationType entryMenuOperationType : entryMenuOperationTypes ) {
			switch ( entryMenuOperationType ) {
				case GO_TO_USER_PHOTOS_BY_TEAM_MEMBER:
					final List<PhotoTeamMember> photoTeamMembers = userTeamService.getPhotoTeam( photoId ).getPhotoTeamMembers();
					for ( final PhotoTeamMember photoTeamMember : photoTeamMembers ) {
						final PhotoMenuItemGoToAuthorPhotoByTeamMember teamMemberMenuItem = ApplicationContextHelper.getBean( PhotoMenuItemGoToAuthorPhotoByTeamMember.BEAN_NAME );
						teamMemberMenuItem.setPhotoTeamMember( photoTeamMember );

						menuItems.add( teamMemberMenuItem );
					}
					break;
				case GO_TO_USER_PHOTOS_BY_ALBUM:
					final List<UserPhotoAlbum> userPhotoAlbums = userPhotoAlbumService.loadPhotoAlbums( photoId );
					for ( final UserPhotoAlbum userPhotoAlbum : userPhotoAlbums ) {
						final PhotoMenuItemGoToAuthorPhotoByAlbum albumMenuItem = ApplicationContextHelper.getBean( PhotoMenuItemGoToAuthorPhotoByAlbum.BEAN_NAME );
						albumMenuItem.setUserPhotoAlbum( userPhotoAlbum );

						menuItems.add( albumMenuItem );
					}
					break;
				default:
					menuItems.add( createPhotoMenuItemInstance( entryMenuOperationType ) );
			}
		}

		CollectionUtils.filter( menuItems, new Predicate<AbstractPhotoMenuItem>() {
			@Override
			public boolean evaluate( final AbstractPhotoMenuItem commentMenuItem ) {
				return commentMenuItem.isAccessibleForPhoto( photo, userWhoIsCallingMenu );
			}
		} );

		removeSpareSeparators( menuItems );

		for ( final AbstractEntryMenuItem menuItem : menuItems ) {
			menuItem.createMenuItemCommand( photoId, userWhoIsCallingMenu );
		}

		return new EntryMenu( photoId, EntryMenuType.PHOTO, menuItems );
	}

	@Override
	public EntryMenu getUserMenu( final User user, final User userWhoIsCallingMenu ) {
		return getUserMenu( user, userWhoIsCallingMenu, getUserFullMenuItems() );
	}

	@Override
	public EntryMenu getUserMenu( final User user, final User userWhoIsCallingMenu, final List<EntryMenuOperationType> entryMenuOperationTypes ) {

		final List<AbstractUserMenuItem> menuItems = newArrayList();

		for ( final EntryMenuOperationType entryMenuOperationType : entryMenuOperationTypes ) {
			menuItems.add( createUserMenuItemInstance( entryMenuOperationType ) );
		}

		CollectionUtils.filter( menuItems, new Predicate<AbstractUserMenuItem>() {
			@Override
			public boolean evaluate( final AbstractUserMenuItem commentMenuItem ) {
				return commentMenuItem.isAccessibleForUser( user, userWhoIsCallingMenu );
			}
		} );

		removeSpareSeparators( menuItems );

		for ( final AbstractEntryMenuItem menuItem : menuItems ) {
			menuItem.createMenuItemCommand( user.getId(), userWhoIsCallingMenu );
		}

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

	private AbstractCommentMenuItem createCommentMenuItemInstance( final EntryMenuOperationType entryMenuOperationType ) {
		switch ( entryMenuOperationType ) {
			case SEPARATOR:
				return commentMenuItemSeparator;
			case MENU_ITEM_EDIT:
				return ApplicationContextHelper.getBean( CommentMenuItemEdit.BEAN_NAME );
			case COMMENT_REPLY:
				return ApplicationContextHelper.getBean( CommentMenuItemReply.BEAN_NAME );
			case MENU_ITEM_DELETE:
				return ApplicationContextHelper.getBean( CommentMenuItemDelete.BEAN_NAME );
			case COMMENT_COMPLAINT_CUSTOM:
				return ApplicationContextHelper.getBean( CommentMenuItemComplaintCustom.BEAN_NAME );
			case COMMENT_COMPLAINT_SPAM:
				return ApplicationContextHelper.getBean( CommentMenuItemComplaintSpam.BEAN_NAME );
			case COMMENT_COMPLAINT_SWORD_WORDS:
				return ApplicationContextHelper.getBean( CommentMenuItemComplaintSwordWords.BEAN_NAME );
			case BLACK_LIST_ADD:
				return ApplicationContextHelper.getBean( CommentMenuItemBlackListAdd.BEAN_NAME );
			case BLACK_LIST_REMOVE:
				return ApplicationContextHelper.getBean( CommentMenuItemBlackListRemove.BEAN_NAME );
			case GO_TO_USER_PHOTOS:
				return ApplicationContextHelper.getBean( CommentMenuItemGoToCommentAuthorPhotos.BEAN_NAME );
			case GO_TO_USER_PHOTOS_BY_GENRE:
				return ApplicationContextHelper.getBean( CommentMenuItemGoToAuthorPhotoByGenre.BEAN_NAME );
			case SEND_PRIVATE_MESSAGE:
				return ApplicationContextHelper.getBean( CommentMenuItemSendPrivateMessage.BEAN_NAME );
			case COMMENT_ADMIN_SUB_MENU:
				return ApplicationContextHelper.getBean( CommentAdminSubMenuItem.BEAN_NAME );
			case ADMIN_SUB_MENU_LOCK_USER:
				return ApplicationContextHelper.getBean( CommentAdminSubMenuItemLockUser.BEAN_NAME );
		}

		throw new IllegalArgumentException( String.format( "Illegal comment EntryMenuOperationType: %s", entryMenuOperationType ) );
	}

	private AbstractPhotoMenuItem createPhotoMenuItemInstance( final EntryMenuOperationType entryMenuOperationType ) {
		switch ( entryMenuOperationType ) {
			case SEPARATOR:
				return photoMenuItemSeparator;
			case MENU_ITEM_EDIT:
				return ApplicationContextHelper.getBean( PhotoMenuItemEdit.BEAN_NAME );
			case MENU_ITEM_DELETE:
				return ApplicationContextHelper.getBean( PhotoMenuItemDelete.BEAN_NAME );
			case GO_TO_USER_PHOTOS:
				return ApplicationContextHelper.getBean( PhotoMenuItemGoToAuthorPhotos.BEAN_NAME );
			case GO_TO_USER_PHOTOS_BY_GENRE:
				return ApplicationContextHelper.getBean( PhotoMenuItemGoToAuthorPhotoByGenre.BEAN_NAME );
			case GO_TO_USER_PHOTOS_BY_TEAM_MEMBER:
				return ApplicationContextHelper.getBean( PhotoMenuItemGoToAuthorPhotoByTeamMember.BEAN_NAME );
			case GO_TO_USER_PHOTOS_BY_ALBUM:
				return ApplicationContextHelper.getBean( PhotoMenuItemGoToAuthorPhotoByAlbum.BEAN_NAME );
			case SEND_PRIVATE_MESSAGE:
				return ApplicationContextHelper.getBean( PhotoMenuItemSendPrivateMessage.BEAN_NAME );
			case PHOTO_INFO:
				return ApplicationContextHelper.getBean( PhotoMenuItemInfo.BEAN_NAME );
		}

		throw new IllegalArgumentException( String.format( "Illegal photo EntryMenuOperationType: %s", entryMenuOperationType ) );
	}

	private AbstractUserMenuItem createUserMenuItemInstance( final EntryMenuOperationType entryMenuOperationType ) {
		switch ( entryMenuOperationType ) {
			case SEPARATOR:
				return userMenuItemSeparator;
			case GO_TO_USER_PHOTOS:
				return ApplicationContextHelper.getBean( UserMenuItemGoToPhotos.BEAN_NAME );
			case SEND_PRIVATE_MESSAGE:
				return ApplicationContextHelper.<UserMenuItemSendPrivateMessage>getBean( UserMenuItemSendPrivateMessage.BEAN_NAME );
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
