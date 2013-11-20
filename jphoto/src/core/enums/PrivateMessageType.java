package core.enums;

import utils.StringUtilities;
import utils.TranslatorUtils;

public enum PrivateMessageType {

	USER_PRIVATE_MESSAGE_IN( 1, "Received messages", "privateIn.png" )
	, USER_PRIVATE_MESSAGE_OUT( 4, "Sent messages", "privateOut.png" )
	, SYSTEM_INFORMATION( 3, "System messages", "notification.png" )
//	, ADMIN_MESSAGE( 2, "Messages from admin", "admin.png" )
	;

	private final int id;
	private final String name;
	private final String icon;

	private PrivateMessageType( final int id, final String name, final String icon ) {
		this.id = id;
		this.name = name;
		this.icon = icon;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getIcon() {
		return icon;
	}

	public String getNameTranslated() {
		return StringUtilities.toUpperCaseFirst( TranslatorUtils.translate( name ) );
	}

	public static PrivateMessageType getById( final int id ) {
		for ( final PrivateMessageType entryType : PrivateMessageType.values() ) {
			if ( entryType.getId() == id ) {
				return entryType;
			}
		}

		throw new IllegalArgumentException( String.format( "Illegal UserMessageType id: %s", id ) );
	}
}
