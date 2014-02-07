package core.general.activity;

import core.general.base.AbstractBaseEntity;
import core.general.photo.Photo;
import core.general.user.User;
import core.services.security.Services;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.Date;

public abstract class AbstractActivityStreamEntry extends AbstractBaseEntity {

	protected static final String ACTIVITY_XML_TAG_ROOT = "activity";
	protected static final String ACTIVITY_XML_TAG_ID = "id";

	private final ActivityType activityType;

	protected Date activityTime;

	protected int activityOfUserId;
	protected int activityOfPhotoId;

	protected boolean activityOfDeletedEntry;

	protected final Services services;

	public abstract String buildActivityXML();

	public abstract String getActivityDescription();

	/* Loading from DB */
	protected AbstractActivityStreamEntry( final ActivityType activityType, final Services services ) {
		this.activityType = activityType;
		this.services = services;
	}

	public String getDisplayActivityIcon() {
		return StringUtils.EMPTY;
	}

	public String getDisplayActivityUserLink() {
		if ( activityOfUserId == 0 ) {
			return StringUtils.EMPTY;
		}

		final User user = services.getUserService().load( activityOfUserId );

		if ( user == null ) {
			return String.format( "<font color='red'>The member #%d does not exist</font>", activityOfUserId ); // TODO: the member MUST be n DB or there is an error otherwise
		}

		return services.getEntityLinkUtilsService().getUserCardLink( user );
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

	public boolean isActivityOfDeletedEntry() {
		return activityOfDeletedEntry;
	}

	public void setActivityOfDeletedEntry( final boolean activityOfDeletedEntry ) {
		this.activityOfDeletedEntry = activityOfDeletedEntry;
	}

	protected String getXML( final String xmlTagPhotoId, final int id ) {
		final Document document = DocumentHelper.createDocument();

		final Element rootElement = document.addElement( ACTIVITY_XML_TAG_ROOT );
		rootElement.addElement( xmlTagPhotoId ).addText( String.valueOf( id ) );

		return document.asXML();
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

	protected void registerActivityEntryAsInvisibleForActivityStream() {
		activityOfDeletedEntry = true;
	}
}
