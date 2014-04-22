package core.general.menus;

import java.util.EnumSet;

public enum EntryMenuOperationType {

	SEPARATOR( 0, "Separator", "" )
	, MENU_ITEM_EDIT( 1, "Edit", "menu_edit.png" )
	, MENU_ITEM_DELETE( 2, "Delete", "menu_delete.png" )

	, GO_TO_USER_PHOTOS( 11, "Go to photos", "menu_go_to_photos.png" )
	, GO_TO_USER_PHOTOS_BY_GENRE( 12, "Go to photos", "menu_go_to_photos.png" )
	, GO_TO_USER_PHOTOS_BY_TEAM_MEMBER( 13, "Go to photos", "menu_go_to_photos.png" )
	, GO_TO_USER_PHOTOS_BY_ALBUM( 14, "Go to photos", "menu_go_to_photos.png" )

	, COMMENT_COMPLAINT_SPAM( 21, "Spam", "menu_comment_spam.png" )
	, COMMENT_COMPLAINT_SWORD_WORDS( 22, "Sword worlds", "menu_comment_sword_words.png" )
	, COMMENT_COMPLAINT_CUSTOM( 23, "Custom complaint", "menu_comment_custom_complaint.png" )

	, COMMENT_REPLY( 31, "reply", "menu_comment_reply.png" )

	, BLACK_LIST_ADD( 41, "Add to my black list", "menu_black_list_add.png" )
	, BLACK_LIST_REMOVE( 42, "remove user from my black list", "menu_black_list_remove.png" )

	, SEND_PRIVATE_MESSAGE( 51, "Send private message", "menu_send_private_message.png" )

	, PHOTO_INFO( 61, "Photo info", "menu_photo_info.png" )
	, PHOTO_COMPLAINT_COPYRIGHT( 62, "Complain copyright", "menu_photo_complaint_copyright.png" )

	, ADMIN_SUB_MENU( 71, "Admin submenu", "menu_sub_menu.png" )

	, ADMIN_SUB_MENU_LOCK_USER( 81, "Lock user", "lock_user.png" )
	, ADMIN_MENU_ITEM_EDIT( 82, "Edit", "menu_edit.png" )
	, ADMIN_MENU_ITEM_DELETE( 83, "Delete", "menu_delete.png" )
	, ADMIN_MENU_ITEM_NUDE_CONTENT_SET( 84, "Set nude content", "menu_photo_nude_content_set.png" )
	, ADMIN_MENU_ITEM_NUDE_CONTENT_REMOVE( 85, "Remove nude content", "menu_photo_nude_content_remove.png" )

	;

	private static EnumSet<EntryMenuOperationType> SUB_MENUS = EnumSet.of( ADMIN_SUB_MENU );

	private final int id;
	private final String icon;

	private EntryMenuOperationType( final int id, final String name, final String icon ) {
		this.id = id;
		this.icon = icon;
	}

	public String getIcon() {
		return icon;
	}

	public int getId() {
		return id;
	}

	public static EntryMenuOperationType getById( final int id ) {
		for ( final EntryMenuOperationType entryMenuOperationType : EntryMenuOperationType.values() ) {
			if ( entryMenuOperationType.getId() == id ) {
				return entryMenuOperationType;
			}
		}

		throw new IllegalArgumentException( String.format( "Illegal EntryMenuOperationType id: %d", id ) );
	}

	public boolean isSubMenu() {
		return SUB_MENUS.contains( this );
	}
}
