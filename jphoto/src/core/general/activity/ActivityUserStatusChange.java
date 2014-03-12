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

	private static final String ACTIVITY_XML_TAG_USER_ID = "userId";
	private static final String ACTIVITY_XML_TAG_OLD_STATUS_ID = "old_status";
	private static final String ACTIVITY_XML_TAG_NEW_STATUS_ID = "new_status";

	private User user;
	private UserStatus oldStatus;
	private UserStatus newStatus;

	public ActivityUserStatusChange( final String activityXML, final Services services ) throws DocumentException {
		super( ActivityType.PHOTO_VOTING, services );

		final Document document = DocumentHelper.parseText( activityXML );

		final Element rootElement = document.getRootElement();

		final int userId = NumberUtils.convertToInt( rootElement.element( ACTIVITY_XML_TAG_USER_ID ).getText() );
		user = services.getUserService().load( userId );

		oldStatus = UserStatus.getById( NumberUtils.convertToInt( rootElement.element( ACTIVITY_XML_TAG_OLD_STATUS_ID ).getText() ) );
		newStatus = UserStatus.getById( NumberUtils.convertToInt( rootElement.element( ACTIVITY_XML_TAG_NEW_STATUS_ID ).getText() ) );
	}

	public ActivityUserStatusChange( final User user, final UserStatus oldStatus, final UserStatus newStatus, final Date activityTime, final Services services ) {
		super( ActivityType.USER_MEMBERSHIP, services );

		this.user = user;
		this.oldStatus = oldStatus;
		this.newStatus = newStatus;

		this.activityTime = activityTime;
	}

	@Override
	public String buildActivityXML() {
		final Document document = DocumentHelper.createDocument();

		final Element rootElement = document.addElement( ACTIVITY_XML_TAG_ROOT );
		rootElement.addElement( ACTIVITY_XML_TAG_USER_ID ).addText( String.valueOf( user.getId() ) );
		rootElement.addElement( ACTIVITY_XML_TAG_OLD_STATUS_ID ).addText( String.valueOf( oldStatus.getId() ) );
		rootElement.addElement( ACTIVITY_XML_TAG_NEW_STATUS_ID ).addText( String.valueOf( newStatus.getId() ) );

		return document.asXML();
	}

	@Override
	public String getActivityDescription() {
		final TranslatorService translatorService = services.getTranslatorService();

		return translatorService.translate( "$1: the status in the club has been changed from $2 to $3"
			, user.getNameEscaped()
			, translatorService.translate( oldStatus.getName() )
			, translatorService.translate( newStatus.getName() )
		);
	}

	@Override
	public String toString() {
		return String.format( "%s: old status: %s, new status: %s", user, oldStatus, newStatus );
	}
}
