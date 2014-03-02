package core.general.activity;

import core.general.user.UserRankInGenreVoting;
import core.services.security.Services;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import utils.NumberUtils;
import utils.TranslatorUtils;

public class ActivityVotingForUserRankInGenre extends AbstractActivityStreamEntry {

	protected static final String ACTIVITY_XML_TAG_USER_ID = "userId";
	protected static final String ACTIVITY_XML_TAG_VOTER_ID = "voterId";
	protected static final String ACTIVITY_XML_TAG_GENRE_ID = "genreId";
	protected static final String ACTIVITY_XML_TAG_POINTS = "points";

	private int userVotedForId;
	private int genreId;
	private int points;

	public ActivityVotingForUserRankInGenre( final String activityXML, final Services services ) throws DocumentException {
		super( ActivityType.VOTING_FOR_USER_RANK_IN_GENRE, services );

		final Document document = DocumentHelper.parseText( activityXML );

		final Element rootElement = document.getRootElement();
		activityOfUserId = NumberUtils.convertToInt( rootElement.element( ACTIVITY_XML_TAG_VOTER_ID ).getText() );
		userVotedForId = NumberUtils.convertToInt( rootElement.element( ACTIVITY_XML_TAG_USER_ID ).getText() );
		genreId = NumberUtils.convertToInt( rootElement.element( ACTIVITY_XML_TAG_GENRE_ID ).getText() );
		points = NumberUtils.convertToInt( rootElement.element( ACTIVITY_XML_TAG_POINTS ).getText() );
	}

	public ActivityVotingForUserRankInGenre( final UserRankInGenreVoting rankInGenreVoting, final Services services ) {
		super( ActivityType.VOTING_FOR_USER_RANK_IN_GENRE, services );

		this.activityTime = rankInGenreVoting.getVotingTime();
		activityOfUserId = rankInGenreVoting.getVoterId();

		userVotedForId = rankInGenreVoting.getUserId();
		genreId = rankInGenreVoting.getGenreId();
		points = rankInGenreVoting.getPoints();
	}

	@Override
	public String buildActivityXML() {
		final Document document = DocumentHelper.createDocument();

		final Element rootElement = document.addElement( ACTIVITY_XML_TAG_ROOT );
		rootElement.addElement( ACTIVITY_XML_TAG_USER_ID ).addText( String.valueOf( userVotedForId ) );
		rootElement.addElement( ACTIVITY_XML_TAG_VOTER_ID ).addText( String.valueOf( activityOfUserId ) );
		rootElement.addElement( ACTIVITY_XML_TAG_GENRE_ID ).addText( String.valueOf( genreId ) );
		rootElement.addElement( ACTIVITY_XML_TAG_POINTS ).addText( getPoints() );

		return document.asXML();
	}

	@Override
	public String getActivityDescription() {

		return TranslatorUtils.translate( "voted for rank of $1 in category $2 ( $3 )"
			, services.getEntityLinkUtilsService().getUserCardLink( services.getUserService().load( userVotedForId ) )
			, services.getGenreService().load( genreId ).getName()
			, getPoints()
		);
	}

	private String getPoints() {
		return points > 0 ? String.format( "+%d", points ) : String.valueOf( points );
	}
}
