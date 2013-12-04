package core.general.user;

import core.interfaces.Identifiable;
import utils.StringUtilities;
import utils.TranslatorUtils;

public enum EmailNotificationType implements Identifiable {

	NEW_PHOTO_OF_FAVORITE_MEMBER( 1, "New photo of favorite members" )
	, NEW_PHOTO_OF_FRIEND( 8, "New photo of friend member " )
	, NEW_PHOTO_OF_TRACKING_MEMBER( 4, "New uploaded photo of member you signed on new photos" )
	, COMMENT_TO_USER_PHOTO( 2, "New comment to your photo" )
	, COMMENT_TO_TRACKING_PHOTO( 3, "New comment to photo you signed" )
	, PRIVATE_MESSAGE( 5, "New private message" )
	, ADMIN_MESSAGE( 6, "Messages from admin" )
	, SYSTEM_INFORMATION( 7, "Messages from system" )
	;

	private final int id;
	private final String name;

	private EmailNotificationType( final int id, final String name ) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getNameTranslated() {
		return StringUtilities.toUpperCaseFirst( TranslatorUtils.translate( name ) );
	}

	public static EmailNotificationType getById( final int id ) {
		for ( final EmailNotificationType entryType : EmailNotificationType.values() ) {
			if ( entryType.getId() == id ) {
				return entryType;
			}
		}

		return null;
	}
}
