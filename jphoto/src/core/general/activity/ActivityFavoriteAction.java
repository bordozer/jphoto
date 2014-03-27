package core.general.activity;

import core.enums.FavoriteEntryType;
import core.general.user.User;
import core.services.security.Services;
import core.services.utils.EntityLinkUtilsService;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import utils.NumberUtils;

import java.util.Date;

public class ActivityFavoriteAction extends AbstractActivityStreamEntry {

	protected static final String ACTIVITY_XML_TAG_FAVORITE_ENTRY_ID = "favoriteEntryId";
	protected static final String ACTIVITY_XML_TAG_FAVORITE_TYPE_ID = "favoriteTypeId";

	private int favoriteEntryId;
	private FavoriteEntryType favoriteType;

	public ActivityFavoriteAction( final User user, final Date activityTime, final String activityXML, final Services services ) throws DocumentException {
		super( user, activityTime, ActivityType.FAVORITE_ACTION, services );

		final Document document = DocumentHelper.parseText( activityXML );
		final Element rootElement = document.getRootElement();

		favoriteEntryId = NumberUtils.convertToInt( rootElement.element( ACTIVITY_XML_TAG_FAVORITE_ENTRY_ID ).getText() );
		favoriteType = FavoriteEntryType.getById( NumberUtils.convertToInt( rootElement.element( ACTIVITY_XML_TAG_FAVORITE_TYPE_ID ).getText() ) );
	}

	public ActivityFavoriteAction( final User user, final int favoriteEntryId, final Date activityTime, final FavoriteEntryType entryType, final Services services ) {
		super( user, activityTime, ActivityType.FAVORITE_ACTION, services );

		this.favoriteEntryId = favoriteEntryId;
		this.favoriteType = entryType;
	}

	@Override
	public Document getActivityXML() {
		final Document document = super.getActivityXML();

		final Element rootElement = document.getRootElement();
		rootElement.addElement( ACTIVITY_XML_TAG_FAVORITE_ENTRY_ID ).addText( String.valueOf( favoriteEntryId ) );
		rootElement.addElement( ACTIVITY_XML_TAG_FAVORITE_TYPE_ID ).addText( String.valueOf( favoriteType.getId() ) );

		return document;
	}

	@Override
	public String getDisplayActivityDescription() {
		return services.getTranslatorService().translate( "added $1 to $2"
			, getCurrentUserLanguage()
			, getFavoriteEntry( favoriteEntryId, favoriteType )
			, favoriteType.getNameTranslated()
		);
	}

	@Override
	public String toString() {
		return String.format( "%s added %s to %s", activityOfUser, favoriteEntryId, favoriteType );
	}

	@Override
	public String getDisplayActivityIcon() {
		if ( favoriteType.isRelatedToPhoto() ) {
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
				return linkUtilsService.getPhotoCardLink( services.getPhotoService().load( favoriteEntryId ), getCurrentUserLanguage() );
		}

		throw new IllegalArgumentException( String.format( "Illegal FavoriteEntryType: %s", entryType ) );
	}
}
