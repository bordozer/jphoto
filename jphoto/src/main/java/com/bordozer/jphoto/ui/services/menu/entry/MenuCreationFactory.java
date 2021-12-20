package com.bordozer.jphoto.ui.services.menu.entry;

import com.bordozer.jphoto.core.general.genre.Genre;
import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.photo.PhotoComment;
import com.bordozer.jphoto.core.general.photoTeam.PhotoTeamMember;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.general.user.userAlbums.UserPhotoAlbum;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItem;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenuData;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenuType;
import com.bordozer.jphoto.ui.services.menu.entry.items.MenuItemSeparator;
import com.bordozer.jphoto.ui.services.menu.entry.items.PopupMenuAssignable;
import com.bordozer.jphoto.ui.services.menu.entry.items.comment.admin.CommentAdminSubMenuItem;
import com.bordozer.jphoto.ui.services.menu.entry.items.comment.admin.CommentAdminSubMenuItemLockCommentAuthor;
import com.bordozer.jphoto.ui.services.menu.entry.items.comment.admin.CommentMenuItemDeleteAdmin;
import com.bordozer.jphoto.ui.services.menu.entry.items.comment.admin.CommentMenuItemEditAdmin;
import com.bordozer.jphoto.ui.services.menu.entry.items.comment.bookmark.CommentMenuItemBlackListAdd;
import com.bordozer.jphoto.ui.services.menu.entry.items.comment.bookmark.CommentMenuItemBlackListRemove;
import com.bordozer.jphoto.ui.services.menu.entry.items.comment.complain.CommentMenuItemComplaintCustom;
import com.bordozer.jphoto.ui.services.menu.entry.items.comment.complain.CommentMenuItemComplaintSpam;
import com.bordozer.jphoto.ui.services.menu.entry.items.comment.complain.CommentMenuItemComplaintSwordWords;
import com.bordozer.jphoto.ui.services.menu.entry.items.comment.goTo.CommentMenuItemGoToAuthorPhotoByGenre;
import com.bordozer.jphoto.ui.services.menu.entry.items.comment.goTo.CommentMenuItemGoToCommentAuthorPhotos;
import com.bordozer.jphoto.ui.services.menu.entry.items.comment.operations.CommentMenuItemDelete;
import com.bordozer.jphoto.ui.services.menu.entry.items.comment.operations.CommentMenuItemEdit;
import com.bordozer.jphoto.ui.services.menu.entry.items.comment.operations.CommentMenuItemReply;
import com.bordozer.jphoto.ui.services.menu.entry.items.comment.user.CommentMenuItemSendPrivateMessage;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.admin.PhotoAdminMovePhotoToGenreSubMenu;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.admin.PhotoAdminMovePhotoToGenreSubMenuItem;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.admin.PhotoAdminSubMenuItem;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.admin.PhotoAdminSubMenuItemLockPhotoAuthor;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.admin.PhotoAdminSubMenuItemPhotoRestriction;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.admin.PhotoMenuItemAdminGeneratePreview;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.admin.PhotoMenuItemAdminNudeContentRemove;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.admin.PhotoMenuItemAdminNudeContentSet;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.admin.PhotoMenuItemDeleteAdmin;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.admin.PhotoMenuItemEditAdmin;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.complain.PhotoComplaintCopyrightMenuItem;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.goTo.PhotoMenuItemGoToAuthorPhotoByAlbum;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.goTo.PhotoMenuItemGoToAuthorPhotoByGenre;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.goTo.PhotoMenuItemGoToAuthorPhotoByTeamMember;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.goTo.PhotoMenuItemGoToAuthorPhotos;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.operation.PhotoMenuItemDelete;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.operation.PhotoMenuItemEdit;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.photo.PhotoMenuItemInfo;
import com.bordozer.jphoto.ui.services.menu.entry.items.photo.user.PhotoMenuItemSendPrivateMessage;
import com.bordozer.jphoto.ui.services.menu.entry.items.user.admin.UserAdminSubMenuItem;
import com.bordozer.jphoto.ui.services.menu.entry.items.user.admin.UserAdminSubMenuItemLockUser;
import com.bordozer.jphoto.ui.services.menu.entry.items.user.goTo.UserMenuItemGoToPhotos;
import com.bordozer.jphoto.ui.services.menu.entry.items.user.user.UserMenuItemSendPrivateMessage;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class MenuCreationFactory {

    static List<AbstractEntryMenuItem<? extends PopupMenuAssignable>> getAllMenuEntries(final PopupMenuAssignable menuEntry, final User accessor, final EntryMenuData entryMenuData, final EntryMenuType menuType, final Services services) {

        switch (menuType) {
            case USER:
                return USER_MENU_CREATION_STRATEGY.getMenuEntries((User) menuEntry, accessor, entryMenuData, services);
            case PHOTO:
                return PHOTO_MENU_CREATION_STRATEGY.getMenuEntries((Photo) menuEntry, accessor, entryMenuData, services);
            case COMMENT:
                return COMMENT_MENU_CREATION_STRATEGY.getMenuEntries((PhotoComment) menuEntry, accessor, entryMenuData, services);
        }

        throw new IllegalArgumentException(String.format("Illegal popup menu entry type: %s", menuEntry));
    }

    static final AbstractMenuCreationStrategy<User> USER_MENU_CREATION_STRATEGY = new AbstractMenuCreationStrategy<User>() {
        @Override
        List<AbstractEntryMenuItem<? extends PopupMenuAssignable>> getMenuEntries(final User user, final User accessor, final EntryMenuData entryMenuData, final Services services) {

            final List<AbstractEntryMenuItem<? extends PopupMenuAssignable>> menuItems = newArrayList();

            switch (entryMenuData.getEntryMenuItemType()) {
                case SEPARATOR:
                    menuItems.add(new MenuItemSeparator<>(user, accessor, services));
                    break;
                case GO_TO_USER_PHOTOS:
                    menuItems.add(new UserMenuItemGoToPhotos(user, accessor, services));
                    break;
                case SEND_PRIVATE_MESSAGE:
                    menuItems.add(new UserMenuItemSendPrivateMessage(user, accessor, services));
                    break;
                case ADMIN_SUB_MENU:
                    menuItems.add(new UserAdminSubMenuItem(user, accessor, services));
                    break;
                case ADMIN_SUB_MENU_LOCK_USER:
                    menuItems.add(new UserAdminSubMenuItemLockUser(user, accessor, services));
                    break;
                default:
                    throw new IllegalArgumentException(String.format("Illegal user EntryMenuOperationType: %s", entryMenuData));
            }

            return menuItems;
        }
    };

    static final AbstractMenuCreationStrategy<Photo> PHOTO_MENU_CREATION_STRATEGY = new AbstractMenuCreationStrategy<Photo>() {

        @Override
        List<AbstractEntryMenuItem<? extends PopupMenuAssignable>> getMenuEntries(final Photo photo, final User accessor, final EntryMenuData entryMenuData, final Services services) {

            final List<AbstractEntryMenuItem<? extends PopupMenuAssignable>> menuItems = newArrayList();

            switch (entryMenuData.getEntryMenuItemType()) {
                case SEPARATOR:
                    menuItems.add(new MenuItemSeparator<>(photo, accessor, services));
                    break;
                case MENU_ITEM_EDIT:
                    menuItems.add(new PhotoMenuItemEdit(photo, accessor, services));
                    break;
                case MENU_ITEM_DELETE:
                    menuItems.add(new PhotoMenuItemDelete(photo, accessor, services));
                    break;
                case ADMIN_MOVE_PHOTO_TO_GENRE_SUB_MENU:
                    menuItems.add(new PhotoAdminMovePhotoToGenreSubMenu(photo, accessor, services));
                    break;
                case ADMIN_MOVE_PHOTO_TO_GENRE_SUB_MENU_ITEM:
                    final Genre genre = (Genre) entryMenuData.getCustomObject();
                    menuItems.add(new PhotoAdminMovePhotoToGenreSubMenuItem(photo, accessor, genre, services));
                    break;
                case PHOTO_COMPLAINT_COPYRIGHT:
                    menuItems.add(new PhotoComplaintCopyrightMenuItem(photo, accessor, services));
                    break;
                case GO_TO_USER_PHOTOS:
                    menuItems.add(new PhotoMenuItemGoToAuthorPhotos(photo, accessor, services));
                    break;
                case GO_TO_USER_PHOTOS_BY_GENRE:
                    menuItems.add(new PhotoMenuItemGoToAuthorPhotoByGenre(photo, accessor, services));
                    break;
                case SEND_PRIVATE_MESSAGE:
                    menuItems.add(new PhotoMenuItemSendPrivateMessage(photo, accessor, services));
                    break;
                case PHOTO_INFO:
                    menuItems.add(new PhotoMenuItemInfo(photo, accessor, services));
                    break;
                case ADMIN_SUB_MENU:
                    menuItems.add(new PhotoAdminSubMenuItem(photo, accessor, services));
                    break;
                case ADMIN_SUB_MENU_LOCK_USER:
                    menuItems.add(new PhotoAdminSubMenuItemLockPhotoAuthor(photo, accessor, services));
                    break;
                case ADMIN_SUB_MENU_LOCK_PHOTO:
                    menuItems.add(new PhotoAdminSubMenuItemPhotoRestriction(photo, accessor, services));
                    break;
                case ADMIN_MENU_ITEM_NUDE_CONTENT_SET:
                    menuItems.add(new PhotoMenuItemAdminNudeContentSet(photo, accessor, services));
                    break;
                case ADMIN_MENU_ITEM_NUDE_CONTENT_REMOVE:
                    menuItems.add(new PhotoMenuItemAdminNudeContentRemove(photo, accessor, services));
                    break;
                case ADMIN_MENU_ITEM_GENERATE_PREVIEW:
                    menuItems.add(new PhotoMenuItemAdminGeneratePreview(photo, accessor, services));
                    break;
                case ADMIN_MENU_ITEM_EDIT:
                    menuItems.add(new PhotoMenuItemEditAdmin(photo, accessor, services));
                    break;
                case ADMIN_MENU_ITEM_DELETE:
                    menuItems.add(new PhotoMenuItemDeleteAdmin(photo, accessor, services));
                    break;
                case GO_TO_USER_PHOTOS_BY_TEAM_MEMBER:
                    final List<PhotoTeamMember> photoTeamMembers = services.getUserTeamService().getPhotoTeam(photo.getId()).getPhotoTeamMembers();
                    for (final PhotoTeamMember photoTeamMember : photoTeamMembers) {
                        menuItems.add(new PhotoMenuItemGoToAuthorPhotoByTeamMember(photo, accessor, services, photoTeamMember));
                    }
                    break;
                case GO_TO_USER_PHOTOS_BY_ALBUM:
                    final List<UserPhotoAlbum> userPhotoAlbums = services.getUserPhotoAlbumService().loadPhotoAlbums(photo.getId());
                    for (final UserPhotoAlbum userPhotoAlbum : userPhotoAlbums) {
                        menuItems.add(new PhotoMenuItemGoToAuthorPhotoByAlbum(photo, accessor, services, userPhotoAlbum));
                    }
                    break;
                default:
                    throw new IllegalArgumentException(String.format("Illegal photo EntryMenuOperationType: %s", entryMenuData));
            }

            return menuItems;
        }
    };

    static final AbstractMenuCreationStrategy<PhotoComment> COMMENT_MENU_CREATION_STRATEGY = new AbstractMenuCreationStrategy<PhotoComment>() {

        @Override
        List<AbstractEntryMenuItem<? extends PopupMenuAssignable>> getMenuEntries(final PhotoComment photoComment, final User accessor, final EntryMenuData entryMenuData, final Services services) {

            final List<AbstractEntryMenuItem<? extends PopupMenuAssignable>> menuItems = newArrayList();

            switch (entryMenuData.getEntryMenuItemType()) {
                case SEPARATOR:
                    menuItems.add(new MenuItemSeparator<>(photoComment, accessor, services));
                    break;
                case MENU_ITEM_EDIT:
                    menuItems.add(new CommentMenuItemEdit(photoComment, accessor, services));
                    break;
                case COMMENT_REPLY:
                    menuItems.add(new CommentMenuItemReply(photoComment, accessor, services, services.getDateUtilsService().getCurrentTime()));
                    break;
                case MENU_ITEM_DELETE:
                    menuItems.add(new CommentMenuItemDelete(photoComment, accessor, services));
                    break;
                case COMMENT_COMPLAINT_CUSTOM:
                    menuItems.add(new CommentMenuItemComplaintCustom(photoComment, accessor, services));
                    break;
                case COMMENT_COMPLAINT_SPAM:
                    menuItems.add(new CommentMenuItemComplaintSpam(photoComment, accessor, services));
                    break;
                case COMMENT_COMPLAINT_SWORD_WORDS:
                    menuItems.add(new CommentMenuItemComplaintSwordWords(photoComment, accessor, services));
                    break;
                case BLACK_LIST_ADD:
                    menuItems.add(new CommentMenuItemBlackListAdd(photoComment, accessor, services));
                    break;
                case BLACK_LIST_REMOVE:
                    menuItems.add(new CommentMenuItemBlackListRemove(photoComment, accessor, services));
                    break;
                case GO_TO_USER_PHOTOS:
                    menuItems.add(new CommentMenuItemGoToCommentAuthorPhotos(photoComment, accessor, services));
                    break;
                case GO_TO_USER_PHOTOS_BY_GENRE:
                    menuItems.add(new CommentMenuItemGoToAuthorPhotoByGenre(photoComment, accessor, services));
                    break;
                case SEND_PRIVATE_MESSAGE:
                    menuItems.add(new CommentMenuItemSendPrivateMessage(photoComment, accessor, services));
                    break;
                case ADMIN_SUB_MENU:
                    menuItems.add(new CommentAdminSubMenuItem(photoComment, accessor, services));
                    break;
                case ADMIN_MENU_ITEM_EDIT:
                    menuItems.add(new CommentMenuItemEditAdmin(photoComment, accessor, services));
                    break;
                case ADMIN_MENU_ITEM_DELETE:
                    menuItems.add(new CommentMenuItemDeleteAdmin(photoComment, accessor, services));
                    break;
                case ADMIN_SUB_MENU_LOCK_USER:
                    menuItems.add(new CommentAdminSubMenuItemLockCommentAuthor(photoComment, accessor, services));
                    break;
                default:
                    throw new IllegalArgumentException(String.format("Illegal comment EntryMenuOperationType: %s", entryMenuData));
            }

            return menuItems;
        }
    };
}
