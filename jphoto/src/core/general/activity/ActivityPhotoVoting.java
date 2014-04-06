package core.general.activity;

import core.general.photo.Photo;
import core.general.photo.PhotoVotingCategory;
import core.general.user.User;
import core.general.user.UserPhotoVote;
import core.services.entry.VotingCategoryService;
import core.services.system.Services;
import core.services.translator.message.TranslatableMessage;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import utils.NumberUtils;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

public class ActivityPhotoVoting extends AbstractPhotoActivityStreamEntry {

	private static final String ACTIVITY_XML_TAG_VOTES = "votes";
	private static final String ACTIVITY_XML_TAG_VOTE = "vote";
	private static final String ACTIVITY_XML_TAG_VOTING_CATEGORY_ID = "voting-category-id";

	private final Map<PhotoVotingCategory, Integer> votes = newHashMap();

	public ActivityPhotoVoting( final User user, final Photo photo, final Date activityTime, final String activityXML, final Services services ) throws DocumentException {
		super( user, photo, activityTime, ActivityType.PHOTO_VOTING, services );

		final VotingCategoryService votingCategoryService = services.getVotingCategoryService();

		final Document document = DocumentHelper.parseText( activityXML );
		final Element rootElement = document.getRootElement();

		final Element votesElement = rootElement.element( ACTIVITY_XML_TAG_VOTES );
		final Iterator votesIterator = votesElement.elementIterator( ACTIVITY_XML_TAG_VOTE );
		while ( votesIterator.hasNext() ) {

			final Element voteElement = ( Element ) votesIterator.next();

			final int votingPoints = NumberUtils.convertToInt( voteElement.getText() );

			final int photoVotingCategoryId = NumberUtils.convertToInt( voteElement.attribute( ACTIVITY_XML_TAG_VOTING_CATEGORY_ID ).getValue() );
			final PhotoVotingCategory photoVotingCategory = votingCategoryService.load( photoVotingCategoryId );
			if ( photoVotingCategory != null ) {
				votes.put( photoVotingCategory, votingPoints );
			}
		}

		initActivityTranslatableText();
	}

	public ActivityPhotoVoting( final User user, final Photo photo, final List<UserPhotoVote> userPhotoVotes, final Date activityTime, final Services services ) {
		super( user, photo, activityTime, ActivityType.PHOTO_VOTING, services );

		for ( final UserPhotoVote userPhotoVote : userPhotoVotes ) {
			votes.put( userPhotoVote.getPhotoVotingCategory(), userPhotoVote.getMark() );
		}

		initActivityTranslatableText();
	}

	@Override
	public Document getActivityXML() {
		final Document document = super.getActivityXML();
		final Element votesElement = document.getRootElement().addElement( ACTIVITY_XML_TAG_VOTES );

		for ( final PhotoVotingCategory votingCategory : votes.keySet() ) {
			final int points = votes.get( votingCategory );
			final Element element = votesElement.addElement( ACTIVITY_XML_TAG_VOTE );
			element.addAttribute( ACTIVITY_XML_TAG_VOTING_CATEGORY_ID, String.valueOf( votingCategory.getId() ) );
			element.addText( String.valueOf( points ) );
		}

		return document;
	}

	@Override
	protected TranslatableMessage getActivityTranslatableText() {

		final TranslatableMessage translatableMessage = new TranslatableMessage( "appraised photo $1<br />", services )
			.addPhotoCardLinkParameter( activityOfPhoto )
			;

		for ( final PhotoVotingCategory photoVotingCategory : votes.keySet() ) {
			final int points = votes.get( photoVotingCategory );

			final TranslatableMessage photoVotingCategoryMessage = new TranslatableMessage( "$1: $2", services )
				.addPhotoVotingCategoryParameterParameter( photoVotingCategory )
				.addIntegerParameter( points );

			translatableMessage.addTranslatableMessageParameter( photoVotingCategoryMessage );
			translatableMessage.string( "<br />" );
		}

		return translatableMessage;
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
