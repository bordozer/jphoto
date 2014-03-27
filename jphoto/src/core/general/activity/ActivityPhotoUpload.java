package core.general.activity;

import core.context.EnvironmentContext;
import core.general.configuration.ConfigurationKey;
import core.general.photo.Photo;
import core.services.security.Services;
import core.services.translator.Language;
import org.dom4j.Document;

public class ActivityPhotoUpload extends AbstractPhotoActivityStreamEntry {

	public ActivityPhotoUpload( final Photo photo, final Services services ) {
		super( services.getUserService().load( photo.getUserId() ), photo, photo.getUploadTime(), ActivityType.PHOTO_UPLOAD, services );
	}

	@Override
	public Document getActivityXML() {
		return getEmptyDocument();
	}

	@Override
	public String getDisplayActivityDescription() {
		final Language language = getCurrentUserLanguage();
		return services.getTranslatorService().translate( "uploaded photo $1", language, services.getEntityLinkUtilsService().getPhotoCardLink( activityOfPhoto, language ) );
	}

	public String getDisplayActivityUserLink() {

		// there is acceptable to use EnvironmentContext.getCurrentUser() because this is UI called method
		if ( services.getSecurityService().isPhotoAuthorNameMustBeHidden( activityOfPhoto, EnvironmentContext.getCurrentUser() ) ) {
			return services.getConfigurationService().getString( ConfigurationKey.PHOTO_UPLOAD_ANONYMOUS_NAME );
		}

		return services.getEntityLinkUtilsService().getUserCardLink( services.getUserService().load( activityOfPhoto.getUserId() ) );
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
