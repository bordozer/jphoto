package core.general.activity;

import core.general.base.AbstractBaseEntity;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.security.Services;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.Date;

public abstract class AbstractActivityStreamEntry extends AbstractBaseEntity {

	private static final String ACTIVITY_XML_TAG_ROOT = "activity";
	private static final String ACTIVITY_XML_TAG_USER_ID = "userId";

	protected final User activityOfUser;
	protected final Date activityTime;
	protected final ActivityType activityType;

	protected final Services services;

	public abstract String getDisplayActivityDescription();

	/* Loading from DB */
	public AbstractActivityStreamEntry( final User activityOfUser, final Date activityTime, final ActivityType activityType, final Services services ) {
		this.activityOfUser = activityOfUser;
		this.activityTime = activityTime;
		this.activityType = activityType;

		this.services = services;
	}

	public String getDisplayActivityIcon() {
		return StringUtils.EMPTY;
	}

	public String getDisplayActivityUserLink() {
		if ( activityOfUser == null ) {
			return StringUtils.EMPTY;
		}

		return services.getEntityLinkUtilsService().getUserCardLink( activityOfUser );
	}

	public int getDisplayActivityUserId() {
		return activityOfUser.getId();
	}

	public ActivityType getActivityType() {
		return activityType;
	}

	public Date getActivityTime() {
		return activityTime;
	}

	public User getActivityOfUser() {
		return activityOfUser;
	}

	public int getActivityOfPhotoId() {
		return 0;
	}

	public Document getActivityXML() {
		final Document document = getEmptyDocument();

		final Element rootElement = document.addElement( ACTIVITY_XML_TAG_ROOT );
		rootElement.addElement( ACTIVITY_XML_TAG_USER_ID ).addText( String.valueOf( activityOfUser.getId() ) );

		return document;
	}

	protected String getPhotoIcon( final Photo photo ) {
		if( photo == null ) {
			return StringUtils.EMPTY;
		}

		return String.format( "<a href='%1$s'><img src='%2$s' height='30' alt='%3$s' title='%3$s'/></a>"
			, services.getUrlUtilsService().getPhotoCardLink( photo.getId() )
			, services.getUserPhotoFilePathUtilsService().getPhotoUrl( photo )
			, photo.getNameEscaped()
		);
	}
}
