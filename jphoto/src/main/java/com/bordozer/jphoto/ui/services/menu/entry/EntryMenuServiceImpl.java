package com.bordozer.jphoto.ui.services.menu.entry;

import com.bordozer.jphoto.core.general.photo.Photo;
import com.bordozer.jphoto.core.general.photo.PhotoComment;
import com.bordozer.jphoto.core.general.user.User;
import com.bordozer.jphoto.core.services.system.Services;
import com.bordozer.jphoto.ui.services.menu.entry.items.AbstractEntryMenuItem;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenu;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenuData;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenuOperationType;
import com.bordozer.jphoto.ui.services.menu.entry.items.EntryMenuType;
import com.bordozer.jphoto.ui.services.menu.entry.items.PopupMenuAssignable;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Service("entryMenuService")
public class EntryMenuServiceImpl implements EntryMenuService {

    @Autowired
    private Services services;

    @Override
    public EntryMenu getUserMenu(final User user, final User accessor) {
        return getUserMenu(user, accessor, getUserFullMenuItems());
    }

    @Override
    public EntryMenu getUserMenu(final User user, final User accessor, final List<EntryMenuData> entryMenuOperationTypes) {
        return getGenericMenu(user, accessor, entryMenuOperationTypes, EntryMenuType.USER);
    }

    @Override
    public EntryMenu getPhotoMenu(final Photo photo, final User accessor) {
        return getPhotoMenu(photo, accessor, getPhotoFullMenuItems());
    }

    @Override
    public EntryMenu getPhotoMenu(final Photo photo, final User accessor, final List<EntryMenuData> entryMenuOperationTypes) {
        return getGenericMenu(photo, accessor, entryMenuOperationTypes, EntryMenuType.PHOTO);
    }

    @Override
    public EntryMenu getCommentMenu(final PhotoComment photoComment, final User accessor) {
        return getGenericMenu(photoComment, accessor, getCommentFullMenuItems(), EntryMenuType.COMMENT);
    }

    @Override
    public EntryMenu getCommentMenu(final PhotoComment photoComment, final User accessor, final List<EntryMenuData> entryMenuOperationTypes) {
        return getGenericMenu(photoComment, accessor, entryMenuOperationTypes, EntryMenuType.COMMENT);
    }

    @Override
    public List<EntryMenuData> getPhotoFullMenuItems() {
        final List<EntryMenuData> menuItems = newArrayList();

        menuItems.add(new EntryMenuData(EntryMenuOperationType.PHOTO_INFO));

        menuItems.add(new EntryMenuData(EntryMenuOperationType.SEPARATOR));

        menuItems.add(new EntryMenuData(EntryMenuOperationType.MENU_ITEM_EDIT));
        menuItems.add(new EntryMenuData(EntryMenuOperationType.MENU_ITEM_DELETE));

        menuItems.add(new EntryMenuData(EntryMenuOperationType.SEPARATOR));

        menuItems.add(new EntryMenuData(EntryMenuOperationType.PHOTO_COMPLAINT_COPYRIGHT));

        menuItems.add(new EntryMenuData(EntryMenuOperationType.SEPARATOR));

        menuItems.add(new EntryMenuData(EntryMenuOperationType.GO_TO_USER_PHOTOS));
        menuItems.add(new EntryMenuData(EntryMenuOperationType.GO_TO_USER_PHOTOS_BY_GENRE));

        menuItems.add(new EntryMenuData(EntryMenuOperationType.SEPARATOR));

        menuItems.add(new EntryMenuData(EntryMenuOperationType.GO_TO_USER_PHOTOS_BY_TEAM_MEMBER));

        menuItems.add(new EntryMenuData(EntryMenuOperationType.SEPARATOR));

        menuItems.add(new EntryMenuData(EntryMenuOperationType.GO_TO_USER_PHOTOS_BY_ALBUM));

        menuItems.add(new EntryMenuData(EntryMenuOperationType.SEPARATOR));

        menuItems.add(new EntryMenuData(EntryMenuOperationType.SEND_PRIVATE_MESSAGE));

        menuItems.add(new EntryMenuData(EntryMenuOperationType.SEPARATOR));

        menuItems.add(new EntryMenuData(EntryMenuOperationType.ADMIN_SUB_MENU));

        return menuItems;
    }

