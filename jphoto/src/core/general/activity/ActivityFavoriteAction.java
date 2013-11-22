package core.general.activity;

import core.enums.FavoriteEntryType;
import core.services.security.Services;
import core.services.utils.EntityLinkUtilsService;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import utils.NumberUtils;
import utils.TranslatorUtils;

import java.util.Date;

public class ActivityFavoriteAction extends AbstractActivityStreamEntry {

	protected static final String ACTIVITY_XML_TAG_VOTER_ID = "userId";
	protected static final String ACTIVITY_XML_TAG_FAVORITE_ENTRY_ID = "favoriteEntryId";
	protected static final String ACTIVITY_XML_TAG_FAVORITE_TYPE_ID = "favoriteTypeId";

	private int favoriteEntryId;
	private FavoriteEntryType favoriteType;

	public ActivityFavoriteAction( final String activityXML, final Services services ) throws DocumentException {
		super( ActivityType.FAVORITE_ACTION, services );

		final Document document = DocumentHelper.parseText( activityXML );

		final Element rootElement = document.getRootElement();
		activityOfUserId = NumberUtils.convertToInt( rootElement.element( ACTIVITY_XML_TAG_VOTER_ID ).getText() );
		favoriteEntryId = NumberUtils.convertToInt( rootElement.element( ACTIVITY_XML_TAG_FAVORITE_ENTRY_ID ).getText() );
		favoriteType = FavoriteEntryType.getById( NumberUtils.convertToInt( rootElement.element( ACTIVITY_XML_TAG_FAVORITE_TYPE_ID ).getText() ) );
	}

	public ActivityFavoriteAction( final int userId, final int favoriteEntryId, final Date time, final FavoriteEntryType entryType, final Services services ) {
		super( ActivityType.FAVORITE_ACTION, services );

		this.activityTime = time;

		this.activityOfUserId = userId;
		this.favoriteEntryId = favoriteEntryId;
		this.favoriteType = entryType;
	}

	@Override
	public String buildActivityXML() {
		final Document document = DocumentHelper.createDocument();

		final Element rootElement = document.addElement( ACTIVITY_XML_TAG_ROOT );
		rootElement.addElement( ACTIVITY_XML_TAG_VOTER_ID ).addText( String.valueOf( activityOfUserId ) );
		rootElement.addElement( ACTIVITY_XML_TAG_FAVORITE_ENTRY_ID ).addText( String.valueOf( favoriteEntryId ) );
		rootElement.addElement( ACTIVITY_XML_TAG_FAVORITE_TYPE_ID ).addText( String.valueOf( favoriteType.getId() ) );

		return document.asXML();
	}

	@Override
	public String getActivityDescription() {
		return TranslatorUtils.translate( "added $1 to $2"
			, getFavoriteEntry( favoriteEntryId, favoriteType )
			, favoriteType.getNameTranslated()
		);
	}

	@Override
	public String toString() {
		return String.format( "%s added %s to %s", activityOfUserId, favoriteEntryId, favoriteType );
	}

	@Override
	public String getDisplayActivityIcon() {
		if ( favoriteType == FavoriteEntryType.PHOTO || favoriteType == FavoriteEntryType.BOOKMARK ) {
			return getPhotoIcon( services.getPhotoService().load( favoriteEntryId ) );
		}

		return super.getDisplayActivityIcon();
	}

	private String getFavoriteEntry( final int favoriteEntryId, final FavoriteEntryType entryType ) {
		final EntityLinkUtilsService linkUtilsService = services.getEntityLinkUtilsService();

		switch ( entryType ) {
			case USER:
				case FRIEND:
				case BLACKLIST:
				case NEW_PHOTO_NOTIFICATION:
				return linkUtilsService.getUserCardLink( services.getUserService().load( favoriteEntryId ) );
			case PHOTO:
				case BOOKMARK:
				case NEW_COMMENTS_NOTIFICATION:
				return linkUtilsService.getPhotoCardLink( services.getPhotoService().load( favoriteEntryId ) );
		}

		throw new IllegalArgumentException( String.format( "Illegal FavoriteEntryType: %s", entryType ) );
	}
}
