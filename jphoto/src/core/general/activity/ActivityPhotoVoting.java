package core.general.activity;

import core.general.photo.Photo;
import core.general.user.User;
import core.general.user.UserPhotoVote;
import core.services.security.Services;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class ActivityPhotoVoting extends AbstractPhotoActivityStreamEntry {

	private static final String ACTIVITY_XML_TAG_VOTES = "votes";

	private String votes;

	public ActivityPhotoVoting( final User user, final Photo photo, final Date activityTime, final String activityXML, final Services services ) throws DocumentException {
		super( user, photo, activityTime, ActivityType.PHOTO_VOTING, services );

		final Document document = DocumentHelper.parseText( activityXML );
		final Element rootElement = document.getRootElement();

		votes = rootElement.element( ACTIVITY_XML_TAG_VOTES ).getText();
	}

	public ActivityPhotoVoting( final User user, final Photo photo, final List<UserPhotoVote> userPhotoVotes, final Date activityTime, final Services services ) {
		super( user, photo, activityTime, ActivityType.PHOTO_VOTING, services );

		final List<String> arr = newArrayList();
		for ( final UserPhotoVote userPhotoVote : userPhotoVotes ) {
			arr.add( String.format( "%s: %s", userPhotoVote.getPhotoVotingCategory().getName(), userPhotoVote.getMark() ) );
		}
		votes = StringUtils.join( arr, "<br />" );
	}

	@Override
	public Document getActivityXML() {
		final Document document = super.getActivityXML();
		document.getRootElement().addElement( ACTIVITY_XML_TAG_VOTES ).addText( votes );
		return document;
	}

	@Override
	public String getDisplayActivityDescription() {

		return services.getTranslatorService().translate( "voted for photo $1<br />$2"
			, services.getEntityLinkUtilsService().getPhotoCardLink( activityOfPhoto )
			, votes
		);
	}

	@Override
	public String getDisplayActivityIcon() {
		return getPhotoIcon( activityOfPhoto );
	}

	@Override
	public String toString() {
		return String.format( "%s: %s voted for %s", getActivityType(), activityOfUser, activityOfPhoto );
	}
}
