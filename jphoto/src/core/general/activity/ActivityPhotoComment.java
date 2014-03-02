package core.general.activity;

import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.services.security.Services;
import core.services.utils.EntityLinkUtilsService;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import utils.NumberUtils;
import utils.TranslatorUtils;

public class ActivityPhotoComment extends AbstractActivityStreamEntry {

	private PhotoComment comment;
	private Photo photo;

	public ActivityPhotoComment( final String activityXML, final Services services ) throws DocumentException {
		super( ActivityType.PHOTO_COMMENT, services );

		final Document document = DocumentHelper.parseText( activityXML );

		final Element rootElement = document.getRootElement();
		final int commentId = NumberUtils.convertToInt( rootElement.element( ACTIVITY_XML_TAG_ID ).getText() );

		comment = services.getPhotoCommentService().load( commentId );
		if ( comment == null ) {
			registerActivityEntryAsInvisibleForActivityStream();
		} else {
			photo = services.getPhotoService().load( comment.getPhotoId() );
			if ( photo == null ) {
				registerActivityEntryAsInvisibleForActivityStream();
			}
		}
	}

	public ActivityPhotoComment( final PhotoComment comment, final Services services ) {
		super( ActivityType.PHOTO_COMMENT, services );
		this.comment = comment;
		this.activityTime = comment.getCreationTime();

		activityOfUserId = comment.getCommentAuthor().getId();
		activityOfPhotoId = comment.getPhotoId();
	}

	@Override
	public String buildActivityXML() {
		return getXML( ACTIVITY_XML_TAG_ID, comment.getId() );
	}

	@Override
	public String getActivityDescription() {
		final EntityLinkUtilsService linkUtilsService = services.getEntityLinkUtilsService();

		return TranslatorUtils.translate( "commented photo $1"
			, linkUtilsService.getPhotoCardLink( photo )
		);
	}

	@Override
	public String getDisplayActivityIcon() {
		return getPhotoIcon( photo );
	}

	@Override
	public String toString() {
		return String.format( "%s: %s", getActivityType(), comment );
	}
}
