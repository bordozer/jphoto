package core.general.activity;

import core.context.EnvironmentContext;
import core.general.genre.Genre;
import core.general.user.User;
import core.services.security.Services;
import core.services.translator.TranslatorService;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import utils.NumberUtils;

import java.util.Date;

public class ActivityUserRankInGenreChanged extends AbstractActivityStreamEntry {

	private static final String ACTIVITY_XML_TAG_GENRE_ID = "genreId";
	private static final String ACTIVITY_XML_TAG_OLD_RANK = "old_rank";
	private static final String ACTIVITY_XML_TAG_NEW_RANK = "new_rank";

	private final Genre genre;
	private final int oldRank;
	private final int newRank;

	public ActivityUserRankInGenreChanged( final User activityOfUser, final Date activityTime, final String activityXML, final Services services ) throws DocumentException {
		super( activityOfUser, activityTime, ActivityType.USER_RANK_IN_GENRE_CHANGED, services );

		final Document document = DocumentHelper.parseText( activityXML );
		final Element rootElement = document.getRootElement();

		genre = services.getGenreService().load( NumberUtils.convertToInt( rootElement.element( ACTIVITY_XML_TAG_GENRE_ID ).getText() ) );
		oldRank = 	NumberUtils.convertToInt( rootElement.element( ACTIVITY_XML_TAG_OLD_RANK ).getText() );
		newRank = 	NumberUtils.convertToInt( rootElement.element( ACTIVITY_XML_TAG_NEW_RANK ).getText() );
	}

	public ActivityUserRankInGenreChanged( final User user, final Genre genre, final int oldRank, final int newRank, final Date activityTime, final Services services ) {
		super( user, activityTime, ActivityType.USER_RANK_IN_GENRE_CHANGED, services );

		this.genre = genre;
		this.oldRank = oldRank;
		this.newRank = newRank;
	}

	@Override
	public Document getActivityXML() {
		final Document document = super.getActivityXML();
		final Element rootElement = document.getRootElement();

		rootElement.addElement( ACTIVITY_XML_TAG_GENRE_ID ).addText( String.valueOf( genre.getId() ) );
		rootElement.addElement( ACTIVITY_XML_TAG_OLD_RANK ).addText( String.valueOf( oldRank ) );
		rootElement.addElement( ACTIVITY_XML_TAG_NEW_RANK ).addText( String.valueOf( newRank ) );

		return document;
	}

	@Override
	public String getDisplayActivityDescription() {
		final TranslatorService translatorService = services.getTranslatorService();

		return translatorService.translate( "rank in category '$1' has changed from $2 to $3"
			, services.getEntityLinkUtilsService().getPhotosByUserByGenreLink( activityOfUser, genre, EnvironmentContext.getCurrentUser().getLanguage() )
			, String.valueOf( oldRank )
			, String.valueOf( newRank )
		);
	}
}
