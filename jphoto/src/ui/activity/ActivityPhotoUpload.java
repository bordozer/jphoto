package ui.activity;

import core.general.configuration.ConfigurationKey;
import core.general.photo.Photo;
import core.services.system.Services;
import core.services.translator.message.TranslatableMessage;
import org.dom4j.Document;
import ui.context.EnvironmentContext;


public class ActivityPhotoUpload extends AbstractPhotoActivityStreamEntry {

	public ActivityPhotoUpload( final Photo photo, final Services services ) {
		super( services.getUserService().load( photo.getUserId() ), photo, photo.getUploadTime(), ActivityType.PHOTO_UPLOAD, services );

		initActivityTranslatableText();
	}

	@Override
	public Document getActivityXML() {
		return getEmptyDocument();
	}

	@Override
	protected TranslatableMessage getActivityTranslatableText() {
		return new TranslatableMessage( "activity stream entry: uploaded photo $1", services ).addPhotoCardLinkParameter( activityOfPhoto );
	}

	public TranslatableMessage getDisplayActivityUserLink() {
		final TranslatableMessage translatableMessage = new TranslatableMessage( services );

		if ( services.getSecurityService().isPhotoAuthorNameMustBeHidden( activityOfPhoto, EnvironmentContext.getCurrentUser() ) ) {
			return translatableMessage.string( services.getConfigurationService().getString( ConfigurationKey.PHOTO_UPLOAD_ANONYMOUS_NAME ) );
		}

		return translatableMessage.addUserCardLinkParameter( activityOfPhoto.getUserId() );
	}

	@Override
	public int getDisplayActivityUserId() {
		// there is acceptable to use EnvironmentContext.getCurrentUser() because this is UI called method
		return services.getSecurityService().isPhotoAuthorNameMustBeHidden( activityOfPhoto, EnvironmentContext.getCurrentUser() ) ? 0 : activityOfUser.getId();
	}

	@Override
	public String getDisplayActivityIcon() {
		return getPhotoIcon( activityOfPhoto );
	}

	@Override
	public String toString() {
		return String.format( "%s: %s", getActivityType(), activityOfPhoto );
	}
}
