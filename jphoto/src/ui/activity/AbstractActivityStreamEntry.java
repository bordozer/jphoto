package ui.activity;

import core.general.base.AbstractBaseEntity;
import core.general.photo.Photo;
import core.general.user.User;
import core.interfaces.Cacheable;
import core.log.LogHelper;
import core.services.system.Services;
import core.services.translator.Language;
import core.services.translator.message.EmptyTranslatableMessage;
import core.services.translator.message.TranslatableMessage;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import ui.context.EnvironmentContext;

import java.util.Date;

public abstract class AbstractActivityStreamEntry extends AbstractBaseEntity {

	private static final String ACTIVITY_XML_TAG_ROOT = "activity";
	private static final String ACTIVITY_XML_TAG_USER_ID = "userId";

	protected final User activityOfUser;
	protected final Date activityTime;
	protected final ActivityType activityType;

	protected final Services services;

	protected abstract TranslatableMessage getActivityTranslatableText();

	protected LogHelper log = new LogHelper( this.getClass() );

	/* Loading from DB */
	public AbstractActivityStreamEntry( final User activityOfUser, final Date activityTime, final ActivityType activityType, final Services services ) {
		this.activityOfUser = activityOfUser;
		this.activityTime = activityTime;
		this.activityType = activityType;

		this.services = services;
	}

	public String getDisplayActivityIcon() {
		return StringUtils.EMPTY;
	}

	public String getActivityText( final Language language ) {
		return getActivityTranslatableText().build( language );
	}

	public String getActivityTextForAdmin( final Language language ) {
		return StringUtils.EMPTY;
	}

	public TranslatableMessage getDisplayActivityUserLink() {
		if ( activityOfUser == null ) {
			return new EmptyTranslatableMessage();
		}

		return new TranslatableMessage( services ).addUserCardLinkParameter( activityOfUser );
	}

	public int getDisplayActivityUserId() {
		return activityOfUser.getId();
	}

	public ActivityType getActivityType() {
		return activityType;
	}

	public Date getActivityTime() {
		return activityTime;
	}

	public User getActivityOfUser() {
		return activityOfUser;
	}

	public int getActivityOfPhotoId() {
		return 0; // This activity has nothing to do with photo
	}

	public Document getActivityXML() {
		final Document document = getEmptyDocument();

		final Element rootElement = document.addElement( ACTIVITY_XML_TAG_ROOT );
		rootElement.addElement( ACTIVITY_XML_TAG_USER_ID ).addText( String.valueOf( activityOfUser.getId() ) );

		return document;
	}

	public String getActivityXMLFormatted() {

		return getActivityXML().asXML();

		/*final Document document = getActivityXML();

		try {
			final OutputFormat format = OutputFormat.createPrettyPrint();
			final Writer writer = new StringWriter();
			final XMLWriter output = new XMLWriter( writer, format );
			output.write( document );
			output.close();
		} catch ( IOException e ) {
			log.error( String.format( "Can not format XML: %s", document.asXML() ), e );
		}

		throw new BaseRuntimeException( "TEMP" );*/


		/*final Transformer transformer;
		try {
			transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty( OutputKeys.INDENT, "yes" );

		} catch ( final TransformerConfigurationException e ) {
			log.error( "Can not format XML", e );
		}*/
	}

	protected String getPhotoIcon( final Photo photo ) {

		if( photo == null || ! services.getPhotoService().exists( photo.getId() ) ) {
			final String title = services.getTranslatorService().translate( "AbstractActivityStreamEntry: the photo is deleted", EnvironmentContext.getLanguage() );
			return String.format( "<img src='%s/entryNotFound.png' height='50' title='%s'/>", services.getUrlUtilsService().getSiteImagesPath(), title );
		}

		return String.format( "<a href='%1$s'><img src='%2$s' height='50' alt='%3$s' title='%3$s'/></a>"
			, services.getUrlUtilsService().getPhotoCardLink( photo.getId() )
			, services.getUserPhotoFilePathUtilsService().getPhotoUrl( photo )
			, photo.getNameEscaped()
		);
	}
}
