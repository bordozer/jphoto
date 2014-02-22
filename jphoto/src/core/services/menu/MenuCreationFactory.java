package core.services.menu;

import core.general.menus.*;
import core.general.menus.comment.items.*;
import core.general.menus.photo.items.*;
import core.general.menus.user.items.UserMenuItemGoToPhotos;
import core.general.menus.user.items.UserMenuItemSendPrivateMessage;
import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.general.photoTeam.PhotoTeamMember;
import core.general.user.User;
import core.general.user.userAlbums.UserPhotoAlbum;
import core.services.security.Services;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class MenuCreationFactory<T extends PopupMenuAssignable> {

	List<AbstractEntryMenuItem<T>> getAllMenuEntries( final T menuEntry, final User accessor, final EntryMenuOperationType entryMenuOperationType, final EntryMenuType menuType, final Services services ) {

		switch ( menuType ) {
			case USER:
				return USER_MENU_CREATION_STRATEGY.getMenuEntries( ( User ) menuEntry, accessor, entryMenuOperationType, services );
			case PHOTO:
				return PHOTO_MENU_CREATION_STRATEGY.getMenuEntries( ( Photo ) menuEntry, accessor, entryMenuOperationType, services );
			case COMMENT:
				return COMMENT_MENU_CREATION_STRATEGY.getMenuEntries( ( PhotoComment ) menuEntry, accessor, entryMenuOperationType, services );
		}

		throw new IllegalArgumentException( String.format( "Illegal popup menu entry type: %s", menuEntry ) );
	}

	final AbstractMenuCreationStrategy<User> USER_MENU_CREATION_STRATEGY = new AbstractMenuCreationStrategy<User>() {
		@Override
		List<AbstractEntryMenuItem<User>> getMenuEntries( final User user, final User accessor, final EntryMenuOperationType entryMenuOperationType, final Services services ) {

			final List<AbstractEntryMenuItem<User>> menuItems = newArrayList();

			switch ( entryMenuOperationType ) {
				case SEPARATOR:
					menuItems.add( new MenuItemSeparator<>( user, accessor, services ) );
					break;
				case GO_TO_USER_PHOTOS:
					menuItems.add( new UserMenuItemGoToPhotos( user, accessor, services ) );
					break;
				case SEND_PRIVATE_MESSAGE:
					menuItems.add( new UserMenuItemSendPrivateMessage( user, accessor, services ) );
					break;
				default:
					throw new IllegalArgumentException( String.format( "Illegal user EntryMenuOperationType: %s", entryMenuOperationType ) );
			}

			return menuItems;
		}
	};

	final AbstractMenuCreationStrategy<Photo> PHOTO_MENU_CREATION_STRATEGY = new AbstractMenuCreationStrategy<Photo>() {

		@Override
		List<AbstractEntryMenuItem<Photo>> getMenuEntries( final Photo photo, final User accessor, final EntryMenuOperationType entryMenuOperationType, final Services services ) {

			final List<AbstractEntryMenuItem<Photo>> menuItems = newArrayList();

			switch ( entryMenuOperationType ) {
				case SEPARATOR:
					menuItems.add( new MenuItemSeparator<>( photo, accessor, services ) );
					break;
				case MENU_ITEM_EDIT:
					menuItems.add( new PhotoMenuItemEdit( photo, accessor, services ) );
					break;
				case MENU_ITEM_DELETE:
					menuItems.add( new PhotoMenuItemDelete( photo, accessor, services ) );
					break;
				case GO_TO_USER_PHOTOS:
					menuItems.add( new PhotoMenuItemGoToAuthorPhotos( photo, accessor, services ) );
					break;
				case GO_TO_USER_PHOTOS_BY_GENRE:
					menuItems.add( new PhotoMenuItemGoToAuthorPhotoByGenre( photo, accessor, services ) );
					break;
				case SEND_PRIVATE_MESSAGE:
					menuItems.add( new PhotoMenuItemSendPrivateMessage( photo, accessor, services ) );
					break;
				case PHOTO_INFO:
					menuItems.add( new PhotoMenuItemInfo( photo, accessor, services ) );
					break;
				case GO_TO_USER_PHOTOS_BY_TEAM_MEMBER:
					final List<PhotoTeamMember> photoTeamMembers = services.getUserTeamService().getPhotoTeam( photo.getId() ).getPhotoTeamMembers();
					for ( final PhotoTeamMember photoTeamMember : photoTeamMembers ) {
						menuItems.add( new PhotoMenuItemGoToAuthorPhotoByTeamMember( photo, accessor, services, photoTeamMember ) );
					}
					break;
				case GO_TO_USER_PHOTOS_BY_ALBUM:
					final List<UserPhotoAlbum> userPhotoAlbums = services.getUserPhotoAlbumService().loadPhotoAlbums( photo.getId() );
					for ( final UserPhotoAlbum userPhotoAlbum : userPhotoAlbums ) {
						menuItems.add( new PhotoMenuItemGoToAuthorPhotoByAlbum( photo, accessor, services, userPhotoAlbum ) );
					}
					break;
				default:
					throw new IllegalArgumentException( String.format( "Illegal photo EntryMenuOperationType: %s", entryMenuOperationType ) );
			}

			return menuItems;
		}
	};

	final AbstractMenuCreationStrategy<PhotoComment> COMMENT_MENU_CREATION_STRATEGY = new AbstractMenuCreationStrategy<PhotoComment>() {

		@Override
		List<AbstractEntryMenuItem<PhotoComment>> getMenuEntries( final PhotoComment photoComment, final User accessor, final EntryMenuOperationType entryMenuOperationType, final Services services ) {

			final List<AbstractEntryMenuItem<PhotoComment>> menuItems = newArrayList();

			switch ( entryMenuOperationType ) {
				case SEPARATOR:
					menuItems.add( new MenuItemSeparator<>( photoComment, accessor, services ) );
					break;
				case MENU_ITEM_EDIT:
					menuItems.add( new CommentMenuItemEdit( photoComment, accessor, services ) );
					break;
				case COMMENT_REPLY:
					menuItems.add( new CommentMenuItemReply( photoComment, accessor, services ) );
					break;
				case MENU_ITEM_DELETE:
					menuItems.add( new CommentMenuItemDelete( photoComment, accessor, services ) );
					break;
				case COMMENT_COMPLAINT_CUSTOM:
					menuItems.add( new CommentMenuItemComplaintCustom( photoComment, accessor, services ) );
					break;
				case COMMENT_COMPLAINT_SPAM:
					menuItems.add( new CommentMenuItemComplaintSpam( photoComment, accessor, services ) );
					break;
				case COMMENT_COMPLAINT_SWORD_WORDS:
					menuItems.add( new CommentMenuItemComplaintSwordWords( photoComment, accessor, services ) );
					break;
				case BLACK_LIST_ADD:
					menuItems.add( new CommentMenuItemBlackListAdd( photoComment, accessor, services ) );
					break;
				case BLACK_LIST_REMOVE:
					menuItems.add( new CommentMenuItemBlackListRemove( photoComment, accessor, services ) );
					break;
				case GO_TO_USER_PHOTOS:
					menuItems.add( new CommentMenuItemGoToCommentAuthorPhotos( photoComment, accessor, services ) );
					break;
				case GO_TO_USER_PHOTOS_BY_GENRE:
					menuItems.add( new CommentMenuItemGoToAuthorPhotoByGenre( photoComment, accessor, services ) );
					break;
				case SEND_PRIVATE_MESSAGE:
					menuItems.add( new CommentMenuItemSendPrivateMessage( photoComment, accessor, services ) );
					break;
				case COMMENT_ADMIN_SUB_MENU:
					menuItems.add( new CommentAdminSubMenuItem( photoComment, accessor, services ) );
					break;
				case ADMIN_SUB_MENU_LOCK_USER:
					menuItems.add( new CommentAdminSubMenuItemLockUser( photoComment, accessor, services ) );
					break;
				default:
					throw new IllegalArgumentException( String.format( "Illegal comment EntryMenuOperationType: %s", entryMenuOperationType ) );
			}

			return menuItems;
		}
	};
}
