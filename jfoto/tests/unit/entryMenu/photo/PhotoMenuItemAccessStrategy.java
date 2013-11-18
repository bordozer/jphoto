package entryMenu.photo;

import core.general.menus.photo.AbstractPhotoMenuItem;
import entryMenu.AbstractEntryMenuItemAccessStrategy;
import entryMenu.Initialable;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

abstract class PhotoMenuItemAccessStrategy extends AbstractEntryMenuItemAccessStrategy<AbstractPhotoMenuItem> {

	public static PhotoMenuItemAccessStrategy getPhotoMenuItemInaccessibleStrategy() {

		return new PhotoMenuItemAccessStrategy() {
			@Override
			public void assertMenuItemAccess( final AbstractPhotoMenuItem menuItem, final Initialable initialConditions ) {
				final PhotoInitialConditions conditions = ( PhotoInitialConditions ) initialConditions;
				assertFalse( MENU_ITEM_SHOULD_NOT_BE_ACCESSIBLE_BUT_IT_IS, menuItem.isAccessibleForPhoto( conditions.getPhoto(), conditions.getUserWhoIsCallingMenu() ) );
			}
		};
	}

	public static PhotoMenuItemAccessStrategy getPhotoMenuItemAccessibleStrategy() {

		return new PhotoMenuItemAccessStrategy() {
			@Override
			public void assertMenuItemAccess( final AbstractPhotoMenuItem menuItem, final Initialable initialConditions ) {
				final PhotoInitialConditions conditions = ( PhotoInitialConditions ) initialConditions;
				assertTrue( MENU_ITEM_SHOULD_BE_ACCESSIBLE_BUT_IT_IS_NOT, menuItem.isAccessibleForPhoto( conditions.getPhoto(), conditions.getUserWhoIsCallingMenu() ) );
			}
		};
	}
}
