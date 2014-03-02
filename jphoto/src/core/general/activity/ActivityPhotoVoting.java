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
import utils.NumberUtils;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class ActivityPhotoVoting extends AbstractActivityStreamEntry {

	private static final String ACTIVITY_XML_TAG_VOTER_ID = "userId";
	private static final String ACTIVITY_XML_TAG_PHOTO_ID = "photoId";

	private static final String ACTIVITY_XML_TAG_VOTES = "votes";

	private User voter;
	private Photo photo;
	private String votes;

	public ActivityPhotoVoting( final String activityXML, final Services services ) throws DocumentException {
		super( ActivityType.PHOTO_VOTING, services );

		final Document document = DocumentHelper.parseText( activityXML );

		final Element rootElement = document.getRootElement();
		final int voterId = NumberUtils.convertToInt( rootElement.element( ACTIVITY_XML_TAG_VOTER_ID ).getText() );
		final int photoId = NumberUtils.convertToInt( rootElement.element( ACTIVITY_XML_TAG_PHOTO_ID ).getText() );
		votes = rootElement.element( ACTIVITY_XML_TAG_VOTES ).getText();

		voter = services.getUserService().load( voterId );
		photo = services.getPhotoService().load( photoId );

		if ( photo == null ) {
			registerActivityEntryAsInvisibleForActivityStream();
		}
	}

	public ActivityPhotoVoting( final User voter, final Photo photo, final List<UserPhotoVote> userPhotoVotes, final Date activityTime, final Services services ) {
		super( ActivityType.PHOTO_VOTING, services );

		this.voter = voter;
		this.photo = photo;
		this.activityTime = activityTime;

		activityOfUserId = this.voter.getId();
		activityOfPhotoId = photo.getId();

		final List<String> arr = newArrayList();
		for ( final UserPhotoVote userPhotoVote : userPhotoVotes ) {
			arr.add( String.format( "%s: %s", userPhotoVote.getPhotoVotingCategory().getName(), userPhotoVote.getMark() ) );
		}
		votes = StringUtils.join( arr, "<br />" );
	}

	@Override
	public String buildActivityXML() {
		final Document document = DocumentHelper.createDocument();

		final Element rootElement = document.addElement( ACTIVITY_XML_TAG_ROOT );
		rootElement.addElement( ACTIVITY_XML_TAG_VOTER_ID ).addText( String.valueOf( voter.getId() ) );
		rootElement.addElement( ACTIVITY_XML_TAG_PHOTO_ID ).addText( String.valueOf( photo.getId() ) );
		rootElement.addElement( ACTIVITY_XML_TAG_VOTES ).addText( votes );

		return document.asXML();
	}

	@Override
	public String getActivityDescription() {

		return services.getTranslatorService().translate( "voted for photo $1<br />$2"
			, services.getEntityLinkUtilsService().getPhotoCardLink( photo )
			, votes
		);
	}

	@Override
	public String getDisplayActivityIcon() {
		return getPhotoIcon( photo );
	}

	@Override
	public String toString() {
		return String.format( "%s: %s voted for %s", getActivityType(), voter, photo );
	}
}
