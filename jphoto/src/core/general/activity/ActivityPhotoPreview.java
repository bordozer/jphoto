package core.general.activity;

import core.general.photo.Photo;
import core.general.photo.PhotoPreview;
import core.general.user.User;
import core.services.security.Services;
import core.services.utils.EntityLinkUtilsService;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import utils.NumberUtils;

import java.util.Date;

public class ActivityPhotoPreview extends AbstractPhotoActivityStreamEntry {

	protected static final String ACTIVITY_XML_TAG_PHOTO_PREVIEW_ID = "photoPreviewId";

	private PhotoPreview preview;

	public ActivityPhotoPreview( final User user, final Photo photo, final Date activityTime, final String activityXML, final Services services ) throws DocumentException {
		super( user, photo, activityTime, ActivityType.PHOTO_PREVIEW, services );

		final Document document = DocumentHelper.parseText( activityXML );
		final Element rootElement = document.getRootElement();

		final int previewId = NumberUtils.convertToInt( rootElement.element( ACTIVITY_XML_TAG_PHOTO_PREVIEW_ID ).getText() );
		preview = services.getPhotoPreviewService().load( previewId );
	}

	public ActivityPhotoPreview( final PhotoPreview preview, final Services services ) {
		super( preview.getUser(), preview.getPhoto(), preview.getPreviewTime(), ActivityType.PHOTO_PREVIEW, services );

		this.preview = preview;
	}

	@Override
	public Document getActivityXML() {
		final Document document = super.getActivityXML();
		document.getRootElement().addElement( ACTIVITY_XML_TAG_PHOTO_PREVIEW_ID ).addText( String.valueOf( preview.getId() ) );
		return document;
	}

	@Override
	public String getDisplayActivityDescription() {
		if ( preview == null ) {
			return StringUtils.EMPTY;
		}

		final EntityLinkUtilsService linkUtilsService = services.getEntityLinkUtilsService();

		return services.getTranslatorService().translate( "viewed photo $1"
			, linkUtilsService.getPhotoCardLink( preview.getPhoto() )
		);
	}

	@Override
	public String getDisplayActivityIcon() {
		if ( preview == null ) {
			return StringUtils.EMPTY;
		}

		return getPhotoIcon( preview.getPhoto() );
	}

	@Override
	public String toString() {
		return String.format( "%s: %s", getActivityType(), preview );
	}
}