    @Override
    public List<EntryMenuData> getCommentFullMenuItems() {
        final List<EntryMenuData> menuItems = newArrayList();

        menuItems.add(new EntryMenuData(EntryMenuOperationType.COMMENT_REPLY));

        menuItems.add(new EntryMenuData(EntryMenuOperationType.SEPARATOR));

        menuItems.add(new EntryMenuData(EntryMenuOperationType.MENU_ITEM_EDIT));
        menuItems.add(new EntryMenuData(EntryMenuOperationType.MENU_ITEM_DELETE));

        //		menuItems.add( EntryMenuOperationType.SEPARATOR );

        //		menuItems.add( EntryMenuOperationType.BLACK_LIST_ADD );
        //		menuItems.add( EntryMenuOperationType.BLACK_LIST_REMOVE );

        menuItems.add(new EntryMenuData(EntryMenuOperationType.SEPARATOR));

        menuItems.addAll(getCommentComplaintOnlyMenuItems());

        return menuItems;
    }

    @Override
    public List<EntryMenuData> getCommentComplaintOnlyMenuItems() {
        final List<EntryMenuData> menuItems = newArrayList();

        menuItems.add(new EntryMenuData(EntryMenuOperationType.COMMENT_COMPLAINT_SPAM));
        menuItems.add(new EntryMenuData(EntryMenuOperationType.COMMENT_COMPLAINT_SWORD_WORDS));
        menuItems.add(new EntryMenuData(EntryMenuOperationType.COMMENT_COMPLAINT_CUSTOM));

        menuItems.add(new EntryMenuData(EntryMenuOperationType.SEPARATOR));

        menuItems.add(new EntryMenuData(EntryMenuOperationType.GO_TO_USER_PHOTOS));
        menuItems.add(new EntryMenuData(EntryMenuOperationType.GO_TO_USER_PHOTOS_BY_GENRE));

        menuItems.add(new EntryMenuData(EntryMenuOperationType.SEPARATOR));

        menuItems.add(new EntryMenuData(EntryMenuOperationType.SEND_PRIVATE_MESSAGE));

        menuItems.add(new EntryMenuData(EntryMenuOperationType.SEPARATOR));

        menuItems.add(new EntryMenuData(EntryMenuOperationType.ADMIN_SUB_MENU));

        return menuItems;
    }

    private List<EntryMenuData> getUserFullMenuItems() {
        final List<EntryMenuData> menuItems = newArrayList();

        menuItems.add(new EntryMenuData(EntryMenuOperationType.GO_TO_USER_PHOTOS));

        menuItems.add(new EntryMenuData(EntryMenuOperationType.SEPARATOR));

        menuItems.add(new EntryMenuData(EntryMenuOperationType.SEND_PRIVATE_MESSAGE));

        menuItems.add(new EntryMenuData(EntryMenuOperationType.SEPARATOR));

        menuItems.add(new EntryMenuData(EntryMenuOperationType.ADMIN_SUB_MENU));

        return menuItems;
    }

    private EntryMenu getGenericMenu(final PopupMenuAssignable menuEntry, final User accessor, final List<EntryMenuData> entryMenuDataList, final EntryMenuType menuType) {

        final List<AbstractEntryMenuItem<? extends PopupMenuAssignable>> menuItems = newArrayList();

        for (final EntryMenuData entryMenuData : entryMenuDataList) {
            menuItems.addAll(MenuCreationFactory.getAllMenuEntries(menuEntry, accessor, entryMenuData, menuType, services));
        }

        CollectionUtils.filter(menuItems, new Predicate<AbstractEntryMenuItem<? extends PopupMenuAssignable>>() {
            @Override
            public boolean evaluate(final AbstractEntryMenuItem<? extends PopupMenuAssignable> abstractEntryMenuItem) {
                return abstractEntryMenuItem.isAccessibleFor();
            }
        });

        removeSpareSeparators(menuItems);

        return new EntryMenu(menuEntry, menuType, menuItems, accessor.getLanguage(), services);
    }

    private void removeSpareSeparators(final List<? extends AbstractEntryMenuItem> menuItems) {

        if (menuItems == null || menuItems.size() == 0) {
            return;
        }

        final Iterator<? extends AbstractEntryMenuItem> iterator = menuItems.iterator();
        EntryMenuOperationType entryMenuType = null;
        while (iterator.hasNext()) {
            final AbstractEntryMenuItem menuItem = iterator.next();
            if (entryMenuType != null && entryMenuType == EntryMenuOperationType.SEPARATOR && menuItem.getEntryMenuType() == EntryMenuOperationType.SEPARATOR) {
                iterator.remove();
                continue;
            }
            entryMenuType = menuItem.getEntryMenuType();
        }

        if (menuItems.get(0).getEntryMenuType() == EntryMenuOperationType.SEPARATOR) {
            menuItems.remove(0);
        }

        if (menuItems.size() == 0) {
            return;
        }

        final int lastIndex = menuItems.size() - 1;
        if (menuItems.get(lastIndex).getEntryMenuType() == EntryMenuOperationType.SEPARATOR) {
            menuItems.remove(lastIndex);
        }
    }
}
