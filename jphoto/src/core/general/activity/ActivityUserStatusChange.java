package core.general.activity;

import core.general.user.User;
import core.general.user.UserStatus;
import core.services.security.Services;
import core.services.translator.TranslatorService;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import utils.NumberUtils;

import java.util.Date;

public class ActivityUserStatusChange extends AbstractActivityStreamEntry {

	private static final String ACTIVITY_XML_TAG_OLD_STATUS_ID = "old_status";
	private static final String ACTIVITY_XML_TAG_NEW_STATUS_ID = "new_status";

	private UserStatus oldStatus;
	private UserStatus newStatus;

	public ActivityUserStatusChange( final User user, final Date activityTime, final String activityXML, final Services services ) throws DocumentException {
		super( user, activityTime, ActivityType.USER_STATUS, services );

		final Document document = DocumentHelper.parseText( activityXML );
		final Element rootElement = document.getRootElement();

		oldStatus = UserStatus.getById( NumberUtils.convertToInt( rootElement.element( ACTIVITY_XML_TAG_OLD_STATUS_ID ).getText() ) );
		newStatus = UserStatus.getById( NumberUtils.convertToInt( rootElement.element( ACTIVITY_XML_TAG_NEW_STATUS_ID ).getText() ) );

	}

	public ActivityUserStatusChange( final User user, final UserStatus oldStatus, final UserStatus newStatus, final Date activityTime, final Services services ) {
		super( user, activityTime, ActivityType.USER_STATUS, services );

		this.oldStatus = oldStatus;
		this.newStatus = newStatus;
	}

	@Override
	public Document getActivityXML() {
		final Document document = super.getActivityXML();

		final Element rootElement = document.getRootElement();
		rootElement.addElement( ACTIVITY_XML_TAG_OLD_STATUS_ID ).addText( String.valueOf( oldStatus.getId() ) );
		rootElement.addElement( ACTIVITY_XML_TAG_NEW_STATUS_ID ).addText( String.valueOf( newStatus.getId() ) );

		return document;
	}

	@Override
	public String getDisplayActivityDescription() {
		final TranslatorService translatorService = services.getTranslatorService();

		return translatorService.translate( "the status in the club has been changed from $1 to $2"
			, translatorService.translate( oldStatus.getName() )
			, translatorService.translate( newStatus.getName() )
		);
	}

	@Override
	public String toString() {
		return String.format( "%s: old status: %s, new status: %s", activityOfUser, oldStatus, newStatus );
	}
}
