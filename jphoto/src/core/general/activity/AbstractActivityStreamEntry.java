package core.general.activity;

import core.general.base.AbstractBaseEntity;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.security.Services;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import utils.StringUtilities;

import java.util.Date;

public abstract class AbstractActivityStreamEntry extends AbstractBaseEntity {

	protected static final String ACTIVITY_XML_TAG_ROOT = "activity";
	protected static final String ACTIVITY_XML_TAG_ID = "id";

	private final ActivityType activityType;

	protected Date activityTime;

	protected int activityOfUserId;
	protected int activityOfPhotoId;

	protected final Services services;

	public abstract String buildActivityXML();

	public abstract String getActivityDescription();

	protected AbstractActivityStreamEntry( final ActivityType activityType, final Services services ) {
		this.activityType = activityType;
		this.services = services;
	}

	public String getDisplayActivityPicture() {
		return StringUtils.EMPTY;
	}

	public String getDisplayActivityUserLink() {
		if ( activityOfUserId == 0 ) {
			return StringUtils.EMPTY;
		}

		return services.getEntityLinkUtilsService().getUserCardLink( services.getUserService().load( activityOfUserId ) );
	}

	public int getDisplayActivityUserId() {
		return activityOfUserId;
	}

	public ActivityType getActivityType() {
		return activityType;
	}

	public Date getActivityTime() {
		return activityTime;
	}

	public void setActivityTime( final Date activityTime ) {
		this.activityTime = activityTime;
	}

	public int getActivityOfUserId() {
		return activityOfUserId;
	}

	public void setActivityOfUserId( final int activityOfUserId ) {
		this.activityOfUserId = activityOfUserId;
	}

	public int getActivityOfPhotoId() {
		return activityOfPhotoId;
	}

	public void setActivityOfPhotoId( final int activityOfPhotoId ) {
		this.activityOfPhotoId = activityOfPhotoId;
	}

	protected String getXML( final String xmlTagPhotoId, final int id ) {
		final Document document = DocumentHelper.createDocument();

		final Element rootElement = document.addElement( ACTIVITY_XML_TAG_ROOT );
		rootElement.addElement( xmlTagPhotoId ).addText( String.valueOf( id ) );

		return document.asXML();
	}

	protected String getPhotoIcon( final Photo photo ) {
		return String.format( "<a href='%1$s'><img src='%2$s' height='30' alt='%3$s' title='%3$s'/></a>"
			, services.getUrlUtilsService().getPhotoCardLink( photo.getId() )
			, services.getUserPhotoFilePathUtilsService().getPhotoUrl( photo )
			, photo.getNameEscaped()
		);
	}
}
