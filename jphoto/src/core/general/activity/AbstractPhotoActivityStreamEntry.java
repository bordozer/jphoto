package core.general.activity;

import core.general.photo.Photo;
import core.general.user.User;
import core.services.security.Services;
import org.dom4j.Document;

import java.util.Date;

public abstract class AbstractPhotoActivityStreamEntry extends AbstractActivityStreamEntry {

	protected static final String ACTIVITY_XML_TAG_PHOTO_ID = "photoId";

	protected Photo activityOfPhoto;

	public AbstractPhotoActivityStreamEntry( final User user, final Photo photo, final Date activityTime, final ActivityType activityType, final Services services ) {
		super( user, activityTime, activityType, services );

		this.activityOfPhoto = photo;
	}

	@Override
	public Document getActivityXML() {
		final Document document = super.getActivityXML();
		document.getRootElement().addElement( ACTIVITY_XML_TAG_PHOTO_ID).addText( String.valueOf( activityOfPhoto.getId() ) );
		return document;
	}

	@Override
	public int getActivityOfPhotoId() {
		return activityOfPhoto.getId();
	}
}
