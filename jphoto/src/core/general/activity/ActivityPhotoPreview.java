package core.general.activity;

import core.general.photo.PhotoPreview;
import core.services.security.Services;
import core.services.utils.EntityLinkUtilsService;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import utils.NumberUtils;

public class ActivityPhotoPreview extends AbstractActivityStreamEntry {

	private PhotoPreview preview;

	public ActivityPhotoPreview( final String activityXML, final Services services ) throws DocumentException {
		super( ActivityType.PHOTO_PREVIEW, services );

		final Document document = DocumentHelper.parseText( activityXML );

		final Element rootElement = document.getRootElement();
		final int previewId = NumberUtils.convertToInt( rootElement.element( ACTIVITY_XML_TAG_ID ).getText() );
		preview = services.getPhotoPreviewService().load( previewId );
		if ( preview == null ) {
			registerActivityEntryAsInvisibleForActivityStream();
		}
	}

	public ActivityPhotoPreview( final PhotoPreview preview, final Services services ) {
		super( ActivityType.PHOTO_PREVIEW, services );
		this.preview = preview;
		this.activityTime = preview.getPreviewTime();

		activityOfUserId = preview.getUser().getId();
		activityOfPhotoId = preview.getPhoto().getId();
	}

	@Override
	public String buildActivityXML() {
		return getXML( ACTIVITY_XML_TAG_ID, preview.getId() );
	}

	@Override
	public String getActivityDescription() {
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
