package ui.activity;

import core.general.photo.Photo;
import core.general.photo.PhotoComment;
import core.general.user.User;
import core.services.system.Services;
import core.services.translator.Language;
import core.services.translator.message.TranslatableMessage;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import ui.context.EnvironmentContext;
import utils.NumberUtils;

import java.util.Date;

public class ActivityPhotoComment extends AbstractPhotoActivityStreamEntry {

	protected static final String ACTIVITY_XML_TAG_PHOTO_COMMENT_ID = "photoCommentId";

	private PhotoComment comment;

	public ActivityPhotoComment( final User user, final Photo photo, final Date activityTime, final String activityXML, final Services services ) throws DocumentException {
		super( user, photo, activityTime, ActivityType.PHOTO_COMMENT, services );

		final Document document = DocumentHelper.parseText( activityXML );
		final Element rootElement = document.getRootElement();

		final int commentId = NumberUtils.convertToInt( rootElement.element( ACTIVITY_XML_TAG_PHOTO_COMMENT_ID ).getText() );
		comment = services.getPhotoCommentService().load( commentId );
	}

	public ActivityPhotoComment( final PhotoComment comment, final Services services ) {
		super( comment.getCommentAuthor(), services.getPhotoService().load( comment.getPhotoId() ), comment.getCreationTime(), ActivityType.PHOTO_COMMENT, services );
		this.comment = comment;
	}

	@Override
	public Document getActivityXML() {
		final Document document = super.getActivityXML();
		document.getRootElement().addElement( ACTIVITY_XML_TAG_PHOTO_COMMENT_ID).addText( String.valueOf( comment.getId() ) );
		return document;
	}

	@Override
	protected TranslatableMessage getActivityTranslatableText() {
		return new TranslatableMessage( "activity stream entry: commented photo $1", services ).addPhotoCardLinkParameter( activityOfPhoto );
	}

	@Override
	public String getActivityTextForAdmin( final Language language ) {
		if ( services.getSecurityService().isSuperAdminUser( EnvironmentContext.getCurrentUser() ) ) {
			return new TranslatableMessage( services ).string( "<div class='photoCommentText'>" ).string( comment.getCommentText() ).string( "</div>" ).build( language );
		}

		return super.getActivityTextForAdmin( language );
	}

	@Override
	public String getDisplayActivityIcon() {
		return getPhotoIcon( activityOfPhoto );
	}

	@Override
	public String toString() {
		return String.format( "%s: %s", getActivityType(), comment );
	}
}
