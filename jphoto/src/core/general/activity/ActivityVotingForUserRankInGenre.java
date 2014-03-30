package core.general.activity;

import core.general.user.User;
import core.general.user.UserRankInGenreVoting;
import core.services.security.Services;
import core.services.translator.message.TranslatableMessage;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import utils.NumberUtils;

import java.util.Date;

public class ActivityVotingForUserRankInGenre extends AbstractActivityStreamEntry {

	protected static final String ACTIVITY_XML_TAG_USER_ID_VOTED_FOR = "userId";
	protected static final String ACTIVITY_XML_TAG_GENRE_ID = "genreId";
	protected static final String ACTIVITY_XML_TAG_POINTS = "points";

	private int userVotedForId;
	private int genreId;
	private int points;

	public ActivityVotingForUserRankInGenre( final User user, final Date activityTime, final String activityXML, final Services services ) throws DocumentException {
		super( user, activityTime, ActivityType.VOTING_FOR_USER_RANK_IN_GENRE, services );

		final Document document = DocumentHelper.parseText( activityXML );
		final Element rootElement = document.getRootElement();

		userVotedForId = NumberUtils.convertToInt( rootElement.element( ACTIVITY_XML_TAG_USER_ID_VOTED_FOR ).getText() );
		genreId = NumberUtils.convertToInt( rootElement.element( ACTIVITY_XML_TAG_GENRE_ID ).getText() );
		points = NumberUtils.convertToInt( rootElement.element( ACTIVITY_XML_TAG_POINTS ).getText() );
	}

	public ActivityVotingForUserRankInGenre( final UserRankInGenreVoting rankInGenreVoting, final Services services ) {
		super( services.getUserService().load( rankInGenreVoting.getUserId() ), rankInGenreVoting.getVotingTime(), ActivityType.VOTING_FOR_USER_RANK_IN_GENRE, services );

		userVotedForId = rankInGenreVoting.getUserId();
		genreId = rankInGenreVoting.getGenreId();
		points = rankInGenreVoting.getPoints();
	}

	@Override
	public Document getActivityXML() {
		final Document document = super.getActivityXML();

		final Element rootElement = document.getRootElement();
		rootElement.addElement( ACTIVITY_XML_TAG_USER_ID_VOTED_FOR ).addText( String.valueOf( userVotedForId ) );
		rootElement.addElement( ACTIVITY_XML_TAG_GENRE_ID ).addText( String.valueOf( genreId ) );
		rootElement.addElement( ACTIVITY_XML_TAG_POINTS ).addText( getPoints() );

		return document;
	}

	@Override
	public String getDisplayActivityDescription() {

		final TranslatableMessage translatableMessage = new TranslatableMessage( "voted for rank of $1 in category $2 ( $3 )", services )
			.addUserCardLinkParameter( userVotedForId )
			.addPhotosByGenreLinkParameter( genreId )
			.addStringParameter( getPoints() )
			;

		return translatableMessage.build( getCurrentUserLanguage() );
	}

	private String getPoints() {
		return points > 0 ? String.format( "+%d", points ) : String.valueOf( points );
	}
}
